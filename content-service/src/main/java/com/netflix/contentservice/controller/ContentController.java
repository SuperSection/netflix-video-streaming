package com.netflix.contentservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.contentservice.dto.MovieRequest;
import com.netflix.contentservice.dto.MovieResponse;
import com.netflix.contentservice.service.ContentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping
    public Page<MovieResponse> getMovies(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<String> genres,
            Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return contentService.searchMovies(search, pageable);
        }
        if (genres != null && !genres.isEmpty()) {
            return contentService.getMoviesByGenre(genres, pageable);
        }
        return contentService.getAllMovies(pageable);
    }

    @GetMapping("/{id}")
    public MovieResponse getMovieById(@PathVariable UUID id) {
        return contentService.getMovieById(id);
    }

    @GetMapping("/genre/{genre}")
    public Page<MovieResponse> getMoviesByGenre(@PathVariable String genre, Pageable pageable) {
        return contentService.getMoviesBySingleGenre(genre, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse addMovie(@Valid @RequestBody MovieRequest request) {
        return contentService.addMovie(request);
    }

    @PutMapping("/{id}")
    public MovieResponse updateMovie(@PathVariable UUID id, @Valid @RequestBody MovieRequest request) {
        return contentService.updateMovie(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable UUID id) {
        contentService.deleteMovie(id);
    }
}
