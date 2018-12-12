package com.capgemini.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capgemini.exceptions.InsufficientBalanceException;
import com.capgemini.exceptions.InsufficientInitialAmountException;
import com.capgemini.exceptions.InvalidAccountNumberException;
import com.capgemini.model.Account;
import com.capgemini.repository.AccountRepository;
import com.capgemini.service.AccountService;
import com.capgemini.service.AccountServiceImpl;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
public class AccountTest {

	AccountService accountService;
	
	@Mock
	AccountRepository accountRepository;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		accountService = new AccountServiceImpl(accountRepository);
	}

	/*
	 * create account
	 * 1.when the amount is less than 500 then system should throw exception
	 * 2.when the valid info is passed account should be created successfully
	 */
	
	@Test(expected=com.capgemini.exceptions.InsufficientInitialAmountException.class)
	public void whenTheAmountIsLessThan500SystemShouldThrowException() throws InsufficientInitialAmountException
	{
		accountService.createAccount(101, 400);
	}
	
	@Test
	public void whenTheValidInfoIsPassedAccountShouldBeCreatedSuccessfully() throws InsufficientInitialAmountException
	{
		Account account =new Account();
		account.setAccountNumber(101);
		account.setAmount(5000);
		when(accountRepository.save(account)).thenReturn(true);
		assertEquals(account, accountService.createAccount(101, 5000));
	}
	@Test
	public void whenAccountExistsOrNot() throws  InvalidAccountNumberException{
		Account account= new Account();
		account.setAccountNumber(1234567890);
		account.setAmount(2000);
		when(accountRepository.searchAccount(account.getAccountNumber())).thenReturn(account);
		assertEquals(account, accountService.depositAmount(1234567890, 2000));
	}
	@Test(expected=com.capgemini.exceptions.InvalidAccountNumberException.class)
	public void whenTheAccountnumberIsInvalidWhileWithdraw() throws InvalidAccountNumberException, InsufficientBalanceException{
		accountService.withdrawAmount(1234567890, 100);
	}
	
	@Test(expected=com.capgemini.exceptions.InsufficientBalanceException.class)
	public void whenTheAmountIsInSufficientWhileWithdraw() throws InvalidAccountNumberException, InsufficientBalanceException{
		Account account= new Account();
		account.setAccountNumber(1234567890);
		account.setAmount(500);
		when(accountRepository.searchAccount(account.getAccountNumber())).thenReturn(account);
		accountService.withdrawAmount(1234567890, 100);
	}
	
	@Test
	public void whenTheWithdrawIsDoneSuccessfully() throws InvalidAccountNumberException, InsufficientBalanceException{
		Account account= new Account();
		account.setAccountNumber(1234567890);
		account.setAmount(2000);
		when(accountRepository.searchAccount(account.getAccountNumber())).thenReturn(account);
		assertEquals(account, accountService.withdrawAmount(1234567890, 2000));
	} 
}

