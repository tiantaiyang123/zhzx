drop table if exists `sys_application_app`;
create table `sys_application_app`
(
    id int(11) auto_increment not null comment 'Id',
    parent_id int(11) not null default 0 comment '父级Id',
    type varchar(50) not null default 'MENU' comment '权限类型 [MENU.菜单 BUTTON.按钮]',
    name varchar(255) not null comment '名称',
    code varchar(255) not null comment '编码',
    path varchar(255) null comment '跳转路径',
    hidden boolean not null default false comment '是否隐藏',
    hide_children boolean not null default false comment '是否隐藏子菜单',
    icon varchar(255) null comment '图标',
    sort_order int(11) not null default 0 comment '序号',
    editor_id int(11) not null default 1 comment '操作人Id',
    editor_name varchar(255) not null default 'admin' comment '操作人',
    create_time datetime not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='手机app应用配置表';

drop table if exists `sys_application_role_app`;
create table `sys_application_role_app`
(
    id int(11) auto_increment not null comment 'Id',
    role_id int(11) not null default 0 comment '角色Id',
    application_app_id int(11) not null default 0 comment '手机应用Id',
    primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='手机app角色应用范围表';

drop table if exists `sys_user_application_prefer_app`;
create table `sys_user_application_prefer_app`
(
    id int(11) auto_increment not null comment 'Id',
    user_id int(11) not null default 0 comment '用户Id',
    application_app_id int(11) not null default 0 comment '手机应用Id',
    sort_order int(11) not null default 0 comment '序号',
    primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='手机app用户首页应用表';

create unique index ix_code on `sys_application_app` (code);
create index ix_parent_id on `sys_application_app` (parent_id);

create index ix_application_app_id on `sys_application_role_app` (application_app_id);
create index ix_role_id on `sys_application_role_app` (role_id);

create index ix_application_app_id on `sys_user_application_prefer_app` (application_app_id);
create index ix_user_id on `sys_user_application_prefer_app` (user_id);

drop table if exists `sys_schoolyard`;
create table `sys_schoolyard`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  name varchar(50) not null comment '名称',
  faculty varchar(50) not null comment '学部',
  create_time datetime not null default CURRENT_TIMESTAMP comment '创建时间',
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='校区表';

drop table if exists `sys_dormitory`;
create table `sys_dormitory`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  name varchar(50) not null comment '名称',
  building varchar(50) not null comment '楼栋',
  floor varchar(50) not null comment '楼层',
  create_time datetime not null default CURRENT_TIMESTAMP comment '创建时间',
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='宿舍表';

drop table if exists `sys_student_dormitory`;
create table `sys_student_dormitory`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  student_id int(11) not null comment '学生id',
  dormitory_id int(11) not null comment '宿舍id',
  bed varchar(50) not null comment '宿舍床位',
  is_default varchar(50) not null default 'YES' comment '是否启用 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP comment '创建时间',
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='宿舍学生表';

drop table if exists `sys_academic_year_semester`;
create table `sys_academic_year_semester`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  year varchar(20) not null comment '学年',
  semester varchar(50) not null default 'Q1' comment '学期 [Q1.第一学期 Q2.第二学期]',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  is_default varchar(50) not null default 'YES' comment '是否为当前学年 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='学年学期表';

drop table if exists `sys_subject`;
create table `sys_subject`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  name varchar(20) not null comment '科目名称',
  has_weight varchar(50) not null default 'NO' comment '是否为赋分科目 [YES.是 NO.否]',
  is_main varchar(50) not null default 'YES' comment '是否为主要科目 [YES.是 NO.否]',
  max_score int(11) not null comment '总分',
  subject_alias varchar(20) not null comment '学科别名',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='科目表';

drop table if exists `sys_subject_`;
create table `sys_subject_`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='科目表';

drop table if exists `sys_grade`;
create table `sys_grade`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  schoolyard_id int(11) not null default 0 comment '校区ID sys_schoolyard.id',
  name varchar(50) not null comment '名称',
  grade_leader varchar(255) null comment '年级组长 多选 用[,]分割 sys_staff.name',
  team_leader varchar(255) null comment '教研组长 多选 用[,]分割 sys_staff.name',
  academy_ratio_a int(11) not null default 0 comment '学业等级A等比例',
  academy_ratio_b int(11) not null default 0 comment '学业等级B等比例',
  academy_ratio_c int(11) not null default 0 comment '学业等级C等比例',
  academy_ratio_d int(11) not null default 0 comment '学业等级D等比例',
  academy_ratio_e int(11) not null default 0 comment '学业等级E等比例',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='年级表';

drop table if exists `sys_clazz`;
create table `sys_clazz`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  academic_year_semester_id int(11) not null comment '学年学期 sys_academic_year_semester.id',
  grade_id int(11) not null comment '年级 sys_grade.id',
  name varchar(50) not null comment '名称 sys_label.classify=CLAZZ',
  student_count int(4) not null default 0 comment '学生人数',
  start_year int(4) not null default 0 comment '入学年份',
  head_teacher varchar(50) not null comment '班主任 sys_staff.name',
  clazz_nature varchar(50) not null default 'SCIENCE' comment '班级性质 [LIBERAL.文科班 SCIENCE.理科班 OTHER.不分科]',
  clazz_division varchar(50) not null comment '班级分科 多选 用[,]分割 sys_subject.id',
  subject_level varchar(50) null comment '学科水平科目 多选 用[,]分割 sys_subject.id',
  clazz_level varchar(50) not null comment '班级层次',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='班级表';

drop table if exists `sys_clazz_teacher`;
create table `sys_clazz_teacher`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  name varchar(50) not null comment '课程名称',
  teacher varchar(50) not null comment '任课老师 多选 用[,]分割 sys_staff.name',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='班级任课老师表';

drop table if exists `sys_department`;
create table `sys_department`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  name varchar(50) not null comment '名称',
  principal varchar(255) not null comment '部门负责人 多选 用[,]分割 sys_staff.name',
  parent_id int(11) not null default 0 comment '父级ID sys_department.id',
  sort_order int(11) not null default 0 comment '序号',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='部门表';

drop table if exists `sys_function_department`;
create table `sys_function_department`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  name varchar(50) not null comment '名称',
  sort_order int(11) not null default 0 comment '序号',
  parent_id int(11) not null default 0 comment '父级ID sys_function_department.id',
  principal_id int(11) not null default 0 comment '负责人ID sys_user.id',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='职能部门表(用于一日常规意见与建议)';

drop table if exists `sys_staff`;
create table `sys_staff`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  department varchar(255) not null comment '部门 多选 用[,]分割 sys_department.name',
  employee_number varchar(50) not null comment '职工号',
  name varchar(50) not null comment '姓名',
  gender varchar(50) not null default 'M' comment '性别 [M.男 W.女]',
  phone varchar(20) not null comment '手机号',
  education varchar(50) not null comment '学历',
  title varchar(50) not null comment '职称',
  card_number varchar(50) not null comment '一卡通号',
  id_number varchar(50) not null comment '身份证号',
  compilation_situation varchar(50) not null default 'ORGANIZATION' comment '编制情况 [ORGANIZATION.在编 SCHOOL_EMPLOYMENT.校聘]',
  personnel_situation varchar(50) not null default 'TEACHER' comment '人事情况 [TEACHER.教师 STAFF.职员]',
  is_delete varchar(50) not null default 'NO' comment '是否删除 [YES.是 NO.否]',
  send_message varchar(50) not null default 'YES' comment '是否发送值班提醒 [YES.是 NO.否]',
  function varchar(50) not null default 'OTHER' comment '职能 [DEAN.教务处 PRINCIPAL.校长 COMMITTEE.团委 MORAL.德育处 OTHER.其他]',
  honor varchar(255) null comment '荣誉',
  wx_username varchar(255) null comment '职能',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='教职工表';

drop table if exists `sys_staff_message_refuse`;
create table `sys_staff_message_refuse`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  staff_id int(11) not null comment 'staff_id',
  name varchar(50) not null comment '名称',
  remark varchar(255) null comment '描述',
  status varchar(50) not null default 'YES' comment '是否拒绝接收此类消息 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='老师信息接收管理表';

drop table if exists `sys_staff_category`;
create table `sys_staff_category`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  parent_id int(11) not null comment '父类id',
  name varchar(50) not null comment '名称',
  remark varchar(255) null comment '描述',
  create_subordinate varchar(50) not null default 'YES' comment '是否允许创建下级 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='老师分类表';

drop table if exists `sys_staff_category_relation`;
create table `sys_staff_category_relation`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  staff_category_id int(11) not null comment '分类id',
  staff_id int(11) not null comment 'teahcerID',
  parent_category varchar(50) not null comment '名称',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='老师分类关联表';

drop table if exists `sys_staff_message`;
create table `sys_staff_message`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  staff_id int(11) not null comment 'teahcerID',
  staff_name varchar(50) not null comment '教师名称',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='老师微信表';

drop table if exists `sys_staff_message_receiver_relation`;
create table `sys_staff_message_receiver_relation`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  staff_message_id int(11) not null comment 'sys_staff_message.id',
  staff_id int(11) null comment '职工id',
  student_id int(11) null comment '学生id',
  receiver_id int(11) not null comment '接收人id,学生或者职工id',
  receiver_type varchar(50) not null default 'STUDENT' comment '类型 [TEACHER.教师 STUDENT.家长]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='老师微信发送人关联表';

drop table if exists `sys_staff_subject`;
create table `sys_staff_subject`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  staff_id int(11) not null comment '教师ID sys_staff.id',
  subject_id int(11) not null comment '科目ID sys_subject.id',
  is_current varchar(50) not null comment '是否当值 [YES.是 NO.否]',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='老师科目表';

drop table if exists `sys_staff_clazz_adviser`;
create table `sys_staff_clazz_adviser`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  staff_id int(11) not null comment '教师ID sys_staff.id',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  is_current varchar(50) not null comment '是否当值 [YES.是 NO.否]',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='班主任表';

drop table if exists `sys_staff_lesson_teacher`;
create table `sys_staff_lesson_teacher`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  staff_id int(11) not null comment '教师ID sys_staff.id',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  subject_id int(11) not null comment '科目ID sys_subject.id',
  is_current varchar(50) not null comment '是否当值 [YES.是 NO.否]',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='任课老师表';

drop table if exists `sys_staff_research_leader`;
create table `sys_staff_research_leader`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  staff_id int(11) not null comment '教师ID sys_staff.id',
  subject_id int(11) not null comment '科目ID sys_subject.id',
  is_current varchar(50) not null comment '是否当值 [YES.是 NO.否]',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='教研组长表';

drop table if exists `sys_staff_lesson_leader`;
create table `sys_staff_lesson_leader`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  academic_year_semester_id int(11) not null comment '学年学期 sys_academic_year_semester.id',
  grade_id int(11) not null comment '年级 sys_grade.id',
  staff_id int(11) not null comment '教师ID sys_staff.id',
  subject_id int(11) not null comment '科目ID sys_subject.id',
  is_current varchar(50) not null comment '是否当值 [YES.是 NO.否]',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='备课组长表';

drop table if exists `sys_staff_grade_leader`;
create table `sys_staff_grade_leader`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  academic_year_semester_id int(11) not null comment '学年学期 sys_academic_year_semester.id',
  grade_id int(11) not null comment '年级 sys_grade.id',
  staff_id int(11) not null comment '教师ID sys_staff.id',
  is_current varchar(50) not null comment '是否当值 [YES.是 NO.否]',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='年级组长表';

drop table if exists `sys_student`;
create table `sys_student`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  student_number varchar(50) not null comment '学号',
  id_number varchar(50) not null comment '身份证号',
  order_number varchar(50) not null comment '编号',
  card_number varchar(50) not null comment '一卡通号',
  name varchar(50) not null comment '姓名',
  student_type varchar(50) not null default 'DAY' comment '学生类型 [LIVE.住校生 DAY.走读生]',
  gender varchar(50) not null default 'M' comment '性别 [M.男 W.女]',
  nationality varchar(20) not null comment '民族',
  admission_way varchar(50) not null comment '入学方式',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='学生表';

drop table if exists `sys_student_parent`;
create table `sys_student_parent`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  student_id int(11) not null comment '学生ID sys_student.id',
  name varchar(50) not null comment '姓名',
  phone varchar(20) not null comment '手机号',
  relation varchar(50) not null default 'F' comment '关系 [F.父亲 M.母亲 FGF.爷爷 FGM.奶奶 MGF.外公 MGM.外婆 O.其他]',
  wx_parent_id varchar(255) null comment '微信家长id',
  type varchar(50) not null default 'NO_CREATE' comment '是否在微信创建家长联系人发送消息 [NOT_CREATE.未创建 CREATED.已创建 NO_CREATE.不创建]',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='学生家长表';

drop table if exists `sys_student_clazz`;
create table `sys_student_clazz`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  student_id int(11) not null comment '学生ID sys_student.id',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='学生班级表';

drop table if exists `sys_label`;
create table `sys_label`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  classify varchar(50) null comment '分类',
  name varchar(100) not null comment '标签',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='标签表';

drop table if exists `sys_course`;
create table `sys_course`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  academic_year_semester_id int(11) not null comment '学年学期ID sys_academic_year_semester.id',
  grade_id int(11) not null comment '年级ID sys_grade.id',
  week int(3) not null default 1 comment '星期',
  sort_order int(2) not null default 0 comment '节次',
  course_name varchar(20) not null comment '课程名称',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  clazz_name varchar(50) null comment '班级',
  teacher_id int(11) not null comment '老师ID sys_staff.id',
  teacher_name varchar(50) null comment '老师',
  editor_id int(11) not null default 1 comment '操作人ID',
  editor_name varchar(255) not null default 'admin' comment '操作人',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='课程表';

drop table if exists `sys_course_time`;
create table `sys_course_time`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  grade_id int(11) not null comment '年级ID sys_grade.id',
  sort_order int(2) not null default 0 comment '节次',
  start_time varchar(5) not null comment '开始时间',
  end_time varchar(5) not null comment '结束时间',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='课程时间表';

drop table if exists `sys_role`;
create table `sys_role`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  name varchar(255) not null comment '名称',
  remark varchar(255) null comment '备注',
  editor_id int(11) not null default 1 comment '操作人ID',
  editor_name varchar(255) not null default 'admin' comment '操作人',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='角色表';

drop table if exists `sys_user`;
create table `sys_user`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  username varchar(255) not null comment '用户名',
  login_number varchar(255) not null comment '登录编号',
  password varchar(255) null comment '密码',
  real_name varchar(255) null comment '姓名',
  avatar varchar(255) null comment '头像',
  staff_id int(11) null default 0 comment '教职工ID sys_staff.id',
  student_id int(11) null default 0 comment '学生ID sys_student.id',
  is_delete varchar(50) not null default 'NO' comment '是否删除 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='用户表';

drop table if exists `sys_user_role`;
create table `sys_user_role`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  user_id int(11) not null comment '用户ID',
  role_id int(11) not null comment '角色ID',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='用户角色关系表';

drop table if exists `sys_authority`;
create table `sys_authority`
(
  id int(11) auto_increment not null comment 'Id',
  parent_id int(11) not null default 0 comment '父级Id',
  type varchar(50) not null default 'MENU' comment '权限类型 [MENU.菜单 BUTTON.按钮]',
  name varchar(255) not null comment '名称',
  path varchar(255) null comment '路径',
  redirect varchar(255) null comment '重定向路径',
  keep_alive boolean not null default true comment '是否缓存',
  hidden boolean not null default false comment '是否隐藏',
  hide_children boolean not null default false comment '是否隐藏子菜单',
  component varchar(255) null comment '页面组件',
  title varchar(255) not null comment '标题',
  icon varchar(255) null comment '图标',
  target varchar(255) null comment '链接目标',
  hidden_header_content boolean not null default false comment '是否隐藏header',
  sort_order int(11) not null default 0 comment '序号',
  editor_id int(11) not null default 1 comment '操作人Id',
  editor_name varchar(255) not null default 'admin' comment '操作人',
  create_time datetime not null default CURRENT_TIMESTAMP comment '创建时间',
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='权限表';

drop table if exists `sys_role_authority`;
create table `sys_role_authority`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  role_id int(11) not null comment 'sys_role.id',
  authority_id int(11) not null comment 'sys_authority.id',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='角色权限对应表';

drop table if exists `sys_settings`;
create table `sys_settings`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  code varchar(50) not null comment '编码',
  remark varchar(255) not null comment '说明',
  params text null comment '参数json',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='系统配置表';

drop table if exists `sys_teaching_result_classify`;
create table `sys_teaching_result_classify`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  name varchar(255) not null comment '名称',
  parent_id int(11) not null default 0 comment '父级ID',
  sort_order int(11) not null default 0 comment '序号',
  editor_id int(11) not null default 1 comment '操作人ID',
  editor_name varchar(255) not null default 'admin' comment '操作人',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='教学成果分类表';

drop table if exists `sys_teaching_result_declaration_classify`;
create table `sys_teaching_result_declaration_classify`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  name varchar(255) not null comment '名称',
  parent_id int(11) not null default 0 comment '父级ID',
  sort_order int(11) not null default 0 comment '序号',
  editor_id int(11) not null default 1 comment '操作人ID',
  editor_name varchar(255) not null default 'admin' comment '操作人',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='教学成果参评申报类别表';

drop table if exists `tmg_teaching_result`;
create table `tmg_teaching_result`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teacher_id int(11) not null comment '教师ID sys_stuff.id',
  result_classify_id int(11) not null comment '成果分类ID sys_teaching_result_classify.id',
  result_date varchar(7) not null comment '成果时间(年-月)',
  academic_year_semester_id int(11) not null comment '学年学期ID sys_academic_year_semester.id',
  year varchar(20) not null default '' comment '学年',
  semester varchar(50) not null default 'Q1' comment '学期 [Q1.第一学期 Q2.第二学期]',
  level varchar(50) not null comment '级别 [HXQK.核心期刊 GJJ.国家级 SJ.省级 DSJ.市级 QJ.区级 XJ.校级 NJZ.年级组 ZYZ.教研组]',
  name varchar(255) not null comment '成果名称',
  reviewer_id int(11) null comment '审核人ID sys_stuff.id',
  state varchar(50) not null default 'PENDING_REVIEW' comment '状态 [PENDING_REVIEW.待审核 PASSED.已通过 REJECTED.已驳回]',
  reason varchar(255) null comment '原因',
  editor_id int(11) not null comment '操作人ID sys_user.id',
  editor_name varchar(255) not null comment '操作人 sys_user.real_name',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='教学成果表';

drop table if exists `tmg_teaching_result_attachment`;
create table `tmg_teaching_result_attachment`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  url varchar(2000) not null comment '附件url',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='教学成果附件表';

drop table if exists `tmg_teaching_result_history`;
create table `tmg_teaching_result_history`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  classify_id1 int(11) not null default 0 comment '申报类别ID1 sys_teaching_result_declaration_classify.id',
  classify_id2 int(11) not null default 0 comment '申报类别ID2 sys_teaching_result_declaration_classify.id',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='教学成果参评申报历史表';

drop table if exists `tmg_teaching_result_grry`;
create table `tmg_teaching_result_grry`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  rych varchar(255) not null comment '荣誉称号 和tmg_teaching_result.name一致',
  fzdw varchar(255) not null comment '发证单位 可以从sys_label.classify == FZDW选择，也可以录入',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='个人荣誉表';

drop table if exists `tmg_teaching_result_jtry`;
create table `tmg_teaching_result_jtry`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  rych varchar(255) not null comment '荣誉称号',
  hjmc varchar(255) not null comment '获奖名称 和tmg_teaching_result.name一致',
  fzdw varchar(255) not null comment '发证单位 可以从sys_label.classify == FZDW选择，也可以录入',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='集体荣誉表';

drop table if exists `tmg_teaching_result_bjry`;
create table `tmg_teaching_result_bjry`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  rych varchar(255) not null comment '荣誉称号 和tmg_teaching_result.name一致',
  hjmc varchar(255) not null comment '获奖名称',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='班级荣誉表';

drop table if exists `tmg_teaching_result_lwfb`;
create table `tmg_teaching_result_lwfb`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  lwbt varchar(255) not null comment '论文标题 和tmg_teaching_result.name一致',
  issnkh varchar(255) not null comment 'ISSN刊号',
  issnsh varchar(255) not null comment 'ISSN书号',
  cnkh varchar(255) not null comment 'CN刊号',
  qkmc varchar(255) not null comment '期刊名称',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='论文发表';

drop table if exists `tmg_teaching_result_lwpbhj`;
create table `tmg_teaching_result_lwpbhj`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  lwbt varchar(255) not null comment '论文标题 和tmg_teaching_result.name一致',
  hjqk varchar(255) not null comment '获奖情况',
  zzdw varchar(255) not null comment '组织单位',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='论文评比获奖表';

drop table if exists `tmg_teaching_result_lzcb`;
create table `tmg_teaching_result_lzcb`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  zzmc varchar(255) not null comment '著作名称 和tmg_teaching_result.name一致',
  isbnsh varchar(255) not null comment 'ISBN书号',
  cbs varchar(255) not null comment '出版社',
  wczsjbl varchar(255) not null comment '完成字数及比例',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='论著出版表';

drop table if exists `tmg_teaching_result_cbzc`;
create table `tmg_teaching_result_cbzc`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  zzmc varchar(255) not null comment '著作名称 和tmg_teaching_result.name一致',
  zzdw varchar(255) not null comment '组织单位',
  isbnsh varchar(255) not null comment 'ISBN书号',
  cbs varchar(255) not null comment '出版社',
  wczsjbl varchar(255) not null comment '完成字数及比例',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='参编教材表';

drop table if exists `tmg_teaching_result_ktyj`;
create table `tmg_teaching_result_ktyj`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  ktmc varchar(255) not null comment '课题名称 和tmg_teaching_result.name一致',
  ktbh varchar(255) not null comment '课题编号',
  cyfs varchar(50) not null default 'CY' comment '参与方式 [ZC.主持 CY.参与]',
  yjzt varchar(50) not null default 'YJZ' comment '研究状态 [YJZ.研究中 YJT.已结题]',
  zzdw varchar(255) not null comment '组织单位',
  ktsj varchar(7) not null comment '开题时间(年-月)',
  jtsj varchar(7) not null comment '结题时间(年-月)',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='课题研究表';

drop table if exists `tmg_teaching_result_sfkks`;
create table `tmg_teaching_result_sfkks`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  ktmc varchar(255) not null comment '课题名称 和tmg_teaching_result.name一致',
  zzdw varchar(255) not null comment '组织单位',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='示范课、研究课开设表';

drop table if exists `tmg_teaching_result_ykhj`;
create table `tmg_teaching_result_ykhj`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  zzdw varchar(255) not null comment '组织单位',
  ktmc varchar(255) not null comment '课题名称 和tmg_teaching_result.name一致',
  bsmc varchar(255) not null comment '比赛名称 可以从sys_label.classify == YKHJ_BSMC选择，也可以录入',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='优课获奖表';

drop table if exists `tmg_teaching_result_jzks`;
create table `tmg_teaching_result_jzks`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  jzmc varchar(255) not null comment '讲座名称 和tmg_teaching_result.name一致',
  zzdw varchar(255) not null comment '组织单位',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='讲座开设表';

drop table if exists `tmg_teaching_result_grjshj`;
create table `tmg_teaching_result_grjshj`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  zzdw varchar(255) not null comment '组织单位',
  hjqk varchar(255) not null comment '获奖情况',
  bsmc varchar(255) not null comment '比赛名称 和tmg_teaching_result.name一致 可以从sys_label.classify == GRJS_BSMC选择，也可以录入',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='个人竞赛获奖表';

drop table if exists `tmg_teaching_result_zdxshj`;
create table `tmg_teaching_result_zdxshj`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  hjqk varchar(255) not null comment '获奖情况',
  zzdw varchar(255) not null comment '组织单位',
  bsmc varchar(255) not null comment '比赛名称 和tmg_teaching_result.name一致',
  xsxm varchar(255) not null comment '学生姓名',
  xsbj varchar(255) not null comment '学生班级',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='指导学生获奖表';

drop table if exists `tmg_teaching_result_zdqnjs`;
create table `tmg_teaching_result_zdqnjs`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  hjqk varchar(255) not null comment '获奖情况 和tmg_teaching_result.name一致',
  kssj varchar(7) not null comment '开始时间(年-月)',
  jssj varchar(7) not null comment '结束时间(年-月)',
  jsmc varchar(255) not null comment '教师姓名',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='指导青年教师表';

drop table if exists `tmg_teaching_result_jxpx`;
create table `tmg_teaching_result_jxpx`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  pxmc varchar(255) not null comment '培训名称 和tmg_teaching_result.name一致',
  zzdw varchar(255) not null comment '组织单位',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='进修培训表';

drop table if exists `tmg_teaching_result_wkhj`;
create table `tmg_teaching_result_wkhj`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_result_id int(11) not null comment '教学成果ID tmg_teaching_result.id',
  wkmc varchar(255) not null comment '微课名称 和tmg_teaching_result.name一致',
  hdmc varchar(255) not null comment '活动名称',
  hjmc varchar(255) not null comment '获奖名次',
  zzdw varchar(255) not null comment '组织单位',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='微课获奖表';

drop table if exists `dat_student_remark`;
create table `dat_student_remark`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  student_id int(11) not null comment '学生ID sys_student.id',
  teacher_id int(11) not null comment '教师ID sys_staff.id',
  remark text not null comment '教师对学生的批注评价',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='学生评价表';

drop table if exists `dat_exam_publish`;
create table `dat_exam_publish`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  grade_id int(11) not null comment '年级ID sys_grade.id',
  publish_time datetime not null comment '发布日期',
  remark varchar(255) null comment '备注',
  editor varchar(20) not null comment '填报人',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='考试发布表';

drop table if exists `dat_exam_publish_relation`;
create table `dat_exam_publish_relation`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  exam_publish_id int(11) not null comment '考试发布表ID dat_exam_publish.id',
  exam_id int(11) not null comment '考试ID dat_exam.id',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='考试发布子表';

drop table if exists `dat_exam_print_log`;
create table `dat_exam_print_log`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  exam_publish_id int(11) not null comment '考试发布表ID dat_exam_publish.id',
  student_id int(11) not null comment '学生id',
  num int(2) null default 1 comment '打印次数',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='考试打印日志表';

drop table if exists `dat_exam`;
create table `dat_exam`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  schoolyard_id int(11) not null comment '校区ID sys_schoolyard.id',
  academic_year_semester_id int(11) not null comment '学年学期ID sys_academic_year_semester.id',
  grade_id int(11) not null comment '年级ID sys_grade.id',
  name varchar(255) not null comment '考试名称',
  exam_type_id int(11) not null comment '考试分类ID dat_exam_type.id',
  exam_subjects varchar(255) not null comment '考试科目 多选 用[,]分割 sys_subject.name',
  exam_start_date date not null comment '考试开始日期',
  exam_end_date date not null comment '考试结束日期',
  academy_ratio_update_time datetime not null default CURRENT_TIMESTAMP comment '学业等级更新日期，对应grade表同名字段',
  is_publish varchar(50) not null default 'NO' comment '是否发布 [YES.是 NO.否]',
  cal_natural_score varchar(50) not null default 'NO' comment '是否需要计算赋分 [YES.是 NO.否] [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='考试表';

drop table if exists `dat_exam_type`;
create table `dat_exam_type`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  name varchar(255) not null comment '分类名称',
  weight int(11) not null default 0 comment '权重',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='考试分类表';

drop table if exists `dat_exam_goal_template`;
create table `dat_exam_goal_template`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  name varchar(255) not null comment '模板名称',
  setting_type varchar(50) not null default 'SCORE' comment '设置方式 [SCORE.分数目标 COUNT.比例目标]',
  target_type varchar(50) not null default 'YSF' comment '目标类型 [YSF.原始分 FF.赋分]',
  subject_type varchar(50) not null default 'SCIENCE' comment '目标科目类型 [LIBERAL.文科目标 SCIENCE.理科目标 OTHER.不分科目标]',
  goal_value int(5) not null default 0 comment '总分目标值',
  subject_value int(5) not null default 0 comment '单科目标值',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='目标模板表';

drop table if exists `dat_exam_goal`;
create table `dat_exam_goal`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  exam_id int(11) not null comment 'dat_exam.id',
  name varchar(255) not null comment '目标名称',
  setting_type varchar(50) not null default 'SCORE' comment '设置方式 [SCORE.分数目标 COUNT.比例目标]',
  target_type varchar(50) not null default 'YSF' comment '目标类型 [YSF.原始分 FF.赋分]',
  subject_type varchar(50) not null default 'SCIENCE' comment '目标科目类型 [LIBERAL.文科目标 SCIENCE.理科目标 OTHER.不分科目标]',
  goal_value int(5) not null default 0 comment '总分目标值',
  subject_value int(5) not null default 0 comment '单科目标值',
  goal_reference text not null comment '手动输入的各班级目标参考，用json格式',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='考试目标表';

drop table if exists `dat_exam_goal_clazz`;
create table `dat_exam_goal_clazz`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  exam_goal_id int(11) not null comment 'dat_exam_goal.id',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  goal_value int(5) not null default 0 comment '总分目标值',
  subject_value int(5) not null default 0 comment '单科目标值',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='考试目标班级表';

drop table if exists `dat_exam_goal_sub`;
create table `dat_exam_goal_sub`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  exam_id int(11) not null comment 'dat_exam.id',
  subject_id int(11) not null comment '学科id,原始分分段90001，赋分分段100001',
  subject_type varchar(50) not null default 'SCIENCE' comment '目标科目类型 [LIBERAL.文科目标 SCIENCE.理科目标 OTHER.不分科目标]',
  type varchar(50) not null default 'ge' comment '分段逻辑 [ge.大于等于 le.小于等于]',
  goal_value int(5) not null default 0 comment '目标值',
  style_json varchar(200) not null default 0 comment '分数段样式，用json格式',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='考试分段表';

drop table if exists `dat_exam_goal_natural_template`;
create table `dat_exam_goal_natural_template`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  grade_id int(11) not null comment '年级id',
  subject_id int(11) not null comment '学科id',
  name varchar(255) not null comment '模板名称',
  academy_ratio_a int(11) not null default 0 comment '学业等级A等比例',
  academy_ratio_b int(11) not null default 0 comment '学业等级B等比例',
  academy_ratio_c int(11) not null default 0 comment '学业等级C等比例',
  academy_ratio_d int(11) not null default 0 comment '学业等级D等比例',
  academy_ratio_e int(11) not null default 0 comment '学业等级E等比例',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='考试赋分模板表';

drop table if exists `dat_exam_goal_natural`;
create table `dat_exam_goal_natural`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  exam_id int(11) not null comment 'dat_exam.id',
  subject_id int(11) not null comment '学科id',
  name varchar(255) not null comment '模板名称',
  academy_ratio_a int(11) not null default 0 comment '学业等级A等比例',
  academy_ratio_b int(11) not null default 0 comment '学业等级B等比例',
  academy_ratio_c int(11) not null default 0 comment '学业等级C等比例',
  academy_ratio_d int(11) not null default 0 comment '学业等级D等比例',
  academy_ratio_e int(11) not null default 0 comment '学业等级E等比例',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='考试赋分表';

drop table if exists `dat_exam_result`;
create table `dat_exam_result`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  exam_id int(11) not null comment 'dat_exam.id',
  student_id int(11) not null comment 'sys_student.id',
  clazz_id int(11) not null comment 'sys_clazz.id',
  total_score decimal(9, 2) not null comment '原始分总分',
  total_weighted_score decimal(9, 2) not null comment '原始分赋分',
  chinese_score decimal(9, 2) not null comment '语文成绩',
  math_score decimal(9, 2) not null comment '数学成绩',
  english_score decimal(9, 2) not null comment '英语成绩',
  physics_score decimal(9, 2) not null comment '物理成绩',
  chemistry_score decimal(9, 2) not null comment '化学原始分',
  chemistry_weighted_score decimal(9, 2) not null comment '化学赋分',
  biology_score decimal(9, 2) not null comment '生物原始分',
  biology_weighted_score decimal(9, 2) not null comment '生物赋分',
  history_score decimal(9, 2) not null comment '历史成绩',
  politics_score decimal(9, 2) not null comment '政治原始分',
  politics_weighted_score decimal(9, 2) not null comment '政治赋分',
  geography_score decimal(9, 2) not null comment '地理原始分',
  geography_weighted_score decimal(9, 2) not null comment '地理赋分',
  clazz_rank int(3) not null default 0 comment '班级排名',
  grade_rank int(3) not null default 0 comment '年级排名',
  other varchar(1024) null comment '其他成绩信息',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='考试结果表';

drop table if exists `dat_practice`;
create table `dat_practice`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  schoolyard_id int(11) not null comment '校区ID sys_schoolyard.id',
  academic_year_semester_id int(11) not null comment '学年学期ID sys_academic_year_semester.id',
  grade_id int(11) not null comment '年级ID sys_grade.id',
  staff_id int(11) not null comment '教师ID sys_staff.id',
  name varchar(255) not null comment '名称',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='班级练习表';

drop table if exists `dat_practice_topic`;
create table `dat_practice_topic`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  practice_id int(11) not null comment '练习ID dat_practice.id',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  topic_name varchar(50) null comment '题目',
  topic_number int(3) not null comment '题号',
  topic_type varchar(20) not null comment '题型',
  score decimal(7, 2) not null comment '分值',
  difficulty decimal(7, 2) not null comment '难度',
  distinguish decimal(7, 2) not null comment '区分度',
  grade_average decimal(7, 2) not null comment '年级均分',
  grade_rate decimal(7, 2) not null comment '年级得分率',
  average decimal(7, 2) not null comment '均分',
  rate decimal(7, 2) not null comment '得分率',
  rank int(3) not null comment '排名',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='小题得分情况表';

drop table if exists `day_teacher_duty`;
create table `day_teacher_duty`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teacher_id int(11) not null comment '教师ID sys_staff.id',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  duty_type varchar(50) not null default 'STAGE_ONE' comment '教师值班类型 [STAGE_ONE.第一阶段 STAGE_TWO.第二阶段 GRADE_TOTAL_DUTY.年级总值班 TOTAL_DUTY.总值班]',
  duty_mode varchar(50) not null default 'NORMAL' comment '教师值班模式 [NORMAL.正常模式 HOLIDAY.假期模式]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='教师值班表';

drop table if exists `day_teacher_duty_clazz`;
create table `day_teacher_duty_clazz`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teacher_duty_id int(11) not null comment '教师值班ID day_teacher_duty.id',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  is_comfirm varchar(50) not null default 'NO' comment '是否确认 [YES.是 NO.否]',
  is_leader_comfirm varchar(50) not null default 'NO' comment '值班领导是否确认 [YES.是 NO.否]',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='教师值班班级表';

drop table if exists `day_teacher_duty_substitute`;
create table `day_teacher_duty_substitute`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teacher_duty_id int(11) not null comment '教师值班ID day_teacher_duty.id',
  teacher_old_id int(11) not null default 0 comment '原值班教师ID sys_staff.id',
  teacher_id int(11) not null comment '代班教师ID sys_staff.id',
  is_agree varchar(50) null comment '是否同意 初始值为 null [YES.是 NO.否]',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='教师值班代班表';

drop table if exists `day_night_study`;
create table `day_night_study`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teacher_duty_clazz_id int(11) not null comment '教师值班班级ID day_teacher_duty_clazz.id',
  should_student_count int(5) not null comment '应到学生数',
  actual_student_count int(5) not null comment '实到学生数',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='晚自习表';

drop table if exists `day_night_study_detail`;
create table `day_night_study_detail`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  night_study_id int(11) not null comment '晚自习ID day_night_study.id',
  classify varchar(50) not null default 'OTHER' comment '分类 [ABSENT.缺席 BE_LATE.迟到 IN_OUT_CLASSROOM.中途出入教室 OTHER.其他]',
  student_id int(11) not null comment '学生ID sys_student.id',
  reason varchar(255) not null comment '原因',
  is_notice varchar(50) not null default 'YES' comment '是否通知班主任 [YES.是 NO.否]',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='晚自习明细表';

drop table if exists `day_night_study_detail_image`;
create table `day_night_study_detail_image`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  night_study_detail_id int(11) not null comment '晚自习明细ID day_night_study_detail.id',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='晚自习明细照片表';

drop table if exists `day_leader_duty`;
create table `day_leader_duty`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  duty_type varchar(50) not null default 'ROUTINE' comment '领导值班类型 [ROUTINE.常规管理 NIGHT_STUDY.晚自习]',
  leader_id int(11) null comment '值班领导ID sys_staff.id',
  phone varchar(20) null comment '电话',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='领导值班表';

drop table if exists `day_leader_duty_substitute`;
create table `day_leader_duty_substitute`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  duty_type varchar(50) not null default 'ROUTINE' comment '领导值班类型 [ROUTINE.常规管理 NIGHT_STUDY.晚自习]',
  leader_duty_id int(11) not null comment '领导值班ID day_leader_duty.id',
  leader_old_id int(11) not null default 0 comment '原值班领导ID sys_staff.id',
  leader_id int(11) not null comment '代班领导ID sys_staff.id',
  is_agree varchar(50) null comment '是否同意 初始值为 null [YES.是 NO.否]',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='领导值班代班表';

drop table if exists `day_south_gate`;
create table `day_south_gate`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  leader_duty_id int(11) not null comment '领导值班ID day_leader_duty.id',
  doctor_in_place varchar(50) not null default 'YES' comment '校医是否就位 [YES.是 NO.否]',
  doctor_description varchar(255) null comment '情况说明',
  guard_in_place varchar(50) not null default 'YES' comment '保安是否就位 [YES.是 NO.否]',
  guard_description varchar(255) null comment '情况说明',
  thermometer_in_place varchar(50) not null default 'YES' comment '远红外测温仪是否正常 [YES.是 NO.否]',
  thermometer_description varchar(255) null comment '情况说明',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  check_time datetime not null comment '检查时间',
  has_contingency varchar(50) not null default 'NO' comment '是否有偶发事件 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='南大门准备情况表';

drop table if exists `day_south_gate_images`;
create table `day_south_gate_images`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  south_gate_id int(11) not null comment '南大门准备情况ID day_south_gate.id',
  image_classify varchar(50) not null default 'DOCTOR' comment '图片分类 [DOCTOR.校医情况 GUARD.保安情况 THERMOMETER.远红外测温仪情况]',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='南大门准备情况照片表';

drop table if exists `day_breakfast`;
create table `day_breakfast`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  leader_duty_id int(11) not null comment '领导值班ID day_leader_duty.id',
  dining_order varchar(50) not null default 'NORMAL' comment '就餐秩序 [NORMAL.正常 IMPROVE.有待改进]',
  is_accompany_meal varchar(50) not null default 'YES' comment '是否陪餐 [YES.是 NO.否]',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  check_time datetime not null comment '检查时间',
  has_contingency varchar(50) not null default 'NO' comment '是否有偶发事件 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='早餐情况表';

drop table if exists `day_breakfast_images`;
create table `day_breakfast_images`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  breakfast_id int(11) not null comment '早餐情况ID day_breakfast.id',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='早餐情况照片表';

drop table if exists `day_morning_reading`;
create table `day_morning_reading`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  leader_duty_id int(11) not null comment '领导值班ID day_leader_duty.id',
  reading_order1 varchar(50) not null default 'NORMAL' comment '高一年级早读秩序 [NORMAL.正常 IMPROVE.有待改进]',
  evaluation_level1 varchar(50) not null default 'A' comment '高一年级评价等级 [A.A B.B C.C D.D]',
  reading_order2 varchar(50) not null default 'NORMAL' comment '高二年级早读秩序 [NORMAL.正常 IMPROVE.有待改进]',
  evaluation_level2 varchar(50) not null default 'A' comment '高二年级评价等级 [A.A B.B C.C D.D]',
  reading_order3 varchar(50) not null default 'NORMAL' comment '高三年级早读秩序 [NORMAL.正常 IMPROVE.有待改进]',
  evaluation_level3 varchar(50) not null default 'A' comment '高三年级评价等级 [A.A B.B C.C D.D]',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  check_time datetime not null comment '检查时间',
  has_contingency varchar(50) not null default 'NO' comment '是否有偶发事件 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='早读情况表';

drop table if exists `day_morning_reading_images`;
create table `day_morning_reading_images`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  morning_reading_id int(11) not null comment '早餐情况ID day_morning_reading.id',
  image_classify varchar(50) not null default 'ONE' comment '图片分类 [ONE.高一年级 TWO.高二年级 THREE.高三年级]',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='早读情况照片表';

drop table if exists `day_break_activity`;
create table `day_break_activity`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  leader_duty_id int(11) not null comment '领导值班ID day_leader_duty.id',
  activity_grade varchar(255) not null comment '活动年级 多选 用[,]分割',
  activity_type varchar(50) not null comment '活动类型 [MEETING.晨会 EXERCISE.课间操 RUNNING.跑操]',
  activity_order varchar(50) not null default 'NORMAL' comment '活动秩序 [NORMAL.正常 IMPROVE.有待改进]',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  check_time datetime not null comment '检查时间',
  has_contingency varchar(50) not null default 'NO' comment '是否有偶发事件 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='大课间活动情况表';

drop table if exists `day_break_activity_images`;
create table `day_break_activity_images`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  break_activity_id int(11) not null comment '大课间活动情况ID day_break_activity.id',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='大课间活动情况照片表';

drop table if exists `day_lunch`;
create table `day_lunch`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  leader_duty_id int(11) not null comment '领导值班ID day_leader_duty.id',
  dining_order varchar(50) not null default 'NORMAL' comment '就餐秩序 [NORMAL.正常 IMPROVE.有待改进]',
  is_accompany_meal varchar(50) not null default 'YES' comment '是否陪餐 [YES.是 NO.否]',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  check_time datetime not null comment '检查时间',
  has_contingency varchar(50) not null default 'NO' comment '是否有偶发事件 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='午餐情况表';

drop table if exists `day_lunch_images`;
create table `day_lunch_images`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  lunch_id int(11) not null comment '午餐情况ID day_lunch.id',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='午餐情况照片表';

drop table if exists `day_teaching_area`;
create table `day_teaching_area`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  leader_duty_id int(11) not null comment '领导值班ID day_leader_duty.id',
  teaching_area_order1 varchar(50) not null default 'NORMAL' comment '高一年级教学区秩序 [NORMAL.正常 IMPROVE.有待改进]',
  teaching_area_order2 varchar(50) not null default 'NORMAL' comment '高二年级教学区秩序 [NORMAL.正常 IMPROVE.有待改进]',
  teaching_area_order3 varchar(50) not null default 'NORMAL' comment '高三年级教学区秩序 [NORMAL.正常 IMPROVE.有待改进]',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  check_time datetime not null comment '检查时间',
  has_contingency varchar(50) not null default 'NO' comment '是否有偶发事件 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='教学区秩序表';

drop table if exists `day_teaching_area_images`;
create table `day_teaching_area_images`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  teaching_area_id int(11) not null comment '教学区秩序ID day_teaching_area.id',
  image_classify varchar(50) not null default 'ONE' comment '图片分类 [ONE.高一年级 TWO.高二年级 THREE.高三年级]',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='教学区秩序照片表';

drop table if exists `day_noon_sport_area`;
create table `day_noon_sport_area`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  leader_duty_id int(11) not null comment '领导值班ID day_leader_duty.id',
  sport_area_order varchar(50) not null default 'NORMAL' comment '运动区秩序 [NORMAL.正常 IMPROVE.有待改进]',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  check_time datetime not null comment '检查时间',
  has_contingency varchar(50) not null default 'NO' comment '是否有偶发事件 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='午班运动区秩序表';

drop table if exists `day_noon_sport_area_images`;
create table `day_noon_sport_area_images`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  sport_area_id int(11) not null comment '午班运动区秩序ID day_noon_sport_area.id',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='午班运动区秩序照片表';

drop table if exists `day_dinner`;
create table `day_dinner`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  leader_duty_id int(11) not null comment '领导值班ID day_leader_duty.id',
  dinner_order varchar(50) not null default 'NORMAL' comment '就餐秩序 [NORMAL.正常 IMPROVE.有待改进]',
  is_accompany_meal varchar(50) not null default 'YES' comment '是否陪餐 [YES.是 NO.否]',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  check_time datetime not null comment '检查时间',
  has_contingency varchar(50) not null default 'NO' comment '是否有偶发事件 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='晚餐情况表';

drop table if exists `day_dinner_images`;
create table `day_dinner_images`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  dinner_id int(11) not null comment '晚餐情况ID day_dinner.id',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='晚餐情况照片表';

drop table if exists `day_go_out`;
create table `day_go_out`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  leader_duty_id int(11) not null comment '领导值班ID day_leader_duty.id',
  go_out_order varchar(50) not null default 'NORMAL' comment '走读生提前出门情况 [NORMAL.正常 IMPROVE.有待改进]',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  check_time datetime not null comment '检查时间',
  has_contingency varchar(50) not null default 'NO' comment '是否有偶发事件 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='走读生提前出门表';

drop table if exists `day_go_out_images`;
create table `day_go_out_images`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  go_out_id int(11) not null comment '走读生提前出门ID day_go_out.id',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='走读生提前出门照片表';

drop table if exists `day_night_sport_area`;
create table `day_night_sport_area`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  leader_duty_id int(11) not null comment '领导值班ID day_leader_duty.id',
  sport_area_order varchar(50) not null default 'NORMAL' comment '运动区秩序 [NORMAL.正常 IMPROVE.有待改进]',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  check_time datetime not null comment '检查时间',
  has_contingency varchar(50) not null default 'NO' comment '是否有偶发事件 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='晚班运动区秩序表';

drop table if exists `day_night_sport_area_images`;
create table `day_night_sport_area_images`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  sport_area_id int(11) not null comment '晚班运动区秩序ID day_night_sport_area.id',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='晚班运动区秩序照片表';

drop table if exists `day_night_study_duty`;
create table `day_night_study_duty`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  leader_duty_id int(11) not null comment '领导值班ID day_leader_duty.id',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  has_contingency varchar(50) not null default 'NO' comment '是否有偶发事件 [YES.是 NO.否]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='晚自习行政值班表';

drop table if exists `day_night_study_duty_grade_deduction`;
create table `day_night_study_duty_grade_deduction`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  night_study_duty_id int(11) not null comment '晚自习行政值班ID day_night_study_duty.id',
  grade_id int(11) not null comment '年级ID sys_grade.id',
  Description varchar(255) not null comment '情况说明',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='晚自习行政值班年级扣分表(不用)';

drop table if exists `day_night_study_duty_grade_images`;
create table `day_night_study_duty_grade_images`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  night_study_duty_id int(11) not null comment '晚自习行政值班ID day_night_study_duty.id',
  grade_id int(11) not null comment '年级ID sys_grade.id',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='晚自习行政值班年级图片表';

drop table if exists `day_night_study_duty_clazz`;
create table `day_night_study_duty_clazz`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  night_study_duty_id int(11) not null comment '晚自习行政值班ID day_night_study_duty.id',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  teacher varchar(20) not null comment '值班老师',
  should_student_count int(5) not null default 0 comment '应到学生数',
  actual_student_count int(5) not null default 0 comment '实到学生数',
  score int(5) not null default 95 comment '得分',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='晚自习行政值班班级情况表';

drop table if exists `day_night_study_duty_clazz_deduction`;
create table `day_night_study_duty_clazz_deduction`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  night_study_duty_clazz_id int(11) not null comment '晚自习行政值班班级情况ID day_night_study_duty_clazz.id',
  Description varchar(255) not null comment '情况说明',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='晚自习行政值班班级扣分表';

drop table if exists `day_incident`;
create table `day_incident`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  classify varchar(50) not null comment '一日常规分类 [DAY_NIGHT_STUDY.晚自习 DAY_SOUTH_GATE.南大门准备情况 DAY_BREAKFAST.早餐就餐情况 DAY_MORNING_READING_1.早读情况-高一年级 DAY_MORNING_READING_2.早读情况-高二年级 DAY_MORNING_READING_3.早读情况-高三年级 DAY_BREAK_ACTIVITY.大课间活动情况 DAY_LUNCH.午餐就餐情况 DAY_TEACHING_AREA_1.教学区秩序-高一年级 DAY_TEACHING_AREA_2.教学区秩序-高二年级 DAY_TEACHING_AREA_3.教学区秩序-高三年级 DAY_NOON_SPORT_AREA.午班运动区秩序 DAY_DINNER.晚餐就餐情况 DAY_GO_OUT.走读生提前出门 DAY_NIGHT_SPORT_AREA.晚班运动区秩序 DAY_NIGHT_STUDY_DUTY.晚自习行政值班表 DAY_OTHER_1.白班其他 DAY_OTHER_2.晚班其他]',
  item_name varchar(50) not null comment '项目名称',
  daily_routine_id int(11) not null comment '一日常规ID (classify为数据表名)',
  content varchar(255) not null comment '内容 可以从sys_label.classify == YRCG_OFSJ选择，也可以录入',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='偶发事件表';

drop table if exists `day_incident_images`;
create table `day_incident_images`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  incident_id int(11) not null comment '偶发事件ID day_comment.id',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='偶发事件照片表';

drop table if exists `day_comment`;
create table `day_comment`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  classify varchar(50) not null comment '一日常规分类 [DAY_NIGHT_STUDY.晚自习 DAY_SOUTH_GATE.南大门准备情况 DAY_BREAKFAST.早餐就餐情况 DAY_MORNING_READING_1.早读情况-高一年级 DAY_MORNING_READING_2.早读情况-高二年级 DAY_MORNING_READING_3.早读情况-高三年级 DAY_BREAK_ACTIVITY.大课间活动情况 DAY_LUNCH.午餐就餐情况 DAY_TEACHING_AREA_1.教学区秩序-高一年级 DAY_TEACHING_AREA_2.教学区秩序-高二年级 DAY_TEACHING_AREA_3.教学区秩序-高三年级 DAY_NOON_SPORT_AREA.午班运动区秩序 DAY_DINNER.晚餐就餐情况 DAY_GO_OUT.走读生提前出门 DAY_NIGHT_SPORT_AREA.晚班运动区秩序 DAY_NIGHT_STUDY_DUTY.晚自习行政值班表 DAY_OTHER_1.白班其他 DAY_OTHER_2.晚班其他]',
  item_name varchar(50) not null comment '项目名称',
  daily_routine_id int(11) not null comment '一日常规ID (classify为数据表名)',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  editor varchar(20) not null comment '填报人',
  content varchar(255) not null comment '意见与建议 可以从sys_label.classify == YRCG_YJJY选择，也可以录入',
  state varchar(50) not null default 'TO_BE_PUSHED' comment '状态 [TO_BE_PUSHED.待推送 PUSHED.已推送 TO_BE_INSTRUCTION.待批示 PENDING.处理中 PROCESSED.已处理]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='意见与建议表';

drop table if exists `day_comment_images`;
create table `day_comment_images`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  comment_id int(11) not null comment '意见与建议ID day_comment.id',
  url varchar(1024) not null comment '图片链接',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='意见与建议照片表';

drop table if exists `day_comment_process`;
create table `day_comment_process`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  comment_id int(11) not null comment '意见与建议ID day_comment.id',
  need_instruction varchar(50) not null default 'NO' comment '是否需要校长室批示 [YES.是 NO.否]',
  instructions varchar(255) null comment '校长室批示',
  headmaster varchar(20) not null comment '批示人',
  source varchar(50) not null comment '来源 [GRADE.年级组 OFFICE.教务处]',
  state varchar(50) not null comment '状态 [TO_BE_PUSHED.待推送 PUSHED.已推送 TO_BE_INSTRUCTION.待批示 PENDING.处理中 PROCESSED.已处理]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='意见与建议推送表';

drop table if exists `day_comment_task`;
create table `day_comment_task`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  comment_process_id int(11) not null comment '意见与建议推送ID day_comment_process.id',
  sort_order int(3) not null default 0 comment '序号',
  function_department_id int(11) not null comment '职能部门ID sys_function_department.id',
  principal varchar(20) not null comment '职能部门负责人',
  content varchar(255) null comment '处理结果',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='意见与建议处理表';

drop table if exists `std_clazz_teaching_log`;
create table `std_clazz_teaching_log`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  academic_year_semester_id int(11) not null comment '学年学期ID sys_academic_year_semester.id',
  week int(2) not null comment '周数',
  register_date datetime not null comment '日期',
  clazz_id int(11) not null comment '班级ID sys_student.id',
  remark varchar(500) null comment '特殊情况',
  should_num int(4) not null default 0 comment '应到人数',
  actual_num int(4) not null default 0 comment '实到人数',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='班级教学日志表';

drop table if exists `std_clazz_teaching_log_subjects`;
create table `std_clazz_teaching_log_subjects`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  clazz_teaching_log_id int(11) not null comment '班级教学日志ID std_clazz_teaching_log.id',
  sort_order int(2) not null default 0 comment '节次',
  subject_id int(11) null comment '科目ID sys_subject.id',
  teacher_id int(11) null comment '任课老师ID sys_staff.id',
  rules varchar(50) null comment '课堂纪律 [R1.好 R2.中 R3.差]',
  is_on_time varchar(50) null comment '是否准时下课 [YES.是 NO.否]',
  state varchar(50) null comment '状态 [S1.已填报 S2.已更新 S3.已审核]',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='班级教学日志科目表';

drop table if exists `std_clazz_teaching_log_operation`;
create table `std_clazz_teaching_log_operation`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  clazz_teaching_log_id int(11) not null comment '班级教学日志ID std_clazz_teaching_log.id',
  subject_id int(11) null comment '科目ID sys_subject.id',
  duration varchar(50) null comment '时长 [NONE.无 D2.20分钟左右 D3.30分钟左右 D4.40分钟左右 D5.50分钟左右 D6.60分钟左右 D7.70分钟左右 D8.80分钟左右 D9.90分钟以上]',
  operation_date datetime not null comment '作业日期',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='班级教学日志作业量反馈表';

drop table if exists `std_daily_attendance`;
create table `std_daily_attendance`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  academic_year_semester_id int(11) not null comment '学年学期ID sys_academic_year_semester.id',
  week int(2) not null comment '周数',
  register_date datetime not null comment '日期',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  student_id int(11) not null comment '学生ID sys_student.id',
  classify varchar(50) not null comment '分类 sys_label.classify=RCKQ',
  reason varchar(255) null comment '原因',
  academic_hour int(3) not null default 0 comment '所缺课时数',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='日常考勤表';

drop table if exists `std_night_study_attendance`;
create table `std_night_study_attendance`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  academic_year_semester_id int(11) not null comment '学年学期ID sys_academic_year_semester.id',
  week int(2) not null comment '周数',
  register_date datetime not null comment '日期',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  student_id int(11) not null comment '学生ID sys_student.id',
  stage varchar(50) not null comment '自习阶段 [STAGE_ONE.第一阶段 STAGE_TWO.第二阶段]',
  should_num int(4) not null default 0 comment '应到人数',
  actual_num int(4) not null default 0 comment '实到人数',
  classify varchar(50) not null comment '分类 sys_label.classify=WZXKQ',
  reason varchar(255) null comment '原因',
  summarize varchar(255) null comment '值日班长总结',
  sign varchar(30) not null comment '值日班长签名',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='晚自习考勤表';

drop table if exists `std_ill`;
create table `std_ill`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  academic_year_semester_id int(11) not null comment '学年学期ID sys_academic_year_semester.id',
  week int(2) not null comment '周数',
  register_date datetime not null comment '日期',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  student_id int(11) not null comment '学生ID sys_student.id',
  days int(3) not null comment '缺课天数',
  symptom varchar(125) not null comment '因病缺课主要症状',
  ill_name varchar(255) not null comment '因病缺课疾病名称',
  treatment varchar(50) not null comment '就医情况',
  reason varchar(255) null comment '非因病缺课原因',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='因病缺课表';

drop table if exists `mes_message_template`;
create table `mes_message_template`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  name varchar(100) null comment '消息模板名称',
  title varchar(50) not null comment '标题',
  content text not null comment '内容',
  remark varchar(255) null comment '备注',
  created_time datetime not null default CURRENT_TIMESTAMP,
  updated_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='消息模板表';

drop table if exists `mes_message_task`;
create table `mes_message_task`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  message_template_id int(11) null comment '任务模板id',
  name varchar(100) null comment '名称',
  title varchar(50) null comment '标题',
  content text not null comment '内容',
  cron varchar(255) not null comment '发送策略',
  cron_json varchar(500) not null comment '发送策略json',
  send_method varchar(50) not null default 'WX' comment '发送方式 [WX.微信 SHORT_MESSAGE.短信]',
  start_time datetime not null,
  end_time datetime not null,
  editor_id int(11) not null comment '修改人,sys_user_id',
  editor_name varchar(255) not null comment '修改人名称',
  send_type varchar(50) not null default 'NO' comment '是否发送 [YES.是 NO.否]',
  need_write varchar(50) not null default 'NO' comment '是否需要填写 [YES.是 NO.否]',
  repeat_send int(11) not null default 24 comment '重发时间间隔',
  created_time datetime not null default CURRENT_TIMESTAMP,
  updated_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='消息任务表';

drop table if exists `mes_message_task_receiver`;
create table `mes_message_task_receiver`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  message_task_id int(11) not null comment '任务id',
  receiver_id int(11) not null comment '接收人ID sys_user.id',
  receiver_name varchar(255) not null comment '接收人 sys_user.real_name',
  receiver_type varchar(50) not null default 'STUDENT' comment '接收人类型 [TEACHER.教师 STUDENT.家长]',
  created_time datetime not null default CURRENT_TIMESTAMP,
  updated_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='消息发送人员表';

drop table if exists `mes_message`;
create table `mes_message`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  message_task_id int(11) not null comment '任务id',
  name varchar(100) not null comment '名称',
  title varchar(50) not null comment '标题',
  content text not null comment '内容',
  sender_id int(11) not null comment '发送人ID sys_user.id',
  sender_name varchar(255) not null comment '发送人 sys_user.real_name',
  receiver_id int(11) not null comment '接收人ID sys_user.id',
  receiver_name varchar(255) not null comment '接收人 sys_user.real_name',
  receiver_type varchar(50) not null default 'STUDENT' comment '接收人类型 [TEACHER.教师 STUDENT.家长]',
  send_time datetime not null comment '发送时间',
  repeat_send_time datetime null comment '重发时间',
  is_send varchar(50) not null default 'NO' comment '是否已发送 [YES.是 NO.否]',
  is_read varchar(50) not null default 'NO' comment '是否已读 [YES.是 NO.否]',
  is_write varchar(50) not null default 'NO' comment '是否已填 [YES.是 NO.否]',
  need_write varchar(50) not null default 'NO' comment '是否需要填写 [YES.是 NO.否]',
  send_num int(2) not null default 0 comment '发送次数',
  created_time datetime not null default CURRENT_TIMESTAMP,
  updated_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='消息表';

drop table if exists `mes_message_reply`;
create table `mes_message_reply`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  receiver_id int(11) null comment '接收人ID sys_staff.id',
  receiver_name varchar(255) null comment '接收人 sys_staff.real_name',
  receiver_wx_name varchar(255) not null comment '接收人微信名称',
  reply_content varchar(500) not null comment '回复内容',
  reply_type varchar(255) not null comment '回复类型',
  reply_time datetime not null default CURRENT_TIMESTAMP comment '回复时间',
  created_time datetime not null default CURRENT_TIMESTAMP,
  updated_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='消息回复表';

drop table if exists `dat_exam_score_report`;
create table `dat_exam_score_report`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  clazz_id int(11) not null comment '班级ID sys_clazz.id',
  student_id int(11) not null comment '学生ID sys_student.id',
  subject_id int(11) not null comment '学科ID sys_subject.id',
  usual_score int(11) not null comment '平时成绩',
  mid_score int(11) not null comment '期中成绩',
  end_score int(11) not null comment '期末成绩',
  total_score int(11) not null comment '总评成绩',
  editor_id int(11) not null comment '操作人ID sys_user.id',
  editor_name varchar(255) not null comment '操作人 sys_user.real_name',
  create_time datetime not null default CURRENT_TIMESTAMP,
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='成绩单表';

create index ix_clazz_id on `dat_exam_score_report` (clazz_id);
create index ix_student_id on `dat_exam_score_report` (student_id);
create index ix_subject_id on `dat_exam_score_report` (subject_id);
create index ix_editor_id on `dat_exam_score_report` (editor_id);

drop table if exists `sys_log`;
create table `sys_log`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  code varchar(255) not null comment '名称',
  value varchar(255) not null comment '值',
  remark varchar(50) null comment '备注',
  editor_id int(11) not null default 1 comment '操作人ID',
  editor_name varchar(255) not null default 'admin' comment '操作人',
  create_time datetime not null default CURRENT_TIMESTAMP comment '创建时间',
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='日志表';

create index ix_editor_id on `sys_log` (editor_id);

drop table if exists `sys_visit_log`;
create table `sys_visit_log`
(
  id int(11) auto_increment not null comment 'ID系统自动生成',
  project_name varchar(255) not null comment '项目名称',
  num int(11) not null default 1 comment '次数',
  remark varchar(50) null comment '备注',
  editor_id int(11) not null default 1 comment '操作人ID',
  editor_name varchar(255) not null default 'admin' comment '操作人',
  visit_time datetime not null comment '最后一次访问时间时间',
  create_time datetime not null default CURRENT_TIMESTAMP comment '创建时间',
  update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci comment='访问日志表表';

create index ix_editor_id on `sys_visit_log` (editor_id);

create index ix_visit_time on `sys_visit_log` (visit_time);

create unique index ix_name on `sys_schoolyard` (name);


create unique index ix_name on `sys_dormitory` (name);


create index ix_student_id on `sys_student_dormitory` (student_id);
create index ix_dormitory_id on `sys_student_dormitory` (dormitory_id);


create unique index ix_year_semester on `sys_academic_year_semester` (year,semester);


create unique index ix_schoolyard_id_name on `sys_grade` (schoolyard_id,name);


create unique index ix_academic_year_semester_id_grade_id_name on `sys_clazz` (academic_year_semester_id,grade_id,name);


create unique index ix_clazz_id_name on `sys_clazz_teacher` (clazz_id,name);


create unique index ix_name on `sys_department` (name);


create unique index ix_employee_number on `sys_staff` (employee_number);
create index ix_card_number on `sys_staff` (card_number);
create index ix_id_number on `sys_staff` (id_number);
create unique index ix_phone on `sys_staff` (phone);


create index ix_parent_id on `sys_staff_category` (parent_id);


create index ix_staff_id on `sys_staff_category_relation` (staff_id);


create index ix_staff_id on `sys_staff_message` (staff_id);


create index ix_receiver_type_receiver_id on `sys_staff_message_receiver_relation` (receiver_type,receiver_id);


create index ix_subject_id on `sys_staff_subject` (subject_id);
create index ix_staff_id on `sys_staff_subject` (staff_id);


create index ix_clazz_id on `sys_staff_clazz_adviser` (clazz_id);
create index ix_staff_id on `sys_staff_clazz_adviser` (staff_id);


create index ix_clazz_id on `sys_staff_lesson_teacher` (clazz_id);
create index ix_staff_id on `sys_staff_lesson_teacher` (staff_id);
create index ix_subject_id on `sys_staff_lesson_teacher` (subject_id);


create index ix_staff_id on `sys_staff_research_leader` (staff_id);
create index ix_subject_id on `sys_staff_research_leader` (subject_id);


create index ix_academic_year_semester_id on `sys_staff_lesson_leader` (academic_year_semester_id);
create index ix_grade_id on `sys_staff_lesson_leader` (grade_id);
create index ix_staff_id on `sys_staff_lesson_leader` (staff_id);
create index ix_subject_id on `sys_staff_lesson_leader` (subject_id);


create index ix_academic_year_semester_id on `sys_staff_grade_leader` (academic_year_semester_id);
create index ix_grade_id on `sys_staff_grade_leader` (grade_id);
create index ix_staff_id on `sys_staff_grade_leader` (staff_id);


create index ix_student_number on `sys_student` (student_number);
create index ix_card_number on `sys_student` (card_number);
create index ix_id_number on `sys_student` (id_number);
create index ix_order_number on `sys_student` (order_number);


create index ix_student_id on `sys_student_parent` (student_id);


create index ix_clazz_id on `sys_student_clazz` (clazz_id);


create index ix_classify on `sys_label` (classify);


create index ix_academic_year_semester_id_grade_id_week_sort_order_clazz_id on `sys_course` (academic_year_semester_id,grade_id,week,sort_order,clazz_id);


create index ix_grade_id_sort_order on `sys_course_time` (grade_id,sort_order);


create unique index ix_name on `sys_role` (name);


create unique index ix_username on `sys_user` (username);
create unique index ix_login_number on `sys_user` (login_number);


create unique index ix_name on `sys_authority` (name);
create index ix_parent_id on `sys_authority` (parent_id);


create index ix_role_id on `sys_role_authority` (role_id);
create index ix_authority_id on `sys_role_authority` (authority_id);


create unique index ix_code on `sys_settings` (code);


create index ix_teacher_id on `tmg_teaching_result` (teacher_id);
create index ix_academic_year_semester_id on `tmg_teaching_result` (academic_year_semester_id);
create index ix_result_classify_id on `tmg_teaching_result` (result_classify_id);
create index ix_level on `tmg_teaching_result` (level);


create index ix_teaching_result_id on `tmg_teaching_result_attachment` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_history` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_grry` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_jtry` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_bjry` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_lwfb` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_lwpbhj` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_lzcb` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_cbzc` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_ktyj` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_sfkks` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_ykhj` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_jzks` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_grjshj` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_zdxshj` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_zdqnjs` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_jxpx` (teaching_result_id);


create index ix_teaching_result_id on `tmg_teaching_result_wkhj` (teaching_result_id);


create index ix_student_id_teacher_id on `dat_student_remark` (student_id,teacher_id);

create index ix_grade_id on `dat_exam_publish` (grade_id);
create index ix_publish_time on `dat_exam_publish` (publish_time);


create index ix_exam_publish_id on `dat_exam_publish_relation` (exam_publish_id);

create index ix_grade_id on `dat_exam` (grade_id);


create unique index ix_name on `dat_exam_type` (name);


create index ix_create_time on `dat_exam_goal_template` (create_time);


create index ix_exam_id on `dat_exam_goal` (exam_id);


create index ix_exam_goal_id on `dat_exam_goal_clazz` (exam_goal_id);


create index ix_exam_id on `dat_exam_goal_sub` (exam_id);


create index ix_grade_id on `dat_exam_goal_natural_template` (grade_id);


create index ix_exam_id on `dat_exam_goal_natural` (exam_id);


create index ix_exam_id on `dat_exam_result` (exam_id);
create index ix_student_id on `dat_exam_result` (student_id);
create index ix_clazz_id on `dat_exam_result` (clazz_id);


create index ix_staff_id on `dat_practice` (staff_id);
create index ix_create_time on `dat_practice` (create_time);


create index ix_practice_id on `dat_practice_topic` (practice_id);
create index ix_clazz_id on `dat_practice_topic` (clazz_id);


create index ix_teacher_id on `day_teacher_duty` (teacher_id);
create index ix_start_time on `day_teacher_duty` (start_time);


create index ix_teacher_duty_id on `day_teacher_duty_clazz` (teacher_duty_id);
create index ix_clazz_id on `day_teacher_duty_clazz` (clazz_id);


create index ix_teacher_duty_id on `day_teacher_duty_substitute` (teacher_duty_id);
create index ix_teacher_id on `day_teacher_duty_substitute` (teacher_id);


create index ix_teacher_duty_clazz_id on `day_night_study` (teacher_duty_clazz_id);


create index ix_night_study_id on `day_night_study_detail` (night_study_id);


create index ix_night_study_detail_id on `day_night_study_detail_image` (night_study_detail_id);


create index ix_start_time on `day_leader_duty` (start_time);
create index ix_leader_id on `day_leader_duty` (leader_id);


create index ix_leader_duty_id on `day_leader_duty_substitute` (leader_duty_id);
create index ix_leader_id on `day_leader_duty_substitute` (leader_id);


create index ix_leader_duty_id on `day_south_gate` (leader_duty_id);
create index ix_start_time on `day_south_gate` (start_time);


create index ix_south_gate_id on `day_south_gate_images` (south_gate_id);


create index ix_leader_duty_id on `day_breakfast` (leader_duty_id);
create index ix_start_time on `day_breakfast` (start_time);


create index ix_breakfast_id on `day_breakfast_images` (breakfast_id);


create index ix_leader_duty_id on `day_morning_reading` (leader_duty_id);
create index ix_start_time on `day_morning_reading` (start_time);


create index ix_morning_reading_id on `day_morning_reading_images` (morning_reading_id);


create index ix_leader_duty_id on `day_break_activity` (leader_duty_id);
create index ix_start_time on `day_break_activity` (start_time);


create index ix_break_activity_id on `day_break_activity_images` (break_activity_id);


create index ix_leader_duty_id on `day_lunch` (leader_duty_id);
create index ix_start_time on `day_lunch` (start_time);


create index ix_lunch_id on `day_lunch_images` (lunch_id);


create index ix_leader_duty_id on `day_teaching_area` (leader_duty_id);
create index ix_start_time on `day_teaching_area` (start_time);


create index ix_teaching_area_id on `day_teaching_area_images` (teaching_area_id);


create index ix_leader_duty_id on `day_noon_sport_area` (leader_duty_id);
create index ix_start_time on `day_noon_sport_area` (start_time);


create index ix_sport_area_id on `day_noon_sport_area_images` (sport_area_id);


create index ix_leader_duty_id on `day_dinner` (leader_duty_id);
create index ix_start_time on `day_dinner` (start_time);


create index ix_dinner_id on `day_dinner_images` (dinner_id);


create index ix_leader_duty_id on `day_go_out` (leader_duty_id);
create index ix_start_time on `day_go_out` (start_time);


create index ix_go_out_id on `day_go_out_images` (go_out_id);


create index ix_leader_duty_id on `day_night_sport_area` (leader_duty_id);
create index ix_start_time on `day_night_sport_area` (start_time);


create index ix_sport_area_id on `day_night_sport_area_images` (sport_area_id);


create index ix_leader_duty_id on `day_night_study_duty` (leader_duty_id);
create index ix_start_time on `day_night_study_duty` (start_time);


create index ix_night_study_duty_id on `day_night_study_duty_grade_deduction` (night_study_duty_id);


create index ix_night_study_duty_id_grade_id on `day_night_study_duty_grade_images` (night_study_duty_id,grade_id);


create index ix_night_study_duty_id on `day_night_study_duty_clazz` (night_study_duty_id);


create index ix_night_study_duty_clazz_id on `day_night_study_duty_clazz_deduction` (night_study_duty_clazz_id);


create index ix_daily_routine_id on `day_incident` (daily_routine_id);


create index ix_incident_id on `day_incident_images` (incident_id);


create index ix_daily_routine_id on `day_comment` (daily_routine_id);


create index ix_comment_id on `day_comment_images` (comment_id);


create index ix_comment_id on `day_comment_process` (comment_id);


create index ix_comment_process_id on `day_comment_task` (comment_process_id);
create index ix_function_department_id on `day_comment_task` (function_department_id);


create index ix_clazz_id_register_date on `std_clazz_teaching_log` (clazz_id,register_date);


create index ix_clazz_teaching_log_id_sort_order on `std_clazz_teaching_log_subjects` (clazz_teaching_log_id,sort_order);


create index ix_clazz_teaching_log_id_subject_id on `std_clazz_teaching_log_operation` (clazz_teaching_log_id,subject_id);


create index ix_clazz_id_register_date on `std_daily_attendance` (clazz_id,register_date);


create index ix_clazz_id_register_date on `std_night_study_attendance` (clazz_id,register_date);


create index ix_clazz_id_register_date on `std_ill` (clazz_id,register_date);


create index ix_name on `mes_message_template` (name);


create index ix_name on `mes_message_task` (name);


create index ix_message_task_id on `mes_message_task_receiver` (message_task_id);


create index ix_message_task_id on `mes_message` (message_task_id);
create index ix_receiver_id on `mes_message` (receiver_id);
create index ix_sender_id on `mes_message` (sender_id);

create index ix_exam_publish_id on `dat_exam_print_log` (exam_publish_id);
create index ix_student_id on `dat_exam_print_log` (student_id);

create index ix_visit_time on `sys_visit_log` (visit_time);

create index ix_staff_id_name on `sys_staff_message_refuse` (staff_id,name);