package com.healthpay.auth_service.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.healthpay.auth_service.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class JwtService {

    // Lê a variável 'api.security.token.secret' do nosso application.yml
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um token JWT para o usuário que acabou de fazer login.
     */
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("auth-service")
                    .withSubject(user.getEmail())
                    .withIssuedAt(Date.from(Instant.now()))
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro enquanto gerava o token", exception);
        }
    }

    /**
     * Valida um token JWT e retorna o e-mail do usuário dono dele (o 'Subject').
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("auth-service")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            // Se o token for inválido, adulterado ou expirado, cai aqui.
            return "";
        }
    }

    /**
     * Método auxiliar para gerar um tempo de expiração (2 horas a partir de agora).
     */
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
