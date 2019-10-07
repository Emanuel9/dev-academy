package com.orange.devacademy.moviesservice.controller;

import com.orange.devacademy.moviesservice.controller.response.ErrorResponse;
import com.orange.devacademy.moviesservice.model.Movie;
import com.orange.devacademy.moviesservice.service.UserService;
import com.orange.devacademy.moviesservice.service.MoviesService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Optional;

import static com.orange.devacademy.moviesservice.configuration.ApiConstants.API_V1;

@RestController
@RequestMapping(API_V1)
public class MoviesController {

    private final MoviesService moviesService;

    @Autowired
    public MoviesController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @Autowired
    public UserService userService;

    @ApiOperation(
            value = "Get movies", authorizations = {@Authorization(value = "basicAuthApi")}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Successful Operation", response = List.class),
                    @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Wrong input parameters, unknown business-unit", response = ErrorResponse.class),
                    @ApiResponse(code = HttpURLConnection.HTTP_UNAUTHORIZED, message = "Unauthorized, authentication credentials are not valid", response = ErrorResponse.class),
                    @ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Server Error", response = ErrorResponse.class)
            }
    )
    @GetMapping(value = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> getMyMovies(@RequestParam(required = false) String title) {
        if (Optional.ofNullable(title).isPresent())
            return moviesService.getMovieByTitle(title);

        return moviesService.getMyMovies();
    }

}
