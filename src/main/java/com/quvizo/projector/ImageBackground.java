/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.projector;


import com.quvizo.config.EpicSettings;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.quvizo.universal.ProjectorConstants;

/**
 *
 * @author peki
 */
public class ImageBackground
{

    private static final Logger logger = Logger.getLogger(ImageBackground.class.getName());
    private String path = "http://www.dvd-ppt-slideshow.com/images/ppt-background/background-38.jpg";
    private Image img;
    private ImageView backImageView;
    
    public static ImageBackground instance = null;

    public ImageBackground()
    {
	init();
        instance = this;
    }

    private void init()
    {
	try
	{
            String useDef = EpicSettings.getSystemDefault();
            String pathCon = EpicSettings.getScreenImagePath();
            if(useDef.equalsIgnoreCase("yes")){
            path = getClass().getResource("/media/bkg.jpg").toString();
            } else {
            path = pathCon;    
            }
            refresh();
	    
	}
	catch (Exception e)
	{
	    logger.warning("Could not Load Image Background.");
	}
    }

    public void hide()
    {
	backImageView.setVisible(false);
    }

    public void show()
    {
	backImageView.setVisible(true);
    }

    public Image getImg()
    {
	return img;
    }

    public ImageView getImgView()
    {
	return backImageView;
    }

    public String getPath()
    {
	return path;
    }

    public void refresh()
    {
            img = new Image(path);

	    // Create MediaView from MediaPlayer. View must not be recreated as it is attached externally.
	    if (backImageView == null)
	    {
		backImageView = new ImageView();
		backImageView.setImage(img);
		backImageView.setFitWidth(ProjectorConstants.SCREENWIDTH);
		backImageView.setFitHeight(ProjectorConstants.SCREENHEIGHT);
		backImageView.setSmooth(true);
		backImageView.setCache(true);
	    }
	    else
	    {
		backImageView.setImage(img);
	    }
    }
    
    public void setPath(String path)
    {
	this.path = path;
	refresh();
    }
    
    
}
