package com.global.recaudacion;

/**
 *
 * @author mvilches
 */
public class RegistrosPAC {
     private String  codServicio, fechaCrea,dv; //fecha es DD-MM-YYYY
     private int operacion,convenio,debe;

    public RegistrosPAC(String codServicio, String fechaCrea, int operacion, int convenio, int debe, String dv) {
        this.codServicio = codServicio;
        this.fechaCrea = fechaCrea;
        this.operacion = operacion;
        this.convenio = convenio;
        this.debe = debe;
        this.dv=dv;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getCodServicio() {
        return codServicio;
    }
    public void setCodServicio(String codServicio) {
        this.codServicio = codServicio;
    }
    public String getFechaCrea() {
        return fechaCrea;
    }
    public void setFechaCrea(String fechaCrea) {
        this.fechaCrea = fechaCrea;
    }
    public int getOperacion() {
        return operacion;
    }
    public void setOperacion(int operacion) {
        this.operacion = operacion;
    }
    public int getConvenio() {
        return convenio;
    }
    public void setConvenio(int convenio) {
        this.convenio = convenio;
    }
    public int getDebe() {
        return debe;
    }
    public void setDebe(int debe) {
        this.debe = debe;
    }
     
     
}
