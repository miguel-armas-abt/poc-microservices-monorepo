package com.demo.bbq.commons.toolkit.cryptography;

public interface CryptographyStrategy {

    String encrypt(String value);
    String decrypt(String value);

    boolean supports(String alias);
}
