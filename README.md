# Tech Challenge Fase 2 — Gestão de Restaurantes

API REST (Spring Boot 4 / Java 21 / PostgreSQL) para cadastro de **tipos de usuário**, **usuários**, **restaurantes** e **itens de cardápio**.

## Arquitetura (Clean Architecture)

```
com.challengeFase2.fiap
├── domain            # Regras de negócio puras (sem Spring/JPA)
│   ├── model         # UserType, User, Restaurant, OpeningHours, MenuItem
│   ├── repository    # Portas (interfaces) de persistência
│   └── exception     # NotFound, Conflict, BusinessRule
├── application       # Casos de uso
│   ├── command       # Comandos de entrada (records)
│   └── service       # UserTypeService, UserService, RestaurantService, MenuItemService
└── infrastructure    # Detalhes técnicos
    ├── persistence   # Entidades JPA, Spring Data e adapters das portas do domínio
    ├── web           # Controllers REST, DTOs, tratamento global de erros
    └── config        # OpenAPI e carga dos tipos padrão
```

A dependência aponta sempre para dentro (infrastructure → application → domain). SOLID: cada serviço tem uma responsabilidade; os serviços dependem de abstrações (portas) implementadas por adapters (DIP).

### Regras de negócio
- Tipos padrão criados na inicialização: `Dono de Restaurante` e `Cliente`.
- Usuário é associado a um tipo (`userTypeId`); e-mail é único.
- O dono de um restaurante deve ser um usuário do tipo `Dono de Restaurante`.
- Não é possível remover tipo em uso nem usuário que é dono de restaurante (409).
- Remover um restaurante remove seus itens de cardápio.

## Endpoints

| Recurso | Endpoints |
|---|---|
| Tipos de usuário | `POST/GET /api/user-types`, `GET/PUT/DELETE /api/user-types/{id}` |
| Usuários | `POST/GET /api/users`, `GET/PUT/DELETE /api/users/{id}` |
| Restaurantes | `POST/GET /api/restaurants`, `GET/PUT/DELETE /api/restaurants/{id}` |
| Itens do cardápio | `POST/GET /api/restaurants/{restaurantId}/menu-items`, `GET/PUT/DELETE .../{id}` |

Status: `201` criação, `204` remoção, `400` validação, `404` não encontrado, `409` conflito, `422` regra de negócio.
Swagger UI: http://localhost:8080/swagger-ui.html

Exemplo de restaurante:
```json
{"name":"Cantina Bella","address":"Rua das Flores, 100","cuisineType":"Italiana","opensAt":"11:00","closesAt":"23:00","ownerId":1}
```
Exemplo de item: `{"name":"Lasanha","description":"...","price":42.90,"dineInOnly":false,"photoPath":"/images/lasanha.jpg"}`

## Execução

**Docker (app + PostgreSQL):**
```bash
docker compose up --build
```

**Local:** suba um PostgreSQL (`docker compose up db`) e rode `./mvnw spring-boot:run`.
Variáveis: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (padrão `localhost:5432/restaurante`, `postgres`/`postgres`).

## Testes e cobertura
```bash
./mvnw verify
```
Testes unitários (domínio e serviços com Mockito) e de integração (`@SpringBootTest` + MockMvc + H2). O JaCoCo falha o build abaixo de 80% de linhas; relatório em `target/site/jacoco/index.html`.

## Collection Postman
Importe `postman_collection.json` e execute as pastas em ordem (tipos → usuários → restaurantes → itens). Variáveis de ID são preenchidas automaticamente.
