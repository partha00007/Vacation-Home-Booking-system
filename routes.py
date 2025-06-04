#This FastAPI router manages vacation home listings and their reviews, providing endpoints to create, read, update, delete listings and reviews while validating user sessions.
"""Originally created by Shashank, later modified by Partha :
Adding session-based validation for sensitive operations
Simplifying and adjusting the data models for more focused listing data
Improving error handling and data transformation
Cleaning up unnecessary attributes
Adding attribution of authorship and modification""" 

from fastapi import APIRouter, HTTPException, status, Depends
from bson import ObjectId
from bson.decimal128 import Decimal128
from database import client, listings_collection
from auth import validate_session
from pydantic import BaseModel, Field
from typing import List, Optional

router = APIRouter()

# --- Models ---
class Review(BaseModel):
    username: str
    comment: str
    rating: int = Field(..., ge=1, le=5)

class ListingOut(BaseModel):
    id: str = Field(..., alias="_id")  
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

# --- Routes --- #modification starts from here by Partha 
@router.get("/listings", response_model=List[ListingOut])
async def get_listings():
    # Fetch all listings, limit to 15
    listings = list(listings_collection.find().limit(15))
    
    cleaned_listings = []
    for listing in listings:
        # Ensure _id is always a string
        listing["_id"] = str(listing["_id"])
        
        # Use fallback values if keys missing
        title = listing.get("name") or listing.get("title", "No Title")
        location = listing.get("address", {}).get("country", "Unknown")
        price = listing.get("price", 0)
        if isinstance(price, Decimal128):
            price = float(price.to_decimal())
        else:
            price = float(price)
        description = listing.get("description", "No description")
        reviews = listing.get("reviews", [])[:2]

        # Create cleaned listing
        cleaned_listings.append({
            "_id": listing["_id"],
            "title": title,
            "location": location,
            "price": price,
            "description": description,
            "reviews": reviews
        })

    # Return the formatted list
    return cleaned_listings

#newly added
@router.get("/listings/{listing_id}", response_model=ListingOut)
async def get_listing(listing_id: str):
    try:
        # Try finding by string _id
        listing = listings_collection.find_one({"_id": listing_id})
        if not listing:
            # Fallback to ObjectId
            try:
                listing = listings_collection.find_one({"_id": ObjectId(listing_id)})
            except Exception:
                pass
            if not listing:
                raise HTTPException(status_code=404, detail="Listing not found")

        listing["_id"] = str(listing["_id"])
        listing["title"] = listing.get("name") or listing.get("title", "No Title")
        listing["location"] = listing.get("address", {}).get("country", "Unknown")

        price = listing.get("price", 0)
        if isinstance(price, Decimal128):
            price = float(price.to_decimal())
        elif price is None:
            price = 0.0
        else:
            try:
                price = float(price)
            except Exception:
                price = 0.0
        listing["price"] = price

        listing["description"] = listing.get("description", "No description")
        listing["reviews"] = listing.get("reviews", [])

        return listing

    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Invalid ID format: {e}")


# --- Fix other endpoints for _id handling ---

@router.delete("/listings/{listing_id}", dependencies=[Depends(validate_session)])
async def delete_listing(listing_id: str):
    try:
        result = listings_collection.delete_one({"_id": listing_id})
        if result.deleted_count == 0:
            # Try ObjectId fallback
            try:
                result = listings_collection.delete_one({"_id": ObjectId(listing_id)})
            except:
                pass
            if result.deleted_count == 0:
                raise HTTPException(status_code=404, detail="Listing not found")
        return {"message": "Listing deleted successfully"}
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Invalid ID format: {e}")

@router.delete("/listings/{listing_id}/reviews/{review_id}", dependencies=[Depends(validate_session)])
async def delete_review(listing_id: str, review_id: str):
    try:
        result = listings_collection.update_one(
            {"_id": listing_id},
            {"$pull": {"reviews": {"_id": review_id}}}
        )
        if result.modified_count == 0:
            # Fallback to ObjectId
            try:
                result = listings_collection.update_one(
                    {"_id": ObjectId(listing_id)},
                    {"$pull": {"reviews": {"_id": review_id}}}
                )
            except:
                pass
            if result.modified_count == 0:
                raise HTTPException(status_code=404, detail="Review not found")
        return {"message": "Review deleted successfully"}
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Invalid ID format: {e}")


@router.get("/listings/{listing_id}/reviews", dependencies=[Depends(validate_session)])
async def get_reviews_for_listing(listing_id: str):
    try:
        listing = listings_collection.find_one({"_id": listing_id})
        if not listing:
            # Try ObjectId fallback
            try:
                listing = listings_collection.find_one({"_id": ObjectId(listing_id)})
            except:
                pass
            if not listing:
                raise HTTPException(status_code=404, detail="Listing not found")
        return listing.get("reviews", [])
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Invalid ID format: {e}")

@router.post("/listings/{listing_id}/reviews", status_code=status.HTTP_201_CREATED, dependencies=[Depends(validate_session)])
async def add_review(listing_id: str, review: Review):
    try:
        result = listings_collection.update_one(
            {"_id": listing_id},
            {"$push": {"reviews": review.dict()}}
        )
        if result.matched_count == 0:
            # Fallback to ObjectId
            try:
                result = listings_collection.update_one(
                    {"_id": ObjectId(listing_id)},
                    {"$push": {"reviews": review.dict()}}
                )
            except:
                pass
            if result.matched_count == 0:
                raise HTTPException(status_code=404, detail="Listing not found")
        return {"message": "Review added"}
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Invalid ID format: {e}")