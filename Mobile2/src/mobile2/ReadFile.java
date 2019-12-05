/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile2;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;

public class ReadFile {
    
    private ArrayList<Invention> inventionList = new ArrayList();
    
    public ReadFile() {
        String nombreArchivo = "EXCEL1.xlsx";
        String rutaArchivo = "C:\\Users\\juang\\Desktop\\GuessWhat\\" + nombreArchivo;
        
        try (FileInputStream file = new FileInputStream(new File(rutaArchivo))) {
            // leer archivo excel
            XSSFWorkbook worbook = new XSSFWorkbook(file);
            //obtener la hoja que se va leer
            XSSFSheet sheet = worbook.getSheetAt(0);
            //obtener todas las filas de la hoja excel
            Iterator<Row> rowIterator = sheet.iterator();
            
            Row row;
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Invention invent = new Invention();
                row = rowIterator.next();
                //se obtiene las celdas por fila
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell cell;
                cell = cellIterator.next();
                invent.setId((int) cell.getNumericCellValue());
                cell = cellIterator.next();
                invent.setYear((int) cell.getNumericCellValue());
                cell = cellIterator.next();
                invent.setInvention(cell.getStringCellValue());
                cell = cellIterator.next();
                invent.setPerson(cell.getStringCellValue());
                cell = cellIterator.next();
                invent.setCountry(cell.getStringCellValue());
                cell = cellIterator.next();
                invent.setHint(cell.getStringCellValue());
                inventionList.add(invent);
                
            }
           // Collections.sort(inventionList, new YearSort());
        } catch (Exception e) {
            e.getMessage();
        }
        
    }
    
    public boolean readCorrectly() {
        return (!inventionList.isEmpty());
    }
    
    public ArrayList<Invention> getInventionList() {
        return inventionList;
    }
    
    public void setInventionList(ArrayList<Invention> inventionList) {
        this.inventionList = inventionList;
    }
    
}
