#!/usr/bin/env bash
openssl aes-256-cbc -K $encrypted_930808c487b2_key -iv $encrypted_930808c487b2_iv -in deployment/signingkey.asc.enc -out deployment/signingkey.asc -d
echo "$GPG_PASSPHRASE" | gpg --batch --yes --no-tty --passphrase-fd 0 --fast-import deployment/signingkey.asc
mvn deploy -P build-deployment -Dmaven.test.skip=true --settings deployment/settings.xml
