/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.projector.pages;

import java.util.ArrayList;
import javafx.animation.ParallelTransition;
import javafx.scene.Group;
import com.quvizo.projector.nodes.PXNode;

/**
 * Projection Scene. Is typically just an ArrayList of PXNodes.
 * @author peki
 */
public abstract class  PXPage extends ArrayList<PXNode>
{
    
    public void prepareExitTransitions(ParallelTransition exitSequence)
    {
	for(PXNode node:this)
	{
	    if(node.getExitTransition()!=null)
            {
               exitSequence.getChildren().add(node.getExitTransition());
            }
            else
            {
                node.removeFromGroup();
                node = null;
            }
	}
	
	onExit();
    }
    
    public void prepareEntryTransitions(ParallelTransition entrySequence)
    {
	for(PXNode node:this)
	{
	    if(node.getEntryTransition()!= null)
	    {
                entrySequence.getChildren().add(node.getEntryTransition());
	    }
	}
    }

    /**
     * Called externally to spill contents of this page into JFX canvas
     * @param group 
     */
    public void addToGroup(Group group)
    {
	for(PXNode node:this)
	{
	    node.addToGroup(group);
	}
	
	onTriggered();
    }

    /**
     * Override this to force any other action aside from normal entry effects <br>
     * and drawing that you want to execute upon after an item is selected in the live table. <br>
     * e.g launching a video file externally, etc.
     * 
     * <br>
     * <br>
     */
    protected void onTriggered()
    {
	
    }

    /**
     * Override to do perform an action when exiting this page.
     */
    protected void onExit()
    {
	
    }
   
}
