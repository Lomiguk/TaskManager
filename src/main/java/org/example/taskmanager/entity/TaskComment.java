package org.example.taskmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="task_comment")
public class TaskComment {

    @Id
    private UUID id;
    @Column(name="author_id")
    private UUID authorId;
    @Column(name="task_id")
    private UUID taskId;
    private String content;
}
