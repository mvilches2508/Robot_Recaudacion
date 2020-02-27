package com.global.recaudacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.ir.ContinueNode;

/**
 *
 * @author mvilches
 */
public class ProcesadorArcRecaudacion {

    private ArrayList<ArrayList> registros = new ArrayList<ArrayList>();
    private String opera, dgitivoVerificador, conv, deb, codigoDeServicio, fechaCreacion;
    private ArrayList<RegistrosPAC> filas = new ArrayList<>();
    private ArrayList<String> cabeceras = new ArrayList<>();

    public ProcesadorArcRecaudacion() {
        cabeceras.add("opera");
        cabeceras.add("conv");
        cabeceras.add("deb");
        cabeceras.add("Codigo de Servicio");
        cabeceras.add("Fecha Creacion");
    }

    public String getFechaEjecucion() {
        Calendar calendario = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-ddHHmmss");

        return df.format(calendario.getTime());
    }

    public String getFechaFormateada(String fecha) {

        String fechaFormateada = fecha.substring(0, 2) + "/" + fecha.substring(2, 4) + "/" + fecha.substring(4, 8);

        return fechaFormateada;
    }

    public Date setFecha(String fecha) {
        Date fechaFormateada;
        try {
            fechaFormateada = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
            return fechaFormateada;
        } catch (ParseException ex) {
            System.out.println("Error al transformar fecha: " + ex);
            return null;
        }
    }

    public boolean leeArchivo(String archivoBanco) {
        try {
            
            FileReader fr = new FileReader(archivoBanco);
            BufferedReader br = new BufferedReader(fr);
            String linea = "";
            loop:
            while ((linea = br.readLine()) != null) {
                leeTrama(linea);
            }
            return true;
        } catch (NumberFormatException ex) {
            System.err.println("ERROR: " + ex);
             System.err.println("codigoDeServicio"+codigoDeServicio+ "fechaCreacion"+fechaCreacion+ "opera"+opera+ "conv"+conv+ "deb"+deb+ dgitivoVerificador);                           
             return false;
        }catch(Exception ex){
            System.err.println("ERROR: " + ex);
            return false;
        }
    }
    
    private boolean leeTrama(String trama){
        try{
                // System.out.println("linea: "+linea);
                opera = trama.substring(96, 104);
                dgitivoVerificador = trama.substring(104, 105);
                //System.out.println("opera: "+opera);
                // System.out.println("dv: "+dv);
                conv = trama.substring(0, 4);
                // System.out.println("conv: "+conv);
                deb = trama.substring(10, 13);
                //  System.out.println("deb: "+deb);
                codigoDeServicio = trama.substring(85, 105);
                // System.out.println("codigoDeServicio: "+codigoDeServicio);
                fechaCreacion = trama.substring(128, 136);
                //  System.out.println("fechaCreacion: "+fechaCreacion);   
                RegistrosPAC pac = new RegistrosPAC(codigoDeServicio, fechaCreacion, Integer.parseInt(opera), Integer.parseInt(conv), Integer.parseInt(deb), dgitivoVerificador);
                filas.add(pac);
            
            return true;
        } catch (NumberFormatException ex) {
            System.err.println("ERROR: " + ex);
             System.err.println("codigoDeServicio"+codigoDeServicio+ "fechaCreacion"+fechaCreacion+ "opera"+opera+ "conv"+conv+ "deb"+deb+ dgitivoVerificador);                           
             return false;
        }
    }
    
