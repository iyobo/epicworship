/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.data.tablemodels;

import com.quvizo.projector.pages.PXVideoPage;
import com.quvizo.ui.ProjectorView;

/**
 *
 * @author BuffLogic
 */
public class LiveVideoItem extends LiveTableItem
{

    public LiveVideoItem(String name, String type, String content)
    {
	super(name, type, content);
    }
    
    
    @Override
    public void pushToProjector()
    {
	ProjectorView.getInstance().pushScene(new PXVideoPage(getContentProperty().getValue()));
    }
    
}
