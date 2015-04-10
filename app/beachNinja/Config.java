package beachNinja;

import play.Play;

public class Config {

    private boolean testMode;
    public Config() {

        String testModeStr = Play.application().configuration().getString("testMode");
        if ((testModeStr == null) || (testModeStr.equals("false"))) {
            testMode = false;
        }
        else {
            testMode = true;
        }

    }

    public boolean isTestMode() {
        return testMode;
    }
}
