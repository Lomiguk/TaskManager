package org.example.taskmanager.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordUtil {

    private final PasswordEncoder encoder;


    public String getEncodedPassword(String origin) {
        return encoder.encode(origin);
    }
}
