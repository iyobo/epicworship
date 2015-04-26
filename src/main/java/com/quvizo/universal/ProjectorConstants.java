/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.universal;

import java.awt.GraphicsDevice;

/**
 *
 * @author BuffLogic
 */
public class ProjectorConstants
{
    /*  1000
     *-------- 
     *|	     | 1000
     *|	     |
     *--------
     */
    public static GraphicsDevice screen;
    
    private static double virtualWidth = 1000;
    private static double virtualHeight = 1000;
    private  static double virtualSongTextX= 100;
    private  static double virtualSongTextY = 500;
    private  static double virtualSongTitleX= 40;
    private  static double virtualSongTitleY = 920;
    private  static double virtualSongTextWidth = 800;
    private  static double virtualSongTextHeight = 800;
    private  static double virtualSongTitleWidth = 800;
    private  static double virtualSongTitleHeight = 60;
    
    public static double vertifact = 1;
    public static double horifact = 1;
    
    public static double SCREENX = 0;
    public static double SCREENY = 0;
    public static double SCREENWIDTH = 1000;
    public static double SCREENHEIGHT = 1000;

    public static int getSCREENHEIGHT()
    {
        return (int)SCREENHEIGHT;
    }

    public static int getSCREENWIDTH()
    {
        return (int)SCREENWIDTH;
    }

    public static int getSCREENX()
    {
        return (int)SCREENX;
    }

    public static int getSCREENY()
    {
        return (int)SCREENY;
    }
    
    
    
    public static void calibrate(double screenW, double screenH)
    {
        SCREENWIDTH = screenW;
        SCREENHEIGHT = screenH;
        
        horifact = screenW/virtualWidth;
	vertifact = screenH/virtualHeight;
	
	System.out.println(horifact + " : "+vertifact);
    }
    
    public static double hori(double num)
    {
	return num*horifact;
    }
    
    public static double verti(double num)
    {
	return num*vertifact;
    }
    
    public static double getSongWidth()
    {
	return virtualSongTextWidth*horifact;
    }
    
    public static double getSongHeight()
    {
	return virtualSongTextHeight*vertifact;
    }
    
    public static double getSongX()
    {
	return virtualSongTextX*horifact;
    }
    
    public static double getSongY()
    {
	return virtualSongTextY*vertifact;
    }
    
    public static double getTitleWidth()
    {
	return virtualSongTitleWidth*horifact;
    }
    
    public static double getTitleHeight()
    {
	return virtualSongTitleHeight*vertifact;
    }
    
    public static double getTitleX()
    {
	return virtualSongTitleX*horifact;
    }
    
    public static double getTitleY()
    {
	return virtualSongTitleY*vertifact;
    }
    
}
