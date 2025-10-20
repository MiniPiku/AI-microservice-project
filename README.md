# Fitness Microservice Application

A comprehensive fitness tracking application built with Spring Boot microservices architecture, featuring user management, activity tracking, AI-powered recommendations, and real-time event processing.

## 🏗️ Architecture Overview

This application follows a microservices architecture pattern with the following components:

- **Eureka Server** - Service discovery and registration
- **Config Server** - Centralized configuration management
- **API Gateway** - Single entry point for all client requests
- **User Service** - User management and authentication
- **Activity Service** - Fitness activity tracking
- **AI Service** - AI-powered fitness recommendations

## 🚀 Services

### 1. Eureka Server (Port: 8761)
- **Purpose**: Service discovery and registration center
- **Technology**: Spring Cloud Netflix Eureka
- **Features**: 
  - Service registry for all microservices
  - Health monitoring
  - Load balancing support

### 2. Config Server (Port: 8084)
- **Purpose**: Centralized configuration management
- **Technology**: Spring Cloud Config Server
- **Features**:
  - Externalized configuration for all services
  - Native file system configuration storage
  - Environment-specific configurations

### 3. API Gateway (Port: 8085)
- **Purpose**: Single entry point for all client requests
- **Technology**: Spring Cloud Gateway
- **Features**:
  - Route management for all microservices
  - Load balancing with service discovery
  - Request routing based on path patterns

### 4. User Service (Port: 8081)
- **Purpose**: User management and authentication
- **Technology**: Spring Boot, Spring Data JPA, PostgreSQL
- **Features**:
  - User registration and profile management
  - User validation across services
  - Role-based access control (USER, ADMIN)
- **Database**: PostgreSQL
- **Endpoints**:
  - `POST /api/user/register` - User registration
  - `GET /api/user/{userId}` - Get user profile
  - `GET /api/user/{userId}/validate` - Validate user existence

### 5. Activity Service (Port: 8082)
- **Purpose**: Fitness activity tracking and management
- **Technology**: Spring Boot, Spring Data MongoDB, Kafka
- **Features**:
  - Activity tracking (Running, Walking, Swimming)
  - Real-time event publishing to Kafka
  - Activity metrics and analytics
- **Database**: MongoDB
- **Message Broker**: Apache Kafka
- **Endpoints**:
  - `POST /api/activity` - Track new activity

### 6. AI Service (Port: 8083)
- **Purpose**: AI-powered fitness recommendations
- **Technology**: Spring Boot, Spring Data MongoDB, Kafka, Gemini AI
- **Features**:
  - AI-generated fitness recommendations
  - Activity analysis and suggestions
  - Safety recommendations
  - Real-time event processing from Kafka
- **Database**: MongoDB
- **AI Integration**: Google Gemini API
- **Endpoints**:
  - `POST /api/recommendations` - Create recommendation
  - `GET /api/recommendations` - Get all recommendations
  - `GET /api/recommendations/user/{userId}` - Get user-specific recommendations
  - `GET /api/recommendations/activity/{activityId}` - Get activity-specific recommendations

## 🛠️ Technology Stack

### Backend Technologies
- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Cloud 2025.0.0**
- **Spring Data JPA** (User Service)
- **Spring Data MongoDB** (Activity & AI Services)
- **Spring Cloud Gateway** (API Gateway)
- **Spring Cloud Config** (Configuration Management)
- **Netflix Eureka** (Service Discovery)

### Databases
- **PostgreSQL** - User Service data storage
- **MongoDB** - Activity and AI Service data storage

### Message Broker
- **Apache Kafka** - Event-driven communication between services

### AI Integration
- **Google Gemini API** - AI-powered recommendations

### Development Tools
- **Lombok** - Code generation and boilerplate reduction
- **Maven** - Build and dependency management

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+**
- **PostgreSQL** (for User Service)
- **MongoDB** (for Activity and AI Services)
- **Apache Kafka** (for event streaming)
- **Docker** (optional, for PostgreSQL container)

## 🚀 Getting Started

### 1. Database Setup

#### PostgreSQL Setup (User Service)
```bash
# Using Docker (recommended)
cd userservice
docker-compose up -d

# Or install PostgreSQL locally and create database:
# Database: mydb
# Username: admin
# Password: 1234
# Port: 5332
```

#### MongoDB Setup
```bash
# Install MongoDB locally
# Default connection: mongodb://localhost:27017/ai-microservice-project
```

### 2. Apache Kafka Setup
```bash
# Download and start Kafka
# Default connection: localhost:9092
```

### 3. Environment Variables
Set the following environment variable for AI Service:
```bash
export Gemini-API-Key=your_gemini_api_key_here
```

### 4. Service Startup Order

Start the services in the following order:

1. **Eureka Server**
```bash
cd eureka
mvn spring-boot:run
```

