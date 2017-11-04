/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.model.despatchers;

import com.org.model.beans.DocumentoCabBean;
import com.org.model.beans.DocumentodetBean;
import com.org.model.beans.Leyenda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author oswaldo
 */
public class DElectronicoDespachador {
    private static Log log = LogFactory.getLog(DElectronicoDespachador.class);

    public static DocumentoCabBean cargarDocElectronico(String pdocu_codigo, Connection conn) {
        DocumentoCabBean b = null;

//        Connection conn = null;
        try {
//            conn = ConnectionPool.obtenerConexionMysql();
            String sql = "SELECT DOCU_CODIGO,";
            sql += " EMPR_RAZONSOCIAL,";
            sql += " EMPR_UBIGEO,";
            sql += " EMPR_NOMBRECOMERCIAL,";
            sql += " EMPR_DIRECCION,";
            sql += " EMPR_PROVINCIA,";
            sql += " EMPR_DEPARTAMENTO,";
            sql += " EMPR_DISTRITO,";
            sql += " EMPR_PAIS,";
            sql += " EMPR_NRORUC,";
            sql += " EMPR_TIPODOC,";
            sql += " CLIE_NUMERO,";
            sql += " CLIE_TIPODOC,";
            sql += " CLIE_NOMBRE,";
            sql += " DOCU_FECHA,";
            sql += " DOCU_TIPODOCUMENTO,";
            sql += " DOCU_NUMERO,";
            sql += " DOCU_MONEDA,";
            sql += " DOCU_GRAVADA  as  DOCU_GRAVADA,";
            sql += " DOCU_INAFECTA  as  DOCU_INAFECTA,";
            sql += " DOCU_EXONERADA  as  DOCU_EXONERADA,";
            sql += " DOCU_GRATUITA  as  DOCU_GRATUITA,";
            sql += " DOCU_DESCUENTO  as  DOCU_DESCUENTO,";
            sql += " DOCU_SUBTOTAL  as  DOCU_SUBTOTAL,";
            sql += " DOCU_TOTAL  as  DOCU_TOTAL,";
            sql += " DOCU_IGV  as  DOCU_IGV,";
            sql += " TASA_IGV,";
            sql += " DOCU_ISC,";
            sql += " TASA_ISC,";
            sql += " DOCU_OTROSTRIBUTOS  as  DOCU_OTROSTRIBUTOS,";
            sql += " TASA_OTROSTRIBUTOS,";

            sql += " DOCU_OTROSCARGOS  as  DOCU_OTROSCARGOS,";
            sql += " DOCU_PERCEPCION  as  DOCU_PERCEPCION,";
            sql += " docu_enviaws, ";
            sql += " idExterno, ";
            sql += " clie_correo_cpe1, ";
            sql += " clie_correo_cpe2 ";
            
            
            sql += " FROM cabecera";
            sql += " WHERE  DOCU_CODIGO = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pdocu_codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                b = new DocumentoCabBean();
                b.setDocu_codigo(rs.getString("docu_codigo"));
                b.setEmpr_razonsocial(rs.getString("empr_razonsocial"));
                b.setEmpr_ubigeo(rs.getString("empr_ubigeo"));
                b.setEmpr_nombrecomercial(rs.getString("empr_nombrecomercial"));
                b.setEmpr_direccion(rs.getString("empr_direccion"));
                b.setEmpr_provincia(rs.getString("empr_provincia"));
                b.setEmpr_departamento(rs.getString("empr_departamento"));
                b.setEmpr_distrito(rs.getString("empr_distrito"));
                b.setEmpr_pais(rs.getString("empr_pais"));
                b.setEmpr_nroruc(rs.getString("empr_nroruc"));
                b.setEmpr_tipodoc(rs.getString("empr_tipodoc"));
                b.setClie_numero(rs.getString("clie_numero"));
                b.setClie_tipodoc(rs.getString("clie_tipodoc"));
                b.setClie_nombre(rs.getString("clie_nombre"));
                b.setDocu_fecha(rs.getString("docu_fecha"));
                b.setDocu_tipodocumento(rs.getString("docu_tipodocumento"));
                b.setDocu_numero(rs.getString("docu_numero"));
                b.setDocu_moneda(rs.getString("docu_moneda"));
                b.setDocu_gravada(rs.getString("docu_gravada"));
                b.setDocu_inafecta(rs.getString("docu_inafecta"));
                b.setDocu_exonerada(rs.getString("docu_exonerada"));
                b.setDocu_gratuita(rs.getString("docu_gratuita"));
                b.setDocu_descuento(rs.getString("docu_descuento"));
                b.setDocu_subtotal(rs.getString("docu_subtotal"));
                b.setDocu_total(rs.getString("docu_total"));
                b.setDocu_igv(rs.getString("docu_igv"));
                b.setTasa_igv(rs.getString("tasa_igv"));
                b.setDocu_isc(rs.getString("docu_isc"));
                b.setTasa_isc(rs.getString("tasa_isc"));
                b.setDocu_otrostributos(rs.getString("docu_otrostributos"));
                b.setTasa_otrostributos(rs.getString("tasa_otrostributos"));
                b.setDocu_otroscargos(rs.getString("docu_otroscargos"));

                b.setDocu_enviaws(rs.getString("docu_enviaws"));
                b.setIdExterno(rs.getString("idExterno"));
                b.setClie_correo_cpe1(rs.getString("clie_correo_cpe1"));
                b.setClie_correo_cpe2(rs.getString("clie_correo_cpe2"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
//            ConnectionPool.closeConexion(conn);
        }
        return b;
    }

    public static List<DocumentodetBean> cargarDetDocElectronico(String pdocu_codigo, Connection conn) throws SQLException {
        List<DocumentodetBean> detalle = new ArrayList<DocumentodetBean>();
//        Connection conn = null;
        try {
//            conn = ConnectionPool.obtenerConexionMysql();
            String sql = "SELECT  DOCU_CODIGO,";
            sql += " ITEM_ORDEN,";
            sql += " ITEM_UNIDAD,";
            sql += " ITEM_CANTIDAD,";
            sql += " ITEM_CODPRODUCTO,";
            sql += " ITEM_DESCRIPCION,";
            sql += " ITEM_AFECTACION,";
            sql += " ITEM_PVENTA, ";
            sql += " item_pventa_no_onerosa,";
            sql += " ITEM_TO_SUBTOTAL,";
            sql += " ITEM_TO_IGV ";

            sql += " FROM detalle";
            sql += " WHERE DOCU_CODIGO = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pdocu_codigo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DocumentodetBean cdetalle = new DocumentodetBean();
                cdetalle.setDocu_codigo(rs.getString("docu_codigo"));
                cdetalle.setItem_orden(rs.getString("item_orden"));
                cdetalle.setItem_unidad(rs.getString("item_unidad"));
                cdetalle.setItem_cantidad(rs.getString("item_cantidad"));
                cdetalle.setItem_codproducto(rs.getString("item_codproducto"));
                cdetalle.setItem_descripcion(rs.getString("item_descripcion"));
                cdetalle.setItem_afectacion(rs.getString("item_afectacion"));
                cdetalle.setItem_pventa(rs.getString("item_pventa"));
                cdetalle.setItem_pventa_no_onerosa(rs.getString("item_pventa_no_onerosa"));
                cdetalle.setItem_ti_subtotal(rs.getString("item_to_subtotal"));
                cdetalle.setItem_ti_igv(rs.getString("item_to_igv"));

                detalle.add(cdetalle);
            }
        } catch (Exception ex) {
            throw new SQLException(ex);
        } finally {
//            ConnectionPool.closeConexion(conn);
        }
        return detalle;
    }

    public static List<Leyenda> cargarDetDocElectronicoLeyenda(String pdocu_codigo, Connection conn) throws SQLException {
        List<Leyenda> detalle = new ArrayList<Leyenda>();

        try {

            String sql = "SELECT  leyenda_codigo, "
                    + "leyenda_texto ";
            // Anticipos
            sql += " FROM leyenda ";
            sql += " WHERE DOCU_CODIGO = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pdocu_codigo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Leyenda leyenda = new Leyenda();
                leyenda.setLeyendaCodigo(rs.getString("leyenda_codigo"));
                leyenda.setLeyendaTexto(rs.getString("leyenda_texto"));
                // Anticipos

                detalle.add(leyenda);
            }
        } catch (Exception ex) {
            throw new SQLException(ex);
        } finally {
        }
        return detalle;
    }

