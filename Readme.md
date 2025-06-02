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
git clone <your-repo-url>
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
concurrently "uvicorn main:app --reload --port 8001" "python manage.py runserver 8000"
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

## 🧠 Support

If you get stuck, open an issue or contact the developer.
