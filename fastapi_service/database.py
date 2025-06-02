from pymongo import MongoClient
from dotenv import load_dotenv
import os
#partha (made this changes)
# Load environment variables from a .env file in the same directory

load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), "..", "config", ".env"))

# Get MongoDB URI from environment
mongo_uri = os.getenv("MONGODB_URI")

if not mongo_uri:
    raise ValueError("MONGODB_URI not found in .env file")

# Connect to MongoDB
client = MongoClient(mongo_uri)

# Access the sample_airbnb database and the listingsAndReviews collection
db = client["sample_airbnb"]
listings_collection = db["listingsAndReviews"]

