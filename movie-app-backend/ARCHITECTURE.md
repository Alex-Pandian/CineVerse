# 🏗️ Architecture & Design Documentation

## System Architecture

### High-Level Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                              │
│  (Web Browser / Mobile App / API Client)                         │
└────────────────────────┬────────────────────────────────────────┘
                         │ HTTP/HTTPS Requests
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                        API GATEWAY                               │
│  Server: localhost:8080/api                                     │
│  Protocol: REST (HTTP/HTTPS)                                    │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                   SPRING BOOT APPLICATION                        │
│                      (movie-app-backend)                         │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                 SECURITY LAYER                           │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ JwtAuthenticationFilter                             │ │  │
│  │  │ - Extract JWT from Authorization header            │ │  │
│  │  │ - Validate token signature & expiration            │ │  │
│  │  │ - Load user from database                          │ │  │
│  │  │ - Set SecurityContext with authenticated user      │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                          ▲                                 │  │
│  │                          │                                 │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ SecurityConfig                                      │ │  │
│  │  │ - Enable stateless session management             │ │  │
│  │  │ - CSRF disabled                                    │ │  │
│  │  │ - Register JWT filter                             │ │  │
│  │  │ - Configure public/protected endpoints            │ │  │
│  │  │ - BCryptPasswordEncoder bean                       │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  └──────────────────────────────────────────────────────────┘  │
│                          ▲                                      │
│                          │                                      │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                 PRESENTATION LAYER (Controllers)         │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ AuthController                                     │ │  │
│  │  │ POST /api/auth/register                           │ │  │
│  │  │ POST /api/auth/login                              │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ MovieController                                    │ │  │
│  │  │ GET/POST /api/movies                              │ │  │
│  │  │ (Protected - requires JWT)                        │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ GlobalExceptionHandler                            │ │  │
│  │  │ - Centralized exception handling                  │ │  │
│  │  │ - Consistent error responses                      │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  └──────────────────────────────────────────────────────────┘  │
│                          ▲                                      │
│                          │                                      │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              BUSINESS LOGIC LAYER (Services)             │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ AuthService                                        │ │  │
│  │  │ - register(RegisterRequest)                        │ │  │
│  │  │   • Validate email uniqueness                      │ │  │
│  │  │   • Encrypt password with BCrypt                  │ │  │
│  │  │   • Save user to database                         │ │  │
│  │  │ - login(LoginRequest)                             │ │  │
│  │  │   • Find user by email                            │ │  │
│  │  │   • Verify password with BCrypt                   │ │  │
│  │  │   • Generate JWT token                            │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ MovieService                                       │ │  │
│  │  │ - CRUD operations for movies                       │ │  │
│  │  │ - Business logic and validations                  │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  └──────────────────────────────────────────────────────────┘  │
│                          ▲                                      │
│                          │                                      │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │             DATA ACCESS LAYER (Repositories)            │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ UserRepository                                     │ │  │
│  │  │ - extends JpaRepository<User, Long>               │ │  │
│  │  │ - findByEmail(String email)                       │ │  │
│  │  │ - save(User)                                      │ │  │
│  │  │ - findById(Long)                                  │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ MovieRepository                                    │ │  │
│  │  │ - extends JpaRepository<Movie, Long>              │ │  │
│  │  │ - CRUD operations via Spring Data JPA             │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  └──────────────────────────────────────────────────────────┘  │
│                          ▲                                      │
│                          │ JPA/Hibernate ORM                   │
│                          │                                      │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              UTILITY LAYER                              │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ JwtUtil                                            │ │  │
│  │  │ - generateToken(String username)                  │ │  │
│  │  │ - validateToken(String token)                     │ │  │
│  │  │ - extractUsername(String token)                   │ │  │
│  │  │ - extractClaim(...)                               │ │  │
│  │  │ Algorithm: HMAC-SHA512                            │ │  │
│  │  │ Secret: From application.properties               │ │  │
│  │  │ Expiration: Configurable (24h default)            │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ AppConstants, LoggerFactory, etc.                 │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
                         │
                         │ JDBC/SQL
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    DATABASE LAYER                               │
│  MySQL 8.0+                                                     │
│                                                                 │
│  ┌─────────────────────┐      ┌─────────────────────┐         │
│  │    users table      │      │    movies table     │         │
│  ├─────────────────────┤      ├─────────────────────┤         │
│  │ id (PK)             │      │ id (PK)             │         │
│  │ name                │      │ title               │         │
│  │ email (UNIQUE)      │      │ description         │         │
│  │ password (hashed)   │      │ release_date        │         │
│  │ created_at          │      │ created_at          │         │
│  │ updated_at          │      │ updated_at          │         │
│  └─────────────────────┘      └─────────────────────┘         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Layer-by-Layer Architecture

