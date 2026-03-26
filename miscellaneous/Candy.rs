use std::io::{self, Write};

fn main() 
{
    print!("Enter a number: ");
    io::stdout().flush().unwrap(); 

    let mut input = String::new();
    io::stdin().read_line(&mut input).expect("Failed to read input");

    match input.trim().parse::<i32>() {
        Ok(num) => {
            if num % 2 == 0 {
                println!("{} is an even number.", num);
            } else {
                println!("{} is an odd number.", num);
            }
        }
        Err(_) => println!("Invalid number"),
    }
}








