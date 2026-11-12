package factory;

import model.SavingsAccount;

/** Creates {@link SavingsAccount} instances. */

public final class SavingsAccountCreator implements AccountCreator
{
    @Override
    public SavingsAccount createAccount(long accountNumber, String password)
    {
        return new SavingsAccount(accountNumber, password);
    }
}
