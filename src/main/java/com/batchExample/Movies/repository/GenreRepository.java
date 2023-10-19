package com.batchExample.Movies.repository;

import com.batchExample.Movies.entity.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

//    @Query("SELECT g FROM Genre g WHERE g.name = :name")
    Optional<Genre> findByName(String name);
}
