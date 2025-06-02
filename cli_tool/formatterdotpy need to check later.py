#in formatter.py theke eta edit korte hobe time pele

from rich.table import Table
from rich.console import Console

console = Console()

def print_reviews(reviews):
    # Try to get up to 5 positive reviews first
    positive_reviews = [r for r in reviews if r.get("rating", 0) >= 4 and r.get("reviewer_name") and r.get("comments")]
    selected_reviews = positive_reviews[:5]

    # If no positive reviews, fall back to first 5 general reviews
    if not selected_reviews:
        console.print("[bold yellow]No positive reviews found. Showing 5 recent reviews instead.[/bold yellow]")
        selected_reviews = reviews[:5]

    if not selected_reviews:
        console.print("[bold red]No reviews available.[/bold red]")
        return

    # Summary Table
    table = Table(title="Reviews")
    table.add_column("Review ID", style="cyan", no_wrap=True)
    table.add_column("Reviewer", style="magenta", no_wrap=True)
    table.add_column("Date", style="green", no_wrap=True)
    table.add_column("Comment (Preview)", style="yellow")

    for review in selected_reviews:
        review_id = str(review.get("_id", "-"))
        reviewer = review.get("reviewer_name", "-")
        date = str(review.get("date", "-"))[:10]
        comment = review.get("comments", "-").strip().replace("\n", " ")
        preview = (comment[:60] + "...") if len(comment) > 60 else comment
        table.add_row(review_id, reviewer, date, preview)

    console.print(table)

    # Full Comments
    console.print("\n[bold underline]Full Comments:[/bold underline]")
    for review in selected_reviews:
        reviewer = review.get("reviewer_name", "-")
        comment = review.get("comments", "-").strip()
        console.print(f"\n[bold]{reviewer}:[/bold] {comment}")