### 1. Client Layer
- Web browsers
- Mobile applications
- API clients (cURL, Postman, etc.)
- Frontend applications

### 2. API Gateway / Server
- Spring Boot Application
- Listens on port 8080
- Context path: `/api`
- RESTful endpoints

### 3. Security Layer
- **JwtAuthenticationFilter**: Intercepts all requests
- **SecurityConfig**: Defines security rules
- **BCryptPasswordEncoder**: Encrypts passwords
- **UserDetailsService**: Loads user information

### 4. Presentation Layer (Controllers)
- **AuthController**: Handles authentication
- **MovieController**: Handles movie CRUD
- **GlobalExceptionHandler**: Centralized exception handling
- Translates HTTP requests to business operations

### 5. Business Logic Layer (Services)
- **AuthService**: Registration and login logic
- **MovieService**: Movie operations logic
- Data validation
- Business rule enforcement

### 6. Data Access Layer (Repositories)
- **UserRepository**: User data operations
- **MovieRepository**: Movie data operations
- JPA/Spring Data abstractions
- SQL query generation

### 7. Utility Layer
- **JwtUtil**: JWT operations
- **AppConstants**: Application constants
- Helper functions

### 8. Database Layer
- MySQL 8.0+
- User and Movie tables
- Indexes and relationships
- Persistent storage

---

## Component Interaction Diagram

### Registration Flow

```
┌─────┐                    ┌──────────────┐      ┌────────────┐
│User │                    │AuthController│      │AuthService │
└─┬───┘                    └──────┬───────┘      └─────┬──────┘
  │ POST /api/auth/register       │                    │
  ├──────────────────────────────>│                    │
  │                               │ register()         │
  │                               ├───────────────────>│
  │                               │                    │
  │                               │ Check email        │
  │                               │ Encrypt password   │
  │                               │ Save user          │
  │                               │ Return response    │
  │                               │<───────────────────┤
  │ 201 Created + AuthResponse     │                    │
  │<──────────────────────────────┤                    │
  │  {userId, name, email}        │                    │
```

### Login Flow

```
┌─────┐                    ┌──────────────┐      ┌────────────┐      ┌─────────┐
│User │                    │AuthController│      │AuthService │      │JwtUtil  │
└─┬───┘                    └──────┬───────┘      └─────┬──────┘      └────┬────┘
  │ POST /api/auth/login          │                    │                  │
  ├──────────────────────────────>│                    │                  │
  │                               │ login()            │                  │
  │                               ├───────────────────>│                  │
  │                               │                    │                  │
  │                               │ Find user          │                  │
  │                               │ Verify password    │                  │
  │                               │ generateToken()    │                  │
  │                               │                   ├─────────────────>│
  │                               │<─────────────────┤ token             │
  │                               │ Return response    │                  │
  │                               │<───────────────────┤                  │
  │ 200 OK + AuthResponse          │                    │                  │
  │<──────────────────────────────┤                    │                  │
  │  {token, userId, name, email} │                    │                  │
```

### Protected Request Flow

```
┌─────┐                           ┌──────────────────┐      ┌──────────┐
│User │                           │JwtAuthFilter     │      │JwtUtil   │
└─┬───┘                           └────────┬─────────┘      └────┬─────┘
  │ GET /api/movies                        │                     │
  │ Authorization: Bearer <token>          │                     │
  ├──────────────────────────────────────>│                     │
  │                                        │ Extract token       │
  │                                        ├────────────────────>│
  │                                        │                     │
  │                                        │ validateToken()     │
  │                                        │<────────────────────┤
  │                                        │ (valid)             │
  │                                        │                     │
  │                                        │ extractUsername()   │
  │                                        ├────────────────────>│
  │                                        │<────────────────────┤
  │                                        │ email               │
  │                                        │                     │
  │                                        │ Load user from DB   │
  │                                        │ Set SecurityContext │
  │                                        │                     │
  │ Request processed with auth context    │                     │
  │<──────────────────────────────────────┤                     │
  │ 200 OK + Movies                        │                     │
```

