
package com.global.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class File_Propiedades 
{
 
  public Properties getProperties() 
  {
        try
        {
            Properties propiedades = new Properties();
//            File arc_prop = new File("C:\\Global\\config.properties");
//            FileInputStream fis = new FileInputStream(arc_prop);
//            propiedades.load(fis);
            propiedades.load(getClass().getResourceAsStream("config.properties"));
            //propiedades.load( getClass().getResourceAsStream("C:\\Global\\config.properties") );
            if (!propiedades.isEmpty()) 
            {                
                return propiedades;
            } else {
                return null;
            }
        } catch (IOException ex) {
            return null;
        }
   }
}