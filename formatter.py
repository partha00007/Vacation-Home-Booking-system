#This Python script uses the rich library to format and display console tables for user, listing, and review data, along with status messages for operations.
#Partha
from rich.console import Console
from rich.table import Table

console = Console() # Create a Console object to print to the terminal using rich formatting

 
#Prints a message to the console with different colors based on the HTTP status code.
#If the status is 200, it indicates success with a green message.For other statuses, it prints an error message in red.

def print_message(status, message):
    if status == 200:
        console.print(f"[bold green]Success:[/bold green] {message}")
    else:
        console.print(f"[bold red]Error:[/bold red] {message}")

#Prints a formatted table of users, including their ID, username, email, and role.

def print_users(users):
    table = Table(title="User List")
    table.add_column("User ID", justify="right", style="cyan")
    table.add_column("Username", style="magenta")
    table.add_column("Email", style="green")
    table.add_column("Role", style="yellow")

    for user in users:
        role_name = user.get("role", {}).get("roleName", "N/A") # Safely get role information; defaults to "N/A" if no role is found
        table.add_row(
            str(user.get("userId", "-")),
            user.get("userName", "-"),
            user.get("email", "-"),
            role_name
        )

    console.print(table)

def print_listings(listings): #Prints a summary table of vacation home listings, including the listing ID, name, and price.
    table = Table(title="Listing Summary")
    table.add_column("Listing ID", style="cyan")
    table.add_column("Name", style="magenta")
    table.add_column("Price", justify="right", style="green")

    for listing in listings:
        listing_id = listing.get("_id", "-")  
        name = listing.get("title", "-")
        price = f"${listing.get('price', 0):.1f}"
        table.add_row(listing_id, name, price)

    console.print(table)     # Print the table to the console

def print_reviews(listing_id, reviews):
    console.print(f"\n[bold cyan]Reviews for Listing {listing_id}[/bold cyan]\n")
    # Create a table for displaying the review information

    table = Table(title="Review Summary")
    table.add_column("Review ID", style="cyan")
    table.add_column("Reviewer", style="magenta")
    table.add_column("Date", style="green")
    table.add_column("Comment", style="white")

    for review in reviews:         # Get review details; default to "-" if not found
        review_id = review.get("_id", "-")
        reviewer = review.get("reviewer_name", "-")
        date = review.get("date", "-").split("T")[0]
        comment = review.get("comments", "-").strip().replace("\n", " ")[:80] + "..."

        table.add_row(review_id, reviewer, date, comment)

    console.print(table)
