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
package com.quvizo.universal;

import com.quvizo.EpicWorship;
import com.quvizo.config.EpicSettings;
import com.quvizo.projector.pages.PXVideoPage;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import com.quvizo.ui.ProjectorView;
import com.quvizo.ui.fxmisc.Qui;
import com.quvizo.ui.misc.OptionsDialog;
import com.quvizo.ui.modes.BrowserMode;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.stage.Screen;

/**
 * Handles everything.
 *
 * @author peki
 */
public class EpicOverlord {

    private static EpicOverlord instance;
    private static Logger logger = Logger.getLogger(EpicOverlord.class.getCanonicalName());

    /**
     * Be sure initializeInstance has been called once already, otherwise this
     * will return null;
     *
     * @return
     */
    public static EpicOverlord getInstance() {
        return instance;
    }

    public static EpicOverlord initializeInstance(Stage primaryStage) throws IOException {
        if (instance == null) {
            instance = new EpicOverlord(primaryStage);
        }

        return instance;
    }
    //----------End of Singleton stuff
    public static Stage primaryStage;
    public static Stage projectorStage;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Stage getProjectorStage() {
        return projectorStage;
    }

    private EpicOverlord(Stage primaryStage) throws IOException {        
        this.primaryStage = primaryStage;

        EntityOverlord.getInstance();
        
        Qui.showScreenPicker();
        initDirector();
        
        initProjector();

        //initWebView();
        //this.primaryStage.sizeToScene();
        primaryStage.toFront();

        //start countdown auto-play Timer to play 3 minute video 9:26 am        
        //launchCountdownTimer();

        //start fullscreen
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
    }

    /**
     * initialize Main UI
     */
    private void initDirector() throws IOException {
        //using FXML
        URL url = getClass().getResource("/fxml/DirectorTemplate.fxml");
        final Parent fxmlRoot = FXMLLoader.load(url,EpicSettings.bundle);
        primaryStage.setScene(new Scene(fxmlRoot));

        //Using Code [Deprecated]
        //Group uiGroup = new Group();
        //Scene scene = new Scene(uiGroup, 300, 250);
        //DirectorView.initInstance(uiGroup);//sees to the population of the DX uigroup with UI objects
        //primaryStage.setScene(scene);
        //primaryStage.setWidth(1050);
        //primaryStage.setHeight(870);

        primaryStage.getIcons().add(EpicWorship.icon);
        primaryStage.show();


    }

