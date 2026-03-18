# JWT Authentication Implementation Guide

## Overview
This document describes the JWT-based authentication implementation for the Movie App Backend using Spring Boot 3.x standards.

## Components

### 1. JwtUtil Class
**Location:** `src/main/java/com/movie_app_backend/movie_app_backend/util/JwtUtil.java`

**Responsibilities:**
- Generate JWT tokens with HS512 algorithm
- Validate JWT tokens (signature, expiration, format)
- Extract username from tokens
- Extract claims from tokens

**Key Methods:**
- `generateToken(String username)` - Creates a signed JWT token
- `validateToken(String token)` - Validates token signature and expiration
- `extractUsername(String token)` - Extracts the subject (username) from token
- `extractClaim(String token, Function<Claims, T> claimsResolver)` - Extracts specific claims

**Configuration:**
- Secret Key: Configured via `app.jwt.secret` property
- Expiration Time: Configured via `app.jwt.expiration` property (default: 24 hours)
- Algorithm: HMAC SHA-512 (HS512)

### 2. JwtAuthenticationFilter
**Location:** `src/main/java/com/movie_app_backend/movie_app_backend/security/JwtAuthenticationFilter.java`

**Responsibilities:**
- Intercepts HTTP requests
- Extracts JWT token from Authorization header
- Validates token using JwtUtil
- Sets authentication in SecurityContext

**Request Format:**
```
Authorization: Bearer <jwt_token>
```

**Flow:**
1. Extract token from `Authorization` header
2. Validate token format (must start with "Bearer ")
3. Validate token using JwtUtil
4. Extract username from token
5. Load user details using UserDetailsService
6. Create UsernamePasswordAuthenticationToken
7. Set authentication in SecurityContext

### 3. UserDetailsServiceImpl
**Location:** `src/main/java/com/movie_app_backend/movie_app_backend/security/UserDetailsServiceImpl.java`

**Responsibilities:**
- Implement Spring Security's UserDetailsService
- Load user details from database by email
- Build UserDetails object with authorities

**Default Authority:** ROLE_USER

### 4. SecurityConfig
**Location:** `src/main/java/com/movie_app_backend/movie_app_backend/security/SecurityConfig.java`

**Security Configuration:**
- **Session Management:** STATELESS (no session cookies)
- **CSRF Protection:** DISABLED (appropriate for stateless API)
- **Public Endpoints:**
  - `/api/auth/**` - Authentication endpoints (register, login)
  - `/api/public/**` - Public endpoints
  - `/swagger-ui/**` - Swagger UI documentation
  - `/v3/api-docs/**` - OpenAPI documentation

- **Protected Endpoints:** All other endpoints require authentication

- **JWT Filter:** Added before UsernamePasswordAuthenticationFilter

- **Exception Handling:**
  - Authentication errors: 401 Unauthorized
  - Authorization errors: 403 Forbidden

### 5. AuthService
**Location:** `src/main/java/com/movie_app_backend/movie_app_backend/service/AuthService.java`

**Methods:**
- `register(RegisterRequest request)` - Register new user with email validation and password encryption
- `login(LoginRequest request)` - Authenticate user and return JWT token

**Features:**
- Email uniqueness validation
- BCrypt password encryption
- Detailed logging
- Transaction management

### 6. AuthController
**Location:** `src/main/java/com/movie_app_backend/movie_app_backend/controller/AuthController.java`

**Endpoints:**
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user and get JWT token

## Authentication Flow

### Registration Flow
1. User sends registration request with name, email, and password
2. AuthService validates email uniqueness
3. Password is encrypted using BCrypt
4. User is saved to database
5. Response returns user details (without token)

### Login Flow
1. User sends login request with email and password
2. AuthService retrieves user from database
3. Password is verified against BCrypt hash
4. JwtUtil generates JWT token with email as subject
5. Response returns JWT token and user details

### Request Authentication Flow
1. Client includes JWT in Authorization header: `Authorization: Bearer <token>`
2. JwtAuthenticationFilter extracts token from header
3. Token is validated using JwtUtil
4. Username is extracted from token
5. User details are loaded from database
6. UsernamePasswordAuthenticationToken is created and set in SecurityContext
7. Request is processed with authenticated user context

## Configuration Properties

```properties
# JWT Configuration
app.jwt.secret=<your-secret-key>              # Secret key for signing tokens (min 256 bits for HS512)
app.jwt.expiration=86400000                   # Token expiration time in milliseconds

# Server Configuration
server.port=8080
server.servlet.context-path=/api
```

## API Examples

### Register User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "securePassword123"
  }'
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

### Login User
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "message": "Login successful!"
}
```

### Access Protected Resource
```bash
curl -X GET http://localhost:8080/api/movies \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

## Security Best Practices

1. **Secret Key Management:**
   - Change the default secret key in production
   - Use a strong random value (at least 256 bits for HS512)
   - Store in environment variables, not in code

2. **Token Expiration:**
   - Set appropriate expiration time
   - Implement refresh token mechanism for longer sessions
   - Default: 24 hours

3. **HTTPS:**
   - Always use HTTPS in production
   - Never send tokens over unencrypted connections

4. **Password Encoding:**
   - Uses BCrypt with strength 10
   - Passwords are never returned in responses
   - Stored as salted hashes in database

5. **Stateless Architecture:**
   - No server-side session storage
   - Reduced server memory footprint
   - Easier horizontal scaling

## Error Handling

The application includes a GlobalExceptionHandler that provides consistent error responses:

- **EmailAlreadyExistsException** - 409 Conflict
- **InvalidCredentialsException** - 401 Unauthorized
- **UsernameNotFoundException** - 404 Not Found
- **Generic Exception** - 500 Internal Server Error

## Dependencies

Required Maven dependencies:
- `spring-boot-starter-security` - Spring Security framework
- `spring-boot-starter-data-jpa` - JPA/Hibernate
- `mysql-connector-j` - MySQL driver
- `jjwt-api` - JWT API
- `jjwt-impl` - JWT implementation
- `jjwt-jackson` - JWT Jackson support
- `lombok` - Boilerplate reduction

## Testing

To test the authentication endpoints:

1. Start the application: `mvn spring-boot:run`
2. Register a new user with POST to `/api/auth/register`
3. Login with the created credentials to get a token
4. Use the token in the Authorization header for protected endpoints

## Future Enhancements

1. Add refresh token mechanism
2. Implement role-based access control (RBAC)
3. Add token revocation/blacklist
4. Implement social authentication (OAuth2, Google, etc.)
5. Add rate limiting for login attempts
6. Implement two-factor authentication (2FA)

