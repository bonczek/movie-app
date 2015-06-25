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

public class MovieDAO {

    private static final Logger logger = LoggerFactory.getLogger(MovieDAO.class);

    DataSource dataSource = DB.getDataSource();

    public static final String TOP_MOVIES_QUERY = "SELECT id, title, vote_average FROM movie ORDER BY vote_average DESC limit 20";

    public void getTopMoviesList() {

        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(TOP_MOVIES_QUERY);
            while (result.next()) {
                int id = result.getInt("id");
                String title = result.getString("title");
                float vote = result.getFloat("vote_average");

                logger.info("Movie - id: {}, title: {}, vote: {}", id, title, vote);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Genre> getMovieGenres() {
        final String MOVIE_GENRIES_QUERY = "SELECT g.id AS id, g.name AS name, COUNT(*) AS movie_count " +
                "FROM genre g, movie_genre " +
                "WHERE genre_id = g.id " +
                "GROUP BY g.id";

        List<Genre> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(MOVIE_GENRIES_QUERY);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                int count = result.getInt("movie_count");
                results.add(new Genre(id, name, count));
                logger.info("Movie - id: {}, title: {}, count: {}", id, name, count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
