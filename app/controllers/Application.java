package controllers;

import java.util.List;

import models.Beach;
import dataManagement.BeachSorter;
import beachNinja.Config;
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
        return TODO;
    }

    public static Scoreboard getScoreboard() {
        if (scoreboard == null)
            scoreboard = new Scoreboard();
        return scoreboard;
    }

    public static void updateScoreboard() {
        scoreboard = new Scoreboard();
    }
}
