#!/usr/bin/env bash

REPO=~/code/blopker.github.com/maven-repo/
SOURCE=~/play/repository/local/pheme

function test {
  "$@"
   status=$?
   if [ $status -ne 0 ]; then
     echo "error with $@"
     exit
   fi
   return $status
}

test play clean

if [ ! -d $REPO ]; then
  test git clone git@github.com:blopker/blopker.github.com.git $REPO
fi

test play publish
#test cp -rv $SOURCE $REPO

test cd $REPO

test git pull
test git add --all :/
test git commit -am "Updated releases"
test git push origin master
