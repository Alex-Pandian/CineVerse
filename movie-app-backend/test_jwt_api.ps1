# JWT Authentication API Testing Script - PowerShell Version
# This script contains example commands to test the JWT authentication implementation

# Configuration
$BaseUrl = "http://localhost:8080/api"
$ContentType = "application/json"

Write-Host "================================" -ForegroundColor Cyan
Write-Host "JWT Authentication API Testing" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

# 1. Register a New User
Write-Host "1. Registering a new user..." -ForegroundColor Blue
Write-Host ""
Write-Host "Request:" -ForegroundColor Green
Write-Host "POST $BaseUrl/auth/register"
Write-Host "Headers: Content-Type: $ContentType"
Write-Host "Body:"
Write-Host ""

$RegisterBody = @{
    name = "John Doe"
    email = "john.doe@example.com"
    password = "securePassword123"
} | ConvertTo-Json

Write-Host $RegisterBody
Write-Host ""
Write-Host "Response:" -ForegroundColor Green

try {
    $RegisterResponse = Invoke-RestMethod -Uri "$BaseUrl/auth/register" `
        -Method Post `
        -ContentType $ContentType `
        -Body $RegisterBody

    Write-Host ($RegisterResponse | ConvertTo-Json) -ForegroundColor White
    Write-Host ""
    Write-Host "✓ User registered successfully" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "-----------------------------------" -ForegroundColor Cyan
Write-Host ""

# 2. Login with the registered user
Write-Host "2. Logging in with the registered user..." -ForegroundColor Blue
Write-Host ""
Write-Host "Request:" -ForegroundColor Green
Write-Host "POST $BaseUrl/auth/login"
Write-Host "Headers: Content-Type: $ContentType"
Write-Host "Body:"
Write-Host ""

$LoginBody = @{
    email = "john.doe@example.com"
    password = "securePassword123"
} | ConvertTo-Json

Write-Host $LoginBody
Write-Host ""
Write-Host "Response:" -ForegroundColor Green

try {
    $LoginResponse = Invoke-RestMethod -Uri "$BaseUrl/auth/login" `
        -Method Post `
        -ContentType $ContentType `
        -Body $LoginBody

    Write-Host ($LoginResponse | ConvertTo-Json) -ForegroundColor White
    Write-Host ""

    # Extract JWT token
    $JwtToken = $LoginResponse.token

    if ($JwtToken) {
        Write-Host "✓ Login successful" -ForegroundColor Green
        Write-Host ""
        Write-Host "JWT Token:" -ForegroundColor Green
        Write-Host $JwtToken
        Write-Host ""
        Write-Host "-----------------------------------" -ForegroundColor Cyan
        Write-Host ""

        # 3. Test Protected Endpoint
        Write-Host "3. Accessing protected endpoint with JWT token..." -ForegroundColor Blue
        Write-Host ""
        Write-Host "Request:" -ForegroundColor Green
        Write-Host "GET $BaseUrl/movies"
        Write-Host "Headers:"
        Write-Host "  Content-Type: $ContentType"
        Write-Host "  Authorization: Bearer `$JwtToken"
        Write-Host ""
        Write-Host "Response:" -ForegroundColor Green

        try {
            $Headers = @{
                "Authorization" = "Bearer $JwtToken"
            }

            $ProtectedResponse = Invoke-RestMethod -Uri "$BaseUrl/movies" `
                -Method Get `
                -ContentType $ContentType `
                -Headers $Headers

            Write-Host ($ProtectedResponse | ConvertTo-Json) -ForegroundColor White
            Write-Host ""
            Write-Host "✓ Protected endpoint accessed successfully" -ForegroundColor Green
        } catch {
            Write-Host "Error accessing protected endpoint: $($_.Exception.Message)" -ForegroundColor Red
        }
    } else {
        Write-Host "Warning: Could not extract JWT token from login response" -ForegroundColor Yellow
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "-----------------------------------" -ForegroundColor Cyan
Write-Host ""

# 4. Test Invalid Credentials
Write-Host "4. Testing with invalid credentials..." -ForegroundColor Blue
Write-Host ""
Write-Host "Request:" -ForegroundColor Green
Write-Host "POST $BaseUrl/auth/login"
Write-Host "Headers: Content-Type: $ContentType"
Write-Host "Body:"
Write-Host ""

$InvalidBody = @{
    email = "john.doe@example.com"
    password = "wrongPassword"
} | ConvertTo-Json

Write-Host $InvalidBody
Write-Host ""
Write-Host "Response:" -ForegroundColor Green

try {
    $InvalidResponse = Invoke-RestMethod -Uri "$BaseUrl/auth/login" `
        -Method Post `
        -ContentType $ContentType `
        -Body $InvalidBody

    Write-Host ($InvalidResponse | ConvertTo-Json) -ForegroundColor White
} catch {
    Write-Host ($_.ErrorDetails.Message | ConvertFrom-Json | ConvertTo-Json) -ForegroundColor Yellow
    Write-Host ""
    Write-Host "✓ Invalid credentials correctly rejected" -ForegroundColor Green
}

Write-Host ""
Write-Host "-----------------------------------" -ForegroundColor Cyan
Write-Host ""

# 5. Test Duplicate Email Registration
Write-Host "5. Testing duplicate email registration..." -ForegroundColor Blue
Write-Host ""
Write-Host "Request:" -ForegroundColor Green
Write-Host "POST $BaseUrl/auth/register"
Write-Host "Headers: Content-Type: $ContentType"
Write-Host "Body:"
Write-Host ""

$DuplicateBody = @{
    name = "Another User"
    email = "john.doe@example.com"
    password = "anotherPassword123"
} | ConvertTo-Json

Write-Host $DuplicateBody
Write-Host ""
Write-Host "Response:" -ForegroundColor Green

try {
    $DuplicateResponse = Invoke-RestMethod -Uri "$BaseUrl/auth/register" `
        -Method Post `
        -ContentType $ContentType `
        -Body $DuplicateBody

    Write-Host ($DuplicateResponse | ConvertTo-Json) -ForegroundColor White
} catch {
    Write-Host ($_.ErrorDetails.Message | ConvertFrom-Json | ConvertTo-Json) -ForegroundColor Yellow
    Write-Host ""
    Write-Host "✓ Duplicate email correctly rejected" -ForegroundColor Green
}

Write-Host ""
Write-Host "================================" -ForegroundColor Cyan
Write-Host "Testing Complete!" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan

