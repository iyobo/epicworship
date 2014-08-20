/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.data.tablemodels;

import java.util.jar.Attributes;
import com.quvizo.projector.pages.PXSongPage;
import com.quvizo.projector.pages.PXWebHTMLPage;
import com.quvizo.projector.pages.PXWebVideoPage;
import com.quvizo.ui.ProjectorView;

/**
 *
 * @author BuffLogic
 */
public class LiveWebHTMLItem extends LiveTableItem
{

    public LiveWebHTMLItem(String name, String type, String content)
    {
	super(name, type, content);
    }
    
    
    @Override
    public void pushToProjector()
    {
	ProjectorView.getInstance().pushScene(new PXWebHTMLPage(getContentProperty().getValue()));
    }
    
}
