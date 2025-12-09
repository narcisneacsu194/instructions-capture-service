# Instructions Capture Service

A Spring Boot microservice that processes trade instructions from:

- REST API (JSON + CSV upload)
- Kafka inbound topic (`instructions.inbound`)

The service:

1. Normalizes incoming trade messages  
2. Masks sensitive data  
3. Transforms canonical model → platform-specific JSON  
4. Stores trades temporarily in memory  
5. Publishes transformed messages to `instructions.outbound`  
6. Is fully containerized with Kafka + Zookeeper  
7. Exposes Swagger/OpenAPI documentation  

---

# 🚀 Tech Stack

- **Java 17 (OpenJDK 17)**
- **Spring Boot 3**
- **Spring Web**
- **Spring Kafka**
- **OpenCSV**
- **Swagger / Springdoc OpenAPI**
- **Docker Compose**
- **Maven Wrapper (mvnw / mvnw.cmd)**

---

# 📦 Project Structure

```
instructions-capture-service/
├── docker-compose.yml
├── Dockerfile
├── pom.xml
├── mvnw
├── mvnw.cmd
├── sample-trade.json
├── sample-trades.csv
├── postman-collection.json
├── README.md
│
├── src/
│   ├── main/java/com/example/instructions/
│   │   ├── InstructionsCaptureApplication.java
│   │   ├── controller/TradeController.java
│   │   ├── model/CanonicalTrade.java
│   │   ├── model/PlatformTrade.java
│   │   ├── service/TradeService.java
│   │   ├── service/KafkaPublisher.java
│   │   ├── service/KafkaListenerService.java
│   │   ├── util/TradeTransformer.java
│   │   └── config/OpenApiConfig.java
│   │
│   └── resources/application.yml
│
└── src/test/java/com/example/instructions/
    ├── InstructionsCaptureApplicationTest.java
    ├── service/TradeServiceTest.java
    └── util/TradeTransformerTest.java
```

---

# 🐳 Running the Application with Docker Compose

Run everything (Kafka + Zookeeper + App):

```bash
docker compose up --build
```

Kafka automatically creates topics:
- `instructions.inbound`
- `instructions.outbound`

---

# 🌐 Access Swagger UI

http://localhost:8080/swagger-ui/index.html

OpenAPI JSON:
http://localhost:8080/v3/api-docs

---

# 🧪 Testing the Application

## 1️⃣ Test JSON Endpoint

```bash
curl -X POST http://localhost:8080/trade/json   -H "Content-Type: application/json"   -d @sample-trade.json
```

Expected response:

```
Processed JSON trade
```

---

## 2️⃣ Test CSV Upload

```bash
curl -X POST http://localhost:8080/trade/csv   -F "file=@sample-trades.csv"
```

Each CSV row becomes a Kafka message.

---

## 3️⃣ Verify Kafka Output

Start Kafka consumer:

```bash
docker compose exec kafka kafka-console-consumer   --bootstrap-server kafka:9092   --topic instructions.outbound   --from-beginning
```

You should see transformed JSON:

```json
{
  "platformId": "ACCT123",
  "trade": {
    "account": "*****6789",
    "security": "ABC123",
    "type": "B",
    "amount": 100000.0,
    "timestamp": "2025-08-04T21:15:33Z"
  }
}
```

---

# ▶️ Running Locally Without Docker

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

---

# 🧪 Unit Tests

Linux/macOS:

```bash
./mvnw test
```

Windows:

```bash
mvnw.cmd test
```

---

# 🐳 Build Docker Image Manually

```bash
./mvnw clean package -DskipTests
docker build -t instructions-capture-service .
docker run -p 8080:8080 instructions-capture-service
```

---

# 📬 Postman Collection

File included:

```
postman-collection.json
```

---

# 🎯 Summary

This project provides:

- Full REST + Kafka pipeline  
- Automatic topic creation  
- Swagger documentation  
- Example JSON + CSV files  
- Unit tests  
- Maven wrapper  
- One-command startup:

```bash
docker compose up --build
```

Ready for production-like demonstration or interview review.
