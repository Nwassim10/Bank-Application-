package bank.service;

import bank.dto.AccountDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.messaging.handler.annotation.Payload;

public interface KafkaService {
    public AccountDTO createAccountKafka(String accountPayload) throws JsonProcessingException;
    public void depositKafka(String transactionPayload) throws JsonProcessingException;
    public void withdrawKafka(String transactionPayload) throws JsonProcessingException;
}
