import click
from api import user
from formatter import print_users, print_message

@click.group()
def cli():
    """Admin CLI Tool to manage users, listings, and reviews."""
    pass

@cli.group()
def users():
    """Manage users"""
    pass

@users.command("list")
def list_users():
    """List all users"""
    users_data = user.list_users()
    print_users(users_data)

@users.command("delete")
@click.argument("user_id")
def delete_user(user_id):
    """Delete a user by ID"""
    status, message = user.delete_user(user_id)
    print_message(status, message)

if __name__ == "__main__":
    cli()