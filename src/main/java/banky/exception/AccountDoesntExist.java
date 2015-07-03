package banky.exception;

public class AccountDoesntExist extends RuntimeException {

    public AccountDoesntExist(String message) {
        super(message);
    }
}
