ALTER TABLE refresh_tokens ALTER COLUMN regular_users_id DROP NOT NULL;
ALTER TABLE refresh_tokens ALTER COLUMN company_id  DROP NOT NULL ;

ALTER TABLE refresh_tokens
ADD CONSTRAINT chk_refresh_tokens_one_owner CHECK ( (regular_users_id IS NULL ) <> (company_id IS NULL) );

--the <> is exclusive OR (XOR), makes sure that only one user is tied to a refresh token record