package pt.isel.vulnerableapp.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * VULNERABILIDADES:
 * - CWE-502 - Deserialization of Untrusted Data
 * - CWE-327 - Use of Broken Cryptographic Algorithm
 * 
 * @author C-Academy - Engenharia de Software Seguro
 */
public class SecurityUtils {

    // ==================== CWE-502: Insecure Deserialization ====================

    /**
     * VULNERÁVEL: Desserialização de dados não confiáveis
     * 
     * Ataque: Enviar objeto serializado malicioso (ysoserial)
     */
    public Object deserializeUserSession(HttpServletRequest request) {
        String sessionData = request.getParameter("session"); 
        
        if (sessionData == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("session".equals(cookie.getName())) {
                        sessionData = cookie.getValue(); 
                        break;
                    }
                }
            }
        }
        
        if (sessionData != null) {
            try {
                byte[] data = Base64.getDecoder().decode(sessionData);
                
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais); 
                
                return ois.readObject();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }

    /**
     * VULNERÁVEL: Desserialização de ficheiro
     */
    public Object loadObjectFromFile(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) { 
            
            return ois.readObject();
            
        } catch (Exception e) {
            return null;
        }
    }

    // ==================== CWE-327: Weak Cryptographic Algorithms ====================

    /**
     * VULNERÁVEL: MD5 para hash de passwords
     */
    public String hashPasswordMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); 
            
            byte[] hash = md.digest(password.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * VULNERÁVEL: SHA-1 para integridade
     */
    public String hashFileSHA1(byte[] fileContent) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1"); 
            
            byte[] hash = md.digest(fileContent);
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * VULNERÁVEL: DES para encriptação
     */
    public byte[] encryptWithDES(String data, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(
                key.getBytes(), 0, 8, "DES"
            );
            
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); 
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            return cipher.doFinal(data.getBytes());
            
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * VULNERÁVEL: AES com ECB mode
     */
    public byte[] encryptWithAESECB(String data, byte[] key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); 
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            return cipher.doFinal(data.getBytes());
            
        } catch (Exception e) {
            return null;
        }
    }
}
