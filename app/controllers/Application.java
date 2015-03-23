package controllers;

import models.Beach;
import models.SignificantError;
import org.apache.commons.lang3.exception.ExceptionUtils;
import play.mvc.*;

import views.html.*;
import beachNinja.BeachScraper;

import java.util.List;

public class Application extends Controller {

    public static final boolean productionMode = false;

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result test() {
        return TODO;
    }

    public static Result scrapeCpdAllBeaches() {
        List<Beach> beaches = Beach.find.all();
        for (Beach beach: beaches) {
            try {
                BeachScraper.scrapeCpdPage(beach);
            }
            catch (Exception e) {
                SignificantError.write(ExceptionUtils.getStackFrames(e));
            }
        }

        return TODO;
    }
}
