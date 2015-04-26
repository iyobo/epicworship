/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.projector.pages;

import com.quvizo.projector.MediaProjector;


/**
 * A song page includes a Song body and a song title
 *
 * @author peki
 */
public class PXVideoPage extends PXPage
{
    String path;
    public PXVideoPage(String path)
    {
        this.path = path;
        
    }

    @Override
    protected void onTriggered()
    {
	super.onTriggered();
	launchMedia();
    }

    public void launchMedia()
    {
        MediaProjector.getInstance().playVideo(path);
    }

    @Override
    protected void onExit()
    {
        MediaProjector.getInstance().hideVideo();
    }
    
    
}
