/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author vinicius.pinheiro
 */
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class Criptografia {

    public static final String CHAVE_ALTERNATIVA = "configuracaoPDVnS2e63206";
    public static final String CHAVE = "c0nf1gpr0p#1";
    
    public static final String criptografarTexto(String texto, String chave) throws Exception {

        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        PBEParameterSpec ps = new PBEParameterSpec(new byte[]{3, 1, 4, 1, 5, 9, 2, 6}, 20);
        KeySpec ks = new PBEKeySpec(chave.toCharArray());
        SecretKey skey = skf.generateSecret(ks);
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(Cipher.ENCRYPT_MODE, skey, ps);
        return Base64.getEncoder().encodeToString(cipher.doFinal(texto.getBytes()));
    }

    @Deprecated
    public static final String criptografarString(String texto, String chave) throws Exception {
        return criptografarTexto(texto, chave);
    }

    public static final String descriptografarTexto(String texto, String chave) throws Exception {

        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        KeySpec ks = new PBEKeySpec(chave.toCharArray());
        PBEParameterSpec ps = new PBEParameterSpec(new byte[]{3, 1, 4, 1, 5, 9, 2, 6}, 20);
        SecretKey skey = skf.generateSecret(ks);
        cipher.init(Cipher.DECRYPT_MODE, skey, ps);
        try {
            return new String(cipher.doFinal(Base64.getDecoder().decode(texto)));
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            throw new Exception("Erro ao descriptografar texto", ex);
        }
    }

    @Deprecated
    public static final String descriptografarString(String texto, String chave) throws Exception {
        return descriptografarTexto(texto, chave);
    }

     public static String gerarHash(String campo) throws Exception {       
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(campo.getBytes());
        byte[] hashMd5 = md.digest();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < hashMd5.length; i++) {
            int parteAlta = ((hashMd5[i] >> 4) & 0xf) << 4;
            int parteBaixa = hashMd5[i] & 0xf;
            if (parteAlta == 0) {
                s.append('0');
            }
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return s.toString();
    }

}
