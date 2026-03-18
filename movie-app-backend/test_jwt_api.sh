#!/bin/bash
# JWT Authentication API Testing Script
# This script contains example cURL commands to test the JWT authentication implementation

# Configuration
BASE_URL="http://localhost:8080/api"
CONTENT_TYPE="Content-Type: application/json"

echo "================================"
echo "JWT Authentication API Testing"
echo "================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 1. Register a New User
echo -e "${BLUE}1. Registering a new user...${NC}"
echo ""
echo "Request:"
echo "POST $BASE_URL/auth/register"
echo "Headers: $CONTENT_TYPE"
echo "Body:"
echo ""

REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "securePassword123"
  }')

echo "{
  \"name\": \"John Doe\",
  \"email\": \"john.doe@example.com\",
  \"password\": \"securePassword123\"
}"
echo ""
echo "Response:"
echo "$REGISTER_RESPONSE" | jq '.' 2>/dev/null || echo "$REGISTER_RESPONSE"
echo ""
echo -e "${GREEN}✓ User registered successfully${NC}"
echo ""
echo "-----------------------------------"
echo ""

# 2. Login with the registered user
echo -e "${BLUE}2. Logging in with the registered user...${NC}"
echo ""
echo "Request:"
echo "POST $BASE_URL/auth/login"
echo "Headers: $CONTENT_TYPE"
echo "Body:"
echo ""

LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "$CONTENT_TYPE" \
  -d '{
    "email": "john.doe@example.com",
    "password": "securePassword123"
  }')

echo "{
  \"email\": \"john.doe@example.com\",
  \"password\": \"securePassword123\"
}"
echo ""
echo "Response:"
echo "$LOGIN_RESPONSE" | jq '.' 2>/dev/null || echo "$LOGIN_RESPONSE"
echo ""

# Extract JWT token from response
JWT_TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token' 2>/dev/null)

if [ ! -z "$JWT_TOKEN" ] && [ "$JWT_TOKEN" != "null" ]; then
    echo -e "${GREEN}✓ Login successful${NC}"
    echo ""
    echo "JWT Token:"
    echo "$JWT_TOKEN"
    echo ""
    echo "-----------------------------------"
    echo ""

    # 3. Decode JWT Token (optional - requires jq and base64)
    echo -e "${BLUE}3. Decoding JWT Token...${NC}"
    echo ""

    # Extract header
    HEADER=$(echo "$JWT_TOKEN" | cut -d'.' -f1)
    PAYLOAD=$(echo "$JWT_TOKEN" | cut -d'.' -f2)

    # Decode (add padding if needed)
    PAYLOAD_WITH_PADDING="${PAYLOAD}=="

    echo "Token Header + Payload (decoded):"
    echo ""
    echo "Payload:"
    echo "$PAYLOAD_WITH_PADDING" | base64 -d 2>/dev/null | jq '.' 2>/dev/null || echo "Install jq for better formatting"
    echo ""
    echo "-----------------------------------"
    echo ""

    # 4. Test Protected Endpoint
    echo -e "${BLUE}4. Accessing protected endpoint with JWT token...${NC}"
    echo ""
    echo "Request:"
    echo "GET $BASE_URL/movies"
    echo "Headers:"
    echo "  $CONTENT_TYPE"
    echo "  Authorization: Bearer \$JWT_TOKEN"
    echo ""
    echo "Response:"

    PROTECTED_RESPONSE=$(curl -s -X GET "$BASE_URL/movies" \
      -H "$CONTENT_TYPE" \
      -H "Authorization: Bearer $JWT_TOKEN")

    echo "$PROTECTED_RESPONSE" | jq '.' 2>/dev/null || echo "$PROTECTED_RESPONSE"
    echo ""
    echo -e "${GREEN}✓ Protected endpoint accessed successfully${NC}"
    echo ""
else
    echo -e "${BLUE}Warning: Could not extract JWT token from login response${NC}"
    echo "Login response was:"
    echo "$LOGIN_RESPONSE"
fi

echo "-----------------------------------"
echo ""

# 5. Test Invalid Credentials
echo -e "${BLUE}5. Testing with invalid credentials...${NC}"
echo ""
echo "Request:"
echo "POST $BASE_URL/auth/login"
echo "Headers: $CONTENT_TYPE"
echo "Body:"
echo ""

echo "{
  \"email\": \"john.doe@example.com\",
  \"password\": \"wrongPassword\"
}"
echo ""
echo "Response:"

INVALID_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "$CONTENT_TYPE" \
  -d '{
    "email": "john.doe@example.com",
    "password": "wrongPassword"
  }')

echo "$INVALID_RESPONSE" | jq '.' 2>/dev/null || echo "$INVALID_RESPONSE"
echo ""
echo -e "${GREEN}✓ Invalid credentials correctly rejected${NC}"
echo ""

echo "-----------------------------------"
echo ""

# 6. Test Duplicate Email Registration
echo -e "${BLUE}6. Testing duplicate email registration...${NC}"
echo ""
echo "Request:"
echo "POST $BASE_URL/auth/register"
echo "Headers: $CONTENT_TYPE"
echo "Body:"
echo ""

echo "{
  \"name\": \"Another User\",
  \"email\": \"john.doe@example.com\",
  \"password\": \"anotherPassword123\"
}"
echo ""
echo "Response:"

DUPLICATE_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Another User",
    "email": "john.doe@example.com",
    "password": "anotherPassword123"
  }')

echo "$DUPLICATE_RESPONSE" | jq '.' 2>/dev/null || echo "$DUPLICATE_RESPONSE"
echo ""
echo -e "${GREEN}✓ Duplicate email correctly rejected${NC}"
echo ""

echo "================================"
echo "Testing Complete!"
echo "================================"

