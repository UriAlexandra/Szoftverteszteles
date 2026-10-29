package org.example;

/**
 * Értesítési szolgáltatás (pl. e-mail, SMS vagy push-értesítés).
 *
 * <p>Éles környezetben egy valódi, külső szolgáltatás valósítja meg. A tesztekben
 * Mockito-val készült mock objektum helyettesíti, így nem megy ki valódi üzenet.
 */
public interface NotificationService {

    /** Elküldi az üzenetet. */
    void send(String message);
}
