insert into `member_profile` (member_id, nickname, profile_pic_url, created_at, updated_at) values
	(1, '최상위', null, now(), now()),
	(2, '관리자1', null, now(), now()),
	(3, '관리자2', null, now(), now()),
	(4, '관리자3', null, now(), now()),
	(5, '회원1', null, now(), now()),
	(6, '회원2', 'https://culturabcs.gob.mx/defaults/profile.png', now(), now()),
	(7, '회원3', 'https://culturabcs.gob.mx/defaults/profile.png', now(), now()),
	(8, '회원4', null, now(), now()),
	(9, '회원5', 'https://culturabcs.gob.mx/defaults/profile.png', now(), now());

insert into `post` (post_id, member_id, title, content, is_blinded, created_at, updated_at) values
	(1, 5, 'Aperiam eos in repellendus doloremque.', 'Iusto magni nemo quisquam iusto. Nulla dicta libero.', false, now(), now()),
	(2, 5, 'A quia vero sapiente. Facilis et excepturi.', 'Illo omnis perferendis magni. Dicta doloremque nihil expedita nisi.
Quo maiores sapiente nesciunt.', false, now(), now()),
	(3, 5, 'Et repellat autem sit placeat aliquam.', 'Minus quia dicta dolore natus quam dolorum. Sit corporis fugit aliquid. Excepturi omnis rem.', false, now(), now()),
	(4, 7, 'Enim doloremque eius ex eum impedit.', 'Dolores quo aliquid quasi facilis.', false, now(), now()),
	(5, 5, 'Labore ab eveniet unde deserunt a illum.', 'Vitae ducimus eius nisi tenetur placeat id. Quas modi architecto.', false, now(), now()),
	(6, 9, 'Temporibus odit ab corrupti iste minima.', 'Aut officia dolorum ipsum quisquam error ea.', false, now(), now()),
	(7, 6, 'Nobis saepe ducimus optio.', 'Totam eos quibusdam.', false, now(), now()),
	(8, 6, 'Fugiat molestiae velit.', 'Ipsam itaque eaque. Iste fuga dolores harum laudantium quaerat.', false, now(), now()),
	(9, 9, 'Deleniti fugit libero mollitia libero quas.', 'Modi labore pariatur quasi. Ipsam et blanditiis.
Quibusdam quo blanditiis neque eaque.', false, now(), now()),
	(10, 6, 'Alias iure velit optio quis optio.', 'Eveniet expedita tenetur. Sed exercitationem dolores suscipit.', false, now(), now());

insert into `comment` (comment_id, post_id, member_id, parent_comment_id, content, is_blinded, created_at, updated_at) values
	(1, 9, 5, null, 'Ea aspernatur aut asperiores at laborum. Deserunt ad molestiae ex doloremque ipsum temporibus.', false, now(), now()),
	(2, 9, 7, 1, 'Pariatur veniam voluptatibus iste. Laboriosam non sunt qui cupiditate dolore.', false, now(), now()),
	(3, 9, 8, 1, 'Quod ab eum velit.
Illum commodi voluptatem accusamus atque esse impedit. Laudantium est sit ex at.', false, now(), now()),
	(4, 2, 6, null, 'Autem eius nam facere illo. Illo ipsa esse rerum atque.', false, now(), now()),
	(5, 2, 7, 4, 'Dolorum qui nemo voluptates corrupti consectetur. Quis officiis laborum reiciendis at magnam.', false, now(), now()),
	(6, 2, 5, 4, 'Officiis commodi est vitae inventore facilis. Modi quo assumenda illo.', false, now(), now()),
	(7, 2, 6, 4, 'Recusandae quae illo vero nesciunt necessitatibus. Quasi quam pariatur explicabo.', false, now(), now()),
	(8, 3, 8, null, 'Occaecati officiis asperiores doloremque vero quasi voluptas.', false, now(), now()),
	(9, 3, 6, 8, 'Repudiandae iusto minus sapiente ea. Repellat alias omnis quos. Iste reiciendis quos dolorum.', false, now(), now()),
	(10, 3, 9, null, 'Reprehenderit molestiae repellat veritatis soluta id numquam. Corrupti officia ipsam eligendi cum.', false, now(), now()),
	(11, 3, 9, 10, 'Delectus dolore laudantium tenetur. Iusto molestiae at amet velit nisi enim.', false, now(), now()),
	(12, 4, 7, null, 'Eaque iste nam assumenda. Recusandae dolorum animi ullam. Assumenda ullam voluptas in.', false, now(), now()),
	(13, 4, 8, 12, 'Consectetur inventore architecto maiores. Corrupti ipsa quam ex beatae.', false, now(), now()),
	(14, 3, 5, null, 'Accusamus eos dolorum. Sunt aperiam itaque reiciendis iste. Officiis tempora animi.', false, now(), now()),
	(15, 3, 8, 14, 'Sequi esse magni qui nesciunt suscipit. Sit quo dolores. Vero fugit id aliquid.', false, now(), now()),
	(16, 3, 5, 14, 'Magnam fugiat accusamus aspernatur. Impedit dolorum veniam quisquam ducimus consectetur inventore.', false, now(), now()),
	(17, 3, 8, 14, 'Consequatur amet ut.
Quae error beatae. Excepturi earum delectus tempora assumenda minus quae.', false, now(), now()),
	(18, 3, 6, 14, 'Veniam asperiores possimus soluta sit. Quos deserunt sit fugit.', false, now(), now()),
	(19, 10, 7, null, 'Eveniet atque corporis voluptate. Itaque unde eum omnis eos cupiditate iste quas.', false, now(), now()),
	(20, 10, 6, 19, 'Velit natus reprehenderit nemo rerum veritatis autem voluptates.', false, now(), now()),
	(21, 10, 7, 19, 'Dolorem molestiae quisquam doloribus laboriosam id. Rem molestiae dolores molestiae.', false, now(), now()),
	(22, 10, 7, 19, 'Corrupti ratione odit nemo optio non consectetur ad. Ducimus quia nisi ut ex doloribus et.', false, now(), now()),
	(23, 9, 9, null, 'Expedita aut minima id consequuntur aspernatur enim.', false, now(), now()),
	(24, 9, 5, 23, 'Voluptatem asperiores aperiam labore officia repellat velit odit.', false, now(), now()),
	(25, 9, 7, 23, 'Dignissimos ipsum ipsum sunt. Exercitationem velit mollitia rerum. Nobis commodi voluptatum nulla.', false, now(), now()),
	(26, 9, 6, 23, 'Nobis temporibus quisquam. Debitis quas voluptate aliquid eum labore.', false, now(), now()),
	(27, 7, 8, null, 'Natus fugiat aperiam magni dolorum aperiam autem. Ad quidem odit odit nostrum.', false, now(), now()),
	(28, 7, 9, 27, 'Possimus quibusdam quibusdam tempore dignissimos at culpa quia. Esse eum libero.', false, now(), now()),
	(29, 7, 8, 27, 'Perspiciatis adipisci officiis ipsam atque. Neque incidunt sapiente minima hic amet.', false, now(), now()),
	(30, 2, 9, null, 'Ab minima totam nulla doloremque maiores modi at. Accusantium tempora rerum quod provident.', false, now(), now()),
	(31, 2, 9, 30, 'Necessitatibus similique nulla nihil beatae laborum. Deleniti deleniti facere odio.', false, now(), now()),
	(32, 2, 5, 30, 'Magnam minima excepturi ipsa quos. Numquam sed reprehenderit eveniet quas similique quis.', false, now(), now()),
	(33, 2, 5, 30, 'Provident consequuntur aut repellat eius sint. Eligendi ea error similique quas nulla impedit.', false, now(), now());

insert into `like` (like_id, member_id, post_id, created_at, updated_at) values
	(1, 8, 2, now(), now()),
	(2, 10, 10, now(), now()),
	(3, 6, 7, now(), now()),
	(4, 6, 6, now(), now()),
	(5, 7, 9, now(), now()),
	(6, 8, 8, now(), now()),
	(7, 10, 5, now(), now()),
	(8, 8, 3, now(), now()),
	(9, 10, 6, now(), now()),
	(10, 9, 4, now(), now());

insert into `bookmark` (bookmark_id, member_id, post_id, created_at, updated_at) values
	(1, 5, 4, now(), now()),
	(2, 5, 6, now(), now());

insert into `report` (report_id, member_id, contents_id, contents_type, created_at, updated_at) values
	(1, 10, 1, 'POST', now(), now()),
	(2, 7, 8, 'POST', now(), now()),
	(3, 7, 7, 'COMMENT', now(), now()),
	(4, 9, 8, 'COMMENT', now(), now());

