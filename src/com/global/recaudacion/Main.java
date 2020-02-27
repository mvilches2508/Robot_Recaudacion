package com.global.recaudacion;

import com.global.util.File_Propiedades;
import java.awt.AWTException;
import java.util.Properties;

/**
 *
 * @author mvilches
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, AWTException {

        /**
         * Configuraciones*
         */
        System.out.println("Configurando ....");
        Properties propiedades = new File_Propiedades().getProperties();
        ProcesadorArcRecaudacion procesador = new ProcesadorArcRecaudacion();
        Ftp ftp = new Ftp();
        Excell excell = new Excell();
        String archivoBanco = propiedades.getProperty("archivoBanco");
        String rutaArchivo = propiedades.getProperty("rutaArchivo");
        String rutaFTP = propiedades.getProperty("rutaFTP");
        String arcNominaCargo = propiedades.getProperty("arcNominaCargo");
        Object[][] datatypes = null;
       String archivo = null;
       System.out.println("Fin Configuraciones ....");
        /**
         * subir archivo universo banco*
         */
     /*   try {
            System.out.println(procesador.getFechaEjecucion()+" Subiendo archivo banco en bruto - FTP");
            ftp.conectar();
            ftp.cd(rutaFTP);
            ftp.putFichero(rutaArchivo, rutaFTP, archivoBanco);
            System.out.println(procesador.getFechaEjecucion()+" archivo banco en bruto cargado - FTP");
       } catch (Exception ex) {
            System.err.println(procesador.getFechaEjecucion()+" Error al subir archivo banco bruto - FTP: " + ex);
        }*/
        /**
         * PROCESADOR DE ARCHIVO UNIVERSO**
         */
        try {
            System.out.println(procesador.getFechaEjecucion()+" Procesando Universo");
            String ARC_BANCO = rutaArchivo + archivoBanco;
            procesador.leeArchivo(ARC_BANCO);
            procesador.limpiezaRegistros();
            datatypes = procesador.getRegistros();
            System.out.println(procesador.getFechaEjecucion()+" Fin procesamiento de Universo");
        } catch (Exception ex) {
            System.err.println(procesador.getFechaEjecucion()+" Error al procesar universo : " + ex);
       }
        /**
         * Escritura en excell del universo limpio*
         */
        try {
            System.out.println(procesador.getFechaEjecucion()+" Creando excell con el universo");
            archivo = "UniversoLimpio" + procesador.getFechaEjecucion() + ".xlsx";
            String universoArchivo = rutaArchivo + archivo;
            excell.escribirExcell(universoArchivo, datatypes, "UNIVERSO");
            System.out.println(procesador.getFechaEjecucion()+" archivo " + universoArchivo + " creado");
        } catch (Exception ex) {
            System.err.println(procesador.getFechaEjecucion()+" Error al crear universo en excell: " + ex);
        }

        /**
         * Subir universo limpio*
         */
        try {
            System.out.println(procesador.getFechaEjecucion()+" guardando respaldo de universo excell procesado");
            ftp.putFichero(rutaArchivo, rutaFTP, archivo);
            ftp.desconectar();
            System.out.println(procesador.getFechaEjecucion()+" respaldo cargado");
        } catch (Exception ex) {
            System.err.println(procesador.getFechaEjecucion()+" Error al guardar respaldo excell del universo: " + ex);
        }
        /**
         * Lectorua y armado de nóminas*
         */
//        String archivoNomina = "NominaPACCarga" + procesador.getFechaEjecucion() + ".xlsx";
//        excell.escribirExcell(rutaArchivo + archivoNomina, procesador.getRegistrosNomina(excell.leerExcell(rutaArchivo + arcNominaCargo)), "Hoja1");
//        String baseUrl = "https://54.226.52.175/";
//        //            System.setProperty("webdriver.gecko.driver", "C:\\drivers\\geckodriver.exe");    //Para efecto de este ejemplo se utilizará FIREFOX            
//        //            FirefoxDriver driver = new FirefoxDriver();
//// ArrayList<String> cliArgsCap = new ArrayList<String>();
////    DesiredCapabilities capabilities  = DesiredCapabilities.phantomjs();
////
////    cliArgsCap.add ("--web-security=false");
////    cliArgsCap.add ("--ssl-protocol=any");
////    cliArgsCap.add ( "--ignore-ssl-errors=true");
////    capabilities.setCapability ("takesScreenshot", true);
////    capabilities.setCapability (PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
////    capabilities.setCapability (PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[] { "--logLevel=2" }
////    );
////             System.setProperty("phantomjs.binary.path", "C:\\drivers\\phantomjs.exe"); //ejemplo entornos de servidores sin GUI
////    PhantomJSDriver driver  = new PhantomJSDriver(capabilities);
//
////         
//        ChromeOptions options = new ChromeOptions();
//        Map<String, Object> prefs = new HashMap<String, Object>();
//        prefs.put("profile.default_content_setting_values.plugins", 1);
//        prefs.put("profile.content_settings.plugin_whitelist.adobe-flash-player", 1);
//        prefs.put("profile.content_settings.exceptions.plugins.*,*.per_resource.adobe-flash-player", 1);
//        // Enable Flash for this site
//        prefs.put("PluginsAllowedForUrls", baseUrl);
//        options.setExperimentalOption("prefs", prefs);
//        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver.exe"); //ejemplo entornos de servidores sin GUI
//        ChromeDriver driver = new ChromeDriver(options);
//
//        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
//
//        driver.get(baseUrl + "carteratest/seclogin.aspx");
//        // driver.switchTo().frame(driver.findElement(By.name("body")));
//        WebDriverWait wait = new WebDriverWait(driver, 15);
//
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("vUSUARIONOMBRE"))).sendKeys("robotest");
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("vUSUARIOPASSWORD"))).sendKeys("robotest");
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("BTNENTER"))).click();
////            driver.findElement(By.id("vUSUARIONOMBRE")).sendKeys("robotest");
////            driver.findElement(By.name("vUSUARIOPASSWORD")).sendKeys("robotest");
////            driver.findElement(By.name("BTNENTER")).click();
//Robot robot =  new Robot();
//        driver.get(baseUrl
//                + "carteratest/subiruniversopac.aspx?1");
//        //  driver.findElement(By.cssSelector("a[title=\"Subir Universo\"]")).click();
//        Thread.sleep(10000);
//        robot.keyPress(KeyEvent.VK_F5);
//        robot.keyRelease(KeyEvent.VK_F5);
//        Thread.sleep(60000);
//        driver.findElement(By.id("UPLOADIFY1ContainerFile")).sendKeys("C:\\Global\\UniversoLimpio2018-07-16151819.xlsx");
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("UPLOADIFY1ContainerFileUploader"))).click();
//        Thread.sleep(15000);
//        Teclado teclado = new Teclado();
//        teclado.setTexto("UniversoLimpio2018-07-16151819.xlsx");
//        teclado.teclearTexto();
//        
//        robot.keyPress(KeyEvent.VK_ENTER);
//       robot.keyRelease(KeyEvent.VK_ENTER);
//        Thread.sleep(600000);
//      
//            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//            FileUtils.copyFile(screenshot, new File("C:\\Global\\login.jpg"));
        //driver.close();
    }

}

