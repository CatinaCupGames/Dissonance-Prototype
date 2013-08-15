package com.tog.framework.game.input;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Rumbler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver
 * Date: 8/14/13
 * Time: 11:24 PM
 */

public final class JoystickHandler
{
	/**
	 * Default constructor.
	 */
	private JoystickHandler()
	{
	}

	private static Map<String, JoystickThread> threads = new HashMap<String, JoystickThread>();

	private static Map<String, Controller> controllers = new HashMap<String, Controller>();
	private static Map<Controller, Rumbler[]> rumblers = new HashMap<Controller, Rumbler[]>();

	/**
	 * Acquire all (enabled) available Serial Device controllers.
	 */
	public static Controller[] getEnabledControllers()
	{
		Controller[] controllers1 = ControllerEnvironment.getDefaultEnvironment().getControllers();

		for(final Controller controller2 : controllers1)
		{
			if(!(controller2.getName().contains("mous")) && !(controller2.getName().contains("keyboar"))
					&& !(controller2.getName().contains("Mous")) && !(controller2.getName().contains("Keyboar")))
			{
				controllers.put(controller2.getName(), controller2);
			}
		}

		for(final Controller controller : controllers.values())
		{
			System.out.println(controller.getName() + " : " + controller.getPortNumber() + " : " + controller.getPortType() + " : " + controller.getType());

			rumblers.put(controller, controller.getRumblers());

			for(final Rumbler rumbler : rumblers.get(controller))
			{
				System.out.println("  - " + rumbler.getAxisName() + " : " + rumbler.getAxisIdentifier());
			}
		}

		return controllers.values().toArray(new Controller[controllers.size()]);
	}

	public static JoystickThread createControllerThread(int id)
	{
		Controller controller = (Controller)controllers.values().toArray()[id];
		JoystickThread thread = new JoystickThread(controller);

		return thread;
	}

	public static final class JoystickThread implements Runnable
	{
		/**
		 * Default constructor.
		 */
		private JoystickThread()
		{
			controller = null;
		}
		/**
		 * Constructor that takes in the controller to handle.
		 */
		public JoystickThread(Controller controller)
		{
			this.controller = controller;
		}

		@Override
		public void run()
		{
			float lastPollData = 0.0f;

			while(true)
			{
				controller.poll();

				for(Component component : controller.getComponents())
				{
					if(component.getName().equals("Cross"))
					{
						if(lastPollData != component.getPollData())
						{
							System.out.println(component.getPollData());
						}

						lastPollData = component.getPollData();
					}
				}
			}
		}

		private final Controller controller;

		public Controller getController()
		{
			return controller;
		}
	}
}
