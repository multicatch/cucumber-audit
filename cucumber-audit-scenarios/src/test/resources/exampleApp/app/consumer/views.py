import random

from django.shortcuts import render

from app.consumer import settings
from app.consumer.backend import fetch_token, AuthorizationError, fetch_user, renew_token


def index(request):
    error = request.GET.get('error')
    code = request.GET.get('code')
    state = request.GET.get('state')
    actual_state = request.session.get('state', -1)

    try:
        token = fetch_token(code, state, actual_state)
        if token is not None:
            request.session['token'] = token
    except AuthorizationError as err:
        error = str(err)

    token = request.session.get('token', None)
    logged_in = token is not None
    if not logged_in:
        request.session['state'] = random.randint(1000, 10000000)

    user = None
    try:
        user = fetch_user(token)
    except AuthorizationError:
        token = renew_token(token)
        request.session['token'] = token
        user = fetch_user(token)

    return render(request, 'resource/result.html', {
        'logged_in': logged_in,
        'error': error,
        'user': user,
        'authorization_link':
            '%s/authorize/?response_type=code&state=%d&client_id=%s' %
            (settings.OAUTH2_SERVER_URL,
             request.session.get('state', None),
             settings.OAUTH2_CLIENT_ID),
    })
