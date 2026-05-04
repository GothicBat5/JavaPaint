#include <iostream>
#include <vector>
#include <algorithm>

int main()
{
    int so = 1;
    std::vector<std::string> item = {"Alfa", "Zoron", "Munich", "Xeyi", "DonM", "HaycQ"};
    
    std::sort(item.begin(), item.end());
    
    for(const auto& out : item)
    {
        std::cout<<so<<": "<<out<<std::endl; 
        so++; 
    }
}
