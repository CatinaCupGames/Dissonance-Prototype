/**
 * Name:    SLFS - Simple Language File System
 * Author:  Oliver Yasuna
 * Date:    10/5/13
 * Time:    2:16 PM
 */

/**
 * TODO: Add support for list, arrays, etc, as nodes.
 */

package com.dissonance.framework.config;

import com.dissonance.framework.config.util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
    /**
     * Main constructor.
     * Set a name and initialize the nodes array.
     *
     * @param name Name of this SLFS object.
     */
    public SLFS(String name)
    {
        this.name = name;
        nodes = new ArrayList<Object>();
    }

    //////////////////////////////////////////////////
    //                  VARIABLES                   //
    //////////////////////////////////////////////////
    /**
     * Name of this SLFS object.
     */
    private final String name;
    /**
     * List of nodes in this SLFS object.
     */
    private final List<Object> nodes;

    /**
     * The header of the file.
     */
    private String header;

    /**
     * File to save/load data to/from.
     */
    private File file;

    //////////////////////////////////////////////////
    //                  FUNCTIONS                   //
    //////////////////////////////////////////////////
    /**
     * Add a node to this SLFS object.
     * Check to make sure it doesn't already exist.
     *
     * @param node Node to add to this SLFS object.
     * @return If the node was added.
     */
    public boolean addNode(Object node)
    {
        for(Object n : nodes)
        {
            if(node.getClass().getName().equals(n.getClass().getName()))
            {
                return false;
            }
        }

        return nodes.add(node);
    }

    /**
     * Save the data from the nodes.
     *
     * @return If saving was successful.
     */
    public boolean save()
    {
        StringBuilder output = new StringBuilder();

        output.append("ver=1").append("\n");

        if(header.length() > 0)
        {
            String[] lines = header.split("\n");

            int length = 0;
            for(String l : lines)
            {
                if(length < l.length())
                {
                    length = l.length();
                }
            }

            for(int i = 0; i < length + 6; i++)
            {
                output.append("#");
            }
            output.append("\n");
            for(String l : lines)
            {
                output.append("## ").append(l);
                for(int i = 0; i < length - l.length(); i++)
                {
                    output.append(" ");
                }
                output.append(" ##").append("\n");
            }
            for(int i = 0; i < length + 6; i++)
            {
                output.append("#");
            }
            output.append("\n\n");

            //output.append(header).append("\n");
        }
        //System.out.println(output.toString().length());

        Class cls;
        String name;
        Field[] fields;
        Class<? extends Annotation> type;
        Field field;
        String line;
        for(Object o : nodes)
        {
            cls = o.getClass();

            if(cls.isAnnotationPresent(ANode.class))
            {
                name = cls.getAnnotation(ANode.class).toString();
                name = name.split("=")[1];
                name = name.replace(')', ':');

                output.append(name).append("\n");

                fields = cls.getDeclaredFields();

                for(Field f : fields)
                {
                    for(Annotation a : f.getAnnotations())
                    {
                        // TODO: Not sure if this if statement is needed.
                        //       If I get rid of it, it might take longer to check,
                        //       due to the checking of each type.
                        if(a.annotationType().getSimpleName().startsWith("A"))
                        {
                            type = a.annotationType();

                            try {
                                if(type == AByte.class)
                                {
                                    field = f;

                                    line = field.getName() + ": 0x" + field.getByte(o);
                                    output.append("  ").append(line).append("\n");
                                } else if(type == AShort.class) {
                                    field = f;

                                    line = field.getName() + ": " + field.getShort(o) + "s";
                                    output.append("  ").append(line).append("\n");
                                } else if(type == AInteger.class) {
                                    field = f;

                                    line = field.getName() + ": " + field.getInt(o) + "i";
                                    output.append("  ").append(line).append("\n");
                                } else if(type == ALong.class) {
                                    field = f;

                                    line = field.getName() + ": " + field.getLong(o) + "l";
                                    output.append("  ").append(line).append("\n");
                                } else if(type == AFloat.class) {
                                    field = f;

                                    line = field.getName() + ": " + field.getFloat(o) + "f";
                                    output.append("  ").append(line).append("\n");
                                } else if(type == ADouble.class) {
                                    field = f;

                                    line = field.getName() + ": " + field.getDouble(o) + "d";
                                    output.append("  ").append(line).append("\n");
                                } else if(type == ABoolean.class) {
                                    field = f;

                                    line = field.getName() + ": " + field.getBoolean(o);
                                    output.append("  ").append(line).append("\n");
                                } else if(type == AChar.class) {
                                    field = f;

                                    line = field.getName() + ": '" + field.getChar(o) + "'";
                                    output.append("  ").append(line).append("\n");
                                } else if(type == AString.class) {
                                    field = f;

                                    line = field.getName() + ": \"" + field.get(o) + "\"";
                                    output.append("  ").append(line).append("\n");
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            return false;
        } finally {
            if(out != null)
            {
                out.println(output.toString());
                out.close();

                return true;
            }

            return false;
        }
    }

    /**
     * Set the file to save/load data to/from from a String.
     *
     * @param file File to save/load data to/from as a String.
     */
    public void setFile(String file)
    {
        File file1 = new File(file);

        setFile(file1);
    }

    //////////////////////////////////////////////////
    //             GETTERS AND SETTERS              //
    //////////////////////////////////////////////////
    /**
     * Get the name of this SLFS object.
     *
     * @return {@link SLFS#name} - Name of this SLFS object.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the nodes in this SLFS object.
     *
     * @return {@link SLFS#nodes} - List of nodes in this SLFS object.
     */
    public List<Object> getNodes()
    {
        return nodes;
    }

    /**
     * Get the header of the file.
     *
     * @return {@link SLFS#header} - Header of the file.
     */
    public String getHeader()
    {
        return header;
    }

    /**
     * Set the header of the file.
     *
     * @param header Header of the file.
     */
    public void setHeader(String header)
    {
        this.header = header;
    }

    /**
     * Get the file to save/load data to/from.
     *
     * @return {@link SLFS#file} - File to save/load data to/from.
     */
    public File getFile()
    {
        return file;
    }

    /**
     * Set the file to save/load data to/from.
     *
     * @param file File to save/load data to/from.
     */
    public void setFile(File file)
    {
        this.file = file;
    }
}
