package service;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.api.routing.Router;
import play.db.Databases;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.routing.RoutingDsl;
import play.server.Server;

import java.util.Map;

import static play.mvc.Results.ok;
import static org.junit.Assert.*;

public class MovieServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MovieServiceTest.class);

    MovieService movieService;
    WSClient ws;
    Server server;

    @Before
    public void setup() {
        Router router = new RoutingDsl().GET("http://www.omdbapi.com/")
                .routeTo(() -> ok().sendResource("omdbapi-response.json"))
                .build();

        server = Server.forRouter(router);
        ws = WS.newClient(server.httpPort());

        movieService = new MovieService(Databases.inMemory().getDataSource());
        movieService.ws = ws;

    }

    @After
    public void tearDown() {
        ws.close();
        server.stop();
    }

//    @Test
//    public void testOmdbApi() throws Exception {
//        JsonNode json = movieService.getMovieInfoFromOmd("Inception").get(10000);
//        json.fieldNames().forEachRemaining(f -> logger.info(f.toString()));
//    }

    @Test
    public void testMovieInfo() throws Exception {
        logger.info("testMovieInfo");
        Map<String, String> map = movieService.getMovieInfo("Inception").get(10000);//.forEach((a,b) -> logger.info(a + ": " + b));
        assertEquals(map.size(), 5);
        map.forEach((a, b) -> logger.info(a + ": " + b));
    }

}
