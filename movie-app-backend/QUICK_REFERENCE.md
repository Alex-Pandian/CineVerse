# 🚀 Quick Reference Guide - JWT Authentication

## 📍 File Locations

```
Security Files:
├── security/SecurityConfig.java           → Main Spring Security configuration
├── security/JwtAuthenticationFilter.java  → JWT token extraction & validation
├── security/UserDetailsServiceImpl.java    → User details service implementation

Authentication Files:
├── service/AuthService.java               → Business logic for register/login
├── controller/AuthController.java         → REST endpoints for auth

JWT Utilities:
├── util/JwtUtil.java                      → Token generation & validation methods

Models & Data:
├── model/User.java                        → User entity with email unique constraint
├── repository/UserRepository.java         → Custom findByEmail() method
├── dto/RegisterRequest.java               → Registration input
├── dto/LoginRequest.java                  → Login input
├── dto/AuthResponse.java                  → Auth response with token

Exception Handling:
├── exception/GlobalExceptionHandler.java  → Centralized exception handling
├── exception/EmailAlreadyExistsException.java
├── exception/InvalidCredentialsException.java

Configuration:
└── resources/application.properties       → JWT & database config
```

---

## 🔐 Security Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│              AUTHENTICATION FLOW                             │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  1. REGISTRATION                                             │
│     POST /api/auth/register                                  │
│     ├─ AuthController receives request                       │
│     ├─ AuthService validates email uniqueness               │
│     ├─ BCrypt encrypts password                             │
│     ├─ User saved to database                               │
│     └─ Return user details (no token)                       │
│                                                               │
│  2. LOGIN                                                    │
│     POST /api/auth/login                                     │
│     ├─ AuthController receives request                       │
│     ├─ AuthService validates credentials                    │
│     ├─ Password verified with BCrypt                        │
│     ├─ JwtUtil generates JWT token (HS512)                 │
│     └─ Return token + user details                          │
│                                                               │
│  3. PROTECTED REQUEST                                        │
│     GET /api/movies (with Authorization header)             │
│     ├─ JwtAuthenticationFilter intercepts                   │
│     ├─ Extract token from "Authorization: Bearer <token>"  │
│     ├─ JwtUtil validates token signature & expiration       │
│     ├─ Extract username from token                          │
│     ├─ UserDetailsService loads user from DB               │
│     ├─ SecurityContext authenticated with user              │
│     └─ Request proceeds with authentication                 │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 📝 Code Snippets - Key Components

### 1. JWT Token Generation (JwtUtil.java)
```java
@Component
public class JwtUtil {
    public String generateToken(String username) {
        return createToken(username, jwtExpirationInMs);
    }
    
    public Boolean validateToken(String token) {
        // Validates signature and expiration
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}
```

### 2. JWT Authentication Filter (JwtAuthenticationFilter.java)
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    FilterChain filterChain) {
        // Extract token from Authorization header
        String jwt = extractTokenFromRequest(request);
        
        // Validate and authenticate
        if (StringUtils.hasText(jwt) && jwtUtil.validateToken(jwt)) {
            String username = jwtUtil.extractUsername(jwt);
            var userDetails = userDetailsService.loadUserByUsername(username);
            
            // Set authentication in SecurityContext
            UsernamePasswordAuthenticationToken auth = 
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        
        filterChain.doFilter(request, response);
    }
}
```

### 3. Security Configuration (SecurityConfig.java)
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disabled for stateless JWT
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                jwtAuthenticationFilter, 
                UsernamePasswordAuthenticationFilter.class
            );
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
```

### 4. Authentication Service (AuthService.java)
```java
@Service
public class AuthService {
    
    public AuthResponse register(RegisterRequest request) {
        // Check email uniqueness
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        
        // Encrypt password
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        
        // Save user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        
        return AuthResponse.builder()
            .userId(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .message("User registered successfully!")
            .build();
    }
    
    public AuthResponse login(LoginRequest request) {
        // Find user
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        
        // Generate token
        String token = jwtUtil.generateToken(user.getEmail());
        
        return AuthResponse.builder()
            .token(token)
            .userId(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .message("Login successful!")
            .build();
    }
}
```

---

## 🔌 Configuration Quick Reference

### application.properties
```properties
# JWT Settings
app.jwt.secret=mySecretKeyForJWTTokenGenerationAndValidationPurposesOnlyChangeInProduction
app.jwt.expiration=86400000                    # 24 hours

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/movieapp
spring.datasource.username=root
spring.datasource.password=alex@123
spring.jpa.hibernate.ddl-auto=update

# Server
server.port=8080
server.servlet.context-path=/api
```

---

## 🧪 Testing Quick Commands

### PowerShell (Windows)
```powershell
# Run test script
.\test_jwt_api.ps1

# Or use individual commands
$body = @{email="test@example.com"; password="pass123"} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
    -Method Post -ContentType "application/json" -Body $body
$token = $response.token
```

### Bash (Linux/Mac)
```bash
# Run test script
./test_jwt_api.sh

# Or use cURL directly
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"pass123"}' | jq -r '.token')

curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/movies
```

---

## ⚡ API Endpoint Cheat Sheet

| Method | Endpoint | Auth | Body | Returns |
|--------|----------|------|------|---------|
| POST | `/api/auth/register` | No | RegisterRequest | AuthResponse |
| POST | `/api/auth/login` | No | LoginRequest | AuthResponse (with token) |
| GET | `/api/movies` | Yes | - | List of movies |
| POST | `/api/movies` | Yes | MovieDTO | Created movie |

