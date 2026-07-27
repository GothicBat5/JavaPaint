using System;

namespace CollectionZen
{
    public class ClassicCalc
    {
        static void Main()
        {
            Console.WriteLine("Calc \n");
            
            Console.Write("Num One: ");
            if(!int.TryParse(Console.ReadLine(), out int num1))
            {
                Console.WriteLine("\nInvalid Input\n");
                return;
            }
            
            Console.Write("Operator: ");
            string op = Console.ReadLine();
            
            int Rss = 0;
            bool Valid = true;
            
            Console.Write("Num Two: ");
            if(!int.TryParse(Console.ReadLine(), out int num2))
            {
                Console.WriteLine("\nInvalid Input\n");
                return;
            }
            
            switch(op)
            {
                case "+": Rss = num1 + num2; break;
                
                case "-": Rss = num1 - num2; break;
                
                case "*": Rss = num1 * num2; break;
                
                case "/": 
                if(num2 != 0) Rss = num1 / num2;
                
                else {
                    Console.WriteLine("\nInvalid Input\n");
                    Valid = false;
                }
                break;
                
                default: Console.WriteLine("\nInvalid Input\n");
                Valid = false;
                break;
            }
            
            if(Valid) Console.WriteLine($"\nResult: {Rss}");
        }
    }
}

