package com.global.recaudacion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author mvilches
 */
public class Archivo {
 
    public boolean escribeArchivo(String archivo, String texto)
    {
               try    
               {  //INSTRUCCCION DE QUE EL ARCHIVO SE EDITARA
                    FileWriter fw = new FileWriter(CreaArchivo(archivo));     
                    //LOS VALORES A ESCRIBIR EN EL ARCHIVO
                    BufferedWriter write = new BufferedWriter(fw);
                    write.write(texto);
                    write.close();
                    fw.close();
                    return true;
               }catch(IOException e)
               {
                   System.out.println("Error al escribir archivo \n" +e);
                   return false;
               }catch(Exception e)
               {
                   System.out.println("Error Inesperado al construir archivo \n" +e);
                   return false;
               }
    }
    public File CreaArchivo(String ruta)throws Exception 
    {        
        File archivoIn = new File(ruta);
        archivoIn.createNewFile();
        return archivoIn;        
    }  
}
