package service;

import com.fasterxml.jackson.databind.JsonNode;
import model.Movie;
import model.MovieDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;


import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    MovieDAO movieDAO;

    //@Inject
    //@todo: injecting WSClient
    WSClient ws = WS.client();

    public MovieService() {
        movieDAO = new MovieDAO();
    }

    public MovieService(DataSource dataSource) {
        this.movieDAO = new MovieDAO(dataSource);
    }

    public Optional<Movie> getMovie(int id) {
        Optional<Movie> movieOpt = movieDAO.findMovieById(id);
        if(!movieOpt.isPresent())
            return Optional.empty();
        Movie movie = movieOpt.get();
        movie.setGenres(movieDAO.getMovieGenres(id));
        return Optional.of(movie);
    }

    public F.Promise<JsonNode> getMovieInfoFromOmd(String title) {
        logger.info("getMovieInfoFromOmd");
        WSRequest request = ws.url("http://www.omdbapi.com/")
                .setQueryParameter("t", title);
        return request.get().map(response -> response.asJson());
    }

    public F.Promise<Map<String, String>> getMovieInfo(String title) {
        logger.info("getMovieInfo");
        return getMovieInfoFromOmd(title).flatMap(response -> parseJsonDescription(response));
    }

    public F.Promise<Map<String, String>> parseJsonDescription(JsonNode json) {
        logger.info("parseJson");
        json.fieldNames().forEachRemaining(f -> logger.info(f.toString()));

        Map<String, String> descriptionMap = new HashMap<>();
        descriptionMap.put("Year", json.findValue("Year").asText());
        descriptionMap.put("Released", json.findValue("Released").asText());
        descriptionMap.put("Runtime", json.findValue("Runtime").asText());
        descriptionMap.put("Actors", json.findValue("Actors").asText());
        descriptionMap.put("Plot", json.findValue("Plot").asText());
        return F.Promise.promise(() -> descriptionMap);
    }

}
