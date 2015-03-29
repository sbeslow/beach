package controllers;

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
}
