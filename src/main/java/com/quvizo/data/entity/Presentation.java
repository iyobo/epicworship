/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.quvizo.data.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author peki
 */
@Entity
@Table(name = "PRESENTATION", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Presentation.findAll", query = "SELECT p FROM Presentation p ORDER BY p.id DESC"),
    @NamedQuery(name = "Presentation.findById", query = "SELECT p FROM Presentation p WHERE p.id = :id"),
    @NamedQuery(name = "Presentation.findByAddedOn", query = "SELECT p FROM Presentation p WHERE p.addedOn = :addedOn"),
    @NamedQuery(name = "Presentation.findByName", query = "SELECT p FROM Presentation p WHERE p.name = :name"),
    @NamedQuery(name = "Presentation.findByUpdatedOn", query = "SELECT p FROM Presentation p WHERE p.updatedOn = :updatedOn")
})
public class Presentation implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "ADDED_ON", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedOn;
    @Basic(optional = false)
    @Column(name = "NAME", nullable = false, length = 150)
    private String name;
    @Basic(optional = false)
    @Column(name = "UPDATED_ON", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;

    public Presentation()
    {
    }

    public Presentation(Integer id)
    {
        this.id = id;
    }

    public Presentation(Integer id, Date addedOn, String name, Date updatedOn)
    {
        this.id = id;
        this.addedOn = addedOn;
        this.name = name;
        this.updatedOn = updatedOn;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Date getAddedOn()
    {
        return addedOn;
    }

    public void setAddedOn(Date addedOn)
    {
        this.addedOn = addedOn;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getUpdatedOn()
    {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn)
    {
        this.updatedOn = updatedOn;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Presentation))
        {
            return false;
        }
        Presentation other = (Presentation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
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
    
}
