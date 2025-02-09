#!/bin/sh

cd solutions
mkdir -p ../src/solutions
for i in practical*; do
  zip -r "../src/solutions/$i.zip" "$i"
done
