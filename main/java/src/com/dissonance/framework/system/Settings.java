package com.dissonance.framework.system;

import net.java.games.input.Controller;

import java.util.HashMap;
import java.util.Map;

public final class Settings
{
    /**
     * A list of controllers and if they are enabled.
     */
    public static Map<Controller, Boolean> controllers = new HashMap<Controller, Boolean>();
}
