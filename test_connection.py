from database import listings_collection

# Test: insert a simple listing
sample = {
    "title": "Test Home",
    "location": "Mumbai",
    "price": 100,
    "description": "Test entry",
    "reviews": []
}

result = listings_collection.insert_one(sample)
print("Inserted ID:", result.inserted_id)
