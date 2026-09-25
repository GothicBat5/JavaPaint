using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;

/*
Plsss play around these conceptss lol,
just get with the flow, I understand oop,
enums would be next
*/

namespace Classics
{
    public class EnumExamples
    {
        public static void Main()
        {
            Console.WriteLine($"Country: {Country.USA}");

            Console.WriteLine($"Number: {(int)Country.Ecuador}");

            // Population
          //names and testing 
            string name = CountryPopulation.Japan.ToString();
            int population = (int)CountryPopulation.Poland;

            Console.WriteLine($"Name: {name}");
            Console.WriteLine($"Population: {population} million");

            // Land area
            double area = LandArea(Country.USA);

            Console.WriteLine($"Area of {Country.USA}: {area:N0} km²");
        }

        public static double LandArea(Country country)
        {
            switch (country)
            {
                case Country.Japan:
                    return 377975;

                case Country.China:
                    return 9596961;

                case Country.USA:
                    return 9833517;

                case Country.Russia:
                    return 17098242;

                case Country.Chile:
                    return 756102;

                case Country.Ecuador:
                    return 276841;

                case Country.Poland:
                    return 312696;

                default:
                    return 0;
            }
        }
    }

    public enum Country
    {
        Japan,
        China,
        USA,
        Russia,
        Chile,
        Ecuador,
        Poland
    }

    public enum CountryPopulation
    {
        Japan = 122,
        China = 1410,
        USA = 349,
        Russia = 143,
        Chile = 19,
        Ecuador = 18,
        Poland = 38
    }
}
