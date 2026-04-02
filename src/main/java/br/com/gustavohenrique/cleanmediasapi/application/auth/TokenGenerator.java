package br.com.gustavohenrique.cleanmediasapi.application.auth;

import br.com.gustavohenrique.cleanmediasapi.domain.user.User;

// Porta de saída da camada de aplicação.
// AuthServiceImpl depende desta interface — não do JwtService diretamente.
// Isso inverte a dependência: a infrastructure implementa o contrato definido pela aplicação.
public interface TokenGenerator {

    String generate(User user);
}
