
import java.util.Scanner;

public class Menu
{
    static Scanner scanner = new Scanner(System.in);

    static void printMenu()
    {
        System.out.println("\n==== MAIN MENU ====");
        System.out.println("[1] Area of Circle");
        System.out.println("[2] Area of Rectangle");
        System.out.println("[3] Lbs to Kgs");
        System.out.println("[4] Kgs to Lbs");
        System.out.println("[5] Exit");
    }

    static void areaOfCircle()
    {
        double radius = getDouble("Enter radius: ");

        double area = Math.PI * radius * radius;

        System.out.printf("Circle Area: %.2f%n", area);
    }

    static void areaOfRectangle()
    {
        double length = getDouble("Enter length: ");
        double width  = getDouble("Enter width: ");

        double area = length * width;

        System.out.printf("Rectangle Area: %.2f%n", area);
    }

    static void lbsToKg()
    {
        double lbs = getDouble("Enter pounds: ");

        double kg = lbs * 0.45359237;

        System.out.printf("%.2f lbs = %.2f kg%n", lbs, kg);
    }

    static void kgToLbs()
    {
        double kg = getDouble("Enter kilograms: ");

        double lbs = kg * 2.20462;

        System.out.printf("%.2f kg = %.2f lbs%n", kg, lbs);
    }


    static int getInt(String message)
    {
        while (true)
        {
            System.out.print(message);

            if (scanner.hasNextInt())
            {
                return scanner.nextInt();
            }

            System.out.println("Please enter a valid integer.");
            scanner.next();
        }
    }

    static double getDouble(String message)
    {
        while (true)
        {
            System.out.print(message);

            if (scanner.hasNextDouble())
            {
                return scanner.nextDouble();
            }

            System.out.println("Please enter a valid number.");
            scanner.next();
        }
    }
    
    public static void main(String[] args)
    {
        int userChoice;

        do
        {
            printMenu();
            userChoice = getInt("Choose an option: ");

            switch (userChoice)
            {
                case 1:
                    areaOfCircle();
                    break;

                case 2:
                    areaOfRectangle();
                    break;

                case 3:
                    lbsToKg();
                    break;

                case 4:
                    kgToLbs();
                    break;

                case 5:
                    System.out.println("Program Ended.");
                    break;

                default:
                    System.out.println("Invalid option.\n");
            }

        } 
        
        while (userChoice != 5);

        scanner.close();
    }
}
