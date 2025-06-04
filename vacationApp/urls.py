"""
================================================================================
User Management API URL Configuration - Django
--------------------------------------------------------------------------------
Created by: Shanjida  
Modified by: Partha  
Last Updated: [Insert Date Here]

Description:
------------
Defines URL patterns for user management features in the application. These routes 
are connected to the corresponding view functions that handle registration, login, 
logout, session validation, user activation, deletion, and data retrieval.

Key Endpoints:
--------------
- POST   /register/           : Register a new user
- POST   /login/              : Authenticate user and initiate session
- POST   /logout/             : Logout user and clear session
- GET    /users/              : Get all users (admin only)
- GET    /users/active/       : Get all active users (admin only)
- POST   /users/delete/       : Soft delete a user by username (admin only)
- POST   /users/activate/     : Reactivate a user account (admin only)
- GET    /validate_session/   : Check if user session is valid

Note:
-----
Some endpoints are restricted to admin users and require session-based authentication.
================================================================================
"""

from django.urls import path
from . import views

urlpatterns = [
    path('register/', views.register, name='register'),                # POST: Register new user
    path('login/', views.login, name='login'),                          # POST: Login user
    path('logout/', views.logout, name='logout'),                       # POST: Logout user

    path('users/', views.get_all_users, name='get_all_users'),          # GET: Get all users (admin only)
    path('users/active/', views.get_active_users, name='get_active_users'),  # GET: Get active users (admin only)

    path('users/delete/', views.delete_user, name='delete_user'),   # DELETE: Delete user by userName (admin only)
    path('users/activate/', views.activate_user, name='activate_user'),  # POST: Activate user by userName (admin only)
    path('validate_session/', views.validate_session, name='validate_session'), #partha 
    #path('listings/', views.get_listings, name='get_listings'), #partha 


]
