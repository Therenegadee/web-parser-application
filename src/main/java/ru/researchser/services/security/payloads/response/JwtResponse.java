package ru.researchser.services.security.payloads.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class JwtResponse {
    private final String accessToken;
    private final String type = "Bearer ";
    private final Long id;
    private final String username;
    private final String email;
    private final List<String> roles;
}
