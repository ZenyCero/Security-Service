package com.social.securityservice.persistence.repository;

import com.social.securityservice.persistence.entity.Users;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<Users, Long> {
    Mono<Users> findByUsernameOrEmail(String username, String email);
}
