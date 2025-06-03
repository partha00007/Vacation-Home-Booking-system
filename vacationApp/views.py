## Originally created by Shanjida, modified by Partha to add session validation for FastAPI

from django.http import JsonResponse
from rest_framework.decorators import api_view
from django.utils import timezone
from .models import User, Role
from .serializers import UserSerializer
from django.contrib.auth.hashers import check_password
from database import listings_collection


@api_view(['POST'])
def register(request):
    role_id = request.data.get('role')
    try:
        role = Role.objects.get(roleId=role_id)
    except Role.DoesNotExist:
        return JsonResponse({'error': f'Role with ID {role_id} does not exist.'}, status=400)

    serializer = UserSerializer(data=request.data)
    if serializer.is_valid():
        try:
            user = serializer.save()
            return JsonResponse({'message': 'registration successful', 'user': serializer.data}, status=201)
        except Exception as e:
            print("Error during user save:", str(e))
            return JsonResponse({'error': 'Internal server error during save.'}, status=500)
    else:
        print("Validation Errors:", serializer.errors)
        return JsonResponse(serializer.errors, status=400)

@api_view(['POST'])
def login(request):
    username = request.data.get('userName')
    password = request.data.get('password')

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

        # Store userName in session
        request.session['userName'] = user.userName

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

@api_view(['POST'])
def logout(request):
    username = request.session.get('userName')
    if not username:
        return JsonResponse({'error': 'User not logged in'}, status=400)

    try:
        user = User.objects.get(userName=username, isLogin=True, isDeleted=False)
    except User.DoesNotExist:
        return JsonResponse({'error': 'User not logged in or does not exist'}, status=400)

    user.isLogin = False
    user.logoutTime = timezone.now()
    user.save()

    # Clear session
    request.session.flush()

    return JsonResponse({'message': 'Logout successful'}, status=200)

@api_view(['GET'])
def get_all_users(request):
    username = request.session.get('userName')
    if not username:
        return JsonResponse({'error': 'Authentication required'}, status=401)

    try:
        logged_in_user = User.objects.get(userName=username, isDeleted=False, isLogin=True)
    except User.DoesNotExist:
        return JsonResponse({'error': 'User not logged in'}, status=401)

    if logged_in_user.role.roleName.lower() != 'admin':
        return JsonResponse({'error': 'Permission denied, admin only'}, status=403)

    users = User.objects.filter(isDeleted=False)
    serializer = UserSerializer(users, many=True)
    return JsonResponse(serializer.data, safe=False, status=200)

@api_view(['GET'])
def get_active_users(request):
    username = request.session.get('userName')
    if not username:
        return JsonResponse({'error': 'Authentication required'}, status=401)

    try:
        logged_in_user = User.objects.get(userName=username, isDeleted=False, isLogin=True)
    except User.DoesNotExist:
        return JsonResponse({'error': 'User not logged in'}, status=401)

    if logged_in_user.role.roleName.lower() != 'admin':
        return JsonResponse({'error': 'Permission denied, admin only'}, status=403)

    users = User.objects.filter(isDeleted=False, isActive=True)
    serializer = UserSerializer(users, many=True)
    return JsonResponse(serializer.data, safe=False, status=200)

@api_view(['POST'])
def delete_user(request):
    username = request.session.get('userName')
    deleteUser = request.data.get('userName')
    if not username:
        return JsonResponse({'error': 'Authentication required'}, status=401)

    try:
        logged_in_user = User.objects.get(userName=username, isDeleted=False, isLogin=True)
    except User.DoesNotExist:
        return JsonResponse({'error': 'User not logged in'}, status=401)

    if logged_in_user.role.roleName.lower() != 'admin':
        return JsonResponse({'error': 'Permission denied, admin only'}, status=403)

    try:
        user_to_delete = User.objects.get(userName=deleteUser, isDeleted=False)
    except User.DoesNotExist:
        return JsonResponse({'error': 'User not found'}, status=404)

    user_to_delete.isDeleted = True
    user_to_delete.save()

    return JsonResponse({'message': 'User deleted successfully'}, status=200)

@api_view(['POST'])
def activate_user(request):
    username = request.session.get('userName')
    activeUser = request.data.get('userName')

    if not username:
        return JsonResponse({'error': 'Authentication required'}, status=401)

    try:
        logged_in_user = User.objects.get(userName=username, isDeleted=False, isLogin=True)
    except User.DoesNotExist:
        return JsonResponse({'error': 'User not logged in'}, status=401)

    if logged_in_user.role.roleName.lower() != 'admin':
        return JsonResponse({'error': 'Permission denied, admin only'}, status=403)

    try:
        
        user_to_activate = User.objects.get(userName=activeUser, isDeleted=False)
        if user_to_activate.isActive:
            return JsonResponse({'error': 'User is already active'}, status=400)

    except User.DoesNotExist:
        return JsonResponse({'error': 'User not found'}, status=404)

    user_to_activate.isActive = True
    user_to_activate.save()

    return JsonResponse({'message': 'User activated successfully'}, status=200)

@api_view(['GET']) #partha
def validate_session(request):
    if 'userName' in request.session:
        return JsonResponse({'message': 'Valid session'}, status=200)
    return JsonResponse({'error': 'Invalid session'}, status=401)
