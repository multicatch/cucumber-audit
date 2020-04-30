#!/usr/bin/env bash
openssl aes-256-cbc -K $encrypted_930808c487b2_key -iv $encrypted_930808c487b2_iv -in deployment/signingkey.asc.enc -out deployment/signingkey.asc -d
echo "$GPG_PASSPHRASE" | gpg --fast-import deployment/signingkey.asc
mvn deploy -P build-deployment --settings deployment/settings.xml
