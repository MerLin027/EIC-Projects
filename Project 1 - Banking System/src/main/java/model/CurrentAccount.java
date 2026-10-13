package model;

import exception.NegativeCurrentBalanceException;

import java.time.Instant;

public final class CurrentAccount extends Account
{
    long overdraftLimit = 5000;
    //
    public CurrentAccount(long accountNumber, String password)
    {
        super(accountNumber, password);
    }
    //
    @Override
    public long withdraw(long amount) throws NegativeCurrentBalanceException
    {
        if(getBalance() >= amount)
        {
            setBalance(getBalance() - amount);
            transactions.add(new Transaction("Withdrawal", amount, Instant.now(), getBalance()));
            return getBalance();
        }
        else if((getBalance() - amount) >= -overdraftLimit)
        {
            setBalance(getBalance() - amount);
            transactions.add(new Transaction("Withdrawal", amount, Instant.now(), getBalance()));
            return getBalance();
        }
        else
            throw new NegativeCurrentBalanceException("Exceedance of Overdraft Limit");
    }
    //
    public long getOverdraftLimit() {
        return overdraftLimit;
    }
    public void setOverdraftLimit(long overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}
