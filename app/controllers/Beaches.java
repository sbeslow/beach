package controllers;

import models.Beach;
import play.mvc.Controller;
import play.mvc.Result;

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
}
