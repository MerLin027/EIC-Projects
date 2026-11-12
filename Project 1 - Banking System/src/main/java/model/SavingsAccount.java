package model;

import exception.NegativeSavingsBalanceException;

import java.time.Instant;

/** A savings account. The balance can never go below zero. */


public final class SavingsAccount extends Account
{
    //
    public SavingsAccount(long accountNumber, String password)
    {
        super(accountNumber, password);
    }
    /**
     * Withdraws money if the balance covers it.
     *
     * @param amount the amount to withdraw
     * @return the new balance
     * @throws NegativeSavingsBalanceException if the balance is lower than {@code amount}
     */
    @Override
    public synchronized long withdraw(long amount) throws NegativeSavingsBalanceException
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
