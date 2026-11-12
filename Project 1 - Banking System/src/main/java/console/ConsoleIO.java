package console;

import enumeration.ConsoleColor;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Console input and output helper, shared as a singleton so the whole application uses a single
 * {@link Scanner} on {@code System.in} (two scanners on the same stream can swallow each other's input).
 *
 * <p>The number readers do not handle a closed input stream: the scanner's
 * {@link java.util.NoSuchElementException} propagates so that {@code main} can end the program.
 */

final class ConsoleIO
{
    private final Scanner sc = new Scanner(System.in);
    private static final ConsoleIO instance = new ConsoleIO();
    //
    private ConsoleIO()
    {
    }
    static ConsoleIO getInstance()
    {
        return instance;
    }
    /** Prints a message in green (success). */
    final Consumer<String> printSuccess = message ->
    {
        System.out.println(ConsoleColor.GREEN.getCode() + message + ConsoleColor.RESET.getCode());
    };
    /** Prints a message in red (error). */
     final Consumer <String> printError = message ->
    {
        System.out.println(ConsoleColor.RED.getCode() + message + ConsoleColor.RESET.getCode());
    };
    /** Prints a message in blue (exit and logout notices). */
     final Consumer <String> printExit = message ->
    {
        System.out.println(ConsoleColor.BLUE.getCode() + message + ConsoleColor.RESET.getCode());
    };
    /** Reads a line and parses it as an {@code int}, repeating with an error message until it is valid. */
     final Supplier<Integer> readInt = () ->
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
    /** Reads a line and parses it as a {@code long}, repeating with an error message until it is valid. */
    final Supplier <Long> readLong = () ->
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
    /**
     * Reads a password. Input is hidden when a real terminal is available; otherwise it falls back
     * to a normal, visible line. Returns an empty string if the terminal stream is closed.
     */
     final Supplier <String> readPassword = () ->
    {
        Console console = System.console();
        // Console is an object allowing us to communicate with the system terminal/real console
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
}
