/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.quvizo.data;

import java.util.List;
import com.quvizo.data.entity.Asset;
import com.quvizo.data.entity.PresentationAssets;
import com.quvizo.data.tablemodels.LiveSongItem;
import com.quvizo.data.tablemodels.LiveTableItem;
import com.quvizo.data.tablemodels.LiveVideoItem;
import com.quvizo.data.tablemodels.LiveWebHTMLItem;
import com.quvizo.data.tablemodels.LiveWebURLItem;
import com.quvizo.data.tablemodels.LiveWebVideoItem;
import com.quvizo.universal.BibleKeeper;
import com.quvizo.universal.EntityOverlord;
import com.quvizo.wrappers.BibleScripture;

/**
 *
 * @author peki
 */
public class PresentationAssetWrapper
{

    String name = "UnKnown";
    PresentationAssets presentationAsset;
    Asset asset;

    public PresentationAssetWrapper(PresentationAssets presentationItem)
    {
	this.presentationAsset = presentationItem;
	loadItem();
    }

    private void loadItem()
    {
	int assid = presentationAsset.getAssetid();
	if (assid != 0)
	{
	    asset = EntityOverlord.getInstance().getEm().find(Asset.class, assid);
	    name = asset.getName();
	}
	else
	{
	    BibleScripture scripture = new BibleScripture(presentationAsset.getBook(), presentationAsset.getChapter(), presentationAsset.getFromverse(), presentationAsset.getToverse());
	    name = scripture.getName();
            
           
            content = BibleKeeper.getInstance().getScripture(scripture);
            
	}
    }
    private String content = "";

    @Override
    public String toString()
    {
	return name;
    }

    public Asset getItem()
    {
	return asset;
    }

    public PresentationAssets getPresentationAsset()
    {
	return presentationAsset;
    }

    /**
     * Generate pages for this asset and attach them to the list parameter
     * @param liveItems 
     */
    public void generatePages(List<LiveTableItem> liveItems)
    {
	if(asset==null) //A bible Scripture
        {
            String[] paragraphs = content.split("\n\n");

	    for (String paragraph : paragraphs)
	    {
		if (!paragraph.isEmpty()) //we don't quite care much for excessive blank lines
		{
		    liveItems.add(new LiveSongItem(name, "Scripture", paragraph));
		}
	    }
        }
        else if (asset.getType().equals(EntityOverlord.ASSETTYPESONG))
	{
	    String[] paragraphs = asset.getContent().split("\n\n");

	    for (String paragraph : paragraphs)
	    {
		if (!paragraph.isEmpty()) //we don't quite care much for excessive blank lines
		{
		    liveItems.add(new LiveSongItem(asset.getName(), "", paragraph));
		}
	    }
	}
	else if (asset.getType().equals(EntityOverlord.ASSETTYPEMEDIA))
	{
	   
	    liveItems.add(new LiveVideoItem(asset.getName(), asset.getType(), asset.getContent()));
            
	}
        else if (asset.getType().equals(EntityOverlord.ASSETTYPEHTML))
	{
	   
	    liveItems.add(new LiveWebHTMLItem(asset.getName(), asset.getType(), asset.getContent()));

	}
        else if (asset.getType().equals(EntityOverlord.ASSETTYPEWEBURL))
	{
	   
	    liveItems.add(new LiveWebURLItem(asset.getName(), asset.getType(), asset.getContent()));

	}
        
	//empty line
	liveItems.add(new LiveSongItem("", "", ""));
       
    }

    public String getContent()
    {
        return content;
    }
    
    
    
}
