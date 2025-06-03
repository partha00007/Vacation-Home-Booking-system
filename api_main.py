# api_main.py
from fastapi import FastAPI
from routes import router
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

app = FastAPI()

# Include the routes from routes.py
app.include_router(router)
