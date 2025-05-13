FROM ubuntu:latest
RUN apt-get update && apt-get install maven openjdk-21-jdk -y

COPY ./asteroids_local_backend /local_backend
WORKDIR /local_backend

CMD ["mvn", "spring-boot:run"]
