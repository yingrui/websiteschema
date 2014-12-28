rem  mvn install:install-file
rem    -Dfile=<path-to-file>
rem    -DgroupId=<group-id>
rem    -DartifactId=<artifact-id>
rem    -Dversion=<version>
rem    -Dpackaging=<packaging>
rem    -DgeneratePom=true
rem
rem  Where: <path-to-file>  the path to the file to load
rem         <group-id>      the group that the file should be registered under
rem         <artifact-id>   the artifact name for the file
rem         <version>       the version of the file
rem         <packaging>     the packaging of the file e.g. jar

call mvn install:install-file -Dfile=../lib/corecomponents-swing-windows32-6.0b11.jar -DgroupId=com.webrenderer -DartifactId=corecomponents-swing -Dversion=6.0b11 -Dpackaging=jar -DgeneratePom=true -Dclassifier=windows32

call mvn install:install-file -Dfile=../lib/webrenderer-swing-6.0b11.jar -DgroupId=com.webrenderer -DartifactId=webrenderer-swing -Dversion=6.0b11 -Dpackaging=jar -DgeneratePom=true

call mvn install:install-file -Dfile=../lib/libwebrenderer-6.0b11-windows32.jar -DgroupId=com.webrenderer -DartifactId=libwebrenderer -Dversion=6.0b11 -Dpackaging=jar -DgeneratePom=true -Dclassifier=windows32
