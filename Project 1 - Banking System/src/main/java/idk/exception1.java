package idk;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class exception1
{
    public static void main(String[] args)
    {
        String filename = "hello.txt";
        try
        {
            FileReader fr = new FileReader(filename);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }
    }
}

class exception2
{
    public static void readFile(String filename)
    {
        // FileReader fr = new FileReader(filename);
    }
    public static void main(String[] args) throws FileNotFoundException
    {
        exception2.readFile("hello.txt");
    }
}

class exception3
{
    public static void print(String str) throws NullPointerException
    {
        System.out.println(str.length());
    }
    public static void main(String[] args)
    {
        String str = null;
        exception3.print(str);
    }
}
