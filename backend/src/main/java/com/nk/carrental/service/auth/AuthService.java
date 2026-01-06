package com.nk.carrental.service.auth;

import com.nk.carrental.dto.auth.AuthResponse;
import com.nk.carrental.dto.auth.LoginRequest;
import com.nk.carrental.dto.auth.RegisterRequest;
import com.nk.carrental.entity.User;
import com.nk.carrental.repository.RoleRepository;
import com.nk.carrental.repository.UserRepository;
import com.nk.carrental.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthService(UserRepository users,
                       RoleRepository roles,
                       PasswordEncoder encoder,
                       AuthenticationManager authManager,
                       JwtService jwtService) {
        this.users = users;
        this.roles = roles;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public void register(RegisterRequest req) {
        if (users.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        var roleUser = roles.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER missing from DB"));

        User u = User.builder()
                .email(req.email())
                .passwordHash(encoder.encode(req.password()))
                .enabled(true)
                .build();

        u.getRoles().add(roleUser);
        users.save(u);
    }

    public AuthResponse login(LoginRequest req) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);

        return new AuthResponse(token);
    }
}
