FROM maven:3.8.1-openjdk-16 AS MAVEN_BUILD

COPY ./ ./

RUN mvn clean package

FROM openjdk:16-alpine

COPY --from=MAVEN_BUILD /target/wator-1.0-SNAPSHOT.jar /

CMD ["sh", "-c", "java --enable-preview -jar /wator-1.0-SNAPSHOT.jar $ARGS"]