    /**
     * Initialize Projector
     */
    @SuppressWarnings("restriction")
	private void initProjector() {

        
        //Get details for spawning new window
//        Screen projectorMonitor = Screen.getScreens().get(Screen.getScreens().size() - 1);

//        Screen projectorMonitor = Screen.getScreens().
//        ProjectorConstants.SCREENX = projectorMonitor.getBounds().getMinX();
//        ProjectorConstants.SCREENY = projectorMonitor.getBounds().getMinY();

        //Create new borderless Window
        projectorStage = new Stage(StageStyle.UNDECORATED);
        projectorStage.setTitle("EpicWorship Projector");
        projectorStage.setX(ProjectorConstants.SCREENX);
        projectorStage.setY(ProjectorConstants.SCREENY);
        projectorStage.setWidth(ProjectorConstants.SCREENWIDTH);
        projectorStage.setHeight(ProjectorConstants.SCREENHEIGHT);

        //Create new scene for Window
       
		Group projectorGroup = new Group();
        Scene projectorScene = new Scene(projectorGroup, projectorStage.getWidth(), projectorStage.getHeight());
        ProjectorConstants.calibrate(projectorStage.getWidth(), projectorStage.getHeight());

        ProjectorView.initInstance(projectorGroup, projectorStage.getWidth(), projectorStage.getHeight());//sees to the population of the DX UIGroup with UI objects

        projectorStage.setScene(projectorScene);
        projectorStage.show();


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            public void handle(WindowEvent event) {
                projectorStage.close();
                System.exit(0);
            }
        });

        projectorStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            public void handle(WindowEvent event) {
                primaryStage.close();
                System.exit(0);
            }
        });

        //if director looses focus, send projector to back
        primaryStage.setOnHidden(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {
                System.out.println("hidden");
                projectorStage.toBack();
            }
        });
        primaryStage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {
                System.out.println("hiding");
                projectorStage.toBack();

            }
        });


    }

    public String getVideoEmbedForURL(String urlstring, boolean showWarnings) {
        String html = "";

        URL url;
        try {
            url = new URL(urlstring);
        } catch (MalformedURLException ex) {
            Logger.getLogger(EpicOverlord.class.getName()).log(Level.SEVERE, null, ex);
            Qui.showMessageBox("Bad URL format", "That's not a proper Web Link from YouTube. Epic WebVideo can't play it. Try copying and pasting it again.");
            return "";
        }

        //TODO: User should really only copy/paste URL
        //What site are we dealing with here? 
        if (urlstring.contains("youtube")) {

            //first get the video Id
            String id = "";


            String[] params = url.getQuery().split("&");

            for (String param : params) {
                if (param.contains("v=")) {
                    id = param.substring(2); //just want the id
                    break;
                }
            }

            //piece in the html
            html = "<iframe width='" + (ProjectorConstants.getSCREENWIDTH() - 30) + "' height='" + (ProjectorConstants.getSCREENHEIGHT() - 50) + "' src='http://www.youtube.com/embed/[[id]]?rel=0' frameborder='0' allowfullscreen></iframe>";
            html = html.replace("[[id]]", id);


        } else if (urlstring.contains("godtube")) {

            //first get the video Id
            String id = "";

            String[] params = url.getQuery().split("&");

            for (String param : params) {
                if (param.contains("v=")) {
                    id = param.substring(2); //just want the id
                    break;
                }
            }

            //piece in the html
            html = "<script type='text/javascript' src='http://www.godtube.com/embed/source/[[id]].js?w=" + (ProjectorConstants.getSCREENWIDTH() - 30) + "&h=" + (ProjectorConstants.getSCREENHEIGHT() - 50) + "&ap=true&sl=false&title=false'></script>";
            html = html.replace("[[id]]", id);

        } else if (urlstring.contains("wingclips")) {
            if (showWarnings) {
                Qui.showMessageBox("Warning", "Embedding from WingClips requires the CONFIGURL of the video and not just what you see on your web browser. \n\nThe CONFIGURL can be gotten from the video's unique HTML embed code and it ends with \"config.js\". If you don't understand what we just said, then you probably should just register with them and download the Video. Thanks");
            }

            //first get the video Id
            String id = "";
            String id2 = "";

            String[] chunks = url.getPath().split("/");

            if (chunks.length < 4) {
                Logger.getLogger(EpicOverlord.class.getName()).severe("Bad wingclips URL: " + urlstring);
                Qui.showMessageBox("Bad/incomplete URL", "Wrong wingclips URL: " + urlstring);
                return "";
            }

            id = chunks[2];
            id2 = chunks[3];
            System.out.println("video id: " + id);
            System.out.println("video id2: " + id2);

            //piece in the html
            html = "<embed width='" + (ProjectorConstants.getSCREENWIDTH() - 30) + "' height='" + (ProjectorConstants.getSCREENHEIGHT() - 50) + "' src='http://www.wingclips.com/embed/player.swf?config=http://www.wingclips.com/player/[[videoid]]/[[videoid2]]/config.js' type='application/x-shockwave-flash' allowfullscreen='true'></embed>";
            html = html.replace("[[videoid]]", id);
            html = html.replace("[[videoid2]]", id2);

        } else if (urlstring.contains("sermonspice")) {
            System.out.println("config: sermonspice");

            if (showWarnings) {
                Qui.showMessageBox("Warning", "Embedding from Sermon Spice requires the COMPLETEURL of the video and not just what you see on your web browser. \n\nThe complete url can be gotten from the video's unique HTML embed code under 'flashvars'. If you don't understand what we just said, then you probably should just pay for the video download. Thanks");
            }

            //piece in the html
            html = "<object width='" + (ProjectorConstants.getSCREENWIDTH() - 30) + "' height='" + (ProjectorConstants.getSCREENHEIGHT() - 50) + "' data='http://cache.sermonspice.com/media/lockdown/flash/flowplayer.commercial-3.2.7.swf' type='application/x-shockwave-flash'><param name='movie' value='http://cache.sermonspice.com/media/lockdown/flash/flowplayer.commercial-3.2.7.swf' /><param name='allowfullscreen' value='true' /><param name='allowscriptaccess' value='always' /><param name='flashvars' value=\"config={'key':'#$fa77153d3d04508bc45','clip':{'autoPlay':false,'autoBuffering':true,'provider':'rtmp'},'plugins':{'rtmp':{'url':'http://cache.sermonspice.com/media/lockdown/flash/flowplayer.rtmp-3.2.3.swf','netConnectionUrl':'rtmp://flash.sermonspice.com/cfx/st'},'controls':{'autoHide':'always','hideDelay':1000,'volumeSliderGradient':'none','volumeSliderColor':'#000000','tooltipTextColor':'#ffffff','buttonOverColor':'#728B94','tooltipColor':'#5F747C','sliderColor':'#ffffff','durationColor':'#ffffff','progressGradient':'medium','sliderGradient':'none','backgroundColor':'#3d3d3d','borderRadius':'0px','backgroundGradient':'none','bufferGradient':'none','progressColor':'#112233','bufferColor':'#445566','timeBgColor':'#555555','timeColor':'#01DAFF','buttonColor':'#375068','height':32,'opacity':0.7,'fullscreen':true,'tooltips':{'buttons':true,'fullscreen':'Enter Fullscreen mode'}}},'playlist':[{'type':'video','provider':'rtmp','urlEncoding':false,'completeUrl':'[[videourl]]','autoPlay':true,'url':'[[videourl]]'}]}\" /></object>";
            html = html.replace("[[videourl]]", urlstring);
            html = html.replace("[[videourl]]", urlstring);


        } else if (urlstring.contains("vimeo.com")) {
            System.out.println("config: Vimeo.com");
            //first get the video Id
            String id = "";

            String[] chunks = url.getPath().split("/");

            if (chunks.length < 2) {
                Logger.getLogger(EpicOverlord.class.getName()).severe("Bad Vimeo URL: " + urlstring);
                return "";
            }

            id = chunks[1];
            System.out.println("video id: " + id);

            //piece in the html
            html = "<iframe src='http://player.vimeo.com/video/[[videoid]]?portrait=0&amp;color=638696&amp;autoplay=1' width='" + (ProjectorConstants.getSCREENWIDTH() - 30) + "' height='" + (ProjectorConstants.getSCREENHEIGHT() - 50) + "' frameborder='0' webkitAllowFullScreen allowFullScreen></iframe>";
            html = html.replace("[[videoid]]", id);

        } else {
            Qui.showMessageBox("Unsupported Site", "This video site is not supported right now. If you'd like it to, send us an email and we'll see what we can do.");
        }

        return html;
    }

    public void showWebView(String content, BrowserMode mode) {
        /*
         * if (browser == null) { initWebView(); } if(content==null) {
         * logger.severe("HTML content/url string is null. Please provide
         * content or URL."); BuffUI.showMessageBox("Error", "HTML content/url
         * string is null. Please provide content or URL."); return; }
         *
         * if (mode == BrowserMode.WEBVIDEO) { System.out.println("Detecting
         * configuration for: " + content); String html =
         * getVideoEmbedForURL(content, false); if (!html.isEmpty()) {
         * browser.showWeb(prepend + html,true); } System.out.println("HTML: " +
         * html); } else if (mode == BrowserMode.EMBED) {
         * System.out.println("Embedding HTML content: " + content);
         * browser.showWeb(content,true); } else if (mode == BrowserMode.URL) {
         * System.out.println("Navigating to: " + content);
         * browser.showWeb(content,false); }
         *
         *
         *
         */
    }

    public void hideWebView() {
        /*
         * System.out.println("Closing webview"); if (browser == null) { return;
         * }
         *
         * browser.hideWeb();
         */
    }
    Timer timer;

    private void launchCountdownTimer() {


        restartCounter();

    }

    public void restartCounter() {
        logger.info("Restarting Countdown!!!");
        if (timer != null) {
            timer.cancel();
        }


        timer = new Timer("Countdown", true);
        Calendar cal = Calendar.getInstance();

        String day = OptionsDialog.instance.daySetting.getStringData();
        if (day.equals(EpicSettings.bundle.getString("week.mon"))) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        } else if (day.equals(EpicSettings.bundle.getString("week.tue"))) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        } else if (day.equals(EpicSettings.bundle.getString("week.wed"))) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        } else if (day.equals(EpicSettings.bundle.getString("week.thu"))) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        } else if (day.equals(EpicSettings.bundle.getString("week.fri"))) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        } else if (day.equals(EpicSettings.bundle.getString("week.sat"))) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        } else if (day.equals(EpicSettings.bundle.getString("week.sun"))) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        } else {
            return; //must be set to none then, so no trigger.
        }
        cal.set(Calendar.HOUR_OF_DAY, OptionsDialog.instance.hourSetting.getIntData());
        cal.set(Calendar.MINUTE, OptionsDialog.instance.minuteSetting.getIntData());
        cal.set(Calendar.SECOND, OptionsDialog.instance.secondsSetting.getIntData());

        Date now = new Date();
        long diff = cal.getTimeInMillis() - now.getTime();

        if (diff < 0) {
            diff += 604800000; //add a week
        }
        logger.info("millis till countdown launch=" + diff);


        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                logger.info("Launching Countdown Video!!!");

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        if (!OptionsDialog.instance.videoSetting.getStringData().isEmpty()) {
                            PXVideoPage videoPage = new PXVideoPage(OptionsDialog.instance.videoSetting.getStringData());
                            logger.info("Video: " + OptionsDialog.instance.videoSetting.getStringData());
                            ProjectorView.getInstance().pushScene(videoPage);
                        } else {
                            logger.info("Countdown Video could not be launched...");
                        }
                        restartCounter();
                    }
                });

            }
        }, diff, 604800000);


    }
}
