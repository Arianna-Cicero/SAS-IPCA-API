# Teste simples do endpoint de mensagens

$baseUrl = "http://localhost:8080"

Write-Host "=== Testando endpoint /messages ===" -ForegroundColor Cyan
Write-Host ""

# 1. POST público (sem autenticação)
Write-Host "[1] POST /messages (público)..." -NoNewline
try {
    $body = @{
        name = "João Silva"
        email = "joao@example.com"
        category = "SUGESTAO"
        message = "Esta é uma mensagem de teste do utilizador web"
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "$baseUrl/messages" -Method POST `
        -ContentType "application/json" -Body $body -ErrorAction Stop
    
    Write-Host " V OK" -ForegroundColor Green
    Write-Host "  ID criado: $($response.id)" -ForegroundColor Gray
    $messageId = $response.id
} catch {
    Write-Host " X ERRO" -ForegroundColor Red
    Write-Host "  $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 2. Login para testar GET
Write-Host ""
Write-Host "[2] Login..." -NoNewline
try {
    $loginBody = @{
        email = "admin@lojasocial.pt"
        password = "admin123"
    } | ConvertTo-Json
    
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST `
        -ContentType "application/json" -Body $loginBody -ErrorAction Stop
    
    $token = $loginResponse.token
    Write-Host " V OK" -ForegroundColor Green
} catch {
    Write-Host " X ERRO" -ForegroundColor Red
    Write-Host "  $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

# 3. GET /messages (protegido)
Write-Host ""
Write-Host "[3] GET /messages (protegido)..." -NoNewline
try {
    $messages = Invoke-RestMethod -Uri "$baseUrl/messages" -Method GET `
        -Headers $headers -ErrorAction Stop
    
    Write-Host " V OK" -ForegroundColor Green
    Write-Host "  Total de mensagens: $($messages.Count)" -ForegroundColor Gray
} catch {
    Write-Host " X ERRO" -ForegroundColor Red
    Write-Host "  $($_.Exception.Message)" -ForegroundColor Red
}

# 4. GET /messages/{id} (protegido)
Write-Host ""
Write-Host "[4] GET /messages/$messageId (protegido)..." -NoNewline
try {
    $message = Invoke-RestMethod -Uri "$baseUrl/messages/$messageId" -Method GET `
        -Headers $headers -ErrorAction Stop
    
    Write-Host " V OK" -ForegroundColor Green
    Write-Host "  Nome: $($message.name)" -ForegroundColor Gray
    Write-Host "  Email: $($message.email)" -ForegroundColor Gray
    Write-Host "  Categoria: $($message.category)" -ForegroundColor Gray
} catch {
    Write-Host " X ERRO" -ForegroundColor Red
    Write-Host "  $($_.Exception.Message)" -ForegroundColor Red
}

# 5. DELETE /messages/{id} (protegido)
Write-Host ""
Write-Host "[5] DELETE /messages/$messageId (protegido)..." -NoNewline
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/messages/$messageId" -Method DELETE `
        -Headers $headers -ErrorAction Stop
    
    Write-Host " V OK" -ForegroundColor Green
} catch {
    Write-Host " X ERRO" -ForegroundColor Red
    Write-Host "  $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "=== Teste completo! ===" -ForegroundColor Green
