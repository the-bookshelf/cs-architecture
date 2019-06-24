package com.packtpub.cinemasservice.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Cinema {

    @Id
    @Column(name = "id_cinema")
    private Integer id;
    private String name;

    @Transient
    private Movie[] availableMovies;
}