    public ArrayList<ArrayList> limpiezaRegistros() {
        registros.add(cabeceras);
        for (int i = 0; i < filas.size(); i++) {
            //System.out.println(filas.get(i).getOperacion() +filas.get(i).getDebe()+ "|" + filas.get(i).getConvenio() + "|" + filas.get(i).getDebe()
            //       + "|" + filas.get(i).getCodServicio() + "|" + filas.get(i).getFechaCrea());
            String operacion = filas.get(i).getOperacion() + filas.get(i).getDv();

            //REGLA RETIRO POR REGLA DEL RUT
            if ((operacion.contains("12639876K") == false)) {
                if (operacion.contains("762065037") == false) {
                    if (operacion.contains("258028821") == false) {
                        //*************Validación de OPeracion V/S Código de Servicio*************   
                        int codServicioInt = Integer.parseInt(filas.get(i).getCodServicio());
                        int operacionInt = Integer.parseInt(operacion);
                        if (codServicioInt == operacionInt) {
                            //*************REGLA RETIRO POR REGLA DEL Correlativo**************
                            if (operacionInt <= 10000000 || operacionInt >= 150000000) {
                                //*************REGLA RETIRO POR REGLA DEL 20***************
                                if (operacionInt >= 200000000 && operacionInt <= 209999999) {

                                    /**
                                     * *************REGISTRO VALIDO
                                     * 20***************************** CAMBIO de
                                     * TIPO de ARREGLO de STRING A OBJECT
                                     */
                                    ArrayList<Object> registroLimpio = new ArrayList<>();
                                    registroLimpio.add(operacionInt - 200000000);
                                    registroLimpio.add(filas.get(i).getConvenio());//cambio por object
                                    registroLimpio.add(filas.get(i).getDebe());
                                    registroLimpio.add(filas.get(i).getCodServicio());
                                    registroLimpio.add(setFecha(getFechaFormateada(filas.get(i).getFechaCrea())));//seteo a DATE

                                    registros.add(registroLimpio);
                                } else {
                                    ArrayList<Object> registroLimpio = new ArrayList<>();
                                    registroLimpio.add(operacionInt);//cambio en vez de sacar desde el arreglo
                                    registroLimpio.add(filas.get(i).getConvenio());
                                    registroLimpio.add(filas.get(i).getDebe());
                                    registroLimpio.add(filas.get(i).getCodServicio());
                                    registroLimpio.add(setFecha(getFechaFormateada(filas.get(i).getFechaCrea())));

                                    registros.add(registroLimpio);

                                }
                            }else{
                                System.out.println("Eliminado por regla del correlativo :"+operacion);
                            }
                        } else {
                            System.out.println("Código de Operación v/s Servicio no coinciden");
                            System.out.println("revisar operacion: " + operacionInt);
                        }
                    } else {
                        System.out.println("Eliminado por regla del rut: " + operacion);
                    }
                } else {
                    System.out.println("Eliminado por regla del rut: " + operacion);
                }
            } else {
                System.out.println("Eliminado por regla del rut: " + operacion);
            }
        }

        return registros;
    }

    public Object[][] getRegistros() {
        Object[][] data = new Object[registros.size()][registros.get(1).size()];

        for (int i = 0; i < registros.size(); i++) {
            for (int j = 0; j < registros.get(i).size(); j++) {
                data[i][j] = registros.get(i).get(j);
            }
        }
        return data;
    }

    public Object[][] getRegistrosNomina(ArrayList<ArrayList> valores) {
        Object[][] data = new Object[valores.size()][valores.get(1).size() - 4];

        int m = 0, l = 0;
        for (int i = 0; i < valores.size(); i++) {
            for (int j = 0; j < valores.get(i).size(); j++) {
                if (j == 0 || j == 1 || j == 2 || j == 4 || j == 5 || j == 6) {
                    if (j == 5 || i == 0 || j == 4) {
                        String valor = valores.get(i).get(j).toString();
                        valor = valor.replace("-", "");
                        data[l][m] = valor.replace(".0", "");
                        m++;
                    } else {
                        if (i == 0) {
                            if (valores.get(i).get(j).toString().contains("CONV")) {
                                data[l][m] = "convenio";
                            }
                            if (valores.get(i).get(j).toString().contains("DEB")) {
                                data[l][m] = "bco_deb";
                            }
                            if (valores.get(i).get(j).toString().contains("BCO_REC")) {
                                data[l][m] = "bco_rec";
                            }
                            if (valores.get(i).get(j).toString().contains("Servicio")) {
                                data[l][m] = "servicio";
                            }
                            if (valores.get(i).get(j).toString().contains("Expr1")) {
                                data[l][m] = "RUT SUSCRIPTOR";
                            }
                            if (valores.get(i).get(j).toString().contains("Monto Cuota")) {
                                data[l][m] = "MONTO";
                            }
                        } else {

                            data[l][m] = valores.get(i).get(j).toString();
                            m++;
                        }
                    }
                }
            }
//            System.out.println("");
            l++;
            m = 0;
            // }
        }
        return data;
    }
}
