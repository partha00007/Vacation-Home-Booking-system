#This script uses an authenticated session to get reviews for a specific listing from the FastAPI backend, returning either the reviews data or an error.
#Partha 
import requests
import json
import os
from dotenv import load_dotenv

load_dotenv(dotenv_path="fd.env")

FASTAPI_BASE = os.getenv("FASTAPI_BASE_URL")
SESSION_FILE = "session.json"
session = requests.Session()

# Load session cookies if available
if os.path.exists(SESSION_FILE):
    with open(SESSION_FILE, "r") as f:
        cookies = json.load(f)
        session.cookies.update(cookies)

"""
    This function fetches reviews for a given listing by its listing ID.
    Args:
    - listing_id (str): The unique ID of the listing to fetch reviews for.
    Returns:
    - If successful: A list of reviews in JSON format.
    - If an error occurs: A list with an error message.
    """

def get_reviews_by_listing_id(listing_id):
    url = f"{FASTAPI_BASE}/listings/{listing_id}/reviews"
    try:
        response = session.get(url)
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        return [{"error": str(e)}]
