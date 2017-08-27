package com.org.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.ElementProxy;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;

public class BolElectronica {

    private static Log log = LogFactory.getLog(BolElectronica.class);

    public static String generarXMLZipiadoBoleta() {
        log.info("generarXMLZipiadoBoleta - Inicializamos el ambiente");
        org.apache.xml.security.Init.init();
        String resultado = "";
        String unidadEnvio; 
        String pathXMLFile;
        try {
            log.info("generarXMLZipiadoBoleta - Extraemos datos para preparar XML ");
            unidadEnvio = "d:\\libre\\";
            log.info("generarXMLZipiadoBoleta - Ruita de directorios " + unidadEnvio);

            log.info("generarXMLZipiadoBoleta - Iniciamos cabecera ");

            pathXMLFile = "d:\\libre\\20601491193-03-B001-88.xml";
            ElementProxy.setDefaultPrefix(Constants.SignatureSpecNS, "ds");
            //Parametros del keystore
            String keystoreType = "JKS";
            String keystoreFile = "d:\\libre\\MiAlmacen.jks";
            String keystorePass = "miAlmacen";
            String privateKeyAlias = "miAlmacen";
            String privateKeyPass = "miAlmacen";
            String certificateAlias = "miAlmacen";
            
            log.info("generarXMLZipiadoBoleta - Lectura de cerificado ");
            CDATASection cdata;
            log.info("generarXMLZipiadoBoleta - Iniciamos la generacion del XML");
            File signatureFile = new File(pathXMLFile);
            ///////////////////Creación del certificado//////////////////////////////
            KeyStore ks = KeyStore.getInstance(keystoreType);
            FileInputStream fis = new FileInputStream(keystoreFile);
            ks.load(fis, keystorePass.toCharArray());
            //obtener la clave privada para firmar
            PrivateKey privateKey = (PrivateKey) ks.getKey(privateKeyAlias, privateKeyPass.toCharArray());
            if (privateKey == null) {
                throw new RuntimeException("Private key is null");
            }
            X509Certificate cert = (X509Certificate) ks.getCertificate(certificateAlias);

            javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            //Firma XML genera espacio para los nombres o tag
            dbf.setNamespaceAware(true);
            javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.newDocument();
            ////////////////////////////////////////////////// 

            log.info("generarXMLZipiadoBoleta - cabecera XML ");
            Element envelope = doc.createElementNS("", "Invoice");
            envelope.setAttributeNS(Constants.NamespaceSpecNS, "xmlns", "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2");
            envelope.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:cac", "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2");
            envelope.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:cbc", "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
            envelope.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:ccts", "urn:un:unece:uncefact:documentation:2");
            envelope.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
            envelope.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:ext", "urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2");
            envelope.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:qdt", "urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2");
            envelope.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:sac", "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1");
            envelope.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:udt", "urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2");
            envelope.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            envelope.appendChild(doc.createTextNode("\n"));
            doc.appendChild(envelope);

            Element UBLExtensions = doc.createElementNS("", "ext:UBLExtensions");
            envelope.appendChild(UBLExtensions);
            Element UBLExtension2 = doc.createElementNS("", "ext:UBLExtension");
            UBLExtension2.appendChild(doc.createTextNode("\n"));
            Element ExtensionContent2 = doc.createElementNS("", "ext:ExtensionContent");
            ExtensionContent2.appendChild(doc.createTextNode("\n"));
            //2do grupo
            Element UBLExtension = doc.createElementNS("", "ext:UBLExtension");
            envelope.appendChild(UBLExtension);
            Element ExtensionContent = doc.createElementNS("", "ext:ExtensionContent");
            envelope.appendChild(ExtensionContent);

            Element AdditionalInformation = doc.createElementNS("", "sac:AdditionalInformation");
            envelope.appendChild(AdditionalInformation);
            AdditionalInformation.appendChild(doc.createTextNode("\n"));
            //agrupa1
            //if (!items.getDocu_gravada().trim().equals("0.00")) {
            //grabado
            Element AdditionalMonetaryTotal1 = doc.createElementNS("", "sac:AdditionalMonetaryTotal");
            envelope.appendChild(AdditionalMonetaryTotal1);
            AdditionalMonetaryTotal1.appendChild(doc.createTextNode("\n"));

            Element ID1 = doc.createElementNS("", "cbc:ID");
            envelope.appendChild(ID1);
            ID1.appendChild(doc.createTextNode("1001"));

            Element PayableAmount1 = doc.createElementNS("", "cbc:PayableAmount");
            PayableAmount1.setAttributeNS(null, "currencyID", "PEN");
            PayableAmount1.setIdAttributeNS(null, "currencyID", true);
            envelope.appendChild(PayableAmount1);
            PayableAmount1.appendChild(doc.createTextNode("30782.55"));

            AdditionalInformation.appendChild(AdditionalMonetaryTotal1);
            AdditionalMonetaryTotal1.appendChild(ID1);
            AdditionalMonetaryTotal1.appendChild(PayableAmount1);


            Element AdditionalProperty = doc.createElementNS("", "sac:AdditionalProperty");
            envelope.appendChild(AdditionalProperty);
            AdditionalProperty.appendChild(doc.createTextNode("\n"));

            Element ID = doc.createElementNS("", "cbc:ID");
            envelope.appendChild(ID);
            ID.appendChild(doc.createTextNode("1000"));

            Element Value = doc.createElementNS("", "cbc:Value");
            envelope.appendChild(Value);
            cdata = doc.createCDATASection("TREINTISEIS MIL TRESCIENTOS VEINTITRES Y 41/100 SOLES");
            Value.appendChild(cdata);

            AdditionalInformation.appendChild(AdditionalProperty);
            AdditionalProperty.appendChild(ID);
            AdditionalProperty.appendChild(Value);
            
            //El baseURI es la URI que se utiliza para anteponer a URIs relativos
            String BaseURI = signatureFile.toURI().toURL().toString();
            //Crea un XML Signature objeto desde el documento, BaseURI and signature algorithm (in this case RSA)
            //XMLSignature sig = new XMLSignature(doc, BaseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA); Cadena URI que se ajusta a la sintaxis URI y representa el archivo XML de entrada
            XMLSignature sig = new XMLSignature(doc, BaseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA);

            ExtensionContent.appendChild(sig.getElement());
            UBLExtension.appendChild(ExtensionContent);
            UBLExtensions.appendChild(UBLExtension);
            UBLExtensions.appendChild(UBLExtension2);
            UBLExtension2.appendChild(ExtensionContent2);
            ExtensionContent2.appendChild(AdditionalInformation);

//bloque1
            Element UBLVersionID = doc.createElementNS("", "cbc:UBLVersionID");
            envelope.appendChild(UBLVersionID);
            UBLVersionID.appendChild(doc.createTextNode("2.0"));

            Element CustomizationID = doc.createElementNS("", "cbc:CustomizationID");
            envelope.appendChild(CustomizationID);
            CustomizationID.appendChild(doc.createTextNode("1.0"));

            Element ID5 = doc.createElementNS("", "cbc:ID");
            envelope.appendChild(ID5);
            ID5.appendChild(doc.createTextNode("B001-88"));

            Element IssueDate = doc.createElementNS("", "cbc:IssueDate");
            envelope.appendChild(IssueDate);
            IssueDate.appendChild(doc.createTextNode("2017-08-27"));

            Element InvoiceTypeCode = doc.createElementNS("", "cbc:InvoiceTypeCode");
            envelope.appendChild(InvoiceTypeCode);
            InvoiceTypeCode.appendChild(doc.createTextNode("03"));

            Element DocumentCurrencyCode = doc.createElementNS("", "cbc:DocumentCurrencyCode");
            envelope.appendChild(DocumentCurrencyCode);
            DocumentCurrencyCode.appendChild(doc.createTextNode("PEN"));
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
            resultado = "0100|Error al generar el archivo de formato xml de la Boleta.";
            log.error("generarXMLZipiadoBoleta - error  " + ex.toString());
        }
        return resultado;
    }
}
