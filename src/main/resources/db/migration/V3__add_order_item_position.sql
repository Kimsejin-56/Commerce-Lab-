ALTER TABLE order_item ADD COLUMN item_position INT NULL;

UPDATE order_item AS item
JOIN (
    SELECT id, ROW_NUMBER() OVER (PARTITION BY order_id ORDER BY id) - 1 AS position
    FROM order_item
) AS ranked ON ranked.id = item.id
SET item.item_position = ranked.position;
