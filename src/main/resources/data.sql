INSERT INTO upper_product_category (name, created_datetime, modified_datetime) VALUES ('티셔츠', NOW(), NOW()), ('바지', NOW(), NOW()), ('신발', NOW(), NOW());
INSERT INTO sub_product_category (upper_product_category_id, name, created_datetime, modified_datetime) VALUES (1, '브이넥 티셔츠', NOW(), NOW()), (1, '라운드 티셔츠', NOW(), NOW()), (2, '반바지', NOW(), NOW()), (2, '냉장고 바지', NOW(), NOW()), (3, '운동화', NOW(), NOW()), (3, '스니커즈', NOW(), NOW());
