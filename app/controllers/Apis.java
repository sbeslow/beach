package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dataManagement.BeachSorter;
import models.Beach;
import models.BeachSnapshot;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Apis extends Controller {
    
    public static Result scoreboard() {
        // If I cared about speed here, I should be writing SQL to get the list in order.  But, I don't.
        List<Beach> beachList = Beach.find.all();
        beachList = BeachSorter.sortByShittiest(beachList);
        
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode beachesNode = factory.arrayNode();
        int rank = 1;
        
        for (Beach beach : beachList) {
        	ObjectNode beachNode = Json.newObject();
        	beachNode.put("name", beach.name);
        	beachNode.put("urlCode", beach.urlCode);
        	beachNode.put("rank", rank);
        	beachNode.put("noRestrict", beach.getSeasonalStats().percentNoRestrict());
        	beachNode.put("advisory", beach.getSeasonalStats().percentAdvisory());
        	beachNode.put("ban", beach.getSeasonalStats().percentBan());
        	beachesNode.add(beachNode);
        	rank++;
        }
        return ok(beachesNode);
    }
    
    public static Result ecoli(String beachUrl) {
    	
    	List<BeachSnapshot> snapshots = BeachSnapshot.find.where().eq("beach.urlCode", beachUrl).orderBy("scrapeTime asc") .findList();
    	if (snapshots.size() == 0) {
    		return TODO;
    	}
    	
    	JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode ecoliNode = factory.arrayNode();
        
        ObjectNode beachEcoliNode = Json.newObject();
        
        
        beachEcoliNode.put("beachName", snapshots.get(0).beach.name);
        
        for (BeachSnapshot snapshot : snapshots) {
        	ObjectNode readingNode = Json.newObject();
        	readingNode.put("dateTime", snapshot.scrapeTime.getMillis());
        	readingNode.put("forecast", snapshot.forecastForToday);
        	readingNode.put("result", snapshot.mostRecentResult);
        	ecoliNode.add(readingNode);
        	
        }
        beachEcoliNode.put("readings", ecoliNode);
    	
    	return ok(beachEcoliNode);
    }
}
