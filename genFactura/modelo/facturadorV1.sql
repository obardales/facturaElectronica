-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema facturador
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema facturador
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `facturador` DEFAULT CHARACTER SET utf8 ;
USE `facturador` ;

-- -----------------------------------------------------
-- Table `facturador`.`cabecera`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturador`.`cabecera` (
  `docu_codigo` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Codigo de tabla Autogenerado',
  `idExterno` VARCHAR(50) NULL DEFAULT NULL,
  `empr_razonsocial` VARCHAR(200) NULL DEFAULT NULL COMMENT 'Razon social de la empresa',
  `empr_ubigeo` VARCHAR(6) NULL DEFAULT NULL COMMENT 'Codigo de Ubigeo de la empresa',
  `empr_nombrecomercial` VARCHAR(200) NULL DEFAULT NULL COMMENT 'Nombre Comercial de la empresa',
  `empr_direccion` VARCHAR(200) NULL DEFAULT NULL COMMENT 'Direccion de la Empresa',
  `empr_provincia` VARCHAR(200) NULL DEFAULT NULL COMMENT 'Provincia de la empresa',
  `empr_departamento` VARCHAR(200) NULL DEFAULT NULL COMMENT 'Departamento de la empresa',
  `empr_distrito` VARCHAR(200) NULL DEFAULT NULL COMMENT 'Distrito de la empresa',
  `empr_pais` VARCHAR(200) NULL DEFAULT NULL COMMENT 'Pais de la empresa - PE',
  `empr_nroruc` VARCHAR(11) NULL DEFAULT NULL COMMENT 'Numero de Ruc de la empresa',
  `empr_tipodoc` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Tiop de documento de la empresa - 6 - RUC',
  `clie_numero` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Documento del cliente',
  `clie_tipodoc` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Tipo de documento del cliente  - 6  - RUC',
  `clie_nombre` VARCHAR(200) NULL DEFAULT NULL COMMENT 'Nombre / Razon Social del cliente',
  `docu_fecha` VARCHAR(45) NULL DEFAULT NULL COMMENT 'fecha del documento formato YYYY-MM-DD',
  `docu_tipodocumento` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Tipo de Documento \n01 - Factura\n03 - Boleta de Venta\n07 - Nota de Credito\n08 - Nota de Debito\n20 - Retenciones',
  `docu_numero` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Numero de Documento\nformat \nserie - numero\nserie: \nF000 factura\nB000 Boleta \nR000 Retenciones\n-------------\nNota de credito  y Nota de Debito\nsi e a una factura\nF000\nsi es a una Boleta\nB000',
  `docu_moneda` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Moneda del documento\nPEN - Soles\nUSD - Dollar',
  `docu_gravada` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Importe total de Documento grabado al IGV',
  `docu_inafecta` VARCHAR(45) NULL DEFAULT NULL COMMENT 'importe inafecto del documento',
  `docu_exonerada` VARCHAR(45) NULL DEFAULT NULL COMMENT 'importe exonerado del IGV  del documento',
  `docu_gratuita` VARCHAR(45) NULL DEFAULT NULL COMMENT 'importe de valor gratuito  del documento',
  `docu_descuento` VARCHAR(45) NULL DEFAULT NULL COMMENT 'importe de descuento del documento',
  `docu_subtotal` VARCHAR(45) NULL DEFAULT NULL COMMENT 'importe subtotal del documento',
  `docu_total` VARCHAR(45) NULL DEFAULT NULL COMMENT 'importe total  del documento\n/ importe total retenido',
  `docu_igv` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Importe del IGV del documento',
  `tasa_igv` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Tasa del IGV =18 ',
  `docu_isc` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Importe  del ISC',
  `tasa_isc` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Tasa ISC',
  `docu_otrostributos` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Otros importes de otros tributos',
  `tasa_otrostributos` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Tasa de otros tributos',
  `docu_otroscargos` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Otros cargos',
  `docu_percepcion` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Importe de percepcion',
  `hashcode` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Has code de la impresion\n\nsYlI7o6ndTiOEYz+Yv4jmz3CnWY=',
  `cdr` VARCHAR(200) NULL DEFAULT NULL COMMENT 'Estado del CDR',
  `cdr_nota` VARCHAR(500) NULL DEFAULT NULL COMMENT 'Nota del CDR',
  `cdr_observacion` VARCHAR(2000) NULL DEFAULT ' ' COMMENT 'Notas de Observaciones del CDR',
  `docu_enviaws` VARCHAR(45) NULL DEFAULT 'S' COMMENT 'Estado si el documento se envia al ws de SUNAT\nS- Si se envia.\nN- No se envia.',
  `docu_proce_status` VARCHAR(45) NULL DEFAULT 'N' COMMENT 'N - Nuevo\nB - Bloqueo\nP - Proceso-\nE - Enviado\nX - Error de Envio\n\n',
  `docu_proce_fecha` DATETIME NULL DEFAULT NULL,
  `docu_link_pdf` VARCHAR(200) NULL DEFAULT NULL,
  `docu_link_cdr` VARCHAR(200) NULL DEFAULT NULL,
  `docu_link_xml` VARCHAR(200) NULL DEFAULT NULL,
  `clie_correo_cpe1` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Correo cliente 1',
  `clie_correo_cpe2` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Correo cliente 2',
  PRIMARY KEY (`docu_codigo`),
  UNIQUE INDEX `idExterno_UNIQUE` (`idExterno` ASC),
  INDEX `idExterno_Cab_idx` (`idExterno` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 83
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `facturador`.`detalle`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturador`.`detalle` (
  `iddetalle` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Codigo autogenerado de la tabla',
  `docu_codigo` INT(11) NOT NULL COMMENT 'Codigo relacion de la tabla cabecera',
  `item_orden` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Numero de Orden',
  `item_unidad` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Unidad de Medida del Item\ndefecto NIU',
  `item_cantidad` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Cantidad de Item',
  `item_codproducto` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Codigo del Producto',
  `item_descripcion` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Descripcion del Producto ',
  `item_afectacion` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Tipo de afectacion\n\n10 Gravado - Operación Onerosa (uso comun)\n\nverificar CATALOGO No. 07',
  `item_tipo_precio_venta` VARCHAR(45) NULL DEFAULT '01' COMMENT 'Código de tipo de precio - Catálogo No. 16',
  `item_pventa` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Precio de venta unitario sin IGV',
  `item_pventa_nohonerosa` VARCHAR(45) NULL DEFAULT '0.00' COMMENT 'Valor referencial unitario por ítem en operaciones no onerosas ( gratuito)',
  `item_to_subtotal` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Importe sub total',
  `item_to_igv` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Importe del IGV',
  PRIMARY KEY (`iddetalle`),
  INDEX `fk_detalle_cabecera_idx` (`docu_codigo` ASC),
  CONSTRAINT `fk_detalle_cabecera`
    FOREIGN KEY (`docu_codigo`)
    REFERENCES `facturador`.`cabecera` (`docu_codigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 232
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `facturador`.`leyenda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturador`.`leyenda` (
  `id_leyenda` INT NOT NULL AUTO_INCREMENT,
  `docu_codigo` INT(11) NOT NULL,
  `leyenda_codigo` VARCHAR(10) NOT NULL,
  `leyenda_texto` VARCHAR(500) NOT NULL,
  INDEX `fk_table1_cabecera1_idx` (`docu_codigo` ASC),
  PRIMARY KEY (`id_leyenda`),
  CONSTRAINT `fk_table1_cabecera1`
    FOREIGN KEY (`docu_codigo`)
    REFERENCES `facturador`.`cabecera` (`docu_codigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
