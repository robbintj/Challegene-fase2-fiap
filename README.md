# 🍽️ Tech Challenge Fase 2 — Gestão de Restaurantes

API REST para gerenciar **tipos de usuário**, **usuários**, **restaurantes** e **itens de cardápio**.

**Tecnologias:** Java 21 · Spring Boot 4 · Spring Data JPA · PostgreSQL 16 · Docker · Swagger (OpenAPI) · JUnit 5 + Mockito · JaCoCo

## Sumário
1. [O que o sistema faz](#1-o-que-o-sistema-faz)
2. [Pré-requisitos](#2-pré-requisitos)
3. [Baixando o projeto](#3-baixando-o-projeto)
4. [Opção A — Tudo no Docker (mais fácil)](#4-opção-a--tudo-no-docker-mais-fácil)
5. [Opção B — Banco no Docker + API no IntelliJ](#5-opção-b--banco-no-docker--api-no-intellij)
6. [Testando a API](#6-testando-a-api)
7. [Endpoints](#7-endpoints)
8. [Banco de dados](#8-banco-de-dados)
9. [Testes e cobertura](#9-testes-e-cobertura)
10. [Arquitetura](#10-arquitetura)
11. [Problemas comuns](#11-problemas-comuns)

---

## 1. O que o sistema faz

| Recurso | Descrição |
|---|---|
| **Tipo de usuário** | Ex.: `Dono de Restaurante` e `Cliente` (criados automaticamente). |
| **Usuário** | Pessoa cadastrada, associada a **um** tipo de usuário. |
| **Restaurante** | Nome, endereço, tipo de cozinha, horário de funcionamento e **dono** (usuário do tipo *Dono de Restaurante*). |
| **Item do cardápio** | Nome, descrição, preço, "somente no local" e caminho da foto. Pertence a um restaurante. |

Regras de negócio principais:
- O e-mail do usuário e o nome do tipo de usuário são únicos (erro `409`).
- Só um usuário do tipo `Dono de Restaurante` pode ser dono de restaurante (erro `422`).
- Não é possível remover um tipo em uso, nem um usuário que é dono de restaurante (erro `409`).
- Remover um restaurante remove também seus itens de cardápio.

## 2. Pré-requisitos

Instale (uma vez):

| Ferramenta | Para quê | Como verificar |
|---|---|---|
| **Git** | baixar o projeto | `git --version` |
| **Docker Desktop** (aberto e rodando) | subir banco/aplicação | `docker --version` e `docker compose version` |
| **JDK 21** | só na Opção B (IntelliJ) | `java -version` (deve mostrar 21) |
| **IntelliJ IDEA** (Community serve) | só na Opção B | — |

> Dica: na Opção A você **não precisa** instalar Java nem Maven; o Docker faz tudo.

## 3. Baixando o projeto

```bash
git clone https://github.com/robbintj/Challegene-fase2-fiap.git
cd Challegene-fase2-fiap
```

## 4. Opção A — Tudo no Docker (mais fácil)

Sobe o **PostgreSQL** e a **API** juntos.

1. Abra o Docker Desktop e aguarde ele ficar ativo.
2. Na pasta do projeto, execute:
   ```bash
   docker compose up --build
   ```
   A primeira vez demora alguns minutos (baixa imagens e compila o projeto).
3. Aguarde aparecer no log algo como `Started FiapApplication`.
4. Acesse o Swagger: **http://localhost:8080/swagger-ui.html**
5. Para parar: `Ctrl + C`. Para remover os containers: `docker compose down`.
   Para **apagar também os dados do banco**: `docker compose down -v`.

Portas ocupadas? Troque-as sem editar arquivos:
```bash
DB_PORT=5433 APP_PORT=8081 docker compose up --build
```
(No Windows PowerShell: `$env:DB_PORT=5433; $env:APP_PORT=8081; docker compose up --build`.)

## 5. Opção B — Banco no Docker + API no IntelliJ

Ideal para desenvolver e depurar.

### Passo 1 — Subir somente o banco
```bash
docker compose up -d db
```
- `-d` roda em segundo plano. Verifique com `docker compose ps` (deve aparecer `healthy`).
- Na primeira execução, o script [`database/01-schema.sql`](database/01-schema.sql) cria as tabelas e os tipos de usuário padrão.
- O banco fica em `localhost:5432` (banco `restaurante`, usuário `postgres`, senha `postgres`).

> Já tem um PostgreSQL usando a porta 5432? Use `DB_PORT=5433 docker compose up -d db` e depois informe `DB_URL=jdbc:postgresql://localhost:5433/restaurante` no Passo 3.

### Passo 2 — Abrir o projeto no IntelliJ
1. **File → Open…** e selecione a pasta do projeto (onde está o `pom.xml`).
2. Se perguntar, escolha **Trust Project** e **Open as Project**.
3. Aguarde o Maven baixar as dependências (barra de progresso no canto inferior direito).
   Se nada acontecer: clique com o botão direito no `pom.xml` → **Maven → Reload project**.
4. Configure o JDK 21: **File → Project Structure → Project → SDK** → selecione o JDK 21 (ou *Add SDK → Download JDK*), e **Language level: 21**.

### Passo 3 — Rodar a aplicação
1. Abra `src/main/java/com/challengeFase2/fiap/FiapApplication.java`.
2. Clique no ícone ▶ ao lado do método `main` → **Run 'FiapApplication'**.
3. Aguarde `Started FiapApplication` no console.
4. Acesse **http://localhost:8080/swagger-ui.html**.

A aplicação já usa por padrão `localhost:5432`, `postgres`/`postgres`. Só é preciso configurar algo se mudou isso:
**Run → Edit Configurations… → FiapApplication → Modify options → Environment variables**, por exemplo:
`DB_URL=jdbc:postgresql://localhost:5433/restaurante;DB_USERNAME=postgres;DB_PASSWORD=postgres`

Alternativa pelo terminal (sem IntelliJ): `./mvnw spring-boot:run` (no Windows: `mvnw.cmd spring-boot:run`).

### Passo 4 — Parar
Botão ■ do IntelliJ para a API; `docker compose stop db` para o banco (ou `docker compose down` para remover o container).

## 6. Testando a API

**Swagger (mais simples):** http://localhost:8080/swagger-ui.html → escolha um endpoint → *Try it out* → *Execute*.

**Postman:** importe [`postman_collection.json`](postman_collection.json) (*Import → Upload Files*). Rode as pastas na ordem:
1. *Tipos de usuário* → 2. *Usuários* → 3. *Restaurantes* → 4. *Itens do cardápio*.
Os IDs criados são guardados automaticamente em variáveis da collection. A variável `baseUrl` é `http://localhost:8080` (altere se mudou a porta).

**Dados de exemplo (opcional):**
```bash
docker compose exec -T db psql -U postgres -d restaurante < database/sample-data.sql
```
Cria 2 usuários, o restaurante *Cantina Bella* e um item de cardápio.

**Fluxo manual com `curl`:**
```bash
# 1. veja os tipos de usuário (anote o id de "Dono de Restaurante")
curl localhost:8080/api/user-types

# 2. crie um usuário dono (troque userTypeId pelo id do passo 1)
curl -X POST localhost:8080/api/users -H "Content-Type: application/json" \
  -d '{"name":"Maria Silva","email":"maria@email.com","userTypeId":1}'

# 3. crie o restaurante (ownerId = id do usuário criado)
curl -X POST localhost:8080/api/restaurants -H "Content-Type: application/json" \
  -d '{"name":"Cantina Bella","address":"Rua das Flores, 100","cuisineType":"Italiana","opensAt":"11:00","closesAt":"23:00","ownerId":1}'

# 4. crie um item de cardápio (1 = id do restaurante)
curl -X POST localhost:8080/api/restaurants/1/menu-items -H "Content-Type: application/json" \
  -d '{"name":"Lasanha","description":"Molho bolonhesa","price":42.90,"dineInOnly":false,"photoPath":"/images/lasanha.jpg"}'
```

## 7. Endpoints

| Recurso | Método e rota |
|---|---|
| Tipos de usuário | `POST /api/user-types` · `GET /api/user-types` · `GET/PUT/DELETE /api/user-types/{id}` |
| Usuários | `POST /api/users` · `GET /api/users` · `GET/PUT/DELETE /api/users/{id}` |
| Restaurantes | `POST /api/restaurants` · `GET /api/restaurants` · `GET/PUT/DELETE /api/restaurants/{id}` |
| Itens do cardápio | `POST/GET /api/restaurants/{restaurantId}/menu-items` · `GET/PUT/DELETE /api/restaurants/{restaurantId}/menu-items/{id}` |

| Status | Significado |
|---|---|
| `200` / `201` / `204` | Sucesso / criado / removido |
| `400` | Dados inválidos ou JSON malformado (a resposta lista os campos com erro) |
| `404` | Recurso não encontrado |
| `409` | Conflito (duplicidade ou vínculo existente) |
| `422` | Regra de negócio violada (ex.: dono sem o tipo correto) |

Corpos de exemplo:
```jsonc
// POST /api/users
{ "name": "Maria Silva", "email": "maria@email.com", "userTypeId": 1 }
// POST /api/restaurants
{ "name": "Cantina Bella", "address": "Rua das Flores, 100", "cuisineType": "Italiana",
  "opensAt": "11:00", "closesAt": "23:00", "ownerId": 1 }
// POST /api/restaurants/1/menu-items
{ "name": "Lasanha", "description": "Molho bolonhesa", "price": 42.90,
  "dineInOnly": false, "photoPath": "/images/lasanha.jpg" }
```

## 8. Banco de dados

- **Imagem:** `postgres:16-alpine` · **Banco:** `restaurante` · **Usuário/senha:** `postgres` / `postgres` · **Dados persistidos** no volume Docker `pgdata`.
- **Script de criação:** [`database/01-schema.sql`](database/01-schema.sql) — executado automaticamente pelo Docker na primeira criação do volume. O Hibernate (`ddl-auto=update`) também cria/ajusta as tabelas se necessário.
- **Dados de exemplo:** [`database/sample-data.sql`](database/sample-data.sql) (opcional).
- **Recriar o banco do zero:** `docker compose down -v && docker compose up -d db`.
- **Acessar o banco por linha de comando:** `docker compose exec db psql -U postgres -d restaurante`
  (comandos úteis: `\dt` lista tabelas, `SELECT * FROM users;`, `\q` sai).
  Também funciona no DBeaver/IntelliJ Database com `localhost:5432`.

Modelo de dados:
```
user_types 1───* users 1───* restaurants 1───* menu_items
```

Variáveis de ambiente da aplicação (todas opcionais):

| Variável | Padrão |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/restaurante` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | `postgres` |

## 9. Testes e cobertura

```bash
./mvnw verify          # Windows: mvnw.cmd verify
```
- **Unitários:** domínio e serviços (Mockito).
- **Integração:** `@SpringBootTest` + MockMvc contra um banco H2 em memória (não precisa do Docker).
- **Cobertura:** JaCoCo falha o build abaixo de **80%** de linhas (atualmente ~97%). Relatório: `target/site/jacoco/index.html`.

## 10. Arquitetura

Clean Architecture: as dependências apontam sempre para dentro (`infrastructure → application → domain`).

```
src/main/java/com/challengeFase2/fiap
├── domain            # Regras de negócio puras (sem Spring/JPA)
│   ├── model         # UserType, User, Restaurant, OpeningHours, MenuItem
│   ├── repository    # Portas (interfaces) de persistência
│   └── exception     # NotFound, Conflict, BusinessRule
├── application       # Casos de uso
│   ├── command       # Dados de entrada (records)
│   └── service       # UserTypeService, UserService, RestaurantService, MenuItemService
└── infrastructure    # Detalhes técnicos
    ├── persistence   # Entidades JPA, Spring Data e adapters das portas do domínio
    ├── web           # Controllers REST, DTOs, tratamento global de erros
    └── config        # OpenAPI e carga dos tipos de usuário padrão
database/             # Scripts SQL (schema e dados de exemplo)
```
Princípios aplicados: **SRP** (um serviço por agregado, validações no domínio), **DIP** (serviços dependem de interfaces; os adapters JPA as implementam), separação entre DTOs da API e modelos de domínio.

## 11. Problemas comuns

| Sintoma | Solução |
|---|---|
| `Cannot connect to the Docker daemon` | Abra o Docker Desktop e espere iniciar. |
| `port is already allocated` / `address already in use` | Porta ocupada. Use `DB_PORT=5433` e/ou `APP_PORT=8081` (veja seção 4) ou pare o que usa a porta. |
| `Connection refused` / `password authentication failed` ao iniciar a API | O banco não está de pé (`docker compose ps`) ou há outro PostgreSQL na 5432 com senha diferente. Ajuste `DB_URL`/`DB_PASSWORD`. |
| IntelliJ: `ClassNotFoundException: ...FiapApplication` | Projeto Maven não importado: botão direito no `pom.xml` → **Maven → Reload project**; depois **Build → Rebuild Project**. |
| IntelliJ: `invalid source release: 21` | Configure o JDK 21 (Passo 2.4). |
| Alterei o script SQL e nada mudou | Scripts só rodam com volume vazio: `docker compose down -v` e suba de novo. |
| Erros `Lombok`/símbolo não encontrado no IntelliJ | **Settings → Build → Compiler → Annotation Processors → Enable annotation processing**. |
