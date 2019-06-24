package com.packtpub.springdatademo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by renriquez on 22/11/17.
 */
@Entity
public class Country {

    @Id
    @Column(name = "id_country")
    private Integer id;
    private String name;

    public Country() {
        super();
    }

    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country [id: " + id + " name: " + name + " ]";
    }
}
