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
            ID5.appendChild(doc.createTextNode("B001-1"));

            Element IssueDate = doc.createElementNS("", "cbc:IssueDate");
            envelope.appendChild(IssueDate);
            IssueDate.appendChild(doc.createTextNode("2017-10-15"));

            Element InvoiceTypeCode = doc.createElementNS("", "cbc:InvoiceTypeCode");
            envelope.appendChild(InvoiceTypeCode);
            InvoiceTypeCode.appendChild(doc.createTextNode("03"));

            Element DocumentCurrencyCode = doc.createElementNS("", "cbc:DocumentCurrencyCode");
            envelope.appendChild(DocumentCurrencyCode);
            DocumentCurrencyCode.appendChild(doc.createTextNode("PEN"));

//bloque2 cac:Signature--------------------------------------------------------
            Element Signature = doc.createElementNS("", "cac:Signature");
            envelope.appendChild(Signature);
            Signature.appendChild(doc.createTextNode("\n"));

            Element ID6 = doc.createElementNS("", "cbc:ID");
            Signature.appendChild(ID6);
            ID6.appendChild(doc.createTextNode("20601491193"));

            Element SignatoryParty = doc.createElementNS("", "cac:SignatoryParty");
            Signature.appendChild(SignatoryParty);
            SignatoryParty.appendChild(doc.createTextNode("\n"));

            Element PartyIdentification = doc.createElementNS("", "cac:PartyIdentification");
            SignatoryParty.appendChild(PartyIdentification);
            PartyIdentification.appendChild(doc.createTextNode("\n"));

            Element ID7 = doc.createElementNS("", "cbc:ID");
            PartyIdentification.appendChild(ID7);
            ID7.appendChild(doc.createTextNode("20601491193"));

            Element PartyName = doc.createElementNS("", "cac:PartyName");
            SignatoryParty.appendChild(PartyName);
            PartyName.appendChild(doc.createTextNode("\n"));

            Element Name = doc.createElementNS("", "cbc:Name");
            PartyName.appendChild(Name);
            cdata = doc.createCDATASection("IMPERIO CORE  S.A.C.");
            Name.appendChild(cdata);

            Element DigitalSignatureAttachment = doc.createElementNS("", "cac:DigitalSignatureAttachment");
            Signature.appendChild(DigitalSignatureAttachment);
            DigitalSignatureAttachment.appendChild(doc.createTextNode("\n"));

            Element ExternalReference = doc.createElementNS("", "cac:ExternalReference");
            DigitalSignatureAttachment.appendChild(ExternalReference);
            ExternalReference.appendChild(doc.createTextNode("\n"));

            Element URI = doc.createElementNS("", "cbc:URI");
            ExternalReference.appendChild(URI);
            URI.appendChild(doc.createTextNode("20601491193"));
            
//bloque3 cac:AccountingSupplierParty-----------------------------------------

            Element AccountingSupplierParty = doc.createElementNS("", "cac:AccountingSupplierParty");
            envelope.appendChild(AccountingSupplierParty);
            AccountingSupplierParty.appendChild(doc.createTextNode("\n"));

            Element CustomerAssignedAccountID = doc.createElementNS("", "cbc:CustomerAssignedAccountID");
            AccountingSupplierParty.appendChild(CustomerAssignedAccountID);
            CustomerAssignedAccountID.appendChild(doc.createTextNode("20601491193"));

            Element AdditionalAccountID = doc.createElementNS("", "cbc:AdditionalAccountID");
            AccountingSupplierParty.appendChild(AdditionalAccountID);
            AdditionalAccountID.appendChild(doc.createTextNode("6"));
