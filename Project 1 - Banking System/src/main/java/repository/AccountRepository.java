package repository;

import model.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public final class AccountRepository
{
    private static volatile AccountRepository instance;
    private final HashMap <Long, Account> accounts = new HashMap <> ();
    //
    AccountRepository()
    {
    }
    //
    public static AccountRepository getInstance()
    {
        if(instance == null)
        {
            synchronized (AccountRepository.class)
            {
                if(instance == null)
                {
                    instance = new AccountRepository();
                }
            }
        }
        return instance;
    }
    //
    public void save(long accountNumber, Account acc)
    {
        accounts.put(accountNumber, acc);
    }
    //
    public void delete(long accountNumber)
    {
        accounts.remove(accountNumber);
    }
    //
    public Optional<Account> findByAccountNumber(long accountNumber)
    {
        return Optional.ofNullable(accounts.get(accountNumber));
    }
    //
    public boolean checkPassword(long accountNumber, String password)
    {
        return accounts.get(accountNumber).getPassword().equals(password);
    }
    //
    public List <Account> findAllAccounts()
    {
        return accounts.values().stream().toList();
    }
}
