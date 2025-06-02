# 🏡 Vacation Home Booking System – Backend (Django + FastAPI)

This project consists of two backend services:

- **Django + Django REST Framework**: Handles user authentication, admin controls, and role-based access.
- **FastAPI + MongoDB**: Manages vacation home listings and user reviews.
- Also includes a **CLI Tool** for administrative tasks.

---

## 🧰 Requirements

- Python 3.9+
- pip
- Git (optional)
- `venv` (for virtual environments)
- MongoDB Atlas connection string

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://gitlab.hsrw.eu/31245/vacation_home_booking_system
cd vacation-home-booking-system
```

### 2. Create and Activate Virtual Environment

```bash
python -m venv venv
# On Windows
venv\Scripts\activate
# On macOS/Linux
source venv/bin/activate
```

### 3. Install Dependencies

If you have a `requirements.txt`:
```bash
pip install -r requirements.txt
```

If not:
```bash
pip install django djangorestframework fastapi pymongo python-dotenv requests click rich uvicorn
```

---

## 🔐 Environment Variables

Create two files in the root project directory:

### `.env` (for FastAPI/Django shared configs)
```
MONGODB_URI=mongodb+srv://<username>:<password>@cluster0.mongodb.net/?retryWrites=true&w=majority
```

### `fd.env` (used by the CLI tool)
```
FASTAPI_BASE_URL=http://127.0.0.1:8001
DJANGO_BASE_URL=http://127.0.0.1:8000
```

---

## ⚙️ Running the Servers

### Start Django (port 8000)
```bash
python manage.py makemigrations
python manage.py migrate
python manage.py createsuperuser
python manage.py runserver 8000
```

### Start FastAPI (port 8001)
```bash
uvicorn main:app --reload --port 8001
```

### Or start both using `concurrently`
```bash
npm install -g concurrently
concurrently "uvicorn fastapi_service.main:app --reload --port 8001" "python backend/manage.py runserver 8000"
```

---

## 🧪 CLI Tool (Admin Operations)

### Basic usage
```bash
python Cli_admin.py --help
```

### Authentication
```bash
python Cli_admin.py login <username> <password>
```

### Manage Users
```bash
python Cli_admin.py users list
python Cli_admin.py users delete <username>
```

### Manage Listings
```bash
python Cli_admin.py listings list
python Cli_admin.py listings delete <listing_id>
```

### Manage Reviews
```bash
python Cli_admin.py reviews list <listing_id>
python Cli_admin.py reviews delete <listing_id> <review_id>
```

### Run API Tests
```bash
python Cli_admin.py test
```

---

## 📬 Django API Endpoints

### 🔐 Register User
- `POST /api/register`

### 🔑 Login
- `POST /api/login`

### 🚪 Logout
- `POST /api/logout`

### 👥 Admin User Controls
- `GET /api/get_all_users`
- `GET /api/get_active_users`
- `POST /api/delete_user`
- `POST /api/activate_user`

---

## 🏠 FastAPI Listings Microservice

- Add/view listings
- Add/view/delete reviews
- Connected to MongoDB Atlas
- Exposed via CLI tool

---

## ⚙️ Admin Panel (Django)

- URL: [http://127.0.0.1:8000/admin](http://127.0.0.1:8000/admin)
- Use the superuser credentials you created.

---

## 📝 Developer Notes

- Django uses session-based authentication (`request.session`)
- Ensure `Role` model is populated for proper user registration
- Users are soft-deleted (`isDeleted` field)
- FastAPI uses MongoDB Atlas and environment variables via `.env`

---

## 💾 Save Your Dependencies

```bash
pip freeze > requirements.txt
```

---

---

## 🐳 Docker Usage

You can run the entire stack (Django, FastAPI, and MongoDB) using Docker.

### 🧱 1. Project Structure (important)
Make sure your project is structured like this:

```
vacation_home_booking_system/
├── backend/                # Django project with manage.py
├── fastapi_service/        # FastAPI service (main.py, etc.)
├── config/                 # Contains .env with MONGODB_URI
├── Dockerfile.django
├── Dockerfile.fastapi
├── docker-compose.yml
├── requirements.txt
└── .dockerignore
```

### ⚙️ 2. Update `.env`

If you're using **MongoDB Atlas (cloud)**, leave `.env` like this:

```env
MONGODB_URI=mongodb+srv://<username>:<password>@cluster.mongodb.net/?retryWrites=true&w=majority
```

> ✅ Do NOT include a `mongo:` service in `docker-compose.yml` in that case.

If you're using **local MongoDB via Docker**, then set:

```env
MONGODB_URI=mongodb://mongo:27017/
```

And include the `mongo:` service in your `docker-compose.yml`.

---

### 🚀 3. Build and Start Services

```bash
docker-compose up --build
```

---

### 👤 4. Create Django Superuser (if needed)

```bash
docker-compose exec django python manage.py migrate
docker-compose exec django python manage.py createsuperuser
```

---

### 🧪 5. Access the Services

- Django: http://localhost:8000/
- FastAPI: http://localhost:8001/

---

### 🧼 6. Stop and Clean Up

```bash
docker-compose down -v
```

This will stop containers and delete volumes.

---

## 🧠 Support

If you get stuck, open an issue or contact the developer.