//***********************************************************
            Element Party = doc.createElementNS("", "cac:Party");
            AccountingSupplierParty.appendChild(Party);
            Party.appendChild(doc.createTextNode("\n"));

            Element PartyName1 = doc.createElementNS("", "cac:PartyName");
            Party.appendChild(PartyName1);//se anade al grupo party
            PartyName1.appendChild(doc.createTextNode("\n"));

            Element Name2 = doc.createElementNS("", "cbc:Name");
            PartyName1.appendChild(Name2);//se anade al grupo partyname1
            cdata = doc.createCDATASection("IMPERIO CORE S.A.C.");
            Name2.appendChild(cdata);

            Element PostalAddress = doc.createElementNS("", "cac:PostalAddress");
            Party.appendChild(PostalAddress);//se anade al grupo party
            PostalAddress.appendChild(doc.createTextNode("\n"));

            Element ID8 = doc.createElementNS("", "cbc:ID");
            PostalAddress.appendChild(ID8);//se anade al grupo PostalAddress
            ID8.appendChild(doc.createTextNode("150128"));

            Element StreetName = doc.createElementNS("", "cbc:StreetName");
            PostalAddress.appendChild(StreetName);//se anade al grupo PostalAddress
            cdata = doc.createCDATASection("AV. EL NARANJAL NRO. 1640 URB. LOS NARANJOS LIMA - LIMA - LOS OLIVOS");
            StreetName.appendChild(cdata);

            Element CityName = doc.createElementNS("", "cbc:CityName");
            PostalAddress.appendChild(CityName);//se anade al grupo PostalAddress
            cdata = doc.createCDATASection("LIMA");
            CityName.appendChild(cdata);

            Element CountrySubentity = doc.createElementNS("", "cbc:CountrySubentity");
            PostalAddress.appendChild(CountrySubentity);//se anade al grupo PostalAddress
            cdata = doc.createCDATASection("LIMA");
            CountrySubentity.appendChild(cdata);

            Element District = doc.createElementNS("", "cbc:District");
            PostalAddress.appendChild(District);//se anade al grupo PostalAddress
            cdata = doc.createCDATASection("LOS OLIVOS");
            District.appendChild(cdata);

            Element Country = doc.createElementNS("", "cac:Country");
            PostalAddress.appendChild(Country);//se anade al grupo PostalAddress
            Country.appendChild(doc.createTextNode("\n"));

            Element IdentificationCode = doc.createElementNS("", "cbc:IdentificationCode");
            Country.appendChild(IdentificationCode);//se anade al grupo Country
            cdata = doc.createCDATASection("PE");
            IdentificationCode.appendChild(cdata);

            Element PartyLegalEntity = doc.createElementNS("", "cac:PartyLegalEntity");
            Party.appendChild(PartyLegalEntity);//se anade al grupo party
            PartyLegalEntity.appendChild(doc.createTextNode("\n"));

            Element RegistrationName = doc.createElementNS("", "cbc:RegistrationName");
            PartyLegalEntity.appendChild(RegistrationName);//se anade al grupo Country
            cdata = doc.createCDATASection("IMPERIO CORE S.A.C.");
            RegistrationName.appendChild(cdata);

// bloque4
            Element AccountingCustomerParty = doc.createElementNS("", "cac:AccountingCustomerParty");
            envelope.appendChild(AccountingCustomerParty);
            AccountingCustomerParty.appendChild(doc.createTextNode("\n"));

            Element CustomerAssignedAccountID1 = doc.createElementNS("", "cbc:CustomerAssignedAccountID");
            AccountingCustomerParty.appendChild(CustomerAssignedAccountID1);//se anade al grupo AccountingCustomerParty
            CustomerAssignedAccountID1.appendChild(doc.createTextNode("12345678"));

            Element AdditionalAccountID1 = doc.createElementNS("", "cbc:AdditionalAccountID");
            AccountingCustomerParty.appendChild(AdditionalAccountID1);//se anade al grupo AccountingCustomerParty
            AdditionalAccountID1.appendChild(doc.createTextNode("1"));

            Element Party1 = doc.createElementNS("", "cac:Party");
            AccountingCustomerParty.appendChild(Party1);//se anade al grupo AccountingCustomerParty
            Party1.appendChild(doc.createTextNode("\n"));

            Element PartyLegalEntity1 = doc.createElementNS("", "cac:PartyLegalEntity");
            Party1.appendChild(PartyLegalEntity1);//se anade al grupo Party1
            PartyLegalEntity1.appendChild(doc.createTextNode("\n"));
            Element RegistrationName1 = doc.createElementNS("", "cbc:RegistrationName");
            PartyLegalEntity1.appendChild(RegistrationName1);//se anade al grupo PartyLegalEntity1
            cdata = doc.createCDATASection("JOSE MANUEL ODRIA");
            RegistrationName1.appendChild(cdata);
            
