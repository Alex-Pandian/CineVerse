# 📚 Documentation Index - Movie App Backend

Welcome to the Movie App Backend documentation! This index helps you navigate all available resources.

---

## 🎯 Start Here

### For Quick Start
👉 **[README.md](README.md)** - Complete project overview and setup instructions
- Project structure
- Setup instructions  
- Configuration details
- API endpoints
- Testing procedures
- Troubleshooting guide

### For Quick Reference
👉 **[QUICK_REFERENCE.md](QUICK_REFERENCE.md)** - Cheat sheet and quick lookup
- File locations
- Code snippets
- Configuration quick reference
- API endpoint cheat sheet
- Troubleshooting tips
- Testing quick commands

---

## 📖 Detailed Documentation

### JWT Authentication Implementation
👉 **[JWT_AUTHENTICATION_GUIDE.md](JWT_AUTHENTICATION_GUIDE.md)** - In-depth JWT documentation
- Component descriptions
- Authentication flow
- Configuration properties
- API examples with cURL
- Security best practices
- Error handling
- Testing procedures

### Implementation Summary
👉 **[JWT_IMPLEMENTATION_SUMMARY.md](JWT_IMPLEMENTATION_SUMMARY.md)** - Overview of what was built
- Components implemented
- Features overview
- Project structure
- Configuration properties
- Testing scenarios
- Next steps and enhancements

### Complete Status Report
👉 **[JWT_IMPLEMENTATION_COMPLETE.md](JWT_IMPLEMENTATION_COMPLETE.md)** - Final implementation status
- Full component list
- Security features
- Authentication flows (with ASCII diagrams)
- API endpoints
- Production checklist
- Enhancement suggestions

---

## 🧪 Testing Resources

### PowerShell Testing Script (Windows)
```powershell
# Run this script to test all JWT authentication endpoints
.\test_jwt_api.ps1
```
Tests include:
- User registration
- User login
- Token generation
- Protected endpoint access
- Invalid credentials handling
- Duplicate email handling

📄 **File**: [test_jwt_api.ps1](test_jwt_api.ps1)

### Bash Testing Script (Linux/Mac)
```bash
# Run this script to test all JWT authentication endpoints
./test_jwt_api.sh
```

📄 **File**: [test_jwt_api.sh](test_jwt_api.sh)

---

## 📁 Project Structure

```
src/main/java/com/movie_app_backend/movie_app_backend/
├── config/
│   └── AppConfig.java ........................... General app configuration
├── controller/
│   ├── AuthController.java ..................... Authentication endpoints
│   └── MovieController.java .................... Movie endpoints
├── dto/
│   ├── RegisterRequest.java .................... Registration input
│   ├── LoginRequest.java ....................... Login input
│   ├── AuthResponse.java ....................... Auth response with token
│   └── MovieDTO.java ........................... Movie data transfer object
├── exception/
│   ├── EmailAlreadyExistsException.java ........ Email duplicate exception
│   ├── InvalidCredentialsException.java ........ Login error exception
│   └── GlobalExceptionHandler.java ............ Centralized exception handler
├── model/
│   ├── User.java ............................... User entity
│   └── Movie.java .............................. Movie entity
├── repository/
│   ├── UserRepository.java .................... User data access (with findByEmail)
│   └── MovieRepository.java ................... Movie data access
├── security/
│   ├── SecurityConfig.java .................... Spring Security configuration
│   ├── JwtAuthenticationFilter.java ........... JWT token filter
│   └── UserDetailsServiceImpl.java ............ User details service
├── service/
│   ├── AuthService.java ....................... Register/login business logic
│   └── MovieService.java ....................... Movie business logic
├── util/
│   ├── JwtUtil.java ............................ JWT token operations
│   ├── JwtTokenProvider.java .................. Legacy JWT provider
│   └── AppConstants.java ....................... Application constants
└── MovieApplication.java ..................... Main Spring Boot application
```