    public static DocumentoCabBean pendienteDocElectronico(Connection conn) {
        DocumentoCabBean b = null;

//        Connection conn = null;
        try {
//            conn = ConnectionPool.obtenerConexionMysql();
            String sql = "SELECT * ";
            sql += " FROM cabecera";
            sql += " WHERE  docu_proce_status = 'N' ";
            sql += " order by docu_codigo LIMIT 1 ";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                b = new DocumentoCabBean();
                b.setDocu_codigo(rs.getString("docu_codigo"));
                b.setEmpr_razonsocial(rs.getString("empr_razonsocial"));
                b.setEmpr_ubigeo(rs.getString("empr_ubigeo"));
                b.setEmpr_nombrecomercial(rs.getString("empr_nombrecomercial"));
                b.setEmpr_direccion(rs.getString("empr_direccion"));
                b.setEmpr_provincia(rs.getString("empr_provincia"));
                b.setEmpr_departamento(rs.getString("empr_departamento"));
                b.setEmpr_distrito(rs.getString("empr_distrito"));
                b.setEmpr_pais(rs.getString("empr_pais"));
                b.setEmpr_nroruc(rs.getString("empr_nroruc"));
                b.setEmpr_tipodoc(rs.getString("empr_tipodoc"));
                b.setClie_numero(rs.getString("clie_numero"));
                b.setClie_tipodoc(rs.getString("clie_tipodoc"));
                b.setClie_nombre(rs.getString("clie_nombre"));
                b.setDocu_fecha(rs.getString("docu_fecha"));
                b.setDocu_tipodocumento(rs.getString("docu_tipodocumento"));
                b.setDocu_numero(rs.getString("docu_numero"));
                b.setDocu_moneda(rs.getString("docu_moneda"));
                b.setDocu_gravada(rs.getString("docu_gravada"));
                b.setDocu_inafecta(rs.getString("docu_inafecta"));
                b.setDocu_exonerada(rs.getString("docu_exonerada"));
                b.setDocu_gratuita(rs.getString("docu_gratuita"));
                b.setDocu_descuento(rs.getString("docu_descuento"));
                b.setDocu_subtotal(rs.getString("docu_subtotal"));
                b.setDocu_total(rs.getString("docu_total"));
                b.setDocu_igv(rs.getString("docu_igv"));
                b.setTasa_igv(rs.getString("tasa_igv"));
                b.setDocu_isc(rs.getString("docu_isc"));
                b.setTasa_isc(rs.getString("tasa_isc"));
                b.setDocu_otrostributos(rs.getString("docu_otrostributos"));
                b.setTasa_otrostributos(rs.getString("tasa_otrostributos"));
                b.setDocu_otroscargos(rs.getString("docu_otroscargos"));
                b.setDocu_enviaws(rs.getString("docu_enviaws"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
//            ConnectionPool.closeConexion(conn);
        }
        return b;
    }

    public static DocumentoCabBean noPendienteDocElectronico(Connection conn) {
        DocumentoCabBean b = null;

//        Connection conn = null;
        try {
//            conn = ConnectionPool.obtenerConexionMysql();
            String sql = "SELECT * ";
            sql += " FROM cabecera";
            sql += " WHERE  docu_proce_status in ('B','P','E','X') and docu_proce_fecha <=  DATE_SUB(NOW(), INTERVAL 10 MINUTE)";
            sql += " order by docu_codigo LIMIT 1 ";

            PreparedStatement ps = conn.prepareStatement(sql);
            //ps.setString(1, proceso);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                b = new DocumentoCabBean();
                b.setDocu_codigo(rs.getString("docu_codigo"));
                b.setEmpr_razonsocial(rs.getString("empr_razonsocial"));
                b.setEmpr_ubigeo(rs.getString("empr_ubigeo"));
                b.setEmpr_nombrecomercial(rs.getString("empr_nombrecomercial"));
                b.setEmpr_direccion(rs.getString("empr_direccion"));
                b.setEmpr_provincia(rs.getString("empr_provincia"));
                b.setEmpr_departamento(rs.getString("empr_departamento"));
                b.setEmpr_distrito(rs.getString("empr_distrito"));
                b.setEmpr_pais(rs.getString("empr_pais"));
                b.setEmpr_nroruc(rs.getString("empr_nroruc"));
                b.setEmpr_tipodoc(rs.getString("empr_tipodoc"));
                b.setClie_numero(rs.getString("clie_numero"));
                b.setClie_tipodoc(rs.getString("clie_tipodoc"));
                b.setClie_nombre(rs.getString("clie_nombre"));
                b.setDocu_fecha(rs.getString("docu_fecha"));
                b.setDocu_tipodocumento(rs.getString("docu_tipodocumento"));
                b.setDocu_numero(rs.getString("docu_numero"));
                b.setDocu_moneda(rs.getString("docu_moneda"));
                b.setDocu_gravada(rs.getString("docu_gravada"));
                b.setDocu_inafecta(rs.getString("docu_inafecta"));
                b.setDocu_exonerada(rs.getString("docu_exonerada"));
                b.setDocu_gratuita(rs.getString("docu_gratuita"));
                b.setDocu_descuento(rs.getString("docu_descuento"));
                b.setDocu_subtotal(rs.getString("docu_subtotal"));
                b.setDocu_total(rs.getString("docu_total"));
                b.setDocu_igv(rs.getString("docu_igv"));
                b.setTasa_igv(rs.getString("tasa_igv"));
                b.setDocu_isc(rs.getString("docu_isc"));
                b.setTasa_isc(rs.getString("tasa_isc"));
                b.setDocu_otrostributos(rs.getString("docu_otrostributos"));
                b.setTasa_otrostributos(rs.getString("tasa_otrostributos"));
                b.setDocu_otroscargos(rs.getString("docu_otroscargos"));
                b.setDocu_enviaws(rs.getString("docu_enviaws"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
//            ConnectionPool.closeConexion(conn);
        }
        return b;
    }
}
