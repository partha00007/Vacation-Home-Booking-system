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


## 📌 Endpoints

---

### 🔐 Register User

**URL:** `/api/register`
**Method:** `POST`
**Auth Required:** ❌

**Request Body:**

```json
{
  "userName": "johndoe",
  "password": "securepassword",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "mobileNo": "1234567890",
  "role": 1
}
```

**Success Response:**

```json
{
  "message": "registration successful",
  "user": {
    ...user data...
  }
}
```

**Error Responses:**

* 400: Role does not exist or validation errors
* 500: Internal server error

---

### 🔑 Login

**URL:** `/api/login`
**Method:** `POST`
**Auth Required:** ❌

**Request Body:**

```json
{
  "userName": "johndoe",
  "password": "securepassword"
}
```

**Success Response:**

```json
{
  "message": "Login successful",
  "user": {
    "userName": "johndoe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "mobileNo": "1234567890",
    "role": "User"
  }
}
```

**Error Responses:**

* 400: Missing fields or invalid credentials

---

### 🚪 Logout

**URL:** `/api/logout`
**Method:** `POST`
**Auth Required:** ✅ (via session)

**Success Response:**

```json
{
  "message": "Logout successful"
}
```

**Error Responses:**

* 400: User not logged in

---

### 👥 Get All Users (Admin Only)

**URL:** `/api/get_all_users`
**Method:** `GET`
**Auth Required:** ✅ (Admin only)

**Success Response:**

```json
[
  {
    "userName": "johndoe",
    "email": "john@example.com",
    ...
  },
  ...
]
```

**Error Responses:**

* 401: Authentication required
* 403: Permission denied

---

### ✅ Get Active Users (Admin Only)

**URL:** `/api/get_active_users`
**Method:** `GET`
**Auth Required:** ✅ (Admin only)

**Success Response:**

```json
[
  {
    "userName": "janedoe",
    "email": "jane@example.com",
    ...
  },
  ...
]
```

**Error Responses:**

* 401: Authentication required
* 403: Permission denied

---

### ❌ Delete User (Admin Only)

**URL:** `/api/delete_user`
**Method:** `POST`
**Auth Required:** ✅ (Admin only)

**Request Body:**

```json
{
  "userName": "johndoe"
}
```

**Success Response:**

```json
{
  "message": "User deleted successfully"
}
```

**Error Responses:**

* 401: Authentication required
* 403: Permission denied
* 404: User not found

---

### 🟢 Activate User (Admin Only)

**URL:** `/api/activate_user`
**Method:** `POST`
**Auth Required:** ✅ (Admin only)

**Request Body:**

```json
{
  "userName": "johndoe"
}
```

**Success Response:**

```json
{
  "message": "User activated successfully"
}
```

**Error Responses:**

* 401: Authentication required
* 403: Permission denied
* 404: User not found
* 400: User is already active

---

## ⚠️ Notes

* Session-based authentication is used (`request.session`).
* Only users with `roleName` = `'admin'` are authorized to use admin-level endpoints.
* Users are soft-deleted using the `isDeleted` field.
* Ensure the `Role` model is properly populated for registration to work.

---

## ✅ Requirements

* Django
* Django REST Framework

---

## 🛠️ Run the Server

```bash
python manage.py runserver
```

---

## 🧪 Testing

Use tools like Postman or curl to test endpoints, making sure cookies are preserved for session-based authentication.


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