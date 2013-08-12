package com.tog.framework.render.texture;

import org.lwjgl.BufferUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Hashtable;

import static org.lwjgl.opengl.GL11.*;

public class TextureLoader {

    private static final ColorModel glAlphaColorModel;
    private static final ColorModel glColorModel;
    private static final IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(1);

    static {

        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,8},
                true,
                false,
                ComponentColorModel.TRANSLUCENT,
                DataBuffer.TYPE_BYTE);

        glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,0},
                false,
                false,
                ComponentColorModel.OPAQUE,
                DataBuffer.TYPE_BYTE);
    }

    /**
     * Load a texture into OpenGL from a image reference on
     * disk.
     *
     * @param resourceName The location of the resource to load
     * @param target The GL target to load the texture against
     * @param dstPixelFormat The pixel format of the screen
     * @param minFilter The minimising filter
     * @param magFilter The magnification filter
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    static Texture getTexture(String resourceName,
                              int target,
                              int dstPixelFormat,
                              int minFilter,
                              int magFilter) throws IOException {
        int srcPixelFormat;

        // create the texture ID for this texture
        int textureID = createTextureID();
        Texture texture = new Texture(target,textureID);

        // bind this texture
        glBindTexture(target, textureID);

        BufferedImage bufferedImage = loadImage(resourceName);
        texture.setWidth(bufferedImage.getWidth());
        texture.setHeight(bufferedImage.getHeight());

        if (bufferedImage.getColorModel().hasAlpha()) {
            srcPixelFormat = GL_RGBA;
        } else {
            srcPixelFormat = GL_RGB;
        }

        // convert that image into a byte buffer of texture data
        ByteBuffer textureBuffer = convertImageData(bufferedImage,texture);

        if (target == GL_TEXTURE_2D) {
            glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
            glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);
        }

        // produce a texture from the byte buffer
        glTexImage2D(target,
                0,
                dstPixelFormat,
                get2Fold(bufferedImage.getWidth()),
                get2Fold(bufferedImage.getHeight()),
                0,
                srcPixelFormat,
                GL_UNSIGNED_BYTE,
                textureBuffer );

        return texture;
    }

    static void disposeTexture(Texture t) {
	glDeleteTextures(t.textureId);
    }

    static Texture convertToTexture(BufferedImage bufferedImage,
		   int target,
		   int dstPixelFormat,
		   int minFilter,
		   int magFilter) {
	int srcPixelFormat;

	// create the texture ID for this texture
	int textureID = createTextureID();
	Texture texture = new Texture(target,textureID);

	// bind this texture
	glBindTexture(target, textureID);

	texture.setWidth(bufferedImage.getWidth());
	texture.setHeight(bufferedImage.getHeight());

	if (bufferedImage.getColorModel().hasAlpha()) {
	    srcPixelFormat = GL_RGBA;
	} else {
	    srcPixelFormat = GL_RGB;
	}

	// convert that image into a byte buffer of texture data
	ByteBuffer textureBuffer = convertImageData(bufferedImage,texture);

	if (target == GL_TEXTURE_2D) {
	    glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
	    glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);
	}

	// produce a texture from the byte buffer
	glTexImage2D(target,
		0,
		dstPixelFormat,
		get2Fold(bufferedImage.getWidth()),
		get2Fold(bufferedImage.getHeight()),
		0,
		srcPixelFormat,
		GL_UNSIGNED_BYTE,
		textureBuffer );

	return texture;
    }

    private static int createTextureID() {
        glGenTextures(textureIDBuffer);
        return textureIDBuffer.get(0);
    }


    /**
     * Get the closest greater power of 2 to the fold number
     *
     * @param fold The target number
     * @return The power of 2
     */
    private static int get2Fold(int fold) {
        int ret = 2;
        while (ret < fold) {
            ret *= 2;
        }
        return ret;
    }

    /**
     * Convert the buffered image to a texture
     *
     * @param bufferedImage The image to convert to a texture
     * @param texture The texture to store the data into
     * @return A buffer containing the data
     */
    private static ByteBuffer convertImageData(BufferedImage bufferedImage,Texture texture) {
        ByteBuffer imageBuffer;
        WritableRaster raster;
        BufferedImage texImage;

        int texWidth = bufferedImage.getWidth();
        int texHeight = bufferedImage.getHeight();

        /*while (texWidth < bufferedImage.getWidth()) {
            texWidth *= 2;
        }
        while (texHeight < bufferedImage.getHeight()) {
            texHeight *= 2;
        }*/

        texture.setTextureHeight(texHeight);
        texture.setTextureWidth(texWidth);

        // create a raster that can be used by OpenGL as a source
        // for a texture
        if (bufferedImage.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
            texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
            texImage = new BufferedImage(glColorModel,raster,false,new Hashtable());
        }

        // copy the source image into the produced image
        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f,0f,0f,0f));
        g.fillRect(0,0,texWidth,texHeight);
        g.drawImage(bufferedImage,0,0,null);

        // build a byte buffer from the temporary image
        // that be used by OpenGL to produce a texture.
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();

        return imageBuffer;
    }

    /**
     * Load a given resource as a buffered image
     *
     * @param ref The location of the resource to load
     * @return The loaded buffered image
     * @throws IOException Indicates a failure to find a resource
     */
    private static BufferedImage loadImage(String ref) throws IOException {
        URL url = TextureLoader.class.getClassLoader().getResource(ref);

        if (url == null) {
            throw new IOException("Cannot find: " + ref);
        }

        // due to an issue with ImageIO and mixed signed code
        // we are now using good oldfashioned ImageIcon to load
        // images and the paint it on top of a new BufferedImage
        Image img = new ImageIcon(url).getImage();
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return bufferedImage;
    }
}
