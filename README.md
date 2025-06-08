# Master Microservices With Spring

This project is a microservices-based system built with Spring Boot. It demonstrates best practices for designing, developing, and deploying
microservices in Java. The system includes several independent services, each responsible for a specific business domain, and uses Docker
for containerization and orchestration.

## Project Structure

- **accounts/**: Account management microservice (Spring Boot)
- **app-manager/**: Central configuration and management service (Spring Boot, acts as Config Server)
- **cards/**: Card management microservice (Spring Boot)
- **loans/**: Loan management microservice (Spring Boot)
- **docker/**: Docker Compose files for local development and database setup

## Main Technologies

- Java 17+
- Spring Boot
- Spring Cloud Config
- Docker & Docker Compose
- Kafka (for event streaming)
- PostgreSQL (optional, for persistence)

## How to Run

1. **Build all services:**
    - For `app-manager` (uses a custom Dockerfile with OpenJDK and curl):
      ```sh
      docker build -t app-manager:0.0.1 ./app-manager
      ```
    - For other services (`accounts`, `cards`, `loans`), use Spring Boot's build image feature (no Dockerfile needed):
      ```sh
      mvn spring-boot:build-image -DskipTests
      ```
      Run this command in each service directory (`accounts/`, `cards/`, `loans/`).

2. **Start the system with Docker Compose:**
   ```sh
   docker-compose -f docker/h2/docker-compose.yaml up --build
   ```
   This will start all services, Kafka, and an H2 database.

3. **Access Services:**
    - Accounts: http://localhost:8080
    - App Manager (Config Server): http://localhost:8071
    - Loans: http://localhost:8090
    - Cards: http://localhost:9000

## Configuration

- Each service uses Spring Cloud Config to load its configuration from the App Manager.
- Environment variables can be set in the `docker-compose.yaml` files.

## Health Checks

- Each service exposes a Spring Boot Actuator health endpoint.
- Docker Compose healthchecks are configured for service readiness.

## Development

- Source code is organized under `src/main/java` and `src/main/resources` in each service.
- Tests are under `src/test/java` and `src/test/resources`.

## Useful Commands

- **Run tests:**
  ```sh
  mvn test
  ```

## License

This project is for educational purposes.
