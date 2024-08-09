package org.example.taskmanager.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtType {

    ACCESS("ACCESS"),
    REFRESH("REFRESH");

    private final String name;
}
