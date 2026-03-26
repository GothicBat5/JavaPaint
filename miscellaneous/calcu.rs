use std::io; 

fn main()
{
    println!("Choose operation: +, -, *, /");
    let operation = read_input("> ");
    let num1 = read_number("NumOne: ");
    let num2 = read_number("NumTwo: ");
    match operation.trim()
    {
        "+" => println!("Result: {}", num1 + num2),
        "-" => println!("Result: {}", num1 - num2),
        "*" => println!("Result: {}", num1 * num2),
        "/" => 
        {
            if num2 == 0.0
            {
                println!("Error: Cannot divide by zero");
            }
            else
            {
                println!("Result: {}", num1 / num2);
            }
        },
        _ => println!("Invalid operation"),
    }
    println!("Program Ended.");
}
fn read_number(prompt: &str) -> f64
{
    loop
    {
        let input = read_input(prompt);
        match input.trim().parse::<f64>()
        {
            Ok(num) => return num,
            Err(_) => println!("Invalid number."),
        }
    }
}
fn read_input(prompt: &str) -> String
{
    use std::io::Write;
    let mut input = String::new();
    print!("{}", prompt);
    io::stdout().flush().unwrap();
    io::stdin().read_line(&mut input).expect("Failed to Read.");
    input
}

