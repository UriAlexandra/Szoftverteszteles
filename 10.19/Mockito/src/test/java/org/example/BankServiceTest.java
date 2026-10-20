package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BankService bankService;

    @Test
    void depositSendsNotification() {
        // Arrange
        BankAccount account = new BankAccount();

        // Act
        bankService.deposit(account, 1000);

        // Assert
        // 1. Állapot ellenőrzése
        assertEquals(1000.0, account.getBalance());

        // 2. Mockito viselkedés ellenőrzése
        verify(notificationService).send("Deposit successful");
    }
}