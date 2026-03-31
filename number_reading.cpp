#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include <limits>
#include <algorithm>
#include <numeric>
#include <cctype>

void clearInput()
{
    std::cin.clear();
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
}

int getChoice()
{
    int choice;
    std::cin >> choice;

    if (std::cin.fail())
    {
        clearInput();
        return -1;
    }

    clearInput();
    return choice;
}

std::vector<int> parseNumbers(const std::string& input)
{
    std::vector<int> numbers;
    std::stringstream ss(input);
    std::string token;

    while (ss >> token)
    {
        try
        {
            size_t pos = 0;
            int value = std::stoi(token, &pos);

            if (pos == token.size())
            {
                numbers.push_back(value);
            }
        }
        catch(...)
        {
            //ignore invalid token
        }
    }

    return numbers;
}

void printNumbers(const std::vector<int>& numbers)
{
    for (int n : numbers)
    {
        std::cout << n << " ";
    }
    std::cout << "\n";
}

void handleNumbers()
{
    std::string input;
    std::cout << "Enter numbers: ";
    std::getline(std::cin, input);

    std::vector<int> numbers = parseNumbers(input);

    if (numbers.empty())
    {
        std::cout << "No valid numbers were entered.\n";
        return;
    }

    int sum = std::accumulate(numbers.begin(), numbers.end(), 0);
    int odd = 0, even = 0;
    int highest = numbers[0];
    int lowest = numbers[0];

    for (int n : numbers)
    {
        if (n % 2 == 0) {
            
            ++even;
        }
        else {
            
            ++odd;
        }
        
        if (n > highest) 
        {
            highest = n;
        }
        
        if (n < lowest) 
        {
            lowest = n;
        }
    }

    double average = static_cast<double>(sum) / numbers.size();

    std::cout << "\nOriginal numbers: ";
    printNumbers(numbers);

    std::cout << "\nSort order:\n";
    std::cout << "[1] Ascending\n";
    std::cout << "[2] Descending\n";
    std::cout << "Choice: ";

    int sortChoice = getChoice();

    std::vector<int> sortedNumbers = numbers;
    if (sortChoice == 1)
    {
        std::sort(sortedNumbers.begin(), sortedNumbers.end());
    }
    else if (sortChoice == 2)
    {
        std::sort(sortedNumbers.begin(), sortedNumbers.end(), std::greater<int>());
    }
    else
    {
        std::cout << "Invalid sort choice. Showing ascending order by default.\n";
        std::sort(sortedNumbers.begin(), sortedNumbers.end());
    }

    std::cout << "\nSorted numbers: ";
    printNumbers(sortedNumbers);

    std::cout << "\nTotal inputs: " << numbers.size() << "\n";
    std::cout << "Total odd numbers: " << odd << "\n";
    std::cout << "Total even numbers: " << even << "\n";
    std::cout << "Highest number: " << highest << "\n";
    std::cout << "Lowest number: " << lowest << "\n";
    std::cout << "Average: " << average << "\n";
    std::cout << "Total sum: " << sum << "\n";
}

void handleWords()
{
    std::string input;
    std::cout << "Enter words: ";
    std::getline(std::cin, input);

    std::stringstream ss(input);
    std::vector<std::string> words;
    std::string word;

    while (ss >> word)
    {
        words.push_back(word);
    }

    if (words.empty())
    {
        std::cout << "No valid words were entered.\n";
        return;
    }

    std::vector<std::string> sortedWords = words;
    std::sort(sortedWords.begin(), sortedWords.end());

    std::cout << "\nOriginal words:\n";
    for (const auto& w : words)
    {
        std::cout << w << "\n";
    }

    std::cout << "\nSorted words:\n";
    for (const auto& w : sortedWords)
    {
        std::cout << w << "\n";
    }
}

int main()
{
    while (true)
    {
        std::cout << "\n= = = Sorting = = =\n";
        std::cout << "[1] Numbers\n";
        std::cout << "[2] Words\n";
        std::cout << "[3] Exit\n";
        std::cout << "Choice: ";

        int choice = getChoice();

        if (choice == 1)
        {
            handleNumbers();
        }
        else if (choice == 2)
        {
            handleWords();
        }
        else if (choice == 3)
        {
            std::cout << "\nProgram ended.\n";
            break;
        }
        else
        {
            std::cout << "Invalid input.\n";
        }
    }

    return 0;
}
