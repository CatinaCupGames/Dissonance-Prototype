package com.tog.framework.game.sprites.animation;

import com.tog.framework.render.RenderService;
import com.tog.framework.system.utils.Validator;

import java.util.ArrayList;

public class AnimationFactory {

    private static final ArrayList<AnimatorData> animate = new ArrayList<AnimatorData>();

    public static int queueAnimator(Animator animator) {
        Validator.validateNotNull(animator, "animator");

        int indexOf;
        AnimatorData ad = new AnimatorData();
        ad.animator = animator;
        synchronized (animate) {
            if ((indexOf = animate.indexOf(ad)) != -1)
                return indexOf;
            animate.add(ad);
            indexOf = animate.size() - 1;
        }
        return indexOf;
    }

    public static boolean removeAnimator(int index) {
        try {
            animate.remove(index);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    public static boolean removeAnimator(Animator animator) {
        AnimatorData ad = new AnimatorData();
        ad.animator = animator;
        int index = animate.indexOf(ad);
        return index != -1 && removeAnimator(index);
    }

    public static void executeTick() throws IllegalAccessException {
        if (Thread.currentThread().getId() != RenderService.RENDER_THREAD_ID)
            throw new IllegalAccessException("executeTick() can only be called by the RenderService thread!");

        synchronized (animate) {
            for (AnimatorData ad : animate) {
                ad.ticks++;
                if (ad.ticks >= ad.animator.getSpeed()) {
                    ad.animator.onAnimate();
                    ad.current_frame_number++;
                    if (ad.current_frame_number >= ad.animator.getFrameCount()) {
                        ad.current_frame_number = 0;
                        ad.animator.wakeUp();
                    }
                    ad.ticks = 0;
                }
            }
        }
    }

    private static class AnimatorData {
        public Animator animator;
        public int ticks;
        public int current_frame_number;

        @Override
        public int hashCode() {
            return animator.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof AnimatorData) {
                AnimatorData ad = (AnimatorData)obj;
                return ad.animator.equals(animator);
            } else if (obj instanceof Animator) {
                Animator a = (Animator)obj;
                return a.equals(animator);
            } else {
                return false;
            }
        }
    }
}