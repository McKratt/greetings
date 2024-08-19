# Build the Docker image

## Locally with docker cli

```shell
cd stat-boostrap
docker build -t bakaar/greetings-stat-composables:2.0.0-sb3 -f src/docker/Dockerfile .
```

## Locally with Maven

```shell
cd stat-bootstrap
mvn -PbuildImage -DskipUTs -DskipITs package
```