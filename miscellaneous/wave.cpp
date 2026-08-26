#include <iostream>

int main()
{

    int count = 0;

    while(count <= 20)
    {
        std::cout<<"Count: "<<count<<"\n";
        count++;
    }

    count = 19;
    
    while(count >= 0)
    {
        std::cout<<"Count: "<<count<<"\n";
        count--;
    }

    return 0;
}
