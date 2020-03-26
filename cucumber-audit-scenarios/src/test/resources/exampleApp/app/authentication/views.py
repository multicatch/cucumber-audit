from django.http import JsonResponse
from oauth2_provider.decorators import protected_resource


@protected_resource()
def get_user(request):
    return JsonResponse({
        'username': request.user.username
    })
