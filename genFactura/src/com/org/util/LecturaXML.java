/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.util;

/**
 *
 * @author oswaldo
 */
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LecturaXML {

    private static Log log = LogFactory.getLog(LecturaXML.class);

    public static String getRespuestaSunat(String path) {
        String respuesta = null;
        String nota = "";
//        Connection conn = null;

        try {
            log.info("LecturaXML.getRespuestaSunat - iniciamos Lectura del contenido del CDR " + path);
            DocumentBuilderFactory fabricaCreadorDocumento = DocumentBuilderFactory.newInstance();
            DocumentBuilder creadorDocumento = fabricaCreadorDocumento.newDocumentBuilder();
            Document documento = creadorDocumento.parse(path);
            //Obtener el elemento raíz del documento
            Element raiz = documento.getDocumentElement();

            //Obtener la lista de nodos que tienen etiqueta "ds:Reference"
            NodeList responsecode = raiz.getElementsByTagName("cbc:ResponseCode");
            for (int i = 0; i < responsecode.getLength(); i++) {
                Node empleado = responsecode.item(i);
                Node datoContenido = empleado.getFirstChild();
                respuesta = datoContenido.getNodeValue();
            }
            NodeList nodesc = raiz.getElementsByTagName("cbc:Description");
            for (int i = 0; i < nodesc.getLength(); i++) {
                Node empleado = nodesc.item(i);
                Node datoContenido = empleado.getFirstChild();
                respuesta = respuesta + "|" + datoContenido.getNodeValue();
            }
            NodeList note = raiz.getElementsByTagName("cbc:Note");
            for (int i = 0; i < note.getLength(); i++) {
                Node empleado = note.item(i);
                Node datoContenido = empleado.getFirstChild();
                nota = nota + datoContenido.getNodeValue() + "\\n";
            }

            String[] cdr = respuesta.split("\\|", 0);
            //=== Guardar el link

        } catch (org.xml.sax.SAXException ex) {
            System.out.println("ERROR: El formato XML del fichero no es correcto\n" + ex.getMessage());
            ex.printStackTrace();
            log.error("LecturaXML.getRespuestaSunat - Error : " + ex.toString());
            Logger.getLogger(LecturaXML.class.getName()).log(Level.SEVERE, null, ex);
            respuesta = "Error al leer el archivo de respuesta";
        } catch (IOException ex) {
            System.out.println("Error al leer el archivo de respuesta\n" + ex.getMessage());
            ex.printStackTrace();
            log.error("LecturaXML.getRespuestaSunat - Error : " + ex.toString());
            respuesta = "Error al leer el archivo de respuesta";
            Logger.getLogger(LecturaXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            System.out.println("ERROR: No se ha podido crear el generador de documentos XML\n" + ex.getMessage());
            ex.printStackTrace();
            log.error("LecturaXML.getRespuestaSunat - Error : " + ex.toString());
            respuesta = "Error al leer el archivo de respuesta";
            Logger.getLogger(LecturaXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            log.error("LecturaXML.getRespuestaSunat - Error : " + ex.toString());
            Logger.getLogger(LecturaXML.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
//            ConnectionPool.closeConexion(conn);
        }
        return respuesta;
    }

    public static String obtenerDigestValue(String path) {
        String firma = null;
        try {
            DocumentBuilderFactory fabricaCreadorDocumento = DocumentBuilderFactory.newInstance();
            DocumentBuilder creadorDocumento = fabricaCreadorDocumento.newDocumentBuilder();
            Document documento = creadorDocumento.parse(path);
            //Obtener el elemento raíz del documento
            Element raiz = documento.getDocumentElement();
            //Obtener la lista de nodos que tienen etiqueta "ds:Reference"
            NodeList listaEmpleados = raiz.getElementsByTagName("ds:Reference");
            //Recorrer la lista de empleados
            for (int i = 0; i < listaEmpleados.getLength(); i++) {
                //Obtener de la lista un empleado tras otro
                Node empleado = listaEmpleados.item(i);
                //Obtener la lista de los datos que contiene ese ds:Reference
                NodeList datosEmpleado = empleado.getChildNodes();
                //Recorrer la lista de los datos que contiene el ds:Reference
                for (int j = 0; j < datosEmpleado.getLength(); j++) {
                    //Obtener de la lista de datos un dato tras otro
                    Node dato = datosEmpleado.item(j);
                    //Comprobar que el dato se trata de un nodo de tipo Element
                    if (dato.getNodeType() == Node.ELEMENT_NODE) {
                        //Mostrar el nombre del tipo de dato
                        if (dato.getNodeName() == "ds:DigestValue") {
                            //El valor está contenido en un hijo del nodo Element
                            Node datoContenido = dato.getFirstChild();
                            //Mostrar el valor contenido en el nodo que debe ser de tipo Text
                            if (datoContenido != null && datoContenido.getNodeType() == Node.TEXT_NODE) {
                                //System.out.println(datoContenido.getNodeValue());
                                if (datoContenido.getNodeValue() != null) {
                                    firma = datoContenido.getNodeValue();
                                }
                            }
                        }
                    }
                }
            }

        } catch (org.xml.sax.SAXException ex) {
            System.out.println("ERROR: El formato XML del fichero no es correcto\n" + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("ERROR: Se ha producido un error al leer el fichero\n" + ex.getMessage());
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            System.out.println("ERROR: No se ha podido crear el generador de documentos XML\n" + ex.getMessage());
            ex.printStackTrace();
        }
        return firma;
    }


}
