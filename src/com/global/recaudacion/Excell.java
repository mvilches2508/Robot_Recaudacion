package com.global.recaudacion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author mvilches
 */
public class Excell {

    public boolean escribirExcell(String archivo, Object[][] datos,String nomHoja) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(nomHoja);
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

        int rowNum = 0;
//        System.out.println("creando excel ...");

        for (Object[] dato : datos) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : dato) {                
                
                
               if (field instanceof String) {
                   Cell cell = row.createCell(colNum++);
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    Cell cell = row.createCell(colNum++);
                    cell.setCellValue((Integer) field);
                } else if(field instanceof Date){
                    Cell cell = row.createCell(colNum++);
                    cell.setCellValue((Date) field);
                    cell.setCellStyle(cellStyle);
                }
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(archivo);
            workbook.write(outputStream);            
            workbook.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("excell creado!!");
        return true;
    }
    public ArrayList<ArrayList> leerExcell(String archivo){
        ArrayList<ArrayList> nomina = new ArrayList<>();
        try {
            
            FileInputStream excelFile = new FileInputStream(new File(archivo));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            
            Iterator<Row> iterator = datatypeSheet.iterator();

            while (iterator.hasNext()) {
                ArrayList<Object> filas = new ArrayList<>();
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();

                while (cellIterator.hasNext()) {

                    Cell currentCell = cellIterator.next();
                  
                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        filas.add(currentCell.getStringCellValue());
//                        System.out.print(currentCell.getStringCellValue() + "--");
                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
//                        System.out.print(currentCell.getNumericCellValue() + "--");
                        filas.add(new BigDecimal(currentCell.getNumericCellValue()));
                    }
                   
                }
                 nomina.add(filas);
//                System.out.println();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nomina;
    }
}
