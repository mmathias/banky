package banky;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import junit.framework.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class RestSteps {

    private TestRestTemplate restTemplate;
    private ResponseEntity<?> lastResponse = null;
    private final static String BASE_URL = "http://localhost:8080";

    @Given("^we (\\w+) the following JSON to \"([^\"]*)\":$")
    public void we_POST_the_following_JSON_to_(String method, String path, String jsonBody) throws Throwable {
        performWithUrl(BASE_URL + path, HttpMethod.valueOf(method), jsonBody, false);
    }

    @And("^the response code should be <response>$")
    public void the_response_code_should_be_response() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @And("^the last call was (\\w+)$")
    public void the_last_call_was_response(HttpStatus status) throws Throwable {
        assertEquals(status, lastResponse.getStatusCode());
    }

    @Then("^the response matches:$")
    public void the_response_matches(String jsonBody) throws Throwable {
        JSONAssert.assertEquals(jsonBody, getJsonResponseString(), false);
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
        createRestTemplate();

        perform(template -> template.exchange(url, method, entity, JsonNode.class, Collections.emptyMap()));
    }

    public <T> ResponseEntity<T> perform(Function<TestRestTemplate, ResponseEntity<T>> block) {
        ResponseEntity<T> response = block.apply(restTemplate);
        if (response != null) {
            lastResponse = response;
        }

        return (ResponseEntity<T>) lastResponse;
    }

    public void createRestTemplate() {

        Optional<ResponseErrorHandler> oldErrorHandler = Optional
                .ofNullable(restTemplate)
                .map(RestTemplate::getErrorHandler);

        restTemplate = new TestRestTemplate();

        ClientHttpRequestInterceptor ri = new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                if (body.length > 0) {

                    try {
                        ObjectMapper mapper  = new ObjectMapper();
                        Object json = mapper.readValue(body, Object.class);

                        mapper.enable(SerializationFeature.INDENT_OUTPUT);
                        String indentedRequestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

//                        LOGGER.debug("Sending request to URI {}: \n\n{}", request.getURI().toASCIIString(), indentedRequestBody);
                    } catch (Exception e) {
                        // Ignore - probably not JSON
                    }

                }
                return execution.execute(request, body);
            }
        };
        restTemplate.setInterceptors(Arrays.asList(ri));

        oldErrorHandler.ifPresent(restTemplate::setErrorHandler);
    }
}