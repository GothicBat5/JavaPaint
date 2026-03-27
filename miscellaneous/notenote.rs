use std::fs::{OpenOptions, read_to_string};
use std::io::{self, Write};
use std::env;

fn main() {
    let args: Vec<String> = env::args().collect();

    if args.len() < 2 
    {
        eprintln!("Usage: {} [add|list|delete] [note]", args[0]);
        return;
    }

    match args[1].as_str() 
    {
        "add" => {
            if args.len() < 3 
            {
                eprintln!("Please provide a note to add.");
                return;
            }
            let note = args[2..].join(" ");
            add_note(&note);
        }
        "list" => list_notes(),
        "delete" => {
            if args.len() < 3 
            {
                eprintln!("Please provide the line number to delete.");
                return;
            }
            let line: usize = args[2].parse().unwrap_or(0);
            delete_note(line);
        }
        _ => eprintln!("Unknown command. Use add, list, or delete."),
    }
}

fn add_note(note: &str) 
{
    let mut file = OpenOptions::new()
        .create(true)
        .append(true)
        .open("notes_mynotes.txt")
        .expect("Unable to open notes file");

    writeln!(file, "{}", note).expect("Unable to write note");
    println!("Note added: {}", note);
}

fn list_notes() 
{
    let content = read_to_string("notes_mynotes.txt").unwrap_or_default();
    if content.is_empty() 
    {
        println!("No notes found.");
        return;
    }

    for (i, line) in content.lines().enumerate() 
    {
        println!("{}: {}", i + 1, line);
    }
}

fn delete_note(line_number: usize) 
{
    let content = read_to_string("notes_mynotes.txt").unwrap_or_default();
    let mut lines: Vec<&str> = content.lines().collect();

    if line_number == 0 || line_number > lines.len() 
    {
        eprintln!("Invalid line number.");
        return;
    }

    lines.remove(line_number - 1);

    let mut file = OpenOptions::new()
        .write(true)
        .truncate(true)
        .open("notes_mynotes.txt")
        .expect("Unable to open notes file");

    for line in lines 
    {
        writeln!(file, "{}", line).expect("Unable to write note");
    }

    println!("Deleted note at line {}", line_number);
}
