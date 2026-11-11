package enumeration;

import java.util.Optional;

public enum AccountType
{
    SAVINGS(0),
    CURRENT(1);
    //
    private final int value;
    //
    private AccountType(int value)
    {
        this.value = value;
    }
    //
    public static Optional <AccountType> lookup (int value)
    {
        for (AccountType choice : values())
        {
            if (choice.value == value)
            {
                return Optional.of(choice);
            }
        }
        return Optional.empty();
    }
}