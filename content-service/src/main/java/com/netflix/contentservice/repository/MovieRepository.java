package com.netflix.contentservice.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.netflix.contentservice.model.Genre;
import com.netflix.contentservice.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, UUID> {

    @Query("SELECT m FROM Movie m WHERE (:genre) MEMBER OF m.genres")
    Page<Movie> findByGenre(@Param("genre") Genre genre, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.genres IN :genres")
    Page<Movie> findByAnyGenreIn(@Param("genres") List<Genre> genres, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.genres IN :genres GROUP BY m HAVING COUNT(DISTINCT m.genres) = :count")
    Page<Movie> findByGenresIn(@Param("genres") List<Genre> genres, @Param("count") int count, Pageable pageable);

    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
