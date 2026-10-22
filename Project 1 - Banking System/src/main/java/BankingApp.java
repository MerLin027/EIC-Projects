import enumeration.AccountMenu;
import enumeration.AccountType;
import enumeration.AppMenu;
import enumeration.ConsoleColor;
import exception.*;
import model.Account;
import model.Transaction;
import service.BankService;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.io.Console;

public final class BankingApp
{
    private final BankService service = BankService.getInstance();
    private final Scanner sc = new Scanner(System.in);
    //
    public final Consumer <String> printSuccess = message -> System.out.println(ConsoleColor.GREEN.getCode() + message + ConsoleColor.RESET.getCode());
    public final Consumer <String> printError = message -> System.out.println(ConsoleColor.RED.getCode() + message + ConsoleColor.RESET.getCode());
    public final Consumer <String> printExit = message -> System.out.println(ConsoleColor.BLUE.getCode() + message + ConsoleColor.RESET.getCode());
    //
    public final Supplier <Integer> readInt = () ->
    {
        while(true)
        {
            String str = sc.nextLine().trim();
            try
            {
                return Integer.parseInt(str);
            }
            catch (NumberFormatException e)
            {
                printError.accept("Please enter a valid number.");
            }
        }
    };
    public final Supplier <Long> readLong = () ->
    {
        while(true)
        {
            String str = sc.nextLine().trim();
            try
            {
                return Long.parseLong(str);
            }
            catch (NumberFormatException e)
            {
                printError.accept("Please enter a valid number.");
            }
        }
    };
    //
    public final Supplier <String> readPassword = () ->
    {
        Console console = System.console(); // it is an object allowing us to communicate with the system terminal/real console
        if(console == null)
        {
            return sc.nextLine();
            // Fallback, if a real terminal is not detected, it will function as normal
            // If a real terminal is detected, it will use the masking
        }
        char[] chars = console.readPassword();
        // reads the input without showing anything, returns null if i/o stream is closed
        // returns the password as char []
        if(chars == null)
            return ""; // prevents NullPointerException
        String password = new String(chars);
        Arrays.fill(chars, ' '); // overwrites the password with blank spaces - good practice
        return password;
    };
    //
    BankingApp()
    {
    }
    //
    public void appMenu(String appMenuText, String accountMenuText, String accountTypeMenu)
    {
        int choice;
        int tempChoice;
        AccountType type;
        String password;
        long accountNumber, totalHoldings, netHoldings;
        String intro = "Welcome to MerLin's Banking App!!";
        printSuccess.accept(intro);
        while (true)
        {
            System.out.println(appMenuText);
            choice = readInt.get();
            Optional <AppMenu> ch = AppMenu.lookup(choice);
            if (ch.isEmpty())
            {
                printError.accept("Invalid choice. Please try again.");
                continue;
            }
            switch (ch.get())
            {
                case CREATE_ACCOUNT:
                    type = null; // resets AccountType to null
                    System.out.println("Choose account type: ");
                    System.out.println(accountTypeMenu);
                    do
                    {
                        tempChoice = readInt.get();
                        switch (tempChoice)
                        {
                            case 1 -> type = AccountType.SAVINGS;
                            case 2 -> type = AccountType.CURRENT;
                            default -> printError.accept("Invalid choice, please try again.");
                        }
                    } while(type == null);
                    System.out.println("Enter password: ");
                    password = readPassword.get();
                    accountNumber = service.createAccount(type, password);
                    printSuccess.accept("Generated Account Number: " + accountNumber);
                    printSuccess.accept("Account has been saved successfully."+"\n"+"Please do not forget your Account Number or Password.");
                    break;
                case DELETE_ACCOUNT:
                    System.out.println("Enter account number to be deleted: ");
                    accountNumber = readLong.get();
                    try
                    {
                        service.deleteAccount(accountNumber);
                        printSuccess.accept("Account has been deleted successfully.");
                    }
                    catch (AccountNotFoundException e)
                    {
                        printError.accept(e.getMessage());
                    }
                    break;
                case LOGIN:
                    System.out.println("Enter account number: ");
                    accountNumber = readLong.get();
                    System.out.println("Enter password: ");
                    password = readPassword.get();
                    try
                    {
                        service.login(accountNumber, password);
                        accountMenu(accountMenuText, accountNumber);
                    }
                    catch (AccountNotFoundException | InvalidCredentialsException e)
                    {
                        printError.accept("Login failed: "+e.getMessage());
                    }
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
                    List <Account> accountList = service.viewTop3Accounts();
                    System.out.println("Top 3 Accounts: ");
                    int count = 1;
                    for (Account account : accountList)
                    {
                        System.out.printf("------------- Account  %s -------------%n", count++);
                        System.out.println(account);
                        System.out.println("------------------------------------------");
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
                case EXIT:
                    printExit.accept("Exiting...");
                    System.exit(0);
            }
        }
    }
    //
    public void accountMenu(String accountMenuText, long accountNumber)
    {
        long accountBalance;
        long amount;
        String accountBalanceInfo, newPassword, password;
        int maxAttempts;
        boolean updated;
        List<Transaction> transactionHistory;
        //
        printSuccess.accept("Welcome, "+accountNumber);
        while (true)
        {
            System.out.println(accountMenuText);
            int choice = readInt.get();
            Optional <AccountMenu> ch = AccountMenu.lookup(choice);
            if (ch.isEmpty())
            {
                printError.accept("Invalid choice. Please try again.");
                continue;
            }
            switch (ch.get())
            {
                case DEPOSIT:
                    System.out.println("Enter the amount to be deposited: ");
                    amount = readLong.get();
                    if(amount <= 0)
                    {
                        printError.accept("Invalid amount, please enter a positive number.");
                        break;
                    }
                    accountBalance = service.deposit(accountNumber, amount);
                    printSuccess.accept("Amount has successfully been deposited.");
                    System.out.println("New Balance: " + accountBalance);
                    break;
                case WITHDRAW:
                    System.out.println("Enter amount to be withdrawn: ");
                    amount = readLong.get();
                    if(amount <= 0)
                    {
                        printError.accept("Invalid amount, please enter a positive number.");
                        break;
                    }
                    try
                    {
                        accountBalance = service.withdraw(accountNumber, amount);
                        printSuccess.accept("Amount has successfully been withdrawn.");
                        System.out.println("New Balance: " + accountBalance);
                    }
                    catch (NegativeSavingsBalanceException | NegativeCurrentBalanceException e)
                    {
                        printError.accept("Invalid Action, Cannot withdraw due to: "+e.getMessage());
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
                        System.out.printf("------------- Transaction  %s -------------%n", count++);
                        System.out.println(t);
                        System.out.println("------------------------------------------");
                    }
                    break;
                case UPDATE_PASSWORD:
                    maxAttempts = 3;
                    updated = false;
                    for(int i=0;i<maxAttempts;i++)
                    {
                        System.out.println("Enter current password: ");
                        password = readPassword.get();
                        System.out.println("Enter new password: ");
                        newPassword = readPassword.get();
                        try
                        {
                            service.updatePassword(accountNumber, password, newPassword);
                            updated = true;
                            printSuccess.accept("Password has been updated successfully.");
                            break;
                        }
                        catch (InvalidCredentialsException e)
                        {
                            printError.accept(e.getMessage());
                            if(i!=maxAttempts-1)
                                printError.accept((maxAttempts-i-1)+" attempts left.");
                        }
                    }
                    if(!updated)
                        printError.accept("Too many failed attempts. Returning to the account menu.");
                    break;
                case LOGOUT:
                    printExit.accept("Logging Out...");
                    return;
            }
        }
    }
    //
    public static void main(String[] args)
    {
        //
        String appMenuText = """
                1 - Create Account
                2 - Delete Account
                3 - Login
                4 - View Total Bank Holdings
                5 - View Net Bank Holdings
                6 - View Top 3 Richest Accounts
                7 - View Top 5 Balances
                8 - Count of Accounts
                9 - Exit
                Enter your choice: """;
        //
        String accountMenuText = """
                1 - Deposit Money
                2 - Withdraw Money
                3 - View Balance
                4 - View Transaction History
                5 - Update Password
                6 - Logout
                Enter your choice: """;
        //
        String accountTypeMenu = """
                1 - Savings Account
                2 - Current Account """;
        // Application flow starts here
        BankingApp app = new BankingApp();
        try
        {
            app.appMenu(appMenuText, accountMenuText, accountTypeMenu);
        }
        catch (NoSuchElementException e)
        {
            app.printError.accept("Input stream closed. Exiting...");
        }
        catch (Exception e)
        {
            app.printError.accept("Something went wrong...\n"+e.getMessage());
        }
    }
}
