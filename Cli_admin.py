#This is a complete admin command-line interface (CLI) tool for managing users, listings, reviews, and testing API endpoints with session-based authentication.
#Partha
import click
import list_user
import user_listing
import user_reviews
from formatter import print_users, print_message
from formatter import print_reviews
from dotenv import load_dotenv
from api_test import test_api_endpoints

# --- Session handling ---
import requests
import json
import os

load_dotenv()

FASTAPI_BASE = os.getenv("FASTAPI_BASE_URL", "http://127.0.0.1:8001")
DJANGO_BASE = os.getenv("DJANGO_BASE_URL", "http://127.0.0.1:8000")


SESSION_FILE = "session.json"
session = requests.Session()

# Load session if available
if os.path.exists(SESSION_FILE):
    with open(SESSION_FILE, "r") as f:
        cookies = json.load(f)
        session.cookies.update(cookies)

def save_session():
    with open(SESSION_FILE, "w") as f:
        json.dump(session.cookies.get_dict(), f)
def load_session():
    if os.path.exists(SESSION_FILE):
        with open(SESSION_FILE, "r") as f:
            cookies = json.load(f)
            session.cookies.update(cookies)


@click.group()
def cli():
    """Admin CLI Tool to manage users, listings, and reviews."""
    pass

# -------- AUTH --------
@cli.command("login")
@click.argument("username")
@click.argument("password")
def login_admin(username, password):
    """Login as admin and store session cookies"""
    url = "http://127.0.0.1:8000/api/login/"
    payload = {
        "userName": username,
        "password": password
    }
    response = session.post(url, json=payload)

    print(f"Response status code: {response.status_code}")
    print(f"Response content: {response.text}")

    if response.status_code == 200:
        save_session()
        click.secho("✅ Login successful", fg="green")  # Print success message
        return True, "Login successful"
    else:
        click.secho(f"❌ {response.json().get('error', 'Login failed')}", fg="red")
        return False, response.json().get("error", "Login failed")

# -------- USERS --------
@cli.group()
def users():
    """Manage users"""
    pass

@users.command("list")
def list_users():
    """List all users"""
    users_data = list_user.list_users()
    print_users(users_data)

@users.command("delete")
@click.argument("user_name")
def delete_user(user_name):
    """Delete a user by userName"""
    status, message = list_user.delete_user(user_name)
    print_message(status, message)

@users.command("activate")
@click.argument("user_name")
def activate_user(user_name):
    """Activate a user by userName"""
    status, message = list_user.activate_user(user_name)
    print_message(status, message)

# -------- LISTINGS --------
@cli.group()
def listings():
    """Manage listings"""
    pass

@listings.command("get")
@click.argument("listing_id")
def get_listing(listing_id):
    """
    Get details of a single listing by ID.
    """
    url = f"{FASTAPI_BASE}/listings/{listing_id}"
    try:
        response = session.get(url)
        response.raise_for_status()
        listing = response.json()
        print("\n🎯 Listing Details:")
        print(f"ID: {listing.get('_id')}")
        print(f"Title: {listing.get('title')}")
        print(f"Location: {listing.get('location')}")
        print(f"Price: {listing.get('price')}")
        print(f"Description: {listing.get('description')}")
    except requests.exceptions.RequestException as e:
        print(f"❌ Error: {e}")


@listings.command("list")
def list_all_listings():
    """List all vacation home listings"""

    url = f"{FASTAPI_BASE}/listings"
    load_session()

    try:
        response = session.get(url)
        response.raise_for_status()
        data = response.json()

        # Optional: Call your rich formatter
        from formatter import print_listings
        print_listings(data)

      

    except requests.exceptions.RequestException as e:
        click.secho(f"❌ Request failed → {e}", fg="red")


        #new added for delete listing
@listings.command("delete")
@click.argument("listing_id")
def delete_listing(listing_id):
    """Delete a listing by ID"""
    url = f"{FASTAPI_BASE}/listings/{listing_id}"
    load_session()

    try:
        response = session.delete(url)
        response.raise_for_status()
        from formatter import print_message
        print_message(response.status_code, response.json().get("message", "You have deleted this specific listing successfully"))



    except requests.exceptions.HTTPError:
        from formatter import print_message
        print_message(response.status_code, f"Failed to delete listing: {response.text}")
    except Exception as e:
        from formatter import print_message
        print_message(500, f"Unexpected error: {str(e)}")

    




# -------- REVIEWS --------
@cli.group()
def reviews():
    """Manage reviews"""
    pass

@reviews.command("list")
@click.argument("listing_id")
def list_reviews(listing_id):
    """List reviews for a specific listing"""
    reviews = user_reviews.get_reviews_by_listing_id(listing_id)
    print_reviews(listing_id, reviews)

@reviews.command("delete")
@click.argument("listing_id")
@click.argument("review_id")
def delete_review(listing_id, review_id):
    """Delete a specific review from a listing"""
    load_session()
    url = f"{FASTAPI_BASE}/listings/{listing_id}/reviews/{review_id}"

    try:
        response = session.delete(url)
        response.raise_for_status()
        click.secho("✅ Review deleted successfully!", fg="green")
    except requests.exceptions.HTTPError as e:
        click.secho(f"❌ Failed to delete review → {response.status_code}: {response.text}", fg="red")
    except Exception as e:
        click.secho(f"❌ Error: {e}", fg="red")

#.............new update add reviews............
@reviews.command("add")
@click.argument("listing_id")
@click.argument("reviewer")
@click.argument("comment")
@click.argument("rating", type=int)
def add_review(listing_id, reviewer, comment, rating):
    """Add a new review for a specific listing."""
    load_session()
    url = f"{FASTAPI_BASE}/listings/{listing_id}/reviews"
    payload = {
        "username": reviewer,
        "comment": comment,
        "rating": rating
    }

    try:
        response = session.post(url, json=payload)
        response.raise_for_status()
        click.secho("✅ Review added successfully!", fg="green")
    except requests.exceptions.HTTPError as e:
        click.secho(f"❌ Failed to add review → {response.status_code}: {response.text}", fg="red")
    except Exception as e:
        click.secho(f"❌ Error: {e}", fg="red")



# -------- API-Test-Endpoint --------

@cli.command("test")
def run_api_tests():
    """Run backend API tests via CLI"""
    click.secho("\n -_- Running API Health Checks...\n", fg="cyan", bold=True)

    results = test_api_endpoints()
    for name, status, ok in results:
        if ok:
            click.secho(f"✅ {name}: OK ({status})", fg="green")
        else:
            click.secho(f"❌ {name}: FAILED ({status})", fg="red")

    click.secho("\n✅ API test complete.\n", fg="cyan", bold=True)


if __name__ == "__main__":
    cli()
