#!/bin/bash
virtualenv venv
source venv/bin/activate
pip3 install -r requirements.txt
python3 manage.py runserver --noreload --nothreading &
DJANGO_SERVER_PID=$!
read -r
kill $DJANGO_SERVER_PID