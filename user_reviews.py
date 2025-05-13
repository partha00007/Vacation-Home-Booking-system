import requests

API_URL = "http://localhost:8000/api"  # dummy link
#for review
def list_reviews():
    response = requests.get(f"{BASE_URL}/reviews/")
    return response.json()

def delete_review(review_id):
    response = requests.delete(f"{BASE_URL}/reviews/{review_id}")# dummy link and need review id 
    return response.status_code, response.text