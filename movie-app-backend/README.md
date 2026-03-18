# Movie App Backend - Spring Boot 3.x JWT Authentication

A comprehensive Spring Boot 3.x application with JWT-based authentication, MySQL database integration, and RESTful API endpoints.

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Technical Stack](#technical-stack)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Security Features](#security-features)
- [Database Schema](#database-schema)
- [Testing](#testing)
- [Deployment](#deployment)
- [Troubleshooting](#troubleshooting)

## 🎯 Project Overview

The Movie App Backend is a production-ready Spring Boot application implementing:

- ✅ **JWT-based authentication** using HS512 algorithm
- ✅ **User registration and login** with email uniqueness validation
- ✅ **Password encryption** using BCrypt with strength 10
- ✅ **Stateless session management** for scalability
- ✅ **Role-based access control** (RBAC) foundation
- ✅ **Global exception handling** with consistent error responses
- ✅ **MySQL database integration** with JPA/Hibernate
- ✅ **Clean architecture** with separated concerns

## 🛠 Technical Stack

### Core Framework
- **Spring Boot**: 3.5.11
- **Java**: 17 (LTS)
- **Maven**: Build automation

### Security
- **Spring Security**: Modern Spring Boot 3.x standards
- **JWT (JJWT)**: 0.12.3 - Token generation and validation
- **BCrypt**: Password encryption

### Database
- **MySQL**: 8.0+
- **Spring Data JPA**: ORM and data access
- **Hibernate**: JPA implementation

### Additional Libraries
- **Lombok**: Boilerplate reduction (@Getter, @Setter, @RequiredArgsConstructor, @Slf4j)
- **SLF4J**: Logging framework

## 📁 Project Structure

```
movie-app-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/movie_app_backend/movie_app_backend/
│   │   │       ├── config/
│   │   │       │   └── AppConfig.java                    # General application configuration
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java               # Authentication endpoints
│   │   │       │   └── MovieController.java              # Movie endpoints
│   │   │       ├── dto/
│   │   │       │   ├── RegisterRequest.java              # Registration request DTO
│   │   │       │   ├── LoginRequest.java                 # Login request DTO
│   │   │       │   ├── AuthResponse.java                 # Authentication response DTO
│   │   │       │   └── MovieDTO.java                     # Movie data transfer object
│   │   │       ├── exception/
│   │   │       │   ├── EmailAlreadyExistsException.java  # Custom email exception
│   │   │       │   ├── InvalidCredentialsException.java  # Custom credentials exception
│   │   │       │   └── GlobalExceptionHandler.java       # Central exception handler
│   │   │       ├── model/
│   │   │       │   ├── User.java                         # User entity
│   │   │       │   └── Movie.java                        # Movie entity
│   │   │       ├── repository/
│   │   │       │   ├── UserRepository.java               # User data access
│   │   │       │   └── MovieRepository.java              # Movie data access
│   │   │       ├── security/
│   │   │       │   ├── SecurityConfig.java               # Spring Security configuration
│   │   │       │   ├── JwtAuthenticationFilter.java      # JWT token filter
│   │   │       │   └── UserDetailsServiceImpl.java        # User details service
│   │   │       ├── service/
│   │   │       │   ├── AuthService.java                  # Authentication business logic
│   │   │       │   └── MovieService.java                 # Movie business logic
│   │   │       ├── util/
│   │   │       │   ├── JwtUtil.java                      # JWT utility methods
│   │   │       │   ├── JwtTokenProvider.java             # Legacy JWT provider (deprecated)
│   │   │       │   └── AppConstants.java                 # Application constants
│   │   │       └── MovieApplication.java                 # Main application class
│   │   └── resources/
│   │       ├── application.properties                    # Application configuration
│   │       ├── static/                                  # Static resources
│   │       └── templates/                               # HTML templates
│   └── test/
│       └── java/
│           └── com/movie_app_backend/
│               └── movie_app_backend/
│                   └── MovieApplicationTests.java       # Integration tests
├── pom.xml                                              # Maven configuration
├── mvnw                                                 # Maven wrapper (Linux/Mac)
├── mvnw.cmd                                             # Maven wrapper (Windows)
├── JWT_AUTHENTICATION_GUIDE.md                          # Detailed JWT documentation
├── JWT_IMPLEMENTATION_SUMMARY.md                        # Implementation summary
├── test_jwt_api.sh                                      # Bash test script
├── test_jwt_api.ps1                                     # PowerShell test script
└── README.md                                            # This file
```

## 🚀 Setup Instructions

### Prerequisites

1. **Java 17 or later**
   ```bash
   java -version
   ```

2. **Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **MySQL 8.0+**
   ```bash
   mysql --version
   ```

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd movie-app-backend
   ```

2. **Create MySQL database**
   ```sql
   CREATE DATABASE movieapp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Update application.properties** (if needed)
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/movieapp
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

   Or use the compiled JAR:
   ```bash
   java -jar target/movie-app-backend-0.0.1-SNAPSHOT.jar
   ```

The application will start on `http://localhost:8080` with context path `/api`.

## ⚙️ Configuration

### application.properties

```properties
# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/movieapp?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=alex@123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true

# Server Configuration
server.port=8080
server.servlet.context-path=/api

# JWT Configuration
app.jwt.secret=mySecretKeyForJWTTokenGenerationAndValidationPurposesOnlyChangeInProduction
app.jwt.expiration=86400000  # 24 hours in milliseconds

# Logging Configuration
logging.level.root=INFO
logging.level.com.movie_app_backend=DEBUG
logging.level.org.springframework.security=DEBUG

# Application Configuration
spring.application.name=Movie App Backend
```

### JWT Configuration

| Property | Description | Default |
|----------|-------------|---------|
| `app.jwt.secret` | Secret key for signing JWT tokens (min 256 bits) | `mySecretKeyForJWTTokenGenerationAndValidationPurposesOnlyChangeInProduction` |
| `app.jwt.expiration` | Token expiration time in milliseconds | `86400000` (24 hours) |

⚠️ **IMPORTANT**: Change the JWT secret in production to a strong random value!

## 🔐 API Endpoints

### Authentication Endpoints

#### 1. Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Response (201 Created):**
```json
{
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "message": "User registered successfully!"
}
```

#### 2. Login User
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "message": "Login successful!"
}
```

### Protected Endpoints

To access protected endpoints, include the JWT token in the Authorization header:

```http
GET /api/movies
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

## 🔐 Authentication

### JWT Token Structure

The JWT token consists of three parts separated by dots:

1. **Header**: Algorithm and token type
   ```json
   {
     "alg": "HS512",
     "typ": "JWT"
   }
   ```

2. **Payload**: Claims and user information
   ```json
   {
     "sub": "john@example.com",
     "iat": 1710699600,
     "exp": 1710786000
   }
   ```

3. **Signature**: HMAC-SHA512 hash of header + payload

### Token Format

```
Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzEwNjk5NjAwLCJleHAiOjE3MTA3ODYwMDB9.signature
```

### Token Validation

The filter validates:
- ✅ Signature correctness
- ✅ Token expiration
- ✅ Format compliance
- ✅ Claims presence

## 🛡️ Security Features

### 1. Password Security
- **Algorithm**: BCrypt with strength 10
- **No Plain Text**: Passwords stored as salted hashes
- **Secure Matching**: Uses `PasswordEncoder.matches()` for validation

### 2. JWT Security
- **Algorithm**: HMAC-SHA512 (HS512)
- **Stateless**: No server-side session storage
- **Expiration**: Configurable token lifespan
- **Signature Verification**: Ensures token integrity

### 3. Session Management
- **Stateless**: No HttpSession creation
- **No Cookies**: Reduced attack surface
- **Scalable**: Suitable for distributed systems

### 4. CSRF Protection
- **Disabled**: Appropriate for stateless JWT API
- **Same-origin**: Unnecessary for token-based auth

### 5. Authentication Filter
- **OncePerRequestFilter**: Ensures single execution per request
- **Order**: Before UsernamePasswordAuthenticationFilter
- **Graceful Failure**: Continues chain on errors

### 6. Exception Handling
- **Centralized**: Global exception handler
- **Consistent Format**: Standardized error responses
- **HTTP Status Codes**: Appropriate for each scenario

## 📊 Database Schema

### Users Table

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Movies Table

```sql
CREATE TABLE movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    release_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

Note: Tables are created automatically by Hibernate with `spring.jpa.hibernate.ddl-auto=update`

## 🧪 Testing

### Using PowerShell (Windows)

```powershell
# Make the script executable if needed
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# Run the test script
.\test_jwt_api.ps1
```

### Using Bash (Linux/Mac)

```bash
# Make the script executable
chmod +x test_jwt_api.sh

# Run the test script
./test_jwt_api.sh
```

### Manual Testing with cURL

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"john@test.com","password":"pass123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@test.com","password":"pass123"}'

# Access protected endpoint
curl -X GET http://localhost:8080/api/movies \
  -H "Authorization: Bearer <token>"
```

### Using Postman

1. Import the collection (create from scratch or use provided)
2. Set base URL: `http://localhost:8080/api`
3. Create requests for `/auth/register` and `/auth/login`
4. Save token from login response
5. Use token in Authorization header for protected endpoints

## 🚢 Deployment

### Production Checklist

- [ ] Change JWT secret to a strong random value (min 256 bits)
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate` (don't auto-create/update)
- [ ] Enable HTTPS/SSL
- [ ] Configure environment variables for sensitive data
- [ ] Set logging level to INFO or WARN
- [ ] Implement refresh token mechanism
- [ ] Add rate limiting
- [ ] Configure CORS if needed
- [ ] Set up database backups
- [ ] Enable monitoring and logging
- [ ] Use connection pooling for database
- [ ] Configure appropriate JWT expiration times

### Environment Variables

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://prod-db:3306/movieapp
export SPRING_DATASOURCE_USERNAME=dbuser
export SPRING_DATASOURCE_PASSWORD=secure_password
export APP_JWT_SECRET=your_very_secure_random_key_here
export APP_JWT_EXPIRATION=86400000
export SERVER_PORT=8080
```

### Docker Deployment

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/movie-app-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 🔧 Troubleshooting

### JWT Token Invalid

**Problem**: "Invalid JWT token" error

**Solutions**:
1. Verify token is not expired (`app.jwt.expiration`)
2. Check JWT secret matches between generation and validation
3. Ensure Authorization header format: `Bearer <token>`
4. Check token wasn't modified after generation

### User Not Found

**Problem**: "User not found with email" error

**Solutions**:
1. Verify user was registered successfully
2. Check email is stored correctly (case-sensitive)
3. Ensure database connection is active
4. Check `UserDetailsServiceImpl.loadUserByUsername()`

### Database Connection Error

**Problem**: "Cannot connect to database" error

**Solutions**:
1. Verify MySQL service is running: `mysql.server start` (Mac) or use Services (Windows)
2. Check credentials in `application.properties`
3. Verify database exists: `CREATE DATABASE movieapp;`
4. Check connection string: `jdbc:mysql://localhost:3306/movieapp`

### BCrypt Password Mismatch

**Problem**: Login fails even with correct password

**Solutions**:
1. Ensure `PasswordEncoder` bean is configured
2. Check password encoding during registration
3. Verify password matches during login using `PasswordEncoder.matches()`
4. Check `AppConfig` or `SecurityConfig` has PasswordEncoder bean

### CORS Issues

**Problem**: "CORS policy" errors in frontend

**Solutions**:
1. Add CORS configuration in `SecurityConfig`
2. Configure allowed origins, methods, headers
3. Handle preflight requests (OPTIONS)

### Stateless Session Error

**Problem**: SecurityContext lost between requests

**Solutions**:
1. Verify `SessionCreationPolicy.STATELESS` is set
2. Check JWT filter is properly configured
3. Ensure token is included in every protected request
4. Verify token hasn't expired

## 📚 Documentation Files

- **JWT_AUTHENTICATION_GUIDE.md** - Detailed JWT implementation guide
- **JWT_IMPLEMENTATION_SUMMARY.md** - Component summary and overview
- **test_jwt_api.ps1** - PowerShell testing script
- **test_jwt_api.sh** - Bash testing script

## 🤝 Contributing

When adding new features:

1. Follow existing code structure and naming conventions
2. Use Lombok annotations for boilerplate reduction
3. Add proper exception handling
4. Include logging statements
5. Write unit and integration tests
6. Update documentation

## 📝 License

This project is licensed under the MIT License.

## 📞 Support

For issues or questions:
1. Check the troubleshooting section
2. Review JWT_AUTHENTICATION_GUIDE.md
3. Check application logs with DEBUG level
4. Verify database connection and schema

---

**Last Updated**: March 2026
**Spring Boot Version**: 3.5.11
**Java Version**: 17 LTS

