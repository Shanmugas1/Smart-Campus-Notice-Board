INSERT INTO users (name, username, password, role) VALUES
('Campus Admin', 'admin', 'admin123', 'ADMIN'),
('Test Student', 'student', 'student123', 'STUDENT');

INSERT INTO notices (title, description, category, posted_by, post_date, expiry_date) VALUES
('Semester Exam Schedule', 'Final exams start Aug 5th.', 'EXAM', 'Campus Admin', CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY)),
('Tech Fest 2026', 'Annual tech fest registrations open.', 'EVENT', 'Campus Admin', CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 15 DAY));
