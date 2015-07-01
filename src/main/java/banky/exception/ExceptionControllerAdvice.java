package banky.exception;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    private ErrorResponse handleException(Exception e, String errorCode, boolean displayCause) {
        String cause = Throwables.getRootCause(e).getMessage();
        logger.debug(cause);
        String message = ErrorCodes.GENERAL_ERROR_RESPONSE_MESSAGE;
        String status = ErrorCodes.ERROR_TEXT;

        return new ErrorResponse(errorCode, cause, message, status);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<ErrorResponse>(handleException(e, ErrorCodes.INTERNAL_ERROR_CODE, true), HttpStatus.FORBIDDEN);
    }
}


