/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.projector.pages;

import com.quvizo.config.EpicSettings;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import com.quvizo.projector.nodes.PXtextNode;
import com.quvizo.universal.ProjectorConstants;
import com.quvizo.universal.UI;

/**
 * A song page includes a Song body and a song title
 *
 * @author peki
 */
public class PXSongPage extends PXPage
{

    protected PXtextNode titleNode;
    protected PXtextNode bodyNode;

    public PXSongPage(String title, String body)
    {
        //Test of font face(type) changed
        bodyNode = new PXtextNode(ProjectorConstants.getSongX(), ProjectorConstants.getSongY(), ProjectorConstants.getSongWidth(), ProjectorConstants.getSongHeight(), new Font(EpicSettings.getFontType(),UI.TEXTSIZE), body, TextAlignment.CENTER, VPos.CENTER);
        //Test for use or no the song title on projection
        if(EpicSettings.getSystemDefault().equalsIgnoreCase("yes") || EpicSettings.getLiveTitleSong().equalsIgnoreCase("yes")){
         titleNode = new PXtextNode(ProjectorConstants.getTitleX(), ProjectorConstants.getTitleY(), ProjectorConstants.getTitleWidth(), ProjectorConstants.getTitleHeight(), new Font(25d), title, TextAlignment.LEFT, VPos.TOP);
         this.add(titleNode);           
        }
        this.add(bodyNode);

        //Default animations?
        //create transition for song entry
        ParallelTransition entryseq = new ParallelTransition();
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), bodyNode.getNode());
        fadeTransition.setFromValue(0f);
        fadeTransition.setToValue(1f);
        fadeTransition.setCycleCount(1);
        
        TranslateTransition translateTransition =
            new TranslateTransition(Duration.millis(500),bodyNode.getNode());
        translateTransition.setFromX(0);
        translateTransition.setToX(ProjectorConstants.getTitleX());
        translateTransition.setCycleCount(1);
	
	
	
	
        entryseq.getChildren().addAll(translateTransition,fadeTransition);
        
        //set default entry behaviour
        bodyNode.setEntryTransition(entryseq);
        
        
        //create transition for song exit
        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(300), bodyNode.getNode());
        fadeOutTransition.setFromValue(1f);
        fadeOutTransition.setToValue(0f);
        fadeOutTransition.setCycleCount(1);

        //set default exit
        bodyNode.setExitTransition(fadeOutTransition);
        
    }

    public PXtextNode getBodyPX()
    {
        return bodyNode;
    }

    public PXtextNode getTitleNode()
    {
        return titleNode;
    }
}
