FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -g 1001 cinema && adduser -D -u 1001 -G cinema cinema

COPY build/libs/*.jar app.jar
RUN chown -R cinema:cinema /app

USER cinema
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]