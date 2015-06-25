package controllers;

import model.MovieDAO;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public Result index() {
        MovieDAO movieDAO = new MovieDAO();
        movieDAO.getTopMoviesList();

        return ok(index.render("Your new application is ready."));
    }

}
