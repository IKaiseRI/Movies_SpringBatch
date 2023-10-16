package com.batchExample.Movies.entity.movie;


import com.batchExample.Movies.entity.genre.Genre;
import com.batchExample.Movies.entity.movie.characteristics.Language;
import com.batchExample.Movies.entity.movie.characteristics.Status;
import com.batchExample.Movies.utils.EntityUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "original_language")
    private Language originalLanguage;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private double popularity;
    private Long budget;
    private Long revenue;
    private int runtime;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    public Movie(String title, String originalLanguage, String releaseDate, double popularity, Long budget, Long revenue, int runtime, String status, Set<Genre> genres) {
        this.title = title;
        setOriginalLanguage(originalLanguage);
        setReleaseDate(EntityUtils.convertToMySQLDate(releaseDate));
        this.popularity = popularity;
        this.budget = budget;
        this.revenue = revenue;
        this.runtime = runtime;
        setStatus(status);
        this.genres = genres;
    }

    public void setOriginalLanguage(String originalLanguage) {
        if (isValidLanguage(originalLanguage)) {
            this.originalLanguage = Language.valueOf(originalLanguage);
        } else {
            throw new IllegalArgumentException(
                    new StringBuilder().append("The provided language: ")
                            .append(originalLanguage).append(" is not valid")
                            .toString()
            );
        }
    }

    public static boolean isValidLanguage(String originalLanguage) {
        return List.of(Language.values()).contains(Language.valueOf(originalLanguage));
    }

    public void setReleaseDate(LocalDate releaseDate) {
        if (isValidDate(releaseDate)) {
            this.releaseDate = releaseDate;
        } else {
            throw new IllegalArgumentException(
                    new StringBuilder().append("The provided date: ")
                            .append(releaseDate).append(" is not valid")
                            .toString()
            );
        }
    }

    public static boolean isValidDate(LocalDate localDate) {
        return !localDate.isAfter(LocalDate.now());
    }

    public void setStatus(String status) {
        if (isValidStatus(status)) {
            this.status = Status.fromString(status);
        } else {
            throw new IllegalArgumentException(
                    new StringBuilder().append("The provided status: ")
                            .append(status).append(" is not valid")
                            .toString()
            );
        }
    }

    public static boolean isValidStatus(String status) {
        return List.of(Status.values()).contains(Status.fromString(status));
    }
}
