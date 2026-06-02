package com.shopHMsic.repository;

import com.shopHMsic.entities.Token;
import com.shopHMsic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);

    @Transactional
    void deleteByUser(User user);
}
