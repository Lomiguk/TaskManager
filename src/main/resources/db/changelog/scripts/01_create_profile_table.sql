CREATE TABLE profile
(
    id       UUID         PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(256) NOT NULL,
    password TEXT         NOT NULL,
    active   BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT profile_email_u UNIQUE (email)
);

COMMENT ON COLUMN profile.id       IS 'Unique user identifier';
COMMENT ON COLUMN profile.name     IS 'User nickname';
COMMENT ON COLUMN profile.email    IS 'User email';
COMMENT ON COLUMN profile.password IS 'User password';
COMMENT ON COLUMN profile.active   IS 'Profile activity status';