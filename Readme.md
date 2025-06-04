Download and open docker 
Login to docker and make sure your engine is running
And now need to install docker on vs studio
Check if your docker versions are ok with this 
docker --version
docker compose version

Use this command 

docker compose up --build

attention , if error check your docker.yml is the same directory with 
dir docker-compose.yml

if you want to migrate anything 
docker compose exec django python manage.py migrate

if you want to check a superuser “
docker compose exec django python manage.py createsuperuser

Now for cli_admin

Open a virtual environment and active it
python -m venv venv
.\venv\Scripts\activate

Use this command to install everything
python -m pip install -r requirements.txt
check if it work and how 
python Cli_admin.py –help

login with super user 
python Cli_admin.py login partha08 641526641527
Check the user and admin
python Cli_admin.py users list
want to delete a user :
python Cli_admin.py users delete


listing list from here (make sure you login to mongodb and change your details in .env file , only your user name and pass )

from backend (fastapi)
open postman (get)

http://localhost:8001/listings

POST

python Cli_admin.py listings list 
Delete the listing: 
python Cli_admin.py listings delete <listing_id>
Example: python Cli_admin.py listings delete 10084023
Reviews list:
python Cli_admin.py reviews list <listing_id>
for example:  python Cli_admin.py reviews list 10091713
Delete Reviews:
python Cli_admin.py reviews delete <listing id> <review_id>
for example python Cli_admin.py reviews delete 10091713 244574712
Run backend API tests via CLI:
python Cli_admin.py test

Vacation Home Booking System - Full Setup & Run Guide (Docker + CLI + FastAPI + Django)
________________________________________
🔍 Project Overview
This is a microservices-based full stack project featuring:
•	Django (8000): User registration, login, and admin controls.
•	FastAPI (8001): Listings and reviews with MongoDB Atlas.
•	CLI Tool: Admin-level command-line control.
•	Postman / Swagger: API testing.
•	Docker: Run everything in one command.
________________________________________
✅ Requirements
•	Python 3.9+
•	Docker & Docker Compose
•	MongoDB Atlas URI 
•	Postman (for manual testing)
________________________________________
📁 File Structure
VACATION_HOME_BOOKING_SYSTEM/
├── assets/                           # Static files (images, icons, etc.)
├── config/                           # Configuration files (e.g., .env)
├── vacationApp/                      # Django application folder
│   ├── migrations/                   # Django migration files
│   ├── __init__.py                   # Init file for the Django app
│   ├── admin.py                      # Django admin interface configurations
│   ├── apps.py                       # App configuration
│   ├── models.py                     # Database models
│   ├── serializers.py                # Serializers for Django models (for API responses)
│   ├── tests.py                      # Django tests
│   ├── urls.py                       # URL routing for Django
│   ├── views.py                      # Views for Django
│   ├── auth.py                       # Authentication logic
│   ├── database.py                   # Database related configurations
│   └── session_helper.py             # Helper functions for session management
├── config/                           # Configuration files
│   ├── .env                          # Environment variable file for sensitive information
├── venv/                             # Python virtual environment
├── .env                               # Global environment variable file
├── api_main.py                        # Main entry point for FastAPI
├── cli_admin.py                       # CLI admin tool for user management
├── docker-compose.yml                 # Docker compose file for service orchestration
├── Dockerfile                         # Dockerfile for the project
├── Dockerfile.fastapi                 # Dockerfile for FastAPI
├── requirements.txt                   # List of dependencies for the project
├── list_user.py                       # CLI script to list users
├── main.py                            # Entry point for FastAPI
├── manage.py                          # Django manage script
├── README.md                          # Project overview file
├── README_final.md                    # Final README file
├── routes.py                          # Routes for FastAPI
├── session.json                       # Session data for storage
└── user_listing.py                    # User listing-related logic________________________________________



⚙️ Environment Setup
config/.env (used by Docker)
MONGODB_URI=mongodb+srv://<user>:<pass>@cluster.mongodb.net/?retryWrites=true&w=majority (put your username and password,remove mine)
fd.env (used by CLI Tool)
FASTAPI_BASE_URL=http://127.0.0.1:8001
DJANGO_BASE_URL=http://127.0.0.1:8000
________________________________________
🚧 Docker Setup
Step 1: Clone the Repo
git clone https://gitlab.hsrw.eu/31245/vacation_home_booking_system
cd vacation_home_booking_system
Step 2: Run Everything
docker-compose up --build
This starts:
•	Django @ localhost:8000
•	FastAPI @ localhost:8001
•	MongoDB (local or Atlas)
________________________________________
👤 Django Admin Setup
Step 1: Apply Migrations
docker-compose exec django python manage.py migrate
Step 2: Create Superuser
docker-compose exec django python manage.py createsuperuser
Step 3: Access Admin Panel
Visit: http://localhost:8000/admin
________________________________________

📂 CLI Tool Commands
CLI Tool Commands
•	python Cli_admin.py login <username> <password>
•	python Cli_admin.py users list
•	python Cli_admin.py users delete <username>
•	python Cli_admin.py users activate <username>
•	python Cli_admin.py listings list
•	python Cli_admin.py listings get <listing_id>
•	python Cli_admin.py listings delete <listing_id>
•	python Cli_admin.py reviews list <listing_id>
•	python Cli_admin.py reviews delete <listing_id> <review_id>
•	python Cli_admin.py reviews add <listing_id> <reviewer> <comment> <rating>
•	python Cli_admin.py test
________________________________________
🔧 Postman or Swagger API Testing
FastAPI @ http://localhost:8001/docs
•	GET /listings
•	GET /listings/{id}
•	POST /listings
•	POST /listings/{id}/reviews
•	DELETE endpoints available too
Django @ http://localhost:8000
•	POST /api/register
•	POST /api/login
•	GET /api/get_all_users
•	POST /api/delete_user
Note: Session cookie required for authenticated endpoints.
________________________________________
🚀 Useful Tips
•	Restart system with:
docker-compose down -v
docker-compose up --build
•	Use Swagger UI for easy API testing
•	Use Postman with cookies if needed
________________________________________
