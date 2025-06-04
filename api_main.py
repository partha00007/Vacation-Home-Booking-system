#partha, This is the main entry point for the FastAPI application.

from fastapi import FastAPI
from routes import router
from dotenv import load_dotenv

# Load environment variables
load_dotenv()
# Create an instance of the FastAPI app
# This instance will be used to define and run the API
app = FastAPI()

# Include the routes from routes.py
app.include_router(router)
