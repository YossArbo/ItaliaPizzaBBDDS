/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.utils.password;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/** 
 * @author Yoss
 */
public class SeguridadUtil {
    
    private static final String ALGORITMO = "SHA-256";
    
    /**
     * Constructor
     */
    private SeguridadUtil() {}
    
    /**
     * Genera el hash SHA-256 para la contraseña ingresada.
     * 
     * @param contrasena la contraseña en texto plano
     * @return el hash SHA-256 codificado en Base64
     * @throws RuntimeException si el algoritmo SHA-256 no está disponible
     */
    public static String hashearContrasena(String contrasena) {
        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITMO);
            byte[] hashBytes = digest.digest(contrasena.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear contraseña: Algoritmo SHA-256 no disponible", e);
        }
    }
    
    /**
     * Valida una contraseña comparándola con su hash.
     * 
     * @param contrasenaplano la contraseña en texto plano a validar
     * @param hashAlmacenado el hash SHA-256 almacenado en base de datos
     * @return true si la contraseña coincide con el hash, false en caso contrario
     */
    public static boolean validarContrasena(String contrasenaplano, String hashAlmacenado) {
        if (contrasenaplano == null || contrasenaplano.isEmpty() || hashAlmacenado == null) {
            return false;
        }
        String hashCalculado = hashearContrasena(contrasenaplano);
        return hashCalculado.equals(hashAlmacenado);
    }
        
}
