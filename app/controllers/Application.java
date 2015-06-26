package controllers;

import model.Movie;
import model.MovieDAO;
import play.*;
import play.libs.F;
import play.mvc.*;

import service.MovieService;
import views.html.*;

public class Application extends Controller {

    //private MovieDAO movieDAO = new MovieDAO();
    private MovieService movieService = new MovieService();

    public Result index() {
        //movieDAO.getTopMoviesList();

        return ok(index.render("Your new application is ready."));
    }

    public Result genres() {
        //movieDAO.getMovieGenres();
        return ok(top_genre.render("Top genres"));
    }

    public F.Promise<Result> movie() {
        //@todo changeme
        //Movie movie = movieService.getMovie(27205).get();
        return movieService.getMovieInfo("Inception")
                .map(i -> ok(movie.render(i)));
        //return ok(movie.render("Movie"));
    }

}
