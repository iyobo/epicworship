/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.quvizo.wrappers;

/**
 *
 * @author BuffLogic
 */
public class BibleScripture {
    String book;
    int chapter;
    int fromverse;
    int toverse;

    public BibleScripture()
    {
    }

    public BibleScripture(String book, int chapter, int fromverse, int toverse)
    {
	this.book = book;
	this.chapter = chapter;
	this.fromverse = fromverse;
	this.toverse = toverse;
    }
    
    public void set(String book, int chapter, int fromverse, int toverse)
    {
	this.book = book;
	this.chapter = chapter;
	this.fromverse = fromverse;
	this.toverse = toverse;
    }

    public String getBook()
    {
	return book;
    }

    public void setBook(String book)
    {
	this.book = book;
    }

    public int getChapter()
    {
	return chapter;
    }

    public void setChapter(int chapter)
    {
	this.chapter = chapter;
    }

    public int getFromverse()
    {
	return fromverse;
    }

    public void setFromverse(int fromverse)
    {
	this.fromverse = fromverse;
    }

    public int getToverse()
    {
	return toverse;
    }

    public void setToverse(int toverse)
    {
	this.toverse = toverse;
    }
    
    public String getName()
    {
	StringBuffer buf = new StringBuffer(book).append(" ").append(chapter);
	
	if(fromverse != 0)
	    buf.append(" : ").append(fromverse);
	
	if(toverse!=fromverse)
	    buf.append(" - ").append(toverse);
	
	return buf.toString();
    }
    
    public boolean isUsable()
    {
        if(this.book!=null && !this.book.isEmpty() && this.chapter>0)
            return true;
        else
            return false;
    }
}
