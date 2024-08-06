package org.example.taskmanager.repository;

import org.example.taskmanager.entity.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfileDAO extends CrudRepository<Profile, UUID> {
}