2. **Config Server**
```bash
cd configserver
mvn spring-boot:run -Dspring.cloud.client.hostname=localhost
```
> **Note**: The `-Dspring.cloud.client.hostname=localhost` VM option is required on Windows to resolve DNS lookup issues with localhost.

3. **User Service**
```bash
cd userservice
mvn spring-boot:run -Duser.timezone=Asia/Kolkata
```
> **Note**: The `-Duser.timezone=Asia/Kolkata` VM option is required to fix timezone issues with created timestamps.

4. **Activity Service**
```bash
cd activityservice
mvn spring-boot:run
```

5. **AI Service**
```bash
cd aiservice
mvn spring-boot:run
```

6. **API Gateway**
```bash
cd gateway
mvn spring-boot:run
```

### 5. IDE Configuration (Alternative to Command Line)

If you're running the services from your IDE (IntelliJ IDEA, Eclipse, etc.), you'll need to add VM options in the run configuration:

#### User Service VM Options:
```
-Duser.timezone=Asia/Kolkata
```

#### Config Server VM Options:
```
-Dspring.cloud.client.hostname=localhost
```

**How to add VM options in IntelliJ IDEA:**
1. Go to `Run` → `Edit Configurations`
2. Select your service configuration
3. In the `VM options` field, add the required VM options
4. Apply and save the configuration

## 📡 API Endpoints

### User Service (via Gateway: http://localhost:8085)
- `POST /api/user/register` - Register new user
- `GET /api/user/{userId}` - Get user profile
- `GET /api/user/{userId}/validate` - Validate user

### Activity Service (via Gateway: http://localhost:8085)
- `POST /api/activity` - Track new activity

### AI Service (via Gateway: http://localhost:8085)
- `POST /api/recommendations` - Create recommendation
- `GET /api/recommendations` - Get all recommendations
- `GET /api/recommendations/user/{userId}` - Get user recommendations
- `GET /api/recommendations/activity/{activityId}` - Get activity recommendations

## 🔄 Data Flow

1. **User Registration**: Users register through the User Service
2. **Activity Tracking**: Users track activities through the Activity Service
3. **Event Publishing**: Activity Service publishes events to Kafka
4. **AI Processing**: AI Service consumes events and generates recommendations
5. **Recommendation Storage**: AI Service stores recommendations in MongoDB
6. **Data Retrieval**: Users can retrieve recommendations through the AI Service

## 🏗️ Project Structure

```
fitness-microservice/
├── eureka/                 # Service Discovery Server
├── configserver/          # Configuration Management Server
├── gateway/               # API Gateway
├── userservice/           # User Management Service
├── activityservice/       # Activity Tracking Service
└── aiservice/            # AI Recommendation Service
```

## 🔧 Configuration

Each service has its own configuration managed by the Config Server:

- **Eureka**: `application.yml` - Service discovery configuration
- **Config Server**: `application.yml` + `/config/*.yml` - Centralized configurations
- **Gateway**: `gateway-service.yml` - Routing and load balancing
- **User Service**: `user-service.yml` - Database and service configuration
- **Activity Service**: `activity-service.yml` - MongoDB and Kafka configuration
- **AI Service**: `ai-service.yml` - MongoDB, Kafka, and Gemini API configuration

## 🧪 Testing

Each service includes test classes for unit testing:

```bash
# Run tests for a specific service
cd [service-name]
mvn test
```

## 📝 Development Notes

- All services use **Spring Boot 3.5.6** with **Java 17**
- **Lombok** is used for reducing boilerplate code
- Services communicate via **REST APIs** and **Kafka events**
- **MongoDB** is used for document-based data (activities, recommendations)
- **PostgreSQL** is used for relational data (users)
- **Eureka** provides service discovery and load balancing
- **Config Server** manages externalized configurations

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🔧 Troubleshooting

### Common Windows Issues

#### 1. Config Server DNS Lookup Issues
**Problem**: Config Server fails to start with DNS lookup errors on Windows
**Solution**: Add VM option `-Dspring.cloud.client.hostname=localhost`

#### 2. User Service Timezone Issues
**Problem**: Created timestamps show incorrect timezone
**Solution**: Add VM option `-Duser.timezone=Asia/Kolkata`

#### 3. Service Discovery Issues
**Problem**: Services not registering with Eureka
**Solution**: Ensure all services are started in the correct order and Eureka is running first

#### 4. Database Connection Issues
**Problem**: Services cannot connect to databases
**Solution**: 
- Verify PostgreSQL is running on port 5332
- Verify MongoDB is running on default port 27017
- Check database credentials in configuration files

#### 5. Kafka Connection Issues
**Problem**: Event publishing/consuming fails
**Solution**: Ensure Kafka is running on localhost:9092

## 🆘 Support

For support and questions, please open an issue in the repository.

---

**Note**: Make sure all required services (PostgreSQL, MongoDB, Kafka) are running before starting the microservices. The services depend on each other and external dependencies for proper functionality.
