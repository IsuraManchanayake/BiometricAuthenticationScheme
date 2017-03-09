/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricauthenticationscheme;

/**
 *
 * @author Isura Manchanayake
 */
public class Encryptor {
    private final String key = "thekey";
    
    public String encrypt(String text) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0, s = text.length(); i < s; i++) {
            sb.append((char)(text.charAt(i) ^ key.charAt(i % 6)));
        }
        return sb.toString();
    }
    
    public String decrypt(String cipher) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0, s = cipher.length(); i < s; i++) {
            sb.append((char)(cipher.charAt(i) ^ key.charAt(i % 6)));
        }
        return sb.toString();
    }
    
}
