package com.dissonance.framework.config.test;

import com.dissonance.framework.config.SLFS;

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
        TestNode testNode = new TestNode();

        SLFS config = new SLFS("myconfig");
        config.setFile("config/test.slfs");
        config.setHeader(
                "Name: TestConfig\n" +
                "Description: These are the settings for the whole game."
        );

        //config.getNodes().add(testNode);
        config.addNode(testNode);

        config.save();
    }
}
