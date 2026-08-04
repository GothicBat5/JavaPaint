#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include <limits>
#include <algorithm>
#include <numeric>
#include <cctype>
#include <iomanip>


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

//  Number helpers

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
                numbers.push_back(value);
        }
        catch (...) 
        {
            /* ignore invalid token */ 
        }
    }

    return numbers;
}

void printNumbers(const std::vector<int>& numbers)
{
    for (int n : numbers)
        std::cout << n << " ";
    std::cout << "\n";
}

//  Dedicated statistics printer

void printStatistics(const std::vector<int>& numbers)
{
    if (numbers.empty())
    {
        std::cout << "No numbers to analyse.\n";
        return;
    }

    // std::minmax_element returns a pair of iterators
    auto [minIt, maxIt] = std::minmax_element(numbers.begin(), numbers.end());

    int total = static_cast<int>(numbers.size());
    long long sum = std::accumulate(numbers.begin(), numbers.end(), 0LL);
    double average = static_cast<double>(sum) / total;

    int oddCount = static_cast<int>(std::count_if(numbers.begin(), numbers.end(),
                        [](int n) { 
                            return n % 2 != 0; 
                        }));
    int evenCount = total - oddCount;

    // Width for aligned output
    const int W = 22;
    std::cout << std::fixed << std::setprecision(2);
    std::cout << "\n--- Statistics ---\n";
    std::cout << std::left
              << std::setw(W) << "Total inputs:"    << total     << "\n"
              << std::setw(W) << "Odd numbers:"     << oddCount  << "\n"
              << std::setw(W) << "Even numbers:"    << evenCount << "\n"
              << std::setw(W) << "Highest number:"  << *maxIt    << "\n"
              << std::setw(W) << "Lowest number:"   << *minIt    << "\n"
              << std::setw(W) << "Average:"         << average   << "\n"
              << std::setw(W) << "Total sum:"       << sum       << "\n";
}

//sort function for numbers

void sortNumbers(std::vector<int>& numbers, bool ascending)
{
    if (ascending) std::sort(numbers.begin(), numbers.end());
        
    else std::sort(numbers.begin(), numbers.end(), std::greater<int>());
}

//  Dedicated sort function for words (case-insensitive

void sortWords(std::vector<std::string>& words, bool ascending)
{
    auto caseInsensitiveLess = [](const std::string& a, const std::string& b)
    {
        return std::lexicographical_compare(a.begin(), a.end(),
            b.begin(), b.end(),
            [](unsigned char ca, unsigned char cb)
            {
                return std::tolower(ca) < std::tolower(cb);
            });
    };

    if (ascending) std::sort(words.begin(), words.end(), caseInsensitiveLess);
        
    else std::sort(words.begin(), words.end(),
                  [&](const std::string& a, const std::string& b)
                  { 
                      return caseInsensitiveLess(b, a); 
                  });
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

    std::cout << "\nOriginal numbers: ";
    printNumbers(numbers);

    // Sort order prompt
    std::cout << "\nSort order:\n";
    std::cout << "[1] Ascending\n";
    std::cout << "[2] Descending\n";
    std::cout << "Choice: ";

    int sortChoice = getChoice();
    bool ascending = (sortChoice != 2);   // default to ascending on invalid input

    if (sortChoice != 1 && sortChoice != 2) std::cout << "Invalid sort choice. Defaulting to ascending.\n";

    std::vector<int> sorted = numbers;
    sortNumbers(sorted, ascending);

    std::cout << "\nSorted numbers: ";
    printNumbers(sorted);

    printStatistics(numbers);
}

void handleWords()
{
    std::string input;
    std::cout << "Enter words: ";
    std::getline(std::cin, input);

    std::stringstream ss(input);
    std::vector<std::string> words;
    std::string word;

    while (ss >> word) words.push_back(word);

    if (words.empty())
    {
        std::cout << "No valid words were entered.\n";
        return;
    }

    std::cout << "\nSort order:\n";
    std::cout << "[1] Ascending  (A → Z, case-insensitive)\n";
    std::cout << "[2] Descending (Z → A, case-insensitive)\n";
    std::cout << "Choice: ";

    int sortChoice = getChoice();
    bool ascending = (sortChoice != 2);

    if (sortChoice != 1 && sortChoice != 2) std::cout << "Invalid sort choice. Defaulting to ascending.\n";

    std::cout << "\nOriginal words:\n";
    for (const auto& w : words)
        std::cout << "  " << w << "\n";

    std::vector<std::string> sorted = words;
    sortWords(sorted, ascending);

    std::cout << "\nSorted words (" << (ascending ? "A→Z" : "Z→A") << ", case-insensitive):\n";
    for (const auto& w : sorted)
        std::cout << "  " << w << "\n";

    std::cout << "\nTotal words: " << words.size() << "\n";
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

        switch (choice)
        {
            case 1: handleNumbers(); break;
            case 2: handleWords();   break;
            case 3:
                std::cout << "\nProgram ended.\n";
                return 0;
            default:
                std::cout << "Invalid input. Please enter 1, 2, or 3.\n";
        }
    }
}
