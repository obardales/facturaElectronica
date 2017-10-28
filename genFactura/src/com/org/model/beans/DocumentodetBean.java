/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.model.beans;

/**
 *
 * @author oswaldo
 */
public class DocumentodetBean {

    private String docu_codigo;
    /**
     * *variable de detalle*****
     */
    private String iddetalle;
    private String item_moneda;
    private String item_orden;
    private String item_unidad;
    private String item_cantidad;
    private String item_codproducto;
    private String item_descripcion;
    private String item_afectacion;
    private String item_pventa;
    private String item_pventa_no_onerosa;
    private String item_ti_subtotal;
    private String item_ti_igv;

    public String getDocu_codigo() {
        return docu_codigo;
    }

    public DocumentodetBean() {
    }

    public void setDocu_codigo(String docu_codigo) {
        this.docu_codigo = docu_codigo;
    }

    public String getIddetalle() {
        return iddetalle;
    }

    public void setIddetalle(String iddetalle) {
        this.iddetalle = iddetalle;
    }

    public String getItem_moneda() {
        return item_moneda;
    }

    public void setItem_moneda(String item_moneda) {
        this.item_moneda = item_moneda;
    }

    public String getItem_orden() {
        return item_orden;
    }

    public void setItem_orden(String item_orden) {
        this.item_orden = item_orden;
    }

    public String getItem_unidad() {
        return item_unidad;
    }

    public void setItem_unidad(String item_unidad) {
        this.item_unidad = item_unidad;
    }

    public String getItem_cantidad() {
        return item_cantidad;
    }

    public void setItem_cantidad(String item_cantidad) {
        this.item_cantidad = item_cantidad;
    }

    public String getItem_codproducto() {
        return item_codproducto;
    }

    public void setItem_codproducto(String item_codproducto) {
        this.item_codproducto = item_codproducto;
    }

    public String getItem_descripcion() {
        return item_descripcion;
    }

    public void setItem_descripcion(String item_descripcion) {
        this.item_descripcion = item_descripcion;
    }

    public String getItem_afectacion() {
        return item_afectacion;
    }

    public void setItem_afectacion(String item_afectacion) {
        this.item_afectacion = item_afectacion;
    }

    public String getItem_pventa() {
        return item_pventa;
    }

    public void setItem_pventa(String item_pventa) {
        this.item_pventa = item_pventa;
    }

    public String getItem_pventa_no_onerosa() {
        return item_pventa_no_onerosa;
    }

    public void setItem_pventa_no_onerosa(String item_pventa_no_onerosa) {
        this.item_pventa_no_onerosa = item_pventa_no_onerosa;
    }

    public String getItem_ti_subtotal() {
        return item_ti_subtotal;
    }

    public void setItem_ti_subtotal(String item_ti_subtotal) {
        this.item_ti_subtotal = item_ti_subtotal;
    }

    public String getItem_ti_igv() {
        return item_ti_igv;
    }

    public void setItem_ti_igv(String item_ti_igv) {
        this.item_ti_igv = item_ti_igv;
    }

    
}
