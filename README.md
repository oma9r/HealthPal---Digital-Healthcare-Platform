# HealthPal - Digital Healthcare Platform

## Advance Software Engineering Course
### Course Project – RESTful API's – Fall 2025
#### Course Instructor: Dr. Amjad AbuHassan

## Overview

HealthPal is a comprehensive digital healthcare platform designed to provide Palestinians with access to medical support, remote consultations, medicine coordination, and donation-driven treatment sponsorships. The application bridges patients, doctors, donors, and medical NGOs to help overcome the collapse or inaccessibility of local healthcare systems.

## Technology Stack

- **Framework**: Spring Boot 3.5.6
- **Language**: Java 17
- **Database**: MySQL 8.0
- **Security**: Spring Security with JWT authentication
- **API Documentation**: Swagger/OpenAPI 3 (SpringDoc)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito

## Features

### Core Features
1. **Remote Medical Consultations** - Virtual clinic access with low-bandwidth support
2. **Medical Sponsorship System** - Treatment funding with transparency dashboard
3. **Medication & Equipment Coordination** - Inventory management for medical supplies
4. **Health Education & Public Health Alerts** - Localized health guides and alerts
5. **Mental Health & Trauma Support** - Counseling portal and support groups
6. **NGO Partnerships** - Verified NGO network and medical missions

### API Endpoints

The API is fully documented using Swagger UI. Access the interactive documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd healthcare
   ```

2. **Set up MySQL database**
   ```bash
   mysql -u root -p
   CREATE DATABASE HealthPal;
   ```
   
   Import the schema:
   ```bash
   mysql -u root -p HealthPal < healthpal.sql
   ```

3. **Configure application properties**
   
   Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/HealthPal?useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   Or use the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080`

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── org/example/healthcare/
│   │       ├── config/          # Configuration classes
│   │       ├── controller/      # REST API controllers
│   │       ├── dto/             # Data Transfer Objects
│   │       ├── exception/       # Exception handling
│   │       ├── model/           # JPA entities
│   │       ├── repository/      # Data access layer
│   │       ├── security/        # Security configuration
│   │       └── service/         # Business logic layer
│   └── resources/
│       ├── application.properties
│       └── logback-spring.xml
└── test/                        # Test files
```

## API Authentication

The API uses JWT (JSON Web Token) authentication. To access protected endpoints:

1. Register a user via `/api/auth/register/{role}`
2. Login via `/api/auth/login` to receive a JWT token
3. Include the token in subsequent requests:
   ```
   Authorization: Bearer <your-token>
   ```

## Roles

- **PATIENT**: Access to personal consultations, treatments, and records
- **DOCTOR**: Access to consultations, patient records, medical records
- **DONOR**: Access to donation features and transparency dashboard
- **NGO**: Access to equipment/supplies management
- **ADMIN**: Full system access

## Testing

Run tests:
```bash
mvn test
```

## Documentation

- [Architecture Documentation](ARCHITECTURE.md)
- [API Documentation](API.md)
- [Contributing Guidelines](CONTRIBUTING.md)

## Team Members

- Omar Abumazen
- Saif Shayeb

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please contact the development team or create an issue in the repository.
