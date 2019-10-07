package com.orange.devacademy.moviesservice.repository;


import com.orange.devacademy.moviesservice.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoviesRepository extends MongoRepository<Movie, String> {

    List<Movie> findByTitle(String title);

}
