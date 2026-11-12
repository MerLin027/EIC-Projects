package model;

import exception.NegativeCurrentBalanceException;

import java.time.Instant;

/** A current account. The balance may go negative, down to the overdraft limit (5000 by default). */

public final class CurrentAccount extends Account
{
    long overdraftLimit = 5000;
    //
    public CurrentAccount(long accountNumber, String password)
    {
        super(accountNumber, password);
    }
    /**
     * Withdraws money as long as the balance does not fall below the negative overdraft limit.
     *
     * @param amount the amount to withdraw
     * @return the new balance
     * @throws NegativeCurrentBalanceException if the withdrawal would exceed the overdraft limit
     */
    @Override
    public synchronized long withdraw(long amount) throws NegativeCurrentBalanceException
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
    public long getOverdraftLimit()
    {
        return overdraftLimit;
    }
    public void setOverdraftLimit(long overdraftLimit)
    {
        this.overdraftLimit = overdraftLimit;
    }
}
