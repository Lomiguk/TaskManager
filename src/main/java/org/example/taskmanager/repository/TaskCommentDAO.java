package org.example.taskmanager.repository;

import org.example.taskmanager.entity.TaskComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskCommentDAO extends JpaRepository<TaskComment, UUID> {

    Page<TaskComment> findAllByTaskId(UUID id, Pageable pageable);
}
