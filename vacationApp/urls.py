#Created by Shanjida but modified by Partha
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
