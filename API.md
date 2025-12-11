# HealthPal API Documentation

## Base URL

```
http://localhost:8080/api
```

## Authentication

All protected endpoints require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Swagger Documentation

Interactive API documentation is available at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## API Endpoints Overview

### Authentication (`/api/auth`)

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| POST | `/api/auth/register/admin` | Register admin user | Public |
| POST | `/api/auth/register/patient` | Register patient | Public |
| POST | `/api/auth/register/doctor` | Register doctor | Public |
| POST | `/api/auth/login` | User login | Public |

### Treatments (`/api/treatments`)

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| GET | `/api/treatments` | Get all treatments | PATIENT, DOCTOR, ADMIN, DONOR |
| GET | `/api/treatments/{id}` | Get treatment by ID | PATIENT, DOCTOR, ADMIN, DONOR |
| GET | `/api/treatments/active` | Get active treatments | PATIENT, DOCTOR, ADMIN, DONOR |
| GET | `/api/treatments/progress/{id}` | Get treatment progress | PATIENT, DOCTOR, ADMIN, DONOR |
| GET | `/api/treatments/patient/{patientId}` | Get patient's treatments | PATIENT, DOCTOR, ADMIN |
| POST | `/api/treatments` | Create treatment | PATIENT |
| PUT | `/api/treatments/{id}` | Update treatment | PATIENT, ADMIN |
| DELETE | `/api/treatments/{id}` | Delete treatment | PATIENT, ADMIN |

### Donations (`/api/donations`)

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| GET | `/api/donations` | Get all donations | ADMIN, DONOR, NGO |
| GET | `/api/donations/{id}` | Get donation by ID | ADMIN, DONOR, NGO |
| POST | `/api/donations` | Create donation | DONOR, NGO, ADMIN |
| PUT | `/api/donations/{id}/status` | Update payment status | ADMIN, NGO |
| GET | `/api/donations/transparency` | Transparency dashboard | ADMIN |

### Equipment (`/api/equipment`)

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| GET | `/api/equipment` | Get all equipment | All |
| GET | `/api/equipment/{id}` | Get equipment by ID | All |
| GET | `/api/equipment/search` | Search equipment | All |
| POST | `/api/equipment` | Add equipment | NGO, ADMIN |
| PUT | `/api/equipment/{id}` | Update equipment | NGO, ADMIN |
| DELETE | `/api/equipment/{id}` | Delete equipment | NGO, ADMIN |

### Supplies (`/api/supplies`)

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| GET | `/api/supplies` | Get all supplies | All |
| GET | `/api/supplies/{id}` | Get supply by ID | All |
| GET | `/api/supplies/category/{category}` | Get by category | All |
| GET | `/api/supplies/expiring` | Get expiring supplies | ADMIN, NGO |
| POST | `/api/supplies` | Add supply | NGO, ADMIN |
| PUT | `/api/supplies/{id}` | Update supply | NGO, ADMIN |

### Consultations (`/api/consultations`)

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| GET | `/api/consultations` | Get user's consultations | PATIENT, DOCTOR, ADMIN |
| GET | `/api/consultations/{id}` | Get consultation by ID | PATIENT, DOCTOR, ADMIN |
| POST | `/api/consultations` | Create consultation | PATIENT |

### Consultation Messages (`/api/consultations/{id}/messages`)

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| GET | `/api/consultations/{id}/messages` | Get messages | PATIENT, DOCTOR, ADMIN |
| POST | `/api/consultations/{id}/messages` | Send message | PATIENT, DOCTOR, ADMIN |

### Medical Records (`/api/medical-records`)

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| GET | `/api/medical-records/patient/{patientId}` | Get patient records | PATIENT, DOCTOR, ADMIN |
| POST | `/api/medical-records` | Create record | DOCTOR, ADMIN |
| GET | `/api/medical-records/{id}` | Get record by ID | PATIENT, DOCTOR, ADMIN |

### Translation (`/api/translate`)

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| POST | `/api/translate` | Translate text | All authenticated |
| POST | `/api/translate/ar-to-en` | Arabic to English | All authenticated |
| POST | `/api/translate/en-to-ar` | English to Arabic | All authenticated |

### Health Alerts (`/api/health-alerts`)

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| GET | `/api/health-alerts` | Get active alerts | All authenticated |
| POST | `/api/health-alerts` | Create alert | ADMIN |

## Request/Response Examples

### Register Patient

**Request:**
```http
POST /api/auth/register/patient
Content-Type: application/json

{
  "user": {
    "fullName": "John Doe",
    "email": "john@example.com",
    "passwordHash": "password123",
    "phoneNumber": "1234567890",
    "role": "PATIENT"
  },
  "dateOfBirth": "1990-01-01",
  "gender": "male",
  "address": "Ramallah, Palestine"
}
```

### Create Treatment

**Request:**
```http
POST /api/treatments
Authorization: Bearer <token>
Content-Type: application/json

{
  "treatmentType": "SURGERY",
  "description": "Heart surgery needed",
  "goalAmount": 50000.00,
  "startDate": "2025-01-15"
}
```

**Response:**
```json
{
  "treatmentId": 1,
  "patient": { ... },
  "treatmentType": "SURGERY",
  "description": "Heart surgery needed",
  "goalAmount": 50000.00,
  "raisedAmount": 0.00,
  "status": "ACTIVE"
}
```

### Get Treatment Progress

**Response:**
```json
{
  "treatmentId": 1,
  "goalAmount": 50000.00,
  "raisedAmount": 25000.00,
  "progressPercent": 50.00
}
```

## Error Responses

All errors follow a standard format:

```json
{
  "timestamp": "2025-01-01T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Treatment not found with id: 999",
  "path": "/api/treatments/999"
}
```

## Status Codes

- `200 OK`: Successful request
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Missing or invalid token
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## Rate Limiting

Currently, rate limiting is not implemented but is planned for future versions.

## Versioning

The API is currently at version 1.0. Future versions will use URL versioning:
- `/api/v1/treatments`
- `/api/v2/treatments`

