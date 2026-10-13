package model;

import exception.NegativeSavingsBalanceException;

import java.time.Instant;

public final class SavingsAccount extends Account
{
    //
    public SavingsAccount(long accountNumber, String password)
    {
        super(accountNumber, password);
    }
    //
    @Override
    public long withdraw(long amount) throws NegativeSavingsBalanceException
    {
        if(getBalance() >= amount)
        {
            setBalance(getBalance() - amount);
            transactions.add(new Transaction("Withdrawal", amount, Instant.now(), getBalance()));
            return getBalance();
        }
        else
            throw new NegativeSavingsBalanceException("Insufficient Balance");
    }
}
