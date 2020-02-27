package com.global.recaudacion;

import com.global.util.File_Propiedades;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;

public class Ftp
{
    
    /**
     *Atributos de Clase
     **/
    private FTPClient ftp;
    Properties propiedades = new File_Propiedades().getProperties();
    private String usuario = propiedades.getProperty("userFTP");
    private String clave = propiedades.getProperty("passFTP");
    private String servidor = propiedades.getProperty("ipFTP");
    private Integer puerto = Integer.parseInt(propiedades.getProperty("porFTP"));
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public void setServidor(String servidor) {
        this.servidor = servidor;
    }
    
    public void setPuerto(Integer puerto) {
        this.puerto = puerto;
    }
    
    
    //---------------------------------------------------------------------
    
    /**
     * Método que establece la conexión con el ftp
     * @return boolean
     * @exception IOException
     */
    public boolean conectar(){
        boolean conect=true;
        int response;
        try {
            // Connect to an SFTP server on port 22
            ftp = new FTPClient();
            ftp.connect(servidor);
            ftp.login(usuario, clave);
            ftp.enterLocalPassiveMode();
            response = ftp.getReplyCode();
            System.out.println("Respuesta: "+response);
            
            if(FTPReply.isPositiveCompletion(response)==true)
            {
                System.out.println(ftp.getStatus());
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
            }else{
                System.out.println("ERROR DE FTP : "+response);
                conect = false;
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        return conect;
    }
    
    //-------------------------------------------------------------------
    /**
     * Método que cambia de directorio ,siendo este en el que queremos trabajar
     * @param String directorio
     * @return boolean
     * @exception IOException
     */
    public boolean cd(String directorio){
        try {
            ftp.changeWorkingDirectory(directorio);
            System.out.println(ftp.printWorkingDirectory());
            
            return true;
        } catch (IOException e) {
            System.out.println("FtpException ="+e.getMessage());
        }
        return false;
    }
    //-------------------------------------------------------------------
    /**
     * Método que crea un fichero en el sftp
     * @param String rutaFicheroSftp (ruta remota)
     * @param String rutaFichero (ruta local donde esta el fichero)
     * @return boolean
     * @exception IOException
     */
    public boolean crearFichero(String rutaFicheroSftp){
        try {
            ftp.makeDirectory(rutaFicheroSftp);
            return true;
        } catch (IOException e) {
            System.out.println("SftpException ="+e.getMessage());
        }
        return false;
    }
    //-------------------------------------------------------------------
    /**
     * Método que elimina un fichero del SFTP
     * @param String rutaFichero = ruta del fichero en el SFTP
     * @return boolean
     * @exception IOException
     */
    public boolean eliminarFichero(String rutaFichero){
        try {
            ftp.deleteFile(rutaFichero);
            return true;
        } catch (IOException e) {
            System.out.println("SftpException ="+e.getMessage());
        }
        return false;
    }
    //-----------------------------------------------------------------
    /**
     * Método que cierra la conexion con el FTP
     * @return boolean
     * @exception IOException
     */
    public boolean desconectar(){
        try{
        ftp.logout();
        ftp.disconnect();
        return true;
        }catch(IOException ex){
            System.out.println("Error al cerrar conexión!!");
            return false;
        }
    }
    //-----------------------------------------------------------------
    /**
     * Método que obtiene el directorio actual, comando pwd
     * @return String
     * @exception IOException
     */
    public String directorioActual(){
        String ruta = null;
        try {
            ruta = ftp.printWorkingDirectory();
        }catch (IOException e) {
            System.out.println("SftpException ="+e.getMessage());
        }
        return ruta;
    }
    //-----------------------------------------------------------------
    /**
     * Método que obtiene un fichero del SFTP y lo crea en un otro directorio local
     * @param String rutaFichero = ruta del fichero en el SFTP
     * @param String rutaLocal = ruta en donde se creara el fichero
     * @return boolean
     * @exception IOException
     */
    public boolean getFichero(String rutaFichero, String rutaLocal){
        boolean retorno=true;
        try {
            FileOutputStream fos = new FileOutputStream(rutaLocal);
            this.ftp.retrieveFile(rutaFichero, fos);
            fos.close();
            
        } catch (IOException e) {
            System.out.println("IOException ="+e.getMessage());
            retorno = false;
        }
        return retorno;
        
    }
    public boolean putFichero(String rutaLocal, String rutaDestino, String archivo){
        boolean retorno=true;
        try {
            File f = new File(rutaLocal+archivo);
            ftp.storeFile(rutaDestino+archivo, new FileInputStream(f));
            return retorno;
        } catch (IOException e) {
            System.out.println("IOException ="+e);
            retorno = false;
        }
        return retorno;
        
    }
    public String[] listarArchivos(){
        String[] archivos=null;
        try{
            archivos = ftp.listNames();
        }catch(Exception e)
        {
            System.out.println(e);
        }
        return archivos;
    }
    //-----------------------------------------------------------------
}