
#include <iostream>
#include <vector> 
#include <string>
#include <sstream>
#include <limits>
#include <algorithm>
#include <cctype>

/*
I need a further imporvement about this personal project of mine
THis looks so messy and shit LMAO
*/

int main()
{
    int choice;
    while (true)
    {
        std::cout << "\n= = = = Sorting = = = =\n";
        std::cout << "\n[1]Numbers\n[2]Words\n[3]Exit\n"; 
        std::cout << "Choice: "; 
        std::cin >> choice;
        if (std::cin.fail()) 
        {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            std::cout << "Invalid input.\n";
            continue;
        }
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        if (choice == 1)
        {
            std::string input; 
            std::cout << "Enter numbers: "; 
            std::getline(std::cin, input); 
            std::stringstream ss(input);
            std::vector<int>numbers;
            int num; 
            while (ss >> num)
            {
                numbers.push_back(num); 
            }
            if (numbers.empty())
            {
                std::cout << "No valid Input ";
                continue;
            }
            int sum = 0, odd = 0, even = 0;
            int highest = numbers[0];
            int lowest = numbers[0];
            for (int n : numbers)
            {
                sum += n; 
                if (n % 2 == 0)++even;
                else ++odd;
                if (n > highest)highest = n;
                if (n < lowest)lowest = n; 
            }
            double average = static_cast<double>(sum) / numbers.size();
            std::cout << "\nTotal Inputs: " << numbers.size() <<"\n";
            std::cout << "Total odd numbers: " << odd << "\n"; 
            std::cout << "Total even numbers: " << even << "\n"; 
            std::cout << "Highest Number: "<<highest<<"\n";
            std::cout << "Lowest Number: " << lowest << "\n"; 
            std::cout << "Average: " << average << "\n";
            std::cout << "Total Sum: " << sum << "\n"; 
        }
        else if (choice == 2)
        {
            std::string input;
            std::cout << "Enter words: ";
            std::getline(std::cin, input);
            std::stringstream ss(input);
            std::vector<std::string>words;
            std::string word;
            while (ss >> word)
            {
                words.push_back(word);
            }
            std::sort(words.begin(), words.end());
            std::cout << "\nSorted words. \n\n";
            for (const auto& w : words)
            {
                std::cout << w << "\n \n";
            }
            std::cout << "\n";
        }
        else if (choice == 3)
        {
            std::cout << "\nProgram Ended\n"; 
            break; 
        }
        else
        {
            std::cout << "Invalid Input. \n"; 
        }
    }
}
