package console;

/**
 * Fixed texts and limits used by the console classes. Package-private on purpose, so nothing
 * outside the {@code console} package can use it. Not instantiable.
 */

final class Constants
{
    private Constants()
    {
    }
    //
    static final String APP_MENU = """
                1 - Create Account
                2 - Delete Account
                3 - Login
                4 - View Total Bank Holdings
                5 - View Net Bank Holdings
                6 - View Top 3 Richest Accounts
                7 - View Top 5 Balances
                8 - Count of Accounts
                9 - Multithreading Demonstration
                10 - Exit
                Enter your choice: """;
    static final String ACCOUNT_MENU = """
                1 - Deposit Money
                2 - Withdraw Money
                3 - View Balance
                4 - View Transaction History
                5 - Update Password
                6 - Logout
                Enter your choice: """;
    static final String ACCOUNT_TYPE_MENU = """
                1 - Savings Account
                2 - Current Account """;
    static final String INVALID_CHOICE = "Invalid choice, please try again.";
    static final String INVALID_AMOUNT = "Invalid amount, please enter a positive number.";
    static final String ACCOUNT_DETAILS_HEADER =
            "--------------- Account %s ---------------%n";
    static final String TRANSACTION_DETAILS_HEADER =
            "------------- Transaction %s -------------%n";
    static final String FOOTER =
            "------------------------------------------";
    static final int MAX_PASSWORD_ATTEMPTS = 3;
}
