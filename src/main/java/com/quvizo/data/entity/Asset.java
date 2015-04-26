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
@Table(name = "ASSET", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Asset.findAll", query = "SELECT a FROM Asset a"),
    @NamedQuery(name = "Asset.findById", query = "SELECT a FROM Asset a WHERE a.id = :id"),
    @NamedQuery(name = "Asset.findByAddedOn", query = "SELECT a FROM Asset a WHERE a.addedOn = :addedOn"),
    @NamedQuery(name = "Asset.findByType", query = "SELECT a FROM Asset a WHERE a.type = :type"),
    @NamedQuery(name = "Asset.findByTypeOrType", query = "SELECT a FROM Asset a WHERE a.type = :type OR a.type= :typeb"),
    @NamedQuery(name = "Asset.findByTypeSearch", query = "SELECT a FROM Asset a WHERE a.type = :type AND (LOWER(a.name) like LOWER(:search) OR LOWER(a.content) like LOWER(:search) OR LOWER(a.author) like LOWER(:search))"),
    @NamedQuery(name = "Asset.findByName", query = "SELECT a FROM Asset a WHERE a.name = :name"),
    @NamedQuery(name = "Asset.findByAuthor", query = "SELECT a FROM Asset a WHERE a.author = :author"),
    @NamedQuery(name = "Asset.findByUpdatedOn", query = "SELECT a FROM Asset a WHERE a.updatedOn = :updatedOn"),
    @NamedQuery(name = "Asset.findByContent", query = "SELECT a FROM Asset a WHERE a.content LIKE :content")
   // @NamedQuery(name = "Asset.findByContentandName", query = "SELECT a FROM Asset a WHERE a.name = :name AND CAST(a.content AS VARCHAR(65000)) = :content")
})
public class Asset implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "ADDED_ON")
    @Temporal(TemporalType.DATE)
    private Date addedOn;
    @Basic(optional = false)
    @Column(name = "TYPE", nullable = false, length = 8)
    private String type;
    @Basic(optional = false)
    @Column(name = "NAME", nullable = false, length = 255)
    private String name;
    @Basic(optional = false)
    @Column(name = "CONTENT", nullable = false, length=32000)
    private String content;
    @Column(name = "UPDATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;
    @Column(name = "AUTHOR", nullable = true, length = 255)
    private String author;

    public Asset()
    {
    }

    public Asset(Integer id)
    {
        this.id = id;
    }

    public Asset(Integer id, String type, String name, String content)
    {
        this.id = id;
        this.type = type;
        this.name = name;
        this.content = content;
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

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Date getUpdatedOn()
    {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn)
    {
        this.updatedOn = updatedOn;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
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
        if (!(object instanceof Asset))
        {
            return false;
        }
        Asset other = (Asset) object;
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
