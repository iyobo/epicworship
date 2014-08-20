/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.quvizo.wrappers;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author BuffLogic
 */
public class BookMeta {
    
    
    ArrayList<Integer> index = new ArrayList<Integer>();

    public BookMeta()
    {
	
    }

    public int getVerseCount(int chapter)
    {
	return index.get(chapter);
    }

    public ArrayList<Integer> getIndex()
    {
	return index;
    }
    
    
    
}
