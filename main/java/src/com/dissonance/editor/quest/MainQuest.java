package com.dissonance.editor.quest;

import com.dissonance.editor.system.CharSequenceCompiler;
import com.dissonance.editor.system.CharSequenceCompilerException;
import com.dissonance.editor.ui.EditorUI;
import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.framework.system.utils.Direction;

import javax.swing.*;
import javax.tools.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MainQuest extends AbstractQuest {
    public static MainQuest INSTANCE;
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private Sprite selectedSprite;
    private String mapName;
    @Override
    public void startQuest() throws Exception {
        INSTANCE = this;
        System.out.println("Displaying Editor UI");
        EditorUI.displayForm();
        mapName = JOptionPane.showInputDialog(EditorUI.FRAME, "Please enter the map name to create a World Loader for", "World Loader Creator", JOptionPane.PLAIN_MESSAGE);
        World world = WorldFactory.getWorld(mapName);
        setWorld(world);
        RenderService.INSTANCE.runOnServiceThread(updateThread, true, true);
    }

    public String generateLoaderCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package com.dissonance.game.w;\n").append("\n");
        builder.append("import com.dissonance.framework.game.world.World;\n");
        for (Sprite sprite : sprites) {
            builder.append("import ").append(sprite.getClass().getCanonicalName()).append(";\n");
        }
        builder.append("\n\n").append("public class ").append(mapName).append(" extends GameWorldLoader {\n");
        builder.append("    @Override\n").append("    public void onLoad(World w) {\n").append("        super.onLoad(w);\n");
        int i = 0;
        for (Sprite sprite : sprites) {
            i++;
            builder.append("\n");
            builder.append("        ").append(sprite.getClass().getSimpleName()).append(" var").append(i).append(" = new ").append(sprite.getClass().getSimpleName()).append("();\n");
            builder.append("        w.loadAndAdd(var").append(i).append(");\n");
            builder.append("        var").append(i).append(".setX(").append(sprite.getX()).append("f);\n");
            builder.append("        var").append(i).append(".setY(").append(sprite.getY()).append("f);\n");
        }
        builder.append("    }\n").append("}");

        return builder.toString();
    }

    public void newSprite() {
        String class_ = (String) JOptionPane.showInputDialog(EditorUI.FRAME, "Please enter the complete classpath for the Sprite object.", "Add Sprite", JOptionPane.PLAIN_MESSAGE);
        if ((class_ != null) && (class_.length() > 0)) {
            Sprite sprite;
            try {
                sprite = Sprite.fromClass(Class.forName(class_));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(EditorUI.FRAME, "The specified Sprite could not be found", "Error adding Sprite", JOptionPane.WARNING_MESSAGE);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(EditorUI.FRAME, "Exception occurred: " + e.getMessage(), "Error adding Sprite", JOptionPane.ERROR_MESSAGE);
                return;
            }

            sprites.add(sprite);
            sprite.setX(0);
            sprite.setY(0);
            getWorld().loadAndAdd(sprite);
            if (PlayableSprite.getCurrentlyPlayingSprite() != null)
                PlayableSprite.getCurrentlyPlayingSprite().freeze();
            this.selectedSprite = sprite;
            EditorUI.INSTANCE.refreshCode();
            Camera.followSprite(selectedSprite);
            EditorUI.INSTANCE.clearComboBox();
            EditorUI.INSTANCE.setComboBox(sprites);
            EditorUI.INSTANCE.setComboIndex(sprites.size());
        }
    }

    private void log(final DiagnosticCollector<JavaFileObject> diagnostics) {
        final StringBuilder msgs = new StringBuilder();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics
                .getDiagnostics()) {
            msgs.append(diagnostic.getMessage(null)).append("\n");
        }
        System.out.println(msgs.toString());

    }

    private final CharSequenceCompiler<WorldLoader> stringCompiler = new CharSequenceCompiler<WorldLoader>(getClass().getClassLoader(), Arrays.asList("-target", "1.7"));
    public void compileAndShow(String javaCode) {
        final DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();
        try {
            Class<WorldLoader> worldLoaderClass = stringCompiler.compile("com.dissonance.game.w." + mapName, javaCode, errs, WorldLoader.class);
            final WorldLoader loader = worldLoaderClass.newInstance();
            log(errs);
            RenderService.INSTANCE.runOnServiceThread(new Runnable() {
                @Override
                public void run() {
                    getWorld().setWorldLoader(loader);
                    getWorld().onDispose();
                    getWorld().init();
                    try {
                        getWorld().load(mapName);
                        if (PlayableSprite.getCurrentlyPlayingSprite() != null)
                            PlayableSprite.getCurrentlyPlayingSprite().freeze();
                    } catch (WorldLoadFailedException e) {
                        e.printStackTrace();
                    }

                    RenderService.INSTANCE.runOnServiceThread(new Runnable() {
                        @Override
                        public void run() {
                            sprites.clear();
                            Iterator<UpdatableDrawable> ud = getWorld().getUpdatables();
                            while (ud.hasNext()) {
                                UpdatableDrawable updatableDrawable = ud.next();
                                if (updatableDrawable instanceof Sprite && updatableDrawable != PlayableSprite.getCurrentlyPlayingSprite()) {
                                    sprites.add((Sprite)updatableDrawable);
                                }
                                selectedSprite = null;
                                EditorUI.INSTANCE.setComboIndex(0);
                            }
                        }
                    }, true);
                }
            });
        } catch (CharSequenceCompilerException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void selectSprite(int index) {
        if (index < 0 || index >= sprites.size()) {
            selectedSprite = null;
            return;
        }
        selectedSprite = sprites.get(index);
        Camera.setPos(Camera.translateToCameraCenter(selectedSprite.getVector(), 32));
        Camera.followSprite(selectedSprite);
    }

    private void update() {
        if (selectedSprite != null) {
            boolean w = InputKeys.isButtonPressed(InputKeys.MOVEUP);
            boolean d = InputKeys.isButtonPressed(InputKeys.MOVERIGHT);
            boolean s = InputKeys.isButtonPressed(InputKeys.MOVEDOWN);
            boolean a = InputKeys.isButtonPressed(InputKeys.MOVELEFT);

            if (w) {
                selectedSprite.setY(selectedSprite.getY() - (5 * RenderService.TIME_DELTA));
                selectedSprite.setFacing(Direction.UP);
                EditorUI.INSTANCE.refreshCode();
            }
            if (s) {
                selectedSprite.setY(selectedSprite.getY() + (5 * RenderService.TIME_DELTA));
                selectedSprite.setFacing(Direction.DOWN);
                EditorUI.INSTANCE.refreshCode();
            }
            if (a) {
                selectedSprite.setX(selectedSprite.getX() - (5 * RenderService.TIME_DELTA));
                selectedSprite.setFacing(Direction.LEFT);
                EditorUI.INSTANCE.refreshCode();
            }
            if (d) {
                selectedSprite.setX(selectedSprite.getX() + (5 * RenderService.TIME_DELTA));
                selectedSprite.setFacing(Direction.RIGHT);
                EditorUI.INSTANCE.refreshCode();
            }
        } else {
            Camera.stopFollowing();
        }
    }



    private final Runnable updateThread = new Runnable() {

        @Override
        public void run() {
            MainQuest.this.update();
        }
    };

    @Override
    public String getName() {
        return "EditorQuest";
    }
}
