package service;

import enumeration.AccountType;
import exception.*;
import factories.AccountCreator;
import factories.CurrentAccountCreator;
import factories.SavingsAccountCreator;
import model.Account;
import model.CurrentAccount;
import model.SavingsAccount;
import model.Transaction;
import repository.AccountRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// account creation
// account deletion
// login
// deposit
// withdraw
// get balance
// get history
// find account

public class BankService
{
    // private static final BankService instance = new BankService();
    // Eager Loading
    // This runs when the JVM loads and initializes the class - therefore, is threadsafe
    // JVM guarantees that class initialization is done only once - locked
    //
    private static BankService instance;
    // Lazy Loading - an object is created only when it is first requested and its instance is returned everytime
    private long countAccountNumber = 1000;
    private final AccountRepository repository = AccountRepository.getInstance();
    //
    private BankService()
    {
    }
    //
    public static synchronized BankService getInstance()
    {
        if (instance == null)
        {
            instance = new BankService();
        }
        return instance;
    }
    //
    public long createAccount(AccountType type, String password)
    {
        AccountCreator creator;
        switch(type)
        {
            case SAVINGS ->
            {
                creator = new SavingsAccountCreator();
                Account account = creator.createAccount(countAccountNumber, password);
                repository.save(countAccountNumber, account);
            }
            //
            case CURRENT ->
            {
                creator = new CurrentAccountCreator();
                Account account = creator.createAccount(countAccountNumber, password);
                repository.save(countAccountNumber, account);
            }
        }
        return countAccountNumber++;
    }
    //
    public void deleteAccount(long accountNumber) throws AccountNotFoundException
    {
        if(repository.findByAccountNumber(accountNumber).isPresent())
        {
            repository.delete(accountNumber);
        }
        else
        {
            throw new AccountNotFoundException("Account not found.");
        }
    }
    //
    public void login(long accountNumber, String password) throws AccountNotFoundException, InvalidCredentialsException
    {
        if(repository.findByAccountNumber(accountNumber).isEmpty())
        {
            throw new AccountNotFoundException("Account not found");
        }
        if(!repository.checkPassword(accountNumber, password))
        {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }
    //
    public long deposit(long accountNumber, long amount)
    {
        Account account = repository.findByAccountNumber(accountNumber).get();
        return account.deposit(amount);
    }
    //
    public long withdraw(long accountNumber, long amount) throws NegativeSavingsBalanceException, NegativeCurrentBalanceException
    {
        Account account = repository.findByAccountNumber(accountNumber).get();
        return account.withdraw(amount);
    }
    //
    public String viewBalance(long accountNumber)
    {
        Account account = repository.findByAccountNumber(accountNumber).get();
        String accountBalanceInfo;
        if(account instanceof CurrentAccount)
        {
            accountBalanceInfo =
                    """
                    Account type: Current
                    Current Balance: %d
                    Overdraft Limit: %d """.formatted(account.getBalance(), ((CurrentAccount) account).getOverdraftLimit());
        }
        else
        {
            accountBalanceInfo =
                    """
                    Account type: Savings
                    Current Balance: %d """.formatted(account.getBalance());
        }
        return accountBalanceInfo;
    }
    //
    public List<Transaction> viewTransactionHistory(long accountNumber)
    {
        Account account = repository.findByAccountNumber(accountNumber).get();
        return account.getTransactions();
    }
    //
    public long viewTotalHoldings ()
    {
        List <Account> accountList = repository.findAllAccounts();
        long totalHoldings = accountList.stream()
                .filter(acc -> acc.getBalance()>0)
                .mapToLong(acc -> acc.getBalance()).sum();
        return totalHoldings;
    }
    //
    public long viewNetHoldings()
    {
        List <Account> accountList = repository.findAllAccounts();
        long netHoldings = accountList.stream()
                .mapToLong(Account::getBalance)
                .sum();
        return netHoldings;
    }
    //
    public List <Account> viewTop3Accounts()
    {
        List <Account> accountList = repository.findAllAccounts().stream()
                .sorted(Comparator.comparingDouble(Account::getBalance).reversed())
                // compares given objects based on a property, here -> balance
                .limit(3)
                .toList();
        return accountList;
    }
    //
    public List <Long> viewTop5Accounts()
    {
        List <Long> balanceList = repository.findAllAccounts().stream()
                .map(Account::getBalance)
                .sorted(Comparator.reverseOrder())
                .limit(5)
                .toList();
        return balanceList;
    }
    //
    public long [] viewCountOfAccounts()
    {
        long a [] = new long [2];
        List <Account> accountList = repository.findAllAccounts();
        a[0] = accountList.stream().filter(acc -> acc instanceof SavingsAccount).count();
        a[1] = accountList.stream().filter(acc -> acc instanceof CurrentAccount).count();
        return a;
    }
}
