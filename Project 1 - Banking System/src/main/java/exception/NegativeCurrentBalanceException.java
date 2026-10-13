package exception;

public class NegativeCurrentBalanceException extends RuntimeException
{
    public NegativeCurrentBalanceException(String message)
    {
        super(message);
    }
}
