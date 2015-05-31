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

            return badRequest("ERRORCODE:2");
        }
        beach.getSeasonalStats(); // Calculate the seasonal stats here.
        return ok(views.html.beaches.show.render(beach));
    }

    public static List<Beach> scoreboard() {

        // If I cared about speed here, I should be writing SQL to get the list in order.  But, I don't.
        List<Beach> beachList = Beach.find.all();
        beachList = BeachSorter.sortByShittiest(beachList);
        return beachList;

    }
    
    public static List<Beach> beachList() {
    	return Beach.find.orderBy("name asc").findList();
    }
    
    public static int rank(String beachUrl) {
    	List<Beach> scoreboard = scoreboard();
    	int i = 1;
    	for (Beach beach: scoreboard) {
    		if (beach.urlCode.equals(beachUrl)) {
    			return i;
    		}
    		i++;
    	}
    	return 100;
    }
}
