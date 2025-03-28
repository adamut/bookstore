# 📘 Assignment Service

This is a Spring Boot application that connects to a PostgreSQL database and runs inside Docker containers using **Docker Compose**.

---

##  Prerequisites

Make sure you have the following installed:
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)
- Java 21 or later
- (Optional) [Maven](https://maven.apache.org/) (if you want to build manually)

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

### 3 **View Logs**
Check the logs of the application or database using:
```sh
docker logs assignment-app -f
```
```sh
docker logs assignment-db -f
```

---

##  API Endpoints
Once the service is running, access it at:
```
http://localhost:8080
```

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

##  Notes
- The application uses PostgreSQL 15.
- Modify `application.properties` for custom configurations.

---

