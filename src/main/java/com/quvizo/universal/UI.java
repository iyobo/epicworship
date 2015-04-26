/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.universal;

import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import com.quvizo.data.entity.Asset;
import com.quvizo.data.entity.Presentation;

/**
 *
 * @author peki
 */
public class UI
{
    public static Presentation selectedPresentation=null;
    public static Presentation livePresentation=null;
    
    public  static Asset selectedSong = null;
    public  static Asset selectedVideo = null;
    public  static Asset selectedImage = null;
    
    public static Insets defaultPadding = new Insets(8);
    
    public static Color TEXTCOLOR = Color.WHITE;
    public static Color TEXTSHADOWCOLOR = Color.BLACK;
    public static int songTableMaxTextLength = 50;
    
    public static double songFontSizeMax = 200d;
    public static float TEXTSHADOWDIMNESS = 0.85f;
    public static int TEXTSIZE=120;
    public static int TEXTSHADOWRADIUS = 150;
}
