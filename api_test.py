#This script tests three API endpoints (user listing, listings, and reviews for a listing) using a session-based approach, reusing cookies if available, and returns the status of each API request as a list of tuples. 
#Partha 
import requests
import json
import os

FASTAPI_BASE = os.getenv("FASTAPI_BASE_URL", "http://127.0.0.1:8001")
DJANGO_BASE = os.getenv("DJANGO_BASE_URL", "http://127.0.0.1:8000")

SESSION_FILE = "session.json"
session = requests.Session()

# Load session cookies
if os.path.exists(SESSION_FILE):
    with open(SESSION_FILE, "r") as f:
        session.cookies.update(json.load(f))

def test_api_endpoints():
    results = []

    # Test user listing
    try:
        r = session.get(f"{DJANGO_BASE}/api/users/")
        results.append(("User list", r.status_code, r.ok))
    except Exception as e:
        results.append(("User list", "ERROR", False))

    # Test listings
    try:
        r = session.get(f"{FASTAPI_BASE}/listings")
        results.append(("Listings", r.status_code, r.ok))
    except Exception as e:
        results.append(("Listings", "ERROR", False))

    # Test reviews (use a valid listing ID if known)
    try:
        r = session.get(f"{FASTAPI_BASE}/listings/10030955/reviews")
        results.append(("Reviews for listing", r.status_code, r.ok))
    except Exception as e:
        results.append(("Reviews", "ERROR", False))

    return results
