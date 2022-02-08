SELECT c.group_id, c.course_id, st.id AS slot_type, c.semester_id, c.max_student
FROM Class_student cs
    JOIN Class c ON cs.class_id = c.id
    JOIN Slot_type st ON cs.class_id = st.class_id
WHERE cs.student_id = 'SE160001';


SELECT c.group_id, c.course_id, st.id AS slot_type, c.semester_id, c.max_student
FROM Class c
    JOIN Slot_type st ON c.id = st.class_id
WHERE c.course_id = 'PRF192' AND NOT c.group_id = 'SE1601';


SELECT *
FROM Slot_type st
WHERE st.class_id = 'SE1602_PRF192';

UPDATE Class_student
SET class_id = 'SE1602_PRF192'
WHERE student_id = 'SE160001' AND class_id = 'SE1601_PRF192';

SELECT * 
FROM slot s 
WHERE s.class_id = 'SE1601_PRF192';

DELETE FROM Attendance 
WHERE student_id = 'SE160001' AND slot_id LIKE 'SE1601_PRF192%';

INSERT Attendance (student_id, slot_id, status)
VALUES ('SE160001', 'SE1602_PRF192_2305_22', 'NOT YET');
