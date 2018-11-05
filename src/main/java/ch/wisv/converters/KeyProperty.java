package ch.wisv.converters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * KeyProperty component.
 */
@Component
public class KeyProperty {

    /** Encryption key. */
    static String DATABASE_ENCRYPTION_KEY;

    /**
     * Set encryption key.
     */
    @Value("${wisvch.database.encryption.key}")
    public void setDatabase(String databaseEncryptionKey) {
        DATABASE_ENCRYPTION_KEY = databaseEncryptionKey;
    }

}
