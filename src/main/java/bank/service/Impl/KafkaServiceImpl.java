package bank.service.Impl;

import bank.dto.AccountDTO;
import bank.instruction.AccountInstruction;
import bank.instruction.TransactionInstruction;
import bank.repository.AccountRepository;
import bank.service.AccountService;
import bank.service.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    @KafkaListener(topics = {"AccountTopic"})
    public AccountDTO createAccountKafka(@Payload String accountPayload) throws JsonProcessingException {
        System.out.println(accountPayload);
        AccountInstruction accountInstruction = objectMapper.readValue(accountPayload, AccountInstruction.class);
        return accountService.createAccount(accountInstruction.getAccountNumber(), accountInstruction.getCustomerName());
    }
    @Override
    @KafkaListener(topics = {"DepositMoney"})
    public void depositKafka(@Payload String transactionPayload) throws JsonProcessingException {
        System.out.println(transactionPayload);
        TransactionInstruction transactionInstruction = objectMapper.readValue(transactionPayload, TransactionInstruction.class);
        accountService.deposit(transactionInstruction.getAccountNumber(), transactionInstruction.getAmount());
        System.out.println("Deposit: " + accountRepository.findByAccountnumber(transactionInstruction.getAccountNumber()));
    }
    @Override
    @KafkaListener(topics = {"WithdrawMoney"})
    public void withdrawKafka(@Payload String transactionPayload) throws JsonProcessingException {
        System.out.println(transactionPayload);
        TransactionInstruction transactionInstruction = objectMapper.readValue(transactionPayload, TransactionInstruction.class);
        accountService.withdraw(transactionInstruction.getAccountNumber(), transactionInstruction.getAmount());
        System.out.println("Withdraw: " + accountRepository.findByAccountnumber(transactionInstruction.getAccountNumber()));
    }
}
