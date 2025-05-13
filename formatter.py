from rich.console import Console
from rich.table import Table

console = Console()

def print_users(users):
    table = Table(title="User List")
    table.add_column("ID", style="cyan")
    table.add_column("Name", style="green")
    table.add_column("Email", style="magenta")

    for user in users:
        table.add_row(str(user["id"]), user["name"], user["email"])

    console.print(table)

def print_message(status, message):
    if status == 200:
        console.print(f"[bold green]Success:[/bold green] {message}")
    else:
        console.print(f"[bold red]Error:[/bold red] {message}")
#for testing purpose (delete later)
if __name__ == "__main__":
    test_users = [
        {"id": 1, "name": "Alice", "email": "alice@example.com"},
        {"id": 2, "name": "Bob", "email": "bob@example.com"},
    ]

    print_users(test_users)

    print_message(200, "User deleted successfully.")
    print_message(404, "User not found.")

