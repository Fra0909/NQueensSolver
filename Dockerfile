FROM openjdk:17-jdk-alpine

# Base directory
WORKDIR /app

# Copying jar into the conatainer
COPY build/libs/NQueensSolver-0.0.1-SNAPSHOT.jar /app/NQueensSolver-0.0.1-SNAPSHOT.jar

# Exposing port used by springboot
EXPOSE 8080

# Running spring app
CMD ["java", "-jar", "NQueensSolver-0.0.1-SNAPSHOT.jar"]
