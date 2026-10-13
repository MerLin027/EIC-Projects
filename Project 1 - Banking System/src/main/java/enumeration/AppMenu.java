package enumeration;

import java.util.Optional;

public enum AppMenu
{
    CREATE_ACCOUNT(1),
    DELETE_ACCOUNT(2),
    LOGIN(3),
    TOTAL_BANK_HOLDINGS(4),
    NET_BANK_HOLDINGS(5),
    TOP_3_ACCOUNTS(6),
    TOP_5_BALANCES(7),
    COUNT_OF_ACCOUNTS(8),
    EXIT(9);
    //
    private final int value; // each enum constant has its own value
    //
    AppMenu(int value)
    {
        this.value = value;
    }
    // constructor is called when initializing each enum constants (by Java)
    // each constant is built by calling the enum's constructor
    //
    public int getValue() // returns the number associated with a particular enum constant
    {
        return value;
    }
    //
    public static Optional <AppMenu> lookup (int value) // static because we call it before we have the actual constant
    {
        for (AppMenu choice : values()) // values() returns an array of the enum constants in the order they are declared
        {
            if (choice.value == value) // compares stored value of enum constant with the user input
            {
                return Optional.of(choice); // if it matches, it returns the enum constant in an optional
            }
        }
        return Optional.empty(); // otherwise it returns an empty optional
    }
}
