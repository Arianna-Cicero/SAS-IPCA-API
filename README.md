# Loja Social IPCA - API

REST API para gestão da Loja Social do IPCA construída com Ktor, Kotlin e PostgreSQL.

## Pré-requisitos

- JDK 17+
- PostgreSQL 12+
- Docker (opcional)

## Configuração

1. Copie `.env.example` para `.env` e configure as variáveis:
```bash
cp .env.example .env
```

2. Configurar base de dados PostgreSQL:
```sql
CREATE DATABASE lojasocial;
```

## Execução

### Com Gradle
```bash
./gradlew run
```

### Com Docker
```bash
docker-compose up -d
```

A API estará disponível em `http://localhost:8080`

## Documentação API

Aceder a `http://localhost:8080/swagger` para documentação OpenAPI interativa.

## Testes

Executar script de testes PowerShell:
```powershell
.\test-crud-complete.ps1
```


## Licença

MIT License