//bloque 5
            Element TaxTotal = doc.createElementNS("", "cac:TaxTotal");
            envelope.appendChild(TaxTotal);
            TaxTotal.appendChild(doc.createTextNode("\n"));

            Element TaxAmount = doc.createElementNS("", "cbc:TaxAmount");
            TaxAmount.setAttributeNS(null, "currencyID", "PEN"); //USD
            TaxAmount.setIdAttributeNS(null, "currencyID", true);
            TaxTotal.appendChild(TaxAmount);//se anade al grupo TaxTotal
            TaxAmount.appendChild(doc.createTextNode("5540.86"));

            Element TaxSubtotal = doc.createElementNS("", "cac:TaxSubtotal");
            TaxTotal.appendChild(TaxSubtotal);//se anade al grupo TaxTotal
            TaxSubtotal.appendChild(doc.createTextNode("\n"));

            Element TaxAmount1 = doc.createElementNS("", "cbc:TaxAmount");
            TaxAmount1.setAttributeNS(null, "currencyID", "PEN");
            TaxAmount1.setIdAttributeNS(null, "currencyID", true);
            TaxSubtotal.appendChild(TaxAmount1);//se anade al grupo TaxSubtotal
            TaxAmount1.appendChild(doc.createTextNode("5540.86"));

            Element TaxCategory = doc.createElementNS("", "cac:TaxCategory");
            TaxSubtotal.appendChild(TaxCategory);//se anade al grupo TaxSubtotal
            TaxCategory.appendChild(doc.createTextNode("\n"));

            Element TaxScheme = doc.createElementNS("", "cac:TaxScheme");
            TaxCategory.appendChild(TaxScheme);//se anade al grupo TaxCategory
            TaxScheme.appendChild(doc.createTextNode("\n"));

            Element ID9 = doc.createElementNS("", "cbc:ID");
            TaxScheme.appendChild(ID9);//se anade al grupo TaxScheme
            ID9.appendChild(doc.createTextNode("1000")); ///================================faltaba poner 1000

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

            Element PayableAmount = doc.createElementNS("", "cbc:PayableAmount");
            PayableAmount.setAttributeNS(null, "currencyID", "PEN");
            PayableAmount.setIdAttributeNS(null, "currencyID", true);
            LegalMonetaryTotal.appendChild(PayableAmount);//se anade al grupo LegalMonetaryTotal
            PayableAmount.appendChild(doc.createTextNode("36323.41"));

