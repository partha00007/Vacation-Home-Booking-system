
import requests

API_URL = "http://localhost:8000/api"  # dummy link
#for user
def list_users():
    response = requests.get(f"{BASE_URL}/users/")# dummy link
    return response.json()

def delete_user(user_id):
    response = requests.delete(f"{BASE_URL}/users/{user_id}")# dummy link and need user id 
    return response.status_code, response.text



