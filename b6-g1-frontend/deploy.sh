#! /bin/bash

# Exit immediately if a simple command exits with a nonzero exit value
set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
HOST_IP=18.141.229.145
PEM_FILE=rookies6_java1.pem

sed -i "s|http://localhost:8080|http://$HOST_IP|" src/services/httpCommon.js

npm install && npm run build

scp -i $PEM_FILE -r build/* ubuntu@$HOST_IP:/home/ubuntu/assetmanagement-fe

ssh -i $PEM_FILE ubuntu@$HOST_IP 'sudo cp -r /home/ubuntu/assetmanagement-fe/* /var/www/html; sudo systemctl restart nginx'
