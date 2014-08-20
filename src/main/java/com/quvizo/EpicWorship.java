/*
 * EpicWorship. Church Projection Software
 Copyright (C) 2012  Philip Iyobose Eki

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.*/
package com.quvizo;

import com.quvizo.config.EpicSettings;
import com.quvizo.ui.fxmisc.BuffUI;
import com.quvizo.ui.fxmisc.Qui;
import com.quvizo.ui.misc.OptionsDialog;

import java.util.Locale;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import com.quvizo.universal.BibleKeeper;
import com.quvizo.universal.EpicOverlord;
import com.quvizo.util.OSUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import javafx.scene.image.Image;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.sf.nimrod.NimRODLookAndFeel;
import net.sf.nimrod.NimRODTheme;

/**
 *
 * @author BuffLogic
 */
public class EpicWorship extends Application {

    private static Logger logger = Logger.getLogger(EpicWorship.class.getName());
    public static Image icon;

    /**
     * @param args the command line arguments. Never touch this. use either init
     * or start.
     */
    public static void main(final String[] args) {
        try {
            // Create an appending file handler
            boolean append = true;
            FileHandler handler = new FileHandler("epiclog.txt", append);

            // Add to the desired logger
            Logger logger = Logger.getLogger("com.bufflogic");
            logger.addHandler(handler);
        } catch (IOException e) {
          System.out.println("This is bad. Logging could not be initialized: "+e);
          BuffUI.showMessageBox("Launch Error", "EpicWorship cannot create log file. Please report this error. You may continue using EpicWorship.");
          
        }
        Application.launch(args);
    }

    public static String getPreferredWorkingDirectory() {
        return System.getProperty("user.home", ".") + File.separator + "BuffLogicData" + File.separator + "EpicWorship";
    }

    @Override
    public void init() throws Exception {

        try {
            logger.info("Starting EpicWorship...");
            String dir = getPreferredWorkingDirectory();

            logger.info("Database & Log location: " + dir);

            // Set the db system directory.
            System.setProperty("derby.system.home", dir);

            // Create an appending file handler
            boolean append = true;
            FileHandler handler = new FileHandler("epiclog.log", append);
            handler.setFormatter(new SimpleFormatter());
            // Add to the desired logger
            Logger logger = Logger.getLogger("com.bufflogic");
            logger.addHandler(handler);

            //redirect system.out
            File file = new File("deeplog.log");
            PrintStream printStream = new PrintStream(new FileOutputStream(file));
            System.setOut(printStream);
            
            //Locale.setDefault(Locale.US);

        } catch (IOException e) {
          System.out.println("This is bad. Logging could not be initialized: "+e);
          BuffUI.showMessageBox("Launch Error", "EpicWorship cannot create log file. Please report this error. You may continue using EpicWorship.");
        }

        //this needs to be done in AWT thread
        if(!OSUtils.isMac())
        {
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                initAndShowGUI();
	            }
	        });
        }
        BibleKeeper.getInstance();

        super.init();



    }

    private void initAndShowGUI() {
        NimRODTheme nt = new NimRODTheme(getClass().getResource("/media/nimrod.ui"));

        NimRODLookAndFeel nf = new NimRODLookAndFeel();
        nf.setCurrentTheme(nt);

        try {
            UIManager.setLookAndFeel(nf);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(EpicWorship.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Initialize Bible
        BibleKeeper.getInstance();

        //Initialize Settings
        /*OptionsDialog.init();*/



    }
    

    @Override
    public void start(final Stage primaryStage) {

        icon = new Image(getClass().getResourceAsStream("/media/logo.png"));
        primaryStage.setTitle(EpicSettings.bundle.getString("program.title"));
        try {
            EpicOverlord.initializeInstance(primaryStage);
        } catch (Exception ex) {
            Logger.getLogger(EpicWorship.class.getName()).log(Level.SEVERE, null, ex);

            Qui.showMessageBox("Critical Error", "EpicWorship has crashed and cannot load.");
            System.exit(0);
        }

    }
}
