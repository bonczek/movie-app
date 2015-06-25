package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.db.DB;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDAO {

    private DataSource dataSource;

    private static final Logger logger = LoggerFactory.getLogger(MovieDAO.class);

    public MovieDAO() {
        this.dataSource = DB.getDataSource();
    }

    public MovieDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Movie> getTopMoviesList() {
        final String TOP_MOVIES_QUERY = "SELECT title, vote_average " +
                "FROM movie " +
                "ORDER BY vote_average DESC, release_date ASC " +
                "limit 20";

        List<Movie> moviesList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(TOP_MOVIES_QUERY);
            while (result.next()) {
                String title = result.getString("title");
                float vote = result.getFloat("vote_average");
                moviesList.add(new Movie(title, vote));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Found: {} movies", moviesList.size());
        return moviesList;
    }

    public List<Genre> getMovieGenres() {
        final String GENRES_MOVIE_COUNT_QUERY = "SELECT g.id AS id, g.name AS name, COUNT(*) AS movie_count " +
                "FROM genre g, movie_genre " +
                "WHERE genre_id = g.id " +
                "GROUP BY g.id";

        logger.trace("Executing genres search with movie count");
        List<Genre> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(GENRES_MOVIE_COUNT_QUERY);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                int count = result.getInt("movie_count");
                results.add(new Genre(id, name, count));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("{} genres found", results.size());
        return results;
    }

    public Optional<Movie> findMovieById(int id) {
        final String FIND_MOVIE_QUERY = "SELECT title, vote_average FROM movie WHERE id = " + id;

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(FIND_MOVIE_QUERY);
            while (result.next()) {
                String title = result.getString("title");
                float vote = result.getInt("vote_average");
                logger.info("Movie - id: {} found", id);
                return Optional.of(new Movie(title, vote));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Movie with id: {} not found", id);
        return Optional.empty();
    }

    public List<String> getMovieGenres(int movieId) {
        final String FIND_MOVIE_GENRES_QUERY = "SELECT name " +
                "FROM genre, movie_genre " +
                "WHERE genre.id = movie_genre.genre_id AND movie_genre.movie_id = " + movieId;

        List<String> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(FIND_MOVIE_GENRES_QUERY);
            while (result.next()) {
                String genre = result.getString("name");
                results.add(genre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Found {} genres", results.size());
        return results;
    }
}
