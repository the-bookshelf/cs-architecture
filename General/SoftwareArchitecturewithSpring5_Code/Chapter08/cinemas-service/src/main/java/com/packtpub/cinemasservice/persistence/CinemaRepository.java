package com.packtpub.cinemasservice.persistence;

import com.packtpub.cinemasservice.domain.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {

}
