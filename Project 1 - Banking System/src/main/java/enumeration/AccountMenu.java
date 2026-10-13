package enumeration;

import java.util.Optional;

public enum AccountMenu
{
    DEPOSIT(1),
    WITHDRAW(2),
    VIEW_BALANCE(3),
    VIEW_TRANSACTIONS(4),
    LOGOUT(5);
    //
    private int value;
    //
    AccountMenu(int value)
    {
        this.value = value;
    }
    //
    public int getValue()
    {
        return value;
    }
    //
    public static Optional <AccountMenu> lookup (int value)
    {
        for (AccountMenu choice : values())
        {
            if (choice.value == value)
            {
                return Optional.of(choice);
            }
        }
        return Optional.empty();
    }
}
