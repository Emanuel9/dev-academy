package com.orange.devacademy.moviesservice.controller;


import com.orange.devacademy.moviesservice.model.Movie;
import com.orange.devacademy.moviesservice.service.FavouritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.orange.devacademy.moviesservice.configuration.ApiConstants.API_V1;

@RestController
@RequestMapping(API_V1)
public class FavouritesController {

    @Autowired
    private FavouritesService favouritesService;

    @PutMapping(value = "/addFavourite")
    public void addToFav(@RequestParam String movieId) {
        favouritesService.addToFavourites(movieId);
    }

    @GetMapping(value = "/getFavourites", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Movie> getFav() {
        return favouritesService.getFavourites();
    }

}
