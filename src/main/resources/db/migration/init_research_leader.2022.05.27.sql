truncate table sys_staff_research_leader;

INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 1, 'YES' from sys_staff where name='王玉忠';
INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 1, 'YES' from sys_staff where name='武健';

INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 2, 'YES' from sys_staff where name='刘金华';
INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 2, 'YES' from sys_staff where name='胡祥志';

INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 3, 'YES' from sys_staff where name='陈春阳';

INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 4, 'YES' from sys_staff where name='钱嘉伟';

INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 5, 'YES' from sys_staff where name='蒋腊梅';

INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 6, 'YES' from sys_staff where name='杨克俊';

INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 9, 'YES' from sys_staff where name='林庆';

INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 7, 'YES' from sys_staff where name='杨梅';

INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 8, 'YES' from sys_staff where name='李强';

INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 11, 'YES' from sys_staff where name='徐庆利';

INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 10, 'YES' from sys_staff where name='谢红娟';
INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 12, 'YES' from sys_staff where name='谢红娟';

INSERT INTO sys_staff_research_leader (`staff_id`, `subject_id`, `is_current`)
 select id, 13, 'YES' from sys_staff where name='朱熙春';


