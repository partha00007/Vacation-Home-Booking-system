"""
================================================================================
Django Admin Configuration for User Management
--------------------------------------------------------------------------------
Created by: Shanjida  

Description:
------------
Registers the User and Role models with the Django admin interface, enabling 
administrators to manage user accounts and roles through the built-in admin panel.

Models Registered:
------------------
- User
- Role

Note:
-----
You can extend this file to customize how these models are displayed and managed
in the admin UI (e.g., list display, search fields, filters).
================================================================================
"""

from django.contrib import admin
from .models import User, Role

admin.site.register(User)
admin.site.register(Role)
