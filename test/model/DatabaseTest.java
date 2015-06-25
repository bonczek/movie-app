package model;


import com.google.common.collect.ImmutableMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.db.Database;
import play.db.Databases;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class DatabaseTest {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseTest.class);

    private Database database;

    private MovieDAO movieDAO;

    @Before
    public void createDatabase() {
        database = Databases.createFrom(
                "org.postgresql.Driver",
                "jdbc:postgresql://ec2-54-217-202-110.eu-west-1.compute.amazonaws.com:5432/d8u6uelvine6d6?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
                ImmutableMap.of(
                        "user", "iwzexazhfjxbbt",
                        "password", "4JVMJFooosyfdM5Y79Si-c691D"
                )
        );
        movieDAO = new MovieDAO(database.getDataSource());
    }

    @After
    public void shutdownDatabase() {
        database.shutdown();
    }

    @Test
    public void testTopGenres() throws Exception {
        logger.trace("Testing top genres search");
        List<Genre> genreList = movieDAO.getMovieGenres();
        genreList.stream().forEach(genre -> logger.info(genre.toString()));

        assertEquals(genreList.size(), 20);
    }

    @Test
    public void testFindMovieById() throws Exception {
        logger.trace("Testing movie search by id");

        Optional<Movie> movieOptional = movieDAO.findMovieById(27205);
        assertTrue(movieOptional.isPresent());
        logger.info(movieOptional.get().toString());
        assertEquals(movieOptional.get().getTitle(), "Inception");
    }

    @Test
    public void testGetMovieGenres() throws Exception {
        logger.trace("Testing genre search by movie id");

        List<String> genresList = movieDAO.getMovieGenres(27205);
        genresList.forEach(genre -> logger.info(genre));
        assertEquals(genresList.size(), 5);
    }

    @Test
    public void testGetTopMovies() throws Exception {
        logger.trace("Testing search of top movies");

        List<Movie> moviesList = movieDAO.getTopMoviesList();
        moviesList.forEach(movie -> logger.info(movie.toString()));
        assertEquals(moviesList.size(), 20);
    }




}
