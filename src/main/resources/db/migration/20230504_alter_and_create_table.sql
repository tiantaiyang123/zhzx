-- 晚自习值班图片填写感悟
alter table `day_night_study_duty_grade_images` add column `remark` varchar(255) NULL COMMENT '' after `url`;

-- 创建公开
drop table if exists `sys_public_course`;
create table `sys_public_course`
(
    id int(11) auto_increment not null comment 'ID系统自动生成',
    academic_year_semester_id int(11) not null comment '学年学期ID sys_academic_year_semester.id',
    schoolyard_id int(11) not null comment '校区 sys_schoolyard.id',
    grade_id int(11) not null comment '年级ID sys_grade.id',
    start_time datetime not null default CURRENT_TIMESTAMP comment '开始时间',
    sort_order varchar(50) not null default 0 comment '节次,逗号分割',
    course_name varchar(255) not null comment '课程名称',
    clazz_id varchar(255) not null comment '班级ID sys_clazz.id 逗号分割',
    clazz_name varchar(255) null comment '班级',
    teacher_id int(11) not null comment '老师ID sys_staff.id',
    teacher_name varchar(50) null comment '老师',
    subject_name varchar(50) null comment '课程组别名称',
    address varchar(255) not null comment '地址',
    classify varchar(50) not null comment '类别',
    editor_id int(11) not null default 1 comment '操作人ID',
    editor_name varchar(255) not null default 'admin' comment '操作人',
    create_time datetime not null default CURRENT_TIMESTAMP,
    update_time datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='公开课表';