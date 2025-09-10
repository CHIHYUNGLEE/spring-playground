CREATE TABLE board_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,   -- 글 번호 (PK)
    title VARCHAR(200) NOT NULL,            -- 제목
    content TEXT NOT NULL,                  -- 내용
    author_id BIGINT NOT NULL,              -- 작성자 ID (users 테이블 FK)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 댓글
CREATE TABLE board_comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,                 -- 어떤 게시글의 댓글인지
    parent_id BIGINT NULL,                   -- 부모 댓글 (NULL이면 일반 댓글, 값 있으면 대댓글)  
    user_id BIGINT NOT NULL,                 -- 작성자
    content VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    CONSTRAINT fk_post FOREIGN KEY (post_id)
        REFERENCES board_posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_parent FOREIGN KEY (parent_id)
        REFERENCES board_comments(id) ON DELETE CASCADE
);