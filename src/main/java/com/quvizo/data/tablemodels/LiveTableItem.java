/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.data.tablemodels;

import javafx.beans.property.SimpleStringProperty;
import com.quvizo.data.entity.Asset;

/**
 *
 * @author peki
 */
public abstract class LiveTableItem
{
    private final SimpleStringProperty nameProperty;
    private final SimpleStringProperty typeProperty;
    private final SimpleStringProperty contentProperty;
    

    public LiveTableItem(String name, String type, String content)
    {
        nameProperty = new SimpleStringProperty(name) ;
        typeProperty = new SimpleStringProperty(type);
	contentProperty = new SimpleStringProperty(content);
    }

    public SimpleStringProperty getContentProperty()
    {
	return contentProperty;
    }

    public SimpleStringProperty getNameProperty()
    {
	return nameProperty;
    }

    public SimpleStringProperty getTypeProperty()
    {
	return typeProperty;
    }


    public  abstract void pushToProjector();
}
