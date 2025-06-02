from fastapi import APIRouter, HTTPException, status, Depends
from bson import ObjectId
from bson.decimal128 import Decimal128
from .database import client, listings_collection
from .auth import validate_session
from pydantic import BaseModel, Field
from typing import List, Optional

router = APIRouter()

# --- Models ---
class Review(BaseModel):
    username: str
    comment: str
    rating: int = Field(..., ge=1, le=5)

class ListingOut(BaseModel):
    id: str = Field(..., alias="_id")  # Ensure it correctly maps '_id' to 'id'
    title: str
    location: str
    price: float
    description: str
    reviews: list

    class Config:
        allow_population_by_field_name = True

class Listing(BaseModel):
    title: str
    location: str
    price: float
    description: Optional[str] = None
    reviews: List[Review] = []

    class Config:
        schema_extra = {
            "example": {
                "title": "Cozy Beach House",
                "location": "Goa, India",
                "price": 150.0,
                "description": "A beachside home with sea view",
                "reviews": []
            }
        }

# --- Routes ---
@router.get("/db-status")
async def db_status():
    try:
        client.admin.command("ping")
        return {"status": "connected"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"DB error: {e}")
#---------------Delete Listing--------------------
@router.delete("/listings/{listing_id}", dependencies=[Depends(validate_session)])
async def delete_listing(listing_id: str):
    # Try to delete the listing directly by _id
    result = listings_collection.delete_one({"_id": listing_id})
    if result.deleted_count == 0:
        raise HTTPException(status_code=404, detail="Listing not found")
    return {"message": "Listing deleted successfully"}


@router.get("/listings", response_model=List[ListingOut], dependencies=[Depends(validate_session)])
async def get_listings():
    listings = list(listings_collection.find())
    cleaned_listings = []

    for listing in listings:
        listing["_id"] = str(listing["_id"])  # Convert ObjectId to string

        title = listing.get("name") or listing.get("title", "No Title")
        location = listing.get("address", {}).get("country", "Unknown")

        price = listing.get("price", 0)
        if isinstance(price, Decimal128):
            price = float(price.to_decimal())  # Correct conversion of Decimal128 to float

        description = listing.get("description", "No description")
        reviews = listing.get("reviews", [])

        cleaned_listings.append({
            "_id": listing["_id"],  # Correct field for MongoDB ObjectId
            "title": title,
            "location": location,
            "price": price,
            "description": description,
            "reviews": reviews
        })

    return cleaned_listings

@router.post("/listings", status_code=status.HTTP_201_CREATED)
def add_listing(listing: Listing):
    listing_dict = listing.dict()
    result = listings_collection.insert_one(listing_dict)
    return {"message": "Listing created", "id": str(result.inserted_id)}

@router.get("/listings/{_id}", response_model=ListingOut)
def get_listing(_id: str):
    try:
        listing = listings_collection.find_one({"_id": ObjectId(_id)})
        if not listing:
            raise HTTPException(status_code=404, detail="Listing not found")
        listing["_id"] = str(listing["_id"])
        return listing
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid ID format")

@router.put("/listings/{_id}")
def update_listing(_id: str, listing: Listing):
    result = listings_collection.replace_one(
        {"_id": ObjectId(_id)}, listing.dict()
    )
    if result.matched_count == 0:
        raise HTTPException(status_code=404, detail="Listing not found")
    return {"message": "Listing updated"}



@router.delete("/listings/{listing_id}/reviews/{review_id}", dependencies=[Depends(validate_session)]) #partha modified
async def delete_review(listing_id: str, review_id: str):
    result = listings_collection.update_one(
        {"_id": listing_id},
        {"$pull": {"reviews": {"_id": review_id}}}
    )
    if result.modified_count == 0:
        raise HTTPException(status_code=404, detail="Review not found")
    return {"message": "Review deleted successfully"}

@router.get("/listings/{listing_id}/reviews", dependencies=[Depends(validate_session)]) #partha modified
async def get_reviews_for_listing(listing_id: str):
    try:
        listing = listings_collection.find_one({"_id": listing_id})
        if not listing:
            raise HTTPException(status_code=404, detail="Listing not found")
        return listing.get("reviews", [])
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid ID format")


@router.post("/listings/{listing_id}/reviews", status_code=status.HTTP_201_CREATED, dependencies=[Depends(validate_session)]) #partha modified
async def add_review(listing_id: str, review: Review):
    try:
        result = listings_collection.update_one(
            {"_id": listing_id},
            {"$push": {"reviews": review.dict()}}
        )
        if result.matched_count == 0:
            raise HTTPException(status_code=404, detail="Listing not found")
        return {"message": "Review added"}
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid ID format")

