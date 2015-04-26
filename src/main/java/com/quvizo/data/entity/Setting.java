/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.data.entity;

import com.quvizo.universal.EntityOverlord;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author peki
 */
@Entity
@Table(name = "SETTING", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Setting.findAll", query = "SELECT a FROM Setting a"),
    @NamedQuery(name = "Setting.findByName", query = "SELECT a FROM Setting a WHERE a.name = :name"),
})
public class Setting implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "NAME", nullable = false, length = 255)
    private String name;
    
    @Column( name = "STRINGDATA", nullable = true)
    private String stringData;
    
    @Column(name = "INTDATA", nullable = true)
    private int intData;
    

    public Setting()
    {
    }

    public Setting(String name, String stringData, int intData)
    {
        this.name = name;
        this.stringData = stringData;
        this.intData = intData;
    }

    public Setting(String name, String stringData)
    {
        this.name = name;
        this.stringData = stringData;
    }

    public Setting(String name, int intData)
    {
        this.name = name;
        this.intData = intData;
    }

    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getIntData()
    {
        return intData;
    }

    public String getStringData()
    {
        return stringData;
    }

    public void setIntData(int intData)
    {
        this.intData = intData;
    }

    public void setStringData(String stringData)
    {
        this.stringData = stringData;
    }
    

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Setting))
        {
            return false;
        }
        Setting other = (Setting) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return getName();
    }
    
    public static List<Setting> getAll()
    {
        return EntityOverlord.getInstance().getEm().createNamedQuery("BibleTranslation.findAll").getResultList();
    }
    
    public static Setting getSetting(String name)
    {
        try{
            return (Setting)EntityOverlord.getInstance().getEm().createNamedQuery("Setting.findByName").setParameter("name", name).getSingleResult();
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    public static void saveSetting(String name, Object value)
    {
        Setting s = new Setting();
        s.setName(name);
        if(value instanceof String)
        {
            s.setStringData(value.toString());
        }
        else
            s.setIntData((Integer)value);
        
        EntityOverlord.getInstance().getEm().getTransaction().begin();
        if(getSetting(name)==null)
        {
            EntityOverlord.getInstance().getEm().persist(s);
        }
        else
            EntityOverlord.getInstance().getEm().merge(s);
        
        EntityOverlord.getInstance().getEm().getTransaction().commit();
    }

}
