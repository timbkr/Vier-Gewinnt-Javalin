<<<<<<< HEAD
<<<<<<< HEAD
# Mit GRADLE im Container (JAR wird im Container gebaut, Container braucht sehr lange zum bauen)

#FROM openjdk:12-jdk-alpine
#
#WORKDIR /app
#
## gradlew & alles ins Image kopieren
#COPY . .
#
## Add execute permissions to the gradlew script
#RUN chmod +x ./gradlew
#
## Tools für gradlew installieren
#RUN apk add --no-cache bash curl unzip
#
## Build mit gradlew (ohne Daemon, damit's im Container sauber läuft)
#RUN ./gradlew build --no-daemon
#
## jetzt das Jar ins Laufzeitimage übernehmen
#FROM openjdk:12-jdk-alpine
#WORKDIR /app
#COPY --from=0 /app/build/libs/*.jar app.jar
#
#EXPOSE 8080
#CMD ["java", "-jar", "app.jar"]

#------------------------------------------------------------------------
# OHNE GRADLE im Container (JAR muss vorher gebaut werden (lokal oder CI/CD) bzw. im REPO sein

FROM openjdk:12-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

=======
## 1. Basis-Image mit JDK
=======
# Mit GRADLE im Container (JAR wird im Container gebaut, Container braucht sehr lange zum bauen)

>>>>>>> 82792b6 (feat(docker): optimize container build)
#FROM openjdk:12-jdk-alpine
#
#WORKDIR /app
#
## gradlew & alles ins Image kopieren
#COPY . .
#
## Add execute permissions to the gradlew script
#RUN chmod +x ./gradlew
#
## Tools für gradlew installieren
#RUN apk add --no-cache bash curl unzip
#
## Build mit gradlew (ohne Daemon, damit's im Container sauber läuft)
#RUN ./gradlew build --no-daemon
#
## jetzt das Jar ins Laufzeitimage übernehmen
#FROM openjdk:12-jdk-alpine
#WORKDIR /app
#COPY --from=0 /app/build/libs/*.jar app.jar
#
#EXPOSE 8080
#CMD ["java", "-jar", "app.jar"]

#------------------------------------------------------------------------
# OHNE GRADLE im Container (JAR muss vorher gebaut werden (lokal oder CI/CD) bzw. im REPO sein

FROM openjdk:12-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
<<<<<<< HEAD
>>>>>>> 0fb7bc9 (Add dockerfile)
=======

>>>>>>> 82792b6 (feat(docker): optimize container build)
