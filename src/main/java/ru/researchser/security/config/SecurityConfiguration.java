package ru.researchser.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.researchser.security.services.AuthTokenFilter;
import ru.researchser.security.services.AuthEntryPointJwt;
import ru.researchser.security.user.UserDetailsService;
import ru.researchser.security.utils.CryptoUtil;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;
    @Value("${mail-service.crypto-util.salt}")
    private String salt;

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig
                .getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request.requestMatchers
                                (
                                        "/api/auth/**",
                                        "/api/test/**",
                                        "/api/parser/**",
                                        "/api/telegram/auth/**"
                                )
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider())
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    public CryptoUtil cryptoUtil() {
        return new CryptoUtil(salt);
    }
}
