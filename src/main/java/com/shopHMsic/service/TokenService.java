package com.shopHMsic.service;

import com.shopHMsic.entities.Token;
import com.shopHMsic.entities.User;
import com.shopHMsic.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TokenService extends BaseService<Token> {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected Class<Token> clazz() {
        return Token.class;
    }

    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Transactional
    public void deleteByUser(User user) {
        tokenRepository.deleteByUser(user);
    }
}
