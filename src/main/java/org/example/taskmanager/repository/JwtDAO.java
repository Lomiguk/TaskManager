package org.example.taskmanager.repository;

import org.example.taskmanager.entity.Jwt;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;
import java.util.List;

public interface JwtDAO extends CrudRepository<Jwt, UUID> {
    void deleteByProfileId(UUID profileId);

    List<Jwt> getByProfileId(UUID profileId);

    Jwt findByToken(String token);
}