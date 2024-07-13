package com.demo.bbq.commons.toolkit.cryptography.aes;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.toolkit.cryptography.CryptographyStrategy;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

@Slf4j
@RequiredArgsConstructor
public class AESEncryptor implements CryptographyStrategy {

    public static final String AES_METHOD = "AES";
    public static final String ADVANCED_ENCRYPTION_STANDARD_ALGORITHM_NAME = "AES";
    public static final String ADVANCED_ENCRYPTION_STANDARD_ALGORITHM_MODE = "AES/GCM/NoPadding";

    private final ConfigurationBaseProperties properties;

    public String encrypt(String value) {
        return encrypt(getPublicKey(), value);
    }

    public String decrypt(String cipherMessage) {
        return decrypt(getPublicKey(), cipherMessage);
    }

    private String getPublicKey() {
        return properties.getCryptography().get(AES_METHOD.toLowerCase());
    }

    public boolean supports(String encryptionMethod) {
        return AES_METHOD.equals(encryptionMethod);
    }

    public static String encrypt(String aesKey, String value) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] encryptionSeed = aesKey.getBytes(StandardCharsets.UTF_8);
            byte[] iv = new byte[12];
            secureRandom.nextBytes(iv);

            SecretKey secretKey = new SecretKeySpec(encryptionSeed, ADVANCED_ENCRYPTION_STANDARD_ALGORITHM_NAME);

            Cipher cipher = Cipher.getInstance(ADVANCED_ENCRYPTION_STANDARD_ALGORITHM_MODE);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] cipherText = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));

            ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + cipherText.length);
            byteBuffer.putInt(iv.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);

            return new Base64().encodeAsString(byteBuffer.array());
        } catch (NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException("Invalid message or key", e);
        }
    }

    public static String decrypt(String aesKey, String cipherMessage) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.decodeBase64(cipherMessage));
            int ivLength = byteBuffer.getInt();
            byte[] encryptionSeed = aesKey.getBytes(StandardCharsets.UTF_8);
            if (ivLength < 12 || ivLength >= 16) {
                throw new IllegalArgumentException("Invalid iv length");
            }

            byte[] iv = new byte[ivLength];
            byteBuffer.get(iv);

            byte[] cipherText = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherText);

            SecretKeySpec key = new SecretKeySpec(encryptionSeed, ADVANCED_ENCRYPTION_STANDARD_ALGORITHM_NAME);

            Cipher cipher = Cipher.getInstance(ADVANCED_ENCRYPTION_STANDARD_ALGORITHM_MODE);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

            byte[] decryptedData = cipher.doFinal(cipherText);
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid value", e);
        }
    }
}