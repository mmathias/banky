package banky.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class ErrorResponse extends ResourceSupport {

    private final String code;
    private final String cause;
    private final String message;
    private final String status;

    @JsonCreator
    public ErrorResponse(@JsonProperty("code") String code, @JsonProperty("cause") String cause, @JsonProperty("message") String message, @JsonProperty("status") String status) {
        this.code =  code;
        this.cause = cause;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }
    
    public String getCause() {
        return cause;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}