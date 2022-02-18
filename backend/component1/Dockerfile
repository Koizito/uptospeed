FROM maven:3.8.1-openjdk-11

COPY ./ ./

EXPOSE 8080

CMD ["./mvnw", "compile", "quarkus:dev"]

#docker build --no-cache --progress=plain -t backend/component1:latest .