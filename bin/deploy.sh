#!/bin/sh

VERSION=$1
JAR_FILE=jstestdriver-$VERSION.jar

echo "Deploying $JAR_FILE"

mvn -e deploy:deploy-file \
  -Durl=svn:https://jstd-maven-plugin.googlecode.com/svn/repo \
  -DrepositoryId=jstd-maven-plugin-repo \
  -Dfile=$JAR_FILE \
  -DgroupId=com.google.jstestdriver \
  -DartifactId=jstestdriver \
  -Dversion=$VERSION \
  -Dpackaging=jar
