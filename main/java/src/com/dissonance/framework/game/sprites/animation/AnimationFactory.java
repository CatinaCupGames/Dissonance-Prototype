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

    public static void resetAnimator(int index) {
        if (index < 0 || index >= animate.size())
            return;
        synchronized (animate) {
            AnimatorData ad = animate.get(index);
            ad.last_tick = System.currentTimeMillis();
        }
    }

    public static void executeTick() throws IllegalAccessException {
        if (Thread.currentThread().getId() != RenderService.RENDER_THREAD_ID)
            throw new IllegalAccessException("executeTick() can only be called by the RenderService thread!");

        if (GameService.getCurrentQuest() != null && GameService.getCurrentQuest().isPaused())
            return;

        synchronized (animate) {
            for (AnimatorData ad : animate) {
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
