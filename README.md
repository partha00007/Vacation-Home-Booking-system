# 🏡 Vacation Home Booking System
This is a group project of 3 people. Distribution of this project preson 1(Django),person 2 (Android App) and me Partha Munna( Fastapi -CLI Tools-Mongodb)
A full-stack microservices-based application that simulates an Airbnb-style platform for vacation home listings. Built using **Django**, **FastAPI**, **MongoDB Atlas**, and a powerful **CLI Admin Tool**, it runs seamlessly using **Docker**.

---

## 📚 Project Features

- 🔐 User registration, login/logout via Django
- 🏘️ Listing and review system using FastAPI with MongoDB
- 🛠️ CLI Admin Tool for managing users, listings, and reviews
- 🐳 Dockerized for simple setup and container orchestration

---

## 🧰 Technology Stack

- **Backend Authentication:** Django + Django REST Framework
- **Listing & Reviews API:** FastAPI + PyMongo
- **Database:** MongoDB Atlas (Airbnb sample dataset)
- **CLI Tool:** Python-based script
- **Deployment:** Docker + Docker Compose

---

## ✅ Requirements

- Python 3.9+
- Docker & Docker Compose
- MongoDB Atlas account with access URI
- Git, Postman (for API testing - optional)

---

## 🔁 Clone and Enter the Project

```bash
git clone https://gitlab.hsrw.eu/31245/vacation_home_booking_system
cd vacation_home_booking_system
```

---

## 🔑 Configure Environment Variables

### `config/.env` (used by Docker)
```
MONGODB_URI=mongodb+srv://<your-user>:<your-password>@cluster.mongodb.net/
```

### `fd.env` (used by CLI Tool)
```
FASTAPI_BASE_URL=http://127.0.0.1:8001
DJANGO_BASE_URL=http://127.0.0.1:8000
```

---

## 🐳 Docker Setup

### Step 1: Build and Start All Services

```bash
docker-compose up --build
```

Services started:
- Django @ http://localhost:8000
- FastAPI @ http://localhost:8001

If you see an error, make sure `docker-compose.yml` is in your current directory:
```bash
dir docker-compose.yml
```

---

### Step 2: Apply Django Migrations

```bash
docker-compose exec django python manage.py migrate
```

---

### Step 3: Create Superuser

```bash
docker-compose exec django python manage.py createsuperuser
```

Visit the Django admin panel:  
👉 http://localhost:8000/admin

---

## 💻 CLI Admin Tool

### Step 1: Set Up Python Environment

```bash
python -m venv venv
# On Windows
.env\Scriptsctivate
# On Linux/macOS
source venv/bin/activate
```

---

### Step 2: Install Requirements

```bash
python -m pip install -r requirements.txt
```

---

### Step 3: Use CLI Commands

#### 🔐 Login
```bash
python Cli_admin.py login <username> <password>
```

#### 📋 General Help
```bash
python Cli_admin.py --help
```

#### 👥 Users
```bash
python Cli_admin.py users list
python Cli_admin.py users delete
python Cli_admin.py users activate
```

#### 🏘️ Listings
```bash
python Cli_admin.py listings list
python Cli_admin.py listings get <listing_id>
python Cli_admin.py listings delete <listing_id>
```

#### 📝 Reviews
```bash
python Cli_admin.py reviews list <listing_id>
python Cli_admin.py reviews add <listing_id>
python Cli_admin.py reviews delete <listing_id> <review_id>
```

#### ✅ Test Backend APIs
```bash
python Cli_admin.py test
```

---

## 🧪 API Testing with Swagger/Postman

### 🔗 FastAPI (Listings & Reviews)
- Base URL: `http://localhost:8001`
- Swagger UI: `http://localhost:8001/docs`

**Endpoints:**
- `GET /listings`
- `GET /listings/{id}`
- `POST /listings/{id}/reviews`

---

### 🔗 Django (Authentication)
- Base URL: `http://localhost:8000`

**Endpoints:**
- `POST /register`
- `POST /login`
- `POST /logout`
- `GET /validate_session`
- `GET /get_all_users`
- `POST /delete_user`

> *Use session cookie (`sessionid`) for protected routes.*

---

## 🧠 Sample Output

**GET** `/listings/1003530/reviews`  
**Header:**  
```
Cookie: sessionid=<your-session-id>
```

**Sample JSON Response:**
```json
{
  "_id": "4351675",
  "date": "2013-04-29T04:00:00",
  "listing_id": "1003530",
  "reviewer_name": "Josh",
  "comments": "I had a really pleasant stay..."
}
```

---

## 🔁 Clean Restart

```bash
docker-compose down -v
docker-compose up --build
```

---

## 🧠 Troubleshooting

- **Docker errors?**
  - Ensure `docker-compose.yml` is in the current directory.
  - Try `docker-compose down -v` then rebuild.

- **MongoDB connection issues?**
  - Double-check the `MONGODB_URI` in `.env`.

- **Session issues in Postman?**
  - Use cookies or manually set the `sessionid` header.


---

## 📱 Android Frontend (Kotlin - Android Studio)

### 🧊 Emulator Troubleshooting

If Android Studio shows:

> **Error running 'app': Device is already activating**  
> **No connected devices**

Follow these steps:

#### ✅ 1. Cold Boot the Emulator
- Go to **Tools > Device Manager**
- Click the **▼ dropdown** next to your emulator and select **Cold Boot Now**

#### ✅ 2. Wait Until Boot Finishes
- The emulator must be fully booted (home screen responsive, no loading spinner)
- Test by opening the app drawer or dragging down notifications

#### ✅ 3. Check for Connection
- Wait for Android Studio to detect the device (dropdown near the ▶ Run button should show it)
- Once listed, hit ▶ again

#### 🔁 Still Stuck?
- Close the emulator and restart Android Studio
- Open **Device Manager**, **cold boot** again, and try running the app

> If needed, create a new emulator in **Device Manager** with a recommended image like **Pixel 6 API 33+**


Once the backend (Django & FastAPI) and MongoDB are fully running:


### ✅ Requirements
- Android Studio (latest version)
- Android SDK 33+
- Internet connection to fetch dependencies

### 🚀 Setup & Run

1. Open **Android Studio**
2. Select **"Open"** and navigate to the `frontend/` folder in this project
3. Let **Gradle Sync** complete (automatically starts)
4. Choose an emulator or connected device
5. Click **Run ▶️** to launch the application

> The frontend connects to the backend running on `localhost`, so if you're using a real device, you may need to adjust `BASE_URL` in the app to point to your machine’s local IP.

