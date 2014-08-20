/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.projector.nodes;

import java.util.logging.Logger;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.Effect;

/**
 * Represents any displayable object that will be displayed in the projector.
 * It is essentially a container for Node Objects.
 * Should be part of a PXPage.
 * @author peki
 */
public abstract class PXNode 
{
    protected Transition entryTransition;
    protected Transition exitTransition;
    
    protected static Logger logger = Logger.getLogger(PXNode.class.getName());
    
    protected Group group;
    
    protected Node node;

 
    public Transition getEntryTransition()
    {
        return entryTransition;
    }

    public void setEntryTransition(Transition entryTransition)
    {
        this.entryTransition = entryTransition;
    }

    public Transition getExitTransition()
    {
        return exitTransition;
    }

    public void setExitTransition(Transition exitTransition)
    {
        this.exitTransition = exitTransition;
        
        //be sure to remove this node from scene graph after animation ends
        this.exitTransition.setOnFinished(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event)
            {
                removeFromGroup();
            }
            
        });
    }

    public Node getNode()
    {
        return node;
    }

    
    /**
     * Tell me what group I should populate with myself
     * @param group 
     */
    public void addToGroup(Group group)
    {
        if(group!=null && node!=null)
        {
            this.group = group;
            group.getChildren().add(node);
        }else
        {
            logger.severe("Either group or node is null. Cannot add to Group.");
        }
    }
    
    /**
     * 
     */
    public void removeFromGroup()
    {
        if(group!=null && node!=null)
        {
            //if added to a group
            group.getChildren().remove(node);
        }else
        {
            logger.severe("Either group or node is null. Cannot remove from Group.");
        }
    }


}
