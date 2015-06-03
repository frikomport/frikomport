#!/bin/bash
docker run --rm -v ${PWD}:/code --volumes-from frikomport_tomcat_1 --env CATALINA_HOME=/usr/local/tomcat --link frikomport_oracle_1:oracle frikomport/ant "$@"

