#!/usr/bin/env bash

CONTEXT_NAME=framework
FRAMEWORK_LIBRARIES_VERSION=17.3.3-SNAPSHOT

#fail script on error
set -e


function runFileStoreLiquibase() {
    echo "Running filestore Liquibase"
    java -jar target/file-service-liquibase-${FRAMEWORK_LIBRARIES_VERSION}.jar --url=jdbc:postgresql://localhost:5432/${CONTEXT_NAME}filestore --username=${CONTEXT_NAME} --password=${CONTEXT_NAME} --logLevel=info update
    echo "Finished running filestore liquibase"
}



runFileStoreLiquibase