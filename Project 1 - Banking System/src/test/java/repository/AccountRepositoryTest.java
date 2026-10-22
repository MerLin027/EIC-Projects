package repository;

import model.Account;
import model.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class AccountRepositoryTest
{
    private AccountRepository repository;
    @BeforeEach
    public void setUp()
    {
        repository = new AccountRepository(); // fresh method for each Test
    }
    //
    @Test
    public void save_thenFindAccount_successful()
    {
        Account account = new SavingsAccount(1000, "123");
        repository.save(1000, account);
        Optional <Account> result = repository.findByAccountNumber(account.getAccountNumber());
        assertTrue(result.isPresent());
        assertSame(account, result.get());
    }
    @Test
    public void findAllAccounts_unknownAccountNumber_returnsEmpty()
    {
        Optional <Account> result = repository.findByAccountNumber(999);
        assertTrue(result.isEmpty());
    }
    @Test
    public void delete_existingAccount_successful()
    {
        Account account = new SavingsAccount(1000, "123");
        repository.save(1000, account);
        repository.delete(account.getAccountNumber());
        assertTrue (repository.findByAccountNumber(account.getAccountNumber()).isEmpty());
    }
    @Test
    public void checkPassword_correctPassword_returnsTrue()
    {
        Account account = new SavingsAccount(1000, "123");
        repository.save(1000, account);
        String password = "123";
        boolean result = repository.checkPassword(account.getAccountNumber(), password);
        assertTrue(result);
    }
    @Test
    public void checkPassword_wrongPassword_returnsFalse()
    {
        Account account = new SavingsAccount(1000, "123");
        repository.save(1000, account);
        String password = "456";
        boolean result = repository.checkPassword(account.getAccountNumber(), password);
        assertFalse(result);
    }
}
