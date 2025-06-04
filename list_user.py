#This script provides Python functions to manage users (login, list, delete, activate) via API calls, using session-based authentication and handling session cookies in a file.
#Partha
import requests
import json
import os

# Define constants for API base URL and session file

BASE_URL = "http://127.0.0.1:8000/api"
SESSION_FILE = "session.json"
SESSION = requests.Session()

session = requests.Session() # Initialize a new session

# Load session cookies from file
if os.path.exists(SESSION_FILE):
    with open(SESSION_FILE, "r") as f:
        cookies = requests.utils.cookiejar_from_dict(json.load(f))
        session.cookies = cookies

def save_session(): # Function to save the session cookies to a file
    with open(SESSION_FILE, "w") as f:
        json.dump(requests.utils.dict_from_cookiejar(session.cookies), f)

# Function to log in as an admin user and store session cookies
def login_admin(username, password):
    """Login as admin and store session cookies"""
    url = "http://127.0.0.1:8000/api/login/"
    payload = {"userName": username, "password": password}
    response = session.post(url, json=payload)

    if response.status_code == 200:
        save_session()
        return True, "Login successful"
    else:
        return False, response.json().get("error", "Login failed")

# Function to list all users (requires admin login session)
def list_users():
    """List all users (requires admin login session)"""
    url = f"{BASE_URL}/users/"
    response = session.get(url) # Send GET request to list users


    try:
        if response.status_code == 200:
            return response.json() # Return the user list if successful
        else:
            return [{
                "userId": "-",
                "userName": "ERROR",
                "email": response.json().get("error", f"HTTP {response.status_code}"),
                "role": {"roleName": "-"}
            }]
    except Exception as e: # Handle exceptions, such as invalid JSON response
        return [{
            "userId": "-",
            "userName": "ERROR",
            "email": f"Exception: {str(e)}",
            "role": {"roleName": "-"}
        }]
# Function to delete a user by username (requires admin login session)

def delete_user(user_name):
    """Delete a user by userName (requires admin login session)"""
    url = f"{BASE_URL}/users/delete/"
    payload = {"userName": user_name} # Send userName to delete
    response = session.post(url, json=payload) # Send POST request to delete user

    try:
        print("🔍 Status Code:", response.status_code)
        print("🔍 Raw Body:", response.text)
        
        data = response.json()

        # If status code is 200, it's a success
        if 200 <= response.status_code < 300:
            return True, data.get("message", "User deleted successfully")
        else:
            # Any other status means failure
            return False, data.get("error", f"HTTP {response.status_code}")
            
    except Exception:
        # If JSON parsing fails, handle it gracefully
        return False, f"Could not parse response: {response.status_code} → {response.text}"

# Function to activate a user by username (requires admin login session)

def activate_user(user_name):
    try:
        url = f"{BASE_URL}/users/activate/"  
        response = session.post(url, json={"userName": user_name})

        if response.status_code == 200:
            return True, response.json().get("message", "User activated successfully")
        else:
            return False, response.json().get("error", f"HTTP {response.status_code}")
    except Exception as e:
        return False, str(e)



