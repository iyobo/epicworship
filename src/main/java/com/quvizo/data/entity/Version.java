/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.data.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bufflogic
 */
@Entity
@Table(name = "VERSION")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Version.findAll", query = "SELECT v FROM Version v"),
    @NamedQuery(name = "Version.findById", query = "SELECT v FROM Version v WHERE v.id = :id"),
    @NamedQuery(name = "Version.findByNumber", query = "SELECT v FROM Version v WHERE v.number = :number")
})
public class Version implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "NUMBER")
    private Integer number;

    public Version()
    {
    }

    public Version(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber(Integer number)
    {
        this.number = number;
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
        if(!(object instanceof Version))
        {
            return false;
        }
        Version other = (Version) object;
        if((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.bufflogic.data.entity.Version[ id=" + id + " ]";
    }
    
}
