package controllers;

import dataManagement.BeachSorter;
import models.Beach;
import models.BeachRanking;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class Beaches extends Controller {

    public static Result show(String urlCode) {
    	
    	for (BeachRanking beachRanking : Application.getScoreboard().beachRankings) {
    		if (beachRanking.beachUrl.equals(urlCode)) {
    			return ok(views.html.beaches.show.render(beachRanking));
    		}
    	}

        return badRequest("Unable to find this beach");
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
