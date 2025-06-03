"""
================================================================================
User and Role Models - Django ORM
--------------------------------------------------------------------------------
Created by: Shanjida  


Description:
------------
Defines the core database models for user management using Django's ORM.

Models:
-------
1. Role:
    - roleId (Primary Key)
    - roleName (Unique)
    - isDeleted (Soft delete flag)

2. User:
    - userId (Primary Key)
    - userName (Unique)
    - password (Hashed, automatically on save)
    - firstName, lastName
    - email (Unique), mobileNo
    - role (ForeignKey to Role)
    - loginTime, logoutTime
    - isLogin, isActive, isDeleted (Flags for session and state tracking)

Features:
---------
- User passwords are securely hashed using Django’s `make_password` before save.
- Includes `__str__` methods for clear model string representations.
- Supports soft deletion via `isDeleted` flags.
================================================================================
"""

from django.db import models
from django.contrib.auth.hashers import make_password

class Role(models.Model):
    roleId = models.AutoField(primary_key=True)
    roleName = models.CharField(max_length=100, unique=True)
    isDeleted = models.BooleanField(default=False)

    def __str__(self):
        return self.roleName

class User(models.Model):
    userId = models.AutoField(primary_key=True)
    userName = models.CharField(max_length=150, unique=True)
    password = models.CharField(max_length=128)
    firstName = models.CharField(max_length=50)
    lastName = models.CharField(max_length=50)
    email = models.EmailField(unique=True)
    mobileNo = models.CharField(max_length=15)
    role = models.ForeignKey(Role, on_delete=models.CASCADE)
    loginTime = models.DateTimeField(null=True, blank=True)
    logoutTime = models.DateTimeField(null=True, blank=True)
    isLogin = models.BooleanField(default=False)
    isActive = models.BooleanField(default=True)
    isDeleted = models.BooleanField(default=False)

    def save(self, *args, **kwargs):
        # Automatically hash password if it's not already hashed
        if not self.password.startswith('pbkdf2_sha256'):
            self.password = make_password(self.password)
        super().save(*args, **kwargs)

    def __str__(self):
        return self.userName
