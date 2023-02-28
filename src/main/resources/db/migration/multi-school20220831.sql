-- 班级表
alter table `sys_clazz` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;

-- 教师值班
alter table `day_teacher_duty` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;
create index ix_schoolyard_id on `day_teacher_duty` (schoolyard_id);
create unique index ix_teacher_id_start_time_duty_type on `day_teacher_duty` (teacher_id,start_time,duty_type);

-- 领导值班
alter table `day_leader_duty` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;
create index ix_schoolyard_id on `day_leader_duty` (schoolyard_id);
create unique index ix_leader_id_start_time on `day_leader_duty` (leader_id,start_time);

-- 偶发事件
alter table `day_incident` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;

-- 意见建议
alter table `day_comment` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;

-- routine item
alter table `day_break_activity` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;
alter table `day_breakfast` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;
alter table `day_dinner` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;
alter table `day_go_out` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;
alter table `day_lunch` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;
alter table `day_morning_reading` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;
alter table `day_night_sport_area` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;
alter table `day_noon_sport_area` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;
alter table `day_south_gate` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;
alter table `day_teaching_area` add column `schoolyard_id` int(11) NOT NULL DEFAULT 1 COMMENT '校区ID sys_schoolyard.id' after `id`;

-- modify curr data
update sys_schoolyard set name = '兴隆校区' where id=1;
insert sys_schoolyard (name, faculty) values('雨花校区','高中部');
update sys_clazz set schoolyard_id = 2 where academic_year_semester_id=26 and (id between 89 and 94 or id between 105 and 109);

-- 生僻字教职工修改
update day_teacher_duty set teacher_id=286 where teacher_id=305;
-- modify duty data
update day_teacher_duty a
right join
    (
        select
        distinct b.teacher_duty_id
        from day_teacher_duty_clazz b
        left join sys_clazz c on c.id=b.clazz_id
        where c.schoolyard_id=2
    ) d
on d.teacher_duty_id=a.id
set a.schoolyard_id=2
where to_days(a.start_time)>=to_days(str_to_date('2022-08-30', '%Y-%m-%d')) and a.duty_type not in ('GRADE_TOTAL_DUTY', 'TOTAL_DUTY');