---

## Data Model Diagram

### Entity Relationship Diagram (ERD)

```
┌──────────────────────┐        ┌──────────────────────┐
│      users           │        │      movies          │
├──────────────────────┤        ├──────────────────────┤
│ id (PK)              │        │ id (PK)              │
│ name (String)        │        │ title (String)       │
│ email (String, UNIQ) │        │ description (Text)   │
│ password (String)    │        │ release_date (Date)  │
│ created_at (Tstamp)  │        │ created_at (Tstamp)  │
│ updated_at (Tstamp)  │        │ updated_at (Tstamp)  │
└──────────────────────┘        └──────────────────────┘
        ▲                               ▲
        │                               │
        │ One user can have            │ One movie
        │ many reviews                 │ can have
        │ (future enhancement)         │ many reviews
        │                               │ (future enhancement)
        └───────────┬───────────────────┘
                    │
            ┌───────────────────┐
            │  reviews (future)  │
            ├───────────────────┤
            │ id (PK)            │
            │ user_id (FK)       │
            │ movie_id (FK)      │
            │ rating             │
            │ comment            │
            │ created_at         │
            └───────────────────┘
```

---

## Request/Response Cycle

### Complete Request Flow with Authentication

```
1. CLIENT REQUEST
   ┌─────────────────────────────────┐
   │ GET /api/movies                 │
   │ Authorization: Bearer <token>   │
   └─────────────────────────────────┘
              │
              ▼
2. SERVLET FILTER CHAIN
   ┌──────────────────────────────────────┐
   │ JwtAuthenticationFilter              │
   │ • Extract Authorization header       │
   │ • Parse Bearer token                 │
   │ • Validate token with JwtUtil        │
   │ • Load user from database            │
   │ • Set SecurityContext               │
   └──────────────────────────────────────┘
              │
              ▼
3. DISPATCHER SERVLET
   ┌──────────────────────────────────────┐
   │ Route to appropriate controller       │
   │ Based on URL mapping                  │
   └──────────────────────────────────────┘
              │
              ▼
4. CONTROLLER LAYER
   ┌──────────────────────────────────────┐
   │ MovieController.getMovies()          │
   │ • Check @Secured/@PreAuthorize       │
   │ • Call MovieService                  │
   └──────────────────────────────────────┘
              │
              ▼
5. SERVICE LAYER
   ┌──────────────────────────────────────┐
   │ MovieService.getAllMovies()          │
   │ • Apply business logic               │
   │ • Validate data                      │
   │ • Call repositories                  │
   └──────────────────────────────────────┘
              │
              ▼
6. REPOSITORY LAYER
   ┌──────────────────────────────────────┐
   │ MovieRepository.findAll()            │
   │ • Execute JPA query                  │
   │ • Generate SQL                       │
   │ • Query database                     │
   └──────────────────────────────────────┘
              │
              ▼
7. DATABASE
   ┌──────────────────────────────────────┐
   │ SELECT * FROM movies                 │
   │ Execute SQL                          │
   │ Return results                       │
   └──────────────────────────────────────┘
              │
              ▼
8. RESPONSE BUILDING (Reverse flow)
   ┌──────────────────────────────────────┐
   │ Convert entities to DTOs             │
   │ Serialize to JSON                    │
   │ Set HTTP headers                     │
   │ Set HTTP status code                 │
   └──────────────────────────────────────┘
              │
              ▼
9. HTTP RESPONSE
   ┌──────────────────────────────────────┐
   │ HTTP/1.1 200 OK                      │
   │ Content-Type: application/json       │
   │                                      │
   │ [                                    │
   │   {movie objects...}                 │
   │ ]                                    │
   └──────────────────────────────────────┘
```

---

## JWT Token Lifecycle

