from rest_framework import serializers
from .models import *

from django.contrib.auth.models import User
from django.contrib.auth import authenticate



class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['username', 'password', 'email']
        extra_kwargs = {'password': {'write_only': True}}

    def create(self, validated_data):
        user = User.objects.create_user(**validated_data)
        return user

    def validate(self, data):
        username = data.get('username')
        password = data.get('password')
        if not authenticate(username=username, password=password):
            raise serializers.ValidationError("Invalid credentials")
        return data
    
    