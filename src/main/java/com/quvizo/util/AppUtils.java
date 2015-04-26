/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.util;

import com.quvizo.EpicWorship;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 *
 * @author peki
 */
public class AppUtils
{

    public static void setAtCenter(Component window)
    {
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = window.getSize().width;
        int h = window.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        // Move the window
        window.setLocation(x, y);
    }

    public static Image getImage(String imagename)
    {
        return Toolkit.getDefaultToolkit().getImage("data" + File.separator + "images" + File.separator + imagename);
    }

    public static String getVideoPath(String videoname)
    {
        return "data" + File.separator + "video" + File.separator + videoname;
    }

    public static String getThemePath(String themename)
    {
        return "data" + File.separator + "theme" + File.separator + themename;
    }

    public static String getLogItemPath(String name)
    {
        return "data" + File.separator + "logs" + File.separator + name;
    }

    public static String getConfigItemPath(String name)
    {
        return "config" + File.separator + name;
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException
    {
        if (!destFile.exists())
        {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try
        {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally
        {
            if (source != null)
            {
                source.close();
            }
            if (destination != null)
            {
                destination.close();
            }
        }
    }

    public static String extractFile(InputStream inputStream, String name) throws IOException
    {

        File f = new File(EpicWorship.getPreferredWorkingDirectory() + File.separator + name);

        OutputStream out = new FileOutputStream(f);
        byte buf[] = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0)
        {
            out.write(buf, 0, len);
        }
        out.close();
        inputStream.close();

        return f.toURI().toString();
    }

    public static String readFileAsString(String filePath)
            throws java.io.IOException
    {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1)
        {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}
