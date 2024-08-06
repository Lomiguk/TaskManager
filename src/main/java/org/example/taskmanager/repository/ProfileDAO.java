package org.example.taskmanager.repository;

import org.example.taskmanager.entity.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProfileDAO extends CrudRepository<Profile, UUID> {
    @Query(value = """
    FROM Profile AS p
    ORDER BY p.id
    LIMIT ?1
    OFFSET ?2
    """)
    List<Profile> findAllWithPagination(Integer limit, Integer offset);
}
