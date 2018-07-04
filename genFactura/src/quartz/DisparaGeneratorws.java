package quartz;

import com.org.factories.ConnectionPool;
import com.org.model.beans.DocumentoCabBean;
import com.org.model.despatchers.DElectronicoDespachador;
import com.org.util.LecturaXML;
import com.org.ws.BolElectronica;
import java.sql.Connection;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Clase que implementa la tarea final a ejecutar
 *
 * @author gonzalo.delgado
 *
 */
public class DisparaGeneratorws {

    private static Log log = LogFactory.getLog(DisparaGeneratorws.class);

    public synchronized static void generator() {
        //log.info("generator");
        Connection conn = null;
        try {
            //log.info("generator - conectar a MySQl");
            conn = ConnectionPool.obtenerConexionMysql();
            System.out.println("__Ejecución disparar " + new Date().toString());
            //log.info("generator - buscar pendientes");
            DocumentoCabBean item = DElectronicoDespachador.pendienteDocElectronico(conn);
            String iddoc = null;
            String tipodoc = null;
            String result = "x";
            if (item != null && item.getDocu_tipodocumento().trim() != null) {
                log.info("generator - Existe pendiente");
                iddoc = item.getDocu_codigo();
                //tipodoc = Integer.valueOf(item.getDocu_tipodocumento()).toString().trim();
                tipodoc = item.getDocu_tipodocumento().trim();

                //System.out.println("___Preparando el doc. " + Util.equivalenciaTipoDocNombre(tipodoc) + " " + iddoc);
                System.out.println("___Preparando el doc. " + tipodoc + " " + iddoc);
                log.info("generator - extrayendo datos");
                switch (tipodoc) {
                    case "03":
                        result = BolElectronica.generarXMLZipiadoBoleta(iddoc, conn);
                        break;
                    default:
                        result = "0100|Operacion nula";
                        break;

                }
            }
            if (!result.equals("x")) {
                System.out.println("Resultado " + result);
            }

        } catch (Exception er) {
            er.printStackTrace();
                log.error("generator - error " + er.toString());
            
        } finally {
            ConnectionPool.closeConexion(conn);
        }
    }

}
