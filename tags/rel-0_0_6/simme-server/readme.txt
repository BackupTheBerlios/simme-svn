SimME Server Readme
http://simme.berlios.de


This is the first release for SimME server. This release is available in two
different flavours, which will be explained in more detail below.

#################
## Binary Release

The archive consists of the changelog, release notes and the web archive (WAR).
The WAR file can be deployed on any J2EE server, though it was only tested with
Tomcat 5 yet.

In order to access the database, it is important to change the password deep
within the "password.jar" file. This file is located in

  simme.war/WEB-INF/lib

Within the "password.jar" file, you will have to find the "db.properties" file
(at/einspiel/db/db.properties) and change the value of "db.password" according
to your database configuration. This is due to changes, and database
authentication will soon be directly controlled via "web.xml".


#################
## Source Release

This release nearly contains all the files, which are currently under CVS
control. It should be quite self-explanatory. It is important to put the
following files into the $lib.dir directory (as specified in the file
"ant-build.properties").

  lib/
    password.jar
    servlet.jar

For testing you will additionally need:

  lib/
    junit.jar
    xmlunit.jar


The property client classes should point to the classes directory of the project
SimME (client). In order to deploy directly to an application server, please set
the properties $deploy.user, $deploy.pass and $deploy.url according to your
configuration. They are set in the order indicated in build.xml


########################
## Bugs and Known Issues

Please consult the bug pages at

http://developer.berlios.de/bugs/index.php?group_id=730




Kariem