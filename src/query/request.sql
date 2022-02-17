SELECT c.course_id FROM Class_student cs JOIN class c ON c.id = cs.class_id WHERE cs.student_id = 'SE160001';

SELECT st.id, c.id, st.teacher_id FROM Slot_type st JOIN class c ON c.id = st.class_id WHERE c.course_id = 'PRF192';
SELECT st.id, st.teacher_id FROM Slot_type st WHERE st.class_id = 'SE1601_PRF192';