```
TOKEN GENERATION:
┌─────────────────────────────────────────────────────┐
│ 1. User provides credentials (email + password)     │
│ 2. Password verified with BCrypt                   │
│ 3. JwtUtil.generateToken(email)                    │
│    ├─ Create Claims object                         │
│    ├─ Set subject (email)                          │
│    ├─ Set issuedAt (current time)                 │
│    ├─ Set expiration (current + 24h)              │
│    ├─ Sign with HMAC-SHA512                        │
│    └─ Return Base64 encoded token                  │
│ 4. Token sent to client in response               │
└─────────────────────────────────────────────────────┘
              │
              ▼
TOKEN STORAGE (CLIENT-SIDE):
┌─────────────────────────────────────────────────────┐
│ Client stores token:                                │
│ • Memory (JavaScript)                              │
│ • LocalStorage (Web)                               │
│ • SharedPreferences (Android)                      │
│ • Keychain (iOS)                                   │
└─────────────────────────────────────────────────────┘
              │
              ▼
TOKEN USAGE:
┌─────────────────────────────────────────────────────┐
│ Client includes token in every request:            │
│ Header: Authorization: Bearer <token>              │
└─────────────────────────────────────────────────────┘
              │
              ▼
TOKEN VALIDATION:
┌─────────────────────────────────────────────────────┐
│ 1. JwtAuthenticationFilter extracts token          │
│ 2. JwtUtil.validateToken(token)                    │
│    ├─ Verify signature (ensure not tampered)      │
│    ├─ Check expiration (ensure not expired)       │
│    └─ Parse claims (ensure valid structure)       │
│ 3. If valid:                                       │
│    ├─ Extract username                             │
│    ├─ Load user from database                     │
│    ├─ Create authentication token                 │
│    └─ Set in SecurityContext                      │
│ 4. If invalid:                                     │
│    └─ Return 401 Unauthorized                     │
└─────────────────────────────────────────────────────┘
              │
              ▼
TOKEN EXPIRATION:
┌─────────────────────────────────────────────────────┐
│ 1. Token reaches expiration time (24h)             │
│ 2. JwtUtil detects expiration during validation   │
│ 3. Return 401 Unauthorized to client              │
│ 4. Client must login again to get new token       │
└─────────────────────────────────────────────────────┘
```

---

## Concurrency & Thread Safety

```
Spring Boot Concurrency Model:

┌──────────────────────────────────────────────────────┐
│ Multiple Concurrent Requests                         │
└──────────────────────────────────────────────────────┘
        │  │  │  │  │  │  │  │  │
        ▼  ▼  ▼  ▼  ▼  ▼  ▼  ▼  ▼
┌──────────────────────────────────────────────────────┐
│ Thread Pool (Tomcat Default: 200 threads)           │
│ ┌───────┐ ┌───────┐ ┌───────┐                       │
│ │Thread1│ │Thread2│ │Thread3│ ...                   │
│ └───┬───┘ └───┬───┘ └───┬───┘                       │
│     │         │         │                            │
└─────┼─────────┼─────────┼────────────────────────────┘
      │         │         │
      ▼         ▼         ▼
  ┌───────┐ ┌───────┐ ┌───────┐
  │Request│ │Request│ │Request│
  │   1   │ │   2   │ │   3   │
  └───┬───┘ └───┬───┘ └───┬───┘
      │         │         │
      ▼         ▼         ▼
  ┌───────┐ ┌───────┐ ┌───────┐
  │Filter │ │Filter │ │Filter │
  │   1   │ │   2   │ │   3   │ (Each has own SecurityContext)
  └───┬───┘ └───┬───┘ └───┬───┘
      │         │         │
      ▼         ▼         ▼
  ┌───────┐ ┌───────┐ ┌───────┐
  │Control│ │Control│ │Control│ (Beans are shared, thread-safe)
  │   1   │ │   2   │ │   3   │
  └───────┘ └───────┘ └───────┘

Key Points:
• Each request in separate thread
• SecurityContext is ThreadLocal (thread-safe)
• Beans are singletons (shared but stateless)
• Database connections from pool
• No race conditions for stateless operations
```

---

## Performance Considerations

### Caching Strategy

