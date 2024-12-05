package rw.wetech.HospitalManagementSystem.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import rw.wetech.HospitalManagementSystem.dto.AuthResponse;
import rw.wetech.HospitalManagementSystem.dto.LoginRequest;
import rw.wetech.HospitalManagementSystem.model.User;
import rw.wetech.HospitalManagementSystem.userRepository.UserRepository;
import rw.wetech.HospitalManagementSystem.util.JwtService;

@Service
@Transactional
@Slf4j
public class AuthenticationService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    public AuthResponse authenticate(LoginRequest request) {
        try {
            log.info("Login attempt for username: {}", request.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtService.generateToken(user);
            return new AuthResponse(user.getUsername(), user.getRole().toString(), token);
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw e;
        }
    }
}
