package org.example.taskmanager.repository;

import org.example.taskmanager.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfileDAO extends JpaRepository<Profile, UUID> {

    Boolean existsProfileByEmail(String email);
}