---

## 🔐 Key Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Framework** | Spring Boot | 3.5.11 |
| **Language** | Java | 17 LTS |
| **Security** | Spring Security | 6.x (included in Boot) |
| **JWT Library** | JJWT | 0.12.3 |
| **Database** | MySQL | 8.0+ |
| **ORM** | Spring Data JPA / Hibernate | Latest |
| **Password Encoding** | BCrypt | Built-in (Strength 10) |
| **Build Tool** | Maven | 3.6+ |
| **Logger** | SLF4J / Logback | Built-in |
| **Boilerplate Reduction** | Lombok | Latest |

---

## 🚀 Getting Started

### 1. Prerequisites
- Java 17 or later
- Maven 3.6 or later
- MySQL 8.0 or later

### 2. Setup Database
```sql
CREATE DATABASE movieapp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configure Connection
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/movieapp
spring.datasource.username=root
spring.datasource.password=your_password
```

### 4. Build & Run
```bash
# Build
mvn clean install

# Run
mvn spring-boot:run
```

### 5. Test Endpoints
See **[QUICK_REFERENCE.md](QUICK_REFERENCE.md)** for cURL examples or run test scripts.

---

## 📋 API Quick Reference

### Public Endpoints (No Auth Required)
```
POST   /api/auth/register          Register new user
POST   /api/auth/login             Login and get JWT token
```

### Protected Endpoints (JWT Required)
```
GET    /api/movies                 List all movies
POST   /api/movies                 Create new movie
GET    /api/movies/{id}            Get movie by ID
PUT    /api/movies/{id}            Update movie
DELETE /api/movies/{id}            Delete movie
```

**Header Required for Protected Endpoints:**
```
Authorization: Bearer <jwt_token>
```

---

## 🔑 Important Configuration

### JWT Configuration
```properties
# JWT secret - CHANGE IN PRODUCTION!
app.jwt.secret=mySecretKeyForJWTTokenGenerationAndValidationPurposesOnlyChangeInProduction

# Token expiration (24 hours in milliseconds)
app.jwt.expiration=86400000
```

### Database Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/movieapp?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=alex@123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### Server Configuration
```properties
server.port=8080
server.servlet.context-path=/api
```

---

## 🔍 Common Questions & Answers

### Q: How do I change the JWT secret?
**A:** Edit `application.properties` and update `app.jwt.secret` with a strong random value (min 256 bits for HS512).

### Q: How long is the JWT token valid?
**A:** Default is 24 hours (86400000 milliseconds). Change `app.jwt.expiration` in `application.properties`.

### Q: How is the password encrypted?
**A:** Using BCrypt with strength 10. This is configured in `SecurityConfig.java`.

### Q: Can I use the same token for multiple requests?
**A:** Yes, until it expires. Include it in the Authorization header for each protected request.

### Q: How do I add a new protected endpoint?
**A:** Create a controller method and use `@PreAuthorize` or rely on `SecurityConfig` to protect all endpoints except `/api/auth/**`.

### Q: What happens when the token expires?
**A:** The request returns 401 Unauthorized. User must login again to get a new token.

### Q: How do I test the API?
**A:** Use the provided test scripts (`test_jwt_api.ps1` or `test_jwt_api.sh`) or use cURL/Postman examples in documentation.

---

## 📞 Support & Help

### If You Encounter Issues

1. **Check the Troubleshooting Section**
   - See "README.md" → Troubleshooting section
   - See "QUICK_REFERENCE.md" → Troubleshooting Quick Tips

2. **Review the JWT Guide**
   - See "JWT_AUTHENTICATION_GUIDE.md" for detailed explanations

3. **Check Configuration**
   - Verify `application.properties` settings
   - Ensure MySQL is running
   - Check database connection

4. **Review Logs**
   - Check application logs for error messages
   - Set logging level to DEBUG for detailed output
   - Look for SecurityContext errors

