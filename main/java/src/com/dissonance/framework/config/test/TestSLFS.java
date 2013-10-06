package com.dissonance.framework.config.test;

import com.dissonance.framework.config.SLFS;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Test SLFS library.
 *
 * @author Oliver Yasuna
 * @version 1.0
 */
public class TestSLFS
{
    public static void main(String[] args)
    {
        new TestSLFS(args);
    }

    private TestSLFS(String[] args)
    {
        File file = new File("config/mysave.dat");

        SLFS slfs = new SLFS(file);

        HashMap<String, Object> firstnode = new HashMap<String, Object>();
        {
            firstnode.put("byteVar", (byte)0xf2);
            firstnode.put("shortVar", (short)3);
            firstnode.put("intVar", 5);
            firstnode.put("longVar", 7l);
            firstnode.put("floatVar", 6.0f);
            firstnode.put("doubleVar", 2.0d);
            firstnode.put("booleanVar", false);
            firstnode.put("charVar", 'p');
            firstnode.put("stringVar", "lol");
        }
        slfs.addNode("firstnode", firstnode);

//        HashMap<String, Object> secondnode = new HashMap<String, Object>();
//        {
//            secondnode.put("longVar", 9l);
//
//            HashMap<String, Object> firstchild = new HashMap<String, Object>();
//            {
//                firstchild.put("floatVar", 3.0f);
//                firstchild.put("stringVar", "kk");
//            }
//
//            secondnode.put("firstchild", firstchild);
//        }
//        slfs.addNode("secondnode", secondnode);

        slfs.save(true);
    }
}
