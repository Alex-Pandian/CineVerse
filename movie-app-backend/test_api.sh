#!/bin/bash

# Movie App Backend API Test Script
# Test the registration and login endpoints

BASE_URL="http://localhost:8080"

echo "========================================="
echo "Movie App Backend - API Test"
echo "========================================="
echo ""

# Test 1: Register a new user
echo "TEST 1: Register a new user"
echo "Endpoint: POST $BASE_URL/api/auth/register"
echo ""

curl -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }' \
  -v

echo ""
echo ""
echo "========================================="
echo "TEST 2: Login with the registered user"
echo "Endpoint: POST $BASE_URL/api/auth/login"
echo ""

curl -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }' \
  -v

echo ""
echo ""
echo "========================================="
echo "TEST 3: Search for a movie (public endpoint)"
echo "Endpoint: GET $BASE_URL/api/movies/search"
echo ""

curl -X GET "$BASE_URL/api/movies/search?name=Inception" \
  -H "Content-Type: application/json" \
  -v

echo ""
echo "========================================="
echo "Tests completed!"
echo "========================================="

