/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.util;

/**
 *
 * @author BuffLogic
 */
public class OSUtils
{

    private static String OS = null;
    private static String arch = null;

    public static String getOS()
    {
        if (OS == null)
        {
            OS = System.getProperty("os.name");
        }

        return OS;
    }

    public static String getArch()
    {
        if (arch == null)
        {
            arch = System.getProperty("sun.arch.data.model");
        }

        return arch;
    }


    //OS
    public static boolean isWindows()
    {
        return getOS().startsWith("Windows");
    }

    public static boolean isMac()
    {
        //only care about universal. no ppc
        return getOS().startsWith("Mac OS");
    }

    public static boolean isLinux()
    {
        return getOS().startsWith("Linux");
    }

    //Architecture
    public static boolean isArch32()
    {
        return getArch().startsWith("32");
    }

    public static boolean isArch64()
    {
        return getArch().startsWith("64");
    }

   

}
