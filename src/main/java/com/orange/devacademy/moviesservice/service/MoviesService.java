package com.orange.devacademy.moviesservice.service;

import com.orange.devacademy.moviesservice.model.Movie;
import com.orange.devacademy.moviesservice.repository.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MoviesService {

    private final MoviesRepository moviesRepository;

    @Autowired
    public MoviesService(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    public List<Movie> getMyMovies() {
        return moviesRepository.findAll();
    }

    public List<Movie> getMovieByTitle(String title) {
        return moviesRepository.findByTitle(title);
    }

    public Iterable<Movie> findAllById(Set<String> ids) {
        return moviesRepository.findAllById(ids);
    }
}
