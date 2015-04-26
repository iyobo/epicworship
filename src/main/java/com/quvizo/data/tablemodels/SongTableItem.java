/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.data.tablemodels;

import javafx.beans.property.SimpleStringProperty;
import com.quvizo.data.entity.Asset;
import com.quvizo.universal.UI;

/**
 *
 * @author peki
 */
public class SongTableItem
{
    private final SimpleStringProperty title;
    private final SimpleStringProperty body;
    private Asset songAsset;

    public SongTableItem(Asset songAsset)
    {
        this.songAsset = songAsset;
        title = new SimpleStringProperty(songAsset.getName()) ;
        
	
	String bod = songAsset.getContent();

	if(bod.length()>UI.songTableMaxTextLength) //truncate
	{
	    body = new SimpleStringProperty(bod.substring(0, UI.songTableMaxTextLength-6)+"...");
	}
	else
	    body = new SimpleStringProperty(bod);
    }


    public Asset getSongAsset()
    {
        return songAsset;
    }

    public SimpleStringProperty getBody()
    {
	return body;
    }

    public SimpleStringProperty getTitle()
    {
	return title;
    }

    
    
}
