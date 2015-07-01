package banky.controller;


import banky.controller.dto.AccountDTO;
import banky.controller.dto.TransferDTO;
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

        account.setBalance(account.getBalance() + accountDTO.getAmount());
        accountRepository.save(account);

        transactionService.createLodgeTransaction(account, accountDTO.getAmount());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(
            value = "/transfer",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> transfer(@RequestBody TransferDTO transferDTO) {

        Account accountFrom = accountRepository.findOne(transferDTO.getAccountFromId());
        Account accountTo = accountRepository.findOne(transferDTO.getAccountToId());
        double amount = transferDTO.getAmount();

        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);

        accountRepository.save(accountTo);
        accountRepository.save(accountFrom);
        transactionService.createTransferTransaction(accountFrom, accountTo, amount);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
