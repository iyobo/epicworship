/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.data;

/**
 *
 * @author bufflogic
 */
public class NamedString {
    public String name;
    public String text;

    public NamedString(String name) {
        this.name = name;
    }

    
    public NamedString(String name, String text) {
        this.name = name;
        this.text = text;
    }
    
    @Override
    public String toString()
    {
        if(text!=null && !text.isEmpty())
            return text;
        else
            return name;
    }
}
