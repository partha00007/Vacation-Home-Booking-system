from django.http import JsonResponse
from rest_framework.decorators import api_view
from rest_framework.parsers import JSONParser
from django.utils import timezone
from .models import User, Role
from .serializers import UserSerializer
from django.contrib.auth.hashers import check_password

@api_view(['POST'])
def register(request):
    
    role_id = request.data.get('role')
    try:
        role = Role.objects.get(roleId=role_id)

    except Role.DoesNotExist:
        return JsonResponse({'error': 'Role with ID {} does not exist.'.format(role_id)}, status=400)

   
    serializer = UserSerializer(data=request.data)

    # Check if the data is valid
    if serializer.is_valid():
        try:
            # Try saving the user and return a success response
            user = serializer.save()
            return JsonResponse(serializer.data, status=201)  # User created successfully
        except Exception as e:
            # If there's an exception during the save, log it and return a 500 error
            print("Error during user save:", str(e))
            return JsonResponse({'error': 'Internal server error during save.'}, status=500)
    else:
        # Log validation errors and return a 400 error
        print("Validation Errors:", serializer.errors)
        return JsonResponse(serializer.errors, status=400)  # Bad request if validation fails

@api_view(['GET'])
def login(request):
    username = request.GET.get('userName')
    password = request.GET.get('password')

    if not username or not password:
        return JsonResponse({'error': 'Username and password are required'}, status=400)

    try:
        user = User.objects.get(userName=username, isDeleted=False, isActive=True)
    except User.DoesNotExist:
        return JsonResponse({'error': 'Invalid credentials or user does not exist'}, status=400)

    if check_password(password, user.password):
        user.isLogin = True
        user.loginTime = timezone.now()
        user.save()

        user_data = {
            'userName': user.userName,
            'email': user.email,
            'firstName': user.firstName,
            'lastName': user.lastName,
            'mobileNo': user.mobileNo,
            'role': user.role.roleName,
        }

        return JsonResponse({'message': 'Login successful', 'user': user_data}, status=200)

    return JsonResponse({'error': 'Invalid credentials'}, status=400)
@api_view(['GET'])
def logout(request):
    # Get the username from query parameters
    username = request.GET.get('userName')

    if not username:
        return JsonResponse({'error': 'Username is required'}, status=400)

    try:
        # Find the user who is logged in and ensure they are not deleted
        user = User.objects.get(userName=username, isLogin=True, isDeleted=False)
    except User.DoesNotExist:
        return JsonResponse({'error': 'User not logged in or does not exist'}, status=400)

    # Update logout time and status
    user.isLogin = False
    user.logoutTime = timezone.now()
    user.save()

    return JsonResponse({'message': 'Logout successful'}, status=200)
