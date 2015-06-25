package controllers;

import model.MovieDAO;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    private MovieDAO movieDAO = new MovieDAO();

    public Result index() {
        movieDAO.getTopMoviesList();

        return ok(index.render("Your new application is ready."));
    }

    public Result genres() {
        movieDAO.getMovieGenres();
        return ok(top_genre.render("Top genres"));
    }

}
