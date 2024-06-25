# Build Stage
FROM gradle as builder
WORKDIR /src
COPY --chown=gradle:gradle . /src
COPY . .
RUN gradle build --no-daemon -x test

# Final Stage
FROM openjdk:11-jre
EXPOSE 80 443
WORKDIR /app

# Copy application and static resources from builder image
COPY --from=builder /src/build/libs/app.jar /var/run/app.jar

# COPY REQUIRED DOMAIN KEYS
COPY ./src/main/resources/ /app/resources/

ENTRYPOINT ["java", "-jar", "/var/run/app.jar"]