//detalle factura
            log.info("generarXMLZipiadoBoleta - Iniciamos detalle XML ");
            //for (DocumentoBean listaDet : detdocelec) {
            Element InvoiceLine = doc.createElementNS("", "cac:InvoiceLine");
            envelope.appendChild(InvoiceLine);
            InvoiceLine.appendChild(doc.createTextNode("\n"));

            Element ID11 = doc.createElementNS("", "cbc:ID");
            InvoiceLine.appendChild(ID11);//se anade al grupo InvoiceLine
            ID11.appendChild(doc.createTextNode("1"));

            Element InvoicedQuantity = doc.createElementNS("", "cbc:InvoicedQuantity");
            InvoicedQuantity.setAttributeNS(null, "unitCode", "NIU");
            InvoicedQuantity.setIdAttributeNS(null, "unitCode", true);

            InvoiceLine.appendChild(InvoicedQuantity);//se anade al grupo InvoiceLine
            InvoicedQuantity.appendChild(doc.createTextNode("1"));

            Element LineExtensionAmount1 = doc.createElementNS("", "cbc:LineExtensionAmount");
            LineExtensionAmount1.setAttributeNS(null, "currencyID", "PEN");
            LineExtensionAmount1.setIdAttributeNS(null, "currencyID", true);

            InvoiceLine.appendChild(LineExtensionAmount1);//se anade al grupo InvoiceLine
            LineExtensionAmount1.appendChild(doc.createTextNode("30.51"));

            Element PricingReference = doc.createElementNS("", "cac:PricingReference");
            InvoiceLine.appendChild(PricingReference);//se anade al grupo InvoiceLine
            PricingReference.appendChild(doc.createTextNode("\n"));

            Element AlternativeConditionPrice = doc.createElementNS("", "cac:AlternativeConditionPrice");
            PricingReference.appendChild(AlternativeConditionPrice);//se anade al grupo PricingReference
            AlternativeConditionPrice.appendChild(doc.createTextNode("\n"));

            Element PriceAmount = doc.createElementNS("", "cbc:PriceAmount");
            PriceAmount.setAttributeNS(null, "currencyID", "PEN");
            PriceAmount.setIdAttributeNS(null, "currencyID", true);
            AlternativeConditionPrice.appendChild(PriceAmount);//se anade al grupo AlternativeConditionPrice
            PriceAmount.appendChild(doc.createTextNode("30.51"));

            Element PriceTypeCode = doc.createElementNS("", "cbc:PriceTypeCode");
            AlternativeConditionPrice.appendChild(PriceTypeCode);//se anade al grupo AlternativeConditionPrice
            PriceTypeCode.appendChild(doc.createTextNode("01")); //=================================>Faltaba especificar ite

            Element TaxTotal1 = doc.createElementNS("", "cac:TaxTotal");
            InvoiceLine.appendChild(TaxTotal1);//se anade al grupo InvoiceLine
            TaxTotal1.appendChild(doc.createTextNode("\n"));

            Element TaxAmount2 = doc.createElementNS("", "cbc:TaxAmount");
            TaxAmount2.setAttributeNS(null, "currencyID", "PEN");
            TaxAmount2.setIdAttributeNS(null, "currencyID", true);
            TaxTotal1.appendChild(TaxAmount2);//se anade al grupo TaxTotal1
            TaxAmount2.appendChild(doc.createTextNode("5.49"));

            Element TaxSubtotal1 = doc.createElementNS("", "cac:TaxSubtotal");
            TaxTotal1.appendChild(TaxSubtotal1);//se anade al grupo TaxTotal1
            TaxSubtotal1.appendChild(doc.createTextNode("\n"));

            Element TaxableAmount = doc.createElementNS("", "cbc:TaxableAmount");
            TaxableAmount.setAttributeNS(null, "currencyID", "PEN");
            TaxableAmount.setIdAttributeNS(null, "currencyID", true);

            TaxSubtotal1.appendChild(TaxableAmount);//se anade al grupo TaxSubtotal1
            TaxableAmount.appendChild(doc.createTextNode("5.49"));

            Element TaxAmount3 = doc.createElementNS("", "cbc:TaxAmount");
            TaxAmount3.setAttributeNS(null, "currencyID", "PEN"); //================>errror estaba con item..getItem_moneda()
            TaxAmount3.setIdAttributeNS(null, "currencyID", true);
            TaxSubtotal1.appendChild(TaxAmount3);//se anade al grupo TaxSubtotal1
            TaxAmount3.appendChild(doc.createTextNode("5.49"));

            Element Percent = doc.createElementNS("", "cbc:Percent");
            TaxSubtotal1.appendChild(Percent);//se anade al grupo TaxSubtotal1
            Percent.appendChild(doc.createTextNode("0.00"));

            Element TaxCategory1 = doc.createElementNS("", "cac:TaxCategory");
            TaxSubtotal1.appendChild(TaxCategory1);//se anade al grupo TaxSubtotal1
            TaxCategory1.appendChild(doc.createTextNode("\n"));

            Element ID12 = doc.createElementNS("", "cbc:ID");
            TaxCategory1.appendChild(ID12);//se anade al grupo TaxCategory1
            ID12.appendChild(doc.createTextNode("VAT"));

            Element TaxExemptionReasonCode = doc.createElementNS("", "cbc:TaxExemptionReasonCode");
            TaxCategory1.appendChild(TaxExemptionReasonCode);//se anade al grupo TaxCategory1
            TaxExemptionReasonCode.appendChild(doc.createTextNode("10"));

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
            cdata = doc.createCDATASection("SILLAS");
            Description.appendChild(cdata);

            Element SellersItemIdentification = doc.createElementNS("", "cac:SellersItemIdentification");
            Item.appendChild(SellersItemIdentification);//se anade al grupo Item
            SellersItemIdentification.appendChild(doc.createTextNode("\n"));

            Element ID18 = doc.createElementNS("", "cbc:ID");
            SellersItemIdentification.appendChild(ID18);//se anade al grupo Item
            ID18.appendChild(doc.createTextNode("PCG-1425-5"));

            Element Price = doc.createElementNS("", "cac:Price");
            InvoiceLine.appendChild(Price);//se anade al grupo InvoiceLine
            Price.appendChild(doc.createTextNode("\n"));

            Element PriceAmount2 = doc.createElementNS("", "cbc:PriceAmount");
            PriceAmount2.setAttributeNS(null, "currencyID", "PEN");
            PriceAmount2.setIdAttributeNS(null, "currencyID", true);
            Price.appendChild(PriceAmount2);//se anade al grupo Price
            PriceAmount2.appendChild(doc.createTextNode("30.51"));

            log.info("generarXMLZipiadoBoleta - Prepara firma digital ");
            sig.setId("20601491193");
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
            tf.setOutputProperty(OutputKeys.STANDALONE, "no");
            StreamResult sr = new StreamResult(f);
            tf.transform(new DOMSource(doc), sr);
            sr.getOutputStream().close();
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
            resultado = "0100|Error al generar el archivo de formato xml de la Boleta.";
            log.error("generarXMLZipiadoBoleta - error  " + ex.toString());
        }
        return resultado;
    }
}
