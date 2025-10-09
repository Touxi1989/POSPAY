package com.telpo.pospay.main.util;

/**
 * Created by liangjz on 2016/10/20.
 */
public class RsaKey {
    public java.security.PublicKey getPublicKey(String modulus, String publicExponent) throws Exception {

        java.math.BigInteger m = new java.math.BigInteger(modulus);

        java.math.BigInteger e = new java.math.BigInteger(publicExponent);

        java.security.spec.RSAPublicKeySpec keySpec = new java.security.spec.RSAPublicKeySpec(m,e);

        java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
        java.security.PublicKey publicKey = keyFactory.generatePublic(keySpec);

        return publicKey;

    }



    public java.security.PrivateKey getPrivateKey(String modulus, String privateExponent) throws Exception {

        java.math.BigInteger m = new java.math.BigInteger(modulus);

        java.math.BigInteger e = new java.math.BigInteger(privateExponent);

        java.security.spec.RSAPrivateKeySpec keySpec = new java.security.spec.RSAPrivateKeySpec(m,e);

        java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");

        java.security.PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;

    }



}
