#!/bin/bash

#  mvn install:install-file
#    -Dfile=<path-to-file>
#    -DgroupId=<group-id>
#    -DartifactId=<artifact-id>
#    -Dversion=<version>
#    -Dpackaging=<packaging>
#    -DgeneratePom=true
#
#  Where: <path-to-file>  the path to the file to load
#         <group-id>      the group that the file should be registered under
#         <artifact-id>   the artifact name for the file
#         <version>       the version of the file
#         <packaging>     the packaging of the file e.g. jar

mvn install:install-file -Dfile=../lib/corecomponents-swing-linux64.jar -DgroupId=com.webrenderer -DartifactId=corecomponents-swing -Dversion=6.0b3 -Dpackaging=jar -DgeneratePom=true -Dclassifier=linux64

mvn install:install-file -Dfile=../lib/webrenderer-swing.jar -DgroupId=com.webrenderer -DartifactId=webrenderer-swing -Dversion=6.0b3 -Dpackaging=jar -DgeneratePom=true

mvn install:install-file -Dfile=../lib/webrenderer-swing-linux64.jar -DgroupId=com.webrenderer -DartifactId=libwebrenderer -Dversion=6.0b3 -Dpackaging=jar -DgeneratePom=true -Dclassifier=linux64

mvn install:install-file -Dfile=../lib/weka-3.6.2.jar -DgroupId=weka -DartifactId=weka -Dversion=3.6.2 -Dpackaging=jar -DgeneratePom=true

#mvn install:install-file -Dfile=../lib/weka-src.jar -DgroupId=weka -DartifactId=weka -Dversion=3.6.6 -Dpackaging=jar -DgeneratePom=true -Dclassifier=sources
