/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.projector;

import com.quvizo.Log;
import com.quvizo.ui.director.fxcontroller.LiveTabController;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import com.quvizo.universal.ProjectorConstants;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import javafx.animation.Animation;

/**
 * Only the most awesome background manager ever.
 *
 * @author peki
 */
public class MediaProjector
{

    private static final Logger logger = Logger.getLogger(MediaProjector.class.getName());
    private String path;
    private Media media;
    //private ImageView imageView;
    private MediaView mediaView;
    private MediaPlayer mediaPlayer;
    private static MediaProjector instance = null;
    FadeTransition fadeOutTransition;
    FadeTransition fadeInTransition;

    public static MediaProjector getInstance()
    {
        if(instance == null)
        {
            instance = new MediaProjector();
        }

        return instance;
    }

    private MediaProjector()
    {
        initVideo();
    }

    private void initVideo()
    {
        try
        {
            // Create MediaView from MediaPlayer. View must not be recreated as it is attached externally.
            if(mediaView == null)
            {
                mediaView = new MediaView(mediaPlayer);

                // Make the media rescale to the scene dimensions without preserving the aspect ratio.
                mediaView.setX(0);
                mediaView.setY(0);
                mediaView.setFitWidth(ProjectorConstants.SCREENWIDTH);
                mediaView.setFitHeight(ProjectorConstants.SCREENHEIGHT);
                mediaView.setPreserveRatio(false);
                mediaView.setOpacity(0);

                fadeOutTransition = new FadeTransition(Duration.millis(1000), mediaView);
                fadeOutTransition.setFromValue(1f);
                fadeOutTransition.setToValue(0f);
                fadeOutTransition.setCycleCount(1);

                fadeInTransition = new FadeTransition(Duration.millis(1000), mediaView);
                fadeInTransition.setFromValue(0f);
                fadeInTransition.setToValue(1f);
                fadeInTransition.setCycleCount(1);
            }
        }
        catch(Exception e)
        {
            //logger.warning("Could not Load Media. Either media doesn't exist or not supported.");
            Log.log(this, e, "Could not Load Media. Either media doesn't exist or not supported.");
        }
    }

    public MediaPlayer getMediaPlayer()
    {
        return mediaPlayer;
    }

    public Media getMedia()
    {
        return media;
    }

    public MediaView getMediaView()
    {
        return mediaView;
    }

    public String getPath()
    {
        return path;
    }

    /**
     * fadeout to actual bacground image from background node.
     */
    public void hideVideo()
    {
        LiveTabController.getInstance().getMediaControl().disarmPlayButton();
        fadeOutTransition.play();
        fadeOutTransition.setOnFinished(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if(mediaPlayer!=null)
                    mediaPlayer.stop();

            }
        });

        fadeOutAudio();
    }

    private void fadeOutAudio()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                while(fadeOutTransition.getStatus().equals(Animation.Status.RUNNING) && mediaPlayer != null)
                {
                    double vol = mediaPlayer.getVolume() - 0.1;
                    if(vol < 0)
                    {
                        vol = 0;
                    }
                    mediaPlayer.setVolume(vol);
                    try
                    {
                        sleep(100);
                    }
                    catch(InterruptedException ex)
                    {
                        Logger.getLogger(MediaProjector.class.getName()).log(Level.SEVERE, null, ex);
                        break;
                    }
                }

            }
        }.start();
    }

    public void playVideo(String path)
    {
        try
        {
            this.setPath(new File(path).toURI().toString());
            mediaPlayer.play();
            fadeOutTransition.stop();

            //fadein
            mediaPlayer.setVolume(1);
            fadeInTransition.play();
            LiveTabController.getInstance().getMediaControl().reAttach();
        }
        catch(Exception e)
        {
            Log.log(logger, e, e.getLocalizedMessage());
            media = null;
        }
    }

    public void setPath(String path) throws Exception
    {

            this.path = path;

            //transitionBackground();
            setupVideo();
        
    }

    private void setupVideo() throws Exception
    {
        // Create MediaPlayer from Media.
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
        }

        media = null;
        
        //pre-check for file existence
        if(!new File(new URI(path)).exists())
            throw new Exception("The file does not exist: "+path);
        
        media = new Media(path);


        mediaPlayer = null;
        mediaPlayer = new MediaPlayer(media);


        // Make play start automatically.
        mediaPlayer.setAutoPlay(true);

        mediaPlayer.setOnEndOfMedia(new Runnable()
        {
            @Override
            public void run()
            {
                hideVideo();
            }
        });

        mediaView.setMediaPlayer(mediaPlayer);

    }

    public boolean isShowing()
    {
        return (mediaView.getOpacity() > 0d);
    }
}
