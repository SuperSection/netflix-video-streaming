package com.netflix.contentservice.service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.netflix.contentservice.dto.MovieRequest;
import com.netflix.contentservice.dto.MovieResponse;
import com.netflix.contentservice.model.Genre;
import com.netflix.contentservice.model.Movie;
import com.netflix.contentservice.model.VideoStatus;
import com.netflix.contentservice.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentService {

    private final MovieRepository movieRepository;

    /**
     * Get all movies in the catalog.
     */
    public Page<MovieResponse> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable).map(this::mapToResponse);
    }

    public Page<MovieResponse> getMoviesByGenre(List<String> genres, Pageable pageable) {
        List<Genre> genreEnums = genres.stream()
                .map(g -> Genre.valueOf(g.toUpperCase(Locale.ROOT)))
                .collect(Collectors.toList());
        return movieRepository.findByGenresIn(genreEnums, genreEnums.size(), pageable).map(this::mapToResponse);
    }

    public Page<MovieResponse> getMoviesBySingleGenre(String genre, Pageable pageable) {
        Genre genreEnum = Genre.valueOf(genre.toUpperCase(Locale.ROOT));
        return movieRepository.findByGenre(genreEnum, pageable).map(this::mapToResponse);
    }

    public Page<MovieResponse> searchMovies(String query, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(query, pageable).map(this::mapToResponse);
    }

    public MovieResponse getMovieById(UUID id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
        return mapToResponse(movie);
    }

    /**
     * Add a new Movie to the catalog.
     *
     * Video is not uploaded yet at this stage.
     */
    public MovieResponse addMovie(MovieRequest request) {
        log.info("Adding new movie: {}", request.getTitle());

        Movie movie = mapToEntity(request);
        Movie savedMovie = movieRepository.save(movie);

        log.info("Movie added with ID: {}", savedMovie.getId());

        return mapToResponse(savedMovie);
    }

    public MovieResponse updateMovie(UUID id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setGenres(request.getGenres());
        movie.setDirector(request.getDirector());
        movie.setCast(request.getCast());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setRating(request.getRating());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setMaturityRating(request.getMaturityRating());

        return mapToResponse(movieRepository.save(movie));
    }

    public void deleteMovie(UUID id) {
        if (!movieRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
        movieRepository.deleteById(id);
    }

    public void updateVideoKey(UUID movieId, String videoKey) {
        log.info("Updating video key for movie: {}", movieId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));

        movie.setVideoKey(videoKey);
        movie.setVideoStatus(VideoStatus.UPLOADED);
        movieRepository.save(movie);
    }

    public void updateHlsUrl(UUID movieId, String hlsUrl) {
        log.info("Updating HLS URL for movie: {}", movieId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));

        movie.setHlsUrl(hlsUrl);
        movie.setVideoStatus(VideoStatus.READY);
        movieRepository.save(movie);

        log.info("Movie {} is now ready for streaming", movieId);
    }

    private MovieResponse mapToResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .genres(movie.getGenres())
                .director(movie.getDirector())
                .cast(movie.getCast())
                .releaseDate(movie.getReleaseDate())
                .rating(movie.getRating())
                .posterUrl(movie.getPosterUrl())
                .durationMinutes(movie.getDurationMinutes())
                .maturityRating(movie.getMaturityRating())
                .hlsUrl(movie.getHlsUrl())
                .videoStatus(movie.getVideoStatus())
                .createdAt(movie.getCreatedAt())
                .updatedAt(movie.getUpdatedAt())
                .build();
    }

    private Movie mapToEntity(MovieRequest request) {
        return Movie.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .genres(request.getGenres())
                .director(request.getDirector())
                .cast(request.getCast())
                .releaseDate(request.getReleaseDate())
                .rating(request.getRating())
                .posterUrl(request.getPosterUrl())
                .durationMinutes(request.getDurationMinutes())
                .maturityRating(request.getMaturityRating())
                .videoStatus(VideoStatus.PENDING)
                .build();
    }
}
