--Companies or employers table

CREATE TABLE companies_or_employers(

    id                  UUID                NOT NULL,
    comp_name           VARCHAR(100)        NOT NULL,
    location            VARCHAR(100)        NOT NULL ,
    comp_email          VARCHAR(100)        NOT NULL ,
    password            VARCHAR(50)         NOT NULL ,
    role                VARCHAR(10)         NOT NULL ,

    CONSTRAINT  pk_companies_or_employers   PRIMARY KEY (id),
    CONSTRAINT  uq_companies_or_employee    UNIQUE (comp_name)

);

CREATE INDEX    idx_companies_or_employers_id           ON companies_or_employers(id);
CREATE INDEX    idx_companies_or_employers_comp_name    ON companies_or_employers(comp_name);
CREATE INDEX    idx_companies_or_employers_comp_email   ON companies_or_employers(comp_email);

--regular users or job-seekers table

CREATE TABLE regular_users(

    id                  UUID                 NOT NULL ,
    name                VARCHAR(100)         NOT NULL ,
    age                 INTEGER(7)           NOT NULL,
    gender              VARCHAR(15)          NOT NULL ,
    profession          VARCHAR(50)          NOT NULL ,
    email               VARCHAR(50)          NOT NULL ,
    password            VARCHAR(50)          NOT NULL ,
    roles               VARCHAR(50)          NOT NULL ,

    CONSTRAINT pk_regular_users              PRIMARY KEY (id),
    CONSTRAINT uq_regular_users_email        UNIQUE (email)

);

CREATE INDEX   idx_regular_users_id                     ON regular_users(id);
CREATE INDEX   idx_regular_users_name                   ON regular_users(name);
CREATE INDEX   idx_regular_users_email                  ON regular_users(email);


--jobs table

CREATE TABLE jobs(

    id                  UUID                NOT NULL ,
    job_title           VARCHAR(50)         NOT NULL ,
    description         VARCHAR(255)        NOT NULL ,
    salary              VARCHAR(20)         DEFAULT "Negotiable",
    field               VARCHAR(20)         NOT NULL ,
    availability        VARCHAR(20)         NOT NULL ,
    posted_at           TIMESTAMPTZ         NOT NULL ,
    posted_by           VARCHAR(100)        NOT NULL ,
    company_id          UUID                NOT NULL ,

    CONSTRAINT pk_jobs                      PRIMARY KEY (id),
    CONSTRAINT fk_jobs_companies_or_employers   FOREIGN KEY (company_id)
);

CREATE INDEX    idx_jobs_id             ON jobs(id);
CREATE INDEX    idx_jobs_job_title      ON jobs(job_title);
CREATE INDEX    idx_jobs_field          ON jobs(field);


--job applications table

CREATE TABLE applications(

    id                  UUID                NOT NULL ,
    info                VARCHAR(200)        NOT NULL ,
    applied_at          TIMESTAMPTZ         NOT NULL ,
    status              VARCHAR(20)         NOT NULL ,
    job_id              UUID                NOT NULL ,
    regular_users_id    UUID                NOT NULL ,

    CONSTRAINT pk_applications              PRIMARY KEY (id),
    CONSTRAINT fk_applications_job_id       FOREIGN KEY (job_id),
    CONSTRAINT fk_applications_regular_users_id     FOREIGN KEY (regular_users_id)
);

CREATE INDEX idx_applications_id        ON jobs(id);
CREATE INDEX idx_applications_status    ON jobs(status);


-- refresh token table

CREATE TABLE refresh_tokens(

    id                  UUID                NOT NULL,
    token               UUID                NOT NULL ,
    created_at          TIMESSTAMPTZ        NOT NULL ,
    expires_at          TIMESTAMPTZ         NOT NULL ,
    regular_users_id    UUID                NOT NULL ,
    company_id          UUID                NOT NULL ,

    CONSTRAINT pk_refresh_tokens            PRIMARY KEY (id),
    CONSTRAINT uq_refresh_tokens            UNIQUE  (tokens),
    CONSTRAINT fk_refresh_tokens_reg_user_id        FOREIGN KEY (regular_users_id),
    CONSTRAINT fk_refresh_tokens_comp_id    FOREIGN KEY (company_id)
);

CREATE INDEX idx_refresh_token_id       ON refresh_tokens(id);
CREATE INDEX idx_refresh_token_token    ON refresh_tokens(tokens);

-- notifications

CREATE TABLE notifications(

    id                  UUID                NOT NULL ,
    name                VARCHAR(100)        NOT NULL ,
    email               VARCHAR(50)         NOT NULL,

    CONSTRAINT pk_notifications    PRIMARY KEY (id)
);


