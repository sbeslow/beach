import org.junit.*;

import play.mvc.*;
import play.test.*;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

    @Test
    public void testNullName() {
    	  Result result = callAction(
    			    controllers.routes.ref.Application.index(),
    			    new FakeRequest(POST, "/addPerson?teamName=Bears")
    			  );
    	  assertThat(status(result)).isEqualTo(play.mvc.Http.Status.BAD_REQUEST);
    }

}
