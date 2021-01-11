/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author USER
 */
public class sdp {
    public static void encrypt(String key, InputStream is, OutputStream os)
   throws Exception {encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
   }

 public static void decrypt(String key, InputStream is, OutputStream os)
   throws Exception {encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
 }

public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os)
        throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IOException, InvalidAlgorithmParameterException{
    
       DESKeySpec dks = new DESKeySpec(key.getBytes());
       SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
       SecretKey desKey = skf.generateSecret(dks);
       Cipher cipher = Cipher.getInstance("DES/CBC/PRCS5Padding");
       
       byte[] ivBytes = new byte[8];
       IvParameterSpec iv = new IvParameterSpec(ivBytes);
       
        if(mode == Cipher.ENCRYPT_MODE){
           cipher.init(Cipher.ENCRYPT_MODE, desKey, iv, SecureRandom.getInstance("SHA1PRNG"));
           CipherInputStream cis = new CipherInputStream(is, cipher);
           makeFile(cis, os);
       }
       else if(mode == Cipher.DECRYPT_MODE){
           cipher.init(Cipher.DECRYPT_MODE, desKey, iv, SecureRandom.getInstance("SHA1PRNG"));
           CipherOutputStream cos = new CipherOutputStream(os, cipher);
           makeFile(is, cos);
       }
   }
   private static void makeFile(InputStream is, OutputStream os) throws IOException{
       byte[] bytes = new byte[64];
       int numBytes;
       while((numBytes = is.read(bytes)) != -1){
           os.write(bytes, 0, numBytes);
       }
       os.flush();
       os.close();
       is.close();
   }

}