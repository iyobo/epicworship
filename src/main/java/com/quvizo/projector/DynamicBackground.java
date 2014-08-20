/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.projector;

import com.quvizo.Log;
import com.quvizo.config.EpicSettings;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import com.quvizo.universal.ProjectorConstants;
import com.quvizo.util.AppUtils;
import java.io.IOException;
import java.util.logging.Level;
import javafx.scene.web.WebView;

/**
 * Only the most awesome background manager ever.
 *
 * @author peki
 */
public class DynamicBackground
{

    private static final Logger logger = Logger.getLogger(DynamicBackground.class.getName());
    
    private String path;
    private Media media;
    private ImageView imageView;
    private MediaView mediaView;
    private MediaPlayer mediaPlayer;
    private WebView webView;
    public static DynamicBackground instance = null;

    public DynamicBackground()
    {
	setPathToDefault();
	initVideo();
	initWeb();
	instance = this;
    }

    public void setPathToDefault()
    {
try {
            String useDef = EpicSettings.getSystemDefault();
            String pathCon = EpicSettings.getScreenVideoPath();            
            if(useDef.equalsIgnoreCase("yes")){
            path = AppUtils.extractFile(getClass().getResourceAsStream("/media/yellow.flv"), "yellow.flv");
            } else {
            path = pathCon;           
            }            
        } catch (IOException ex) {
            Logger.getLogger(DynamicBackground.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initVideo()
    {
	try
	{

	    media = new Media(path);


	    // Create MediaPlayer from Media.
	    if (mediaPlayer != null)
	    {
		mediaPlayer.stop();
	    }
	    mediaPlayer = new MediaPlayer(media);


	    // Make play start automatically.
	    mediaPlayer.setAutoPlay(true);
	    mediaPlayer.setVolume(0);

	    // Make the media loop an infinite number of times.
	    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

	    // Create MediaView from MediaPlayer. View must not be recreated as it is attached externally.
	    if (mediaView == null)
	    {
		mediaView = new MediaView(mediaPlayer);
		imageView = new ImageView(new Image(getClass().getResource("/media/white.jpg").toString()));

		// Make the media rescale to the scene dimensions without preserving the aspect ratio.
		mediaView.setX(0);
		mediaView.setY(0);
		mediaView.setFitWidth(ProjectorConstants.SCREENWIDTH);
		mediaView.setFitHeight(ProjectorConstants.SCREENHEIGHT);
		mediaView.setPreserveRatio(false);

		imageView.setX(0);
		imageView.setY(0);
		imageView.setFitWidth(ProjectorConstants.SCREENWIDTH);
		imageView.setFitHeight(ProjectorConstants.SCREENHEIGHT);

		fadeOutTransition = new FadeTransition(Duration.millis(500), mediaView);
		fadeOutTransition.setFromValue(1f);
		fadeOutTransition.setToValue(0f);
		fadeOutTransition.setCycleCount(1);

		fadeInTransition = new FadeTransition(Duration.millis(500), mediaView);
		fadeInTransition.setFromValue(0f);
		fadeInTransition.setToValue(1f);
		fadeInTransition.setCycleCount(1);
	    }
	    else
	    {
		mediaView.setMediaPlayer(mediaPlayer);
	    }


	}
	catch (Exception e)
	{
            Log.log(this, e, "Could not Load Media Background: "+e.getLocalizedMessage());
            
	}
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

    public void setPath(String path)
    {
	this.path = path;

	transitionBackground();
    }

    /**
     * fadeout to actual bacground image from background node.
     */
    public void hideVideo()
    {
	fadeOutTransition.setOnFinished(new EventHandler<ActionEvent>()
	{

	    @Override
	    public void handle(ActionEvent event)
	    {
		mediaPlayer.stop();
	    }
	});

	imageView.setVisible(false);
	fadeOutTransition.play();
    }

    public void showVideo()
    {
	if (mediaView.opacityProperty().getValue() == 0)
	{
	    mediaPlayer.play();
	    fadeInTransition.setOnFinished(new EventHandler<ActionEvent>()
	    {

		@Override
		public void handle(ActionEvent event)
		{
		    //be sure to do nothing.
		}
	    });
	    fadeInTransition.play();

	}
    }
    FadeTransition fadeOutTransition;
    FadeTransition fadeInTransition;

    /**
     * Fade out current video to white. Fade in new video from white. (using white backdrop of imageview)
     */
    private void transitionBackground()
    {
	//after fading in to next video, we don't need to render image view as that is just to help with transition
	fadeInTransition.setOnFinished(new EventHandler<ActionEvent>()
	{

	    @Override
	    public void handle(ActionEvent event)
	    {
		imageView.setVisible(false);
	    }
	});

	//if hidden, we need to go about this smoothly. if still showing video
	if (mediaView.opacityProperty().getValue() != 0)
	{
	    //Set what to do after fadeout
	    fadeOutTransition.setOnFinished(new EventHandler<ActionEvent>()
	    {

		@Override
		public void handle(ActionEvent event)
		{
		    initVideo();
		    fadeInTransition.play();
		}
	    });




	    imageView.setVisible(true);
	    fadeOutTransition.play();
	}
	else
	{
	    //video was previously hidden. So don't do fadeout
	    initVideo();
	    fadeInTransition.play();
	}
    }

    public ImageView getImageView()
    {
	return imageView;
    }

    public WebView getWebView()
    {
	return webView;
    }
    private String webContent = "";

    private void initWeb()
    {
	
	if (webView == null)
	{
	    webView = new WebView();

	    webView.setLayoutX(0);
	    webView.setLayoutY(0);
	    webView.setMinWidth(ProjectorConstants.SCREENWIDTH);
	    webView.setMinHeight(ProjectorConstants.SCREENHEIGHT);

	    webView.setOpacity(0);
	}

	//webView.getEngine().loadContent(webContent);
	webView.getEngine().load(webContent);
    }

    public String getWebContent()
    {
	return webContent;
    }

    public void setWebContent(String webContent)
    {
	this.webContent = webContent;
	initWeb();
	webView.setOpacity(1);
	if (mediaView.getOpacity() > 0)
	{
	    wasVideoPlaying = true;
	    hideVideo();
	}
	else
	{
	    wasVideoPlaying = false;
	}
    }
    boolean wasVideoPlaying = false;

    public void hideWebView()
    {
	
	if (wasVideoPlaying)
	{
	    if (mediaView.opacityProperty().getValue() == 0)
	    {
		mediaPlayer.play();
		fadeInTransition.setOnFinished(new EventHandler<ActionEvent>()
		{

		    @Override
		    public void handle(ActionEvent event)
		    {
			webView.setOpacity(0);
		    }
		});
		fadeInTransition.play();

	    }
	}
	else{
	    webView.setOpacity(0);
	}

    }
}
