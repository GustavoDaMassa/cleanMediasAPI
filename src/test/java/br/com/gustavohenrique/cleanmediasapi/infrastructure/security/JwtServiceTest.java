package br.com.gustavohenrique.cleanmediasapi.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    // Chaves RSA 2048 geradas exclusivamente para testes — não usar em produção
    private static final String TEST_PRIVATE_KEY =
            "LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2UUlCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktjd2dnU2pBZ0VBQW9JQkFRQzRBVzhNSFFydFNSdnEKRFNQekVxM3FrS3VmTFhGUzhldXFlRjlOdk9McjVuMXNIWTNLNWtCd25EajFRNlcyVk5WVXgvclVFUURUYzQ1VwpnZGE4ZFZ2dVZBUFduTXN4dGNOL3ZkWXJKV0EwcWtWMkJqbDRLSjZyYUhGYit0amVkYWRBTHJSRS94TjNVelRxCnlBTVh1ZWpoSXk4V2ozYlVPd0xsOWh5azdvK0loMVQzYnlFTU15ZEhlUkU0UDJNb2NsU0xBWWVydG43RXdDT3oKc3FZQ3JTQzJJQUJRVUwyMFB4bGhiTkJDWU4xUU5ERE5SRXVZYjZyS2pJSmF6RU5IajE5VVlkcmQzY21wRVdMcgpHOUZaTFZzejJXaGVMZ21OMUpjUlNNeUhqNDNCOTA4SDg3MXJnNDMwZDBWS0p0d2VQT0QrMGZlb2tvVG4vNjhyCmFPQjc4QSsxQWdNQkFBRUNnZ0VBUXpmYTNqejUwZWNFT0lNbVg5U3lEZDlkb3ArU0oyL1MzSXB1bmxKalB6UUwKRzJUTWVKT1dkTXpORGVQeGRHTVhMNHFDZWRmZ1NsNkxYOVM4b2tucEZHNUNuTExZazhkSHJMR252RWxDRk5zaQoxdE85U01UbCtjNEVvMHBHMFRWL3JMV2pDeFYzZ2Y2MmRSSXQ5d3ZtSStDYmdnMVVwb0VNTlArc1JTQVU5UldECjIxSG1tdXd2NDlFN0U0TVdoODRnM1o5blZVdWlZZmRXcU9XVzFXbjhURlYyZ21PdEdqQkhJOVo0SDFyWFRwMmIKYUxpUFVZMHlDNW92UEdjTlVPK1M5OUtxQys2NTE3UXhKQ0FqUFc1T2x4T3NzNUNUWVJZcC9nQm9zQ0ZnN25kOQpzbk9rdERvRCs0TThBTm1QbWpmZkIvTFM3SjQ0Ung4MjMvL2FRVStMM1FLQmdRRHR6cVBxREF3Y1pWNVAxSHZVCllzVFlrdjR2d1l6UytRUVhlZTNxaytTUDVSY0FYTm1aZStsVkh3ajZVREs5Wkc0UytiNHd2cG80SmJ0MkErdEUKWTllOWRjNjdZOFNBbmVEMVhNOExweFpkTVMwUlBhTStxNDBONCtIaWFyaGp6Rk15QmhGNkRWTjAvckpJc0EzVgpXd2JkQVBqT2VNOURScWtFTTNWdzN2SmZtd0tCZ1FER0ZSeFZGNnNsTUJaQlBNYVAzUzgyOVlwRWYrQUs5NmRMCjlyNE5URWQ3d2pwY00yTEpuQUs3ZXF2U05KSzNpQkFpQTY5c0NwUFVXUFZVeEdTZVJLT3c0NGVMTy9Gc3BZcWwKWkJ6N1JhQVdHN2FZNVp3K2Q3ZmxoUm53Y3FQWkJyYVVDcnZsS2hXSXNoempVcW10Qk5tWC9xUWdETkVOS0FBVwpSOTk2NzNKSzd3S0JnR2hMODdubmRMeFRmUXlZRFlNMW1rMitFanRSc1BpcWZDL3JxdnZ4eHhHUkVRaDRzSHJQClRLT25pK0Ywb1B4TmVyVlowUjlCeVJPTkFPMmVoR1duUUd4NHFhV24rUHZMd21qNlBsaEcvTmo4ZzE2SFlQeFEKY3ZtL2swY2hOU09YN25rSERrMHdZUVR6MjY5amYzMnVnWkVXSndrdkZQL1ZrclVqK0lMN1ZPTzNBb0dCQUpBUwpQTGc3YmRTdmhCQlJGdzcrVG1BSCtWaFhOZXBrTkg5UTZPeWRKWVVEUVJWY25xYUszWVhrOUdJQXNSSGlXZkdUCmdwenZtSlFqVzlVYXZ5QktSbERiUFBQdFZteXlmd1JLcTdXcTc0UUZCaXR4SGRKbG14REtYWktBbjJHenUwbzAKQUhQdXh4alVpNlJmdEYvT25rRXFDYy94NG45U0w3WUU4cXZreXRsckFvR0Flcm5sT2N3K2Y5MERGR29OM2preAo0RzlOWExtQjVUVld5WmEyV0svY3dWL3F5THdnMFpMdWcrMWdCNitrYURNRDV5ZzNlVE8yTTRvRzhRK2FpYSswCkc3YzdTNnBaelgyL2hnenE1QXMxdTQxb0JNbVFiM2JhT3BSaS9haFJQb0N1YXdsekp1d0kyQmZwaFlwL09ubk4KZlFEN3dwdnZyQ0FQOENtbzh5cXhjNXM9Ci0tLS0tRU5EIFBSSVZBVEUgS0VZLS0tLS0K";

    private static final String TEST_PUBLIC_KEY =
            "LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUF1QUZ2REIwSzdVa2I2ZzBqOHhLdAo2cENybnkxeFV2SHJxbmhmVGJ6aTYrWjliQjJOeXVaQWNKdzQ5VU9sdGxUVlZNZjYxQkVBMDNPT1ZvSFd2SFZiCjdsUUQxcHpMTWJYRGY3M1dLeVZnTktwRmRnWTVlQ2llcTJoeFcvclkzblduUUM2MFJQOFRkMU0wNnNnREY3bm8KNFNNdkZvOTIxRHNDNWZZY3BPNlBpSWRVOTI4aERETW5SM2tST0Q5aktISlVpd0dIcTdaK3hNQWpzN0ttQXEwZwp0aUFBVUZDOXREOFpZV3pRUW1EZFVEUXd6VVJMbUcrcXlveUNXc3hEUjQ5ZlZHSGEzZDNKcVJGaTZ4dlJXUzFiCk05bG9YaTRKamRTWEVVak1oNCtOd2ZkUEIvTzlhNE9OOUhkRlNpYmNIanpnL3RIM3FKS0U1Lyt2SzJqZ2UvQVAKdFFJREFRQUIKLS0tLS1FTkQgUFVCTElDIEtFWS0tLS0tCg==";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        JwtProperties props = new JwtProperties(TEST_PUBLIC_KEY, TEST_PRIVATE_KEY, 3600L);
        jwtService = new JwtService(props);
    }

    @Test
    void shouldGenerateAndDecodeValidToken() {
        String token = jwtService.generateToken("joao@test.com", "USER");

        assertThat(token).isNotBlank();

        JwtDecoder decoder = jwtService.decoder();
        Jwt jwt = decoder.decode(token);

        assertThat(jwt.getSubject()).isEqualTo("joao@test.com");
        assertThat(jwt.getClaim("roles").toString()).isEqualTo("USER");
    }

    @Test
    void shouldGenerateTokenWithCorrectExpiration() {
        String token = jwtService.generateToken("maria@test.com", "ADMIN");

        JwtDecoder decoder = jwtService.decoder();
        Jwt jwt = decoder.decode(token);

        assertThat(jwt.getExpiresAt()).isNotNull();
        assertThat(jwt.getExpiresAt()).isAfter(jwt.getIssuedAt());
    }
}
