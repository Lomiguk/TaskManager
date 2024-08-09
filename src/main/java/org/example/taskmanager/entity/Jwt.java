package org.example.taskmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.taskmanager.api.enums.JwtType;

import java.util.UUID;

@Data
@Entity
@Getter
@Table(name = "jwt_token")
@NoArgsConstructor
@AllArgsConstructor
public class Jwt {

    @Id
    private UUID id;
    @Column(name = "profile_id")
    private UUID profileId;
    private String token;
    private JwtType type;
}
