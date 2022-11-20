FROM openjdk:17-oracle
WORKDIR /
COPY initializr.jar app.jar
EXPOSE 8080

CMD java -jar app.jar