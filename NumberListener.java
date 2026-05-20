import java.util.Scanner;

public class NumberListener
{
    private static void startProgram()
    {
        Scanner scan = new Scanner(System.in);

        printIntro();

        while(true)
        {
            System.out.print("Enter a number: ");
            String input = scan.nextLine();

            if(isExitCommand(input))
            {
                endProgram();
                break;
            }

            processInput(input);
        }

        scan.close();
    }

    private static void printIntro()
    {
        System.out.println("Number Analyzer");
        System.out.println("Prime, Odd, or Even.");
        System.out.println("Type exit to end.\n");
    }

    private static boolean isExitCommand(String input)
    {
        return input.equalsIgnoreCase("exit");
    }

    private static void processInput(String input)
    {
        try
        {
            long number = Long.parseLong(input);

            displayResult(number);
        }
        catch(NumberFormatException e)
        {
            System.out.println("Invalid input!\n");
        }
    }

    private static void displayResult(long number)
    {
        StringBuilder result = new StringBuilder();

        result.append(number).append(" is:\n");

        if(isPrime(number))
        {
            result.append("- Prime\n");
        }

        if(number % 2 == 0)
        {
            result.append("- Even\n");
        }
        else
        {
            result.append("- Odd\n");
        }

        System.out.println(result);
    }

    private static boolean isPrime(long number)
    {
        if(number <= 1)
        {
            return false;
        }

        if(number == 2)
        {
            return true;
        }

        if(number % 2 == 0)
        {
            return false;
        }

        long limit = (long)Math.sqrt(number);

        for(long i = 3; i <= limit; i += 2)
        {
            if(number % i == 0)
            {
                return false;
            }
        }

        return true;
    }

    private static void endProgram()
    {
        System.out.println("\nProgram Ended. Thank you.");
    }
    
    public static void main(String[] args)
    {
        startProgram();
    }
}
