package controllers;

import models.Beach;
import models.BeachSnapshot;
import models.SignificantError;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import play.mvc.Controller;
import play.mvc.Result;
import webScraping.BeachScraper;

import java.util.Map;

public class Beaches extends Controller {

    public static Result show(String urlCode) {

        BeachSnapshot beachSnapshot = null;
        try {
            beachSnapshot= BeachSnapshot.find.where().eq("beach.urlCode", urlCode)
                    .setOrderBy("scrapeTime desc").findList().get(0);

        }
        catch (Exception e) {
            if (!Application.productionMode) {
                // beachSnapshot is null.
                // if this is not production, set fake scrapes
                // TODO: Set these from database
            }
            return badRequest("ERRORCODE:2");
        }

        return ok(views.html.beaches.show.render(beachSnapshot));

    }
}
