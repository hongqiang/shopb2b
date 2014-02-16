package com.hongqiang.shop.common.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Assert;

public final class RSAUtils
{
  private static final Provider provider = new BouncyCastleProvider();
  private static final int VALUE = 1024;

  public static KeyPair generateKeyPair()
  {
    try
    {
      KeyPairGenerator localKeyPairGenerator = KeyPairGenerator.getInstance("RSA", provider);
      localKeyPairGenerator.initialize(VALUE, new SecureRandom());
      return localKeyPairGenerator.generateKeyPair();
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      localNoSuchAlgorithmException.printStackTrace();
    }
    return null;
  }

  public static byte[] encrypt(PublicKey publicKey, byte[] data)
  {
    Assert.notNull(publicKey);
    Assert.notNull(data);
    try
    {
      Cipher localCipher = Cipher.getInstance("RSA", provider);
      localCipher.init(1, publicKey);
      return localCipher.doFinal(data);
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    return null;
  }

  public static String encrypt(PublicKey publicKey, String text)
  {
    Assert.notNull(publicKey);
    Assert.notNull(text);
    byte[] arrayOfByte = encrypt(publicKey, text.getBytes());
    return arrayOfByte != null ? Base64.encodeBase64String(arrayOfByte) : null;
  }

  public static byte[] decrypt(PrivateKey privateKey, byte[] data)
  {
    Assert.notNull(privateKey);
    Assert.notNull(data);
    try
    {
      Cipher localCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
      localCipher.init(2, privateKey);
      return localCipher.doFinal(data);
    }
    catch (Exception localException1)
    {
    }
    return null;
  }

  public static String decrypt(PrivateKey privateKey, String text)
  {
    Assert.notNull(privateKey);
    Assert.notNull(text);
    byte[] arrayOfByte = decrypt(privateKey, Base64.decodeBase64(text));
    return arrayOfByte != null ? new String(arrayOfByte) : null;
  }
}