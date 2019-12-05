import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.HashMap;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Final2 {
	
	static String[][] table = new String [6][101];
	
	public static void final2() {
	
	XSSFRow row;
	XSSFCell cell;
		
	try {
		FileInputStream inputStream = new FileInputStream("D:\\servejin\\Invention Open Source.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		int sheetCn = workbook.getNumberOfSheets();
			for(int cn = 0; cn < sheetCn; cn++){
			XSSFSheet sheet = workbook.getSheetAt(cn);			
			int rows = sheet.getPhysicalNumberOfRows();			
			
			int cells = sheet.getRow(cn).getPhysicalNumberOfCells(); //
			for (int r = 1; r < rows; r++) {
				row = sheet.getRow(r); 
				if (row != null) {
					for (int c = 0; c < cells; c++) {
						cell = row.getCell(c);
						if (cell != null) {
							String value = null;						
							
							switch (cell.getCellType()) {
							case XSSFCell.CELL_TYPE_FORMULA:
								value = "" + cell.getCellFormula();
								break;
							case XSSFCell.CELL_TYPE_NUMERIC:
								value = "" + cell.getNumericCellValue();
								break;
							case XSSFCell.CELL_TYPE_STRING:
								value = "" + cell.getStringCellValue();
								break;
							case XSSFCell.CELL_TYPE_BLANK:
								value = "" +"[blank]";
								break;
							case XSSFCell.CELL_TYPE_ERROR:
								value = "" + cell.getErrorCellValue();
								break;
							default:
							}
							table[c][r] = value;
						} 
						else {
						}
					} 
				}
			} 
		}
		} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
}
