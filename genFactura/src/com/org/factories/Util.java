/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.factories;

/**
 *
 * @author oswaldo
 */
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Util {

    static String eq[][] = { //equivalencia
        {"2", "03", "Boleta"}
    };

    static String rutaini = "/home/certificado/";
    static String rutafin = "/sunat/sunat-docs/config/";


    public static String getPathZipFilesEnvio(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));

            msg = "" + propiedades.getProperty("pathFilesEnvio");

        } catch (Exception ex) {
            msg = "Error en getPathFilesEnvio: " + ex.getMessage();
        }
        return msg;
    }

    public static String getPathZipFilesRecepcion(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));
            msg = "" + propiedades.getProperty("pathFilesRecepcion");
        } catch (Exception ex) {
            msg = "Error en getpathFilesRecepcion: " + ex.getMessage();
        }
        return msg;
    }

    public static String getPathJasperFiles(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));
            msg = "" + propiedades.getProperty("pathFilesJasper");
        } catch (Exception ex) {
            msg = "Error en getpathFilesRecepcion: " + ex.getMessage();
        }
        return msg;
    }

    public static String getPathFilesLogo(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));
            msg = "" + propiedades.getProperty("pathFilesLogo");
        } catch (Exception ex) {
            msg = "Error en getpathFilesRecepcion: " + ex.getMessage();
        }
        return msg;
    }

    public static String getPurl(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));
            msg = "" + propiedades.getProperty("p_url");
        } catch (Exception ex) {
            msg = "Error en getpathFilesRecepcion: " + ex.getMessage();
        }
        return msg;
    }

    public static String getPResolucion(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));
            msg = "" + propiedades.getProperty("p_resolucion");
        } catch (Exception ex) {
            msg = "Error en getpathFilesRecepcion: " + ex.getMessage();
        }
        return msg;
    }

    public static String getFtpServer(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));

            msg = "" + propiedades.getProperty("subirFtpServer");

        } catch (Exception ex) {
            msg = "Error en getPathFilesEnvio: " + ex.getMessage();
        }
        return msg;
    }

    public static String getFtpUser(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));

            msg = "" + propiedades.getProperty("subirFtpUser");

        } catch (Exception ex) {
            msg = "Error en getPathFilesEnvio: " + ex.getMessage();
        }
        return msg;
    }

    public static String getFtpPass(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));

            msg = "" + propiedades.getProperty("subirFtpPass");

        } catch (Exception ex) {
            msg = "Error en getPathFilesEnvio: " + ex.getMessage();
        }
        return msg;
    }

    public static String getFtpBase(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));

            msg = "" + propiedades.getProperty("subirFtpBase");

        } catch (Exception ex) {
            msg = "Error en getPathFilesEnvio: " + ex.getMessage();
        }
        return msg;
    }

    public static String getPropertyValue(String paramName,String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));

            msg = "" + propiedades.getProperty(paramName);
        } catch (Exception ex) {
            msg = "Error en getPathFiles: " + ex.getMessage();
        }
        return msg;
    }

    public static String equivalenciaTipo(String tipo) {
        String result = "";
        for (int i = 0; i < eq.length; i++) {
            if (eq[i][0].equals(tipo)) {
                result = eq[i][1];
            }
        }
        return result;
    }

    public static String equivalenciaTipoDocNombre(String tipo) {
        String result = "";
        for (int i = 0; i < eq.length; i++) {
            if (eq[i][0].equals(tipo)) {
                result = eq[i][2];
            }
        }
        return result;
    }

    public String Desencriptar(String textoEncriptado) throws Exception {
        String secretKey = "imperiocore";
        String base64EncryptedString = "";
        byte message[] = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte digestOfPassword[] = md.digest(secretKey.getBytes("utf-8"));
        byte keyBytes[] = Arrays.copyOf(digestOfPassword, 24);
        javax.crypto.SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        Cipher decipher = Cipher.getInstance("DESede");
        decipher.init(2, key);
        byte plainText[] = decipher.doFinal(message);
        base64EncryptedString = new String(plainText, "UTF-8");
        return base64EncryptedString;
    }

    public String Encriptar(String texto) throws Exception {
        String secretKey = "imperiocore";
        String base64EncryptedString = "";
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte digestOfPassword[] = md.digest(secretKey.getBytes("utf-8"));
        byte keyBytes[] = Arrays.copyOf(digestOfPassword, 24);
        javax.crypto.SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(1, key);
        byte plainTextBytes[] = texto.getBytes("utf-8");
        byte buf[] = cipher.doFinal(plainTextBytes);
        byte base64Bytes[] = Base64.encodeBase64(buf);
        base64EncryptedString = new String(base64Bytes);
        return base64EncryptedString;
    }

    public static String getMailHost(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));

            msg = "" + propiedades.getProperty("mail.server.host");

        } catch (Exception ex) {
            msg = "Error en getPathFilesEnvio: " + ex.getMessage();
        }
        return msg;
    }

    public static String getMailPort(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));

            msg = "" + propiedades.getProperty("mail.server.port");

        } catch (Exception ex) {
            msg = "Error en getPathFilesEnvio: " + ex.getMessage();
        }
        return msg;
    }

    public static String getMailSsl(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));

            msg = "" + propiedades.getProperty("mail.enable.ssl");

        } catch (Exception ex) {
            msg = "Error en getPathFilesEnvio: " + ex.getMessage();
        }
        return msg;
    }

    public static String getMailAuth(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));

            msg = "" + propiedades.getProperty("mail.auth");

        } catch (Exception ex) {
            msg = "Error en getPathFilesEnvio: " + ex.getMessage();
        }
        return msg;
    }

    public static String getMailUser(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));

            msg = "" + propiedades.getProperty("mail.username");

        } catch (Exception ex) {
            msg = "Error en getPathFilesEnvio: " + ex.getMessage();
        }
        return msg;
    }

    public static String getMailPass(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));

            msg = "" + propiedades.getProperty("mail.password");

        } catch (Exception ex) {
            msg = "Error en getPathFilesEnvio: " + ex.getMessage();
        }
        return msg;
    }

    public static String getWsOpcion(String vruc) {
        String msg = "";
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream(rutaini + vruc + rutafin + "property.properties"));

            msg = "" + propiedades.getProperty("wsopcion");

        } catch (Exception ex) {
            msg = "Error en getPathFilesEnvio: " + ex.getMessage();
        }
        return msg;
    }
}
