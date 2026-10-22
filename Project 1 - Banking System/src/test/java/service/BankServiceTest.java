package service;

import enumeration.AccountType;
import exception.AccountNotFoundException;
import exception.InvalidCredentialsException;
import exception.NegativeCurrentBalanceException;
import exception.NegativeSavingsBalanceException;
import model.Account;
import model.CurrentAccount;
import model.SavingsAccount;
import model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest
{
    @Mock private AccountRepository repository;
    @InjectMocks private BankService service;
    // No need for a separate setUp method to create a new mock before every test
    // Mockito extension does it for us
    private Account account;
    private long accountNumber;
    private final String password = "123";
    private final String wrongPassword = "456";
    private final long unknownAccountNumber = 999;
    //
    @BeforeEach
    public void setUp()
    {
        accountNumber = 1000;
        account = new SavingsAccount(accountNumber, password);
    }
    //
    @Test
    public void login_correctPassword_success()
    {
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(repository.checkPassword(accountNumber, password)).thenReturn(true);
        assertDoesNotThrow(()->service.login(accountNumber, password));
    }
    @Test
    public void login_wrongPassword_failure()
    {
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(repository.checkPassword(accountNumber, wrongPassword)).thenReturn(false);
        assertThrows(InvalidCredentialsException.class, ()->service.login(accountNumber,wrongPassword));
    }
    @Test
    public void login_unknownAccount_failure()
    {
        // Unstubbed method call returns default -> in this case, Optional.empty() is returned which is what we need
        assertThrows(AccountNotFoundException.class, ()->service.login(unknownAccountNumber,password));
    }
    @Test
    public void createAccount_firstAccount_success()
    {
        long number = service.createAccount(AccountType.SAVINGS, password);
        assertEquals(1000, number);
    }
    @Test
    public void createAccount_savingsAccount_success()
    {
        service.createAccount(AccountType.SAVINGS, password);
        verify(repository).save(eq(1000L), any(SavingsAccount.class));
    }
    @Test
    public void createAccount_currentAccount_success()
    {
        ArgumentCaptor <Account> captor = ArgumentCaptor.forClass(Account.class);
        service.createAccount(AccountType.CURRENT, password);
        verify(repository).save(eq(1000L), captor.capture());
        Account saved = captor.getValue();
        assertInstanceOf(CurrentAccount.class, saved);
    }
    @Test
    public void createAccount_twoAccounts_success()
    {
        long accountNumber1 = service.createAccount(AccountType.SAVINGS, password);
        long accountNumber2 = service.createAccount(AccountType.SAVINGS, password);
        assertEquals(1000L, accountNumber1);
        assertEquals(1001L, accountNumber2);
    }
    @Test
    public void deleteAccount_knownAccount_success()
    {
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        assertDoesNotThrow(()->service.deleteAccount(accountNumber));
        verify(repository).delete(accountNumber);
    }
    @Test
    public void deleteAccount_unknownAccount_failure()
    {
        // Unstubbed method call returns default -> in this case, Optional.empty() is returned which is what we need
        assertThrows(AccountNotFoundException.class, ()->service.deleteAccount(unknownAccountNumber));
        verify(repository, never()).delete(anyLong());
    }
    @Test
    public void deposit_success()
    {
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        service.deposit(accountNumber, 500);
        assertEquals(1000, account.getBalance());
    }
    @Test
    public void withdraw_savingsAccount_success()
    {
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account)); // initial balance  500
        assertDoesNotThrow(()->service.withdraw(accountNumber, 500));
        assertEquals(0, account.getBalance());
    }
    @Test
    public void withdraw_savingsAccount_failure()
    {
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        assertThrows(NegativeSavingsBalanceException.class, ()->service.withdraw(accountNumber, 1000));
    }
    @Test
    public void withdraw_currentAccount_success()
    {
        Account account1 = new CurrentAccount(accountNumber, password); // initial balance  500
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account1));
        assertDoesNotThrow(()->service.withdraw(accountNumber, 1000));
        assertEquals(-500, account1.getBalance());
    }
    @Test
    public void withdraw_currentAccount_failure()
    {
        Account account1 = new CurrentAccount(accountNumber, password);
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account1));
        assertThrows(NegativeCurrentBalanceException.class, ()->service.withdraw(accountNumber, 10000));
    }
    @Test
    public void viewBalance_savingsAccount_success()
    {
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        String str = service.viewBalance(accountNumber);
        assertTrue(str.contains("Savings"));
    }
    @Test
    public void viewBalance_currentAccount_success()
    {
        Account account1 = new CurrentAccount(accountNumber, password);
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account1));
        String str = service.viewBalance(accountNumber);
        assertTrue(str.contains("Current") && str.contains("Overdraft"));
    }
    @Test
    public void viewTransactionHistory_success()
    {
        account.deposit(500);
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        List<Transaction> list = service.viewTransactionHistory(accountNumber);
        assertEquals(2, list.size());
    }
    @Test
    public void viewTotalHoldings_success()
    {
        Account account1 = new SavingsAccount(1000, password);
        // 500
        Account account2 = new SavingsAccount(1001, password);
        // 500
        Account account3 = new CurrentAccount(1002, password);
        account3.deposit(500);
        // 1000
        Account account4 = new CurrentAccount(1003, password);
        account4.withdraw(3500);
        // -3000
        List <Account> list = new ArrayList<>(List.of(account1, account2, account3, account4));
        when(repository.findAllAccounts()).thenReturn(list);
        long totalHoldings = service.viewTotalHoldings();
        assertEquals(2000, totalHoldings);
    }
    @Test
    public void viewNetHoldings_success()
    {
        Account account1 = new SavingsAccount(1000, password);
        // 500
        Account account2 = new SavingsAccount(1001, password);
        // 500
        Account account3 = new CurrentAccount(1002, password);
        account3.deposit(500);
        // 1000
        Account account4 = new CurrentAccount(1003, password);
        account4.withdraw(3500);
        // -3000
        List <Account> list = new ArrayList<>(List.of(account1, account2, account3, account4));
        when(repository.findAllAccounts()).thenReturn(list);
        long totalHoldings = service.viewNetHoldings();
        assertEquals(-1000, totalHoldings);
    }
    @Test
    public void viewTop3Accounts_moreThan3_success()
    {
        Account account1 = new SavingsAccount(1000, password); // 500
        Account account2 = new SavingsAccount(1001, password);
        account2.deposit(500); // 1000
        Account account3 = new CurrentAccount(1002, password);
        account3.deposit(1500); // 2000
        Account account4 = new CurrentAccount(1003, password);
        account4.deposit(3500); // 4000
        //
        List <Account> list = new ArrayList<>(List.of(account1, account3, account2, account4));
        when(repository.findAllAccounts()).thenReturn(list);
        List <Account> list1 = new ArrayList<>(List.of(account4, account3, account2));
        assertEquals(list1, service.viewTop3Accounts());
    }
    @Test
    public void viewTop3Accounts_lessThan3_success()
    {
        Account account1 = new SavingsAccount(1000, password); // 500
        Account account2 = new SavingsAccount(1001, password);
        account2.deposit(500); // 1000
        //
        List <Account> list = new ArrayList<>(List.of(account1, account2));
        when(repository.findAllAccounts()).thenReturn(list);
        List <Account> list1 = new ArrayList<>(List.of(account2, account1));
        assertEquals(list1, service.viewTop3Accounts());
    }
    @Test
    public void viewTop5Accounts_moreThan5_success()
    {
        Account account1 = new SavingsAccount(1000, password); // 500
        Account account2 = new SavingsAccount(1001, password);
        account2.deposit(500); // 1000
        Account account3 = new CurrentAccount(1002, password);
        account3.deposit(1500); // 2000
        Account account4 = new CurrentAccount(1003, password);
        account4.deposit(3500); // 4000
        Account account5 = new CurrentAccount(1004, password);
        account5.deposit(5000); // 5500
        Account account6 = new CurrentAccount(1005, password);
        account6.deposit(5500); // 6000
        //
        List <Account>  list = new ArrayList<>(List.of(account6, account2, account5, account4, account3, account1));
        when(repository.findAllAccounts()).thenReturn(list);
        List <Long> list1 = new ArrayList <> (List.of(6000L, 5500L, 4000L, 2000L, 1000L));
        assertEquals(list1, service.viewTop5Accounts());
    }
    @Test
    public void viewTop5Accounts_lessThan5_success()
    {
        Account account1 = new SavingsAccount(1000, password); // 500
        Account account2 = new SavingsAccount(1001, password);
        account2.deposit(500); // 1000
        Account account3 = new CurrentAccount(1002, password);
        account3.deposit(1500); // 2000
        //
        List <Account>  list = new ArrayList<>(List.of(account1, account3, account2));
        when(repository.findAllAccounts()).thenReturn(list);
        List <Long> list1 = new ArrayList <> (List.of(2000L, 1000L, 500L));
        assertEquals(list1, service.viewTop5Accounts());
    }
    @Test
    public void viewCountOfAccounts_success()
    {
        Account account1 = new SavingsAccount(1000, password);
        Account account2 = new SavingsAccount(1001, password);
        Account account3 = new CurrentAccount(1002, password);
        Account account4 = new CurrentAccount(1003, password);
        Account account5 = new CurrentAccount(1004, password);
        Account account6 = new CurrentAccount(1005, password);
        //
        List <Account>  list = new ArrayList<>(List.of(account1, account2, account3, account4, account5, account6));
        when(repository.findAllAccounts()).thenReturn(list);
        long a [] = new long [] {2, 4};
        assertArrayEquals(a, service.viewCountOfAccounts());
    }
}
