package model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public sealed abstract class Account permits SavingsAccount, CurrentAccount
{
    protected long accountNumber;
    protected String password;
    protected long balance;
    protected List <Transaction> transactions = new ArrayList<Transaction>();
    //
    public Account(long accountNumber, String password)
    {
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = 500; // No minimum balance rule - opening balance initialized to 500
        transactions.add(new Transaction("Deposit", 500, Instant.now(), 500));
    }
    //
    public long getAccountNumber() {
        return accountNumber;
    }
    public String getPassword() {
        return password;
    }
    public long getBalance() {
        return balance;
    }
    public void setBalance(long balance) {
        this.balance = balance;
    }
    public List<Transaction> getTransactions() {
        return transactions;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    //
    public long deposit(long amount)
    {
        setBalance(getBalance() + amount);
        transactions.add(new Transaction("Deposit", amount, Instant.now(), this.balance));
        return getBalance();
    }
    //
    public abstract long withdraw(long amount);
    //
    @Override
    public String toString()
    {
        return """
                Account No.: %d
                Balance: %d """.formatted(getAccountNumber(), getBalance());
    }
}
