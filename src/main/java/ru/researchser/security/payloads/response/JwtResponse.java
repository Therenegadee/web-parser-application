package ru.researchser.security.payloads.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class JwtResponse {
    private final String accessToken;
    private final Long id;
    private final String username;
}
