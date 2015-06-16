package controllers;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.BeachRanking;
import models.BeachSnapshot;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import scoreboard.EcoliMeasurement;
import models.Scoreboard;

public class Apis extends Controller {
       
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

    public static Result scoreboard() {
        Scoreboard s = Application.getScoreboard();
        return ok(Json.toJson(s));

    }

    public static Result beachInfo(String beachUrl) {

        Scoreboard s = Application.getScoreboard();
        for (BeachRanking beachRanking : s.beachRankings) {
            if (beachRanking.beachUrl.equals(beachUrl)) {
                return ok(Json.toJson(beachRanking));
            }
        }
        return badRequest("Can't find this beach");

    }
    
    public static Result fullSummaries() {
    	
    	return ok(views.html.apis.completeEcoliCsv.render(Application.getScoreboard()));
    }


}
