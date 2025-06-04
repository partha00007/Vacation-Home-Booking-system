#Shashank
from fastapi import FastAPI
from routes import router

app = FastAPI(title="Vacation Home Listings Microservice")

@app.get("/")
def home():
    return {"message": "Welcome to the Vacation Listings API"}

app.include_router(router)
