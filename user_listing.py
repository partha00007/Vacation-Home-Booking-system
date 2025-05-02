import requests

API_URL = "http://localhost:8000/api"  # dummy link

#for listing
def list_listings():
    response = requests.get(f"{BASE_URL}/listings/")
    return response.json()

def delete_listing(listing_id):
    response = requests.delete(f"{BASE_URL}/listings/{listing_id}")# dummy link and need listing id
    return response.status_code, response.text