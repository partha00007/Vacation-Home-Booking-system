# 🏡 Vacation Home Booking System – Backend API

This is the **backend REST API** for the Vacation Home Booking System, built using **Django** and **Django REST Framework**. It handles user registration, login, logout, and role-based access.

---

## 🧰 Requirements

- Python 3.9+
- pip
- Git (optional, but helpful)
- A virtual environment tool (recommended: `venv`)

---

## 🚀 Getting Started

### 1. **Clone the repository**
```bash
git clone https://github.com/yourusername/vacation-home-booking-system.git
cd vacation-home-booking-system
```

### 2. **Create a virtual environment and activate it**
```bash
python -m venv venv
# Activate on Windows
venv\Scripts\activate
# Or on macOS/Linux
source venv/bin/activate
```

### 3. **Install dependencies**
```bash
pip install -r requirements.txt
```


### 4. **Apply migrations**
```bash
python manage.py makemigrations
python manage.py migrate
```

### 5. **Create a superuser (for admin access)**
```bash
python manage.py createsuperuser
```

### 6. **Run the server**
```bash
python manage.py runserver
```

Server will be running at: [http://127.0.0.1:8000/](http://127.0.0.1:8000/)

---



---

## 📌 API Endpoints

> Base URL: `http://127.0.0.1:8000/api/`

### 🔐 Register a new user

**POST** `/api/register/`

**Body (x-www-form-urlencoded or JSON):**
```json
{
  "userName": "demo",
  "password": "987654321",
  "firstName": "demo",
  "lastName": "demo",
  "email": "d@gmail.com",
  "mobileNo": "011323243434",
  "role": "1"
}
```

### 🔑 Login

**GET** `/api/login/?userName=ira&password=987654321`

**Returns:**
```json
{
  "message": "Login successful",
  "user": {
    "userName": "ira",
    "email": "shanjida@gmail.com",
    "firstName": "Shanjida",
    "lastName": "Rahaman",
    "mobileNo": "011323243434",
    "role": "Admin"
  }
}
```

### 🚪 Logout

**GET** `/api/logout/?userName=ira`

---

## ⚙️ Admin Panel

Access the Django admin at:

[http://127.0.0.1:8000/admin](http://127.0.0.1:8000/admin)

Login with your superuser credentials.

---

## 📝 Notes

- Ensure your `User` model includes fields like `isDeleted`, `isLogin`, `loginTime`, `logoutTime`, and links to `Role`.
- You must have predefined roles in your `Role` model (e.g., Admin, User).
- Always check migrations after modifying models.

---

## 📬 Support

If you get stuck, feel free to open an issue or contact the developer.

# Vacation Home Listings Microservice

This is the backend microservice for managing vacation home listings and user reviews.

## Features

•⁠  ⁠Add vacation listings
•⁠  ⁠View all listings (in process)
•⁠  ⁠View individual listing (in process)
•⁠  ⁠Add reviews
•⁠  ⁠Built with FastAPI + MongoDB Atlas
•⁠  ⁠Runs inside Docker

## Setup Instructions

### Using Docker
```bash
docker-compose up --build