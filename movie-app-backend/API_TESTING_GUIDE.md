# Movie App Backend - API Test Guide

## Problem Fixed
The registration endpoint was not working because:
1. **Root Cause**: `server.servlet.context-path=/api` was set in `application.properties`
2. **Issue**: Controllers already have `/api` in their `@RequestMapping`, causing double `/api/api` paths
3. **Solution**: Removed the context-path configuration

## Correct Endpoints (After Fix)

### Authentication Endpoints (Public - No JWT Required)
```
POST http://localhost:8080/api/auth/register
POST http://localhost:8080/api/auth/login
```

### Movie Endpoints (Public - No JWT Required)
```
GET http://localhost:8080/api/movies/search?name=Inception&year=2010&type=movie&page=1
GET http://localhost:8080/api/movies/{imdbId}
GET http://localhost:8080/api/trending?limit=10
GET http://localhost:8080/api/recommendations?genre=Action&limit=10
```

### Protected Endpoints (JWT Required)
```
POST http://localhost:8080/api/favorites/{imdbId}?title=MovieTitle&poster=URL
GET http://localhost:8080/api/favorites
GET http://localhost:8080/api/favorites/{imdbId}/check
DELETE http://localhost:8080/api/favorites/{imdbId}

POST http://localhost:8080/api/history?query=Inception
GET http://localhost:8080/api/history?limit=50
```

## Test Registration

### Using cURL
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Expected Response (201 Created)
```json
{
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "message": "User registered successfully"
}
```

## Test Login

### Using cURL
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Expected Response (200 OK)
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "message": "Login successful"
}
```

## Using JWT Token for Protected Endpoints

Once you have a token from login, use it in the Authorization header:

```bash
curl -X GET http://localhost:8080/api/favorites \
  -H "Authorization: Bearer <your_token_here>"
```

## Troubleshooting

### Issue: 404 Not Found
- Make sure the server is running on port 8080
- Check that you're using `/api/auth/register` not just `/auth/register`

### Issue: 400 Bad Request
- Verify JSON body is valid
- Check all required fields are present (name, email, password for register)

### Issue: 409 Conflict
- Email already registered
- Use a different email address

### Issue: 401 Unauthorized
- Token missing or expired for protected endpoints
- Get a new token by logging in again

## File Modified
- `application.properties`: Removed `server.servlet.context-path=/api` line

