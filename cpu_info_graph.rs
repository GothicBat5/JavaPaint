use sysinfo::{System, SystemExt, CpuExt, DiskExt, NetworkExt, BatteryExt};
use std::{thread, time::Duration};

fn main() 
{
    let mut system = System::new_all();

    loop {

        system.refresh_all();
        print!("\x1B[2J\x1B[1;1H");

        println!("=======*MINI SYSTEM MONITOR*=======\n");

        //RAM
        println!("RAM: Total {} MB | Used {} MB", 
            system.total_memory() / 1024, 
            system.used_memory() / 1024);

        //CPU
        println!("\nCPU:");

        for (i, cpu) in system.cpus().iter().enumerate() 
        {
            println!("  Core {}: {:.2}%", i, cpu.cpu_usage());
        }

        // Disks
        println!("\nDisks:");

        for disk in system.disks() 
        {

            println!("{:?}: {} / {} GB free",
                disk.name(),
                disk.available_space() / 1_000_000_000,
                disk.total_space() / 1_000_000_000
            );
        }

        //Network
        println!("\nNetwork:");
        for (name, data) in system.networks() 
        {
            println!("{}: received {} KB | transmitted {} KB",
                name,
                data.received() / 1024,
                data.transmitted() / 1024
            );
        }

        //Battery
        if let Some(battery) = system.batteries().next() 
        {
            println!("\nBattery: {:.0}% ({:?})", 
                battery.percentage() * 100.0, 
                battery.state()
            );
        }

        println!("\nUptime: {} seconds", system.uptime());
        println!("\n(Refreshing every 1 second... Press Ctrl+C to exit)");

        thread::sleep(Duration::from_secs(1));
    }
}
