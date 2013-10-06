/**
 * Name:    SLFS - Simple Language File System
 * Author:  Oliver Yasuna
 * Date:    10/5/13
 * Time:    7:48 PM
 */

package com.dissonance.framework.config;

import com.dissonance.framework.config.util.ConsoleColor;
import org.lwjgl.Sys;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        header = "";
        nodes = new HashMap<String, HashMap<String, Object>>();
    }

    //////////////////////////////////////////////////
    //                  VARIABLES                   //
    //////////////////////////////////////////////////
    private File file;
    private String header;
    private Map<String, HashMap<String, Object>> nodes;

    //////////////////////////////////////////////////
    //                  FUNCTIONS                   //
    //////////////////////////////////////////////////
    public void addNode(String key, HashMap<String, Object> values)
    {
        nodes.put(key, values);
    }

    public boolean save(boolean debug) throws FileNotFoundException
    {
        String output = format();
        if(!header.contains("#"))
        {
            formatHeader();
        }
        output = header + output;

        if(debug)
        {
            System.out.println(ConsoleColor.ANSI_CYAN + output);
        }

        System.out.println(ConsoleColor.ANSI_YELLOW + (debug ? "\n" : "") + "Saving...");

        PrintWriter out = null;
        out = new PrintWriter(file);

        out.println(output);
        out.close();

        if(file.exists())
        {
            System.out.println(ConsoleColor.ANSI_GREEN + "Successfully saved!" + ConsoleColor.ANSI_RESET);
        } else {
            System.out.println(ConsoleColor.ANSI_RED + "Error while saving!" + ConsoleColor.ANSI_RESET);
        }

        return false;
    }

    public void load(boolean debug) throws IOException
    {
        Charset charset = Charset.defaultCharset();
        byte[] encoded = Files.readAllBytes(Paths.get(file.toURI()));
        String content = charset.decode(ByteBuffer.wrap(encoded)).toString();

        if(debug)
        {
            System.out.println(ConsoleColor.ANSI_CYAN + content);
        }

        System.out.println(ConsoleColor.ANSI_YELLOW + "Loading...");

        Map<String, HashMap<String, Object>> nodes = new HashMap<String, HashMap<String, Object>>();
        HashMap<String, Object> children = new HashMap<String, Object>();
        List<HashMap<String, Object>> childrenList = new ArrayList<HashMap<String, Object>>();

        content = content.split("#\n\n")[1];

        for(int i = content.split("\n").length - 1; i >= 0; i--)
        {
            String line = content.split("\n")[i];

            if(line.startsWith("\u0020") && !line.startsWith("\u0020\t"))
            {
                line = line.replaceFirst("\u0020", "");

                System.out.println(ConsoleColor.ANSI_PURPLE + "Found node:  " + line.split(":")[0]);

                nodes.put(line.split(":")[0], children);
                children = new HashMap<String, Object>();
            } else if(line.startsWith("\u0020\t")) {
                line = line.replaceFirst("\u0020\t", "");

                String[] value = line.split(": ");

                System.out.println(ConsoleColor.ANSI_BLUE + "Found child: \t" + value[0] + "=" + value[1] + "|" + formatForType(value[1]).getClass());

                children.put(value[0], formatForType(value[1]));
            }
        }

        this.nodes = nodes;

        System.out.println(ConsoleColor.ANSI_GREEN + "Successfully loaded!" + ConsoleColor.ANSI_RESET);

        // TODO: This is just for testing.
        //       Remove later.
        file = new File("config/mysave1.dat");
        save(false);
    }

    private Object formatForType(String value)
    {
        if(value.startsWith("0x"))
        {
            return Byte.valueOf(value.split("x")[1]);
        } else if(value.endsWith("s")) {
            return Short.valueOf(value.split("s")[0]);
        } else if(value.endsWith("i")) {
            return Integer.valueOf(value.split("i")[0]);
        } else if(value.endsWith("l")) {
            return Long.valueOf(value.split("l")[0]);
        } else if(value.endsWith("f")) {
            return Float.valueOf(value.split("f")[0]);
        } else if(value.endsWith("d")) {
            return Double.valueOf(value.split("d")[0]);
        } else if(value.equals("true") || value.equals("false")) {
            return Boolean.valueOf(value);
        } else if(value.startsWith("'") && value.endsWith("'")) {
            return value.replaceAll("'", "").charAt(0);
        } else if(value.startsWith("\"")  && value.endsWith("\"")) {
            return String.valueOf(value.replaceAll("\"", ""));
        }

        return null;
    }

    private String format()
    {
        StringBuilder output = new StringBuilder();

        for(String node : nodes.keySet())
        {
            output.append("\u0020" + node + ":");
            output.append(" \n");

            for(String key : nodes.get(node).keySet())
            {
                Object value = nodes.get(node).get(key);

                if(value.getClass() == HashMap.class)
                {
                    output.append("\u0020\t");
                    output.append(key);
                    output.append("\n");

                    for(String key2 : ((HashMap<String, Object>)value).keySet())
                    {
                        Object value2 = ((HashMap<String, Object>)value).get(key2);

                        output.append("\u0020\t\t");
                        output.append(key2 + ": " + formatValue(value2));
                        output.append("\n");
                    }
                } else {
                    output.append("\u0020\t");
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
            //value = "0x" + String.format("%02x", value);
            value = "0x" + value;
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

    private void formatHeader()
    {
        String[] lines = header.split("\n");
        header = "";

        int longest = 0;

        for(String s : lines)
        {
            if(longest < s.length())
            {
                longest = s.length();
            }
        }

        for(int i = 0; i < longest + 6; i++)
        {
            header += "#";
        }
        header += "\n";
        for(String s : lines)
        {
            header += "## " + s;
            for(int i = 0; i < longest - s.length(); i++)
            {
                header += " ";
            }
            header += " ##" + "\n";
        }
        for(int i = 0; i < longest + 6; i++)
        {
            header += "#";
        }
        header += "\n\n";
    }

    //////////////////////////////////////////////////
    //             GETTERS AND SETTERS              //
    //////////////////////////////////////////////////

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public Map<String, HashMap<String, Object>> getNodes()
    {
        return nodes;
    }

    public void setNodes(Map<String, HashMap<String, Object>> nodes)
    {
        this.nodes = nodes;
    }
}
