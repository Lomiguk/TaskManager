package org.example.taskmanager.configuration;

import lombok.RequiredArgsConstructor;
import org.example.taskmanager.security.JwtFilter;
import org.example.taskmanager.util.ProfileUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.example.taskmanager.util.SecurityConfigurationConstraint.PERMITTED_ALL;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfiguration {

    public static final List<String> ORIGINS_PATTERNS = List.of("*");
    public static final Boolean ALLOW_CREDENTIALS = true;
    public static final List<String> HTTP_METHODS = List.of("GET", "POST", "PUT", "PATCH", "DELETE");
    public static final List<String> HEADERS = List.of("*");

    private final ProfileUtil profileUtil;
    private final JwtFilter jwtFilter;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(
                        request -> {
                            var corsConfiguration = new CorsConfiguration();
                            corsConfiguration.setAllowedOriginPatterns(ORIGINS_PATTERNS);
                            corsConfiguration.setAllowCredentials(ALLOW_CREDENTIALS);
                            corsConfiguration.setAllowedMethods(HTTP_METHODS);
                            corsConfiguration.setAllowedHeaders(HEADERS);
                            return corsConfiguration;
                        }
                ))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(PERMITTED_ALL).permitAll()
                        //.requestMatchers("/profiles/**").hasAuthority(ADMIN_ROLE)
                        .requestMatchers("/tasks/**").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(profileUtil.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
