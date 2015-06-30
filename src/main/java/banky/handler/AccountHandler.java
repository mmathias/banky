package banky.handler;

import banky.model.Account;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;

public class AccountHandler {

    @HandleBeforeCreate
    @HandleBeforeSave
    public void beforeCreateAccount(Account account) {

    }
}
