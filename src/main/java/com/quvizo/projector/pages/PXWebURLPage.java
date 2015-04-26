/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.projector.pages;

import com.quvizo.projector.DynamicBackground;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import com.quvizo.projector.nodes.PXtextNode;
import com.quvizo.ui.modes.BrowserMode;
import com.quvizo.universal.EpicOverlord;
import com.quvizo.universal.ProjectorConstants;
import com.quvizo.universal.UI;

/**
 * A song page includes a Song body and a song title
 *
 * @author peki
 */
public class PXWebURLPage extends PXPage
{

    String htmlcontent = "";

    public PXWebURLPage(String html)
    {
        htmlcontent = html;
	
    }

    @Override
    protected void onTriggered()
    {
	super.onTriggered();
	
	//Now flip to WebView, feed it with HTML, and watch it go. Possibly also spawn an undecorated control pad for video in director Window.
	//DynamicBackground.instance.setWebContent(htmlcontent); //webview is ideal, but doesn't support flash which is necessary so leave for now
	
	EpicOverlord.getInstance().showWebView(htmlcontent,BrowserMode.URL);
    }

    @Override
    protected void onExit()
    {
	super.onExit();
	
	//DynamicBackground.instance.hideWebView();
	EpicOverlord.getInstance().hideWebView();
    }
    
    
}
