#!/bin/zsh
set -e
cd greetings-service
mvn clean install

cd greetings-bootstrap
mvn spring-boot:build-image

cd ../../greetings-stat-service
mvn clean install

cd stat-bootstrap
mvn spring-boot:build-image
