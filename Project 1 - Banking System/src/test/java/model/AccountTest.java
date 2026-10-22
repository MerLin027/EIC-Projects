package model;

import exception.NegativeCurrentBalanceException;
import exception.NegativeSavingsBalanceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Convention - method_situation_expectedResult

public class AccountTest
{
    // Initial balance is set to 500 by default (constructor)
    // Overdraft Limit is set to 5000 for Current Accounts by default (constructor)
    @Test
    public void deposit_successful()
    {
       SavingsAccount account = new SavingsAccount(1000, "123");
       account.deposit(500);
       assertEquals(1000,account.getBalance());
    }
    @Test
    public void savings_withdraw_belowBalance_successful()
    {
        SavingsAccount account = new SavingsAccount(1000, "123");
        account.deposit(500);
        account.withdraw(500);
        assertEquals(500,account.getBalance());
    }
    @Test
    public void current_withdraw_belowBalance_successful()
    {
        CurrentAccount account = new CurrentAccount(1000, "123");
        account.deposit(500);
        account.withdraw(2000);
        assertEquals(-1000,account.getBalance());
    }

    @Test
    public void savings_withdraw_belowBalance_failure()
    {
        SavingsAccount account = new SavingsAccount(1000, "123");
        account.deposit(500);
        assertThrows(NegativeSavingsBalanceException.class, () -> account.withdraw(2000));
    }
    @Test
    public void current_withdraw_belowBalance_failure()
    {
        CurrentAccount account = new CurrentAccount(1000, "123");
        assertThrows(NegativeCurrentBalanceException.class, () -> account.withdraw(6000));
    }
}