### Request Headers for Protected Endpoints
```
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIn0...
```

---

## 🔍 Key Class Relationships

```
┌─────────────────────────────────────────────────────────────┐
│                    USER REGISTRATION                         │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  RegisterRequest                                             │
│       ↓                                                       │
│  AuthController.register()                                   │
│       ↓                                                       │
│  AuthService.register()                                      │
│       ├─ EmailAlreadyExistsException check                  │
│       ├─ PasswordEncoder.encode() → BCrypt                 │
│       ├─ UserRepository.save() → MySQL                     │
│       ↓                                                       │
│  AuthResponse                                                │
│                                                               │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                      USER LOGIN                              │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  LoginRequest                                                │
│       ↓                                                       │
│  AuthController.login()                                      │
│       ↓                                                       │
│  AuthService.login()                                         │
│       ├─ UserRepository.findByEmail()                       │
│       ├─ PasswordEncoder.matches() → BCrypt                 │
│       ├─ JwtUtil.generateToken() → HS512                   │
│       ↓                                                       │
│  AuthResponse (with JWT token)                              │
│                                                               │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│               PROTECTED ENDPOINT REQUEST                      │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  HTTP Request + Authorization Header                        │
│       ↓                                                       │
│  JwtAuthenticationFilter.doFilterInternal()                  │
│       ├─ Extract token from header                          │
│       ├─ JwtUtil.validateToken() → HS512 verify            │
│       ├─ JwtUtil.extractUsername()                          │
│       ├─ UserDetailsServiceImpl.loadUserByUsername()        │
│       ├─ SecurityContext.setAuthentication()                │
│       ↓                                                       │
│  Controller processes with authenticated user               │
│       ↓                                                       │
│  Response                                                    │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 🛡️ Security Layer Summary

| Component | Purpose | Technology |
|-----------|---------|-----------|
| **JwtUtil** | Generate/validate tokens | JJWT + HS512 |
| **PasswordEncoder** | Hash passwords | BCrypt |
| **JwtAuthenticationFilter** | Extract & validate tokens | Spring Security Filter |
| **SecurityConfig** | Configure security | Spring Security DSL |
| **UserDetailsService** | Load user info | Spring Security |
| **GlobalExceptionHandler** | Handle errors | Spring REST advice |

---

## 📊 Token Lifecycle

```
1. USER LOGS IN
   ├─ Credentials validated
   ├─ Token generated with expiration
   └─ Token returned to client

2. CLIENT STORES TOKEN
   ├─ In memory
   ├─ LocalStorage (web)
   └─ SharedPreferences (mobile)

3. CLIENT SENDS REQUESTS
   ├─ Includes token in Authorization header
   ├─ "Bearer <token>"
   └─ Every protected request

4. SERVER VALIDATES TOKEN
   ├─ Signature verification (HS512)
   ├─ Expiration check
   └─ User loading from DB

5. TOKEN EXPIRES
   ├─ Client receives 401 Unauthorized
   ├─ User must login again
   └─ New token generated

OPTIONAL: REFRESH TOKEN (future enhancement)
   ├─ Use refresh token to get new access token
   ├─ Without re-entering credentials
   └─ Extend session without login
```

---

## 🔧 Troubleshooting Quick Tips

| Issue | Cause | Solution |
|-------|-------|----------|
| 401 Unauthorized | Invalid/missing token | Include token in Authorization header |
| 409 Conflict | Email already exists | Register with different email |
| Token expired | Token older than 24h | Login again to get new token |
| Invalid credentials | Wrong password | Check email/password |
| Database error | MySQL not running | Start MySQL service |
| CORS error | Frontend on different origin | Configure CORS in SecurityConfig |

---

## ✅ Implementation Checklist

- [x] JWT token generation (HS512)
- [x] JWT token validation (signature + expiration)
- [x] JWT filter implementation
- [x] User registration with validation
- [x] User login with BCrypt verification
- [x] Password encryption
- [x] Protected endpoint security
- [x] Exception handling
- [x] Spring Security configuration
- [x] Database schema and entities
- [x] JPA repositories
- [x] REST controllers and DTOs
- [x] Configuration properties
- [x] Logging setup
- [x] Documentation and guides
- [x] Test scripts

---

## 📱 Client-Side Integration

### JavaScript/TypeScript Example
```javascript
// Register
const registerResponse = await fetch('/api/auth/register', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    name: 'John Doe',
    email: 'john@example.com',
    password: 'securePassword123'
  })
});

// Login
const loginResponse = await fetch('/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    email: 'john@example.com',
    password: 'securePassword123'
  })
});
const { token } = await loginResponse.json();

// Protected request
const moviesResponse = await fetch('/api/movies', {
  headers: { 'Authorization': `Bearer ${token}` }
});
```

---

## 🚀 Deployment Checklist

- [ ] Change JWT secret to secure random value
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate`
- [ ] Enable HTTPS/SSL
- [ ] Configure environment variables
- [ ] Set logging to WARN level
- [ ] Configure database backups
- [ ] Set up monitoring
- [ ] Enable rate limiting
- [ ] Configure CORS if needed
- [ ] Test all endpoints
- [ ] Load test the application
- [ ] Monitor logs and errors

---

**Last Updated**: March 2026  
**Reference Version**: Spring Boot 3.5.11 + Java 17 + JWT (HS512)

