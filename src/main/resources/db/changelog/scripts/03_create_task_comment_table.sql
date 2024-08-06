CREATE TABLE task_comment
(
    id        UUID PRIMARY KEY,
    author_id UUID NOT NULL,
    task_id   UUID NOT NULL,
    content   TEXT NOT NULL,
    CONSTRAINT task_comment_author_fk FOREIGN KEY (author_id) REFERENCES profile (id),
    CONSTRAINT task_comment_task_fk   FOREIGN KEY (task_id)   REFERENCES task (id)
);

COMMENT ON COLUMN task_comment.id IS 'Unique task comment identifier';
COMMENT ON COLUMN task_comment.author_id IS 'Comment author ID';
COMMENT ON COLUMN task_comment.task_id IS 'Task ID';
COMMENT ON COLUMN task_comment.content IS 'Comment Content';