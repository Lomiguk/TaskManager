package org.example.taskmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.taskmanager.api.enums.TaskPriority;
import org.example.taskmanager.api.enums.TaskStatus;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name="task")
@AllArgsConstructor
public class Task implements Cloneable{

    @Id
    private UUID id;
    private String label;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
    private UUID authorId;
    private UUID executorId;

    @Override
    public Task clone() {
        try {
            var clone = (Task) super.clone();
            clone.setId(id);
            clone.setLabel(label);
            clone.setDescription(description);
            clone.setStatus(status);
            clone.setPriority(priority);
            clone.setAuthorId(authorId);
            clone.setExecutorId(executorId);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
