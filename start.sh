#!/bin/sh

cd gateway-antifraud-mock
nodemon index.js &

cd ../database
docker-compose down
docker-compose up