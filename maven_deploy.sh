#!/usr/bin/env bash
echo "$GPG_SIGNINGKEY" > deployment/signingkey.asc
echo "$GPG_PASSPHRASE" | gpg --batch --yes --no-tty --passphrase-fd 0 --fast-import deployment/signingkey.asc
mvn deploy -P build-deployment -Dmaven.test.skip=true --settings deployment/settings.xml
