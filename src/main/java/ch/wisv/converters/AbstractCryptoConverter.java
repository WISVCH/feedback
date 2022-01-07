package ch.wisv.converters;

import static ch.wisv.converters.KeyProperty.DATABASE_ENCRYPTION_KEY;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.AttributeConverter;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * AbstractCryptoConverter class.
 *
 * @param <T>
 */
abstract class AbstractCryptoConverter<T> implements AttributeConverter<T, String> {

    /**
     * Used for concatenating cipher text and iv together.
     * This will not cause issues when the text contains hashtags,
     * since a Base64 encoded string cannot contain hashtags.
     * https://en.wikipedia.org/wiki/Base64#Base64_table
     */
    private static final String CONCATENATION = "####";

    /** CipherInitializer. */
    private CipherInitializer cipherInitializer;

    /**
     * AbstractCryptoConverter constructor.
     */
    public AbstractCryptoConverter() {
        this(new CipherInitializer());
    }

    /**
     * AbstractCryptoConverter constructor.
     *
     * @param cipherInitializer of type CipherInitializer
     */
    public AbstractCryptoConverter(CipherInitializer cipherInitializer) {
        this.cipherInitializer = cipherInitializer;
    }

    /**
     * Convert th entity attribute to the database data.
     *
     * @param attribute of type T
     *
     * @return String
     */
    @Override
    public String convertToDatabaseColumn(T attribute) {
        if (isNotEmpty(DATABASE_ENCRYPTION_KEY) && isNotNullOrEmpty(attribute)) {
            try {
                Cipher cipher = cipherInitializer.prepareAndInitCipher(Cipher.ENCRYPT_MODE, DATABASE_ENCRYPTION_KEY, null);

                return this.concatenatedCipherTextAndIv(this.encrypt(cipher, attribute), cipher.getIV());
            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException |
                    NoSuchPaddingException | IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            }
        }

        return this.entityAttributeToString(attribute);
    }

    /**
     * Convert the database data to the entity attribute.
     *
     * @param dbData of type String.
     *
     * @return T
     */
    @Override
    public T convertToEntityAttribute(String dbData) {
        if (isNotEmpty(DATABASE_ENCRYPTION_KEY) && isNotEmpty(dbData)) {
            try {
                Pair<String, byte[]> cipherTextAndIv = this.splitDbData(dbData);
                Cipher cipher = cipherInitializer.prepareAndInitCipher(Cipher.DECRYPT_MODE, DATABASE_ENCRYPTION_KEY, cipherTextAndIv.getRight());

                return this.decrypt(cipher, cipherTextAndIv.getLeft());
            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException |
                    NoSuchPaddingException | IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            }
        }

        return this.stringToEntityAttribute(dbData);
    }

    /**
     * Concatenated cipher text and IV together.
     *
     * @param cipherText of type String
     * @param iv         of type byte[]
     *
     * @return String
     */
    private String concatenatedCipherTextAndIv(String cipherText, byte[] iv) {
        return cipherText + CONCATENATION + Base64.getEncoder().encodeToString(iv);
    }

    /**
     * Split db data into cipher text and IV.
     *
     * @param dbData of type String
     *
     * @return Pair
     */
    private Pair<String, byte[]> splitDbData(String dbData) {
        String[] splitDbData = dbData.split(CONCATENATION);
        String cipherText = splitDbData[0];
        byte[] iv = Base64.getDecoder().decode(splitDbData[1]);

        return new ImmutablePair<>(cipherText, iv);
    }

    /**
     * Do final Cipher call.
     *
     * @param cipher of type String
     * @param bytes  of type byte[]
     *
     * @return byte[]
     *
     * @throws IllegalBlockSizeException when the block size is wrong
     * @throws BadPaddingException       when the padding is wrong
     */
    private byte[] callCipherDoFinal(Cipher cipher, byte[] bytes) throws IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(bytes);
    }

    /**
     * Decrypt database data with a give cipher.
     *
     * @param cipher of type Cipher
     * @param dbData of type String
     *
     * @return T
     *
     * @throws IllegalBlockSizeException when the block size is wrong
     * @throws BadPaddingException       when the padding is wrong
     */
    private T decrypt(Cipher cipher, String dbData) throws IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = Base64.getDecoder().decode(dbData);
        byte[] decryptedBytes = this.callCipherDoFinal(cipher, encryptedBytes);

        return this.stringToEntityAttribute(new String(decryptedBytes));
    }

    /**
     * Encrypt attribute with a give cipher.
     *
     * @param cipher    of type Cipher
     * @param attribute of type T
     *
     * @return String
     *
     * @throws IllegalBlockSizeException when the block size is wrong
     * @throws BadPaddingException       when the padding is wrong
     */
    private String encrypt(Cipher cipher, T attribute) throws IllegalBlockSizeException, BadPaddingException {
        byte[] bytesToEncrypt = this.entityAttributeToString(attribute).getBytes();
        byte[] encryptedBytes = this.callCipherDoFinal(cipher, bytesToEncrypt);

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Check attribute is not null or empty.
     *
     * @param attribute of type T
     *
     * @return boolean
     */
    abstract boolean isNotNullOrEmpty(T attribute);

    /**
     * Convert database data to entity attribute.
     *
     * @param dbData of type String
     *
     * @return T
     */
    abstract T stringToEntityAttribute(String dbData);

    /**
     * Convert entity attribute to database data.
     *
     * @param attribute of type T
     *
     * @return String
     */
    abstract String entityAttributeToString(T attribute);
}
