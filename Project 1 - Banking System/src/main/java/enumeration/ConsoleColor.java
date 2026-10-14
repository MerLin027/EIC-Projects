package enumeration;

public enum ConsoleColor
{
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    BLUE("\033[34m"),
    RESET("\u001B[0m");
    //
    private final String code;
    //
    ConsoleColor(String code)
    {
        this.code = code;
    }
    //
    public String getCode()
    {
        return code;
    }
}