package banky.service;

import banky.controller.dto.AccountDTO;
import banky.model.Account;

public interface TransactionService {

    void createLodgeTransaction(Account account, double amount);

    void createTransferTransaction(Account accountFrom, Account accountTo, double amount);
}
