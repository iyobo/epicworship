/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.ui;

import com.quvizo.projector.DynamicBackground;
import javafx.animation.ParallelTransition;
import javafx.scene.Group;
import com.quvizo.projector.ImageBackground;
import com.quvizo.projector.MediaProjector;
import com.quvizo.projector.pages.PXPage;
import com.quvizo.universal.ProjectorConstants;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Projector View
 * @author peki
 */
public class ProjectorView
{
    private static ProjectorView instance;
    public static ProjectorView getInstance()
    {
        return instance;
    }
    public static ProjectorView initInstance(Group group,double width, double height)
    {
        if(instance==null)
            instance = new ProjectorView(group,width,height);
        
        return instance;
    }
    
    private Group staticGroup;//Do not add anything to this scene unless you know what you are doing.
    private Group group; //group that represents each pxscene that gets pushed. This scene will be cleared frequently to make way for new PXScenes.
    private DynamicBackground motionBackground;
    private ImageBackground imageBackground;
    private Text textAlert;
    private Rectangle bgAlert;   
    
    private ProjectorView(Group uiGroup,double width, double height)
    {
        this.staticGroup = uiGroup;
        group = new Group();

        //First add an imageView to the scene
        
        // Add the MotionBack to the Scene. This component will never be removed.
        motionBackground = new DynamicBackground();
        
	
	imageBackground = new ImageBackground();
        
	staticGroup.getChildren().add(imageBackground.getImgView());
	staticGroup.getChildren().add(motionBackground.getImageView());
	staticGroup.getChildren().add(motionBackground.getWebView());
        staticGroup.getChildren().add(motionBackground.getMediaView());
        staticGroup.getChildren().add(MediaProjector.getInstance().getMediaView());
        //New feature Alert
                textAlert = new Text();
        textAlert.setX(0);
        textAlert.setY(((ProjectorConstants.SCREENHEIGHT / 10)/4)*3);        
        textAlert.setFont(Font.font("Arial", 46));
        textAlert.setFill(Color.WHITE);        
        textAlert.setVisible(false);

        bgAlert = new Rectangle();
        bgAlert.setX(0);
        bgAlert.setY(0);
        bgAlert.setWidth(ProjectorConstants.SCREENWIDTH);
        bgAlert.setHeight(ProjectorConstants.SCREENHEIGHT / 10);
        bgAlert.setFill(Color.BLACK);
        bgAlert.setVisible(false);
        staticGroup.getChildren().add(bgAlert);
        staticGroup.getChildren().add(textAlert);
	
        initComponents();
	staticGroup.getChildren().add(group);
	
    }

    //Populate DirectorView User Interface with components.
    private void initComponents()
    {

        //PXSongPage startupSongPage = new PXSongPage("Lorem Title", "Learn how to create animation in JavaFX, including timelines, transitions, key frames ... Example 2 shows a code snippet for a path transition that is applied to a rectangle.");
       
        //add to scenegraph
        //pushScene(startupSongPage);
        
    }

    
    private PXPage currentPage;
    
    public void pushScene(PXPage newPage)
    {
        ParallelTransition sequence = new ParallelTransition();
        
	if(currentPage!=null)
	{
	    currentPage.prepareExitTransitions(sequence);
	}
	
	currentPage = newPage;
	
	currentPage.addToGroup(group);
        currentPage.prepareEntryTransitions(sequence);
        
        sequence.play();
	
    }

    public ImageBackground getImageBackground()
    {
        return imageBackground;
    }

    public DynamicBackground getMotionBackground()
    {
        return motionBackground;
    }

    public Text getTextAlert() {
        return textAlert;
    }

    public Rectangle getBgAlert() {
        return bgAlert;
    }

    
}
