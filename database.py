from pymongo import MongoClient
import os
from dotenv import load_dotenv

load_dotenv()

client = MongoClient(os.getenv("MONGODB_URI"))
db = client["sample_airbnb"]
listings_collection = db["listingsAndReviews"]
