truncate table sys_user;
truncate table sys_staff;
insert sys_user (username,login_number,password,real_name) values('admin','admin','34be8da3b5eff97e83eeb201624d7080894d037d123572d64a112b3f429ab1294be6e1dfa232597f94d8a549f1dfeb431b9bdce0fe878314002833e9f344b6b4', '管理员');

truncate table sys_role;
insert sys_role (name,remark,editor_id,editor_name) values('ROLE_ADMIN','管理员',1,'admin');
insert sys_role (name,remark,editor_id,editor_name) values('ROLE_STUDENT_DUTY','值班班长',1,'admin');
insert sys_role (name,remark,editor_id,editor_name) values('ROLE_STAFF','教职工',1,'admin');
insert sys_role (name,remark,editor_id,editor_name) values('ROLE_STUDENT','学生',1,'admin');


truncate table sys_role_authority;
insert sys_role_authority(role_id,authority_id) select 1,id from sys_authority where title in ('消息管理','教师分组','发送人员','系统设置') or parent_id=(select sa.id from sys_authority sa where sa.title='系统设置');
insert sys_role_authority(role_id,authority_id) select 2,id from sys_authority where title in ('学生管理','教学日志填报','日常考勤登记','晚自习考勤登记','因病缺课登记');
insert sys_role_authority(role_id,authority_id) select 3,id from sys_authority where title in ('消息管理','我的消息','成果管理','我的成果','文章管理');

insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(1,'校长室','xu_f','徐飞','M','18912981975','','正高级','3796814705','321002197501261218','TEACHER','OTHER','特级教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(2,'18912981975','xu_f','c77ff4b2f9accd520807dd5d81c22c9cae857b7252e64645819ba81929cc3b902e53a2bde26fa161196c519452edaf9409dbe6bfcec9503005d56e52a9ae1d86','徐飞',1);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(2,'党委','li_b','李兵','M','13851938995','','高级','3796830193','320114197106110014','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(3,'13851938995','li_b','67e49107a753b0dd142d02e09d55702ffff2e44d834cdcc41763759219a78f9112c8279bf4ac4d6c15a5002af754e050b9df02334856697e02ee7fc632360d28','李兵',2);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(3,'校长室','song_h','宋辉','M','13705196086','','正高级','3796801409','320125196402260038','TEACHER','OTHER','特级教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(4,'13705196086','song_h','8d56bea9ae33a068442343727478cce578be53145c2973b996dbc036d6055a58f0a7fd7cd92d5d69f2f7bdfaa3493f27fa26a94d899c54432143d9b5d0c27390','宋辉',3);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(4,'校长室','wang_sh','王圣华','M','18051989989','','一级','3796846193','320107196704030331','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(5,'18051989989','wang_sh','a7abd387d5b3e4181a660c68b05ab1f172fe110e5bac39a21af4b353f136c9bab6a4099811508057b163bbdb3a6889c2c6a0b58e152a0c8abb602e6bfc29ea02','王圣华',4);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(5,'党委','zhou_lx','周立新','M','13705180292','','高级','3796810609','320106196908270437','TEACHER','OTHER','市学科带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(6,'13705180292','zhou_lx','370fb31ad1ca4868efd7f997bbab3102ad668ce1fd83bfad4beaf8b02b49169d711536341b60e8e1999163e4d4e51125a55c3b3db1ac6d4fca5df9c3e92c272c','周立新',5);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(6,'校长室','qian_h','钱华','W','13675105899','','正高级','3796819489','320106197209160428','TEACHER','OTHER','市学科带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(7,'13675105899','qian_h','47bccc12b0a795f03ba480e2ed6b6c018fa259bea9569d74280b320a439ef7071a943434a764e9ecab7d4fde29dbe3099d229842fd793c6144e537cade214601','钱华',6);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(7,'德育处','zhang_hn','张海宁','W','13851944440','','高级','3796814497','320106197611293229','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(8,'13851944440','zhang_hn','1759cdae80d0c493fbb80284e483e057bcdec9f9716ac61e938b4a9be19b8d10464761684b9f6d2d7e7b9b39fc0b61c44cb8af5576d8d0dfd1ef89bf88719373','张海宁',7);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(8,'党政办公室','xia_xf','夏新峰','M','13901592770','','高级','3796797793','320721197707201639','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(9,'13901592770','xia_xf','905343ddb00302ae836d02c24f998a82fcd5901432b0f8516c088e0321952574775556cdea02b206a3f80cd3271c64eb38d07c6bd2af99cc707e75b2cc189e90','夏新峰',8);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(9,'总务处','chen_lq','陈柳青','W','13851663257','','高级','3796845409','320106197903150465','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(10,'13851663257','chen_lq','f5a5f6aeaff80440039e93b0bb05e69e4b86670ed4775892f95b1328d2f57dc1ff840b19eb495af49b580cae8a9536ac05feaa5990fa520caa1a98d8c38f6b50','陈柳青',9);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(10,'党政办公室','tu_ss','涂珊珊','W','13611591967','','一级','3796845041','320104198812180441','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(11,'13611591967','tu_ss','92c0e3713480e990ab33bde3bf1c736360048c5ea46226949b3251d76c33931c15ef14cfc7aca96e45bd432167ec5f692a459ca045df62450278ffadf845b9ff','涂珊珊',10);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(11,'党政办公室','liu_q','刘青','W','13913972462','','一级','3796815361','320102198204053227','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(12,'13913972462','liu_q','80666003c693fac89e97f4f4074d2a1089315008b4058fc218d9fc3063369d3ce6e24b427e955a1a2e5398f0d9cb1b706ea49c92f925d3568bb88d82e98b30ee','刘青',11);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(12,'心理健康教育中心','tao_dh','陶德华','W','13655197099','','正高级','3796839329','320102196803172464','TEACHER','OTHER','特级教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(13,'13655197099','tao_dh','13fa17ec1ed3ba7369a00c0bf7c0a06ce11f86bcb77741950ec3b026b92f9b8d657ba7ab956a6a88e6f362959a9891132ab706c306e5c25dfb488e4427a0afc5','陶德华',12);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(13,'督察室','yan_lw','严龙文','M','13815854921','','高级','3796819121','320106196703200419','TEACHER','OTHER','市学科带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(14,'13815854921','yan_lw','bf2d1ccc19a584780a2c61fd8ca4ef407de850897b3fbd30785fdb9f42583facf9fbca601b741db4cf56b776a79b1594d831135d32a7c7d3349189834b69c1ca','严龙文',13);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(14,'工会','niu_g','牛刚','M','13951798656','','高级','3796849009','320106196812141251','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(15,'13951798656','niu_g','66a716c71b305c76c0edb7ab3718d1d5713b765a4c8a13cbbece5d62236893c5c3e71d23ff499956846fbcb5f06b4a0d2faab5d7b0aff817545301efa2e5d18d','牛刚',14);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(15,'教研处','chen_yy','陈义永','M','13851548830','','高级','3796821025','320924197907153416','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(16,'13851548830','chen_yy','ae0cb30017c21b4f421a76eecbc4bd0a7f9b2598b243c32f2d96dfa37a2b0ba39ef843d8bc95b2c24e7156f16dd36835706aad7890193e10b1146528268ffba9','陈义永',15);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(16,'教研处','bian_ss','卞姗姗','W','13913880246','','正高级','3796783937','320106198010302462','TEACHER','OTHER','市学科带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(17,'13913880246','bian_ss','5a8c5e0767d25e45b94b47b78aaa560c65fe6bbabf4a830dea506d906913b17392899cd945453ba947aed478f9a018a583b8a425a17a6a8c0766dbadb57dd41c','卞姗姗',16);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(17,'教务处','puyang_kh','濮阳康和','M','13913334028','','高级','3796798833','320125197906284310','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(18,'13913334028','puyang_kh','151fc182aaec274c28e43d02b33a535126786a3eede82da850a4e1dca44243dd453b7b9377468e7026f415b37207da2430c00321517e94f4c00189efe96b76aa','濮阳康和',17);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(18,'信息中心','yao_fq','姚发权','M','13952005739','','高级','3796789441','340825197606143112','TEACHER','OTHER','市学科带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(19,'13952005739','yao_fq','a9cfe4782549a70d95b86c92f97a7f0c04e6abebde06769b95133be4dc4a981b4a72feeacb2a65c22c74f38666053197115076656af7609f5bac3e35b91f891d','姚发权',18);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(19,'总务处','zhang_yehong','张业宏','M','13951993566','','高级','3796791537','320124197101061818','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(20,'13951993566','zhang_yehong','e62e98a597aa5e290767546647fb1b3917d804105ce810ff795dd924c6afa66550e308883b83f0946a94392c4051faab1f197688b08adb20e4e1c939fe18eeff','张业宏',19);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(20,'德育处','li_zq','李钟全','M','15996309663','','一级','3796808353','320124198606030819','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(21,'15996309663','li_zq','456ea44fd20a1741ccd25aa832d8500ee41b953f2aa2aa36737261fe903a2a67f85e5843a0020dabb2a65f9063dfac19898add414f6c1ee50688c6b7f2cb6911','李钟全',20);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(21,'德育处','dai_j','戴俊','M','13770724470','','二级','3796817793','320583198712158930','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(22,'13770724470','dai_j','74c0fd1710f1f9fe0801747be72eb365f91d99abae8b8d2a83e5b6419b4ebb10d73628991b878d96240a2f864e3d1a8e177e0ed8e54ceb3e60cce1b12028154e','戴俊',21);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(22,'总务处','lu_j','陆俊','M','13951914730','','高级','3796848625','321028197804050219','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(23,'13951914730','lu_j','f8a79ecce2a44488c115ad6b7482a5ecd9b1db78681c1895f18a7487fa901b91b064d20ed0c9beea5f311fa60f9b6b31f52b2b19f17d942697ac31fb3805955a','陆俊',22);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(23,'信息中心','tao_j','陶佳','M','13951981727','','一级','3796793873','320104197712081652','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(24,'13951981727','tao_j','d50098e29cc8f4f7ad543af97279be8eefcea04cf08d1b12533e77672d4aba2827604ff7607c4b32c0328a256edca82bd9ebb0c9a487479cd3982b527e7762e6','陶佳',23);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(24,'总务处','liu_pp','刘培培','W','13512536413','','行政教辅','3796818513','320311198111105240','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(25,'13512536413','liu_pp','28546ead4b0a9e08279b05a36973a7fe6b3aae712f870eb5e94c7bad382a5cd110f7d23c620ad140687517245ad32e2a90452c6bab3f99ccefb041fda9ee1da5','刘培培',24);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(25,'教务处','chen_xh','陈晓浒','M','13770989118','','高级','3796828849','320102197906230019','TEACHER','OTHER','市学科带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(26,'13770989118','chen_xh','e60fc86e6b3ad4614f7dbea4c1193c3ad35ce2dcd24f538566f6a57a523a44fb852015fd1046935341d902ef80cdeb19c83d8226f23186ceadb27b30c5521f12','陈晓浒',25);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(26,'党政办公室','lv_y','吕洋','W','13851797077','','行政教辅','3796824225','320104197710162029','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(27,'13851797077','lv_y','bb626f0d00c223ed32964b74c2c66a2ea27a77fe7312a05c1585b18b11efac05cf8c6d7348c53ba177ca94b9afbb22562af15e3bec8dc63eaafd8e3797308ee8','吕洋',26);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(27,'教务处','xu_cy','徐晨月','W','13951826392','','行政教辅','3796820417','320104197510211623','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(28,'13951826392','xu_cy','8c0c6d2a97462441dbdad789a165c720dace9a3a360f8dac071daf8d1330e617b073a8acc45dfc19961405659596d7b46a068393790c5df8c20333297bfbea7f','徐晨月',27);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(28,'教务处','zeng_r','曾嵘','W','13770679939','','一级','3796821441','320112197707020425','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(29,'13770679939','zeng_r','4fe51b2369a951fc08569ec710d0120c58d72e4d1d99611605f84c6b012526dde7e4aed0a3959f80a5733583b882f259dddaa12c1116fc7be7d2248d6e195cd7','曾嵘',28);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(29,'教务处','zhang_xc','张晓春','W','13915926476','','一级','3796812097','320223197901071140','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(30,'13915926476','zhang_xc','f2eab03d9dfdf98251ff8d38801c20a1128e38cd7b3b4542063ff018acd5bc65e932ab05214435436bebcb2fc8b56e140dee9ae69f49add404bb89c78aca7936','张晓春',29);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(30,'教务处','cheng_p','程璞','W','13814059953','','一级','3796784113','362426198503090040','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(31,'13814059953','cheng_p','ddb73939013179d2882619f912040005dbfd8fe27acdd7c13d5f975b40d4629807f3144c4b896d17a0ecffe6c9a17612786739cb8581b9d71ccdcb364fab0e93','程璞',30);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(31,'教务处','tang_jl','唐锦丽','W','13913027464','','高级','3796795041','320102197305061627','STAFF','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(32,'13913027464','tang_jl','ef4ea99430adc996c91cf58d6ba31a5c567fe2fd8ba786813bbe26ffe3c030de91b9a491668211aac43de4e36ab40ab86eb29135283da8b45c20f047bcd0d113','唐锦丽',31);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(32,'总务处','xu_n','徐宁','M','13851638665','','行政教辅','3796813953','320104197304030418','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(33,'13851638665','xu_n','851c1d310f8780c91f657019ca629b67cc7e868ffa3df47eb8e3b965bee25b94ddbadfed740ff940c1dc215349f37022aa97961866171eb4fea275cba1255d60','徐宁',32);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(33,'总务处','zhou_l','周灵','W','13621593577','','行政教辅','3796835921','320102198509125025','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(34,'13621593577','zhou_l','4ad838792e34e51390b95b4f369e07daf1c06918a85600316467531df1588476a1210a70f00d35d10751b690f8d9f65d1e3f73cb9e461ff6605dc33066c8ce57','周灵',33);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(34,'信息中心','wei_g','韦广','M','13813836670','','行政教辅','3796829473','321302198005020838','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(35,'13813836670','wei_g','5107e47324d3a0cf58191bf2d6eaac2e0db0da83da71b86feb383aaf45aef09db70b723458b2a7c6373013a56e513925f8a1ac345fec30c32487e6b6e343f894','韦广',34);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(35,'总务处','zhu_y','朱砚','M','13505190804','','行政教辅','3796789137','32010419770828043X','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(36,'13505190804','zhu_y','ef71fce09959b900c386f22601f63ce74565b22a2789247eca02e7b06eaf78eee08c21648ebbf0467d8fde081a0a28fac3b29ac398098f4aaf510a5b196d9c1a','朱砚',35);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(36,'教务处','wang_yz','王玉忠','M','13770696530','','高级','3796795793','320911196712074631','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(37,'13770696530','wang_yz','2d1c3addffb29e49144f1b617aa2f1b28e10d00c7606b82c254f978c169f632673c51e5cd88fd8f801f5e9701d6007c1b82a4d8365c07df7af70d3028585eba1','王玉忠',36);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(37,'教务处','li_hm','李洪明','M','13584098280','','高级','3796827121','320104196210080057','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(38,'13584098280','li_hm','4c9899a7088b78d33362a6bee8d273c702a0c09264d9b9343037f3b9104b18ca4bb40f2d8c268a2e562d574f69fcbb450cd2976d40bdb1c83a00b4d1329bfc23','李洪明',37);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(38,'教务处','wang_yq','王亚琦','W','13505185511','','高级','3796805217','513101197507166023','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(39,'13505185511','wang_yq','bf486ebe108acb75970aeed02c73a91dbf798eb9b599b7fdc435e2bb6350040db3c835d479f1736225a43a753637812f1046550f740edb1ac6851294c9f0c338','王亚琦',38);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(39,'教务处','sun_jy','孙京媛','W','18952092932','','一级','3796802129','320103198011101807','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(40,'18952092932','sun_jy','abbc23060d47d72b2561e9906a47e6d64a2044becc37324a768c2935e559fbf1b9db0112b2a685effcd3f57b3b4de1926a7415532978f6a39279bc49607afe7c','孙京媛',39);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(40,'教务处','zhu_dd','朱丹丹','W','13770729961','','高级','3796808273','320102198101060846','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(41,'13770729961','zhu_dd','e2669474d6f2fba32ccd998405c2164346624d355670f07b83d3b37e4f4abb615145d4d08ed887ac48a5726fd8499d5b2a81deaa56be54c7755cb1fa13dea38f','朱丹丹',40);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(41,'教务处','yuan_kr','袁康蕊','W','13813911074','','高级','3796804769','320104198201041223','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(42,'13813911074','yuan_kr','4d191fe92fc235f8b2d9b28584b2d912363b9a9efa6398b0173ff89ea9de395e39bb02d2c8e33cb193cf8ad85cf93d3c15b9009bd42030f940a99bb5accde572','袁康蕊',41);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(42,'教务处','jin_x','金星','M','15365135593','','高级','3796816241','320103198305070276','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(43,'15365135593','jin_x','380b27070ad8e38cd950abe81f41bc82d4a50b3bad596a795599e052840f02b254e6a9fba29b8e9a585c63a0af63ff463fb8834302cf0f27d7f4971dacba9d40','金星',42);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(43,'教务处','shi_wm','施伟敏','W','13814101336','','一级','3796785585','320124198406053266','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(44,'13814101336','shi_wm','d5a7c949cddebcf0763a11b3381e87bf54c98d0c461c3786376f034cf27f5411c2ea3801c15e9174bb7b025a984595f86716dfd1c46b85323780dc9b91cd2cd9','施伟敏',43);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(44,'教务处','liu_y1','刘颖','W','13913991722','','高级','3796803713','220104197011041320','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(45,'13913991722','liu_y1','de45cfc78b2d28e50466acfacf3cb1e4c1e3122709e2b01264713bc2e9655590ca0a38553d0bdfe7816dce00ed326b397b47ee2ea3e18a9393971bdf912cc2b6','刘颖',44);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(45,'教务处','zhao_yh','赵云华','W','13851401200','','一级','3796815921','321181198508047463','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(46,'13851401200','zhao_yh','1ec5dfa1e1f7182a65c86b3f2c15db554516f9df6249774a4599e5918f49c160846113a867b10fde06e84b2229fd1bc60521d92d989d8ffed8e318a3c5ecc864','赵云华',45);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(46,'教务处','zhou_cl','周春丽','W','13851875416','','高级','3796810929','320831197102030062','TEACHER','OTHER','市学科带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(47,'13851875416','zhou_cl','2974c5a732e282b68480b1b112d7715e834389164393d3e7134a73fa4baeafa5bafaf81eb1acbae1d760c27d8a41306c9d35bdd44ce4876d0d7d90788630878d','周春丽',46);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(47,'教务处','zhao_tt','赵婷婷','W','13851527410','','高级','3796815025','32010619780306042X','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(48,'13851527410','zhao_tt','8163d87b9754fcf1d8d3de6748657e7bd9ebf67ec7161697cf752f8b61edf979998c56a0d64bb89e2bd6426e0843a316aa1ff53f74344ee2db683ff72f4d1ea9','赵婷婷',47);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(48,'教务处','lu_b','陆斌','W','13601585946','','二级','3796808609','320722198811027766','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(49,'13601585946','lu_b','9149010db28f2e7aa063f4a19d8f88d3211013d7528719d3fbeb1437b9a2e453b68c1c43e0d52c9c3db7bcbed0a9828b2527b89fdc7df3f7f6444e3efe17195f','陆斌',48);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(49,'教务处','chen_ying','陈颖','W','18260057139','','一级','3796837041','32032119890912382X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(50,'18260057139','chen_ying','744be9acb3c33a019135268834ca2d1e4b2be917e1dedef6324da69daa3b704f16520fb87230257560aaeba814b9857a2ec67169ce8fea62582ba44d6d5a3079','陈颖',49);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(50,'教务处','liu_ff','刘芳芳','W','18551671226','','一级','3796825521','230421199201092421','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(51,'18551671226','liu_ff','79adc8e6715b3929c5a27148a1c3686574b779b8a326fe590e6338be6e709ad4c67adfab1e89beabdbd0d0cd3c2d8fd22d45f1ac48fa72f20c3d3d6bb7f2faf5','刘芳芳',50);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(51,'教务处','liu_jh','刘金华','M','13770798608','','高级','3796821873','320106196801100497','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(52,'13770798608','liu_jh','23c54b54c1f29418c3aa1d501f9b9b28ec456da4486daff83bd4f9683e4ca5500f0b82c2ab00e6ae6ae1d7e451ead7f4021de3e6c153acfc6e3962ae09d9d1dc','刘金华',51);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(52,'教务处','zhu_wd','朱卫东','M','13851563594','','高级','3796828353','320106196805160454','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(53,'13851563594','zhu_wd','1a006135375b1585a5e50bbf3ac9783d6574e69f43bf68ba651eec3ee27890c7b302bf9d5bd991a9f0490cb49670242e7f5bcef92957e2dd97ebf01706447c3b','朱卫东',52);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(53,'教务处','yang_js','杨厥帅','M','13851563394','','高级','3796815761','320721197412311611','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(54,'13851563394','yang_js','b79f20ffe62ef24a104237bc283c64a62b28e249a4c80a1fe8b864bc98f1751069b9069725b68cc98ef40c20a6a113a7e983a1d627c863aec2e6af04ad3680ee','杨厥帅',53);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(54,'教务处','zhou_yl','周亚林','M','13851651901','','高级','3796841233','321028196205144614','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(55,'13851651901','zhou_yl','92e28d98bc0928e81a1c416ac661634c6ce0c85af4ddd677c17c9a0d97d150eebe5626a484205ba5fa303397a2afb7dd2395f1b39f682d353177a0c7225dabef','周亚林',54);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(55,'教务处','pan_jw','潘俊文','M','13952041211','','高级','3796834529','320422196308092311','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(56,'13952041211','pan_jw','4820f0abde55a9ef2ca8b423de2e7f6ec6852f5205a64edb2c15c356341ffca4b9876541f13425be6d0c29c9b64e67d54843299ef377f028bbe659149feffcd7','潘俊文',55);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(56,'教务处','xue_ad','薛安定','M','15905140584','','高级','3796817649','32112119771102221X','TEACHER','OTHER','德育工作带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(57,'15905140584','xue_ad','40c20cfe1b088e6f1fe925c1926ebcc5357d4c07ae2381f2ac91a4202c45e11fe44aad53e1500467d48673e377635248b0cad8e275fadb9b2fa5ae0e2da445a1','薛安定',56);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(57,'教务处','hu_xz','胡祥志','M','13851795070','','一级','3796832097','321081198011120017','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(58,'13851795070','hu_xz','da02e288470c36df9f5cc3f5785ecbf9c9573490b1751eccf34808a4c496851d8ab6421eb315ba3631210f8fc819feb07df54b5e66f2cdfb50e1abb496764a6f','胡祥志',57);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(58,'教务处','xu_d','徐朵','W','18001587628','','高级','3796796353','320104198011070824','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(59,'18001587628','xu_d','95309b4e072efdb8c684774f3a10e6887f4d968e5969227b7e2d3a71b4cbc31f314f7b34f501c5ad95436c356a8e29218839a80ff6035322b8666b411de5d04d','徐朵',58);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(59,'教务处','dou_bq','窦宝泉','M','13805173438','','高级','3796818433','32083019620629001X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(60,'13805173438','dou_bq','e7d8c2242a46cfc22d2b110a977164eb11a8cd273d9e554c54ba5e8294e39bb77cdc39d9e5e50b9b11623cdbae51c16855b122c698ca3adcc98dedb774d4c255','窦宝泉',59);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(60,'教务处','zhu_cx','朱成学','M','13776621192','','高级','3796797345','320325196301200010','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(61,'13776621192','zhu_cx','0270893848046260391305e921fe36e60c46a68276a7614b54db818980753a8268c5fd92a74b7db1657d434098032f16901fd93125a9a9b96828cc3e0751fdb5','朱成学',60);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(61,'教务处','lv_n','吕娜','W','13921436970','','一级','3796801809','37078119811030052X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(62,'13921436970','lv_n','2cb899759f878b5ae8aa3bf3bb16774519b8e3971477c98b245c1af5d82dc183e4adc20db034514fa740b3e0d08ecfae1c678511ffb53240124d534ff98401aa','吕娜',61);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(62,'教务处','xu_cz','徐成中','M','13405826868','','高级','3796794033','320902197910290034','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(63,'13405826868','xu_cz','25b64b2772b6d41614d6d9fafaf8589f95a129eb441cc9001f7e4f8b2e7fa55becc2d7b5a8ce15588762b0a5ffdeb94d3049e2a033c2238e84321c4ebbe3ed27','徐成中',62);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(63,'教务处','shi_f','施峰','M','13701464073','','一级','3796809345','320211198207014516','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(64,'13701464073','shi_f','54e5ece1ffd8292afe5a8f2ba2509d2e12a4251da1a2c89241d69a497fa080bb4b6eae49b613720a545ef9d23cae37467f7b5e5e47d71bebccbd611bbdfef250','施峰',63);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(64,'教务处','peng_xx','彭晓霞','W','15951815712','','一级','3796839377','32068119850417762X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(65,'15951815712','peng_xx','3beba714cef4c762ad2876c6935cf02b6fac7daec4ff4c446dba98e0aa5013676d77bf923b08f9fdd0931142a0500f4c406a39fdcdc2ded08d3b7a2f93194630','彭晓霞',64);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(65,'教务处','cao_xy','曹晓琰','W','15050561596','','高级','3796788513','320683198108022864','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(66,'15050561596','cao_xy','d97616e4a13cc0e63360b37c6d118cdb7dcbbdba36457be76e4e703c8fd2f466077a6247448ae839b2b6fe687214163bf8e562225d9c5eb54a72a0f88159d63f','曹晓琰',65);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(66,'教务处','qiao_ww','乔文雯','W','13951884790','','一级','3796799121','320106198602090440','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(67,'13951884790','qiao_ww','f7f5f7558966433e1e8425d2d436d4bada31aeaa7c44a1a2a48fe961defe9e2b9eef5fbc5cb256860ecd5307c70688032e0848ab9c5b42d773c850da9072eb81','乔文雯',66);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(67,'教务处','xia_xq','夏小强','M','13913832815','','高级','3796816369','362425198204150414','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(68,'13913832815','xia_xq','3247aeabe463447737d64142e2e2508bb701f480f656937943cea7c48c42a4e219805b67fedf050d39a76b9b821464cb24c20dd3af0518e10e3628a1024ef6f8','夏小强',67);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(68,'教务处','wang_hh','王慧慧','W','15251866337','','一级','3796799681','370829198611263560','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(69,'15251866337','wang_hh','5535055e96929c9efaf6bd4e8ac415421229f2792bdd92771a94adb67bcbef22a1efbaf180df61fd03f538c489880a71ece79f0a20ff6ce1c9d8ea1d3c572eb1','王慧慧',68);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(69,'教务处','zhou_xy','周星月','W','13776622113','','一级','3796786721','320111198801290825','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(70,'13776622113','zhou_xy','3f3cb3d7f47fa1028989c74c2f373d495688723b912c0ccfdb9acbd9f1b4da008a4d9ef3af4d23b6c20a89575ef4b880aed1f73ab80483f4bde1b5d16be1e4d3','周星月',69);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(70,'教务处','yan_hui','严卉','W','15050582512','','一级','3796780385','320922198802073024','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(71,'15050582512','yan_hui','ef6660e71a9cad6b25d9389eb06643975c3628dc0680308a32bcf806e95e96f666d6bd31798f15c455c97e2917b57e1449cfd950e638d390b1bb14157ffd72b8','严卉',70);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(71,'教务处','li_xm','李学明','M','13952085101','','高级','3796808401','32091119620509091X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(72,'13952085101','li_xm','34708c1085e95811e9e35547ddee59c70a0147a467cf9a1865883952179cfe68d24913ef83fbecea309b0adccccb19dc3e1f9e15ff7a0b80c4363fab85e38508','李学明',71);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(72,'教务处','wu_h','吴虹','W','13815875535','','高级','3796820817','320104197010220443','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(73,'13815875535','wu_h','78e6c63fffa8c1ef790364124721a16410620ba358ffdf9c1ba0cb04c9a5c42b46bbd3f1109f168e5eb1389c5c50637ca1cd8e39a1483c7e8edc1abb58536bed','吴虹',72);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(73,'教务处','ge_w','葛玮','W','13913981301','','高级','3796791809','320103197102010543','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(74,'13913981301','ge_w','d3fb79b4528cc3c2d18b4bcd162e21f9d7049806cf6ba1c3cce862cfab1970e51d2f6ee3b7b0fc1750d699cb35901266ea64380c5edbb2f2a184b6b23c68b2b2','葛玮',73);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(74,'教务处','nie_j','聂菁','W','13505180832','','高级','3796825345','320103197810071769','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(75,'13505180832','nie_j','b74d2874488255becf4168f6f87590dfbcd481bbb542c7a3942621cc59208b62e0e141d5d2244b6bd58e00a7dd995be7b68cdba70c5f3df833de35df2d06fdc4','聂菁',74);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(75,'教务处','zhang_y1','张玥','W','13813863552','','高级','3796804049','320104197902051622','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(76,'13813863552','zhang_y1','0a09e6538212a993e43e211543691a6e7fb11e1f3cbb251a80818278405a04516155986f030e11100a304eb981e28b051a8d47b1c6f9a160450e8210ab540601','张玥',75);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(76,'教务处','zhu_xy','朱娴燕','W','13913828847','','一级','3796843041','320926197612045023','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(77,'13913828847','zhu_xy','df37edac91560c10048cb41230704da35780c34458686b8cf9affb1be814897d841ece2dad9906dad6c24275379ee227f2767870339c25728770cd4dfcd7d111','朱娴燕',76);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(77,'教务处','sha_yx','沙银霞','W','13951715392','','一级','3796782289','32128119771121458X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(78,'13951715392','sha_yx','4acdf54cbc12a56c22b52a27edb1371bdb261a9ad953a60130f5e5af50bda40f7a27a6568faf224d595630e6462122f63c615b3c2ee02f3028144b0e43a81eef','沙银霞',77);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(78,'教务处','chen_cy','陈春阳','M','13814025965','','高级','3796837025','320103196511110014','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(79,'13814025965','chen_cy','67e49107a753b0dd142d02e09d55702ffff2e44d834cdcc41763759219a78f9112c8279bf4ac4d6c15a5002af754e050b9df02334856697e02ee7fc632360d28','陈春阳',78);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(79,'教务处','deng_wd','邓卫东','M','13951796765','','高级','3796794369','320106197012270471','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(80,'13951796765','deng_wd','20d56d790edc46975ec03cc6b69d320fa0f836e9afae78145a5908513dd4fac2b8bca69d859f46567d53b1a5f7d18ea8fe8d76c150bc85ff0cd3ebe613f8e4bb','邓卫东',79);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(80,'教务处','li_x','李霞','W','13770988889','','一级','3796794401','320113198110261649','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(81,'13770988889','li_x','94623a72dfa52ad1d1c50bd4d94d256a895bc9104f029cca590ec16ba7398a812bc99546154aac517ddd4ecf84e26fa780d5759f48535cebf6f80661a9acf898','李霞',80);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(81,'教务处','gao_l','高蕾','W','13951999584','','一级','3796829457','320106197904163225','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(82,'13951999584','gao_l','b5a24a8b5188dd002a88875e947bc0750097dd752f8bd175b96b95cd7681e3ebf5eb1d9c5e4948ff72c5fd1b51f41f8347d9d1da642571246968737e27f5e02e','高蕾',81);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(82,'教务处','zhu_jm','祝夕越','W','13813092106','','一级','3796824241','32012419861205282X','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(83,'13813092106','zhu_jm','e4983258bdab029e2ea4388a17ddae26ec8c3614e7a192a702b662828cff0c3d11348307351f18f36dd1ab68c24e62ed2778c880c29df55ef4d2c431a1df3c54','祝夕越',82);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(83,'教务处','wu_yz','吴雅芝','W','15150530258','','一级','3796822193','32010419880205082X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(84,'15150530258','wu_yz','17f81ed14d75c07c29561d854d48a32104b4558b0af774f62e4098b87e94c293a84b37c7a8444594f93e9263977d7a7c697bd14a36b945cdede6ffab78d376e6','吴雅芝',83);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(84,'教务处','tang_f','汤凡','W','13770840786','','高级','3796815249','321322198701100220','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(85,'13770840786','tang_f','a4cc0d3a61545daf0f5514c10189dc723e9acb070c16f7da6276abd85c2cbea75d78a7491e47c1d0b24fa36b74f35e03c62e2d927f3f573983f41dd7818823e1','汤凡',84);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(85,'教务处','wei_zn','魏之宁','W','18762321479','','一级','3796813345','321284198811040226','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(86,'18762321479','wei_zn','f2e2c0215a243a3208787323462654fb0fcac69188ff1145593044daf705eee7e0f8921c081f92d2f5a65c45bc74b796413b65ded9637bc562a0a8e3567d1a5a','魏之宁',85);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(86,'教务处','chen_x','陈希','W','17372772503','','一级','3796835041','320106199207313223','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(87,'17372772503','chen_x','fb9f86e682dff1f15cde786dbf0e56505471b5f8d8bbc593b6988dcd884c3c55fe1ec4cb976f8d3b1359dedb987f404241150faf942a2d5a2cdb2908e4b140fc','陈希',86);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(87,'教务处','qian_jw','钱嘉伟','M','13951694622','','高级','3796787137','320107197306163416','TEACHER','OTHER','市学科带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(88,'13951694622','qian_jw','24f4c0a57ced21c751e1deb8556441fc7547135ec5e8bb573238980037bd70a466573f32c5427998d714808efefd2a7e1db94b4478925300d2efde32e36848b5','钱嘉伟',87);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(88,'教务处','qin_cx','秦春霞','W','13851600150','','高级','3796782385','320721197409290020','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(89,'13851600150','qin_cx','c7c003c2c80c0bedd3c3b43f5cb5a835424b91ad58c8771c59cef383a4b81fbe43719a81857d7086ca8320aa186e12fa56f7381b90c0b828e8b3cd0303278414','秦春霞',88);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(89,'教务处','guo_y','郭颖','W','18260031512','','高级','3796814977','32032419760102626X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(90,'18260031512','guo_y','97f7e67b5f57e14d2693a98b1f2355913b4b26cf3a56e56acc228ee66b3055c4eb1a916179f99eb5a0a14ca7eb22be7d457527ad50e46d5675809bed974af654','郭颖',89);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(90,'教务处','lv_cy','吕驰云','W','18136688648','','高级','3796848929','320919196807020469','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(91,'18136688648','lv_cy','cae8aca3fcca388a3b250d59d464b56f828ffea7ec1861852bde259450ca8519b2d494f325750929c14d9a1125060967b1ae05fd8af573d56fb8927a7f53ca1e','吕驰云',90);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(91,'教务处','xu_l','徐礼','M','13813980093','','高级','3796805553','320922196412100817','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(92,'13813980093','xu_l','525165f1529f6addc43b81c6516cf6f8601731daeb8d5e2d08cf3598d1cf9ab8f89b7002efe5975510962b254cda0139ea24167155b2af198886eb4d550980ce','徐礼',91);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(92,'教务处','lu_xl','卢向林','M','13813818177','','高级','3796793169','320911196609120951','TEACHER','OTHER','德育工作带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(93,'13813818177','lu_xl','c90bdeea73922a725c4eb23a2d5066dec52ba32f60e0452c9f171b11dc1b9272e00fd4e07c39158fd9b23bdbf51d35b4ac1434a8615de5250c8732a6e4d51700','卢向林',92);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(93,'教务处','huang_f','黄芳','W','13182842858','','一级','3796789313','320107198109061829','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(94,'13182842858','huang_f','c440ee9cfec94f619d5b99909b7deeb873b2f481f178581fe199e9db3fb3e96ea4878d4f125aa5a3a688f68f6d6d3d2cc49be3d8443c09b73b921d0bdad5bafe','黄芳',93);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(94,'教务处','jiang_x','蒋绚','W','13852280042','','一级','3796794817','330421197902110041','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(95,'13852280042','jiang_x','2852f87405e76484a4875e7af90cfef7f04e2722ced21bce69d0d5136c52a8ba433adf11b0fbd565bb06dcde183d79ac53a8aa5a20aad80976f1f735793d3729','蒋绚',94);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(95,'教务处','jiao_bs','焦炳胜','M','13912976802','','高级','3796814721','320105198210211630','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(96,'13912976802','jiao_bs','ffb90f99e201b5d3ea404cf383b865ec6b37aaec649bd9b0a12b155b6fe0ebe9ee264952c1dd7d0c39a3c8270d5f78ec22e41b9468a4e242883223d81b93a4b6','焦炳胜',95);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(96,'教务处','zhu_bj','朱柏瑾','M','15195885282','','一级','3796807713','532130198409270037','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(97,'15195885282','zhu_bj','d512707d9915c6c49af3cd2ba01bb25a9290d2ca7ca0f1da09f454e33f2d64c1535387fbc4594dc8d1e4728f28d0e2905de18292e525df633ecfe102ead96812','朱柏瑾',96);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(97,'教务处','chen_w','陈蔚','M','13813929767','','二级','3796794865','321102198708270453','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(98,'13813929767','chen_w','707d8905b210687a80318b70ea3b9ad81b049a91fc0c4ee2756ac1881c228a102841f808550d0207717c6ea1340b39e5958f322414824c811cc5371d512ba696','陈蔚',97);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(98,'教务处','tang_c','唐超','M','13815416167','','一级','3796833585','320104198810100436','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(99,'13815416167','tang_c','8130b834882a5a2999a931ead45e58549a65faa05d5d0c3ade127ffdde3d691645ff8d30fb65b144c2303188ec11d23546b94a527fea7cdeafb904e0f651545b','唐超',98);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(99,'教务处','jiang_lm','蒋腊梅','W','13851651892','','高级','3796787025','320106196812100425','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(100,'13851651892','jiang_lm','f049b637f2b7e822feade570f00e075e3813f83d80fdff11af406aa30a278ee5c63116bdb55e3387e0c15f808f29abc5f25aed61e15599e892534fba9d98b832','蒋腊梅',99);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(100,'教务处','zhou_y1','周勇','M','13770989108','','高级','3796786481','32108419631026001X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(101,'13770989108','zhou_y1','c8a2d83ee7fcfb130ecaf5237ec8eed115a4d19918be00afe63779688a80c6285eda86d02ecf0c7354a4e9bb3c5d56c71facdfd9bfa60ab39c199322e44d6d41','周勇',100);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(101,'教务处','jiang_gf','姜国锋','M','13951980100','','一级','3796825265','320624197512184318','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(102,'13951980100','jiang_gf','3cf1f55db3098e4119e120a649e5b36c81e1c3ae36da95ce93c5500447d912e2c18a4cd6d9533e36fa6ab3fd79821b882a1c8ba2e61f688d1f6648d079c60834','姜国锋',101);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(102,'教务处','lv_yx','吕永秀','W','13951735396','','高级','3796844817','320706197710140028','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(103,'13951735396','lv_yx','1c17300e9ed234285e2550133506b5f63196f7614f687f1a5117542c23e44ef1873918d8857d1a892c5ba207fd58df6b7db8f8d857e544b1b0c6e94a054a69aa','吕永秀',102);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(103,'教务处','chen_k','陈珂','W','13915960916','','一级','3796818577','320103197809161767','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(104,'13915960916','chen_k','2aaa6ff0c7afa2e48cc2c18d235335537c4dfc503bda0cac87b5fe0eb7b473b90edeaf278e63e4c3219194b0be5956985d96fcd9a277b9e157dc2c9625ba76d6','陈珂',103);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(104,'教务处','li_y','李严','M','13601467560','','一级','3796800017','320825197212210614','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(105,'13601467560','li_y','2a160bf72ff39999771dcb6a66fd134786b46c58561690687112793954dc4d4a94c056986f6244e3b5d6859f5410fd105969780b19d1c77533281b8b7a85d812','李严',104);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(105,'教务处','yang_kj','杨克俊','W','13851486746','','高级','3796831649','320113196710170848','TEACHER','OTHER','德育工作带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(106,'13851486746','yang_kj','49b977d99a82ddd04a91b01b85da1ff5149b2d29c4ddb588235662a18e2b1352199c14fb21ed8cc9cf915657bf3a0018aee23d03e33800c448db854a6e412f65','杨克俊',105);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(106,'教务处','tang_j','唐杰','W','13951904894','','一级','3796793905','320113197808223244','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(107,'13951904894','tang_j','9b53cb8fb385da416fc1a0c7e95783e23a0e06ea362d3eeb284c272d39cce7192fe2f5142faa638177dcd7abb1dd9910140d0f46a5d9eb2f0fba262b4b446ff3','唐杰',106);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(107,'教务处','zhang_ping','张萍','W','13951634544','','一级','3796833921','320102197709171224','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(108,'13951634544','zhang_ping','0fe24299389c049b4d3ca6095f8d67bc7d00605d9bbfe3dda59f36276dee22523c513331acc246242978fe3a4c46f2869b24cd2f4060f276bc7edff80c3e7083','张萍',107);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(108,'教务处','li_kx','李可祥','M','13951651689','','高级','3796789809','320113196505161635','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(109,'13951651689','li_kx','510c243e290e74d32f6a43c4a53813ee5edb225ebfc22c5bc0b80829d77af63139ff8950e43b104d28fd0f00453ab769075380af68fa32d9eb7c887b0f6cbca3','李可祥',108);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(109,'教务处','xie_j','谢俊','W','13770613071','','一级','3796816577','362203198407183524','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(110,'13770613071','xie_j','e57780f9b8b97869cde0bbef00d12257b816d0b343499cb5f580b7a87398db43430fd6edc3c4ff56a08aa3492a5f9d57939a837429fb02b9c8fb40e4909e705b','谢俊',109);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(110,'教务处','xia_y','夏雁','W','13913035999','','高级','3796818401','321002197010081247','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(111,'13913035999','xia_y','f17053e727a358e915db481798c4d696c5ec3ef147b6c3dc60ad7a4df5273f0349d85261230fd3fad51769162b476668a6a2a8867bf2baaf4b3afc142669aa6e','夏雁',110);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(111,'教务处','yang_m','杨梅','W','13776656713','','一级','3796810881','610581198112133744','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(112,'13776656713','yang_m','b5c8be300e00a5aa69b7e4427febe3c81e68d22f8efccfaac4c8b1a6246dc2daf855b6e8814e35337cfad32f2999319f4598d8b6206dbcc8a128f197b99f8016','杨梅',111);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(112,'教务处','ye_z','叶子','W','13675110803','','二级','3796795969','320112199110161689','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(113,'13675110803','ye_z','20473dd1dcc9baef68b1339678745f595a1196055a07c54d54f2172e579a02555ba4672f6d1dc5afa1be8d25d58b8904532b6562ed8ffa2eb4a5c2cfa5a90f32','叶子',112);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(113,'教务处','lin_q','林庆','W','13913942018','','高级','3796805409','320106197010020444','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(114,'13913942018','lin_q','e0a0b3474a40a086017de2feb5f28bacf51b4bb5373070c5fa25f30c9e678f161a3ab1ddc4ffca9f1b261917cc5fabeb54d83eba46786ae7450b28236fad055c','林庆',113);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(114,'教务处','du_yx','杜永秀','M','13675107668','','一级','3796798609','320106197606110432','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(115,'13675107668','du_yx','563164da1abee360115042d2f283bbbb3971686ec603f66ebf8b84db0d48f9063e99654aa18d4a98494dc981aa847b6b30ee5780b511f68d38b6454ed6ed5b7e','杜永秀',114);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(115,'教务处','wan_q','万泉','W','18952092866','','高级','3796799857','320104197001290829','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(116,'18952092866','wan_q','7f00bd0c2ea1e4a347b6b8256792a770e8c98718b5fd9f274905ad9dd7012c36d8095c71dee6eed1c60405ef0196272a0cd941debd5db12492115ddc4478febb','万泉',115);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(116,'教务处','li_q','李强','M','13952042930','','高级','3796799249','321023196211290210','TEACHER','OTHER','市学科带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(117,'13952042930','li_q','5bc5cfa26bfc11cde12ea1008ce914f413361b13ff03b21041057eabd10b22ed7a71df5556c7c9cccae8850b4e43a7700a3779833ec141b8208dc073e78b1e77','李强',116);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(117,'教务处','zhao_hx','赵宏霞','W','15996493248','','高级','3796820353','320113197410102020','TEACHER','OTHER','市学科带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(118,'15996493248','zhao_hx','1b129577998f88e89cb9234c0738cb8948cf7913bb304391d6927661f6b53fb2bb0574288ecbc97d9e4cf5ab0a240a3ad455c43758eb40981b397512e625ed5a','赵宏霞',117);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(118,'教务处','zhou_n','周宁','W','18915960210','','高级','3796808097','320102198002100822','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(119,'18915960210','zhou_n','0074fb3ee68d1160222bdf3b3ab16ec0045d6eebd055ca921c1eb8f8e5cddaf02b7d68204161f29706200a1d43a75d42ad00d0c744d2f170189e36fad006f9de','周宁',118);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(119,'教务处','yang_xn','杨小娜','W','15850785467','','一级','3796803985','130227198705042822','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(120,'15850785467','yang_xn','06bf470a2ad6d421ab26e1ab6d5d6011eff780f035db5da76b7960e214d01e658ad2ab8759f0693ca75f51a21b46b349c53e47991560874620c10e7892122345','杨小娜',119);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(120,'信息中心','zhu_xc','朱熙春','W','13813893053','','高级','3796789233','320103197810112022','TEACHER','OTHER','市学科带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(121,'13813893053','zhu_xc','a89d73dc1d9d6037c3310dc14f3dc7b4ba7f60f06599ac8fd24df20311e864e3c4209194c03113dc6759f2e973509e9a65517d6bf9baf29fe287b33f9feb234a','朱熙春',120);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(121,'信息中心','ge_wj','葛伟江','M','13062516217','','一级','3796826785','320102197812181615','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(122,'13062516217','ge_wj','863de216e55b98dbcb53c39d1f32069a7349b80a1c4e2e7ce26ac2cfc93feaa40eaa8ac59be0b360ffa84e34212bfbff9c0855e1a03526a6b6dddec902b2abc4','葛伟江',121);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(122,'信息中心','zhu_qs','朱青松','M','13851889310','','高级','3796845713','320105197908180018','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(123,'13851889310','zhu_qs','239837b1f83802e849060514d16dc6ca548fad2a7bcac2e6277af52fbe46c720eb0381cc299235eed9adbc81f6c7360f9ee1f153fc6fe3d62bf989917ed123c2','朱青松',122);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(123,'教务处','jiang_yh','姜玉华','M','13952096151','','正高级','3796785873','320104196410170014','TEACHER','OTHER','特级教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(124,'13952096151','jiang_yh','1d2511fe0032b4d3d558a8f84345326cafaf59fc89709c0d732c1e99727494ff226bb2409d768717039662fb5606fb22eceb50203a92816f7da413b2cae17830','姜玉华',123);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(124,'教务处','xu_ql','徐庆利','W','13815864668','','高级','3796807857','320112197903071625','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(125,'13815864668','xu_ql','85fd86ae93068f7895d7f8843df1abd5e36adf3f8465f4a6e225f3057a165b8cf934b4db335a635ecbb72e9ea8303e3e4f6b003f5335b28515edff3dc73bff9f','徐庆利',124);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(125,'教务处','dong_fl','董芳琳','W','18061884826','','一级','3796823009','370785198411078147','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(126,'18061884826','dong_fl','604c4322fe16ce8b98077cbbbba8f572b325f728edbe78bbfbd1995e52ae5d7c481f28bb796bb274b8d31cc1972bd816706d25999ad6609c67b4f131e2632d95','董芳琳',125);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(126,'教务处','xie_hj','谢红娟','W','13451821385','','高级','3796790497','320219197302230264','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(127,'13451821385','xie_hj','ab58b44dab472aa4b75148fa3b1457f259f2cd06ca6918c534475d2de00b9ad22b50e23b5df8cd07f68d66a83d23ac66f205735c195366682089d33013917e33','谢红娟',126);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(127,'教务处','meng_qq','孟倩倩','W','13851790305','','一级','3796829089','320106198409101224','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(128,'13851790305','meng_qq','83e4bc009547db15316412822b54f11c7cf336da4b62adfcf72efc653cffb7265888ab014c5c911459bac4f201a48d112d84e25931cddba24df0faafb8924f86','孟倩倩',127);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(128,'教务处','zuo_yj','左永军','M','13851891197','','高级','3796819217','320302197501054011','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(129,'13851891197','zuo_yj','b15a7d1e41ce70e00bda98a31902812768463042371f4967f0474f0c1d4e36a685538dbfcfe9ad89551ad807477b292b0a1ff798abeceda77c3c3f68f2ae4fd7','左永军',128);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(129,'教务处','dong_gj','董国俊','M','13913974258','','高级','3796846273','320923196402200319','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(130,'13913974258','dong_gj','5fca40674ef58aa3c4aebe5339c1f456bd178c81b7598b0acc9580b604bd21370f0128f10cb8fdccb8eda6c45f60e39a4c0a275e94c32ceb91aec8ecaa9f12be','董国俊',129);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(130,'教务处','zong_y','宗晔','W','17372772870','','二级','3796785473','131002199401083622','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(131,'17372772870','zong_y','326628f4a10441c0de7259b16564226688ce73c4b710cb279c5f1d5452396f309b4aaab53813e86e14242c71e500aafd2e94b6e1006cc276cc5fa0c9bb1e065c','宗晔',130);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(131,'教务处','deng_f','邓飞','M','15996269361','','一级','3796803153','360121199111213157','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(132,'15996269361','deng_f','e21b07a48a3eb01dffe87da592d886132c1eda4150e2c9c045dc03e9605d4bf6142387d1316afe755ba237f0a5fbb3440371817df3c6e69971734620ea038dd9','邓飞',131);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(132,'教务处','wang_ym','汪亚萌','M','15996269153','','二级','3796836289','342921199112161610','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(133,'15996269153','wang_ym','7b5e03c7111308c9930653d6ed0b7616d7de9a65235921683a0ba6ead623ed3b83a73ba67ba127615eb65158756905f01c177a5aa78a9530bcac6cb974eae709','汪亚萌',132);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(133,'教务处','wu_j','武健','M','13705141356','','高级','3796789777','320123197305102419','TEACHER','OTHER','市学科带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(134,'13705141356','wu_j','0415b750fb5d432aec650b3ca5ddc4b7e2fffa3140291d778d8facea0236f600bb10f5e981d8287b911ad1730748786b00e926b1cf6efdf0a8649734151c6a74','武健',133);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(134,'教务处','fang_zy','方泽运','M','13856688815','','一级','3796838769','340881198503033039','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(135,'13856688815','fang_zy','b95627488508964588a385e1170d95cb668b3a956f5252da13becbdace6c1b58dd344d73e5d5efc6f44d14255c0add603d22132e7472b968ff585b9bedddceaf','方泽运',134);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(135,'教务处','feng_m','凤鸣','W','13151535053','','一级','3796833297','320125198210310028','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(136,'13151535053','feng_m','497f57392e2452bdc5581a7f0d5cd0bbd46d042492e880e5f12b3ca9035c98d784f6167dca217cf45df9140f53b17e6a188b8147c63e7f380d4ebfe823ad1e8d','凤鸣',135);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(136,'教务处','song_w','宋薇','W','15161303100','','一级','3796793585','320721198701165440','TEACHER','OTHER','市优秀青年教师');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(137,'15161303100','song_w','15714e2d3af3bfc0b717de63dffa892a3979b0730d786b8d5a358a20c37d16410c797b4733c8c949d764905aead864a04781f10f82e9c25ade537728d3a8b3a7','宋薇',136);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(137,'教务处','zhuang_wp','庄文品','M','13770950264','','一级','3796818945','320721197912015018','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(138,'13770950264','zhuang_wp','7d538de01e116215f0ea215dec4d6d4ab9e7d60e5eb0c21ace9c4893edc36756a87247062270a07dce2e40a84bfec785be80edcbbcc113dbb6ff20cda0b02a05','庄文品',137);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(138,'教务处','chen_xy','陈心怡','W','13813954717','','二级','3796787249','320104199406272820','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(139,'13813954717','chen_xy','9eb885a0fbcf054c93ccfccfe2fa5653b5830615f9acb2fea3d533d54cfe615da26fd0ae0b6fd179f91427f8bc7ff88fc212ce48d08cc9fbc2d5430a95beae92','陈心怡',138);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(139,'教务处','li_yj','李雅洁','W','18602554316','','高级','3796839713','320103198103161268','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(140,'18602554316','li_yj','e7348072d7b8f501304f62e3165de46ceec206a47e207baa208cf629b95b76e902dad2f9e1a937a786216a81f1d52dcf48e5f1e1cb1d67c21aa8162735b0fb8c','李雅洁',139);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(140,'教务处','tang_h','唐辉','M','15060068369','','二级','3796843553','342222199406110059','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(141,'15060068369','tang_h','f5d45bb833b25324b860c2ae709d5a568a354c1249a5055236c64c6316ba869279e59aff13f0a80bfa091cf668ae435f925883a1ad9b10b4b9a79837d4092c1e','唐辉',140);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(141,'教务处','zhou_lp','周丽萍','W','13236500322','','二级','3796812705','362430199509100042','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(142,'13236500322','zhou_lp','eca56f91ddb14432e90045ef7be01a022977ff350af0b42ad12115690c1491fb209f3985ec08eee3c03c7e5ceb54c20321ce41c3f9391446c81235f2ffcbf5c1','周丽萍',141);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(142,'教务处','li_yao','李瑶','W','15552253873','','二级','3796792305','37028119961021182X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(143,'15552253873','li_yao','6b72e84a3ef6dbf290e49b657ce3c32ced727acd9063ea715a2d80604a6d053187e1ea47c4368a359492ad9f1597ba2ab6b026a278e34209d8ff009f4aa61205','李瑶',142);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(143,'教务处','ren_fq','任发青','W','18351863512','','二级','3796824945','320123199411294628','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(144,'18351863512','ren_fq','835586c6ada24ce6ef82fc3dbc18205f505d866e4b9bb144575b0e05ceb6b5d86ca78ecdcba0609dc484da8814169ec33d4f29a22464e29a5427a3bfee55d18a','任发青',143);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(144,'教务处','li_b1','李冰','W','18105160328','','一级','3796829617','410105198803280107','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(145,'18105160328','li_b1','fab6f90de1bc238f3ccd273d7da2c59b85cc72bfa5a580f60aed95cd08c29f5cefa49dbd8047abcc19754c5ef99a23882bf484004866743f49b823dc0c40ad6e','李冰',144);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(145,'教务处','yu_jx','于金霞','W','13969340109','','二级','3796833873','370305199609123725','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(146,'13969340109','yu_jx','ec9815df5a4c82cd1a4e82b8452fbabcf5d967b975f1648be305ca7dbd9e1281d6fc8be39663e4cf71104ce0339cf88e8f2e850edf4a2953e15ee9e8c5a82ade','于金霞',145);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(146,'教务处','liu_jy','刘嘉懿','W','13941529632','','二级','3796783169','210603199607284065','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(147,'13941529632','liu_jy','8de14ce8c0625d04ebbcd234f0ae962ac434b6c7a9776b034869f7c4c71d33059a1b9a1c1d36945db33d7373f40a2cd303780cb7e786cfe2a904e9dec4ada780','刘嘉懿',146);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(147,'教务处','zhang_xx','张笑暄','W','13505168007','','二级','3796791297','320102199608292421','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(148,'13505168007','zhang_xx','5a90aace4b0b7d0d529ee9aea6b665bc23ff5664942b43935a1d8abf90053b849608187818da93ba2660f32eb40f160795020dd07f988fddbead889a6ccee111','张笑暄',147);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(148,'教务处','zhou_wx','周玟汐','W','19967688202','','二级','3796779937','43112219940523002X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(149,'19967688202','zhou_wx','923382639612349b7868d77d3e6a259d9f3c1d204bcef0b1fbf984ae9363411c61823427d72983520abcc82c50ee65b6f53d550a04be08b62dc0468ff459c95a','周玟汐',148);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(149,'教务处','li_mm','李梦梅','W','15651023013','','二级','3796838961','320121199509242145','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(150,'15651023013','li_mm','87a07227994118ecd9c5a8810d1300ed936a684bf95436fc20df6a450f4775b2006c8c6714be6535c292245afe0aa4a92e2b2c313a7d1c46af4d7f4ee8ce5d3f','李梦梅',149);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(150,'教务处','chen_mh','陈慕华','M','15251787787','','二级','3796846913','320125199411034810','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(151,'15251787787','chen_mh','4aa3907cf8f627d83ff49f62c886ffe8b0ad4cd7f66e9049f6e451f5b2cbef076bfb68b9cfc05d989fb4d3c1b08b6d522f2416ac9b2e5a0bcd3a7c948a36226b','陈慕华',150);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(151,'教务处','sun_cl','孙春雷','W','18852071399','','二级','3796845489','320924199602082126','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(152,'18852071399','sun_cl','e3b5faf815896414f442179d513f88dca381e6f1ed32e2c0953a1367618d0373cd3b62746636c4cd9681ebabd3d60820bc061f46e2686fe651c65583cd93a30a','孙春雷',151);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(152,'教务处','fang_jw','方婧雯','W','15651769617','','二级','3796844609','320102199605302428','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(153,'15651769617','fang_jw','b402f22e19c82a86f371dd5d7806f16a9e63278b77bf4477b124428686212e5bdb04739f2f397296508cd894b140576b51371a06ff5f2aa82a6614e04ae7b4e3','方婧雯',152);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(153,'教务处','zhang_y2','张瑜','W','18094202971','','二级','3796824561','320705199510133547','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(154,'18094202971','zhang_y2','c7abe233f36e289191c111f60bc2cc99f69dd1596f1e02334317aa76990aec950395306aa2467f54ba5689ae64a40a63a3603dea6b646e77e32e5c37ad2d1c38','张瑜',153);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(154,'教务处','ma_w','马文','W','13003032881','','二级','3796800769','34030319970213122X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(155,'13003032881','ma_w','9e884602ba14c3e9a570189f3808e35b3e25c9525c6210b989fc2a699f1bb25534dca61c9f50a3de0dd3732fc072af0993ad2da73a00a0ba24ee81b06d6626bc','马文',154);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(155,'教务处','xu_x','徐雪','W','13222095700','','二级','3796836689','371324199701065628','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(156,'13222095700','xu_x','26dbd1f60b2d2553d662f18b88da959927b7e68c0fb0bb52d133ffdbc4bf727f8a7b8d7b994bc4ab4c0f8d8a7ee1fcac36aea54b4ec735464bbd0e22ef7150dc','徐雪',155);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(156,'教务处','he_ss','何赛赛','M','18260729119','','二级','3796789921','320382199601245211','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(157,'18260729119','he_ss','83cb2cc731e4962c6a4052baed608d308c3a6a14626c4771ecbc84be768abf48b63fee6a5d226c1700f2a7de3038dbae31c851cb8edb71a502925fd93459ee75','何赛赛',156);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(157,'教务处','zhang_j1','张佳','W','13657217865','','二级','3796827185','342529199602233823','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(158,'13657217865','zhang_j1','0e1de68746f8f0d9992b24b74eaa32c13bc430450d32cf44bb7707bed4d612b4dfe103a7104ad76027338a5186db672ae42c710eb0f3be33b9496752762b7184','张佳',157);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(158,'教务处','li_aj','李爱娟','W','15156573657','','二级','3796837153','342601199607224626','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(159,'15156573657','li_aj','74cc68914a63a8a74c17cdc590dd102f8175fa2019a3a4afc03299cb0b8497c3b5008f1ac83dd9559185405de8c826965b6c8664e8f4f4f2105f29a3d657a8dd','李爱娟',158);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(159,'教务处','yu_hy','余海燕','W','18326614170','','二级','3796806321','34112219960809402X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(160,'18326614170','yu_hy','396b418982a2d610eacce2f52146098aae35381abe8ce9f0bb1c58815ea461f5a38d831b6af03920b5bf7981036baafd0dd2403221acc92f2cbbf3736916b571','余海燕',159);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(160,'教务处','du_k','杜康','M','18066035985','','二级','3796826849','321324199601140011','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(161,'18066035985','du_k','7a4c01b932d7bf82f0f93a1a312a9dae4ad8524ea1c5f6018215fa203bd84b1152c138af46d5f6ed578609bf291f3e87b7796f73124f0e13163293b38c3a958c','杜康',160);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(161,'教务处','wu_km','吴可蒙','W','18362968361','','二级','1501464666','320322199608158640','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(162,'18362968361','wu_km','9660d81d6163db0b47a5669516ed188c08d3bcd7d28f534d3aa782b3d1e83dcbc13353c9ad914952c55c6e4d491b698e31f06fe12c10d42d70d26b5c65cb141d','吴可蒙',161);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(162,'教务处','liu_y2','刘赢','W','17372772565','','一级','3796821617','320125199007254627','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(163,'17372772565','liu_y2','527f0cae3f9d0c0dc35d68fdb45d6603633cae97acc7180f73efdcea6456f9d29da58d94a1c45aa26db1be1d7ef7b40513c58362dcaa4015a0eed86472c62092','刘赢',162);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(163,'教务处','xu_l1','许磊','M','15195863885','','一级','3796801313','320482198607123814','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(164,'15195863885','xu_l1','5f4c97741cd9a84231a99c88dffd95fff156289b7ba1950d7a26cc0ecb4b4093735315fd12e11976fde9639e0e7782bb975dd671e7fe14230a67644f9fb2926f','许磊',163);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(164,'教务处','zhou_n1','周娜','W','18013864210','','一级','3796799537','321283199302215620','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(165,'18013864210','zhou_n1','1360e57b65b7a376ae13b6a0005b466d269164344831fe0fe940142644350e96b9950a236b1d90d2d5f6a6d317f0b6315489f13dcc80c72236efda019e9b1240','周娜',164);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(165,'教务处','guo_l','郭俐','W','15950530468','','高级','3796811025','320882198207120229','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(166,'15950530468','guo_l','9d3e8ee6a7acda0a5c7cdc81b6cc164823371b6250261e403cb71118b8dea01785df9431657bb06707d7489acc8522eaa32122a19e246af7e51b468fd10284f1','郭俐',165);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(166,'教务处','chen_xy1','陈星羽','W','13770697972','','二级','3796845665','320705199312252027','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(167,'13770697972','chen_xy1','817e68c05c42d8efd86984f8581483dded140578a3660a49819b71af954c0d6703e5aac29f7b77f55f403a7b6857a18d399ef093cd7adf5e7e819055fdc827d7','陈星羽',166);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(167,'教务处','zhi_j','支婕','W','13913910560','','一级','3796825761','340803198706122323','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(168,'13913910560','zhi_j','5d5880b586287664ee79db124751dbe4c4cb041ab59bfdb7815268990640462c7f44b874e6a99317463f988abb3d3f5955a6729c8a496652fdc646b10a35b5c3','支婕',167);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(168,'教务处','xie_jq','谢家强','M','18098470789','','一级','3796780321','340221198607068210','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(169,'18098470789','xie_jq','f74a423f4f49f637397ca9aaea7bb30f7152b4471eb09fe4acb9d6b2f99e7e329eb029addfca48a6b14496c8e422745d3f904a638fdbb406134dbd7a0018ac65','谢家强',168);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(169,'教务处','su_yh','苏义虎','M','15395655229','','二级','3796797361','340881198612245911','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(170,'15395655229','su_yh','f4421bb82582b7f6a6de3bb130468dc623dd7489827a33f02dc73bdbcb31492de4e039141d2c07120ae52c67ce8646b315ff31c18ce7dd6a3b35aa1c05da524e','苏义虎',169);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(170,'教务处','wang_y1','王嬿','W','15805275519','','高级','3796809617','321002197708050020','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(171,'15805275519','wang_y1','60426dcf5509c726e8cab6c72261c490e80e094e6d9264b9e79a9f8d8fdc7773e38863de36021480a32c87d5c13afbde02601127cd4019897633bce20447495a','王嬿',170);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(171,'教务处','sun_jw','孙霁蔚','W','13913858307','','一级','3796825729','320623197902220023','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(172,'13913858307','sun_jw','f77b0a9e54ceadbeeedcc870e0f0147ccd78ac528cb9eed1ad6ff682c83c2af0d5c854c54240ffd0c2105a55f22bef044f023998b76d5f861f792798211ce91c','孙霁蔚',171);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(172,'教务处','wang_h2','王华','W','13776417033','','一级','3796811457','320104198804130823','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(173,'13776417033','wang_h2','e6ab3ab56e71d1459bcfa1e1cb6bf15ac4b1fbf616e113ec9f4b12f92c0ea04a39403e407b228cafb62c1e6731dd278ec5c00da145138d32cf95c0f39c71e1b3','王华',172);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(173,'教务处','ding_wt','丁文婷','W','15950562819','','二级','3796823489','320723198811070824','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(174,'15950562819','ding_wt','95309b4e072efdb8c684774f3a10e6887f4d968e5969227b7e2d3a71b4cbc31f314f7b34f501c5ad95436c356a8e29218839a80ff6035322b8666b411de5d04d','丁文婷',173);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(174,'教务处','xie_ln','谢丽娜','W','18362967975','','二级','3796816881','321281198308241726','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(175,'18362967975','xie_ln','8fdcb72e6383ba5e4efd6916a568180d6b165c334b8bc8960727022088cbdf07baa2b61fe697fcf83f3dc275dd1224acfe55b35293ec3fbc163d355fb8777eb0','谢丽娜',174);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(175,'教务处','xiang_h','项红','W','15380899626','','二级','3796796961','320123198906282421','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(176,'15380899626','xiang_h','2013c9c8b5d48bfd1b46a8f3725fe6bcfba9c13d952f702cfe2d1b74a5f0fe11746dc8103fd546ff5f390aef963d195b963085cd54b9ae070a7aba872c677e0f','项红',175);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(176,'教务处','tang_sg','唐三根','M','13585150230','','二级','3796792273','320106197502150413','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(177,'13585150230','tang_sg','72497fdd9ba4e93b1c6fa317511923cb55baa11cd97838a7803824e1acb04c7d0b41f2a82acee3d291e490be11d402b98cb64908d8d362a59eeb0fd15c4a04ec','唐三根',176);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(177,'教务处','lv_q','吕泉','W','13855558535','','二级','3796815601','340502199005250025','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(178,'13855558535','lv_q','e3a5e3df2b16c241a166c9e9c9f8c274732f365f287489f9726d192beaf077135e9ebf70e4520d9a252baf402402d4f7072f068f80ca4177e03cadde77f1a822','吕泉',177);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(178,'教务处','dong_yj','董玉娟','W','18705151320','','二级','3796809809','341126198401207825','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(179,'18705151320','dong_yj','0785307c468a189e81ffe190090c81a743cc1de359b0e44e8be4ebe7a15b6880505e30a85bd7ecec7df2ca28de669c5477d981c531e6565a8576b604567f1ca0','董玉娟',178);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(179,'教务处','wang_xr','王欣然','W','17714418369','','二级','3796805089','341102199301280420','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(180,'17714418369','wang_xr','7dd5308ba39391bfbc7b1f1ebbca008d6ec84f3189423ef555d9a65e4fed6d7dc8efd62ca270006e6d2ac66d00b6ebac3e36ec8ded96b50de1d1bc8ee0c72b98','王欣然',179);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(180,'教务处','ma_rb','马仁标','M','15162126899','','二级','3796820593','342422199502191435','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(181,'15162126899','ma_rb','a4e65d333edded6757c1b62c23f0ba5b78e2a593a5d0a6695357716aa810581e4efe2e8c1f61fce5490f7caa5e684b5c28437491cfa58c8eb34c34a3c92bfaa9','马仁标',180);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(181,'教务处','xu_hao','徐昊','M','13851458187','','二级','3796793953','320106198811060432','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(182,'13851458187','xu_hao','db60b0ddd1e85945ef8b4ae90353eddc16696e95e25f076f86b1a5ae9c2b316f0ae10a9e57f5180ae03dc4ee209535b5ecccb148f99ce157ff19a25f3aab8a73','徐昊',181);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(182,'教务处','wang_n','王宁','W','18905184505','','二级','3796848849','230621199111170220','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(183,'18905184505','wang_n','cb860d49e340f860b69d37fddeda1048dcd207d524ebcaeb5402327302ca0f2e5449d86e5adf8dc2e3449e846b9d2808a4d5220363b450be5b328f48c11e4adb','王宁',182);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(183,'教务处','huang_yq','黄砚清','W','13813811449','','二级','3796848913','320104199010151627','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(184,'13813811449','huang_yq','64a9d31476e994dbc54329b91a7ae9e737f4a43e4fab3eb1b7c2bc04e12f2504682048478486948494cdec3ec2bbdc5c84c78ce07477d7faabca07a996f65c73','黄砚清',183);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(184,'教务处','rong_hx','荣合兴','M','18061726905','','二级','3796848385','372922198410303290','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(185,'18061726905','rong_hx','e01cfff00c80260b092bc07f1cb5c5701c9d31ebbd4f15b41592ea3bdcd0563e74725a711fefeebee70cd92f9cbd04abd2a59473c8b1e4bb0be70c7ea246da60','荣合兴',184);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(185,'教务处','yang_xy1','杨晓英','W','15951812030','','二级','3796802321','142625198308081740','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(186,'15951812030','yang_xy1','6c33f3ea25a13a6fe723420a95d857eda88e0f58b90a9d437f0fdd0cca9cc4453af3a4469a1beb3f8bec8ea9571a3a1cbbfc78bb2ac863410a6a170266ff588d','杨晓英',185);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(186,'教务处','han_l','韩璐','W','18652029784','','二级','3796812369','320104199411140021','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(187,'18652029784','han_l','a902b439be77da99d9f16d29c0688df4b1bf68235fddc7e20fa82408eec589b46aedd8eb16467868ddcb7fa890f9cc983d355324cbc85120b2da28e17dd9ee4b','韩璐',186);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(187,'教务处','chen_xa','陈学安','M','18771187973','','二级','3796842945','421122198608122137','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(188,'18771187973','chen_xa','d631b3de96a6ece65a8d0aa057c90e383e805830bcbbbc6bd485f8038c52c4b7a80a8836e6801fa58dc0f3d0b9ac7c1ab6ca28dafbba5f85fe727aa8a9d8f0ce','陈学安',187);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(188,'教务处','wang_l1','王立','M','18115622723','','二级','3796837905','320103199712222278','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(189,'18115622723','wang_l1','88dd1de054bf24eff564acf65b7a430f4dd19cc5442da4c0830446427994b029627384984e8c95dd68cc18434f0549fcd489158a5351fcd3fa665aec9b4e7b51','王立',188);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(189,'教务处','wang_xx1','王晓雪','W','18921448001','','二级','3796798753','23230119880414782X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(190,'18921448001','wang_xx1','63a900a53b577e87690b227b496656dbbb434b6c93b622dac693039f42e96c60b6648fcbc80c37396b247223f8db1f184fece0fc804cc2670a371a4d704b9bcc','王晓雪',189);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(190,'心理健康教育中心','sun_ww','孙卫巍','M','17626041204','','二级','3796804577','320723199912044259','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(191,'17626041204','sun_ww','68b34ae9d98a37c86050d5e097d27e1173aa461659fff500e651a79d6884614394e637c5d64cde7a502e1ce39eb4d271a715826791ab470569d9e6e4e91d072d','孙卫巍',190);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(191,'教务处','zhao_rf','赵瑞芬','W','13913305185','','高级','3796839121','320622196512104544','TEACHER','OTHER','市学科带头人');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(192,'13913305185','zhao_rf','272cf3ace2baa497f87b19ffb76c2f6722e6e6de1f96383f334846ef25149b2392810ac996d0f867613daabca51ba0525187035f06c129501bb11c2f0787a3ea','赵瑞芬',191);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(192,'教务处','wei_ep','韦恩培','M','15851850517','','高级','3796792673','32082919701217145X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(193,'15851850517','wei_ep','b742e87c63aa4b7405ad0be5a56fc52501386ae479b2c5c9db5c36201ea538ffbd7c15eccfc3db144131b0696e3f9ec85ae47bf6614ca86f3ab4cfa288c55920','韦恩培',192);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(193,'教务处','ma_sl','马士龙','M','15952356076','','高级','3796805265','341281198208263171','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(194,'15952356076','ma_sl','d262b860267459d1def6ec89fefb1303a7812931990163d4367d320cf8882645ca020dfbdd0fe3e4ac65cc9dc496d07e5d557caf2a15c3be14e655394e94131a','马士龙',193);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(194,'教务处','qian_sn','钱胜楠','W','13675169101','','一级','3796835441','320623198207060042','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(195,'13675169101','qian_sn','133054f5d71a3972b902fb56617b895a34f045d1c135ff614840a14529fc99faeeedf2029d40c11637c763e8d62af840a9e70c406b397949585dd5d94dd68e1a','钱胜楠',194);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(195,'教务处','su_jj','苏锦娟','W','13814071201','','一级','3796812209','32088119770825702X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(196,'13814071201','su_jj','e14ac903a06bd6b1b6f80d8df1065bcb30795c5721b0aaa39a7ca8d0e0023306397cccfb57074381ba628f6666b562e22866e16a4bf21fdef7cbb0bdea224e46','苏锦娟',195);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(196,'教务处','liang_b','梁波','M','13016956684','','一级','3796843793','32083019871011281X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(197,'13016956684','liang_b','7f0af3d0f7d218b5cf35f2ca0453c5891b6f94827500a8f3f23b08401ae7812994efa860dede7d5386cefe3a5f745fbdabaeb737e51f0f6c0e0f2c5560f5b988','梁波',196);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(197,'教务处','zhang_fl','张风丽','W','13851459266','','一级','3796847073','410621198307022544','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(198,'13851459266','zhang_fl','ca082b431190a7ddf7f2a189403ac5c098b4cc7f0291944eebdb0376832f5bb07939eda4b84898a566fa9a9e3081c7465a5f45de86c3caa0d031b1f3eae9c217','张风丽',197);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(198,'教务处','chen_mp','陈茂萍','W','13805187635','','一级','3796807313','321002197811191227','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(199,'13805187635','chen_mp','545634298a7dc1b453db639436fe22ec03b355f2b7395c8bab33368568634ccd28b9f5c21226d3d58ee292db0eaf21a482670d2add6a62cbfcf6fcacf06723f8','陈茂萍',198);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(199,'教务处','zhou_cx','周朝迅','M','15905145677','','一级','3796845057','321023198904016037','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(200,'15905145677','zhou_cx','757dcac08892bcf7ca68e8d603ad656f3d26a7c0da0b878b1e6f69669a497ef111f16b4b621217bf00f664415c47e04f5403f9cb999a602b342989895db460f7','周朝迅',199);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(200,'教务处','qian_juan','钱娟','W','13814064192','','一级','3796823297','321085197911087428','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(201,'13814064192','qian_juan','580c84869a380a85667d455d2168b3e44f3eab4a75fce0d7f4cfb2e464defa82870a3086321fbc20efca75c3661d765799c1759a664b566a617cc4b667c25272','钱娟',200);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(201,'党政办公室','qin_ss','秦珊珊','W','15895958131','','行政教辅','3796816385','341222198309090281','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(202,'15895958131','qin_ss','761cf9aac7f32c90cb84878644948b10565fb24f28be66e7e7f0d0750fb746e9ed6cac7182f9ccf0e0d03eca46669d4a5e7d015966d708f1a37c103bfeb3dbd2','秦珊珊',201);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(202,'总务处','hao_zq','郝正清','M','13512506411','','行政教辅','3796790001','320106196710292470','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(203,'13512506411','hao_zq','8dcb5ac7b104c2e2cbb85420963323e70e08b9c5640a47090529504fcb5b9d2b0792bec24876c89c441c944b3cbffb78b21e5bca8b6a21cb1d39f2e9b4a5d50d','郝正清',202);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(203,'党政办公室','fang_cq','方承清','W','13770965006','','行政教辅','3796845361','320121198102123924','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(204,'13770965006','fang_cq','aff608d5e4f57fa90db18cbc00cd90b2affa1d7a19857aa2bc6d81ca1ae30b86e654026555cb1f887bec8ee28fa0a6602adea2dc1840a7a25cabdfd8e1487b79','方承清',203);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(204,'总务处','ji_m','吉鸣','M','13605159319','','行政教辅','3796842657','320104197101171210','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(205,'13605159319','ji_m','134eab1015029abba1c49bd77e0b1ad813ac35ea061f59145f1ead1e4a4470eee9764dcfcf60855b775cd0b47f922fb98a53e3b3142a5a8332aed12494b7ba5e','吉鸣',204);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(205,'德育处','liang_yz','梁裕柱','M','18651827859','','行政教辅','3796807665','321002196512110912','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(206,'18651827859','liang_yz','8531a1662c300369b69d8d81db3209a44cb32a002f198d884d16e70417917316341a0c69510961118ad37ecfaabeb31d0cd80f7a4abd6ba03276d85b88d29c6b','梁裕柱',205);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(206,'德育处','hou_bj','侯宝金','M','13814142590','','行政教辅','3796836161','320103196605062017','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(207,'13814142590','hou_bj','7ec0ec8d2692eaa6ab8d900d0972e4aaa0cded09e32492e30c38767e9350ae0065f202eb59988252d041a5f81717c4a7ce83d236b580b74e04e847a637680cc0','侯宝金',206);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(207,'总务处','tang_cl','唐春楼','M','13951835980','','行政教辅','3796826657','320103196304210530','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(208,'13951835980','tang_cl','c0954cdd5fe94ec0db622d39e8c0dd383a1abb441b60c4d644010cc0b7673b763f55de731cf7bc39e6e27ece574210b40f4b5a59d8ab45e89332ac9061daa60d','唐春楼',207);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(208,'总务处','wang_h','王虹','M','13951616237','','行政教辅','3796839009','320105197210041411','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(209,'13951616237','wang_h','2b17a5ef74493c7862bfab0ba2240d69941e31d938b0c01faf8bfe5893a3f6b7fecde42b1e4a66f149626b0fe48da54f74121bb222c3328ce2265f21d82af691','王虹',208);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(209,'德育处','chu_rb','褚仁宝','M','18913381586','','行政教辅','3796801537','341122198012151615','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(210,'18913381586','chu_rb','b1595bc65dcfcacc7db140f9bfcde6f255419450f8b396d0396e7fa9076dc300e0f7c0d7663d94ebc0cac37311a63f9fb8ce56e76ea5b327918ee50e27201e2f','褚仁宝',209);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(210,'信息中心','zhang_h','张航','M','13813869128','','行政教辅','3796805841','320106198004221615','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(211,'13813869128','zhang_h','916f193d67a08fd3ee23f8327e9b4929782cd2e754236ff2f90e88c124daf9ae4b680a3b54d976a6f4092a63731074ea89cb8c935a57d24e981a412a2d834cf2','张航',210);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(211,'总务处','shi_wd','史伟东','M','13337729955','','行政教辅','3796796721','320106197203141218','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(212,'13337729955','shi_wd','84038cdbe3f3caec6752f3c4cb330c599fa277e398870327874fddbe7fa06e9fc3397b212eeabde228156f107ccde203f40fb504aaec78c1be95732125decc69','史伟东',211);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(212,'德育处','zhu_y1','朱瑜','W','18705170489','','行政教辅','3796812577','341122197509120043','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(213,'18705170489','zhu_y1','94817d48ba02845586311fd66a7639af0fb7471cf7eaf2f7c5127c700a579105ed4d8ae458797b904d5dd35468508cf58ba4b723ed1a15fd0cffa97a019cd003','朱瑜',212);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(213,'德育处','lu_q','陆琦','M','13951902068','','行政教辅','3796834593','320106196603230434','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(214,'13951902068','lu_q','151096a3bb145794de8eba5b37e4023e19a82c3304c2d7fc5c9ff58ed1e260ba1d7add5add1507e99b75f831542f460af016fa2750f3fc7ba1c754bfc82a4957','陆琦',213);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(214,'德育处','wang_sc','王世存','M','13601466369','','行政教辅','3796786769','320621196111050059','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(215,'13601466369','wang_sc','1aa56aeb8dd282474dc72a6cb4fb152c88cda0109d26188ba9742158627aa9097194984579e0ed41c9793e5bf02b91e414902f7b999ed8a24cd7f2ab4d2bd787','王世存',214);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(215,'教务处','chen_y','陈莹','W','15150554468','','行政教辅','3796802753','320125199102084320','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(216,'15150554468','chen_y','3c5450610cb66950a57f6fd14e268313a199c0507686315d722210b1f96ce4188d498343f978a3d7986817d5e6c3d7079a382543c64d93363a1b35b30f84bd0d','陈莹',215);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(216,'教务处','wang_yj','王亚娟','W','15715169287','','行政教辅','3796830321','412702198410224564','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(217,'15715169287','wang_yj','e39ddafe31682e0e8940015c73136888c42be006e5d3e0d66f6e92408349532ae1c8cdd00e65a787f4952a96fdb50f5ef8e96c989befef0395e4c13923823462','王亚娟',216);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(217,'教研处','liang_c','梁川','W','17372772720','','行政教辅','3796821281','320105198903201224','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(218,'17372772720','liang_c','9267925c5ee3d5e770fde07203d87939a1bb4d8525cbd3a0538f7a4c8b925d4edbbe98eecf7b348452ac92bcf0663f5a7842047337cb30725e5e78ab5412e056','梁川',217);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(218,'总务处','li_li','李黎','W','13655199059','','行政教辅','3796809777','320114198404151847','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(219,'13655199059','li_li','b05cd4f8385cba1b91650be64fa36767f09557c4ce5483e356a62f1491a62ba9afddec8e7fc71aed7365edeaaf1b7182d40bd4524e451a792d609b22d4e40488','李黎',218);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(219,'德育处','lv_yue','吕悦','W','13813887735','','行政教辅','3796825873','320104198312251223','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(220,'13813887735','lv_yue','250287840e8e24032933c1fb3e7ffa143ca7504420c76d4514dcf98bf4b2b488b5095556b8d9e0e1713c5adf1572238c272334ba707b58ea3e8ea20c6ef3c4d5','吕悦',219);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(220,'教务处','zhuo_s','卓姗','W','17372772705','','行政教辅','3796839233','320303199205011223','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(221,'17372772705','zhuo_s','93337e1bb059fafc6c243d0848444907a5b8177ef57552b4c497b4b097f888aacfb0a2b175ff08f97538e8b042366416019bca6fada5b286db080f63d69f0be2','卓姗',220);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(221,'德育处','hou_xx','侯祥霞','W','15365046160','','行政教辅','3796835889','320121197112293527','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(222,'15365046160','hou_xx','72959ce8d76db147bcb0c28ad0026090d64d3f74a36102181fa5d5dd3782264e3a902a2606d1f4c108f1d01938732d973671e498213dd0be6d71065aefa1e386','侯祥霞',221);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(222,'德育处','rui_ql','芮巧玲','W','15951741298','','行政教辅','3796849377','320121197206200029','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(223,'15951741298','rui_ql','ad67f582d096e13b5472749735f935a829e5babaabf18f05eff70794a7f6a562704570e3980a4ccf9df9b063355035cb05baa815c550836a0299d923e899f33b','芮巧玲',222);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(223,'总务处','wang_zm','王志梅','W','13382029998','','行政教辅','3796827713','320123198003130021','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(224,'13382029998','wang_zm','a5bbfe73ad1b9368f5c0d3536e1bc184df3fe97539db680b1d395ae44f9351f2898bf8e59f438affe0040ba9d1106591592f859170ec5906481fc4fce210f133','王志梅',223);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(224,'总务处','shen_q','沈秋','W','17372772910','','行政教辅','3796829553','320105198210251624','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(225,'17372772910','shen_q','52daa557451f94dca3d050ff23848f18f8702daec5d84d11d9aab008d80489bbab97dfe0f6d9e1f0dd9b9620be5a87305bd30ec9c21cffebfd1502c041604a19','沈秋',224);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(225,'总务处','zhang_hj1','张颢珏','W','18061858957','','行政教辅','3796823841','320113198301241229','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(226,'18061858957','zhang_hj1','692f20339b3a41c12bf26b6c9f1ffd41cfdd35b9cdba361b7afcd815a7d0cb91f1c4461257c2a3d981677c90423c934efb5e293580bd152a894f32a9a25c7105','张颢珏',225);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(226,'德育处','liu_mf1','刘明凤','W','13913818732','','行政教辅','3796831745','320121198302203945','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(227,'13913818732','liu_mf1','d61e0c049b2f5c5fad2f2aa20755db371b1894bf71354445c4398305018530ff78adbcc6ec75926fdcbe97b7aab796566444074207bd234443cab8fd42fa9eb9','刘明凤',226);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(227,'信息中心','zhang_yc','张奕苁','M','18651611421','','行政教辅','3796809905','320102199301231659','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(228,'18651611421','zhang_yc','071a8626a1ed0c88b62399f619b550544e555a104e2e0d5b20a4ff67c34b8e1aa6f89aec0f1c4ebf16356c6ab950ae26124c904fcebcf828d63d2cc9bfba78f0','张奕苁',227);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(228,'信息中心','qiu_ly','裘乐益','M','13505196826','','行政教辅','3796847617','330683199006010017','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(229,'13505196826','qiu_ly','2ade2f6f11e90b1fb0755c06553de7114940b16ab732583dc1a76a211dcdfcb82962d62b37242ba7655f11211792e6daf0b156f92de51fe4418852bf3c92d735','裘乐益',228);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(229,'教务处','pan_j','潘婕','W','13585136965','','行政教辅','3796808145','320802198204142025','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(230,'13585136965','pan_j','3f08c24986d3b3d4b439cf2b9bbefc78874abfd6d82c6da53301cc7a02986713f96aef08fbe0817415af2497455b46ce4a14adfc6fddac75dcd62ec42d0de3e9','潘婕',229);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(230,'德育处','gen_jq','耿建青','W','15195824797','','行政教辅','3796817057','320123197503161022','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(231,'15195824797','gen_jq','8afa99685ed903f33105e1bc09076cfe80ad75be3e870cbadecc919a8b74fc5ad0538f593721210c534c46649187587876f05910e4cf3fbe48a7674b92a89e61','耿建青',230);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(231,'总务处','wu_l','吴亮','W','13813969811','','行政教辅','3796838993','320105198002051249','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(232,'13813969811','wu_l','450861b2753c55dd5be1fba036f4bd9709b13a1032f621072ee6f847fa2902e01b9c8b29f2a6579941c08c8eea45b66d67b171fa60b6ba13e1d1126d609a5817','吴亮',231);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(232,'德育处','cai_zj','蔡志娟','W','13611564137','','行政教辅','3796827249','320123197911231026','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(233,'13611564137','cai_zj','d56e444f8369235068d2208703a24b8a3f226cf00a7b7a863a01fce24e384bbf23fdb120b1cf62063294493a08df12b1a041e5d5056e61cc137cb688c2a3e9e2','蔡志娟',232);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(233,'德育处','gao_yp','高云鹏','M','15312992325','','行政教辅','3796789633','321121199203250717','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(234,'15312992325','gao_yp','c03796ff3b4a386717607d9e0ab822bcbaf32ddfd14175196da69716ecd7c9a35734f654a2397e39f30838093f73a241d82f645ff0f2b412565cf46101c51f73','高云鹏',233);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(234,'德育处','peng_hf','彭海峰','M','13912955425','','行政教辅','3796849169','320621198006300019','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(235,'13912955425','peng_hf','caae1594d60d5c1c8d5d8e09b43a4bdf15b614a31e015e0f21a05410f132a4fa40a9afd488b4502df8eeafda64b7f1c7cd11045f37910171d3190c0620305165','彭海峰',234);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(235,'德育处','wu_h1','吴华','W','18921429928','','行政教辅','3796784097','341122197608161641','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(236,'18921429928','wu_h1','1096ec6a2480a96214c172cc01234a99270b9d36a15eb23ce52302d21ad55bb7ad576023017f517f67bb0950368b9c5df66b3a4cc9b11755080d77fac44f62de','吴华',235);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(236,'教务处','liu_yx','刘亚雄','W','17372772895','','','3796806673','42010219640224124X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(237,'17372772895','liu_yx','738cccf56d092887f413b54dd493e87a2f43f9a5a80819065612355c082414efe1ff3fb72d0590851dc18842e3a3500f63cd7936e4b20965ccfa83f87d09b21e','刘亚雄',236);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(237,'心理健康教育中心','ma_db','马杜彬','W','18851692815','','','3796795841','330382199612103122','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(238,'18851692815','ma_db','5657ba9a851fe05ce0a33689372a0b6a39ca322cb7edec601f922116c98083567071702944627265c81523b4f7a8da948f9bf2c84021e8e9620004649bbd88f4','马杜彬',237);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(238,'教务处','zhang_gy','张桂苑','W','15735648623','','','3796836321','140521199710300042','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(239,'15735648623','zhang_gy','3d811db55272c34144d1322bab24050bf56a6f5a18eadd55d051f61916701367276a34e39ede79767c24cc46ebafad83a856a15d3952e495610c1c200d44c9e4','张桂苑',238);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(239,'教务处','zhang_hm','张红梅','W','18952091066','','','3796802897','320111197202110429','STAFF','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(240,'18952091066','zhang_hm','61019adb62ca1bede9b4e61e8e698fc6761d9f64d7c80db38cb265b49457d2715f7705a4297175242d6ed1e3bad151eb01b614203020032d811c265ac62113ad','张红梅',239);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(240,'教务处','zhang_hf','张惠芬','W','13813971886','','','3796841201','32010419610514002X','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(241,'13813971886','zhang_hf','f9e5ba6ecf269213e37a714c6e60ace82462f087ef1aeb4c64404e19ccb697f4374ee3d8e6800d38c691f61e6bea46c595bd4f1464a8516bbeda88232ff13323','张惠芬',240);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(241,'教务处','chen_jj','陈加军','M','13401920597','','','3796818529','320830196205271618','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(242,'13401920597','chen_jj','5fac97520a74d0edccda9470d0c3903393d3eafcd6f5c99cdd925354c0bbb16de5dfd72d1f9938e7adaa34a6e8a6bb00447c3cf46149453ce11a448598513c65','陈加军',241);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(242,'教务处','wang_xp','王小平','M','13851546673','','','3796847121','320103196010182079','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(243,'13851546673','wang_xp','f2dfb6d4e7b8310ee9de7dbcfcb18fa9b18f54809646afdf328da24d0838453f682913cdd15dc335f431108026bfcefa524c0d9ede3c78edcffd06f910e4d5ba','王小平',242);
insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)
values(243,'教务处','zhang_hl','张鸿亮','M','13002590158','','','3796782353','320102195707161218','TEACHER','OTHER','');
insert into sys_user (id, username, login_number, password, real_name, staff_id)
values(244,'13002590158','zhang_hl','a53da14c8cadb7a7cf746eeffb3c0f7d757d0fdd431256f28fcbd9a6ed41f2b4de04695b46b1ff08422db14783a039bb330af3f90562496aee4b738fd38fb8dd','张鸿亮',243);

truncate table sys_user_role;
insert sys_user_role (user_id,role_id) values(1,1);
insert into sys_user_role(user_id, role_id) select su.id, 3 from sys_staff ss left join sys_user su on su.staff_id=ss.id;
