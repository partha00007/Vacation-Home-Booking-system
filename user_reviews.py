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

def get_reviews_by_listing_id(listing_id):
    url = f"{FASTAPI_BASE}/listings/{listing_id}/reviews"
    try:
        response = session.get(url)
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        return [{"error": str(e)}]
