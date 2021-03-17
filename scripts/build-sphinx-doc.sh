#!/usr/bin/env bash
#
# Builds ginipaybusiness's sphinx documentation.
#
# Must be executed from the project root.
#
set -e
#set -x

cd ginipaybusiness/src/doc
virtualenv ./virtualenv
source virtualenv/bin/activate
pip install -r requirements.txt

make clean
make html singlehtml