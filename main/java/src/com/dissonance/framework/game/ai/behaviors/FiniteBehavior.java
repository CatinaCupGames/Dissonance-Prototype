package com.dissonance.framework.game.ai.behaviors;

public interface FiniteBehavior extends Behavior {
    public interface FiniteBehaviorEvent {
        public interface OnFinished {
            public void onFinished(FiniteBehavior behavior);
        }
    }
}
