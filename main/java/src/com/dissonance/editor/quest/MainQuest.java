package com.dissonance.editor.quest;

import com.dissonance.editor.system.CharSequenceCompiler;
import com.dissonance.editor.system.CharSequenceCompilerException;
import com.dissonance.editor.ui.EditorUI;
import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.UIElement;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.framework.system.utils.Direction;
import org.lwjgl.util.vector.Vector2f;

import javax.swing.*;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MainQuest extends AbstractQuest {
    public static MainQuest INSTANCE;
    public static ClassLoader loader = URLClassLoader.newInstance(new URL[] {}, MainQuest.class.getClassLoader());
    public static int SPEED = 5;
    private final CharSequenceCompiler<WorldLoader> stringCompiler = new CharSequenceCompiler<WorldLoader>(loader, Arrays.asList("-target", "1.7"));
    private ArrayList<Drawable> sprites = new ArrayList<Drawable>();
    private Drawable selectedSprite;
    public String mapName;
    private boolean adding = false;
    public boolean customCode = false;
    @Override
    public void startQuest() throws Exception {
        INSTANCE = this;
        System.out.println("Displaying Editor UI");
        try {
            EditorUI.displayForm();
        } catch (RuntimeException e) {
            e.printStackTrace();
            RenderService.kill();
            return;
        }
        mapName = JOptionPane.showInputDialog(EditorUI.FRAME, "Please enter the map name to create a World Loader for", "World Loader Creator", JOptionPane.PLAIN_MESSAGE);
        World world = WorldFactory.getWorld(mapName);
        setWorld(world);
        RenderService.INSTANCE.runOnServiceThread(updateThread, true, true);
    }

    public String generateLoaderCode() {
        if (!customCode && EditorUI.INSTANCE.codeTextArea.getText().isEmpty()) customCode = false;
        if (!customCode) {
            StringBuilder builder = new StringBuilder();
            builder.append("package com.dissonance.game.w;\n").append("\n");
            builder.append("import com.dissonance.framework.game.world.World;\n");
            ArrayList<String> temp = new ArrayList<String>();
            for (Drawable sprite : sprites) {
                if (temp.contains(sprite.getClass().getCanonicalName()))
                    continue;
                temp.add(sprite.getClass().getCanonicalName());
                builder.append("import ").append(sprite.getClass().getCanonicalName()).append(";\n");
            }
            builder.append("\n\n").append("public class ").append(mapName).append(" extends GameWorldLoader {\n");
            builder.append("    @Override\n").append("    public void onLoad(World w) {\n").append("        super.onLoad(w);\n");
            int i = 0;
            for (Drawable sprite : sprites) {
                i++;
                builder.append("\n");
                builder.append("        ").append(sprite.getClass().getSimpleName()).append(" var").append(i).append(" = new ").append(sprite.getClass().getSimpleName()).append("();\n");
                builder.append("        w.loadAndAdd(var").append(i).append(");\n");
                builder.append("        var").append(i).append(".setX(").append(sprite.getX()).append("f);\n");
                builder.append("        var").append(i).append(".setY(").append(sprite.getY()).append("f);\n");
            }
            builder.append("    }\n").append("}");

            return builder.toString();
        } else {
            if (getSpriteCount() != sprites.size()) {
                EditorUI.FRAME.requestFocus();
                JOptionPane.showMessageDialog(EditorUI.FRAME, "The Sprite list seems to be out of date!\nCompile the World Loader code and try again.", "Error moving Sprite", JOptionPane.WARNING_MESSAGE);
                EditorUI.INSTANCE.setComboIndex(0);
                return EditorUI.INSTANCE.codeTextArea.getText();
            }
            String code = EditorUI.INSTANCE.codeTextArea.getText();
            if (selectedSprite == null) return code;
            String varName = getVarNameFor(sprites.indexOf(selectedSprite));
            if (varName.equalsIgnoreCase("???") && adding) { //Assume we need to add it.
                code = code.substring(0, code.lastIndexOf("}")); //Get rid of } closing onLoad
                code = code.substring(0, code.lastIndexOf("}")); //Get rid of } closing class
                code += "\n        " + selectedSprite.getClass().getSimpleName() + " var" + sprites.size() + " = new " + selectedSprite.getClass().getSimpleName() + "();\n" +
                        "        w.loadAndAdd(var" + sprites.size() + ");\n" +
                        "        var" + sprites.size() + ".setX(" + selectedSprite.getX() + "f);\n" +
                        "        var" + sprites.size() + ".setY(" + selectedSprite.getY() + "f);\n" +
                        "    }\n" +
                        "}";
                return code;
            } else if (code.contains(varName + ".setX") && code.contains(varName + ".setY") && (selectedSprite instanceof Sprite || selectedSprite instanceof UIElement)) {
                String[] lines = code.split("\n");
                String newCode = "";
                for (String line : lines) {
                    String s = line;
                    if (s.contains(varName + ".setX")) {
                        s = "        " + varName + ".setX(" + selectedSprite.getX() + "f);";
                    } else if (s.contains(varName + ".setY")) {
                        s = "        " + varName + ".setY(" + selectedSprite.getY() + "f);";
                    }
                    newCode += s + "\n";
                }
                return newCode;
            } else if (selectedSprite instanceof Sprite || selectedSprite instanceof UIElement) {
                boolean addX = !code.contains(varName + ".setX");
                boolean addY = !code.contains(varName + ".setY");
                String[] lines = code.split("\n");
                String newCode = "";
                int toAdd = 0;
                toAdd += addX ? 1 : 0;
                toAdd += addY ? 1 : 0;
                int index = getLineNumberFor(sprites.indexOf(selectedSprite));
                if (index == -1) {
                    EditorUI.FRAME.requestFocus();
                    JOptionPane.showMessageDialog(EditorUI.FRAME, "The Sprite list seems to be out of date!\nCompile the World Loader code and try again.", "Error moving Sprite", JOptionPane.WARNING_MESSAGE);
                    EditorUI.INSTANCE.setComboIndex(0);
                    return code;
                }
                for (int i = 0; i < lines.length + toAdd; i++) {
                    if (i <= index) {
                        newCode += lines[i] + "\n";
                    } else if (i > index && i - 1 == index) {
                        if (addX && addY) {
                            newCode += "        " + varName + ".setX(" + selectedSprite.getX() + "f);\n        " + varName + ".setY(" + selectedSprite.getY() + "f);";
                        } else if (addX) {
                            newCode += "        " + varName + ".setX(" + selectedSprite.getX() + "f);\n";
                        } else if (addY) {
                            newCode +=  "         " + varName + ".setY(" + selectedSprite.getY() + "f);\n";
                        }
                    } else {
                        newCode += lines[i - toAdd] + "\n";
                    }
                }

                return newCode;
            } else {
                return code;
            }
        }
    }

    public boolean checkBeforeCompile(String javaCode) {
        String[] lines = javaCode.split("\n");
        ArrayList<String> sprites = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            String s = lines[i];
            if (s.contains("loadAndAdd")) {
                String varName = s.trim().replace("w.loadAndAdd(", "").replace(" ", "").replace(");", "");
                if (!sprites.contains(varName))
                    sprites.add(varName);
                else {
                    Object[] options = { "Yes, continue compile", "No, abort compile" };
                    int n = JOptionPane.showOptionDialog(EditorUI.FRAME, "The compiler has detected that you are adding a Sprite to the World more than once\nLine: " + i + " ('" + s.trim() + "')\nThis can cause errors in the Editor, do you want to continue?", "Compiler Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                    if (n == 1) return false;
                }
            }
            if (s.contains("loadAnimatedTextureForSprite") || s.contains("addSprite")) {
                Object[] options = { "Yes, continue compile", "No, abort compile" };
                int n = JOptionPane.showOptionDialog(EditorUI.FRAME, "The compiler has detected that you are using the deprecated method '" + s.trim() + "'\nIt is recommended that you instead use 'loadAndAdd(Sprite)'\nLine: " + i + " ('" + s.trim() + "')\nThis can cause errors in the Editor, do you want to continue?", "Compiler Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                if (n == 1) return false;
            }
        }

        return true;
    }

    public int getSpriteCount() {
        String code = EditorUI.INSTANCE.codeTextArea.getText();
        int count = 0;
        String[] lines = code.split("\n");
        for (String s : lines) {
            if (s.contains("loadAndAdd") || s.contains("displayUI") || s.contains("addSprite")) {
                count++;
            }
        }
        return count;
    }

    public int getLineNumberFor(int target) {
        String code = EditorUI.INSTANCE.codeTextArea.getText();
        String[] lines = code.split("\n");
        int i = 0;
        for (int ii = 0; ii < lines.length; ii++) {
            String s = lines[ii];
            if (s.contains("loadAndAdd") || s.contains("displayUI") || s.contains("addSprite")) {
                if (target == i) {
                    return ii;
                } else {
                    i++;
                }
            }
        }
        return -1;
    }

    public String getVarNameFor(int target) {
        String code = EditorUI.INSTANCE.codeTextArea.getText();
        String varName;
        String[] lines = code.split("\n");
        int i = 0;
        for (String s : lines) {
            if (s.contains("loadAndAdd")) {
                if (target == i) {
                    varName = s.trim().split("\\.")[1].replace("loadAndAdd(", "").replace(" ", "").replace(");", "");
                    return varName;
                } else {
                    i++;
                }
            } else if (s.contains("displayUI")) {
                if (target == i) {
                    varName = s.trim().split("\\.")[0].replace(" ", "");
                    return varName;
                } else {
                    i++;
                }
            } else if (s.contains("addSprite")) {
                if (target == i) {
                    varName = s.trim().split("\\.")[1].replace("addSprite(", "").replace(" ", "").replace(");", "");
                    return varName;
                } else {
                    i++;
                }
            }
        }
        return "???";
    }

    public void newSprite() {
        String class_ = (String) JOptionPane.showInputDialog(EditorUI.FRAME, "Please enter name of the Sprite class\nor the complete classpath for the Sprite class.", "Add Sprite", JOptionPane.PLAIN_MESSAGE);
        if ((class_ != null) && (class_.length() > 0)) {
            Sprite sprite;
            try {
                sprite = Sprite.fromClass(Class.forName(class_));
            } catch (ClassNotFoundException e) {
                class_ = "com.dissonance.game.sprites." + class_;
                try {
                    sprite = Sprite.fromClass(Class.forName(class_));
                } catch (ClassNotFoundException e1) {
                    EditorUI.FRAME.requestFocus();
                    JOptionPane.showMessageDialog(EditorUI.FRAME, "The specified Sprite could not be found", "Error adding Sprite", JOptionPane.WARNING_MESSAGE);
                    return;
                } catch (RuntimeException e1) {
                    EditorUI.FRAME.requestFocus();
                    JOptionPane.showMessageDialog(EditorUI.FRAME, "The specified Sprite throw an error!\nCheck the log for more info!", "Error adding Sprite", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                EditorUI.FRAME.requestFocus();
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
            adding = true;
            EditorUI.INSTANCE.refreshCode();
            adding = false;
            if (selectedSprite instanceof Sprite)
                Camera.followSprite((Sprite)selectedSprite);
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

    private int compileCount;
    public boolean compileAndShow(String javaCode) {
        if (!checkBeforeCompile(javaCode)) return false;
        final DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();
        try {
            javaCode = javaCode.replace("public class " + mapName, "public class " + mapName + compileCount);
            Class<WorldLoader> worldLoaderClass = stringCompiler.compile("com.dissonance.game.w." + mapName + compileCount, javaCode, errs, WorldLoader.class);
            compileCount++;
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
                                if (updatableDrawable != PlayableSprite.getCurrentlyPlayingSprite()) {
                                    sprites.add(updatableDrawable);
                                }
                                selectedSprite = null;
                                EditorUI.INSTANCE.clearComboBox();
                                EditorUI.INSTANCE.setComboBox(sprites);
                                EditorUI.INSTANCE.setComboIndex(0);
                                if (PlayableSprite.getCurrentlyPlayingSprite() != null) {
                                    getWorld().removeSprite(PlayableSprite.getCurrentlyPlayingSprite()); //Remove player
                                }
                            }
                        }
                    }, true);
                }
            });
            return true;
        } catch (CharSequenceCompilerException e) {
            EditorUI.FRAME.requestFocus();
            JOptionPane.showMessageDialog(EditorUI.FRAME, "Compilation failed. Check the console for more details.", "Error compiling", JOptionPane.ERROR_MESSAGE);
            log(errs);
        } catch (InstantiationException e) {
            EditorUI.FRAME.requestFocus();
            JOptionPane.showMessageDialog(EditorUI.FRAME, "Compilation failed. The World Loader compiled does not have a default constructor!", "Error loading", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            EditorUI.FRAME.requestFocus();
            JOptionPane.showMessageDialog(EditorUI.FRAME, "Compilation failed. The World Loader compiled is private!\nCheck the console for more details.", "Error Loading", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }

    public void selectSprite(int index) {
        if (index < 0 || index >= sprites.size()) {
            selectedSprite = null;
            return;
        }
        selectedSprite = sprites.get(index);
        Camera.setPos(Camera.translateToCameraCenter(new Vector2f(selectedSprite.getX(), selectedSprite.getY()), 32));
        if (selectedSprite instanceof Sprite)
            Camera.followSprite((Sprite)selectedSprite);
    }

    private boolean tip = false;
    private void update() {
        boolean w = InputKeys.isButtonPressed(InputKeys.MOVEUP);
        boolean d = InputKeys.isButtonPressed(InputKeys.MOVERIGHT);
        boolean s = InputKeys.isButtonPressed(InputKeys.MOVEDOWN);
        boolean a = InputKeys.isButtonPressed(InputKeys.MOVELEFT);
        if ((w || a || s || d) && selectedSprite != null && selectedSprite instanceof Sprite) {
            Sprite ss = (Sprite)selectedSprite;

            if (w) {
                ss.setY(selectedSprite.getY() - (SPEED * RenderService.TIME_DELTA));
                ss.setFacing(Direction.UP);
                EditorUI.INSTANCE.refreshCode();
            }
            if (s) {
                ss.setY(selectedSprite.getY() + (SPEED * RenderService.TIME_DELTA));
                ss.setFacing(Direction.DOWN);
                EditorUI.INSTANCE.refreshCode();
            }
            if (a) {
                ss.setX(selectedSprite.getX() - (SPEED * RenderService.TIME_DELTA));
                ss.setFacing(Direction.LEFT);
                EditorUI.INSTANCE.refreshCode();
            }
            if (d) {
                ss.setX(selectedSprite.getX() + (SPEED * RenderService.TIME_DELTA));
                ss.setFacing(Direction.RIGHT);
                EditorUI.INSTANCE.refreshCode();
            }
        } else if ((w || a || s || d) && selectedSprite != null && selectedSprite instanceof UIElement) {
            UIElement ss = (UIElement)selectedSprite;
            if (w) {
                ss.setY(selectedSprite.getY() - (SPEED * RenderService.TIME_DELTA));
                EditorUI.INSTANCE.refreshCode();
            }
            if (s) {
                ss.setY(selectedSprite.getY() + (SPEED * RenderService.TIME_DELTA));
                EditorUI.INSTANCE.refreshCode();
            }
            if (a) {
                ss.setX(selectedSprite.getX() - (SPEED * RenderService.TIME_DELTA));
                EditorUI.INSTANCE.refreshCode();
            }
            if (d) {
                ss.setX(selectedSprite.getX() + (SPEED * RenderService.TIME_DELTA));
                EditorUI.INSTANCE.refreshCode();
            }
        } else if (selectedSprite == null) {
            Camera.stopFollowing();
            if (!tip) {
                tip = w || a || s || d;
                if (tip) {
                    EditorUI.FRAME.requestFocus();
                    JOptionPane.showMessageDialog(EditorUI.FRAME, "No Sprite selected!\nTo move a Sprite, you must select it in the dropdown menu.", "Tip", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    @Override
    public void endQuest() throws IllegalAccessException {
        super.endQuest();
        EditorUI.FRAME.dispose();
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
