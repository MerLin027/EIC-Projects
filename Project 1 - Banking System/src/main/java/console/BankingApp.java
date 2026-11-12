package console;

import enumeration.AccountMenu;
import enumeration.AccountType;
import enumeration.AppMenu;
import exception.*;
import model.Account;
import model.Transaction;
import service.BankService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static console.Constants.*;

/**
 * Console entry point and menu flow for the banking system.
 *
 * <p>Handles user interaction only: it reads choices through {@link ConsoleIO}, calls
 * {@link BankService} and prints the results. Business rules live in the service layer.
 * Business exceptions are caught here and shown as messages, so bad input never crashes the program.
 */

public final class BankingApp
{
    private final BankService service = BankService.getInstance();
    private final ConsoleIO consoleIO = ConsoleIO.getInstance();
    //
    BankingApp()
    {
    }
    /**
     * Runs the main (logged-out) menu loop: create or delete an account, log in, view bank-wide
     * reports, run the multithreading demo, or exit. A successful login opens {@link #accountMenu}.
     */
    public void appMenu()
    {
        int choice;
        AccountType type;
        long accountNumber, totalHoldings, netHoldings;
        String password;
        consoleIO.printSuccess.accept("Welcome to MerLin's Banking App!!");
        while (true)
        {
            System.out.println(APP_MENU);
            choice = consoleIO.readInt.get();
            Optional<AppMenu> appMenuChoice = AppMenu.lookup(choice);
            if (appMenuChoice.isEmpty())
            {
                consoleIO.printError.accept(INVALID_CHOICE);
                continue;
            }
            switch (appMenuChoice.get())
            {
                case CREATE_ACCOUNT:
                    type = null; // resets AccountType to null
                    System.out.println("Choose account type: ");
                    System.out.println(ACCOUNT_TYPE_MENU);
                    do
                    {
                        choice = consoleIO.readInt.get();
                        Optional <AccountType> accountTypeChoice = AccountType.lookup(choice);
                        if(accountTypeChoice.isEmpty())
                        {
                            consoleIO.printError.accept(INVALID_CHOICE);
                        }
                        else
                            type = accountTypeChoice.get();
                    } while(type == null);
                    System.out.println("Enter password: ");
                    password = consoleIO.readPassword.get();
                    accountNumber = service.createAccount(type, password);
                    consoleIO.printSuccess.accept("Generated Account Number: " + accountNumber);
                    consoleIO.printSuccess.accept("Account has been saved successfully."
                            + "\n" + "Please do not forget your Account Number or Password.");
                    break;
                case DELETE_ACCOUNT:
                    System.out.println("Enter account number to be deleted: ");
                    accountNumber = consoleIO.readLong.get();
                    try
                    {
                        service.deleteAccount(accountNumber);
                        consoleIO.printSuccess.accept("Account has been deleted successfully.");
                    }
                    catch (AccountNotFoundException e)
                    {
                        consoleIO.printError.accept(e.getMessage());
                    }
                    break;
                case LOGIN:
                    System.out.println("Enter account number: ");
                    accountNumber = consoleIO.readLong.get();
                    System.out.println("Enter password: ");
                    password = consoleIO.readPassword.get();
                    try
                    {
                        service.login(accountNumber, password);
                    }
                    catch (AccountNotFoundException | InvalidCredentialsException e)
                    {
                        consoleIO.printError.accept("Login failed: "+e.getMessage());
                        continue;
                    }
                    accountMenu(accountNumber);
                    break;
                case TOTAL_BANK_HOLDINGS:
                    totalHoldings = service.viewTotalHoldings();
                    System.out.println("Total Bank Holdings: " + totalHoldings);
                    break;
                case NET_BANK_HOLDINGS:
                    netHoldings = service.viewNetHoldings();
                    System.out.println("Net Bank Holdings: " + netHoldings);
                    break;
                case TOP_3_ACCOUNTS:
                    List<Account> accountList = service.viewTop3Accounts();
                    System.out.println("Top 3 Accounts: ");
                    int count = 1;
                    for (Account account : accountList)
                    {
                        System.out.printf(ACCOUNT_DETAILS_HEADER, count++);
                        System.out.println(account);
                        System.out.println(FOOTER);
                    }
                    break;
                case TOP_5_BALANCES:
                    List <Long> balanceList = service.viewTop5Accounts();
                    System.out.println("Top 5 Accounts: ");
                    System.out.println(balanceList);
                    break;
                case COUNT_OF_ACCOUNTS:
                    long a [] = service.viewCountOfAccounts();
                    System.out.println("No. of Savings Accounts: " + a[0]);
                    System.out.println("No. of Current Accounts: " + a[1]);
                    break;
                case MULTITHREADING_DEMO:
                    try
                    {
                        service.multithreadingDemo();
                    }
                    catch (InterruptedException e)
                    {
                        consoleIO.printError.accept(e.getMessage());
                    }
                    break;
                case EXIT:
                    consoleIO.printExit.accept("Exiting...");
                    System.exit(0);
            }
        }
    }
    /**
     * Runs the menu loop for a logged-in account: deposit, withdraw, view balance and transactions,
     * and update the password. Returns to the caller on logout.
     *
     * @param accountNumber the account the user has logged in to
     */
    public void accountMenu(long accountNumber)
    {
        long accountBalance;
        long amount;
        String accountBalanceInfo, newPassword, password;
        boolean updated;
        List<Transaction> transactionHistory;
        //
        consoleIO.printSuccess.accept("Welcome, "+accountNumber);
        while (true)
        {
            System.out.println(ACCOUNT_MENU);
            int choice = consoleIO.readInt.get();
            Optional <AccountMenu> ch = AccountMenu.lookup(choice);
            if (ch.isEmpty())
            {
                consoleIO.printError.accept(INVALID_CHOICE);
                continue;
            }
            switch (ch.get())
            {
                case DEPOSIT:
                    System.out.println("Enter the amount to be deposited: ");
                    amount = consoleIO.readLong.get();
                    if(amount <= 0)
                    {
                        consoleIO.printError.accept(INVALID_AMOUNT);
                        break;
                    }
                    accountBalance = service.deposit(accountNumber, amount);
                    consoleIO.printSuccess.accept("Amount has successfully been deposited.");
                    System.out.println("New Balance: " + accountBalance);
                    break;
                case WITHDRAW:
                    System.out.println("Enter amount to be withdrawn: ");
                    amount = consoleIO.readLong.get();
                    if(amount <= 0)
                    {
                        consoleIO.printError.accept(INVALID_AMOUNT);
                        break;
                    }
                    try
                    {
                        accountBalance = service.withdraw(accountNumber, amount);
                        consoleIO.printSuccess.accept("Amount has successfully been withdrawn.");
                        System.out.println("New Balance: " + accountBalance);
                    }
                    catch (NegativeSavingsBalanceException | NegativeCurrentBalanceException e)
                    {
                        consoleIO.printError.accept("Invalid Action, Cannot withdraw due to: "+e.getMessage());
                    }
                    break;
                case VIEW_BALANCE:
                    accountBalanceInfo = service.viewBalance(accountNumber);
                    System.out.println(accountBalanceInfo);
                    break;
                case VIEW_TRANSACTIONS:
                    transactionHistory = service.viewTransactionHistory(accountNumber);
                    System.out.println("Transaction History: ");
                    int count = 1;
                    for(Transaction t : transactionHistory)
                    {
                        System.out.printf(TRANSACTION_DETAILS_HEADER, count++);
                        System.out.println(t);
                        System.out.println(FOOTER);
                    }
                    break;
                case UPDATE_PASSWORD:
                    updated = false;
                    for(int i=0;i<MAX_PASSWORD_ATTEMPTS;i++)
                    {
                        System.out.println("Enter current password: ");
                        password = consoleIO.readPassword.get();
                        System.out.println("Enter new password: ");
                        newPassword = consoleIO.readPassword.get();
                        try
                        {
                            service.updatePassword(accountNumber, password, newPassword);
                            updated = true;
                            consoleIO.printSuccess.accept("Password has been updated successfully.");
                            break;
                        }
                        catch (InvalidCredentialsException e)
                        {
                            consoleIO.printError.accept(e.getMessage());
                            if(i!=MAX_PASSWORD_ATTEMPTS-1)
                                consoleIO.printError.accept((MAX_PASSWORD_ATTEMPTS-i-1)+" attempts left.");
                        }
                    }
                    if(!updated)
                        consoleIO.printError.accept("Too many failed attempts. Returning to the account menu.");
                    break;
                case LOGOUT:
                    consoleIO.printExit.accept("Logging Out...");
                    return;
            }
        }
    }
    /**
     * Starts the application. A closed input stream (Ctrl+D / Ctrl+Z) ends the program with a short
     * message, and any unexpected error is reported instead of crashing.
     */
    public static void main(String[] args)
    {
        // Application flow starts here
        BankingApp app = new BankingApp();
        try
        {
            app.appMenu();
        }
        catch (NoSuchElementException e)
        {
            app.consoleIO.printError.accept("Input stream closed. Exiting...");
        }
        catch (Exception e)
        {
            app.consoleIO.printError.accept("Something went wrong:\n"+e.getMessage());
        }
    }
}
