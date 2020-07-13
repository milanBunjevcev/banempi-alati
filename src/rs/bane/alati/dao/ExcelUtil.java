package rs.bane.alati.dao;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

    public static void copyXLSXSheet(XSSFWorkbook srcWorkbook, XSSFWorkbook destWorkbook,
            XSSFSheet srcSheet, XSSFSheet destSheet) {
        FormulaEvaluator evaluator = srcWorkbook.getCreationHelper().createFormulaEvaluator();
        for (Iterator<Row> rit = srcSheet.rowIterator(); rit.hasNext();) {
            Row row = rit.next();
            int r = row.getRowNum();
            Row destRow = destSheet.createRow(r);
            for (Iterator<Cell> cit = row.cellIterator(); cit.hasNext();) {
                Cell cell = cit.next();
                int c = cell.getColumnIndex();
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            destRow.createCell(c).setCellValue(cell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            destRow.createCell(c).setCellValue(cell.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            destRow.createCell(c).setCellValue(cell.toString());
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            destRow.createCell(c).setCellValue("");
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            destRow.createCell(c).setCellValue(cell.getErrorCellValue());
                            break;

                        // CELL_TYPE_FORMULA will never occur
                        case Cell.CELL_TYPE_FORMULA:
                            destRow.createCell(c).setCellValue(cell.getNumericCellValue());
                            break;
                    }
                }
            }
        }
        if (destWorkbook.getNumberOfNames() == 0 && srcSheet.getSheetName().equalsIgnoreCase("podaci")) {
            for (XSSFName nameS : srcWorkbook.getAllNames()) {
                if (!nameS.getNameName().startsWith("_")) {
                    XSSFName nameD = destWorkbook.createName();
                    nameD.setNameName(nameS.getNameName());
                    nameD.setRefersToFormula(nameS.getRefersToFormula());
                }
            }
        }
    }

    /**
     * This method for the type of data in the cell, extracts the data and
     * returns it as a string.
     */
    public static String getCellValueAsString(Cell cell) {
        String strCellValue = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    strCellValue = cell.toString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "dd/MM/yyyy");
                        strCellValue = dateFormat.format(cell.getDateCellValue());
                    } else {
                        Double value = cell.getNumericCellValue();
                        Long longValue = value.longValue();
                        strCellValue = new String(longValue.toString());
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    strCellValue = new String(new Boolean(
                            cell.getBooleanCellValue()).toString());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    strCellValue = "";
                    break;
            }
        }
        return strCellValue;
    }
}
