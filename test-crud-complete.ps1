# Script completo para testar GET, POST, PUT e DELETE de todos os endpoints
$baseUrl = "http://localhost:8080"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Teste Completo - Todos os Endpoints  " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Fazer login primeiro
Write-Host "[1] Fazendo login..." -ForegroundColor Yellow
$loginBody = @{
    email = "admin@lojasocial.pt"
    password = "admin123"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST `
        -ContentType "application/json" -Body $loginBody -ErrorAction Stop
    
    # Tentar diferentes campos de token
    if ($loginResponse.token) {
        $token = $loginResponse.token
    } elseif ($loginResponse.sessionToken) {
        $token = $loginResponse.sessionToken
    } elseif ($loginResponse.access_token) {
        $token = $loginResponse.access_token
    } else {
        Write-Host "X Token nao encontrado na resposta" -ForegroundColor Red
        Write-Host "Resposta: $($loginResponse | ConvertTo-Json)" -ForegroundColor Gray
        exit 1
    }
    
    Write-Host "V Login bem-sucedido!" -ForegroundColor Green
    Write-Host "  Token: $($token.Substring(0, [Math]::Min(30, $token.Length)))..." -ForegroundColor Gray
} catch {
    Write-Host "X Erro no login: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

# Validar token
Write-Host ""
Write-Host "[2] Validando token..." -ForegroundColor Yellow
try {
    $validateResponse = Invoke-RestMethod -Uri "$baseUrl/auth/validate" -Method POST `
        -Headers $headers -ErrorAction Stop
    if ($validateResponse.valid) {
        Write-Host "V Token valido!" -ForegroundColor Green
    } else {
        Write-Host "X Token invalido!" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "X Erro ao validar token: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

$successGet = 0
$successPost = 0
$successPut = 0
$successDelete = 0
$failedGet = 0
$failedPost = 0
$failedPut = 0
$failedDelete = 0
$errors = @()
$createdIds = @{}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  PARTE 1: GET (Listar recursos)       " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Lista de endpoints para testar GET
$getEndpoints = @(
    "/courses",
    "/beneficiaries",
    "/collaborators",
    "/donations",
    "/goods",
    "/scheduling",
    "/deliveries",
    "/expiration-alerts",
    "/news",
    "/activity-logs",
    "/entities",
    "/messages"
)

$getCount = 1
foreach ($endpoint in $getEndpoints) {
    Write-Host "  [$getCount/12] GET $endpoint..." -NoNewline
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$endpoint" -Method GET `
            -Headers $headers -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successGet++
    } catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        Write-Host " X ERRO ($statusCode)" -ForegroundColor Red
        $errors += "GET $endpoint : $($_.Exception.Message)"
        $failedGet++
    }
    $getCount++
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  PARTE 2: POST (Criar recursos)       " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# POST /courses
Write-Host "  [1/11] POST /courses..." -NoNewline
try {
    $body = @{ name = "Curso Teste CRUD $(Get-Date -Format 'HHmmss')" } | ConvertTo-Json
    $response = Invoke-RestMethod -Uri "$baseUrl/courses" -Method POST -Headers $headers -Body $body -ErrorAction Stop
    Write-Host " V OK" -ForegroundColor Green
    $successPost++
    if ($response.id) { $createdIds['course'] = $response.id }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "POST /courses: $($_.Exception.Message)"
    $failedPost++
}

# POST /collaborators
Write-Host "  [2/11] POST /collaborators..." -NoNewline
try {
    $body = @{
        name = "Colaborador Teste CRUD"
        email = "crud$(Get-Date -Format 'HHmmss')@test.pt"
        password = "Test123!"
        profile = "STAFF"
    } | ConvertTo-Json
    $response = Invoke-RestMethod -Uri "$baseUrl/collaborators" -Method POST -Headers $headers -Body $body -ErrorAction Stop
    Write-Host " V OK" -ForegroundColor Green
    $successPost++
    if ($response.id) { $createdIds['collaborator'] = $response.id }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "POST /collaborators: $($_.Exception.Message)"
    $failedPost++
}

# POST /donations
Write-Host "  [3/11] POST /donations..." -NoNewline
try {
    $body = @{
        nameDonor = "Doador Teste CRUD"
        type = "ALIMENTOS"
        dateDonor = (Get-Date -Format "yyyy-MM-dd")
        description = "Doacao de teste para CRUD"
    } | ConvertTo-Json
    $response = Invoke-RestMethod -Uri "$baseUrl/donations" -Method POST -Headers $headers -Body $body -ErrorAction Stop
    Write-Host " V OK" -ForegroundColor Green
    $successPost++
    if ($response.id) { $createdIds['donation'] = $response.id }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "POST /donations: $($_.Exception.Message)"
    $failedPost++
}

# POST /news
Write-Host "  [4/11] POST /news..." -NoNewline
try {
    $body = @{
        title = "Noticia Teste CRUD"
        content = "Conteudo da noticia de teste"
        datePublication = (Get-Date -Format "yyyy-MM-dd")
        campaignId = 1
    } | ConvertTo-Json
    $response = Invoke-RestMethod -Uri "$baseUrl/news" -Method POST -Headers $headers -Body $body -ErrorAction Stop
    Write-Host " V OK" -ForegroundColor Green
    $successPost++
    if ($response.id) { $createdIds['news'] = $response.id }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "POST /news: $($_.Exception.Message)"
    $failedPost++
}

# POST /entities
Write-Host "  [5/11] POST /entities..." -NoNewline
try {
    $body = @{ name = "TEST_ENTITY_$(Get-Date -Format 'HHmmss')" } | ConvertTo-Json
    $response = Invoke-RestMethod -Uri "$baseUrl/entities" -Method POST -Headers $headers -Body $body -ErrorAction Stop
    Write-Host " V OK" -ForegroundColor Green
    $successPost++
    if ($response.id) { $createdIds['entity'] = $response.id }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "POST /entities: $($_.Exception.Message)"
    $failedPost++
}

# POST /beneficiaries
Write-Host "  [6/11] POST /beneficiaries..." -NoNewline
try {
    if ($createdIds['course']) {
        $body = @{
            name = "Beneficiario Teste CRUD"
            studentNumber = [int](Get-Date -Format "HHmmss")
            email = "benef$(Get-Date -Format 'HHmmss')@aluno.ipca.pt"
            telephone = "912345678"
            idCourse = $createdIds['course']
        } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/beneficiaries" -Method POST -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPost++
        if ($response.id) { $createdIds['beneficiary'] = $response.id }
    } else {
        Write-Host " X SKIP (sem curso)" -ForegroundColor Yellow
        $failedPost++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "POST /beneficiaries: $($_.Exception.Message)"
    $failedPost++
}

# POST /goods
Write-Host "  [7/11] POST /goods..." -NoNewline
try {
    if ($createdIds['donation']) {
        $body = @{
            name = "Produto Teste CRUD"
            category = "ALIMENTOS"
            quantity = 10
            intake = (Get-Date -Format "yyyy-MM-dd")
            dateValidity = (Get-Date).AddMonths(6).ToString("yyyy-MM-dd")
            donationId = $createdIds['donation'].ToString()
        } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/goods" -Method POST -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPost++
        if ($response.id) { $createdIds['good'] = $response.id }
    } else {
        Write-Host " X SKIP (sem doacao)" -ForegroundColor Yellow
        $failedPost++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "POST /goods: $($_.Exception.Message)"
    $failedPost++
}

# POST /scheduling
Write-Host "  [8/11] POST /scheduling..." -NoNewline
try {
    if ($createdIds['beneficiary'] -and $createdIds['collaborator']) {
        $body = @{
            beneficiaryId = [int]$createdIds['beneficiary']
            collaboratorId = $createdIds['collaborator'].ToString()
            dateDelivery = (Get-Date).AddDays(7).ToString("yyyy-MM-dd")
        } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/scheduling" -Method POST -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPost++
        if ($response.id) { $createdIds['scheduling'] = $response.id }
    } else {
        Write-Host " X SKIP (sem beneficiario/colaborador)" -ForegroundColor Yellow
        $failedPost++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "POST /scheduling: $($_.Exception.Message)"
    $failedPost++
}

# POST /deliveries
Write-Host "  [9/11] POST /deliveries..." -NoNewline
try {
    if ($createdIds['scheduling']) {
        $body = @{
            schedulingId = $createdIds['scheduling']
            dateDelivery = (Get-Date).AddDays(7).ToString("yyyy-MM-dd")
        } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/deliveries" -Method POST -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPost++
        if ($response.id) { $createdIds['delivery'] = $response.id }
    } else {
        Write-Host " X SKIP (sem scheduling)" -ForegroundColor Yellow
        $failedPost++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "POST /deliveries: $($_.Exception.Message)"
    $failedPost++
}

# POST /expiration-alerts
Write-Host " [10/11] POST /expiration-alerts..." -NoNewline
try {
    if ($createdIds['good']) {
        $body = @{
            goodId = $createdIds['good']
            dateAlert = (Get-Date).AddDays(5).ToString("yyyy-MM-dd")
            remainingDays = 5
        } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/expiration-alerts" -Method POST -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPost++
        if ($response.id) { $createdIds['alert'] = $response.id }
    } else {
        Write-Host " X SKIP (sem produto)" -ForegroundColor Yellow
        $failedPost++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "POST /expiration-alerts: $($_.Exception.Message)"
    $failedPost++
}

# POST /activity-logs
Write-Host " [11/12] POST /activity-logs..." -NoNewline
try {
    if ($createdIds['collaborator'] -and $createdIds['entity']) {
        $body = @{
            collaboratorId = $createdIds['collaborator'].ToString()
            entityId = $createdIds['entity']
            action = "CREATE"
            recordId = "test-crud"
        } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/activity-logs" -Method POST -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPost++
        if ($response.id) { $createdIds['activityLog'] = $response.id }
    } else {
        Write-Host " X SKIP (sem colaborador/entidade)" -ForegroundColor Yellow
        $failedPost++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "POST /activity-logs: $($_.Exception.Message)"
    $failedPost++
}

# POST /messages (público - sem headers de autenticação)
Write-Host " [12/12] POST /messages..." -NoNewline
try {
    $publicHeaders = @{
        "Content-Type" = "application/json"
    }
    $body = @{
        name = "Teste CRUD"
        email = "teste@example.com"
        category = "SUGESTAO"
        message = "Esta e uma mensagem de teste do CRUD"
    } | ConvertTo-Json
    $response = Invoke-RestMethod -Uri "$baseUrl/messages" -Method POST -Headers $publicHeaders -Body $body -ErrorAction Stop
    Write-Host " V OK" -ForegroundColor Green
    $successPost++
    if ($response.id) { $createdIds['message'] = $response.id }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "POST /messages: $($_.Exception.Message)"
    $failedPost++
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  PARTE 2: PUT (Atualizar recursos)    " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# PUT /courses/{id}
Write-Host "  [1/10] PUT /courses/{id}..." -NoNewline
try {
    if ($createdIds['course']) {
        $body = @{ name = "Curso Atualizado CRUD" } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/courses/$($createdIds['course'])" -Method PUT -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPut++
    } else {
        Write-Host " X SKIP" -ForegroundColor Yellow
        $failedPut++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "PUT /courses: $($_.Exception.Message)"
    $failedPut++
}

# PUT /collaborators/{id}
Write-Host "  [2/10] PUT /collaborators/{id}..." -NoNewline
try {
    if ($createdIds['collaborator']) {
        $body = @{
            name = "Colaborador Atualizado"
            email = "crud$(Get-Date -Format 'HHmmss')@test.pt"
            profile = "ADMIN"
            active = $true
        } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/collaborators/$($createdIds['collaborator'])" -Method PUT -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPut++
    } else {
        Write-Host " X SKIP" -ForegroundColor Yellow
        $failedPut++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "PUT /collaborators: $($_.Exception.Message)"
    $failedPut++
}

# PUT /donations/{id}
Write-Host "  [3/10] PUT /donations/{id}..." -NoNewline
try {
    if ($createdIds['donation']) {
        $body = @{
            nameDonor = "Doador Atualizado"
            type = "ALIMENTOS"
            dateDonor = (Get-Date -Format "yyyy-MM-dd")
            description = "Doacao atualizada"
        } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/donations/$($createdIds['donation'])" -Method PUT -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPut++
    } else {
        Write-Host " X SKIP" -ForegroundColor Yellow
        $failedPut++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "PUT /donations: $($_.Exception.Message)"
    $failedPut++
}

# PUT /news/{id}
Write-Host "  [4/10] PUT /news/{id}..." -NoNewline
try {
    if ($createdIds['news']) {
        $body = @{
            title = "Noticia Atualizada"
            content = "Conteudo atualizado"
            datePublication = (Get-Date -Format "yyyy-MM-dd")
        } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/news/$($createdIds['news'])" -Method PUT -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPut++
    } else {
        Write-Host " X SKIP" -ForegroundColor Yellow
        $failedPut++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "PUT /news: $($_.Exception.Message)"
    $failedPut++
}

# PUT /beneficiaries/{id}
Write-Host "  [5/10] PUT /beneficiaries/{id}..." -NoNewline
try {
    if ($createdIds['beneficiary'] -and $createdIds['course']) {
        $body = @{
            name = "Beneficiario Atualizado"
            email = "benef_update@aluno.ipca.pt"
            telephone = "923456789"
            idCourse = $createdIds['course']
        } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/beneficiaries/$($createdIds['beneficiary'])" -Method PUT -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPut++
    } else {
        Write-Host " X SKIP" -ForegroundColor Yellow
        $failedPut++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "PUT /beneficiaries: $($_.Exception.Message)"
    $failedPut++
}

# PUT /goods/{id}
Write-Host "  [6/10] PUT /goods/{id}..." -NoNewline
try {
    if ($createdIds['good']) {
        $body = @{
            name = "Produto Atualizado"
            category = "ALIMENTOS"
            quantity = 20
            dateValidity = (Get-Date).AddMonths(6).ToString("yyyy-MM-dd")
        } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/goods/$($createdIds['good'])" -Method PUT -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPut++
    } else {
        Write-Host " X SKIP" -ForegroundColor Yellow
        $failedPut++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "PUT /goods: $($_.Exception.Message)"
    $failedPut++
}

# PUT /scheduling/{id}
Write-Host "  [7/10] PUT /scheduling/{id}..." -NoNewline
try {
    if ($createdIds['scheduling']) {
        $body = @{
            dateDelivery = (Get-Date).AddDays(10).ToString("yyyy-MM-dd")
            status = "PENDING"
        } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/scheduling/$($createdIds['scheduling'])" -Method PUT -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPut++
    } else {
        Write-Host " X SKIP" -ForegroundColor Yellow
        $failedPut++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "PUT /scheduling: $($_.Exception.Message)"
    $failedPut++
}

# PUT /deliveries/{id}/status
Write-Host "  [8/10] PUT /deliveries/{id}/status..." -NoNewline
try {
    if ($createdIds['delivery']) {
        $body = @{ status = "COMPLETED" } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/deliveries/$($createdIds['delivery'])/status" -Method PUT -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPut++
    } else {
        Write-Host " X SKIP" -ForegroundColor Yellow
        $failedPut++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "PUT /deliveries/status: $($_.Exception.Message)"
    $failedPut++
}

# PUT /expiration-alerts/{id}/resolve
Write-Host "  [9/10] PUT /expiration-alerts/{id}/resolve..." -NoNewline
try {
    if ($createdIds['alert']) {
        $body = @{ resolved = $true } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri "$baseUrl/expiration-alerts/$($createdIds['alert'])/resolve" -Method PUT -Headers $headers -Body $body -ErrorAction Stop
        Write-Host " V OK" -ForegroundColor Green
        $successPut++
    } else {
        Write-Host " X SKIP" -ForegroundColor Yellow
        $failedPut++
    }
} catch {
    Write-Host " X ERRO: $($_.Exception.Message)" -ForegroundColor Red
    $errors += "PUT /expiration-alerts/resolve: $($_.Exception.Message)"
    $failedPut++
}

Write-Host " [10/10] Outros endpoints PUT nao aplicaveis" -ForegroundColor Gray

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  PARTE 3: DELETE (Remover recursos)   " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# DELETE na ordem inversa das dependencias
$deleteOrder = @(
    @{ name = "message"; endpoint = "messages"; id = $createdIds['message'] }
    @{ name = "activityLog"; endpoint = "activity-logs"; id = $createdIds['activityLog'] }
    @{ name = "alert"; endpoint = "expiration-alerts"; id = $createdIds['alert'] }
    @{ name = "delivery"; endpoint = "deliveries"; id = $createdIds['delivery'] }
    @{ name = "scheduling"; endpoint = "scheduling"; id = $createdIds['scheduling'] }
    @{ name = "good"; endpoint = "goods"; id = $createdIds['good'] }
    @{ name = "beneficiary"; endpoint = "beneficiaries"; id = $createdIds['beneficiary'] }
    @{ name = "news"; endpoint = "news"; id = $createdIds['news'] }
    @{ name = "entity"; endpoint = "entities"; id = $createdIds['entity'] }
    @{ name = "donation"; endpoint = "donations"; id = $createdIds['donation'] }
    @{ name = "collaborator"; endpoint = "collaborators"; id = $createdIds['collaborator'] }
    @{ name = "course"; endpoint = "courses"; id = $createdIds['course'] }
)

$deleteCount = 1
foreach ($item in $deleteOrder) {
    Write-Host "  [$deleteCount/12] DELETE /$($item.endpoint)/{id}..." -NoNewline
    try {
        if ($item.id) {
            # Tratamento especial para courses - pode ter beneficiarios dependentes
            if ($item.name -eq "course" -and $createdIds['beneficiary']) {
                # Certifica que beneficiario foi apagado
                Start-Sleep -Milliseconds 100
            }
            $response = Invoke-RestMethod -Uri "$baseUrl/$($item.endpoint)/$($item.id)" -Method DELETE -Headers $headers -ErrorAction Stop
            Write-Host " V OK" -ForegroundColor Green
            $successDelete++
        } else {
            Write-Host " X SKIP (nao criado)" -ForegroundColor Yellow
            $failedDelete++
        }
    } catch {
        $statusCode = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.value__ } else { "N/A" }
        Write-Host " X ERRO ($statusCode)" -ForegroundColor Red
        $errors += "DELETE /$($item.endpoint): $($_.Exception.Message)"
        $failedDelete++
    }
    $deleteCount++
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  RESUMO FINAL                         " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "GET (Listar):" -ForegroundColor Yellow
Write-Host "  Sucesso: $successGet" -ForegroundColor Green
Write-Host "  Falhas:  $failedGet" -ForegroundColor $(if ($failedGet -eq 0) { "Green" } else { "Red" })
Write-Host ""
Write-Host "POST (Create):" -ForegroundColor Yellow
Write-Host "  Sucesso: $successPost" -ForegroundColor Green
Write-Host "  Falhas:  $failedPost" -ForegroundColor $(if ($failedPost -eq 0) { "Green" } else { "Red" })
Write-Host ""
Write-Host "PUT (Update):" -ForegroundColor Yellow
Write-Host "  Sucesso: $successPut" -ForegroundColor Green
Write-Host "  Falhas:  $failedPut" -ForegroundColor $(if ($failedPut -eq 0) { "Green" } else { "Red" })
Write-Host ""
Write-Host "DELETE (Remove):" -ForegroundColor Yellow
Write-Host "  Sucesso: $successDelete" -ForegroundColor Green
Write-Host "  Falhas:  $failedDelete" -ForegroundColor $(if ($failedDelete -eq 0) { "Green" } else { "Red" })
Write-Host ""
$totalSuccess = $successGet + $successPost + $successPut + $successDelete
$totalFailed = $failedGet + $failedPost + $failedPut + $failedDelete
$totalTests = $totalSuccess + $totalFailed
Write-Host "TOTAL GERAL:" -ForegroundColor Cyan
Write-Host "  Testes:  $totalTests" -ForegroundColor White
Write-Host "  Sucesso: $totalSuccess ($([math]::Round(($totalSuccess/$totalTests)*100, 1))%)" -ForegroundColor Green
Write-Host "  Falhas:  $totalFailed ($([math]::Round(($totalFailed/$totalTests)*100, 1))%)" -ForegroundColor $(if ($totalFailed -eq 0) { "Green" } else { "Red" })

if ($errors.Count -gt 0) {
    Write-Host ""
    Write-Host "Erros detalhados:" -ForegroundColor Yellow
    foreach ($error in $errors) {
        Write-Host "  - $error" -ForegroundColor Red
    }
}
Write-Host ""
