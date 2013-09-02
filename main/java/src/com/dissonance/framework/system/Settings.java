package com.dissonance.framework.system;

import net.java.games.input.Component;
import net.java.games.input.Controller;

import java.util.HashMap;
import java.util.Map;

public final class Settings
{
    /**
     * A list of controllers and if they are enabled.
     */
    public static Map<Controller, Boolean> controllers = new HashMap<Controller, Boolean>();

    /**
     * Whether or not a controller exists, and if so, is the user using a controller instead of the keyboard.
     */
    public static boolean usingController = false;

    public static String enabledController = "";

	public static Component.Identifier.Axis move;
	public static Component.Identifier.Button jump;
	public static Component.Identifier.Button dodge;
	public static Component.Identifier.Button cycleParty;
	public static Component.Identifier.Button strafe;
	public static Component.Identifier.Button attackSelect;
	public static Component.Identifier.Button specialAbility;
	public static Component.Identifier.Button magic1;
	public static Component.Identifier.Button magic2;
	public static Component.Identifier.Button equipmentStatusMagicMenu;
	public static Component.Identifier.Button pause;
	public static Component.Identifier.Axis extendedLook;
}
