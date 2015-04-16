package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dataManagement.BeachSorter;
import beachNinja.SeasonalStats;
import models.Beach;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Apis extends Controller {

    public static Result advisories(String beachUrlCode) {

        try {
            Beach beach = Beach.find.where().eq("urlCode", beachUrlCode).findUnique();
            SeasonalStats seasonalStats = beach.getSeasonalStats();
            return ok(Json.toJson(seasonalStats));

        }
        catch (Exception e) {
            return ok("{}");
        }
    }
    
    public static Result scoreboard() {
        // If I cared about speed here, I should be writing SQL to get the list in order.  But, I don't.
        List<Beach> beachList = Beach.find.all();
        beachList = BeachSorter.sortByShittiest(beachList);
        
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode beachesNode = factory.arrayNode();
        
        for (Beach beach : beachList) {
        	ObjectNode beachNode = Json.newObject();
        	beachNode.put("name", beach.name);
        	beachNode.put("noRestrict", beach.getSeasonalStats().percentNoRestrict());
        	beachNode.put("advisory", beach.getSeasonalStats().percentAdvisory());
        	beachNode.put("ban", beach.getSeasonalStats().percentBan());
        	beachesNode.add(beachNode);
        }
        return ok(beachesNode);
    }
}
