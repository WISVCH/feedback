package ch.wisv.converters;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * CipherInitializer.
 */
class CipherInitializer {

    /** Encryption method. */
    private static final String CIPHER_INSTANCE_NAME = "AES/CBC/PKCS5Padding";

    /** Secret key algorithm. */
    private static final String SECRET_KEY_ALGORITHM = "AES";

    /**
     * Creates an IvParameterSpec object using the bytes in iv as the IV.
     * The IV is fixed such that we do not have to save the IV.
     * TODO: Random IV to improve security.
     *
     * @param cipher of type Cipher
     *
     * @return AlgorithmParameterSpec
     */
    private AlgorithmParameterSpec getAlgorithmParameterSpec(Cipher cipher) {
        byte[] iv = new byte[cipher.getBlockSize()];

        return new IvParameterSpec(iv);
    }

    /**
     * Prepare Cipher by adding the key and creating an IV.
     *
     * @param encryptionMode of type int
     * @param key            of type String
     *
     * @return Cipher
     *
     * @throws InvalidKeyException                when key is invalid
     * @throws NoSuchPaddingException             when padding method does not exists
     * @throws NoSuchAlgorithmException           when the algorithm does not exists
     * @throws InvalidAlgorithmParameterException when the mode of operation does not exists
     */
    Cipher prepareAndInitCipher(int encryptionMode, String key) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
                                                                       InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME);
        Key secretKey = new SecretKeySpec(key.getBytes(), SECRET_KEY_ALGORITHM);

        AlgorithmParameterSpec algorithmParameters = this.getAlgorithmParameterSpec(cipher);
        cipher.init(encryptionMode, secretKey, algorithmParameters);

        return cipher;
    }
}
