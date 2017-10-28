package com.org.ws;

import com.org.factories.Util;
import com.org.model.beans.DocumentoCabBean;
import com.org.model.beans.DocumentodetBean;
import com.org.model.beans.Leyenda;
import com.org.util.GeneralFunctions;
import com.org.util.HeaderHandlerResolver;
import com.org.util.LecturaXML;
import java.io.BufferedOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.security.KeyStore;
import java.security.PrivateKey;

import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
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

    public static String generarXMLZipiadoBoleta(DocumentoCabBean items, List<DocumentodetBean> detdocelec, List<Leyenda> leyendas) {
        log.info("generarXMLZipiadoBoleta - Inicializamos el ambiente");
        org.apache.xml.security.Init.init();
        String resultado = "";

        String unidadEnvio; // = Util.getPathZipFilesEnvio();
        String pathXMLFile;
        try {
            //String nrodoc = iddocument;//"943317";// request.getParameter("nrodoc");
            log.info("generarXMLZipiadoBoleta - Extraemos datos para preparar XML ");
             unidadEnvio = "d:\\libre\\";
            log.info("generarXMLZipiadoBoleta - Ruta de directorios " + unidadEnvio);
            log.info("generarXMLZipiadoBoleta - Iniciamos cabecera ");
            //crear el Xml firmado
            if (items != null) {
                pathXMLFile = unidadEnvio + items.getEmpr_nroruc() + "-03-" + items.getDocu_numero() + ".xml";
                //======================crear XML =======================
                resultado = creaXml(items, detdocelec, leyendas, unidadEnvio);
                /*=======================ENVIO A SUNAT=============*/
                if (items.getDocu_enviaws().equals("S")) {
                    log.info("generarXMLZipiadoBoleta - Preparando para enviar a SUNAT");
                    resultado = enviarZipASunat(unidadEnvio, items.getEmpr_nroruc() + "-03-" + items.getDocu_numero() + ".zip", items.getEmpr_nroruc());
                } else {
                    /*este caso de boleta no se envia al sunat*/
                    log.info("generarXMLZipiadoBoleta - No se envia a SUNAT");
                    resultado = "0|El Comprobante numero " + items.getDocu_numero() + ", ha sido aceptado.";
                }

                //resultado = "termino de generar el archivo xml de la Boleta Electronica";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resultado = "0100|Error al generar el archivo de formato xml de la Boleta.";
            log.error("generarXMLZipiadoBoleta - error  " + ex.toString());
        }
//
//        try {
//            LecturaXML.guardarProcesoEstado(nrodoc, "O", resultado.split("\\|", 0), conn);
//        } catch (SQLException ex) {
//            Logger.getLogger(BolElectronica.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return resultado;
    }

    public static String enviarZipASunat(String path, String zipFileName, String vruc) {
        String resultado = "";
        String sws = "1";
        log.info("enviarASunat - Prepara ambiente: " + sws);
        try {

            javax.activation.FileDataSource fileDataSource = new javax.activation.FileDataSource(path + zipFileName);
            javax.activation.DataHandler dataHandler = new javax.activation.DataHandler(fileDataSource);
            byte[] respuestaSunat = null;
            //================Enviando a sunat
            switch (sws) {
                case "1":
                    pe.gob.sunat.servicio.registro.comppago.factura.gem.service_bta.BillService_Service_fe ws1 = new pe.gob.sunat.servicio.registro.comppago.factura.gem.service_bta.BillService_Service_fe();
                    HeaderHandlerResolver handlerResolver1 = new HeaderHandlerResolver();
                    handlerResolver1.setVruc(vruc);
                    ws1.setHandlerResolver(handlerResolver1);
                    pe.gob.sunat.servicio.registro.comppago.factura.gem.service_bta.BillService port1 = ws1.getBillServicePort();
                    respuestaSunat = port1.sendBill(zipFileName, dataHandler);
                    log.info("enviarASunat - Ambiente Beta: " + sws);
                    break;
                case "2":
                    pe.gob.sunat.servicio.registro.comppago.factura.gem.servicesqa.BillService_Service_sqa ws2 = new pe.gob.sunat.servicio.registro.comppago.factura.gem.servicesqa.BillService_Service_sqa();
                    HeaderHandlerResolver handlerResolver2 = new HeaderHandlerResolver();
                    handlerResolver2.setVruc(vruc);
                    ws2.setHandlerResolver(handlerResolver2);
                    pe.gob.sunat.servicio.registro.comppago.factura.gem.servicesqa.BillService port2 = ws2.getBillServicePort();
                    respuestaSunat = port2.sendBill(zipFileName, dataHandler);
                    log.info("enviarASunat - Ambiente QA " + sws);
                    break;
                case "3":
                    pe.gob.sunat.servicio.registro.comppago.factura.gem.service.BillService_Service_fe ws3 = new pe.gob.sunat.servicio.registro.comppago.factura.gem.service.BillService_Service_fe();
                    HeaderHandlerResolver handlerResolver3 = new HeaderHandlerResolver();
                    handlerResolver3.setVruc(vruc);
                    ws3.setHandlerResolver(handlerResolver3);
                    pe.gob.sunat.servicio.registro.comppago.factura.gem.service.BillService port3 = ws3.getBillServicePort();
                    respuestaSunat = port3.sendBill(zipFileName, dataHandler);
                    log.info("enviarASunat - Ambiente Produccion " + sws);
                    break;
            }

//            javax.activation.FileDataSource fileDataSource = new javax.activation.FileDataSource(path + zipFileName);
//            javax.activation.DataHandler dataHandler = new javax.activation.DataHandler(fileDataSource);
            //================Grabando la respuesta de sunat en archivo ZIP solo si es nulo
            String pathRecepcion = "d:\\libre\\";
            FileOutputStream fos = new FileOutputStream(pathRecepcion + "R-" + zipFileName);
            fos.write(respuestaSunat);
            fos.close();
            //================Descompremiendo el zip de Sunat
            log.info("enviarASunat - Descomprimiendo CDR " + pathRecepcion + "R-" + zipFileName);
            ZipFile archive = new ZipFile(pathRecepcion + "R-" + zipFileName);
            Enumeration e = archive.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                File file = new File(pathRecepcion, entry.getName());
                if (!file.isDirectory()) {
                    if (entry.isDirectory() && !file.exists()) {
                        file.mkdirs();
                    } else {
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        InputStream in = archive.getInputStream(entry);
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

                        byte[] buffer = new byte[8192];
                        int read;
                        while (-1 != (read = in.read(buffer))) {
                            out.write(buffer, 0, read);
                        }
                        in.close();
                        out.close();
                    }
                }
            }
            archive.close();
            //================leeyendo la resuesta de Sunat
            zipFileName = zipFileName.substring(0, zipFileName.indexOf(".zip"));
            log.info("enviarASunat - Lectura del contenido del CDR ");
            resultado = LecturaXML.getRespuestaSunat(pathRecepcion + "R-" + zipFileName + ".xml");
            System.out.println("==>El envio del Zip a sunat fue exitoso");
            log.info("enviarASunat - Envio a Sunat Exitoso ");
        } catch (javax.xml.ws.soap.SOAPFaultException ex) {
            log.error("enviarASunat - Error " + ex.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("enviarASunat - Error " + e.toString());
        }
        return resultado;
    }

    private static String creaXml(DocumentoCabBean items, List<DocumentodetBean> detdocelec, List<Leyenda> leyendas, String unidadEnvio) {
        String resultado = "";
        try {
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
            String pathXMLFile = unidadEnvio + items.getEmpr_nroruc() + "-03-" + items.getDocu_numero() + ".xml";
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
            //////////////////////////////////////////////////
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
            //doc.appendChild(doc.createComment(" Preamble "));
            doc.appendChild(envelope);
            //doc.appendChild(doc.createComment(" Postamble "));

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
            if (!items.getDocu_gravada().trim().equals("0.00")) {
                Element AdditionalMonetaryTotal1 = doc.createElementNS("", "sac:AdditionalMonetaryTotal");
                envelope.appendChild(AdditionalMonetaryTotal1);
                AdditionalMonetaryTotal1.appendChild(doc.createTextNode("\n"));

                Element ID1 = doc.createElementNS("", "cbc:ID");
                envelope.appendChild(ID1);
                ID1.appendChild(doc.createTextNode("1001"));

                Element PayableAmount1 = doc.createElementNS("", "cbc:PayableAmount");
                PayableAmount1.setAttributeNS(null, "currencyID", items.getDocu_moneda().trim());
                PayableAmount1.setIdAttributeNS(null, "currencyID", true);
                envelope.appendChild(PayableAmount1);
                PayableAmount1.appendChild(doc.createTextNode(items.getDocu_gravada().trim()));

                AdditionalInformation.appendChild(AdditionalMonetaryTotal1);
                AdditionalMonetaryTotal1.appendChild(ID1);
                AdditionalMonetaryTotal1.appendChild(PayableAmount1);
            }
            //agrupa2
            if (!items.getDocu_inafecta().trim().equals("0.00")) {
                Element AdditionalMonetaryTotal2 = doc.createElementNS("", "sac:AdditionalMonetaryTotal");
                envelope.appendChild(AdditionalMonetaryTotal2);
                AdditionalMonetaryTotal2.appendChild(doc.createTextNode("\n"));

                Element ID2 = doc.createElementNS("", "cbc:ID");
                envelope.appendChild(ID2);
                ID2.appendChild(doc.createTextNode("1002"));

                Element PayableAmount2 = doc.createElementNS("", "cbc:PayableAmount");
                PayableAmount2.setAttributeNS(null, "currencyID", items.getDocu_moneda().trim());
                PayableAmount2.setIdAttributeNS(null, "currencyID", true);
                envelope.appendChild(PayableAmount2);
                PayableAmount2.appendChild(doc.createTextNode(items.getDocu_inafecta().trim()));

                AdditionalInformation.appendChild(AdditionalMonetaryTotal2);
                AdditionalMonetaryTotal2.appendChild(ID2);
                AdditionalMonetaryTotal2.appendChild(PayableAmount2);
            }
            //agrupa3
            if (!items.getDocu_exonerada().trim().equals("0.00")) {
                Element AdditionalMonetaryTotal3 = doc.createElementNS("", "sac:AdditionalMonetaryTotal");
                envelope.appendChild(AdditionalMonetaryTotal3);
                AdditionalMonetaryTotal3.appendChild(doc.createTextNode("\n"));

                Element ID3 = doc.createElementNS("", "cbc:ID");
                envelope.appendChild(ID3);
                ID3.appendChild(doc.createTextNode("1003"));

                Element PayableAmount3 = doc.createElementNS("", "cbc:PayableAmount");
                PayableAmount3.setAttributeNS(null, "currencyID", items.getDocu_moneda().trim());
                PayableAmount3.setIdAttributeNS(null, "currencyID", true);
                envelope.appendChild(PayableAmount3);
                PayableAmount3.appendChild(doc.createTextNode(items.getDocu_exonerada().trim()));

                AdditionalInformation.appendChild(AdditionalMonetaryTotal3);
                AdditionalMonetaryTotal3.appendChild(ID3);
                AdditionalMonetaryTotal3.appendChild(PayableAmount3);
            }
            //agrupa4
            if (!items.getDocu_gratuita().trim().equals("0.00")) {
                Element AdditionalMonetaryTotal4 = doc.createElementNS("", "sac:AdditionalMonetaryTotal");
                envelope.appendChild(AdditionalMonetaryTotal4);
                AdditionalMonetaryTotal4.appendChild(doc.createTextNode("\n"));

                Element ID4 = doc.createElementNS("", "cbc:ID");
                envelope.appendChild(ID4);
                ID4.appendChild(doc.createTextNode("1004"));

                Element PayableAmount4 = doc.createElementNS("", "cbc:PayableAmount");
                PayableAmount4.setAttributeNS(null, "currencyID", items.getDocu_moneda().trim());
                PayableAmount4.setIdAttributeNS(null, "currencyID", true);
                envelope.appendChild(PayableAmount4);
                PayableAmount4.appendChild(doc.createTextNode(items.getDocu_gratuita().trim()));

                AdditionalInformation.appendChild(AdditionalMonetaryTotal4);
                AdditionalMonetaryTotal4.appendChild(ID4);
                AdditionalMonetaryTotal4.appendChild(PayableAmount4);
            }
            //agrupa5
            if (!items.getDocu_descuento().trim().equals("0.00")) {
                Element AdditionalMonetaryTotal5 = doc.createElementNS("", "sac:AdditionalMonetaryTotal");
                envelope.appendChild(AdditionalMonetaryTotal5);
                AdditionalMonetaryTotal5.appendChild(doc.createTextNode("\n"));

                Element ID10 = doc.createElementNS("", "cbc:ID");
                envelope.appendChild(ID10);
                ID10.appendChild(doc.createTextNode("2005"));

                Element PayableAmount5 = doc.createElementNS("", "cbc:PayableAmount");
                PayableAmount5.setAttributeNS(null, "currencyID", items.getDocu_moneda().trim());
                PayableAmount5.setIdAttributeNS(null, "currencyID", true);
                envelope.appendChild(PayableAmount5);
                PayableAmount5.appendChild(doc.createTextNode(items.getDocu_descuento().trim()));

                AdditionalInformation.appendChild(AdditionalMonetaryTotal5);
                AdditionalMonetaryTotal5.appendChild(ID10);
                AdditionalMonetaryTotal5.appendChild(PayableAmount5);
            }
            //leyendas
            for (Leyenda leyenda : leyendas) {

                Element AdditionalProperty = doc.createElementNS("", "sac:AdditionalProperty");
                envelope.appendChild(AdditionalProperty);
                AdditionalProperty.appendChild(doc.createTextNode("\n"));

                Element ID = doc.createElementNS("", "cbc:ID");
                envelope.appendChild(ID);
                ID.appendChild(doc.createTextNode(leyenda.getLeyendaCodigo().trim()));

                Element Value = doc.createElementNS("", "cbc:Value");
                envelope.appendChild(Value);
                cdata = doc.createCDATASection(leyenda.getLeyendaTexto().trim());
                Value.appendChild(cdata);

                AdditionalInformation.appendChild(AdditionalProperty);
                AdditionalProperty.appendChild(ID);
                AdditionalProperty.appendChild(Value);

            }

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
            ID5.appendChild(doc.createTextNode(items.getDocu_numero().trim()));

            Element IssueDate = doc.createElementNS("", "cbc:IssueDate");
            envelope.appendChild(IssueDate);
            IssueDate.appendChild(doc.createTextNode(items.getDocu_fecha().trim()));

            Element InvoiceTypeCode = doc.createElementNS("", "cbc:InvoiceTypeCode");
            envelope.appendChild(InvoiceTypeCode);
            InvoiceTypeCode.appendChild(doc.createTextNode(items.getDocu_tipodocumento().trim()));

            Element DocumentCurrencyCode = doc.createElementNS("", "cbc:DocumentCurrencyCode");
            envelope.appendChild(DocumentCurrencyCode);
            DocumentCurrencyCode.appendChild(doc.createTextNode(items.getDocu_moneda().trim()));

//bloque2 cac:Signature--------------------------------------------------------
            Element Signature = doc.createElementNS("", "cac:Signature");
            envelope.appendChild(Signature);
            Signature.appendChild(doc.createTextNode("\n"));

            Element ID6 = doc.createElementNS("", "cbc:ID");
            Signature.appendChild(ID6);
            ID6.appendChild(doc.createTextNode(items.getEmpr_nroruc().trim()));

            Element SignatoryParty = doc.createElementNS("", "cac:SignatoryParty");
            Signature.appendChild(SignatoryParty);
            SignatoryParty.appendChild(doc.createTextNode("\n"));

            Element PartyIdentification = doc.createElementNS("", "cac:PartyIdentification");
            SignatoryParty.appendChild(PartyIdentification);
            PartyIdentification.appendChild(doc.createTextNode("\n"));

            Element ID7 = doc.createElementNS("", "cbc:ID");
            PartyIdentification.appendChild(ID7);
            ID7.appendChild(doc.createTextNode(items.getEmpr_nroruc().trim()));

            Element PartyName = doc.createElementNS("", "cac:PartyName");
            SignatoryParty.appendChild(PartyName);
            PartyName.appendChild(doc.createTextNode("\n"));

            Element Name = doc.createElementNS("", "cbc:Name");
            PartyName.appendChild(Name);
            cdata = doc.createCDATASection(items.getEmpr_razonsocial().trim());
            Name.appendChild(cdata);

            Element DigitalSignatureAttachment = doc.createElementNS("", "cac:DigitalSignatureAttachment");
            Signature.appendChild(DigitalSignatureAttachment);
            DigitalSignatureAttachment.appendChild(doc.createTextNode("\n"));

            Element ExternalReference = doc.createElementNS("", "cac:ExternalReference");
            DigitalSignatureAttachment.appendChild(ExternalReference);
            ExternalReference.appendChild(doc.createTextNode("\n"));

            Element URI = doc.createElementNS("", "cbc:URI");
            ExternalReference.appendChild(URI);
            URI.appendChild(doc.createTextNode(items.getEmpr_nroruc().trim()));
//bloque3 cac:AccountingSupplierParty-----------------------------------------

            Element AccountingSupplierParty = doc.createElementNS("", "cac:AccountingSupplierParty");
            envelope.appendChild(AccountingSupplierParty);
            AccountingSupplierParty.appendChild(doc.createTextNode("\n"));

            Element CustomerAssignedAccountID = doc.createElementNS("", "cbc:CustomerAssignedAccountID");
            AccountingSupplierParty.appendChild(CustomerAssignedAccountID);
            CustomerAssignedAccountID.appendChild(doc.createTextNode(items.getEmpr_nroruc().trim()));

            Element AdditionalAccountID = doc.createElementNS("", "cbc:AdditionalAccountID");
            AccountingSupplierParty.appendChild(AdditionalAccountID);
            AdditionalAccountID.appendChild(doc.createTextNode(items.getEmpr_tipodoc().trim()));
//***********************************************************
            Element Party = doc.createElementNS("", "cac:Party");
            AccountingSupplierParty.appendChild(Party);
            Party.appendChild(doc.createTextNode("\n"));

            Element PartyName1 = doc.createElementNS("", "cac:PartyName");
            Party.appendChild(PartyName1);//se anade al grupo party
            PartyName1.appendChild(doc.createTextNode("\n"));

            Element Name2 = doc.createElementNS("", "cbc:Name");
            PartyName1.appendChild(Name2);//se anade al grupo partyname1
            cdata = doc.createCDATASection(items.getEmpr_razonsocial().trim());
            Name2.appendChild(cdata);

            Element PostalAddress = doc.createElementNS("", "cac:PostalAddress");
            Party.appendChild(PostalAddress);//se anade al grupo party
            PostalAddress.appendChild(doc.createTextNode("\n"));

            Element ID8 = doc.createElementNS("", "cbc:ID");
            PostalAddress.appendChild(ID8);//se anade al grupo PostalAddress
            ID8.appendChild(doc.createTextNode(items.getEmpr_ubigeo().trim()));

            Element StreetName = doc.createElementNS("", "cbc:StreetName");
            PostalAddress.appendChild(StreetName);//se anade al grupo PostalAddress
            cdata = doc.createCDATASection(items.getEmpr_direccion().trim());
            StreetName.appendChild(cdata);

            Element CityName = doc.createElementNS("", "cbc:CityName");
            PostalAddress.appendChild(CityName);//se anade al grupo PostalAddress
            cdata = doc.createCDATASection(items.getEmpr_provincia().trim());
            CityName.appendChild(cdata);

            Element CountrySubentity = doc.createElementNS("", "cbc:CountrySubentity");
            PostalAddress.appendChild(CountrySubentity);//se anade al grupo PostalAddress
            cdata = doc.createCDATASection(items.getEmpr_departamento().trim());
            CountrySubentity.appendChild(cdata);

            Element District = doc.createElementNS("", "cbc:District");
            PostalAddress.appendChild(District);//se anade al grupo PostalAddress
            cdata = doc.createCDATASection(items.getEmpr_distrito().trim());
            District.appendChild(cdata);

            Element Country = doc.createElementNS("", "cac:Country");
            PostalAddress.appendChild(Country);//se anade al grupo PostalAddress
            Country.appendChild(doc.createTextNode("\n"));

            Element IdentificationCode = doc.createElementNS("", "cbc:IdentificationCode");
            Country.appendChild(IdentificationCode);//se anade al grupo Country
            cdata = doc.createCDATASection(items.getEmpr_pais().trim());
            IdentificationCode.appendChild(cdata);

            Element PartyLegalEntity = doc.createElementNS("", "cac:PartyLegalEntity");
            Party.appendChild(PartyLegalEntity);//se anade al grupo party
            PartyLegalEntity.appendChild(doc.createTextNode("\n"));

            Element RegistrationName = doc.createElementNS("", "cbc:RegistrationName");
            PartyLegalEntity.appendChild(RegistrationName);//se anade al grupo Country
            cdata = doc.createCDATASection(items.getEmpr_razonsocial().trim());
            RegistrationName.appendChild(cdata);
// bloque4
            Element AccountingCustomerParty = doc.createElementNS("", "cac:AccountingCustomerParty");
            envelope.appendChild(AccountingCustomerParty);
            AccountingCustomerParty.appendChild(doc.createTextNode("\n"));

            Element CustomerAssignedAccountID1 = doc.createElementNS("", "cbc:CustomerAssignedAccountID");
            AccountingCustomerParty.appendChild(CustomerAssignedAccountID1);//se anade al grupo AccountingCustomerParty
            CustomerAssignedAccountID1.appendChild(doc.createTextNode(items.getClie_numero().trim()));

            Element AdditionalAccountID1 = doc.createElementNS("", "cbc:AdditionalAccountID");
            AccountingCustomerParty.appendChild(AdditionalAccountID1);//se anade al grupo AccountingCustomerParty
            AdditionalAccountID1.appendChild(doc.createTextNode(items.getClie_tipodoc().trim()));

            Element Party1 = doc.createElementNS("", "cac:Party");
            AccountingCustomerParty.appendChild(Party1);//se anade al grupo AccountingCustomerParty
            Party1.appendChild(doc.createTextNode("\n"));

            Element PartyLegalEntity1 = doc.createElementNS("", "cac:PartyLegalEntity");
            Party1.appendChild(PartyLegalEntity1);//se anade al grupo Party1
            PartyLegalEntity1.appendChild(doc.createTextNode("\n"));
            Element RegistrationName1 = doc.createElementNS("", "cbc:RegistrationName");
            PartyLegalEntity1.appendChild(RegistrationName1);//se anade al grupo PartyLegalEntity1
            cdata = doc.createCDATASection(items.getClie_nombre().trim());
            RegistrationName1.appendChild(cdata);

//bloque 5
            Element TaxTotal = doc.createElementNS("", "cac:TaxTotal");
            envelope.appendChild(TaxTotal);
            TaxTotal.appendChild(doc.createTextNode("\n"));

            Element TaxAmount = doc.createElementNS("", "cbc:TaxAmount");
            TaxAmount.setAttributeNS(null, "currencyID", items.getDocu_moneda().trim());
            TaxAmount.setIdAttributeNS(null, "currencyID", true);
            TaxTotal.appendChild(TaxAmount);//se anade al grupo TaxTotal
            TaxAmount.appendChild(doc.createTextNode(items.getDocu_igv().trim()));

            Element TaxSubtotal = doc.createElementNS("", "cac:TaxSubtotal");
            TaxTotal.appendChild(TaxSubtotal);//se anade al grupo TaxTotal
            TaxSubtotal.appendChild(doc.createTextNode("\n"));

            Element TaxAmount1 = doc.createElementNS("", "cbc:TaxAmount");
            TaxAmount1.setAttributeNS(null, "currencyID", items.getDocu_moneda().trim());
            TaxAmount1.setIdAttributeNS(null, "currencyID", true);
            TaxSubtotal.appendChild(TaxAmount1);//se anade al grupo TaxSubtotal
            TaxAmount1.appendChild(doc.createTextNode(items.getDocu_igv().trim()));

            Element TaxCategory = doc.createElementNS("", "cac:TaxCategory");
            TaxSubtotal.appendChild(TaxCategory);//se anade al grupo TaxSubtotal
            TaxCategory.appendChild(doc.createTextNode("\n"));

            Element TaxScheme = doc.createElementNS("", "cac:TaxScheme");
            TaxCategory.appendChild(TaxScheme);//se anade al grupo TaxCategory
            TaxScheme.appendChild(doc.createTextNode("\n"));

            Element ID9 = doc.createElementNS("", "cbc:ID");
            TaxScheme.appendChild(ID9);//se anade al grupo TaxScheme
            ID9.appendChild(doc.createTextNode("1000")); 

            Element Name3 = doc.createElementNS("", "cbc:Name");
            TaxScheme.appendChild(Name3);//se anade al grupo TaxScheme
            Name3.appendChild(doc.createTextNode("IGV"));

            Element TaxTypeCode = doc.createElementNS("", "cbc:TaxTypeCode");
            TaxScheme.appendChild(TaxTypeCode);//se anade al grupo TaxScheme
            TaxTypeCode.appendChild(doc.createTextNode("VAT"));
//bloque 6     
            Element LegalMonetaryTotal = doc.createElementNS("", "cac:LegalMonetaryTotal");
            envelope.appendChild(LegalMonetaryTotal);
            LegalMonetaryTotal.appendChild(doc.createTextNode("\n"));

            if (!items.getDocu_descuento().equals("0.00")) {
                Element AllowanceTotalAmount = doc.createElementNS("", "cbc:AllowanceTotalAmount");
                AllowanceTotalAmount.setAttributeNS(null, "currencyID", items.getDocu_moneda().trim());
                AllowanceTotalAmount.setIdAttributeNS(null, "currencyID", true);
                LegalMonetaryTotal.appendChild(AllowanceTotalAmount);//se anade al grupo LegalMonetaryTotal
                AllowanceTotalAmount.appendChild(doc.createTextNode(items.getDocu_descuento().trim()));
            }

            Element PayableAmount = doc.createElementNS("", "cbc:PayableAmount");
            PayableAmount.setAttributeNS(null, "currencyID", items.getDocu_moneda().trim());
            PayableAmount.setIdAttributeNS(null, "currencyID", true);
            LegalMonetaryTotal.appendChild(PayableAmount);//se anade al grupo LegalMonetaryTotal
            PayableAmount.appendChild(doc.createTextNode(items.getDocu_total().trim()));
//detalle factura
            log.info("generarXMLZipiadoBoleta - Iniciamos detalle XML ");
            for (DocumentodetBean listaDet : detdocelec) {
                Element InvoiceLine = doc.createElementNS("", "cac:InvoiceLine");
                envelope.appendChild(InvoiceLine);
                InvoiceLine.appendChild(doc.createTextNode("\n"));

                Element ID11 = doc.createElementNS("", "cbc:ID");
                InvoiceLine.appendChild(ID11);//se anade al grupo InvoiceLine
                ID11.appendChild(doc.createTextNode(listaDet.getItem_orden().trim()));

                Element InvoicedQuantity = doc.createElementNS("", "cbc:InvoicedQuantity");
                InvoicedQuantity.setAttributeNS(null, "unitCode", listaDet.getItem_unidad().trim());
                InvoicedQuantity.setIdAttributeNS(null, "unitCode", true);

                InvoiceLine.appendChild(InvoicedQuantity);//se anade al grupo InvoiceLine
                InvoicedQuantity.appendChild(doc.createTextNode(listaDet.getItem_cantidad().trim()));

                Element LineExtensionAmount1 = doc.createElementNS("", "cbc:LineExtensionAmount");
                LineExtensionAmount1.setAttributeNS(null, "currencyID", listaDet.getItem_moneda().trim());
                LineExtensionAmount1.setIdAttributeNS(null, "currencyID", true);

                InvoiceLine.appendChild(LineExtensionAmount1);//se anade al grupo InvoiceLine
                LineExtensionAmount1.appendChild(doc.createTextNode(listaDet.getItem_ti_subtotal().trim()));

                Element PricingReference = doc.createElementNS("", "cac:PricingReference");
                InvoiceLine.appendChild(PricingReference);//se anade al grupo InvoiceLine
                PricingReference.appendChild(doc.createTextNode("\n"));

                Element AlternativeConditionPrice = doc.createElementNS("", "cac:AlternativeConditionPrice");
                PricingReference.appendChild(AlternativeConditionPrice);//se anade al grupo PricingReference
                AlternativeConditionPrice.appendChild(doc.createTextNode("\n"));

                Element PriceAmount = doc.createElementNS("", "cbc:PriceAmount");
                PriceAmount.setAttributeNS(null, "currencyID", listaDet.getItem_moneda().trim());
                PriceAmount.setIdAttributeNS(null, "currencyID", true);
                AlternativeConditionPrice.appendChild(PriceAmount);//se anade al grupo AlternativeConditionPrice
                PriceAmount.appendChild(doc.createTextNode(listaDet.getItem_pventa().trim()));

                Element PriceTypeCode = doc.createElementNS("", "cbc:PriceTypeCode");
                AlternativeConditionPrice.appendChild(PriceTypeCode);//se anade al grupo AlternativeConditionPrice
                PriceTypeCode.appendChild(doc.createTextNode("01")); //=================================>Faltaba especificar ite

//                  
                if (!listaDet.getItem_pventa_no_onerosa().equals("0.00")) {
                    Element AlternativeConditionPrice02 = doc.createElementNS("", "cac:AlternativeConditionPrice");
                    PricingReference.appendChild(AlternativeConditionPrice02);//se anade al grupo PricingReference
                    AlternativeConditionPrice02.appendChild(doc.createTextNode("\n"));

                    Element PriceAmount02 = doc.createElementNS("", "cbc:PriceAmount");
                    PriceAmount02.setAttributeNS(null, "currencyID", listaDet.getItem_moneda().trim());
                    PriceAmount02.setIdAttributeNS(null, "currencyID", true);
                    AlternativeConditionPrice02.appendChild(PriceAmount02);//se anade al grupo AlternativeConditionPrice
                    PriceAmount02.appendChild(doc.createTextNode(listaDet.getItem_pventa_no_onerosa().trim()));

                    Element PriceTypeCode02 = doc.createElementNS("", "cbc:PriceTypeCode");
                    AlternativeConditionPrice02.appendChild(PriceTypeCode02);//se anade al grupo AlternativeConditionPrice
                    PriceTypeCode02.appendChild(doc.createTextNode("02")); //==>Para los casos de gatuito venta no Onerosa
                }

//
                Element TaxTotal1 = doc.createElementNS("", "cac:TaxTotal");
                InvoiceLine.appendChild(TaxTotal1);//se anade al grupo InvoiceLine
                TaxTotal1.appendChild(doc.createTextNode("\n"));

                Element TaxAmount2 = doc.createElementNS("", "cbc:TaxAmount");
                TaxAmount2.setAttributeNS(null, "currencyID", listaDet.getItem_moneda().trim());
                TaxAmount2.setIdAttributeNS(null, "currencyID", true);
                TaxTotal1.appendChild(TaxAmount2);//se anade al grupo TaxTotal1
                TaxAmount2.appendChild(doc.createTextNode(listaDet.getItem_ti_igv().trim()));

                Element TaxSubtotal1 = doc.createElementNS("", "cac:TaxSubtotal");
                TaxTotal1.appendChild(TaxSubtotal1);//se anade al grupo TaxTotal1
                TaxSubtotal1.appendChild(doc.createTextNode("\n"));

                Element TaxableAmount = doc.createElementNS("", "cbc:TaxableAmount");
                TaxableAmount.setAttributeNS(null, "currencyID", listaDet.getItem_moneda().trim());
                TaxableAmount.setIdAttributeNS(null, "currencyID", true);

                TaxSubtotal1.appendChild(TaxableAmount);//se anade al grupo TaxSubtotal1
                TaxableAmount.appendChild(doc.createTextNode(listaDet.getItem_ti_igv().trim()));

                Element TaxAmount3 = doc.createElementNS("", "cbc:TaxAmount");
                TaxAmount3.setAttributeNS(null, "currencyID", listaDet.getItem_moneda().trim()); //================>errror estaba con item..getItem_moneda()
                TaxAmount3.setIdAttributeNS(null, "currencyID", true);
                TaxSubtotal1.appendChild(TaxAmount3);//se anade al grupo TaxSubtotal1
                TaxAmount3.appendChild(doc.createTextNode(listaDet.getItem_ti_igv().trim()));

                Element Percent = doc.createElementNS("", "cbc:Percent");
                TaxSubtotal1.appendChild(Percent);//se anade al grupo TaxSubtotal1
                Percent.appendChild(doc.createTextNode("0.0"));

                Element TaxCategory1 = doc.createElementNS("", "cac:TaxCategory");
                TaxSubtotal1.appendChild(TaxCategory1);//se anade al grupo TaxSubtotal1
                TaxCategory1.appendChild(doc.createTextNode("\n"));

                Element ID12 = doc.createElementNS("", "cbc:ID");
                TaxCategory1.appendChild(ID12);//se anade al grupo TaxCategory1
                ID12.appendChild(doc.createTextNode("VAT"));

                Element TaxExemptionReasonCode = doc.createElementNS("", "cbc:TaxExemptionReasonCode");
                TaxCategory1.appendChild(TaxExemptionReasonCode);//se anade al grupo TaxCategory1
                TaxExemptionReasonCode.appendChild(doc.createTextNode(listaDet.getItem_afectacion().trim()));

                Element TierRange = doc.createElementNS("", "cbc:TierRange");
                TaxCategory1.appendChild(TierRange);//se anade al grupo TaxCategory1
                TierRange.appendChild(doc.createTextNode("00"));

                Element TaxScheme1 = doc.createElementNS("", "cac:TaxScheme");
                TaxCategory1.appendChild(TaxScheme1);//se anade al grupo TaxCategory1
                TaxScheme1.appendChild(doc.createTextNode("\n"));

                Element ID15 = doc.createElementNS("", "cbc:ID");
                TaxScheme1.appendChild(ID15);//se anade al grupo TaxCategory1
                ID15.appendChild(doc.createTextNode("1000"));

                Element Name9 = doc.createElementNS("", "cbc:Name");
                TaxScheme1.appendChild(Name9);//se anade al grupo TaxCategory1
                Name9.appendChild(doc.createTextNode("IGV"));

                Element TaxTypeCode1 = doc.createElementNS("", "cbc:TaxTypeCode");
                TaxScheme1.appendChild(TaxTypeCode1);//se anade al grupo TaxCategory1
                TaxTypeCode1.appendChild(doc.createTextNode("VAT"));

                Element Item = doc.createElementNS("", "cac:Item");
                InvoiceLine.appendChild(Item);//se anade al grupo InvoiceLine
                Item.appendChild(doc.createTextNode("\n"));

                Element Description = doc.createElementNS("", "cbc:Description");
                Item.appendChild(Description);//se anade al grupo Item
                cdata = doc.createCDATASection(listaDet.getItem_descripcion().trim());
                Description.appendChild(cdata);

                Element SellersItemIdentification = doc.createElementNS("", "cac:SellersItemIdentification");
                Item.appendChild(SellersItemIdentification);//se anade al grupo Item
                SellersItemIdentification.appendChild(doc.createTextNode("\n"));

                Element ID18 = doc.createElementNS("", "cbc:ID");
                SellersItemIdentification.appendChild(ID18);//se anade al grupo Item
                ID18.appendChild(doc.createTextNode(listaDet.getItem_codproducto().trim()));

                Element Price = doc.createElementNS("", "cac:Price");
                InvoiceLine.appendChild(Price);//se anade al grupo InvoiceLine
                Price.appendChild(doc.createTextNode("\n"));

                Element PriceAmount2 = doc.createElementNS("", "cbc:PriceAmount");
                PriceAmount2.setAttributeNS(null, "currencyID", listaDet.getItem_moneda().trim());
                PriceAmount2.setIdAttributeNS(null, "currencyID", true);
                Price.appendChild(PriceAmount2);//se anade al grupo Price
                PriceAmount2.appendChild(doc.createTextNode(listaDet.getItem_pventa().trim()));
            }
            log.info("generarXMLZipiadoBoleta - Prepara firma digital ");
            sig.setId(items.getEmpr_nroruc());
            sig.addKeyInfo(cert);
            {
                Transforms transforms = new Transforms(doc);
                transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
                sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
            }
            {
                //Firmar el documento
                log.info("generarXMLZipiadoBoleta - firma el XML ");
                sig.sign(privateKey);
            }
            //--------------------fin de construccion del xml---------------------
            ///*combinacion de firma y construccion xml////
            FileOutputStream f = new FileOutputStream(signatureFile);
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            //tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty(OutputKeys.STANDALONE, "no");
            //Writer out = new StringWriter();
            StreamResult sr = new StreamResult(f);
            tf.transform(new DOMSource(doc), sr);
            sr.getOutputStream().close();

            log.info("generarXMLZipiadoBoleta - XML creado " + pathXMLFile);
            //====================== CREAR ZIP PARA EL ENVIO A SUNAT =======================
            resultado = GeneralFunctions.crearZip(items, unidadEnvio, signatureFile);

        } catch (Exception ex) {
            ex.printStackTrace();
            resultado = "0100|Error al generar el archivo de formato xml de la Factura.";
            log.error("generarXMLZipiadoFactura - error  " + ex.toString());

        }
        return resultado;
    }

}
