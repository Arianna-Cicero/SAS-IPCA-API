# ✅ Checklist de Integração - Mobile App & Web

## 📱 API Pronta para Integração

### ✅ Funcionalidades Implementadas

#### 1. **CORS Configurado** ✅
- Permite requisições de aplicações web
- Headers: `Authorization`, `Content-Type`
- Métodos: `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`
- **Nota:** Atualmente configurado com `anyHost()` para desenvolvimento
- **Produção:** Alterar para hosts específicos no `Application.kt`

#### 2. **Autenticação JWT** ✅
- Bearer token authentication
- Login endpoint: `POST /auth/login`
- Validação de token: `POST /auth/validate`
- Token incluir no header: `Authorization: Bearer {token}`

#### 3. **Documentação API** ✅
- Swagger UI: `http://localhost:8080/swagger`
- OpenAPI Spec: `http://localhost:8080/openapi/documentation.yaml`
- Todos os endpoints documentados

#### 4. **Endpoints Testados (42/42 - 100%)** ✅
- **GET** (11): Listar todos os recursos
- **POST** (11): Criar recursos
- **PUT** (9): Atualizar recursos
- **DELETE** (11): Remover recursos

#### 5. **Tratamento de Erros** ✅
- Respostas JSON padronizadas
- Status codes HTTP corretos
- Mensagens de erro descritivas

#### 6. **Serialização JSON** ✅
- Content-Type: `application/json`
- kotlinx.serialization
- DTOs tipados para requests/responses

#### 7. **Logging** ✅
- Activity logs para auditoria
- Tracking de ações dos utilizadores

---

## 🔧 Configuração para Clientes

### **Base URL**
```
http://localhost:8080
```

### **Autenticação**

1. **Login**
```http
POST /auth/login
Content-Type: application/json

{
  "email": "admin@lojasocial.pt",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6...",
  "email": "admin@lojasocial.pt"
}
```

2. **Usar Token**
```http
GET /courses
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...
```

---

## 📋 Endpoints Disponíveis

### **Cursos**
- `GET /courses` - Listar todos
- `POST /courses` - Criar
- `PUT /courses/{id}` - Atualizar
- `DELETE /courses/{id}` - Remover

### **Beneficiários**
- `GET /beneficiaries` - Listar todos
- `POST /beneficiaries` - Criar
- `PUT /beneficiaries/{id}` - Atualizar
- `DELETE /beneficiaries/{id}` - Remover

### **Colaboradores**
- `GET /collaborators` - Listar todos
- `POST /collaborators` - Criar
- `PUT /collaborators/{id}` - Atualizar
- `DELETE /collaborators/{id}` - Remover

### **Agendamentos**
- `GET /scheduling` - Listar todos
- `POST /scheduling` - Criar
- `PUT /scheduling/{id}` - Atualizar
- `DELETE /scheduling/{id}` - Remover

### **Entregas**
- `GET /deliveries` - Listar todas
- `POST /deliveries` - Criar
- `PUT /deliveries/{id}/status` - Atualizar status
- `DELETE /deliveries/{id}` - Remover

### **Produtos (Goods)**
- `GET /goods` - Listar todos
- `POST /goods` - Criar
- `PUT /goods/{id}` - Atualizar
- `DELETE /goods/{id}` - Remover

### **Doações**
- `GET /donations` - Listar todas
- `POST /donations` - Criar
- `PUT /donations/{id}` - Atualizar
- `DELETE /donations/{id}` - Remover

### **Notícias**
- `GET /news` - Listar todas
- `POST /news` - Criar
- `PUT /news/{id}` - Atualizar
- `DELETE /news/{id}` - Remover

### **Alertas de Expiração**
- `GET /expiration-alerts` - Listar todos
- `POST /expiration-alerts` - Criar
- `PUT /expiration-alerts/{id}/resolve` - Resolver
- `DELETE /expiration-alerts/{id}` - Remover

### **Entidades**
- `GET /entities` - Listar todas
- `POST /entities` - Criar
- `DELETE /entities/{id}` - Remover

### **Activity Logs**
- `GET /activity-logs` - Listar logs
- `POST /activity-logs` - Criar log
- `DELETE /activity-logs/{id}` - Remover

---

## ⚠️ Notas para Produção

### **1. CORS - Configuração de Segurança**
No ficheiro `Application.kt`, alterar:
```kotlin
install(CORS) {
    allowHost("seudominio.com", schemes = listOf("https"))
    allowHost("app.seudominio.com", schemes = listOf("https"))
    // Remover anyHost()
}
```

### **2. HTTPS/SSL**
- Configurar certificado SSL
- Usar proxy reverso (Nginx/Apache) ou
- Configurar SSL direto no Ktor

### **3. Variáveis de Ambiente**
Configurar `.env` para produção:
```env
JDBC_DATABASE_URL=jdbc:postgresql://prod-db:5432/lojasocial
JDBC_DATABASE_USER=prod_user
JDBC_DATABASE_PASSWORD=strong_password
SERVER_PORT=8080
ENVIRONMENT=production
LOG_LEVEL=INFO
JWT_SECRET=your-secure-secret-key
```

### **4. Rate Limiting** (Opcional)
Considerar adicionar rate limiting para prevenir abuso.

### **5. Validação de Inputs**
Já implementado em todos os endpoints via validators.

---

## 🧪 Testar Integração

### **cURL**
```bash
# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@lojasocial.pt","password":"admin123"}'

# Listar cursos (com token)
curl -X GET http://localhost:8080/courses \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### **Postman/Insomnia**
1. Importar OpenAPI spec: `http://localhost:8080/openapi/documentation.yaml`
2. Configurar Bearer Token após login
3. Testar endpoints

### **JavaScript/Fetch**
```javascript
// Login
const login = await fetch('http://localhost:8080/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    email: 'admin@lojasocial.pt',
    password: 'admin123'
  })
});
const { token } = await login.json();

// Usar API
const courses = await fetch('http://localhost:8080/courses', {
  headers: { 'Authorization': `Bearer ${token}` }
});
const data = await courses.json();
```

### **Kotlin/Android**
```kotlin
// Retrofit/OkHttp
val client = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(request)
    }
    .build()
```

---

## ✅ Resumo

**Estado Atual:** 🟢 **PRONTO PARA INTEGRAÇÃO**

- ✅ CORS configurado
- ✅ JWT authentication funcional
- ✅ 42 endpoints testados (100%)
- ✅ Documentação Swagger disponível
- ✅ Tratamento de erros padronizado
- ✅ JSON serialization configurada
- ⚠️ **Produção:** Ajustar CORS, SSL, e variáveis de ambiente

**Próximos Passos:**
1. Mobile/Web: Integrar endpoints usando a documentação Swagger
2. Testar fluxos completos (login → CRUD operations)
3. Ajustar CORS e SSL para produção
