
# 📝 Wishlist Service

Este é um microserviço responsável por gerenciar uma **wishlist (lista de desejos)** de produtos por cliente. Permite adicionar, remover, listar e verificar produtos salvos por um cliente, além de buscar por categorias.

---

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Validation
- Spring Data MongoDB
- MongoDB
- JUnit
- Mockito
- MockMvc
- Maven

---

## 📦 Estrutura do Projeto

```
src/
├── controller/             # Endpoints REST
├── domain/                 # Entidades (Wishlist)
├── dtos/                   # Data Transfer Objects (DTOs)
├── exceptions/             # Custom exceptions 
├── infra/                  # handler global
├── repositories/           # Interface MongoDB
├── service/                # Lógica de negócio
└── tests/                  # Testes unitários
```

---

## 📘 Documentação da API

### ➕ POST `/wishlist`

Adiciona uma nova wishlist para um cliente.

**Requisição**:

```json
{
  "customerId": "cust123",
  "productId": "prod999",
  "tagsCategory": "Eletrônicos"
}
```

**Respostas**:

- ✅ `201 Created` – Wishlist salva com sucesso.
- ❌ `400 Bad Request` – Validação falhou (`@NotBlank`).
- ❌ `409 Conflict` – Limite atingido ou item duplicado.

---

### ❌ DELETE `/wishlist`

Remove uma wishlist pelo ID composto (cliente + produto).

**Requisição**:

```json
{
  "customerId": "cust123",
  "productId": "prod999"
}
```

**Respostas**:

- ✅ `200 OK` – Wishlist removida.
- ❌ `404 Not Found` – Wishlist não encontrada.

---

### 📄 GET `/wishlist/customer/{customerId}`

Retorna todas as wishlists de um cliente.

**Resposta**:

```json
{
  "wishlists": [
    {
      "customerId": "cust123",
      "productId": "prod999",
      "tagsCategory": "Eletrônicos"
    }
  ]
}
```

- ✅ `200 OK`
- ❌ `404 Not Found` – Nenhuma wishlist encontrada

---

### 🔍 GET `/wishlist/exists?customerId=...&productId=...`

Verifica se um produto está na wishlist de um cliente.

**Resposta**:

```json
{
  "exists": true
}
```

---

### 🔎 GET `/wishlist/tags/{tag}`

Busca wishlists por **categoria (parcial, case-insensitive)**.

**Exemplo**: `/wishlist/tags/eletr`

**Resposta**:

```json
{
  "wishlists": [ ... ]
}
```

---

## ⚠️ Validações

Campos obrigatórios (via `@NotBlank`):

- `customerId`
- `productId`

---

## ❗ Tratamento de Erros (GlobalExceptionHandler)

| Exceção                             | Código HTTP | Mensagem padrão                                     |
|------------------------------------|-------------|-----------------------------------------------------|
| `MethodArgumentNotValidException`  | `400`       | `"Validation failed"` + detalhes por campo         |
| `WishlistLimitExceededException`   | `409`       | `"Customer has reached the maximum number of wishlists"` |
| `DuplicateKeyException`            | `409`       | `"A record with the same key already exists."`     |
| `WishlistNotFoundException`        | `404`       | `"Wishlist with id ... does not exist."`           |
| `Exception`                        | `500`       | `"An unexpected error occurred."`                  |

---

## ✅ Testes

### Executar testes unitários

```bash
./mvnw test
```

- Testes com cobertura para 100% das classes, 96% dos métodos e 98% das linhas
- Cobrem casos de sucesso e erro

---

## ▶️ Executando a aplicação

1. Configure o `MongoDB` local ou via Docker 
   - Caso docker use o docker-compose na raiz do projeto:
     ```bash
     docker compose up -d
     ```
2. Altere o `application.properties` se necessário
3. Rode a aplicação com:

```bash
./mvnw spring-boot:run
```

---

## 🧪 Exemplo de cURL

```bash
curl -X POST http://localhost:8080/wishlist   -H "Content-Type: application/json"   -d '{"customerId": "cust123", "productId": "prod456", "tagsCategory": "Books"}'
```

---

## 📌 TODO

- [ ] Autenticação via JWT (Spring Security)
- [ ] Paginação em listagens

---

## 🧑‍💻 Autor

Giovane Santiago  
`wishlist-service` © 2025
