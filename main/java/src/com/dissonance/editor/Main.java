package com.dissonance.editor;

import com.dissonance.editor.quest.MainQuest;
import com.dissonance.framework.game.GameService;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.GameSettings;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        GameService.loadEssentials(args);
        GameSettings.Display.fullscreen = false;
        System.out.println("Starting MainQuest");
        GameSettings.Graphics.useFBO = false;
        //AJ IS A POOP
        RenderService.ZOOM_SCALE = 0.5f;
        GameService.beginQuest(new MainQuest());
    }

    /*
    private static void setDefaultHighlightInfo() {
        //run this if you mess up your highlight.dat cause i just messed up mine (badpokerface)
        String classes = "Farrand|Wyatt|DemoLevelWorldLoader|World|BehaviorOffsetFollow|Position|System|String|Exception|" +
                "ArrayList|HashMap|Object|Thread|Class|Date|Math|AbstractWaypointSprite|AnimatedSprite|Behavior|" +
                "CombatSprite|Dialog|Enemy|NPCSprite|PhysicsSprite|PlayableSprite|Sound|Sprite|Weapon|WeaponItem";

        String interfaces = "Drawable|List|Behavior|WaypointSprite";

        String methods = "getFacingDirection|setFacingDirection|getWorld|setBehavior|setWorld|getX|setX|getY|setY|getPosition|" +
                "setPos|getWidth|getHeight|addDrawable|addSprite|removeSprite|loadAndAdd|onLoad";

        try (DataOutputStream stream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(Highlighter.DATA, false)))) {
            stream.writeUTF(classes);
            stream.writeUTF(interfaces);
            stream.writeUTF(methods);
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }
    */
}
