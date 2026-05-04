#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <cctype>

bool isInteger(const std::string& s) 
{
    if (s.empty()) return false;
    
    for (char c : s) 
    {
        if (!std::isdigit(c)) return false;
    }
    return true;
}

bool isDecimal(const std::string& s) 
{
    bool dotSeen = false;
    if (s.empty()) return false;

    for (char c : s) 
    {
        if (c == '.') 
        {
            if (dotSeen) return false;
            dotSeen = true;
        } 
        
        else if (!std::isdigit(c)) 
        {
            return false;
        }
    }
    return dotSeen; // must have one dot
}

bool isSingleChar(const std::string& s) 
{
    return s.length() == 1 && std::isalpha(s[0]); 
}

bool isText(const std::string& s) 
{
    if (s.empty()) return false;
    
    for (char c : s) 
    {
        if (!std::isalpha(c)) return false;
    }
    return true;
}

int main() 
{
    std::string line;

    std::cout << "Input: ";
    std::getline(std::cin, line);

    std::stringstream ss(line);
    std::string token;

    bool hasInt = false, hasDec = false, hasText = false, hasChar = false, hasMixed = false;

    while (ss >> token) 
    {
        if (isInteger(token)) 
        {
            std::cout << token << " -> Integer\n";
            hasInt = true;
        }
        else if (isDecimal(token)) 
        {
            std::cout << token << " -> Decimal\n";
            hasDec = true;
        }
        else if (isSingleChar(token)) 
        {
            std::cout << token << " -> Single Character\n";
            hasChar = true;
        }
        else if (isText(token)) 
        {
            std::cout << token << " -> Text\n";
            hasText = true;
        }
        else 
        {
            std::cout << token << " -> Mixed\n";
            hasMixed = true;
        }
    }

    std::cout << "\nSummary:\n";

    if (hasInt) std::cout<< "- Contains integers\n";
    if (hasDec) std::cout<< "- Contains decimals\n";
    if (hasText) std::cout<< "- Contains text\n";
    if (hasChar) std::cout<< "- Contains single characters\n";
    if (hasMixed) std::cout<< "- Contains mixed values\n";

    return 0;
}
