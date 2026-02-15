package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.token.entity.ConfirmationToken;
import com.dentalapp.backend.model.token.exceptions.InvalidTokenException;
import com.dentalapp.backend.model.token.exceptions.TokenAlreadyUsedException;
import com.dentalapp.backend.model.token.exceptions.TokenExpiredException;
import com.dentalapp.backend.model.token.repository.ConfirmationTokenRepository;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.ConfirmationTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenServiceTests {

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @InjectMocks
    private ConfirmationTokenService confirmationTokenService;

    private final String token = "token";

    @Test
    void testSaveConfirmationToken() {
        User user = new User();
        user.setUserId(1L);

        String tokenCode = confirmationTokenService.saveConfirmationToken(user);
        ArgumentCaptor<ConfirmationToken> tokenCaptor = ArgumentCaptor.forClass(ConfirmationToken.class);
        verify(confirmationTokenRepository).save(tokenCaptor.capture());
        ConfirmationToken savedToken = tokenCaptor.getValue();

        assertNotNull(tokenCode);
        Assertions.assertEquals(tokenCode, savedToken.getToken());
        Assertions.assertEquals(user, savedToken.getUser());
        assertNotNull(savedToken.getCreatedAt());
        assertNotNull(savedToken.getExpiresAt());
        Assertions.assertEquals(savedToken.getCreatedAt().plusMinutes(15).withNano(0), savedToken.getExpiresAt().withNano(0));
        assertNull(savedToken.getConfirmedAt());
    }

    @Test
    void testConfirmToken() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setExpiresAt(LocalDateTime.of(2030, 1, 1, 1, 1));
        confirmationToken.setUser(new User());
        when(confirmationTokenRepository.findByToken(token)).thenReturn(Optional.of(confirmationToken));
        Long userId = confirmationTokenService.confirmToken(token);
        Assertions.assertEquals(confirmationToken.getUser().getUserId(), userId);
    }

    @Test
    void testConfirmTokenNotFound() {
        when(confirmationTokenRepository.findByToken(token)).thenReturn(Optional.empty());
        Assertions.assertThrows(InvalidTokenException.class, () -> confirmationTokenService.confirmToken(token));
    }

    @Test
    void testConfirmTokenAlreadyConfirmed() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setConfirmedAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        when(confirmationTokenRepository.findByToken(token)).thenReturn(Optional.of(confirmationToken));
        Assertions.assertThrows(TokenAlreadyUsedException.class, () -> confirmationTokenService.confirmToken(token));
    }

    @Test
    void testConfirmTokenExpired() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setExpiresAt(LocalDateTime.of(2022, 1, 1, 1, 1));
        when(confirmationTokenRepository.findByToken(token)).thenReturn(Optional.of(confirmationToken));
        Assertions.assertThrows(TokenExpiredException.class, () -> confirmationTokenService.confirmToken(token));
    }
}
