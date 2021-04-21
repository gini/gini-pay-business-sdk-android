#!/usr/bin/env bash
#
# Commits the javadoc to gini-pay-business-sdk-android's gh-pages branch.
#
# Must be executed from the project root.
#
# Parameters (must be in this order):
#   1. git username
#   2. git password
#
set -e
#set -x

if [ $# -ne 2 ]; then
    echo "Pass in the git username and password"
    exit 0
fi

git_user=$1
git_password=$2

rm -rf gh-pages
git clone -b gh-pages https://"$git_user":"$git_password"@github.com/gini/gini-pay-business-sdk-android.git gh-pages

rm -rf gh-pages/kdoc
mkdir -p gh-pages/kdoc
cp -a ginipaybusiness/build/dokka/ gh-pages
mv gh-pages/ginipaybusiness/* gh-pages/kdoc
cd gh-pages
touch .nojekyll
git add -u
git add .
git diff --quiet --exit-code --cached || git commit -a -m 'Gini Pay Business SDK KDoc'
git push