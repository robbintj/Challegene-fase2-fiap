-- =============================================================================
-- (OPCIONAL) Dados de exemplo para testar a API rapidamente.
-- Execute DEPOIS que a aplicação ou o 01-schema.sql tiverem criado as tabelas:
--   docker compose exec -T db psql -U postgres -d restaurante < database/sample-data.sql
-- =============================================================================

INSERT INTO users (name, email, user_type_id)
SELECT 'Maria Silva', 'maria@email.com', id FROM user_types WHERE name = 'Dono de Restaurante'
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, user_type_id)
SELECT 'João Souza', 'joao@email.com', id FROM user_types WHERE name = 'Cliente'
ON CONFLICT (email) DO NOTHING;

INSERT INTO restaurants (name, address, cuisine_type, opens_at, closes_at, owner_id)
SELECT 'Cantina Bella', 'Rua das Flores, 100 - São Paulo/SP', 'Italiana', '11:00', '23:00', id
FROM users WHERE email = 'maria@email.com'
AND NOT EXISTS (SELECT 1 FROM restaurants WHERE name = 'Cantina Bella');

INSERT INTO menu_items (restaurant_id, name, description, price, dine_in_only, photo_path)
SELECT r.id, 'Lasanha à Bolonhesa', 'Lasanha com molho bolonhesa e queijo gratinado', 42.90, false, '/images/lasanha.jpg'
FROM restaurants r WHERE r.name = 'Cantina Bella'
AND NOT EXISTS (SELECT 1 FROM menu_items WHERE name = 'Lasanha à Bolonhesa');
