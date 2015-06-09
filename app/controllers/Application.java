package controllers;

import beachNinja.Config;
import play.Play;
import play.mvc.*;
import models.Scoreboard;
import views.html.*;

public class Application extends Controller {

    public static final Config config = new Config();

    private static Scoreboard scoreboard = null;

    public static Result index() {
    	
        return ok(index.render(getScoreboard()));
    }

    public static Result test() {

        //OneOffScripts.fillEmptyForecasts();

        return ok("Success!");
    }

    public static Result involved() {
        return ok(views.html.getInvolved.render());
    }

    public static Scoreboard getScoreboard() {
        if (scoreboard == null)
            scoreboard = new Scoreboard();
        return scoreboard;
    }

    public static void updateScoreboard() {
        scoreboard = new Scoreboard();
    }

    public static String mapboxAccessCode() {
        return Play.application().configuration().getString("mapboxCode");
    }
    public static String mapboxProjectId() {
        return Play.application().configuration().getString("mapBoxProjectId");
    }

}
