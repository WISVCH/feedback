package ch.wisv.converters;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * StringCryptoConverter.
 */
public class StringCryptoConverter extends AbstractCryptoConverter<String> {

    /**
     * StringCryptoConverter constructor.
     *
     * @param cipherInitializer of type CipherInitializer
     */
    StringCryptoConverter(CipherInitializer cipherInitializer) {
        super(cipherInitializer);
    }

    /**
     * Implementation of isNotNullOrEmpty.
     *
     * @param attribute of type T
     *
     * @return boolean
     */
    @Override
    boolean isNotNullOrEmpty(String attribute) {
        return isNotEmpty(attribute);
    }

    /**
     * Implementation of stringToEntityAttribute.
     *
     * @param dbData of type String
     *
     * @return String
     */
    @Override
    String stringToEntityAttribute(String dbData) {
        return dbData;
    }

    /**
     * Implementation of entityAttributeToString.
     *
     * @param attribute of type T
     *
     * @return String
     */
    @Override
    String entityAttributeToString(String attribute) {
        return attribute;
    }
}
