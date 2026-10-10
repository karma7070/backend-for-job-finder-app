CREATE TABLE event_publication (
                                   id                      UUID            NOT NULL,
                                   publication_date        TIMESTAMPTZ     NOT NULL,
                                   listener_id             VARCHAR(255)    NOT NULL,
                                   serialized_event        VARCHAR(255)    NOT NULL,
                                   event_type              VARCHAR(255)    NOT NULL,
                                   completion_date          TIMESTAMPTZ,
                                   last_resubmission_date   TIMESTAMPTZ,
                                   completion_attempts      INTEGER         NOT NULL DEFAULT 0,
                                   status                   VARCHAR(20),

                                   CONSTRAINT pk_event_publication PRIMARY KEY (id)
);

ALTER TABLE companies_or_employers DROP CONSTRAINT uq_companies_or_employee;
