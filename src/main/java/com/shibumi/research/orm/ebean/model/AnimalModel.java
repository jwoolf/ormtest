package com.shibumi.research.orm.ebean.model;


import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="animal")
public class AnimalModel {

    /*
    see if i can use different interface versions of @Entity for different classes (probably not)
    see if i can programmatically (or declaratively) point both hibernate and ebean to this class
    if so, independently insert animals into table. then test programmatic transaction propagation
    across hibernate and ebean, ebean to hibernate, e->h->e, h->e->h.
    for example, open txn, insert via h, insert via e, fail via e, rollback via e, --> no inserts.
    for example, open txn, insert via h, insert via e, fail via e, rollback via , --> no inserts.
    and so on.
     */


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long m_id;

    @Column(name = "species")
    private String m_species;

    @Column(name = "gender")
    private String m_gender;

    @Column(name = "name")
    private String m_name;

    public AnimalModel(String species, String gender, String name) {
        m_species = species;
        m_gender = gender;
        m_name = name;
    }

    public long getId()
    {
        return m_id;
    }

    public void setId( long id )
    {
        m_id = id;
    }

    public String getSpecies()
    {
        return m_species;
    }

    public void setSpecies( String species )
    {
        m_species = species;
    }

    public String getGender()
    {
        return m_gender;
    }

    public void setGender( String gender )
    {
        m_gender = gender;
    }

    public String getName()
    {
        return m_name;
    }

    public void setName( String name )
    {
        m_name = name;
    }


    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final AnimalModel other = (AnimalModel) obj;
        return Objects.equals(m_id, other.m_id)
                && Objects.equals(m_species, other.m_species)
                && Objects.equals(m_gender, other.m_gender)
                && Objects.equals(m_name, other.m_name);

    }


    @Override
    public int hashCode() {
        return Objects.hash(m_id, m_species, m_gender, m_name);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", m_id)
                .add("species", m_species)
                .add("gender", m_gender)
                .add("name", m_name)
                .toString();
    }


}