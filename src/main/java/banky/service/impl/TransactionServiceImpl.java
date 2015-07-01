package banky.service.impl;

import banky.model.Account;
import banky.model.Transaction;
import banky.model.TransactionType;
import banky.repository.TransactionRepository;
import banky.service.TransactionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;

@Component
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    @Inject
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    void createTransaction(Account accountFrom, Account accountTo, double amount, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(accountFrom);
        transaction.setAccountTo(accountTo);
        transaction.setAmount(amount);
        transaction.setDate(LocalDateTime.now());
        transaction.setTransactionType(transactionType);

        transactionRepository.save(transaction);
    }

    @Override
    public void createLodgeTransaction(Account account, double amount) {
        createTransaction(null, account, amount, TransactionType.LODGEMENT);
    }

    @Override
    public void createTransferTransaction(Account accountFrom, Account accountTo, double amount) {
        createTransaction(accountFrom, accountTo, amount, TransactionType.TRANSFER);
    }
}
