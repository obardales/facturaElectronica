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
        DocumentoCabBean items = null;
        List<DocumentodetBean> detdocelec = null;
        List<Leyenda> leyendas = null;
        BolElectronica.generarXMLZipiadoBoleta(items, detdocelec, leyendas);
    }
    
}
