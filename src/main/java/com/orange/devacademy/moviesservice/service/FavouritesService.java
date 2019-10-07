package com.orange.devacademy.moviesservice.service;

import com.orange.devacademy.moviesservice.model.Movie;
import com.orange.devacademy.moviesservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class FavouritesService {

    @Autowired
    UserService userService;

    @Autowired
    MoviesService moviesService;

    public void addToFavourites(String movieId) {
        User connectedUser = userService.retrieveCurrentLoggedInUser();
        if(connectedUser == null) {
            throw new AuthenticationServiceException("User not logged in!");
        }
        Set<String> myMovies = connectedUser.getMyMovies();
        if(myMovies == null) {
            myMovies = new HashSet<>();
        }
        if(myMovies.add(movieId)) {
            connectedUser.setMyMovies(myMovies);
            userService.saveUser(connectedUser);
        }
    }

    public Iterable<Movie> getFavourites() {
        User connectedUser = userService.retrieveCurrentLoggedInUser();
        if(connectedUser == null) {
            throw new AuthenticationServiceException("User not logged in!");
        }
        if(connectedUser.getMyMovies() == null || connectedUser.getMyMovies().isEmpty()) {
            return Collections.emptySet();
        }
        return moviesService.findAllById(connectedUser.getMyMovies());
    }

}

