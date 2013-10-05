package com.dissonance.framework.config.test;

import com.dissonance.framework.config.util.*;

/**
 * Just a test node.
 *
 * @author Oliver Yasuna
 * @version 1.0
 */
@ANode(name="MyTest")
public class TestNode
{
    /**
     * @deprecated This may not be needed in the end.
     */
    @Deprecated
    public TestNode()
    {
    }

    @AByte
    public byte MyByte = (byte)9;

    @AShort
    public short MyShort = (short)5;

    @AInteger
    public int MyInteger = (int)2;

    @ALong
    public long MyLong = (long)6;

    @AFloat
    public float MyFloat = (float)3;

    @ADouble
    public double MyDouble = (double)7;

    @ABoolean
    public boolean MyBoolean = (boolean)false;

    @AChar
    public char MyChar = (char)'p';

    @AString
    public String MyString = (String)"lol";
}
