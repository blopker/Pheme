#!/usr/bin/env bash

REPO=~/code/blopker.github.com
SOURCE=target/mvn-repo

function test {
  "$@"
   status=$?
   if [ $status -ne 0 ]; then
     echo "error with $@"
     exit
   fi
   return $status
}

test mvn clean
test mvn deploy

if [ ! -d $REPO ]; then
  git clone git@github.com:blopker/blopker.github.com.git $REPO
fi

cp -rv $SOURCE/* $REPO/maven-repo

cd $REPO

git add --all :/
git commit -am "Updated releases"
git push origin master
