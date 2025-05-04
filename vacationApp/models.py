from django.db import models

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
    email = models.EmailField()
    mobileNo = models.CharField(max_length=15)
    roleId = models.ForeignKey(Role, on_delete=models.CASCADE)
    loginTime = models.DateTimeField(null=True, blank=True)
    logoutTime = models.DateTimeField(null=True, blank=True)
    isLogin = models.BooleanField(default=False)
    isActive = models.BooleanField(default=True)
    isDeleted = models.BooleanField(default=False)

    def __str__(self):
        return self.userName

