package factories;

import model.Account;
import model.SavingsAccount;

public final class SavingsAccountCreator implements AccountCreator
{
    @Override
    public SavingsAccount createAccount(long accountNumber, String password)
    {
        return new SavingsAccount(accountNumber, password);
    }
}
