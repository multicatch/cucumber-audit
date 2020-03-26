import requests

from app.consumer import settings


class AuthorizationError(BaseException):
    """raise when there is a problem with authentication"""


def fetch_token(code, state, actual_state):
    if code is not None:
        if str(state) == str(actual_state):
            session = requests.Session()
            session.auth = (settings.OAUTH2_CLIENT_ID, settings.OAUTH2_CLIENT_SECRET)
            response = session.post('%s/token/' % settings.OAUTH2_SERVER_URL,
                                    {
                                        'code': code,
                                        'redirect_uri': settings.REDIRECT_URL,
                                        'grant_type': 'authorization_code'
                                    })
            if response.status_code == 200:
                return response.json()
            else:
                raise AuthorizationError('invalid_request')
        else:
            raise AuthorizationError('invalid_state')


def fetch_user(token):
    if token is not None:
        access_token = token['access_token']
        headers = {'Authorization': 'Bearer %s' % access_token}
        response = requests.get('%s/user/' % settings.OAUTH2_SERVER_URL, headers=headers)
        if response.status_code == 200:
            return response.json()
        else:
            raise AuthorizationError('token_expired')


def renew_token(token):
    session = requests.Session()
    session.auth = (settings.OAUTH2_CLIENT_ID, settings.OAUTH2_CLIENT_SECRET)
    response = session.post('%s/token/' % settings.OAUTH2_SERVER_URL,
                            {
                                'refresh_token': token['refresh_token'],
                                'grant_type': 'refresh_token'
                            })
    if response.status_code == 200:
        return response.json()
    else:
        raise AuthorizationError('invalid_request')
