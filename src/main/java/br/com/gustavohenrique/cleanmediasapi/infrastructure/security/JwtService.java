package br.com.gustavohenrique.cleanmediasapi.infrastructure.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;

@Service
public class JwtService {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;
    private final long expirationSeconds;

    public JwtService(JwtProperties props) {
        try {
            // As chaves são armazenadas como base64(PEM completo com headers).
            // PKCS8EncodedKeySpec e X509EncodedKeySpec esperam DER puro (sem headers PEM).
            // Fluxo: base64 decode → texto PEM → strip headers → base64 decode do corpo → DER bytes
            KeyFactory kf = KeyFactory.getInstance("RSA");
            this.privateKey = (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(derFromPem(props.privateKey())));
            this.publicKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(derFromPem(props.publicKey())));
            this.expirationSeconds = props.expirationSeconds();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize JWT service: " + e.getMessage(), e);
        }
    }

    private byte[] derFromPem(String base64Pem) {
        String pem = new String(Base64.getDecoder().decode(base64Pem));
        String derBase64 = pem
                .replaceAll("-----BEGIN.*?-----", "")
                .replaceAll("-----END.*?-----", "")
                .replaceAll("\\s+", "");
        return Base64.getDecoder().decode(derBase64);
    }

    public String generateToken(String subject, String role) {
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(
                new JWKSet(new RSAKey.Builder(publicKey).privateKey(privateKey).build())
        );
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationSeconds))
                .claim("roles", role)
                .build();
        return new NimbusJwtEncoder(jwkSource)
                .encode(JwtEncoderParameters.from(
                        JwsHeader.with(SignatureAlgorithm.RS256).build(),
                        claims))
                .getTokenValue();
    }

    // Exposto para o SecurityConfig configurar o decoder sem acoplamento circular
    public JwtDecoder decoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
}
