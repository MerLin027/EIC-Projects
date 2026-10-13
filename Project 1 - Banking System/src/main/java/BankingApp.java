import enumeration.AccountMenu;
import enumeration.AccountType;
import enumeration.AppMenu;
import exception.*;
import model.Account;
import model.Transaction;
import service.BankService;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.Arrays;
import java.io.Console;

public final class BankingApp
{
    private final BankService service = BankService.getInstance();
    private final Scanner sc = new Scanner(System.in);
    private final Supplier <Integer> readInt = () ->
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
                System.out.println("Please enter a valid number.");
            }
        }
    };
    private final Supplier <Long> readLong = () ->
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
                System.out.println("Please enter a valid number.");
            }
        }
    };
    //
    Supplier <String> readPassword = () ->
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
        System.out.println(intro);
        while (true)
        {
            System.out.println(appMenuText);
            choice = readInt.get();
            Optional <AppMenu> ch = AppMenu.lookup(choice);
            if (ch.isEmpty())
            {
                System.out.println("Invalid choice. Please try again.");
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
                            default -> System.out.println("Invalid choice, please try again.");
                        }
                    } while(type == null);
                    System.out.println("Enter password: ");
                    password = readPassword.get();
                    accountNumber = service.createAccount(type, password);
                    System.out.println("Generated Account Number: " + accountNumber);
                    System.out.println("Account has been saved successfully."+"\n"+"Please do not forget your Account Number or Password.");
                    break;
                case DELETE_ACCOUNT:
                    System.out.println("Enter account number to be deleted: ");
                    accountNumber = readLong.get();
                    try
                    {
                        service.deleteAccount(accountNumber);
                        System.out.println("Account has been deleted successfully.");
                    }
                    catch (AccountNotFoundException e)
                    {
                        System.out.println(e.getMessage());
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
                        System.out.println("Login failed: "+e.getMessage());
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
                    System.out.println("Exiting...");
                    System.exit(0);
            }
        }
    }
    //
    public void accountMenu(String accountMenuText, long accountNumber)
    {
        long accountBalance;
        long amount;
        String accountBalanceInfo;
        List<Transaction> transactionHistory;
        System.out.println("Welcome, "+accountNumber);
        while (true)
        {
            System.out.println(accountMenuText);
            int choice = readInt.get();
            Optional <AccountMenu> ch = AccountMenu.lookup(choice);
            if (ch.isEmpty())
            {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }
            switch (ch.get())
            {
                case DEPOSIT:
                    System.out.println("Enter the amount to be deposited: ");
                    amount = readLong.get();
                    if(amount <= 0)
                    {
                        System.out.println("Invalid amount, please enter a positive number.");
                        break;
                    }
                    accountBalance = service.deposit(accountNumber, amount);
                    System.out.println("Amount has successfully been deposited.");
                    System.out.println("New Balance: " + accountBalance);
                    break;
                case WITHDRAW:
                    System.out.println("Enter amount to be withdrawn: ");
                    amount = readLong.get();
                    if(amount <= 0)
                    {
                        System.out.println("Invalid amount, please enter a positive number.");
                        break;
                    }
                    try
                    {
                        accountBalance = service.withdraw(accountNumber, amount);
                        System.out.println("Amount has successfully been withdrawn.");
                        System.out.println("New Balance: " + accountBalance);
                    }
                    catch (NegativeSavingsBalanceException | NegativeCurrentBalanceException e)
                    {
                        System.out.println("Invalid Action, Cannot withdraw due to: "+e.getMessage());
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
                case LOGOUT:
                    System.out.println("Logging Out...");
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
                5 - Logout
                Enter your choice: """;
        //
        String accountTypeMenu = """
                1 - Savings Account
                2 - Current Account """;
        // Application flow starts here
        BankingApp app = new BankingApp();
        app.appMenu(appMenuText, accountMenuText, accountTypeMenu);
    }
}
