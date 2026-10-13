package exception;

public class NegativeSavingsBalanceException extends RuntimeException
{
    public NegativeSavingsBalanceException(String message)
    {
        super(message);
    }
}
