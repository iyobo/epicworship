/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.quvizo.wrappers;

import java.util.LinkedHashMap;

/**
 * Contains a bible in it's translation.
 * @author BuffLogic
 */
public class BibleCodex extends LinkedHashMap<String, BibleCodexChapters>
{
    private String Translation;

    public BibleCodex(String Translation)
    {
	super();
	this.Translation = Translation;
    }
    
    
}
