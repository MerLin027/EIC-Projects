package factory;

import model.CurrentAccount;

public final class CurrentAccountCreator implements AccountCreator
{
    @Override
    public CurrentAccount createAccount(long countAccountNumber, String password)
    {
        return new CurrentAccount(countAccountNumber, password);
    }
}
