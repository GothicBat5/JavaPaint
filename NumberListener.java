import java.util.Scanner;

public class NumberListener 
{
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Number: Prime, Odd, Even, \n");
        
        while(true) 
        {
            System.out.print("Enter a number: ");
            String input = scan.nextLine();
            
            if(input.equalsIgnoreCase("exit"))
            {
                System.out.println("\nProgram Ended. Thank you.");
                break;
            }
            
            try {
                
                int num = Integer.parseInt(input);
                
                if(isPrime(num))
                {
                    System.out.println(num+" is a prime number.\n");
                }
                else
                {
                    if(num % 2 == 0)
                    {
                        System.out.println(num+" is a even number.\n");
                    }
                    else
                    {
                        System.out.println(num+" is a odd number.\n");
                    }
                }
            }
            catch(NumberFormatException e)
            {
                System.out.println("Invalid Input!");
            }
        }
        scan.close();
    }
    
    private static boolean isPrime(int np)
    {
        if(np <= 1)
        {
            return false;
        }
        if(np == 2)
        {
            return true;
        }
        if(np % 2 == 0)
        {
            return false;
        }
        
        for(int i = 3; i <= Math.sqrt(np); i += 2)
        {
            if(np % i == 0)
            {
                return false;
            }
        }
        return true; 
    }
}

