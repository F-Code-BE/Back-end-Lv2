SELECT DISTINCT c.id, c.group_id, c.course_id FROM slot s JOIN Class c on s.class_id = c.id WHERE s.teacher_id = 'Hoa01';
-- enter class id
-- enter course id

SELECT * FROM Class_student WHERE class_id = 'SE1601_PRF192';
-- enter student id

SELECT * FROM Mark WHERE student_id = 'SE160002';

-- if the student didn't have mark
INSERT INTO Mark VALUES ('SE1601_PRF192', 'SE160001 ', 7, 'pass');

-- If the student already had mark
UPDATE Mark SET gpa = '6.5', status = 'PASS' WHERE student_id = 'SE160002';


-- check if the student adbsent more than 3 slot
SELECT * FROM Attendance a WHERE student_id = 'SE160001' AND slot_id LIKE 'SE1601_PRF192%';    