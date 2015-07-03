package banky.controller;


import banky.controller.dto.AccountDTO;
import banky.controller.dto.TransferDTO;
import banky.exception.AccountDoesntExist;
import banky.exception.OutOfFoundsException;
import banky.model.Account;
import banky.repository.AccountRepository;
import banky.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
public class BankController {

    private AccountRepository accountRepository;
    private TransactionService transactionService;

    @Inject
    public BankController(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @RequestMapping(
        value = "/accounts/{accountId}/lodge",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> makeLodgement(@PathVariable("accountId") Account account, @RequestBody AccountDTO accountDTO) {

        if (account == null) {
            throw new AccountDoesntExist("Account doesn't exist!");
        }

        account.setBalance(account.getBalance() + accountDTO.getAmount());
        accountRepository.save(account);

        transactionService.createLodgeTransaction(account, accountDTO.getAmount());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(
            value = "/accounts/{accountId}/transfer",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> transfer(@PathVariable("accountId") Account account, @RequestBody TransferDTO transferDTO) {

        Account accountTo = validateBeforeTransfer(account, transferDTO);

        double amount = transferDTO.getAmount();

        account.setBalance(account.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);

        accountRepository.save(accountTo);
        accountRepository.save(account);
        transactionService.createTransferTransaction(account, accountTo, amount);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private Account validateBeforeTransfer(Account account, TransferDTO transferDTO) {
        if (account == null) {
            throw new AccountDoesntExist("Account doesn't exist!");
        }

        if (account.getBalance() < transferDTO.getAmount()) {
            throw new OutOfFoundsException("Not enough money to make this transaction.");
        }

        Account accountTo = accountRepository.findOne(transferDTO.getAccountToId());
        if (accountTo == null) {
            throw new AccountDoesntExist("Account doesn't exist!");
        }

        return accountTo;
    }
}
