package com.dissonance.game.scenes;

import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.w.AtWaldomar;
import org.lwjgl.util.vector.Vector2f;




public class AtWaldomarScene extends SimpleScene {


    @Override
    protected void playScene() throws Throwable {
        RenderService.INSTANCE.fadeFromBlack(3000);
        Vector2f center = Camera.translateToCameraCenter(AtWaldomar.farrand.getVector(), AtWaldomar.farrand.getHeight());
        Camera.setPos(center);

        //WaldomarsMeetingRoom.farrand.setWaypoint(0f, AtWaldomar.farrand.getY(), WaypointType.SIMPLE);
        //WaldomarsMeetingRoom.farrand.setMovementSpeed(3f);



        AtWaldomar.farrand.setMovementSpeed(3f);

        //Thread.sleep(50000);

    }
}
