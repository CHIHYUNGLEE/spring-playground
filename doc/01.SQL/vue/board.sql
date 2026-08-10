-- 20260807 vue 관련 실습 사용 
-- db : spring_playground, id : myuser pw : 1234
CREATE TABLE board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL
);
SELECT * FROM board;
update board set title = '공지사항 등록 테스트' where id = 3;
update board set title = 'Vue 연동 CRUD 확인' where id = 4;
INSERT INTO board(title)
VALUES ('Spring Boot API 연동 테스트');
commit;

--내용 추가
ALTER TABLE board
ADD COLUMN content TEXT;