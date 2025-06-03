"""
================================================================================
User and Role Serializers - Django REST Framework
--------------------------------------------------------------------------------
Created by: Shanjida  

Description:
------------
This module defines serializers for the User and Role models, used to convert 
model instances to and from JSON format and to handle validation logic.

Serializers:
------------
- RoleSerializer:
    Serializes Role model with fields: roleId, roleName.

- UserSerializer:
    Serializes User model with support for:
    - Nested RoleSerializer (read-only)
    - Password hashing using Django's make_password
    - Validation for password length, unique userName and email
    - Role assignment during user creation via roleId from input data

Notes:
------
- The `password` field is write-only for security.
- Custom validation ensures proper user creation and data integrity.
================================================================================
"""



from rest_framework import serializers
from django.core.exceptions import ValidationError
from django.contrib.auth.hashers import make_password
from .models import User, Role

class RoleSerializer(serializers.ModelSerializer):
    class Meta:
        model = Role
        fields = ['roleId', 'roleName']

class UserSerializer(serializers.ModelSerializer):
    role = RoleSerializer(read_only=True)
    password = serializers.CharField(write_only=True)

    class Meta:
        model = User
        fields = ['userId', 'userName', 'password', 'email', 'firstName', 'lastName', 'mobileNo', 'isActive', 'role']

    def validate_password(self, value):
        if len(value) < 8:
            raise ValidationError("Password must be at least 8 characters.")
        return value

    def validate(self, data):
        if User.objects.filter(userName=data['userName']).exists():
            raise ValidationError({"userName": "This username is already taken."})
        if User.objects.filter(email=data['email']).exists():
            raise ValidationError({"email": "This email is already registered."})
        return data

    def create(self, validated_data):
        role_id = self.initial_data.get('role')
        try:
            role_instance = Role.objects.get(roleId=role_id)
        except Role.DoesNotExist:
            raise serializers.ValidationError({"role": "Invalid role ID."})

        validated_data['password'] = make_password(validated_data['password'])  # hash password
        validated_data['role'] = role_instance  # assign the role instance

        return User.objects.create(**validated_data)

