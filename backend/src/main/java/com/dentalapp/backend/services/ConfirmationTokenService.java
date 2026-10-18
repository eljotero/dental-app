package com.dentalapp.backend.services;

import com.dentalapp.backend.model.token.entity.ConfirmationToken;
import com.dentalapp.backend.model.token.exceptions.InvalidTokenException;
import com.dentalapp.backend.model.token.exceptions.TokenAlreadyUsedException;
import com.dentalapp.backend.model.token.exceptions.TokenExpiredException;
import com.dentalapp.backend.model.token.repository.ConfirmationTokenRepository;
import com.dentalapp.backend.model.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS)
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

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
                .orElseThrow(InvalidTokenException::new);
        if (confirmationToken.getConfirmedAt() != null) {
            throw new TokenAlreadyUsedException();
        }
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken.getUser().getUserId();
    }
}
