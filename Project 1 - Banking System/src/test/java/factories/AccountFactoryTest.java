package factories;

import model.Account;
import model.CurrentAccount;
import model.SavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountFactoryTest
{
    @Test
    public void createAccount_returnsSavingsAccount()
    {
        AccountCreator creator = new SavingsAccountCreator();
        Account account = creator.createAccount(1000, "123");
        assertInstanceOf(SavingsAccount.class, account);
    }
    @Test
    public void createAccount_returnsCurrentAccount()
    {
         AccountCreator creator = new CurrentAccountCreator();
         Account account = creator.createAccount(1000, "123");
         assertInstanceOf(CurrentAccount.class, account);
    }
    @Test
    public void savingsCreator_createAccount_setsAccountNumberAndPassword()
    {
        AccountCreator creator = new SavingsAccountCreator();
        Account account = creator.createAccount(1000, "123");
        assertEquals(1000, account.getAccountNumber());
        assertEquals("123", account.getPassword());
    }
    @Test
    public void currentCreator_createAccount_setsAccountNumberAndPassword()
    {
        AccountCreator creator = new CurrentAccountCreator();
        Account account = creator.createAccount(1000, "123");
        assertEquals(1000, account.getAccountNumber());
        assertEquals("123", account.getPassword());
    }
}
