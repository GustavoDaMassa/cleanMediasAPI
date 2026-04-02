package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Spring Data JPA — trabalha apenas com UserJpaEntity, nunca com User de domínio.
// Package-private: ninguém fora deste pacote deve injetar esta interface diretamente.
// Quem precisa de persistência de usuário injeta UserRepository (domínio), não esta.
interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
