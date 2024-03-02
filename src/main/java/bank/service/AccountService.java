package bank.service;

import bank.domain.Account;
import bank.dto.AccountDTO;

import java.util.Collection;



public interface AccountService {
    public AccountDTO createAccount(Long accountNumber, String customerName);
    public AccountDTO getAccount(Long accountNumber);
    public Collection<AccountDTO> getAllAccounts();
    public void deposit (Long accountNumber, double amount);
    public void withdraw (Long accountNumber, double amount);
    public void depositEuros (Long accountNumber, double amount);
    public void withdrawEuros (Long accountNumber, double amount);
    public void transferFunds(Long fromAccountNumber, Long toAccountNumber, double amount, String description);

}
