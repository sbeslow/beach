package controllers;

import java.util.List;

import models.Beach;
import dataManagement.BeachSorter;
import beachNinja.Config;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

    public static final Config config = new Config();

    public static final boolean productionMode = false;

    public static Result index() {
        // If I cared about speed here, I should be writing SQL to get the list in order.  But, I don't.
        List<Beach> beachList = Beach.find.all();
        beachList = BeachSorter.sortByShittiest(beachList);
    	
        return ok(index.render(beachList));
    }

    public static Result test() {
        return TODO;
    }
}
