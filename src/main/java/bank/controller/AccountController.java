package bank.controller;

import bank.dto.AccountDTO;
import bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@CrossOrigin
public class AccountController {
    @Autowired
    AccountService accountService;
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Map<String, String> data){
        Long accountNumber = Long.valueOf(data.get("accountNumber"));
        String customerName = data.get("customerName");
        AccountDTO accountDTO = accountService.createAccount(accountNumber, customerName);
        return new ResponseEntity<AccountDTO>(accountDTO,HttpStatus.OK);
    }
    @PatchMapping("/deposit/{accountNumber}")
    public ResponseEntity<?> deposit(@PathVariable Long accountNumber, @RequestBody Double amount){
        accountService.deposit(accountNumber, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PatchMapping("/withdraw-euros/{accountNumber}")
    public ResponseEntity<?> withdrawEuros(@PathVariable Long accountNumber, @RequestBody Double amount){
        accountService.withdrawEuros(accountNumber, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PatchMapping("/deposit-euros/{accountNumber}")
    public ResponseEntity<?> depositEuros(@PathVariable Long accountNumber, @RequestBody Double amount){
        accountService.depositEuros(accountNumber, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PatchMapping("/transfer-funds/{fromAccountNumber}/{toAccountNumber}")
    public ResponseEntity<?> transferFunds(@PathVariable("fromAccountNumber") Long fromAccountNumber, @PathVariable("toAccountNumber") Long toAccountNumber, @RequestBody Map<String, String> data){
        double amount = Double.parseDouble(data.get("amount"));
        String description = data.get("description");
        accountService.transferFunds(fromAccountNumber,toAccountNumber,amount,description);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<?> getAllAccounts(){
        Collection<AccountDTO> accountDTOS = accountService.getAllAccounts();
        return new ResponseEntity<Collection<AccountDTO>>(accountDTOS, HttpStatus.OK);
    }
}
