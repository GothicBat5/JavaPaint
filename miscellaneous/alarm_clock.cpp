#include <iostream>
#include <chrono>
#include <ctime>
#include <thread>
#include <iomanip>

int main() {
    int alarmHour, alarmMinute;
    std::cout << "Enter alarm time (HH MM): ";
    std::cin >> alarmHour >> alarmMinute;

    std::cout << "Alarm set for "<< std::setw(2)<< std::setfill('0') << alarmHour<< ":"
              << std::setw(2) << std::setfill('0') << alarmMinute << "\n";

    while (true) 
    {
        //current system time
        auto now = std::chrono::system_clock::now();
        std::time_t current = std::chrono::system_clock::to_time_t(now);
        std::tm* localTime = std::localtime(&current);

        int currentHour = localTime->tm_hour;
        int currentMinute = localTime->tm_min;

        std::cout << "\rCurrent time: "<< std::setw(2) << std::setfill('0') << currentHour << ":"
                  << std::setw(2) << std::setfill('0')<< currentMinute<< std::flush;

        // Check alarm condition
        if (currentHour == alarmHour && currentMinute == alarmMinute) 
        {
            std::cout << "\n\nAlarm ringing! Wake up!\n";
            break;
        }

        std::this_thread::sleep_for(std::chrono::seconds(1));
    }

    return 0;
}
