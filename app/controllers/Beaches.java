package controllers;

import dataManagement.BeachSorter;
import models.Beach;
import models.BeachSnapshot;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class Beaches extends Controller {

    public static Result show(String urlCode) {

        Beach beach;
        try {
            beach = Beach.find.where().eq("urlCode", urlCode).findUnique();
        }
        catch (Exception e) {
            if (!Application.productionMode) {
                // beachSnapshot is null.
                // if this is not production, set fake scrapes
                // TODO: Set these from database
            }
            return badRequest("ERRORCODE:2");
        }

        return ok(views.html.beaches.show.render(beach));
    }

    public static List<Beach> scoreboard() {

        // If I cared about speed here, I should be writing SQL to get the list in order.  But, I don't.
        List<Beach> beachList = Beach.find.all();
        beachList = BeachSorter.sortByShittiest(beachList);
        return beachList;

    }
}
