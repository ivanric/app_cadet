#imagen modelo
FROM eclipse-temurin:11.0.24_8-jdk
#argumentos
ARG JAR_FILE=target/CADET.jar
#copia del jar
COPY ${JAR_FILE} CADET.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","CADET.jar"]