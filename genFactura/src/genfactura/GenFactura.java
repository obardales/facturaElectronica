/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genfactura;

import com.org.model.beans.DocumentoCabBean;
import com.org.model.beans.DocumentodetBean;
import com.org.model.beans.Leyenda;
import com.org.ws.BolElectronica;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author obardales
 */
public class GenFactura {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DocumentoCabBean items = new DocumentoCabBean();
        // Empresa o emisor
        items.setEmpr_tipodoc("6");
        items.setEmpr_nroruc("20380456444");
        items.setEmpr_razonsocial("CAYMAN S.A.C");
        items.setEmpr_nombrecomercial("CAYMAN S.A.C");
        items.setEmpr_direccion("Mi casa 145");
        items.setEmpr_distrito("Los Olivo");
        items.setEmpr_provincia("Lima");
        items.setEmpr_departamento("Lima");
        items.setEmpr_ubigeo("150128");
        items.setEmpr_pais("PE");
        
        // Cliente o receptor 
        items.setClie_tipodoc("1"); // Sin docu. "-" solo se usa DNI = "1"// anexo de catalogos
        items.setClie_numero("12345678");
        items.setClie_nombre("Minombre");
        
        //documento
        // Cabecera
        items.setDocu_tipodocumento("03"); // anexo de catalogos 03 refiere a Boleta de Venta Electronica
        items.setDocu_numero("BB01-45"); 
        items.setDocu_fecha("2017-10-28"); // formato segun sunat
        items.setDocu_gravada("18757.46");
        items.setDocu_igv("3376.34");
        items.setDocu_descuento("0.00");
        items.setDocu_exonerada("0.00");
        items.setDocu_gratuita("0.00");
        items.setDocu_inafecta("0.00");
        items.setDocu_isc("0.00");
        items.setDocu_moneda("PEN");
        items.setDocu_otroscargos("0.00");
        items.setDocu_otrostributos("0.00");
        items.setDocu_total("22133.80");
        
        //Detalle
        DocumentodetBean detdoc = new DocumentodetBean();
        List<DocumentodetBean> detdocelec = new ArrayList<>();
        
        detdoc.setItem_orden("1");
        detdoc.setItem_codproducto("12");
        detdoc.setItem_descripcion("Polos M");
        detdoc.setItem_unidad("NIU");
        detdoc.setItem_cantidad("10.00");
        detdoc.setItem_moneda("PEN");
        detdoc.setItem_pventa("25.00");
        detdoc.setItem_ti_subtotal("250.00");
        detdoc.setItem_pventa_no_onerosa("0.00");
                
        detdocelec.add(detdoc);
        
        detdoc = new DocumentodetBean();        
        
        detdoc.setItem_orden("2");
        detdoc.setItem_codproducto("ZZer12");
        detdoc.setItem_descripcion("Jeans M rojo");
        detdoc.setItem_unidad("NIU");
        detdoc.setItem_cantidad("12.00");
        detdoc.setItem_moneda("PEN");
        detdoc.setItem_pventa("50.00");
        detdoc.setItem_ti_subtotal("600.00");
        detdoc.setItem_pventa_no_onerosa("0.00");
        
        detdocelec.add(detdoc);

        //Leyendas
        Leyenda leyenda = new Leyenda();
        List<Leyenda> leyendas = new ArrayList<>();
        
        leyenda.setLeyendaCodigo("1000");
        leyenda.setLeyendaTexto("VEINTIDOS MIL CIENTO TREINTITRES Y 80/100 SOLES");
        
        leyendas.add(leyenda);
        
        BolElectronica.generarXMLZipiadoBoleta(items, detdocelec, leyendas);
    }
    
}
