package banky;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

public class RestSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestSteps.class);

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private ResponseEntity<?> lastResponse = null;
    private final static String BASE_URL = "http://localhost:8080";
    private Properties savedLinks = new Properties();

    @Given("^we (\\w+) the following JSON to \"([^\"]*)\":$")
    public void we_send_by_method_the_following_JSON_to_(String method, String path, String jsonBody) throws Throwable {
        performWithUrl(BASE_URL + path, HttpMethod.valueOf(method), templateReplaceSavedLinks(jsonBody), false);
    }

    @And("^the last call was (\\w+)$")
    public void the_last_call_was_response(HttpStatus status) throws Throwable {
        assertEquals(status, lastResponse.getStatusCode());
    }

    @Then("^the response matches:$")
    public void the_response_matches(String jsonBody) throws Throwable {
        JSONAssert.assertEquals(jsonBody, getJsonResponseString(), false);
    }

    @And("^we accept error responses$")
    public void we_accept_error_responses() throws Throwable {
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // don't throw an exception
            }
        });
    }

    @And("^we save the returned location as a saved-link \"([^\"]*)\"$")
    public void we_save_the_returned_location_as_a_saved_link(String savedLinkName) throws Throwable {
        String location = getMandatoryLocationFor(lastResponse).toASCIIString();
        String id = location.substring(location.lastIndexOf("/") + 1, location.length());
        savedLinks.put(savedLinkName, location);
        savedLinks.put(savedLinkName + "Id", id);
    }

    @Given("^we (\\w+) the following JSON to saved-link \"([^\"]*)\" and sub-path \"([^\"]*)\":$")
    public void we_method_the_following_JSON_to_saved_link_and_sub_path_(String method, String savedLinkName, String subPath, String jsonBody) throws Throwable {
        HttpMethod httpMethod = HttpMethod.valueOf(method);
        performWithUrl(savedLinks.getProperty(savedLinkName) + subPath, httpMethod, jsonBody, false);
    }

    @Given("^we GET the saved-link \"([^\"]*)\"$")
    public void we_GET_the_saved_link(String savedLinkName) throws Throwable {
        performWithUrl(savedLinks.getProperty(savedLinkName), HttpMethod.GET, "", false);
    }

    private String getJsonResponseString() {
        return ((ResponseEntity<JsonNode>) lastResponse).getBody().toString();
    }

    public void performWithUrl(String url, HttpMethod method, String jsonBody, boolean isCompact) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (isCompact) {
            headers.setAccept(Arrays.asList(new MediaType("application", "*+json")));
        } else {
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        }

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        perform(template -> template.exchange(url, method, entity, JsonNode.class, Collections.emptyMap()));
    }

    public <T> ResponseEntity<T> perform(Function<TestRestTemplate, ResponseEntity<T>> block) {
        ResponseEntity<T> response = block.apply(restTemplate);
        if (response != null) {
            lastResponse = response;
        }

        return (ResponseEntity<T>) lastResponse;
    }

    public URI getMandatoryLocationFor(ResponseEntity<?> response) {
        ensureSuccessfulResponse(response);

        return Optional
                .ofNullable(response.getHeaders().getLocation())
                .orElseThrow(() -> new RuntimeException("Location header not present. Error text: " + response.getBody()));
    }

    public void ensureSuccessfulResponse(ResponseEntity<?> response) {
        assertThat("The response was successful", response.getStatusCode().is2xxSuccessful(), is(true));
    }

    private String templateReplaceSavedLinks(String inputString) {
        PropertyPlaceholderHelper propertyReplacer = new PropertyPlaceholderHelper("${", "}", ",", false);

        return propertyReplacer.replacePlaceholders(inputString, savedLinks);
    }
}