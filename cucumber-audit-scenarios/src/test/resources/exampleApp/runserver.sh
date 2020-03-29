#!/bin/bash
virtualenv venv
source venv/bin/activate
pip install -r requirements.txt
python manage.py runserver --noreload --nothreading &
DJANGO_SERVER_PID=$!
read -r
kill $DJANGO_SERVER_PID