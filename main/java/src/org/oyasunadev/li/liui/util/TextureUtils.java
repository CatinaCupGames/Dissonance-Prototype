package org.oyasunadev.li.liui.util;

import com.dissonance.framework.render.texture.Texture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver
 * Date: 9/1/13
 * Time: 7:28 PM
 */

public class TextureUtils
{
	public static Texture getTextureFromColor(Color color) throws Exception
	{
		BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);

		for(int x1 = 0; x1 < image.getWidth(); x1++)
		{
			for(int y1 = 0; y1 < image.getHeight(); y1++)
			{
				image.setRGB(x1, y1, color.getRGB());
			}
		}

        return Texture.convertToTexture("coloredTexture", image);

		/*ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(image, "png", output);

		return TextureLoader.getTexture("PNG", new ByteArrayInputStream(output.toByteArray()));*/
	}
}
