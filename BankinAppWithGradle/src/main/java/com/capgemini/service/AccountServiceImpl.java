package com.capgemini.service;

import com.capgemini.exceptions.InsufficientBalanceException;
import com.capgemini.exceptions.InsufficientInitialAmountException;
import com.capgemini.exceptions.InvalidAccountNumberException;
import com.capgemini.model.Account;
import com.capgemini.repository.AccountRepository;

public class AccountServiceImpl implements AccountService {
	
	/* (non-Javadoc)
	 * @see com.capgemini.service.AccountService#createAccount(int, int)
	 */
	
	AccountRepository accountRepository;
	
	
	public AccountServiceImpl(AccountRepository accountRepository) {
		super();
		this.accountRepository = accountRepository;
	}


	@Override
	public Account createAccount(int accountNumber,int amount) throws InsufficientInitialAmountException
	{
		int a,b,c;
		if(amount<500)
		{
			throw new InsufficientInitialAmountException();
		}
		Account account = new Account();
		account.setAccountNumber(accountNumber);
		
		account.setAmount(amount);
		 
		if(accountRepository.save(account))
		{
			return account;
		}
	     
		return null;
		
	}


	@Override
	public Account depositAmount(int accountNumber, int amount) throws InvalidAccountNumberException {
		Account account=accountRepository.searchAccount(accountNumber);
		if(null==account){
			throw new InvalidAccountNumberException();
		}
		else{
			int bal=account.getAmount();
			account.setAmount(bal+amount);
			accountRepository.save(account);
			return account;
		}
	}
	@Override
	public Account withdrawAmount(int accountNumber, int amount) throws InsufficientBalanceException, InvalidAccountNumberException {
		Account account=accountRepository.searchAccount(accountNumber);
		System.out.println("exception added");
		if(null==account){
			throw new InvalidAccountNumberException();
		}
		else{
			int bal=account.getAmount();
			if(amount<bal){
				throw new InsufficientBalanceException();
			}else{
				account.setAmount(bal-amount);
				accountRepository.save(account);
				return account;
			}
		}
	}
}
