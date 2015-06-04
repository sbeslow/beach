package controllers;

import beachNinja.BeachScraper;
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

        //BeachScraper.scrapeAllCpdPages();

    	/*
    	Beach beach = Beach.find.where().eq("urlCode", "31st-Street-Beach").findUnique();
    	
    	try {
    		BeachScraper.scrapeCpdPage(beach);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return badRequest("Didn't work");
    	}
    	*/
        return ok("Success!");
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
