package com.dentalapp.backend.services;

import com.dentalapp.backend.model.token.entity.ConfirmationToken;
import com.dentalapp.backend.model.token.expceptions.InvalidTokenException;
import com.dentalapp.backend.model.token.expceptions.TokenAlreadyUsedException;
import com.dentalapp.backend.model.token.expceptions.TokenExpiredException;
import com.dentalapp.backend.model.token.repository.ConfirmationTokenRepository;
import com.dentalapp.backend.model.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Transactional
    public String saveConfirmationToken(User user) {
        String tokenCode = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(tokenCode);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        confirmationToken.setConfirmedAt(null);
        confirmationToken.setUser(user);
        confirmationTokenRepository.save(confirmationToken);
        return tokenCode;
    }

    @Transactional
    public Long confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new TokenAlreadyUsedException("Token already confirmed");
        }
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expired");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken.getUser().getUserId();
    }
}
