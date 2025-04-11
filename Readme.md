# 🧩 Apica: Java Backend Assignment

A two-service Spring Boot microservice system for user management and journaling user-related events, powered by **Kafka 4.x (Zookeeper-less)**, **MySQL** in local

---


## 📁 Project Structure

- `journal-service/` – Microservice for journaling Kafka events
- `user-service/` – Microservice for user management
- `docker-compose.yml` – Docker orchestration file
- `Makefile` – Dev utility commands
- `README.md` – Project documentation




## ✨ Overview

This project includes:

- **User Service**: Handles user registration, updates, deletions, and role management.
- **Journal Service**: Listens to Kafka events from the User Service and logs them in a journal database.

Communication between services happens via **Kafka**.

---

## 🔧 First-Time Setup

### 1. Install & Configure Kafka (Zookeeper-Free)

> **Kafka Version Used**: `4.0.0` (Zookeeper is no longer required)

- Download Kafka from the official [Apache Kafka Quickstart](https://kafka.apache.org/quickstart)  
  Direct link: [kafka_2.13-4.0.0.tgz](https://dlcdn.apache.org/kafka/4.0.0/kafka_2.13-4.0.0.tgz)

- Extract the archive and navigate into the Kafka directory:
  ```bash
  tar -xzf kafka_2.13-4.0.0.tgz
  cd kafka_2.13-4.0.0
  ```

- Generate a Kafka Cluster ID:
  ```bash
  bin/kafka-storage.sh random-uuid
  ```

- Format Kafka storage:
  ```bash
  bin/kafka-storage.sh format --standalone -t <your-cluster-id> -c config/server.properties
  ```

- Start Kafka:
  ```bash
  bin/kafka-server-start.sh config/server.properties
  ```

- Verify Kafka is Running

Once started, confirm Kafka is listening by using one of:

```bash
curl localhost:9092
or
telnet localhost 9092
```
> A hanging response means Kafka is up and ready to accept requests.
---

## ⚙️ Application Setup

### 2. MySQL Configuration

- Ensure MySQL is installed and running locally.
- Create the following databases:
  - `apicauserdb`
  - `apicajournaldb`

- Update `application.properties` or `application.yml` in both services with:
  - DB name
  - MySQL username & password

- Example MySQL credentials used in development:
   - spring.datasource.url=jdbc:mysql://localhost:3306/apicauserdb spring.datasource.username=root
   - spring.datasource.password=1111
   - Ensure both services use the correct Kafka configuration:


---

### 3. Running the Services

- Open **two terminals**:
  - Terminal 1:
    ```bash
    cd user-service
    ./mvnw spring-boot:run
    ```

  - Terminal 2:
    ```bash
    cd journal-service
    ./mvnw spring-boot:run
    ```

> Make sure Kafka is up and running before starting the services.

---

## 📤 Kafka Integration

- **User Service** produces messages to a Kafka topic: `user-events`
- **Journal Service** listens to `user-events`, logs the events, and persists them to MySQL

🧭 Kafka Event Flow

A[User Service] -- Produces --> B[(Kafka Topic: user-events)]
B -- Consumes --> C[Journal Service]
C --> D[(MySQL: apicajournaldb)]

---

## 🧪 Kafka Debugging & Log Inspection

### Common Kafka Commands

- **List Topics**
  ```bash
  bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
  ```
- **Create a Topic**
  ```bash
  bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic user-events --partitions 1 --replication-factor 1
  ```
- **Consume Messages**
  ```bash
  bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic user-events --from-beginning
  ```
- **Describe Topic**
  ```bash
  bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic user-events


- **Produce Messages**
  ```bash
  bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic user-events
  ```

> You can open one terminal to produce test messages and another to consume and verify journal logging.

---

## 🧪 Testing the System

You can test the REST APIs using **Postman**, **cURL**, or directly through the integrated **Swagger UI**.

### 🔄 Sample Flow:

1. **Register a User via User Service**  
   - Open: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)  
   - Use the `/api/users` POST endpoint to register a new user.

2. **Kafka Topic Activity**  
   - In a separate terminal, start a Kafka consumer to see real-time messages:
     ```bash
     bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic user-events --from-beginning
     ```

3. **Check Journal Logs**  
   - The **Journal Service** will consume the Kafka message and store it in MySQL.
   - Open: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)  
   - Use the `/api/journal-events` GET endpoint to retrieve stored events.

> You should see the new user action reflected in both the Kafka logs (terminal) and in the journal database (via Journal Service API).

---

## 📑 REST API Documentation

Both services have **Swagger UI** available for easy API exploration and testing.

- **User Service Swagger UI**  
  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

- **Journal Service Swagger UI**  
  [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

[Swagger - User Service](http://localhost:8080/swagger-ui/index.html)
[Swagger - Journal Service](http://localhost:8081/swagger-ui/index.html)


All endpoints for creating, updating, deleting users, and fetching journal entries are documented and testable directly from the browser.

``` md
## 🧬 Sample User Payloads

Use the following JSON for testing user creation and updates.

### Create User

```
{
  "email": "John",
  "password": "Doe",
  "age": 30,
  "role": "ADMIN"
}


## 🐳 Docker Support (Optional)

While the current setup runs locally, you can extend the project using `docker-compose.yml` to containerize:
- Kafka (with Kraft mode)
- MySQL
- Both Spring Boot services

📦 .env File Support (Optional)

You can create a `.env` file at the project root to manage MySQL credentials or Kafka configs for local dev:

```env
MYSQL_USER=root
MYSQL_PASSWORD=1111
KAFKA_BROKER=kafka:9092
```

---

## 💻 Local Development (WSL + Docker)

This project has been tested in **WSL (Windows Subsystem for Linux)** using **Ubuntu + Docker + VS Code Remote WSL**. Here's how to run it smoothly.

### ✅ Initial Setup

Inside your WSL Ubuntu terminal:

```bash
sudo dockerd &         # Start Docker daemon
code .                 # Open the project in VS Code WSL
```

> Ensure Docker Desktop is installed on Windows and WSL integration is enabled.

---

### 🔁 Daily Dev Loop

```bash
make restart           # Rebuild JAR, Docker image, and restart all services
```

---

### 🧼 Cleanup & Lifecycle

```bash
make clean             # Clean Docker images, volumes, and target directories
make down              # Stop all services
make up                # Start services (only if already built)
```

You can also see logs for any container:

```bash
docker logs -f user-service
docker logs -f kafka
```

---

### 🧪 Kafka Terminal Access via Docker

To open a **Kafka producer** terminal in Docker:

```bash
docker run -it --rm \
  --network=apica_default \
  confluentinc/cp-kafka:7.5.0 \
  kafka-console-producer \
  --broker-list kafka:9092 \
  --topic user-events
```

To open a **Kafka consumer** terminal in Docker:

```bash
docker run -it --rm \
  --network=apica_default \
  confluentinc/cp-kafka:7.5.0 \
  kafka-console-consumer \
  --bootstrap-server kafka:9092 \
  --topic user-events \
  --from-beginning
```

---

### ⚙️ Makefile Commands

Your `Makefile` includes helpful dev tasks:

```makefile
.PHONY: build-image up down restart clean nuke

build-image:
	docker-compose build --no-cache user-service

up:
	docker-compose up -d

down:
	docker-compose down

restart: down build-image up

clean:
	docker-compose down --volumes --remove-orphans
	docker system prune -a -f
	docker volume prune -f
	docker network prune -f

# ⚠️ WARNING: This will stop and remove ALL containers, images, volumes, networks
nuke:
	docker stop $(docker ps -aq) || true
	docker rm -f $(docker ps -aq) || true
	docker rmi -f $(docker images -aq) || true
	docker volume prune -f
	docker network prune -f
```

---

### 🧩 Notes on WSL

> If you're using **WSL2**, make sure to:
- Enable Docker integration with WSL (via Docker Desktop settings)
- Start `dockerd` inside WSL before running `make`
- Avoid port conflicts with other local services
- Run `code .` from within WSL terminal to open VS Code properly in WSL context

If Docker commands don’t behave as expected, consider restarting Docker Desktop or WSL (`wsl --shutdown`) and try again.

---



## 🚀 Submission

- Push the complete source code to a public/private Git repository.
- Include:
  - This `README.md`
  - Source code of both microservices
  - SQL setup or initialization scripts (if needed)
  - REST API docs

---

## 🧠 Design Highlights

- **Microservice Communication**: Decoupled using Kafka for better scalability and asynchronous processing.
- **Event-Driven Design**: Every user operation triggers an event, allowing for flexible downstream consumers.
- **Clean Separation of Concerns**: User management and journaling responsibilities are handled in isolated services.
- **Easy Dev Loop**: `Makefile` simplifies build-run-restart workflows locally and with Docker.


## 📌 Assumptions

- Kafka is run without Zookeeper (using KRaft mode from version 4.x)
- Kafka and MySQL are set up manually
- Microservices are started via `mvn spring-boot:run`
- Databases are pre-created
