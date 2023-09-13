#!/bin/bash
set -ex
# install chrome
wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
sudo apt install ./google-chrome-stable_current_amd64.deb

# install drivers
mkdir webdrivers
cd webdrivers

# geckodriver
wget "https://github.com/mozilla/geckodriver/releases/download/v0.33.0/geckodriver-v0.33.0-linux64.tar.gz"
tar xzvf geckodriver-*.tar.gz

# chromedriver
google-chrome --version
MAJOR_CHROME_VERSION=$(google-chrome --version | grep -o "[0-9].*" | head -c2)
CHROME_VERSION_LINK="https://chromedriver.storage.googleapis.com/LATEST_RELEASE_$MAJOR_CHROME_VERSION"
CHROME_VERSION=$(curl $CHROME_VERSION_LINK)
wget -q "https://chromedriver.storage.googleapis.com/$CHROME_VERSION/chromedriver_linux64.zip"
unzip chromedriver_linux64.zip
cd ..