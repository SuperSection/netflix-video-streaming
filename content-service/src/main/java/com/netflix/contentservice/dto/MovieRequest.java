package com.netflix.contentservice.dto;

import java.time.LocalDate;
import java.util.Set;

import com.netflix.contentservice.model.Genre;
import com.netflix.contentservice.model.MaturityRating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Genre is required")
    private Set<Genre> genres;

    private String director;
    private String cast;
    private LocalDate releaseDate;

    @Min(0)
    @Max(10)
    private Double rating;

    private String posterUrl;
    private Integer durationMinutes;

    @NotNull(message = "Maturity Rating is required")
    private MaturityRating maturityRating;
}
