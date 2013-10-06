/**
 * Name:    SLFS - Simple Language File System
 * Author:  Oliver Yasuna
 * Date:    10/5/13
 * Time:    7:48 PM
 */

package com.dissonance.framework.config;

import com.dissonance.framework.config.util.ConsoleColor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Main file for creating, saving, and loading a SLFS's files.
 *
 * @author Oliver Yasuna
 * @version 1.0
 */
public class SLFS
{
    //////////////////////////////////////////////////
    //                 CONSTRUCTORS                 //
    //////////////////////////////////////////////////
    public SLFS(File file)
    {
        this.file = file;
        nodes = new HashMap<String, HashMap<String, Object>>();
    }

    //////////////////////////////////////////////////
    //                  VARIABLES                   //
    //////////////////////////////////////////////////
    private File file;
    private Map<String, HashMap<String, Object>> nodes;

    //////////////////////////////////////////////////
    //                  FUNCTIONS                   //
    //////////////////////////////////////////////////
    public void addNode(String key, HashMap<String, Object> values)
    {
        nodes.put(key, values);
    }

    public boolean save(boolean debug)
    {
        String output = format();

        if(debug)
        {
            System.out.println(ConsoleColor.ANSI_CYAN + "CONTENT OF OUTPUT: ");
            System.out.println(output + ConsoleColor.ANSI_RESET);
        }

        System.out.println(ConsoleColor.ANSI_YELLOW + "Saving...");

        PrintWriter out = null;
        try {
            out = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(out != null)
        {
            out.println(output);
            out.close();
        }

        if(file.exists())
        {
            System.out.println(ConsoleColor.ANSI_GREEN + "Successfully saved!");
        } else {
            System.out.println(ConsoleColor.ANSI_RED + "Error while saving!");
        }

        return false;
    }

    public void load()
    {
    }

    private String format()
    {
        StringBuilder output = new StringBuilder();

        for(String node : nodes.keySet())
        {
            output.append(node + ":");
            output.append("\n");

            for(String key : nodes.get(node).keySet())
            {
                Object value = nodes.get(node).get(key);

                if(value.getClass() == HashMap.class)
                {
                    output.append("\t");
                    output.append(key);
                    output.append("\n");

                    for(String key2 : ((HashMap<String, Object>)value).keySet())
                    {
                        Object value2 = ((HashMap<String, Object>)value).get(key2);

                        output.append("\t\t");
                        output.append(key2 + ": " + formatValue(value2));
                        output.append("\n");
                    }
                } else {
                    output.append("\t");
                    output.append(key + ": " + formatValue(value));
                    output.append("\n");
                }
            }
        }

        return output.toString();
    }

    private Object formatValue(Object value)
    {
        Class cls = value.getClass();

        if(cls == Byte.class)
        {
            value = "0x" + String.format("%02x ", value);
        } else if(cls == Short.class) {
            value = value + "s";
        } else if(cls == Integer.class) {
            value = value + "i";
        } else if(cls == Long.class) {
            value = value + "l";
        } else if(cls == Float.class) {
            value = value + "f";
        } else if(cls == Double.class) {
            value = value + "d";
        } else if(cls == Boolean.class) {
            value = value;
        } else if(cls == Character.class) {
            value = "'" + value + "'";
        } else if(cls == String.class) {
            value = "\"" + value + "\"";
        }

        return value;
    }

    //////////////////////////////////////////////////
    //             GETTERS AND SETTERS              //
    //////////////////////////////////////////////////
}
