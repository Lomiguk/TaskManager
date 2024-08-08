package org.example.taskmanager.repository;

import org.example.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface TaskDAO extends JpaRepository<Task, UUID> {

    Collection<Task> findTaskByAuthorId(UUID authorId);

    Collection<Task> findTaskByExecutorId(UUID executorId);

    Boolean existsByAuthorId(UUID authorId);
    Boolean existsByExecutorId(UUID authorId);
}
