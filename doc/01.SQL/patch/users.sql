--비밀번호 초기화.
ALTER TABLE users
ADD COLUMN reset_token VARCHAR(255) DEFAULT NULL,
ADD COLUMN reset_token_expiry DATETIME DEFAULT NULL;


-- 0: 정상
-- 9100: 탈퇴
ALTER TABLE users ADD COLUMN status INT DEFAULT 0;
