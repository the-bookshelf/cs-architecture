package com.packtpub.moviesservice.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Movie {

    @Id
    @Column(name = "id_movie")
    private Integer id;
    private String name;

}
