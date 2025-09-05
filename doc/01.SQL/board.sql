CREATE TABLE board_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,   -- 글 번호 (PK)
    title VARCHAR(200) NOT NULL,            -- 제목
    content TEXT NOT NULL,                  -- 내용
    author_id BIGINT NOT NULL,              -- 작성자 ID (users 테이블 FK)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);
