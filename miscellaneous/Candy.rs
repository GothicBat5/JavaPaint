use std::io::{self, Write};

fn is_prime(n: i128) -> bool {
    if n <= 1 {
        return false;
    }

    for i in 2..=((n as f64).sqrt() as i128) 
    {
        if n % i == 0 {
            return false;
        }
    }

    true
}

fn main() {
    println!("PRIME, ODD, EVEN");

    print!("Enter a number: ");
    io::stdout().flush().unwrap();

    let mut input = String::new();

    io::stdin()
        .read_line(&mut input)
        .expect("Failed to read input");

    match input.trim().parse::<f64>() {

        Ok(num) => {


            if num.fract() != 0.0 {
                println!("{} is a DECIMAL number.", num);
            }

            else {
                let int_num = num as i128;

                if is_prime(int_num) {
                    println!("{} is a PRIME number.", int_num);
                }

                else if int_num % 2 == 0 {
                    println!("{} is an EVEN number.", int_num);
                }

                else {
                    println!("{} is an ODD number.", int_num);
                }
            }
        }

        Err(_) => {
            println!("Invalid number.");
        }
    }
}





