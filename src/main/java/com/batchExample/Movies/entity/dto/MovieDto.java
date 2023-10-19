package com.batchExample.Movies.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {

    @NotEmpty
    @Size(min = 5, max = 255)
    private String title;

    @NotEmpty
    @Size(min = 2, max = 2)
    private String originalLanguage;

    @NotNull
    private String releaseDate;

    @Positive
    private double popularity;

    @PositiveOrZero
    private Long budget;

    @PositiveOrZero
    private Long revenue;

    @Positive
    private int runtime;

    @NotEmpty
    @Size(max = 20)
    private String status;

    @NotEmpty
    private String genres;

}

