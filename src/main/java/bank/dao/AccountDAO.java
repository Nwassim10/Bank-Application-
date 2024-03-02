package bank.dao;

import bank.domain.Account;

import java.util.Collection;



public interface AccountDAO {
	public void saveAccount(Account account);
	public void updateAccount(Account account);
	public Account loadAccount(long accountnumber);
	public Collection<Account> getAccounts();
}
