from django.urls import path, include

from app.authentication import views

urlpatterns = [
    path('accounts/', include('django.contrib.auth.urls')),
    path('oauth2/', include('oauth2_provider.urls', namespace='oauth2_provider')),
    path('oauth2/user/', views.get_user)
]
