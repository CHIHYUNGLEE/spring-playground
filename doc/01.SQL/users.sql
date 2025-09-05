CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,   -- 회원 고유 번호
    user_id VARCHAR(50) NOT NULL UNIQUE,    -- 이름
    user_name VARCHAR(50) NOT NULL UNIQUE,  -- 아이디
    password VARCHAR(255) NOT NULL,         -- 비밀번호 (암호화 필요)
    email VARCHAR(100) NOT NULL UNIQUE,     -- 이메일
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 가입일
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 정보 수정일
    role VARCHAR(20) DEFAULT 'USER'         -- 권한 (USER / ADMIN 등)
);

--admin 계정 추가
INSERT INTO users (user_id, user_name, password, email, role) VALUES ('이치형', 'admin', '$2a$10$cI9sFLwyD.AnCSDL.lsg1ue38/49ObKCrs/bT/1VjLjT/g./yTYTy', 'admin@example.com', 'ADMIN');