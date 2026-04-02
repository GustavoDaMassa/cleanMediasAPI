package br.com.gustavohenrique.cleanmediasapi.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Mapeia as propriedades jwt.* do application.properties.
// kebab-case (public-key) → camelCase (publicKey) é feito automaticamente pelo Spring.
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String publicKey,
        String privateKey,
        long expirationSeconds
) {}
