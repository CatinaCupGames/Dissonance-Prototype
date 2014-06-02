package com.dissonance.framework.game.sprites.animation;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Validator;

import java.util.ArrayList;

public class AnimationFactory {

    private static final ArrayList<AnimatorData> animate = new ArrayList<AnimatorData>();

    public static int queueAnimator(Animator animator) {
        Validator.validateNotNull(animator, "animator");

        int indexOf;
        AnimatorData ad = new AnimatorData();
        ad.animator = animator;
        synchronized (animate) {
            if (animate.contains(ad))
                return animate.get(animate.indexOf(ad)).ID;
            indexOf = animate.size() - 1;
            ad.ID = indexOf;
            animate.add(ad);
        }
        return indexOf;
    }

    public static boolean removeAnimator(int index) {
        try {
            return animate.remove(getData(index));
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    public static void resetAnimator(int index) {
        synchronized (animate) {
            AnimatorData ad = getData(index);
            if (ad == null)
                return;
            ad.last_tick = System.currentTimeMillis();
        }
    }

    private static AnimatorData getData(int index) {
        for (AnimatorData a : animate) {
            if (a.ID == index)
                return a;
        }
        return null;
    }

    public static void executeTick() throws IllegalAccessException {
        if (Thread.currentThread().getId() != RenderService.RENDER_THREAD_ID)
            throw new IllegalAccessException("executeTick() can only be called by the RenderService thread!");

        if (GameService.getCurrentQuest() != null && GameService.getCurrentQuest().isPaused())
            return;

        synchronized (animate) {
            AnimatorData[] animatorDatas = animate.toArray(new AnimatorData[animate.size()]);
            for (AnimatorData ad : animatorDatas) {
                if (ad.animator.getWorld() != RenderService.INSTANCE.getCurrentDrawingWorld())
                    continue;
                long time = System.currentTimeMillis() - ad.last_tick;
                if (time >= ad.animator.getAnimationSpeed()) {
                    ad.animator.onAnimate();
                    ad.current_frame_number++;
                    if (ad.current_frame_number >= ad.animator.getFrameCount()) {
                        ad.current_frame_number = 0;
                        ad.animator.wakeUp();
                    }
                    ad.last_tick = System.currentTimeMillis();
                }
            }
        }
    }

    private static class AnimatorData {
        public Animator animator;
        public long last_tick;
        public int current_frame_number;
        public int ID;

        public AnimatorData() {
            last_tick = System.currentTimeMillis();
        }

        @Override
        public int hashCode() {
            return animator.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof AnimatorData) {
                AnimatorData ad = (AnimatorData) obj;
                return ad.animator.equals(animator);
            } else if (obj instanceof Animator) {
                Animator a = (Animator) obj;
                return a.equals(animator);
            } else {
                return false;
            }
        }
    }
}
