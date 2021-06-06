#!/bin/zsh
set -e
cd greetings-service/greetings-bootstrap
mvn spring-boot:build-image

cd ../../greetings-stat-service/stat-bootstrap
mvn spring-boot:build-image
