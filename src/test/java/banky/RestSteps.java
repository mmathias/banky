package banky;

import cucumber.annotation.en.And;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.runtime.PendingException;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

public class RestSteps {

    @Given("^we POST the following JSON to \"([^\"]*)\":$")
    public void we_POST_the_following_JSON_to_(String arg1) throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @And("^the response code should be <response>$")
    public void the_response_code_should_be_response() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }
}