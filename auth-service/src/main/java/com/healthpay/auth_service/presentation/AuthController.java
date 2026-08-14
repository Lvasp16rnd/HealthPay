package com.healthpay.auth_service.presentation;

import com.healthpay.auth_service.domain.User;
import com.healthpay.auth_service.domain.UserRepository;
import com.healthpay.auth_service.infrastructure.security.JwtService;
import com.healthpay.auth_service.presentation.dto.AuthRequestDTO;
import com.healthpay.auth_service.presentation.dto.AuthResponseDTO;
import com.healthpay.auth_service.presentation.dto.RegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDTO data) {
        if (userRepository.findByEmail(data.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já cadastrado");
        }

        User newUser = new User();
        newUser.setEmail(data.email());
        newUser.setPassword(passwordEncoder.encode(data.password()));
        newUser.setRole(data.role());

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        var auth = authenticationManager.authenticate(usernamePassword);

        var user = (User) auth.getPrincipal();
        var tokenJWT = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponseDTO(tokenJWT));
    }
}
