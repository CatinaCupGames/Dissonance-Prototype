package org.oyasunadev.li.liui.component.gui;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 10/6/13
 * Time: 11:29 PM
 */

public class MenuItem
{
    public MenuItem(int i, String[] options)
    {
        this.i = i;
        this.options = options;
    }

    public int i;
    public final String[] options;
}
