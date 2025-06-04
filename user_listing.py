#This script defines a function that uses a session-based HTTP request to fetch listings from a FastAPI backend and returns either the listings data or an error message.
#Partha
import requests
import json
import os

BASE_URL = "http://127.0.0.1:8001"
SESSION_FILE = "session.json"
session = requests.Session()

# Load session cookies if available
if os.path.exists(SESSION_FILE):
    with open(SESSION_FILE, "r") as f:
        cookies = requests.utils.cookiejar_from_dict(json.load(f))
        session.cookies = cookies

"""This function sends a GET request to the '/listings/' endpoint to retrieve all listings from the backend API. 
If the request is successful, it returns the listing data; otherwise, it returns an error message."""

def list_listings():
    try:
        url = f"{BASE_URL}/listings/"
        response = session.get(url)

        if response.status_code == 200:
            return True, response.json()
        else:
            return False, response.text
    except Exception as e:
        return False, str(e)
