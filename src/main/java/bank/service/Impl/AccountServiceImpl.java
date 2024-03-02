package bank.service.Impl;

import bank.adapter.AccountAdapter;
import bank.dao.AccountDAO;
import bank.domain.Account;
import bank.domain.Customer;
import bank.dto.AccountDTO;
import bank.events.AccountEvent;
import bank.events.TraceRecord;
import bank.instruction.AccountInstruction;
import bank.instruction.TransactionInstruction;
import bank.jms.JMSSender;
import bank.logging.Logger;
import bank.repository.AccountRepository;
import bank.service.AccountService;
import bank.service.CurrencyConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	@Autowired
	private AccountDAO accountDAO;
	@Autowired
	private CurrencyConverter currencyConverter;
	@Autowired
	private JMSSender jmsSender;
	@Autowired
	private Logger logger;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	private AccountAdapter accountAdapter;
	@Autowired
	ApplicationEventPublisher eventPublisher;

	public AccountDTO createAccount(Long accountNumber, String customerName) {
		Account account = new Account(accountNumber);
		Customer customer = new Customer(customerName);
		account.setCustomer(customer);
		accountRepository.save(account);
		String message = "createAccount with parameters accountNumber= "+accountNumber+" , customerName= "+customerName;
		logger.log(message);
		eventPublisher.publishEvent(new AccountEvent(message));
		return accountAdapter.entityToDto(accountRepository.findByAccountnumber(accountNumber));
	}

	public void deposit(Long accountNumber, double amount) {
		Account account = accountRepository.findByAccountnumber(accountNumber);
		account.deposit(amount);
		accountRepository.save(account);
		String message = "deposit with parameters accountNumber= "+accountNumber+" , amount= "+amount;
		logger.log(message);
		if (amount > 10000){
			jmsSender.sendJMSMessage("Deposit of $ "+amount+" to account with accountNumber= "+accountNumber);
		}
		eventPublisher.publishEvent(new AccountEvent(message));
		eventPublisher.publishEvent(new TraceRecord(accountNumber, message, amount));
	}

	public AccountDTO getAccount(Long accountNumber) {
		Account account = accountRepository.findByAccountnumber(accountNumber);
		return accountAdapter.entityToDto(account);
	}

	public Collection<AccountDTO> getAllAccounts() {
		return accountAdapter.entitiesToAccountDTOs(accountRepository.findAll());
	}

	public void withdraw(Long accountNumber, double amount) {
		Account account = accountRepository.findByAccountnumber(accountNumber);
		account.withdraw(amount);
		accountRepository.save(account);
		String message = "withdraw with parameters accountNumber= "+accountNumber+" , amount= "+amount;
		logger.log(message);
		eventPublisher.publishEvent(new AccountEvent(message));
		eventPublisher.publishEvent(new TraceRecord(accountNumber, message, amount));
	}

	public void depositEuros(Long accountNumber, double amount) {
		Account account = accountRepository.findByAccountnumber(accountNumber);
		double amountDollars = currencyConverter.euroToDollars(amount);
		account.deposit(amountDollars);
		accountRepository.save(account);
		String message = "depositEuros with parameters accountNumber= "+accountNumber+" , amount= "+amount;
		logger.log(message);
		if (amountDollars > 10000){
			jmsSender.sendJMSMessage("Deposit of $ "+amount+" to account with accountNumber= "+accountNumber);
		}
		eventPublisher.publishEvent(new AccountEvent(message));
		eventPublisher.publishEvent(new TraceRecord(accountNumber, message, amount));
	}

	public void withdrawEuros(Long accountNumber, double amount) {
		Account account = accountRepository.findByAccountnumber(accountNumber);
		double amountDollars = currencyConverter.euroToDollars(amount);
		account.withdraw(amountDollars);
		accountRepository.save(account);
		String message = "withdrawEuros with parameters accountNumber= "+accountNumber+" , amount= "+amount;
		logger.log(message);
		eventPublisher.publishEvent(new AccountEvent(message));
		eventPublisher.publishEvent(new TraceRecord(accountNumber, message, amount));
	}

	public void transferFunds(Long fromAccountNumber, Long toAccountNumber, double amount, String description) {
		Account fromAccount = accountRepository.findByAccountnumber(fromAccountNumber);
		Account toAccount = accountRepository.findByAccountnumber(toAccountNumber);
		System.out.println("----- From -----");
		System.out.println(fromAccount);
		System.out.println("----- To -----");
		System.out.println(toAccount);
		System.out.println(description);
		fromAccount.transferFunds(toAccount, amount, description);
		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);
		String message = "transferFunds with parameters fromAccountNumber= "+fromAccountNumber+" , toAccountNumber= "+toAccountNumber+" , amount= "+amount+" , description= "+description;
		logger.log(message);
		if (amount > 10000){
			jmsSender.sendJMSMessage("TransferFunds of $ "+amount+" from account with accountNumber= "+fromAccount+" to account with accountNumber= "+toAccount);
		}
		eventPublisher.publishEvent(new AccountEvent(message));
		eventPublisher.publishEvent(new TraceRecord(fromAccountNumber, message, amount));
	}
}
