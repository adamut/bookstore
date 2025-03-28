# 📘 Assignment Service

This is a Spring Boot application that connects to a PostgreSQL database and runs inside Docker containers using **Docker Compose**.

---

##  Prerequisites

Make sure you have the following installed:
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)
- Java 21 or later
- [Maven](https://maven.apache.org/) (if you want to build manually)

---

##  How to Run the Service

### **1 Clone the Repository**
```sh
git clone https://github.com/adamut/bookstore.git 
cd assignment
```

### **2 Build the Project (Optional if using Docker)**
If you want to build the JAR manually:
```sh
mvn clean package
```
This will generate a JAR file inside `assignment/target/`.

### **3 Start the Service with Docker Compose**
```sh
docker compose up --build
```
This will:
- Build the Spring Boot application.
- Start the application and PostgreSQL database in separate containers.

### **4 Verify the Service**
Check if the containers are running:
```sh
docker ps
```
You should see two running containers:
- `assignment-app` (Spring Boot application)
- `assignment-db` (PostgreSQL database)

Check application logs:
```sh
docker logs assignment-app -f
```

---

##  Environment Variables
The application reads database settings from environment variables, which are defined in `docker-compose.yml`:

| Variable Name                 | Description                    |
|--------------------------------|--------------------------------|
| `SPRING_DATASOURCE_URL`       | Database connection URL       |
| `SPRING_DATASOURCE_USERNAME`  | Database username             |
| `SPRING_DATASOURCE_PASSWORD`  | Database password             |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Hibernate DDL mode (update) |

You can also define these variables manually before running the app.

---

##  Stopping the Service
To stop the running containers:
```sh
docker compose down
```

---

##  Troubleshooting
### 1 **Database Connection Error**
**Error:** `Unable to open JDBC Connection`

**Solution:** Ensure that `SPRING_DATASOURCE_URL` in `docker-compose.yml` is set to `jdbc:postgresql://db:5432/bookstore_db` instead of `localhost`.

### 2 **Port Already in Use**
**Error:** `Bind for 0.0.0.0:8080 failed: port is already allocated`

**Solution:** Stop any existing services using the port:
```sh
sudo lsof -i :8080  # Find the process
sudo kill -9 <PID>  # Kill the process
```
Then restart the service.


---

##  API Endpoints

To test the API, use:
```sh
curl http://localhost:8080/health
```
(or use Postman/Swagger UI if available)

---

## Testing APIs with Postman
A Postman collection has been provided to test the APIs.

### 1. Import the Collection
- Open Postman.
- Go to `File > Import`.
- Select the `Bookstore.postman_collection.json` file.

### 2. Test Endpoints
Use the imported collection to send requests to the service. Some available endpoints include:
- `GET localhost:8080/bookstore/customer/loyalty/{id}` - Get customer loyalty points.
- `GET localhost:8080/bookstore/book/` - Get all books.
- `POST localhost:8080/bookstore/purchase` - Purchase books.

Ensure the service is running before testing.

---

## Key Architectural Decisions
### 1. Relational Database (RDBMS) over NoSQL
We chose PostgreSQL instead of a NoSQL database like MongoDB.

- Reasons:

  - The application involves structured data with relationships (Orders, Customers, Books, etc.).
  - ACID compliance ensures data consistency and integrity.
  - Easier to manage transactions and complex queries.

### 2 Dependency Injection for Decoupling
- Used Spring’s Dependency Injection (DI) to keep components loosely coupled.

  - This makes the application: 
    - Easier to test (mocking dependencies in unit tests).
    - More maintainable (business logic is separate from implementations).

### 3 Vavr for Error Handling & Code Readability
- We integrated Vavr to enhance error handling and improve functional programming aspects:
    - Functional Style: More expressive and less boilerplate.
    - Try Monad: Instead of using traditional try-catch, we handle failures explicitly.
    - Improved Readability: Avoids null checks, making the code more declarative.

--- 

##  What We Focused On

-  Database Reliability – PostgreSQL for strong consistency and ACID compliance.
-  Maintainability – Dependency Injection for modular components.
-  Functional Programming – Vavr for better error handling and expressive code.
-  Environment Configuration – Used environment variables for flexibility.

---

## What We Didn't Focus On
-  Containerized Logging – No centralized logging setup yet (e.g., ELK stack).
-  Production-Grade Security – Credentials are stored in plain text for simplicity.
-  Caching some of the repositories such as customer in order to improve performance
-  No integration or functional tests were added to the project. In addition, not all classes were unit tested.
-  Distributed system architecture for deleting books using a proper scheduler. Current solution focuses on deleting books per instance.
-  Event-Driven Architecture – No event publishing when book stock is empty (e.g., sending alerts for restocking).
- Service Monitoring – We haven't integrated metrics collection yet, but tools like Micrometer could be added in the future for tracking application performance and health.