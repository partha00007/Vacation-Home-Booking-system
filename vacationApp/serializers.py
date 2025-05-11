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
        fields = ['userId', 'userName', 'password', 'email', 'firstName', 'lastName', 'mobileNo', 'role', 'isActive']

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

