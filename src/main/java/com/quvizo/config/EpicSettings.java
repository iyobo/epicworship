/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.config;

import com.quvizo.ui.fxmisc.BuffUI;
import com.sun.media.jfxmedia.logging.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 *
 * @author peki
 */
public class EpicSettings {

    private static final String FILE = "configuration.properties";
    //TODO: Make sure there is a default bundle if locale.getdefault's bundle has not been created in /i18n/
    public static final ResourceBundle bundle = ResourceBundle.getBundle("i18n/Bundle", Locale.getDefault());

    public static String lePropriedade(String propriedade) {
        FileInputStream entrada = null;
        Properties propriedades = new Properties();
        try {
            //TODO: This file may not always exist. First try to recreate it if id does not exist i.e File.exists()            
            File properties = new File(FILE);
            if (!properties.exists()) {
                gravaPropriedade("yes", "", "", "no", "", "yes", "", "", "", "", "", "", "","System");
            }
            entrada = new FileInputStream(FILE);
            propriedades.load(entrada);
        } catch (IOException e) {

            BuffUI.showMessageBox("Class EpicSettings", "Erro = " + e.getMessage()
                    + "\nAccess the file " + FILE);
            Logger.logMsg(Logger.ERROR, e.getMessage());
            System.exit(1);
        }
        return propriedades.getProperty(propriedade);
    }

    public static void gravaPropriedade(String isDefault, String videoPath, String imagePath, String hotkeys,
            String f1, String title, String f2, String f3, String f4, String f5, String f6, String f7, String f8, String font) {
        //Creat properties file
        Properties properties = new Properties();
        try {
            //Set some  content
            properties.setProperty("System.Default", isDefault);
            properties.setProperty("Screen.Video", videoPath);
            properties.setProperty("Screen.Image", imagePath);
            properties.setProperty("Live.Hotkeys", hotkeys);
            properties.setProperty("Live.Title.Song", title);
            properties.setProperty("Live.F1.Video", f1);
            properties.setProperty("Live.F2.Video", f2);
            properties.setProperty("Live.F3.Video", f3);
            properties.setProperty("Live.F4.Video", f4);
            properties.setProperty("Live.F5.Image", f5);
            properties.setProperty("Live.F6.Image", f6);
            properties.setProperty("Live.F7.Image", f7);
            properties.setProperty("Live.F8.Image", f8);
            properties.setProperty("Font.Type", font);

            FileOutputStream fos = new FileOutputStream("configuration.properties");
            //Save date on file
            properties.store(fos, " configuration.properties\n"
                    + "\n ------- General Configurations ------- \n"
                    + "\n Default configurations use System.Default = yes");
            //fecha o arquivo
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getSystemDefault() {
        return lePropriedade("System.Default");
    }

    public static String getScreenVideoPath() {
        return lePropriedade("Screen.Video");
    }

    public static String getLiveHotkeys() {
        return lePropriedade("Live.Hotkeys");
    }

    public static String getScreenImagePath() {
        return lePropriedade("Screen.Image");
    }

    public static String getLiveF1Video() {
        return lePropriedade("Live.F1.Video");
    }

    public static String getLiveTitleSong() {
        return lePropriedade("Live.Title.Song");
    }

    public static String getLiveF2Video() {
        return lePropriedade("Live.F2.Video");
    }

    public static String getLiveF3Video() {
        return lePropriedade("Live.F3.Video");
    }

    public static String getLiveF4Video() {
        return lePropriedade("Live.F4.Video");
    }

    public static String getLiveF5Image() {
        return lePropriedade("Live.F5.Image");
    }

    public static String getLiveF6Image() {
        return lePropriedade("Live.F6.Image");
    }

    public static String getLiveF7Image() {
        return lePropriedade("Live.F7.Image");
    }

    public static String getLiveF8Image() {
        return lePropriedade("Live.F8.Image");
    }
    
    public static String getFontType() {
        return lePropriedade("Font.Type");
    }
}
