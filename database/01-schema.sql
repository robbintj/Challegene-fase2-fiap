-- =============================================================================
-- Script de criação do banco "restaurante" (PostgreSQL 16)
-- Executado automaticamente pelo container Docker na PRIMEIRA inicialização
-- (volume vazio). Também pode ser executado manualmente com o psql.
-- O Hibernate (ddl-auto=update) é compatível com este esquema.
-- =============================================================================

-- Tipos de usuário (ex.: "Dono de Restaurante", "Cliente")
CREATE TABLE IF NOT EXISTS user_types (
    id   BIGSERIAL    PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Usuários: cada usuário está associado a exatamente um tipo
CREATE TABLE IF NOT EXISTS users (
    id           BIGSERIAL    PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    user_type_id BIGINT       NOT NULL REFERENCES user_types (id)
);

-- Restaurantes: o dono (owner_id) é um usuário existente
CREATE TABLE IF NOT EXISTS restaurants (
    id           BIGSERIAL    PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    address      VARCHAR(255) NOT NULL,
    cuisine_type VARCHAR(255) NOT NULL,
    opens_at     TIME         NOT NULL,
    closes_at    TIME         NOT NULL,
    owner_id     BIGINT       NOT NULL REFERENCES users (id)
);

-- Itens do cardápio: pertencem a um restaurante
CREATE TABLE IF NOT EXISTS menu_items (
    id            BIGSERIAL     PRIMARY KEY,
    restaurant_id BIGINT        NOT NULL REFERENCES restaurants (id),
    name          VARCHAR(255)  NOT NULL,
    description   VARCHAR(1000) NOT NULL,
    price         NUMERIC(10,2) NOT NULL,
    dine_in_only  BOOLEAN       NOT NULL,
    photo_path    VARCHAR(255)  NOT NULL
);

-- Tipos de usuário padrão exigidos pelo negócio
INSERT INTO user_types (name) VALUES ('Dono de Restaurante'), ('Cliente')
ON CONFLICT (name) DO NOTHING;
