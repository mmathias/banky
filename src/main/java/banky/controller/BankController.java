package banky.controller;


import banky.controller.dto.AccountDTO;
import banky.controller.dto.TransferDTO;
import banky.model.Account;
import banky.repository.AccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

@RestController
public class BankController {

    private AccountRepository accountRepository;

    @Inject
    public BankController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @RequestMapping(
        value = "/accounts/{accountId}/lodge",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> makeLodgement(@PathVariable("accountId") Account account, @RequestBody AccountDTO accountDTO) {

        account.setBalance(account.getBalance() + accountDTO.getAmount());
        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(
            value = "/transfer",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> transfer(@RequestBody TransferDTO transferDTO) {

        Account accountFrom = accountRepository.findOne(transferDTO.getAccountFromId());
        Account accountTo = accountRepository.findOne(transferDTO.getAccountToId());

        accountFrom.setBalance(accountFrom.getBalance() - transferDTO.getAmount());
        accountTo.setBalance(accountTo.getBalance() + transferDTO.getAmount());

        accountRepository.save(accountTo);
        accountRepository.save(accountFrom);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
