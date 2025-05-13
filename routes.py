from fastapi import APIRouter, HTTPException, status
from pydantic import BaseModel, Field
from typing import Optional, List
from bson import ObjectId
from database import listings_collection
from typing import List
from pydantic import BaseModel

# Response model
class ListingOut(BaseModel):
    _id: str
    title: str
    location: str
    price: int
    description: str
    reviews: list

router = APIRouter()


# --- Helper ---
class PyObjectId(ObjectId):
    @classmethod
    def _get_validators_(cls):
        yield cls.validate

    @classmethod
    def validate(cls, v):
        if not ObjectId.is_valid(v):
            raise ValueError("Invalid object ID")
        return ObjectId(v)

    @classmethod
    def _modify_schema_(cls, field_schema):
        field_schema.update(type="string")


# --- Models ---
class Review(BaseModel):
    username: str
    comment: str
    rating: int = Field(..., ge=1, le=5)


class Listing(BaseModel):
    title: str
    location: str
    price: int
    description: Optional[str] = None
    reviews: List[Review] = []

    class Config:
        schema_extra = {
            "example": {
                "title": "Cozy Beach House",
                "location": "Goa, India",
                "price": 150,
                "description": "A beautiful beachside home with sea view.",
                "reviews": []
            }
        }


# --- Routes ---

@router.get("/listings", response_model=List[ListingOut])
def get_listings():
    listings = list(listings_collection.find())
    for listing in listings:
        listing["_id"] = str(listing["_id"])  # Convert ObjectId to str
    return listings



@router.post("/listings", status_code=status.HTTP_201_CREATED)
def add_listing(listing: Listing):
    listing_dict = listing.dict()
    result = listings_collection.insert_one(listing_dict)
    return {"message": "Listing created", "id": str(result.inserted_id)}


@router.get("/listings/{id}")
def get_listing(id: str):
    try:
        listing = listings_collection.find_one({"_id": ObjectId(id)})
        if not listing:
            raise HTTPException(status_code=404, detail="Listing not found")
        listing["_id"] = str(listing["_id"])
        return listing
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid ID format")


@router.put("/listings/{id}")
def update_listing(id: str, listing: Listing):
    result = listings_collection.replace_one(
        {"_id": ObjectId(id)}, listing.dict()
    )
    if result.matched_count == 0:
        raise HTTPException(status_code=404, detail="Listing not found")
    return {"message": "Listing updated"}


@router.delete("/listings/{id}")
def delete_listing(id: str):
    result = listings_collection.delete_one({"_id": ObjectId(id)})
    if result.deleted_count == 0:
        raise HTTPException(status_code=404, detail="Listing not found")
    return {"message": "Listing deleted"}


@router.post("/listings/{id}/reviews", status_code=status.HTTP_201_CREATED)
def add_review(id: str, review: Review):
    try:
        result = listings_collection.update_one(
            {"_id": ObjectId(id)},
            {"$push": {"reviews": review.dict()}}
        )
        if result.matched_count == 0:
            raise HTTPException(status_code=404, detail="Listing not found")
        return {"message": "Review added"}
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid ID format" )