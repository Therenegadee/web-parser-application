package parser.userService.security.payloads.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtResponse {
    private final String accessToken;
    private final Long id;
    private final String username;
}
