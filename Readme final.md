                                                How to Run Everything:                                                          
1. Clone the Project
•	git clone <your-repo>
•	cd vacation-booking-project
2. Install Dependencies
•	pip install -r requirements.txt
If requirements.txt is missing, install manually:
•	pip install django fastapi pymongo python-dotenv requests click rich uvicorn

3. (dot)  .env Configuration (Very Important)
•	MONGODB_URI=mongodb+srv://<your-username>:<your-password>@cluster0.mongodb.net/?retryWrites=true&w=majority

4. fd.env (used by CLI tool)
•	FASTAPI_BASE_URL=http://127.0.0.1:8001
•	DJANGO_BASE_URL=http://127.0.0.1:8000

5.  Django (Authentication Service)
•	python manage.py runserver 8000

6.  FastAPI (Listings Service)
•	uvicorn main:app --reload --port 8001

7. CLI Tool (Run from root project)
python Cli_admin.py --help
python Cli_admin.py login <username> <password>
Use this: python Cli_admin.py login partha08 641526641527
python Cli_admin.py users list
python Cli_admin.py users delete <username>
Seeing the list:
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
