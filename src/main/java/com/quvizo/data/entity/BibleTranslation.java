/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.data.entity;

import com.quvizo.universal.EntityOverlord;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bufflogic
 */
@Entity
@XmlRootElement
@NamedQueries(
        {
    @NamedQuery(name = "BibleTranslation.findAll", query = "SELECT a FROM BibleTranslation a"),
    @NamedQuery(name = "BibleTranslation.findByName", query = "SELECT a FROM BibleTranslation a WHERE a.name = :name"),})
public class BibleTranslation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String path;
    @Column(nullable = true)
    private String pathtype;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BibleTranslation() {
    }

    public BibleTranslation(String name, String path, String pathtype) {
        this.name = name;
        this.path = path;
        this.pathtype = pathtype;
    }

    public String getPathtype() {
        return pathtype;
    }

    public void setPathtype(String pathtype) {
        this.pathtype = pathtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BibleTranslation)) {
            return false;
        }
        BibleTranslation other = (BibleTranslation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public static List<BibleTranslation> getAll() {
        return EntityOverlord.getInstance().getEm().createNamedQuery("BibleTranslation.findAll").getResultList();
    }

    public static BibleTranslation getBibleTranslation(String name) {
        try {
            return (BibleTranslation) EntityOverlord.getInstance().getEm().createNamedQuery("BibleTranslation.findByName").setParameter("name", name).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
