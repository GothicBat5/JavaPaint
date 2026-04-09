#include <iostream>
#include <vector>
#include <string>
#include <random>
#include <optional>


std::mt19937 rng(std::random_device{}());

std::string randomFrom(const std::vector<std::string>& arr)
{
    
    std::uniform_int_distribution<size_t> dist(0, arr.size() - 1);
    
    return arr[dist(rng)];
}

// Generate word
std::string generateWord(int length) {
    const std::vector<std::string> 
    vowels {"a", "e", "i", "o", "u"};
    
    const std::vector<std::string> 
    consonants
    { "b","c","d","f","g","h","j","k","l","m","n","p","r","s","t","v","w","z",
        "sh","ch","th","kr","st","dr"};

    std::string word;
    word.reserve(length * 2); //avoid reallocations

    std::bernoulli_distribution startDist(0.5);
    bool useConsonant = startDist(rng);

    for (int i = 0; i < length; ++i) 
    {
        if (useConsonant) 
        {
            word += randomFrom(consonants);
        } 
        
        else 
        {
            word += randomFrom(vowels);
        }
        useConsonant = !useConsonant;
    }

    return word;
}

std::optional<int> parseLength(const std::string& input) 
{
    
    try {
        int value = std::stoi(input);
        
        if (value <= 0) return std::nullopt;
        return value;
    }
    
    catch (...) 
    {
        return std::nullopt;
    }
}

int main() 
{
    std::cout<< "Type 'exit' to end program\n";

    std::string input;

    while (true) 
    {
        std::cout<<"\n\nEnter word length >> ";
        std::getline(std::cin, input);

        if (input == "exit" || input == "EXIT") 
        {
            break;
        }

        auto length = parseLength(input);

        if (!length) 
        {
            std::cout<< "Please enter a valid number greater than 0.\n";
            continue;
        }

        std::string word = generateWord(*length);
        std::cout <<"Generated word: "<< word <<"\n";
    }

    return 0;
}
