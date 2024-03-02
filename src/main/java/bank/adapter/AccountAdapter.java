package bank.adapter;

import bank.domain.Account;
import bank.dto.AccountDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountAdapter {

    public AccountDTO entityToDto(Account account){
        return new AccountDTO(account.getAccountnumber(), account.getEntryList(), account.getCustomer());
    }

    public Account dtoToEntity(AccountDTO accountDTO){
        return new Account(accountDTO.getAccountnumber(), accountDTO.getEntryList(), accountDTO.getCustomer());
    }

    public Collection<AccountDTO> entitiesToAccountDTOs(Collection<Account> accounts){
        return accounts.stream().map(account -> entityToDto(account)).collect(Collectors.toList());
    }

    public Collection<Account> dtosToAccountDTOs(Collection<AccountDTO> accountDTOs){
        return accountDTOs.stream().map(accountDTO -> dtoToEntity(accountDTO)).collect(Collectors.toList());
    }

}