```
┌──────────────────────────────────────────┐
│ Request with valid JWT                   │
├──────────────────────────────────────────┤
│ 1. Extract token (fast, in-memory)       │
│ 2. Validate signature (HS512, fast)      │
│ 3. Check expiration (fast)               │
│ 4. Extract username (fast)               │
│ 5. Load user from DB (potential cache)   │
│    ├─ First request: Query DB            │
│    └─ Subsequent: From cache/memory      │
│ 6. Create authentication (fast)          │
└──────────────────────────────────────────┘

Optimization Opportunities:
• Cache user details in memory
• Use Redis for distributed caching
• Implement token blacklist for logout
• Use connection pooling for DB
```

---

## Scalability Architecture

```
Load Balancer (Nginx/HAProxy)
        │
    ┌───┼───┬─────┐
    ▼   ▼   ▼     ▼
  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
  │ App Instance 1  │  │ App Instance 2  │  │ App Instance 3  │
  │ Port: 8080      │  │ Port: 8080      │  │ Port: 8080      │
  │ JVM #1          │  │ JVM #2          │  │ JVM #3          │
  └────────┬────────┘  └────────┬────────┘  └────────┬────────┘
           │                    │                    │
           └────────────────────┼────────────────────┘
                                │
                           ┌────▼────┐
                           │Shared DB │
                           │ MySQL    │
                           └──────────┘

Stateless Benefits:
• No session affinity needed
• Load balancer can round-robin
• Can add/remove instances easily
• Horizontal scaling
• Fault tolerance
```

---

## Security Layers

```
┌────────────────────────────────────────────────────┐
│ Layer 1: HTTPS/TLS                                 │
│ - Encrypt data in transit                          │
│ - Prevent token interception                       │
└────────────────────────────────────────────────────┘
           │
           ▼
┌────────────────────────────────────────────────────┐
│ Layer 2: JWT Signature Verification                │
│ - Ensure token not tampered                        │
│ - HMAC-SHA512 with secret key                      │
└────────────────────────────────────────────────────┘
           │
           ▼
┌────────────────────────────────────────────────────┐
│ Layer 3: Token Expiration                          │
│ - Time-limited tokens (24h)                        │
│ - Prevents long-term compromises                   │
└────────────────────────────────────────────────────┘
           │
           ▼
┌────────────────────────────────────────────────────┐
│ Layer 4: Password Encryption                       │
│ - BCrypt hashing (one-way)                         │
│ - Salted hashes                                    │
│ - Strength 10 (2^10 iterations)                    │
└────────────────────────────────────────────────────┘
           │
           ▼
┌────────────────────────────────────────────────────┐
│ Layer 5: Spring Security                           │
│ - CSRF protection disabled (stateless)             │
│ - CORS configuration                               │
│ - Authorization rules                              │
│ - Exception handling                               │
└────────────────────────────────────────────────────┘
           │
           ▼
┌────────────────────────────────────────────────────┐
│ Layer 6: Database Security                         │
│ - Encrypted passwords stored                       │
│ - SQL injection prevention (JPA)                   │
│ - Access controls                                  │
└────────────────────────────────────────────────────┘
```

---

## Development to Production Journey

```
DEVELOPMENT
┌───────────────────────────┐
│ Security relaxed          │
│ Logging: DEBUG            │
│ HTTPS: Optional           │
│ JWT Secret: Default       │
│ DB: Local MySQL           │
└───────────────────────────┘
          │
          ▼
STAGING
┌───────────────────────────┐
│ Security intermediate     │
│ Logging: INFO             │
│ HTTPS: Required           │
│ JWT Secret: Strong random │
│ DB: Staging MySQL         │
│ Tests run                 │
└───────────────────────────┘
          │
          ▼
PRODUCTION
┌───────────────────────────┐
│ Full security             │
│ Logging: WARN             │
│ HTTPS: Required           │
│ JWT Secret: Very secure   │
│ DB: Production cluster    │
│ Monitoring: Active        │
│ Backups: Automated        │
│ Rate limiting: Active     │
│ WAF rules: Applied        │
└───────────────────────────┘
```

---

**This document provides the architectural foundation for understanding the Movie App Backend system.**

Last Updated: March 17, 2026

