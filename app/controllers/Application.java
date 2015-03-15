package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static final boolean productionMode = false;

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

}
