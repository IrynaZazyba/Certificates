CREATE TABLE `certificate` (
  `id` int(11) NOT NULL auto_increment primary key,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(300) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `lastUpdateDate` datetime DEFAULT NULL,
  `duration` int(11) DEFAULT NULL
);


CREATE TABLE `tag` (
  `id` int(11) NOT NULL auto_increment primary key,
  `name` varchar(45) DEFAULT NULL
);


--
-- Дамп данных таблицы `certificate`
--

-- --------------------------------------------------------

--
-- Структура таблицы `certificate_has_tag`
--

CREATE TABLE `certificate_has_tag` (
  `certificate_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  foreign key (certificate_id) references certificate(id),
  foreign key (tag_id) references tag(id)
);

--
-- Дамп данных таблицы `certificate_has_tag`
--
-- --------------------------------------------------------

--
-- Структура таблицы `tag`
--
--
-- Дамп данных таблицы `tag`
--
--
-- Индексы сохранённых таблиц
--
--
-- --
-- -- Индексы таблицы `certificate`
-- --
-- ALTER TABLE `certificate`
--   ADD PRIMARY KEY (`id`),
--   ADD UNIQUE KEY `id_UNIQUE` (`id`);
--
-- --
-- -- Индексы таблицы `certificate_has_tag`
-- --
-- ALTER TABLE `certificate_has_tag`
--   ADD PRIMARY KEY (`certificate_id`,`tag_id`),
--   ADD KEY `fk_certificate_has_tag_tag1_idx` (`tag_id`),
--   ADD KEY `fk_certificate_has_tag_certificate_idx` (`certificate_id`);
--
-- --
-- -- Индексы таблицы `tag`
-- --
-- ALTER TABLE `tag`
--   ADD PRIMARY KEY (`id`),
--   ADD UNIQUE KEY `id_UNIQUE` (`id`);
--
-- --
-- -- AUTO_INCREMENT для сохранённых таблиц
-- --
--
-- --
-- -- AUTO_INCREMENT для таблицы `certificate`
-- --
-- ALTER TABLE `certificate`
--   MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
-- --
-- -- AUTO_INCREMENT для таблицы `tag`
-- --
-- ALTER TABLE `tag`
--   MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;
-- --
-- -- Ограничения внешнего ключа сохраненных таблиц
-- --
--
-- --
-- -- Ограничения внешнего ключа таблицы `certificate_has_tag`
-- --
-- ALTER TABLE `certificate_has_tag`
--   ADD CONSTRAINT `fk_certificate_has_tag_certificate` FOREIGN KEY (`certificate_id`) REFERENCES `certificate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
--   ADD CONSTRAINT `fk_certificate_has_tag_tag1` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
-- COMMIT;
--
-- /*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
-- /*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
-- /*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
