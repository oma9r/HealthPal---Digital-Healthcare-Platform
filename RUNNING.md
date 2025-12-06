# How to Run HealthPal

This guide explains how to run both the backend (Spring Boot) and frontend (React) applications.

## Prerequisites

### Backend Prerequisites
- **Java 17** or higher
- **Maven 3.6+** (or use the included Maven wrapper)
- **MySQL 8.0+**
- **Git**

### Frontend Prerequisites
- **Node.js 18+** and npm/yarn
- **Backend API** must be running first

---

## Step 1: Start the Backend (Spring Boot)

### 1.1 Set up MySQL Database

1. Open MySQL and create the database:
   ```bash
   mysql -u root -p
   ```

2. In MySQL, create the database:
   ```sql
   CREATE DATABASE HealthPal;
   EXIT;
   ```

3. Import the schema:
   ```bash
   mysql -u root -p HealthPal < healthpal.sql
   ```

### 1.2 Configure Database Connection

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/HealthPal?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

### 1.3 Run the Backend

From the project root directory (`healthcare/`):

**Option 1: Using Maven Wrapper (Recommended)**
```bash
./mvnw spring-boot:run
```

**Option 2: Using Maven (if installed)**
```bash
mvn spring-boot:run
```

**Option 3: Build and Run**
```bash
mvn clean install
mvn spring-boot:run
```

The backend will start on **http://localhost:8080**

✅ **Verify Backend is Running:**
- Visit: http://localhost:8080/swagger-ui.html (Swagger documentation)
- Or: http://localhost:8080/v3/api-docs (OpenAPI JSON)

---

## Step 2: Start the Frontend (React)

### 2.1 Navigate to Frontend Directory

```bash
cd frontend
```

### 2.2 Install Dependencies

**Using npm:**
```bash
npm install
```

**Using yarn:**
```bash
yarn install
```

### 2.3 (Optional) Configure API URL

Create a `.env` file in the `frontend/` directory:
```env
VITE_API_URL=http://localhost:8080/api
```

**Note:** This is optional as the default is already configured in `vite.config.ts`.

### 2.4 Run the Frontend

**Using npm:**
```bash
npm run dev
```

**Using yarn:**
```bash
yarn dev
```

The frontend will start on **http://localhost:3000**

✅ **Verify Frontend is Running:**
- Visit: http://localhost:3000
- You should see the HealthPal login page

---

## Complete Setup Summary

### Terminal 1: Backend (Spring Boot)
```bash
# In healthcare/ directory
./mvnw spring-boot:run
```
Backend runs on: **http://localhost:8080**

### Terminal 2: Frontend (React)
```bash
# In healthcare/frontend/ directory
cd frontend
npm install
npm run dev
```
Frontend runs on: **http://localhost:3000**

---

## Quick Start (All Commands)

```bash
# Terminal 1 - Backend
cd C:\Users\sshay\IdeaProjects\healthcare
./mvnw spring-boot:run

# Terminal 2 - Frontend (new terminal)
cd C:\Users\sshay\IdeaProjects\healthcare\frontend
npm install
npm run dev
```

---

## Accessing the Application

1. **Frontend UI**: http://localhost:3000
2. **Backend API**: http://localhost:8080/api
3. **Swagger UI**: http://localhost:8080/swagger-ui.html

---

## Testing the Application

1. **Register a User:**
   - Go to http://localhost:3000
   - Click "Register here"
   - Select your role (Patient, Doctor, Donor, NGO)
   - Fill in the registration form

2. **Login:**
   - Use the email and password you registered with
   - You'll be redirected to the dashboard

3. **Explore Features:**
   - Based on your role, different features will be available in the sidebar
   - Try booking consultations, viewing treatments, making donations, etc.

---

## Troubleshooting

### Backend Issues

**Problem: Database connection error**
- ✅ Check MySQL is running: `mysql -u root -p`
- ✅ Verify database exists: `SHOW DATABASES;`
- ✅ Check credentials in `application.properties`

**Problem: Port 8080 already in use**
- ✅ Change port in `application.properties`: `server.port=8081`
- ✅ Update frontend proxy in `frontend/vite.config.ts` to match

**Problem: Maven wrapper not executable (Windows)**
- ✅ Use: `mvnw.cmd spring-boot:run` instead

### Frontend Issues

**Problem: npm install fails**
- ✅ Ensure Node.js 18+ is installed: `node --version`
- ✅ Clear npm cache: `npm cache clean --force`
- ✅ Delete `node_modules` and `package-lock.json`, then reinstall

**Problem: Cannot connect to API**
- ✅ Verify backend is running on port 8080
- ✅ Check browser console for CORS errors
- ✅ Verify proxy configuration in `vite.config.ts`

**Problem: Port 3000 already in use**
- ✅ Vite will automatically use the next available port
- ✅ Or specify a port: `npm run dev -- --port 3001`

---

## Building for Production

### Backend
```bash
mvn clean package
java -jar target/healthcare-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
cd frontend
npm run build
```
Built files will be in `frontend/dist/`

---

## Development Tips

- **Hot Reload**: Both frontend and backend support hot reload
- **Backend**: Changes require restart (or use Spring DevTools)
- **Frontend**: Changes auto-reload in browser
- **Database**: Changes to entities will auto-update schema (if `ddl-auto=update`)

---

## Need Help?

- Check the logs:
  - Backend: `logs/healthcare.log`
  - Frontend: Browser console (F12)
- Review documentation:
  - `README.md` - Project overview
  - `API.md` - API documentation
  - `ARCHITECTURE.md` - System architecture
  - `frontend/README.md` - Frontend specific docs

