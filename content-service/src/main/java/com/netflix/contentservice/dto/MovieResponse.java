package com.netflix.contentservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.netflix.contentservice.model.Genre;
import com.netflix.contentservice.model.MaturityRating;
import com.netflix.contentservice.model.VideoStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponse {

    private UUID id;
    private String title;
    private String description;
    private Set<Genre> genres;
    private String director;
    private String cast;
    private LocalDate releaseDate;
    private Double rating;
    private String posterUrl;
    private Integer durationMinutes;
    private MaturityRating maturityRating;
    private String hlsUrl;
    private VideoStatus videoStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
