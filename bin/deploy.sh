#!/bin/sh

JAR_FILE=$1

echo "Deploying $JAR_FILE"

mvn -e deploy:deploy-file \
  -Durl=svn:https://jstd-maven-plugin.googlecode.com/svn/repo \
  -DrepositoryId=jstd-maven-plugin-repo \
  -Dfile=$JAR_FILE \
  -DgroupId=com.google.jstestdriver \
  -DartifactId=jstestdriver \
  -Dversion=1.2.1 \
  -Dpackaging=jar