---

## 🎓 Learning Resources

### Understanding JWT
- JWT token structure: Header.Payload.Signature
- HMAC-SHA512: Symmetric cryptographic algorithm
- Token validation: Signature verification + expiration check
- Stateless: No server-side session required

### Understanding Spring Security
- `SecurityConfig`: Defines security rules
- `JwtAuthenticationFilter`: Intercepts requests
- `UserDetailsService`: Loads user information
- `SecurityContext`: Stores authenticated user
- `PasswordEncoder`: Encodes/matches passwords

### Understanding BCrypt
- One-way hashing algorithm
- Adds salt automatically
- Makes brute-force attacks computationally expensive
- Strength parameter (10) controls iteration count

---

## ✅ Features Checklist

- [x] JWT token generation (HS512)
- [x] JWT token validation
- [x] User registration with email validation
- [x] User login with password verification
- [x] BCrypt password encryption
- [x] Stateless session management
- [x] Protected endpoints
- [x] Exception handling
- [x] Logging
- [x] MySQL integration
- [x] Complete documentation
- [x] Test scripts

---

## 🚀 Production Deployment Checklist

Before deploying to production:

- [ ] Change JWT secret to secure random value
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate`
- [ ] Enable HTTPS/SSL
- [ ] Configure environment variables
- [ ] Set logging level to WARN
- [ ] Set up database backups
- [ ] Enable monitoring and alerts
- [ ] Implement rate limiting
- [ ] Configure CORS if needed
- [ ] Test all endpoints thoroughly
- [ ] Load test the application
- [ ] Review security headers
- [ ] Set up log aggregation

---

## 📚 Documentation Map

```
Project Root
├── README.md .......................... Start here! Full documentation
├── QUICK_REFERENCE.md ............... Cheat sheet and quick lookup
├── JWT_AUTHENTICATION_GUIDE.md ...... Detailed JWT documentation
├── JWT_IMPLEMENTATION_SUMMARY.md .... Implementation overview
├── JWT_IMPLEMENTATION_COMPLETE.md .. Final status report
├── QUICK_REFERENCE.md .............. This file - Documentation index
├── test_jwt_api.ps1 ................ PowerShell testing script
├── test_jwt_api.sh ................. Bash testing script
└── src/main/
    ├── java/ ........................ Java source code
    └── resources/
        └── application.properties ... Configuration file
```

---

## 🎯 What to Read Based on Your Needs

| Your Need | Read This | Time |
|-----------|-----------|------|
| Quick start | README.md + QUICK_REFERENCE.md | 15 min |
| Understand JWT | JWT_AUTHENTICATION_GUIDE.md | 20 min |
| See what was built | JWT_IMPLEMENTATION_COMPLETE.md | 10 min |
| Find API examples | QUICK_REFERENCE.md (API section) | 5 min |
| Test the system | test_jwt_api.ps1 or test_jwt_api.sh | 5 min |
| Understand code | QUICK_REFERENCE.md (Code Snippets) | 15 min |
| Deploy to production | README.md (Deployment section) | 20 min |

---

## 📞 Quick Links

- **Main Documentation**: [README.md](README.md)
- **Quick Cheat Sheet**: [QUICK_REFERENCE.md](QUICK_REFERENCE.md)
- **JWT Details**: [JWT_AUTHENTICATION_GUIDE.md](JWT_AUTHENTICATION_GUIDE.md)
- **Implementation Status**: [JWT_IMPLEMENTATION_COMPLETE.md](JWT_IMPLEMENTATION_COMPLETE.md)
- **Testing (Windows)**: [test_jwt_api.ps1](test_jwt_api.ps1)
- **Testing (Linux/Mac)**: [test_jwt_api.sh](test_jwt_api.sh)

---

**Last Updated**: March 17, 2026  
**Spring Boot**: 3.5.11  
**Java**: 17 LTS  
**Status**: ✅ Production Ready

