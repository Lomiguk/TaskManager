CREATE TABLE task
(
    id          UUID         PRIMARY KEY,
    label       VARCHAR(256) NOT NULL,
    description TEXT         NOT NULL,
    status      TEXT         NOT NULL DEFAULT 'WAITING',
    priority    TEXT         NOT NULL
);

COMMENT ON COLUMN task.id          IS 'Unique user identifier';
COMMENT ON COLUMN task.label       IS 'Label of the task';
COMMENT ON COLUMN task.description IS 'Description of the task';
COMMENT ON COLUMN task.status      IS 'Task progress status';
COMMENT ON COLUMN task.priority    IS 'Task priority';
