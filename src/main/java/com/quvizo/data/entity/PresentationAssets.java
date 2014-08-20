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
@Table(name = "PRESENTATION_ASSETS", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "PresentationAssets.findAll", query = "SELECT p FROM PresentationAssets p"),
    @NamedQuery(name = "PresentationAssets.findById", query = "SELECT p FROM PresentationAssets p WHERE p.id = :id"),
    @NamedQuery(name = "PresentationAssets.findByPid", query = "SELECT p FROM PresentationAssets p WHERE p.pid = :pid ORDER BY p.addedOn"),
    @NamedQuery(name = "PresentationAssets.findByAssetid", query = "SELECT p FROM PresentationAssets p WHERE p.assetid = :assetid"),
    @NamedQuery(name = "PresentationAssets.findByAddedOn", query = "SELECT p FROM PresentationAssets p WHERE p.addedOn = :addedOn"),
    @NamedQuery(name = "PresentationAssets.findByBook", query = "SELECT p FROM PresentationAssets p WHERE p.book = :book"),
    @NamedQuery(name = "PresentationAssets.findByChapter", query = "SELECT p FROM PresentationAssets p WHERE p.chapter = :chapter"),
    @NamedQuery(name = "PresentationAssets.findByFromverse", query = "SELECT p FROM PresentationAssets p WHERE p.fromverse = :fromverse"),
    @NamedQuery(name = "PresentationAssets.findByToverse", query = "SELECT p FROM PresentationAssets p WHERE p.toverse = :toverse")
})
public class PresentationAssets implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "PID")
    private Integer pid;
    @Column(name = "ASSETID")
    private Integer assetid;
    @Column(name = "ADDED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedOn;
    @Column(name = "BOOK", length = 255)
    private String book;
    @Column(name = "CHAPTER")
    private Integer chapter;
    @Column(name = "FROMVERSE")
    private Integer fromverse;
    @Column(name = "TOVERSE")
    private Integer toverse;

    public PresentationAssets()
    {
    }

    public PresentationAssets(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getPid()
    {
        return pid;
    }

    public void setPid(Integer pid)
    {
        this.pid = pid;
    }

    public Integer getAssetid()
    {
        return assetid;
    }

    public void setAssetid(Integer assetid)
    {
        this.assetid = assetid;
    }

    public Date getAddedOn()
    {
        return addedOn;
    }

    public void setAddedOn(Date addedOn)
    {
        this.addedOn = addedOn;
    }

    public String getBook()
    {
        return book;
    }

    public void setBook(String book)
    {
        this.book = book;
    }

    public Integer getChapter()
    {
        return chapter;
    }

    public void setChapter(Integer chapter)
    {
        this.chapter = chapter;
    }

    public Integer getFromverse()
    {
        return fromverse;
    }

    public void setFromverse(Integer fromverse)
    {
        this.fromverse = fromverse;
    }

    public Integer getToverse()
    {
        return toverse;
    }

    public void setToverse(Integer toverse)
    {
        this.toverse = toverse;
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
        if (!(object instanceof PresentationAssets))
        {
            return false;
        }
        PresentationAssets other = (PresentationAssets) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "winningworship.data.entity.PresentationAssets[ id=" + id + " ]";
    }
    
}
