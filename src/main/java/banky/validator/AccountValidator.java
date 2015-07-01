package banky.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AccountValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        errors.reject("DIE");
    }
}
