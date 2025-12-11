# HealthPal Architecture Documentation

## System Architecture

HealthPal follows a **layered architecture** pattern with clear separation of concerns:

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│      (REST Controllers)                 │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│         Business Logic Layer            │
│         (Services)                      │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│         Data Access Layer               │
│      (Repositories)                     │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│         Database Layer                  │
│          (MySQL)                        │
└─────────────────────────────────────────┘
```

## Technology Choices & Justification

### Spring Boot

**Why Spring Boot?**

1. **Scalability**
   - Built-in support for horizontal scaling
   - Stateless REST API design enables load balancing
   - Efficient dependency injection and bean management
   - Connection pooling for database operations

2. **Security**
   - Spring Security framework provides robust authentication/authorization
   - JWT token-based stateless authentication
   - Built-in protection against common vulnerabilities (CSRF, XSS)
   - Role-based access control (RBAC) implementation

3. **Maintainability**
   - Convention over configuration reduces boilerplate code
   - Clear separation of concerns (MVC pattern)
   - Extensive documentation and community support
   - Dependency injection promotes testability and loose coupling

4. **Development Efficiency**
   - Auto-configuration reduces setup time
   - Hot reload capabilities for faster development
   - Rich ecosystem of Spring Boot Starters
   - Built-in embedded server for rapid testing

### MySQL Database

**Why MySQL?**

1. **Data Integrity**
   - ACID compliance ensures transactional consistency
   - Foreign key constraints maintain referential integrity
   - Strong data type enforcement

2. **Relational Data Model**
   - Healthcare data is inherently relational (patients, doctors, treatments, donations)
   - Complex queries with JOINs are efficiently handled
   - Normalized schema reduces data redundancy

3. **Mature & Stable**
   - Widely used in production healthcare systems
   - Extensive documentation and tooling
   - Proven reliability and performance

4. **Compatibility**
   - Excellent Spring Data JPA integration
   - Hibernate ORM support for object-relational mapping
   - Standard SQL for easy maintenance

### REST API

**Why REST over GraphQL or gRPC?**

1. **Simplicity**
   - Easy to understand and implement
   - Standard HTTP methods (GET, POST, PUT, DELETE)
   - Straightforward request/response model

2. **Statelessness**
   - Each request contains all necessary information
   - Scales horizontally without session management
   - Easy caching and load balancing

3. **Caching**
   - HTTP caching mechanisms (ETags, Cache-Control headers)
   - Improved performance for read-heavy operations
   - Reduced server load

4. **Client Flexibility**
   - Works with any HTTP client
   - Easy to test with tools like Postman, curl
   - Clear contract between frontend and backend

## Design Patterns

### 1. Repository Pattern
- Abstraction layer between service and database
- Easily testable with mock repositories
- Consistent data access interface

### 2. Service Layer Pattern
- Business logic separated from controllers
- Transaction management
- Reusable across different controllers

### 3. DTO Pattern
- Data Transfer Objects separate API contracts from entities
- Prevents exposing internal model structure
- Version control for API changes

### 4. Exception Handling Pattern
- Global exception handler with `@ControllerAdvice`
- Standardized error responses
- Centralized error logging

## Security Architecture

### Authentication Flow
```
1. User Registration/Login → JWT Token Generation
2. Token included in Authorization header
3. JwtAuthFilter validates token
4. SecurityContext populated with user details
5. @PreAuthorize checks role permissions
```

### Authorization
- Role-based access control (RBAC)
- Method-level security with `@PreAuthorize`
- Resource-level ownership verification

## Database Schema

Key entities:
- **Users**: Core user authentication
- **Patients, Doctors, Donors, NGOs**: Role-specific profiles
- **Consultations**: Medical consultations
- **Treatments**: Medical treatments/sponsorships
- **Donations**: Funding and equipment donations
- **Medical Records**: Patient medical history
- **Equipment & Supplies**: Inventory management

Relationships are maintained through foreign keys ensuring referential integrity.

## External Integrations

### Translation Service
- Placeholder for translation API (Google Translate, DeepL, etc.)
- Enables Arabic ↔ English translation for consultations
- Configurable via application properties

### Health Alerts Service
- Integration point for public health APIs
- Can fetch alerts from WHO or local health authorities
- Fallback to local storage if API unavailable

## Logging & Monitoring

- **Logback** for application logging
- Request/response logging filter
- MDC (Mapped Diagnostic Context) for request tracking
- Structured log files with rotation

## Error Handling

- Global exception handler catches all exceptions
- Custom exceptions for domain-specific errors
- Standardized error response format
- Proper HTTP status codes

## Future Enhancements

1. **Caching**: Redis for frequently accessed data
2. **Message Queue**: RabbitMQ/Kafka for async processing
3. **File Storage**: AWS S3/Cloudinary for medical documents
4. **Monitoring**: Actuator endpoints for health checks
5. **Rate Limiting**: Bucket4j for API rate limiting
6. **Database Migration**: Flyway/Liquibase for version control

