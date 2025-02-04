--
-- Volcado de datos para la tabla `categories`
--
INSERT INTO `categories` (`category_id`, `name`) VALUES
(1, 'Pantalones'),
(2, 'Camisas'),
(3, 'Gorras'),
(4, 'Polos'),
(5, 'Sudaderas'),
(6, 'Camisetas');

--
-- Volcado de datos para la tabla `items`
--
INSERT INTO `items` (`item_id`, `description`, `image`, `long_description`, `name`, `price`, `size`, `category_id`) VALUES
(1, 'Vaqueros largos de corte ancho', 'Vaqueros1.jpg', 'Vaqueros elegantes de corte ancho. Fabricados en materiales de calidad superior. Ultra resistentes', 'Vaqueros largos', 99.99, 'L', 1),
(2, 'Pantalones vaqueros largos y anchos', 'Vaqueros2.jpg', 'Pantalones vaqueros largos y anchos, cómodos y con un estilo único. Materiales 100% reciclables', 'Vaqueros anchos', 50, 'M', 1),
(3, 'Camisa azul de lino', 'CamisaAzul.jpg', 'Camisa de vestir de color azul y tejida en lino. Ideal para todo tipo de ocasiones', 'Camisa azul', 34.99, 'XS', 2),
(4, 'Camisa blanca de algodón', 'CamisaBlanca.jpg', 'Camisa blanca elegante e idónea para todo tipo de ocasiones. Fabricada 100% en algodón', 'Camisa blanca', 49.99, 'XL', 2),
(5, 'Camiseta negra de algodón', 'CamisetaNegra.jpg', 'Camiseta básica, lisa y muy cómoda en color negro. Fabricada en algodón', 'Camiseta negra', 19.99, 'S', 6),
(6, 'Gorra roja básica', 'GorraRoja.jpg', 'Gorra roja básica. Fabrica en materiales de primera calidad, muy resistentes', 'Gorra roja', 25, 'M', 3),
(7, 'Polo negro de manga corta', 'PoloNegro.jpg', 'Polo negro, elegante y discreto, en color negro y fabricado en lino', 'Polo negro', 34.99, 'XL', 4),
(8, 'Sudadera azul ancha', 'SudaderaAzul.jpg', 'Sudadera ancha de color azul. Básica y discreta. Fabricada en 80% algodón y 20% poliéster', 'Sudadera azul', 60, 'S', 5),
(9, 'Sudadera gris sin capucha', 'SudaderaGris.jpg', 'Sudadera gris sin capucha. Fabricada en 100% algodón. Perfecta para cualquier ocasión', 'Sudadera gris', 44.99, 'XXL', 5);

--
-- Volcado de datos para la tabla `users`
--
INSERT INTO `users` (`user_name`, `email`, `user_first_name`, `user_last_name`, `user_password`) VALUES
('Admin1', 'PPerez123@gmail.com', 'Pablo', 'Pérez', '$2a$10$fibkgvQ1C89UykO4SqlI5OyUKwiHB1tRU4s4tvKTmm19Up9DooQNC'),
('Admin2', 'CarmenHer123@gmail.com', 'Carmen', 'Hernández', '$2a$10$nIdIMoHYGx4rCCBddSqEGewHim11oUzfkjtX0jm7FD1DjMC5/PXq.'),
('User1', 'AlvaroAviles123@gmail.com', 'Alvaro', 'Avilés', '$2a$10$nHqliFs1x8WWw.pPbogAXe8frmiIlaACzc7bt8w7bencLc4D2Hcl.'),
('User2', 'JOrtiz1@gmail.com', 'Juan', 'Ortiz', '$2a$10$.JOjMvcX9b/Gs/IjFvD8seMJvKqzMM3EiV4zYXd0oyTnPXvlrOmcq'),
('Vendor1', 'AnaTorres123@gmail.com', 'Ana', 'Torres', '$2a$10$u8zcc6y.XERHb6TjpzWdYuRZ8AZxwwmWHEl5nVKkB.1NMTkcn0rQC'),
('Vendor2', 'AntonioG123@outlook.es', 'Antonio', 'Gómez', '$2a$10$Yx.0euOSLbl2l3hrauqHTeWYLkSz/2pPps2IL6Qsf17wE.yy7oOoi');

--
-- Volcado de datos para la tabla `roles`
--
INSERT INTO `roles` (`role_name`, `role_description`) VALUES
('Admin', 'Role for the administrators of the shop'),
('User', 'Role for the clients of the shop'),
('Vendor', 'Role for the users of the shop');

--
-- Volcado de datos para la tabla `user_role`
--
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
('Admin1', 'Admin'),
('Admin2', 'Admin'),
('User1', 'User'),
('User2', 'User'),
('Vendor2', 'Vendor'),
('Vendor1', 'Vendor');

--
-- Volcado de datos para la tabla `orders`
--
INSERT INTO `orders` (`order_id`, `address`, `date`, `user_id`) VALUES
(1, 'C/ Gran Vía, 29, Madrid, 28013', '2023-09-10 11:46:11.754000', 'User1'),
(2, 'C/ La Rambla, 91, Barcelona, 08001', '2023-09-10 11:46:43.163000', 'User1'),
(3, 'C/ Mayor, 6, Toledo, 45001', '2023-09-10 11:47:23.890000', 'User2'),
(4, 'C/ Alfonso XII, 3, Valencia, 46010', '2023-09-10 11:48:01.941000', 'User2');

--
-- Volcado de datos para la tabla `item_order`
--
INSERT INTO `item_order` (`item_order_id`, `amount`, `item_id`) VALUES
(1, 5, 2),
(2, 2, 9),
(3, 1, 2),
(4, 10, 2),
(5, 1, 6),
(6, 1, 9),
(7, 2, 8);

--
-- Volcado de datos para la tabla `orders_items_order`
--
INSERT INTO `orders_items_order` (`order_order_id`, `items_order_item_order_id`) VALUES
(1, 1),
(1, 2),
(2, 3),
(3, 4),
(4, 5),
(4, 6),
(4, 7);