package ch.wisv.converters;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class StringCryptoConverterTest {

    private StringCryptoConverter cryptoConverter;

    @Before
    public void setUp() {
        KeyProperty.DATABASE_ENCRYPTION_KEY = "KaPdSgVkYp3s6v12";
        CipherInitializer cipherInitializer = new CipherInitializer();
        this.cryptoConverter = new StringCryptoConverter(cipherInitializer);
    }

    @Test
    public void testEncryptDecrypt() {
        String message = "this is a secret message";

        String cipherPlusIv = cryptoConverter.convertToDatabaseColumn(message);
        String decryptedCipher = cryptoConverter.convertToEntityAttribute(cipherPlusIv);

        assertEquals(message, decryptedCipher);
    }
}