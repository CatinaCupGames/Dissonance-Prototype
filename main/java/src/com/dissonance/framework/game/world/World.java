package com.dissonance.framework.game.world;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.sprites.AnimatedSprite;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.Service;
import com.dissonance.framework.system.ServiceManager;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.framework.system.utils.Validator;
import org.jbox2d.common.Vec2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;

public final class World implements Drawable {
    private static final float GRAVITY = 9.81f;
    private static final float TIME_STEP = 1f / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;

    private transient final ArrayList<Drawable> drawable = new ArrayList<>();
    private transient org.jbox2d.dynamics.World physicsWorld;
    private String name;
    private NodeMap node_map;
    private int ID;
    private transient Service renderingService;
    private transient Texture texture;
    private boolean invalid = true;

    World(int ID) {
        this.ID = ID;
        physicsWorld = new org.jbox2d.dynamics.World(new Vec2(0, GRAVITY));
    }

    public int getID() {
        return ID;
    }

    public void init() {
        renderingService = ServiceManager.createService(RenderService.class);
    }

    public void switchTo() {
        //TODO Move all playable sprites to this world maybe?
        if (renderingService == null)
            return;
        renderingService.provideData(this, RenderService.WORLD_DATA_TYPE);
    }

    @Override
    public void update() {
        if(this.physicsWorld != null) {
            this.physicsWorld.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    @Override
    public void render() {
        if (texture == null)
            return;
        texture.bind();
        float bx = texture.getTextureWidth() / 2;
        float by = texture.getTextureHeight() / 2;
        final float x = 0, y = 0;
        //glColor3f(1f, .5f, .5f); DEBUG LINE FOR TEXTURES
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex2f(x - bx, y - by);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex2f(x + bx, y - by);
        glTexCoord2f(1f, 1f); //top right
        glVertex2f(x + bx, y + by);
        glTexCoord2f(0f, 1f); //top left
        glVertex2f(x - bx, y + by);
        glEnd();
        texture.unbind();
    }

    public void load(final String world) throws WorldLoadFailedException {
        if (renderingService == null)
            throw new WorldLoadFailedException("The RenderService was not created! Try calling World.init() before loading a world.");

        int width;
        int height;
        ArrayList<Block> blocks = new ArrayList<Block>();
        InputStream in = texture.getClass().getClassLoader().getResourceAsStream("worlds/" + world + ".xml");
        if (in != null) {
            try {
                DocumentBuilder db = SpriteTexture.DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
                Document dom = db.parse(in);
                Element elm = dom.getDocumentElement();
                NodeList nl = elm.getElementsByTagName("level");
                if (nl != null && nl.getLength() > 0) {
                    Element level = (Element) nl.item(0);

                    NodeList temp = level.getElementsByTagName("width");
                    if (temp != null && temp.getLength() > 0) {
                        width = Integer.parseInt(temp.item(0).getFirstChild().getNodeValue());
                    }

                    temp = level.getElementsByTagName("height");
                    if (temp != null && temp.getLength() > 0) {
                        height = Integer.parseInt(temp.item(0).getFirstChild().getNodeValue());
                    }

                    temp = level.getElementsByTagName("blocks");
                    if (temp != null && temp.getLength() > 0) {
                        Element block_element = (Element)temp.item(0);
                        NodeList block_types = block_element.getChildNodes();
                        if (block_types != null && block_types.getLength() > 0) {
                            for (int i = 0; i < block_types.getLength(); i++) {
                                Element b = (Element)block_types.item(0);
                                String namespace = b.getTagName();
                                Block.CollisionType collisionType;
                                int zpos = 0;
                                int x = 0;
                                int y = 0;
                                String name = "air";
                                HashMap<String, String> extra = new HashMap<String, String>();

                                temp = b.getElementsByTagName("tilecollision");
                                if (temp != null && temp.getLength() > 0) {
                                    collisionType = Block.CollisionType.fromInt(Integer.parseInt(temp.item(0).getFirstChild().getNodeValue()));
                                } else {
                                    collisionType = Block.CollisionType.PASSABLE; //Assume passable
                                }

                                temp = b.getElementsByTagName("zposition");
                                if (temp != null && temp.getLength() > 0) {
                                    zpos = Integer.parseInt(temp.item(0).getFirstChild().getNodeValue());
                                }

                                temp = b.getElementsByTagName("x");
                                if (temp != null && temp.getLength() > 0) {
                                    x = Integer.parseInt(temp.item(0).getFirstChild().getNodeValue());
                                }

                                temp = b.getElementsByTagName("y");
                                if (temp != null && temp.getLength() > 0) {
                                    y = Integer.parseInt(temp.item(0).getFirstChild().getNodeValue());
                                }

                                temp = b.getElementsByTagName("name");
                                if (temp != null && temp.getLength() > 0) {
                                    name = temp.item(0).getFirstChild().getNodeValue();
                                }

                                temp = b.getElementsByTagName("extras");
                                if (temp != null && temp.getLength() > 0) {
                                    NodeList extras = b.getChildNodes();
                                    if (extras != null && extras.getLength() > 0) {
                                        for (int ii = 0; i < extras.getLength(); ii++) {
                                            Element e = (Element) extras.item(ii);
                                            String key = e.getNodeName();
                                            String value = e.getNodeValue();
                                            extra.put(key, value);
                                        }
                                    }
                                }

                                Block block = new Block(collisionType, zpos, x, y, name, extra);

                                //TODO Attempt to create NPC

                                blocks.add(block);
                            }
                        }
                    }
                }
            } catch (ParserConfigurationException e) {
                throw new WorldLoadFailedException("Failed to parse world file!", e);
            } catch (SAXException e) {
                throw new WorldLoadFailedException("Unknown exception!", e);
            } catch (IOException e) {
                throw new WorldLoadFailedException("Failed to load world file!", e);
            }
        }

        //===TEMP CODE===
        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                try {
                    World.this.texture = Texture.retriveTexture("worlds/" + world + "/" + world + ".png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //===TEMP CODE===

        addDrawable(this);

        if (renderingService.isPaused())
            renderingService.resume();
    }

    public Iterator<Drawable> getDrawable() {
        if (invalid) {
            Collections.sort(drawable);
            invalid = false;
        }
        return drawable.iterator();
    }

    public void invalidateDrawableList() {
        invalid = true;
    }

    private void addDrawable(final Drawable draw, final Runnable run) {
        if (renderingService == null)
            throw new IllegalStateException("init() has not been called on this world!");
        Validator.validateNotNull(draw, "sprite");

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                drawable.add(draw);
                if (!(draw instanceof World))
                    draw.init();
                if (run != null)
                    run.run();
            }
        }, true); //Force adding drawables on next frame
    }

    public void addDrawable(final Drawable draw) {
        addDrawable(draw, null);
    }

    public void addSprite(final Sprite sprite) {
        addDrawable(sprite, new Runnable() {
            @Override
            public void run() {
                sprite.setWorld(World.this);
                sprite.onLoad();
            }
        });
    }

    public void removeSprite(final Sprite sprite) {
        removeDrawable(sprite, new Runnable() {

            @Override
            public void run() {
                sprite.onUnload();
                sprite.setWorld(null);
            }
        });
    }

    public void onUnload() { //This method is called when the world is not shown but is still in memory
        //TODO Do stuff to save memory when this world is not shown
    }

    public void onDispose() {
        drawable.clear();
        texture.dispose();
        renderingService = null;
    }

    public void removeDrawable(final Drawable drawable) {
        removeDrawable(drawable, null);
    }

    public void removeDrawable(final Drawable drawable, final Runnable runnable) {
        if (renderingService == null)
            throw new IllegalStateException("init() has not been called on this world!");
        Validator.validateNotNull(drawable, "sprite");

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                World.this.drawable.remove(drawable);
                if (runnable != null)
                    runnable.run();
            }
        }, true);
    }

    public void loadTextureForSprite(final String resource, final Sprite sprite) {
        if (renderingService == null)
            throw new IllegalStateException("init() has not been called on this world!");
        Validator.validateNotNull(sprite, "sprite");
        Validator.validateNotNull(resource, "resource");

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                try {
                    Texture t = Texture.retriveTexture(resource);
                    sprite.setTexture(t);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadAnimatedTextureForSprite(final AnimatedSprite sprite) {
        if (renderingService == null)
            throw new IllegalStateException("init() has not been called on this world!");
        Validator.validateNotNull(sprite, "sprite");

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                try {
                    SpriteTexture t = SpriteTexture.retriveSpriteTexture(sprite.getSpriteName());
                    sprite.setTexture(t);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadAndAdd(Drawable d) {
        if (d instanceof Sprite) {
            if (d instanceof AnimatedSprite) {
                loadAnimatedTextureForSprite((AnimatedSprite) d);
            }
            addSprite((Sprite) d);
        } else {
            addDrawable(d);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeMap getNodeMap() {
        return node_map;
    }

    public void setNodeMap(NodeMap map) {
        this.node_map = map;
    }

    public org.jbox2d.dynamics.World getPhysicsWorld() {
        return this.physicsWorld;
    }

    public int compareTo(Drawable o) {
        return BEFORE;
    }
}
