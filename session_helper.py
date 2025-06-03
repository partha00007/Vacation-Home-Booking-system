
import requests
import json
import os

SESSION_FILE = "session.json"
session = requests.Session()

# Load cookie from file (if exists)
if os.path.exists(SESSION_FILE):
    with open(SESSION_FILE, "r") as f:
        cookies = json.load(f)
        session.cookies.update(cookies)

def save_session():
    with open(SESSION_FILE, "w") as f:
        json.dump(session.cookies.get_dict(), f)

def get_session():
    return session
