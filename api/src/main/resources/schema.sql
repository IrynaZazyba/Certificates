CREATE TABLE `certificate` (
  `id` int(11) NOT NULL auto_increment primary key,
  `name` varchar(45) DEFAULT NOT NULL,
  `description` varchar(300) DEFAULT NOT NULL,
  `createDate` datetime DEFAULT NOT NULL,
  `lastUpdateDate` datetime DEFAULT NOT NULL,
  `duration` int(11) DEFAULT NOT NULL
);


CREATE TABLE `tag` (
  `id` int(11) NOT NULL auto_increment primary key,
  `name` varchar(45) DEFAULT NOT NULL
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

