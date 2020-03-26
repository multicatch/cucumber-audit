from django.urls import path

from app.consumer import views

urlpatterns = [
    path('', views.index),
]
