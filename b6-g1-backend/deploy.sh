#! /bin/bash

# Exit immediately if a simple command exits with a nonzero exit value
set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
HOST_IP=18.141.229.145
PEM_FILE=rookies6_java1.pem

#export JAVA_HOME=/c/Users/chungbuiv/Java/jdk-15.0.2

./mvnw clean install

scp -i $PEM_FILE -r target/asset-assignment-0.0.1-SNAPSHOT.jar ubuntu@$HOST_IP:/home/ubuntu/assetmanagement-be/asset-assignment.jar

ssh -i $PEM_FILE ubuntu@$HOST_IP '/home/ubuntu/assetmanagement-be/restart.sh'
