package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.db.DB;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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


}
