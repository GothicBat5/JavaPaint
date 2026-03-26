using System;

namespace RhinoS
{
    public class Program
    {
        public static void Main(string[] args)
        {
            Console.WriteLine("Year must be = [YYYY/1940]\nMonth only [1 to 12]");
            Console.WriteLine("What is your fate?");
            Console.WriteLine("This program is created by Chloe Nazz\nReal? idk\n");
            int month = ReadInt("Month: ", 1, 12);
            int year = ReadYear("Year: ");
            int day = ReadDay(month, year);

            DateTime birthday = new DateTime(year, month, day);

            Console.WriteLine($"\nYour birthday is {birthday:MMMM d, yyyy}");
            Console.WriteLine("Press Enter to continue...");
            Console.ReadLine();

            DateTime today = DateTime.Today;
            DateTime startDate = birthday > today ? birthday : today;

            DateTime randomDate = GenerateFutureDate(startDate, startDate.AddYears(50));

            Console.WriteLine($"\nDeath date: {randomDate:MMMM d, yyyy}");
            Console.WriteLine("\n** Done ;-))   **\nLive your life hehe\nWhile ur still alive");
        }

        static int ReadInt(string prompt, int min, int max)
        {
            while (true)
            {
                Console.Write(prompt);
                string input = Console.ReadLine();

                if (int.TryParse(input, out int value) && value >= min && value <= max)
                {
                    return value; 
                }
                Console.WriteLine("Invalid Month.\n");
            }
        }

        static int ReadYear(string prompt)
        {
            while (true)
            {
                Console.Write(prompt);
                string input = Console.ReadLine();

                if (input.Length == 4 && int.TryParse(input, out int year))
                {
                    return year;
                }

                Console.WriteLine("Invalid Year.\n");
            }
        }

        static int ReadDay(int month, int year)
        {
            while (true)
            {
                Console.Write("Day: ");
                string input = Console.ReadLine();

                if (!int.TryParse(input, out int day))
                {
                    Console.WriteLine("Invalid Input \n");
                    continue;
                }

                if (DateTime.TryParse($"{year}-{month}-{day}", out _))
                {
                    return day; 
                }
                Console.WriteLine("Invalid Day.\n");
            }
        }

        static DateTime GenerateFutureDate(DateTime start, DateTime end)
        {
            Random rng = new Random();
            int range = (end - start).Days;
            int randomDays = rng.Next(0, range + 1);
            return start.AddDays(randomDays);
        }
    }
}
