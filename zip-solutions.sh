#!/bin/sh

set -e

cd solutions
rm -rf ../src/solutions
mkdir -p ../src/solutions
for i in practical*; do
  zip -r "../src/solutions/$i.zip" "$i"
done
