mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=JsTestDriver-1.3.3d.xml -Dfile=JsTestDriver-1.3.3d.jar
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=JsTestDriver-1.3.3d.xml -Dfile=JsTestDriver-1.3.3d-src.jar -Dclassifier=sources
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=JsTestDriver-1.3.3d.xml -Dfile=JsTestDriver-1.3.3d-javadoc.jar -Dclassifier=javadoc

mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=coverage-1.3.3d.xml -Dfile=coverage-1.3.3d.jar
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=coverage-1.3.3d.xml -Dfile=coverage-1.3.3d-src.jar -Dclassifier=sources
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=coverage-1.3.3d.xml -Dfile=coverage-1.3.3d-javadoc.jar -Dclassifier=javadoc


#javadoc -d docs -sourcepath JsTestDriver-1.3.3d-src/ -subpackages com

