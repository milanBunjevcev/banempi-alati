package rs.bane.alati.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFConditionalFormattingRule;
import org.apache.poi.xssf.usermodel.XSSFFontFormatting;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheetConditionalFormatting;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import rs.bane.alati.model.analiza.norme.DnevniUcinakRadnik;
import rs.bane.alati.model.analiza.norme.OperacijaModel;

public class ExportNormeXLSX {

    private static void modelirajSheet(XSSFSheet sheet) {
        sheet.setColumnWidth(0, 12 * 261);
        sheet.setColumnWidth(1, 20 * 261);
        sheet.setColumnWidth(2, 23 * 261);
        sheet.setColumnWidth(3, 19 * 261);
        sheet.setColumnWidth(4, 23 * 261);
        sheet.setColumnWidth(5, 9 * 261);
        sheet.setColumnWidth(6, 9 * 261);
        sheet.setColumnWidth(7, 9 * 261);
        sheet.setColumnWidth(8, 9 * 261);

        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
        sheet.getPrintSetup().setLandscape(true);
        sheet.setAutobreaks(true);
        sheet.setFitToPage(true);
        sheet.getPrintSetup().setFitWidth((short) 1);
        sheet.getPrintSetup().setFitHeight((short) 0);
        sheet.setDefaultRowHeight((short) (28.25 * 15));
        sheet.setMargin(Sheet.BottomMargin, 0.5);
        sheet.setMargin(Sheet.TopMargin, 0.25);
        sheet.setMargin(Sheet.LeftMargin, 0.25);
        sheet.setMargin(Sheet.RightMargin, 0.25);
        sheet.getPrintSetup().setFooterMargin(0.25);
    }

    private static CellStyle cellModel(XSSFSheet sheet, String tip) {
        CellStyle cs = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 11);
        if (tip.equalsIgnoreCase("zaglavlje")) {
            cs.setWrapText(true);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cs.setVerticalAlignment(VerticalAlignment.CENTER);
            cs.setBorderBottom(BorderStyle.MEDIUM);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("obicno last")) {
            cs.setBorderBottom(BorderStyle.MEDIUM);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("obicno")) {
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("naslov")) {
            cs.setAlignment(HorizontalAlignment.CENTER);
            font.setBold(true);
            font.setFontHeightInPoints((short) 12);
        } else {
            //throw new IllegalArgumentException();
        }
        cs.setFont(font);
        return cs;
    }

    private static void conFormating(XSSFSheet sheet) {
        XSSFSheetConditionalFormatting cf = sheet.getSheetConditionalFormatting();
        XSSFConditionalFormattingRule rule = cf.createConditionalFormattingRule(ComparisonOperator.EQUAL, "0");

        XSSFFontFormatting fill_pattern = rule.createFontFormatting();
        fill_pattern.setFontColorIndex(IndexedColors.WHITE.getIndex());

        CellRangeAddress[] regions = {CellRangeAddress.valueOf("E" + 1 + ":AJ" + 1500)};

        cf.addConditionalFormatting(regions, rule);
    }

    private static int insertNaslov(XSSFSheet sheet, int currRow, CellStyle csN, CellStyle csZ, String artikal, String operacija) {
        // cell
        Cell cell = sheet.createRow(currRow).createCell(0);
        sheet.getRow(currRow).setHeight((short) (20 * 30));
        cell.setCellStyle(csN);
        cell.setCellValue("PREGLED OSTVARENJA NORMI NA: " + artikal + " - " + " " + operacija);
        sheet.addMergedRegion(new CellRangeAddress(currRow, currRow, 0, 8));
        // zaglavlje    
        Row row = sheet.createRow(currRow + 2);
        sheet.getRow(currRow).setHeight((short) (20 * 60));
        cell = row.createCell(0);
        cell.setCellStyle(csZ);
        cell.setCellValue("DATUM");
        cell = row.createCell(1);
        cell.setCellStyle(csZ);
        cell.setCellValue("RADNIK");
        cell = row.createCell(2);
        cell.setCellStyle(csZ);
        cell.setCellValue("ARTIKAL");
        cell = row.createCell(3);
        cell.setCellStyle(csZ);
        cell.setCellValue("KAT. BR.");
        cell = row.createCell(4);
        cell.setCellStyle(csZ);
        cell.setCellValue("OPERACIJA");
        cell = row.createCell(5);
        cell.setCellStyle(csZ);
        cell.setCellValue("PROIZVEDENO (kom)");
        cell = row.createCell(6);
        cell.setCellStyle(csZ);
        cell.setCellValue("R.V. (h)");
        cell = row.createCell(7);
        cell.setCellStyle(csZ);
        cell.setCellValue("OSTVARENA NORMA (kom / h");
        cell = row.createCell(8);
        cell.setCellStyle(csZ);
        cell.setCellValue("PREDVIÐENA NORMA (kom / h)");

        return currRow + 3;
    }

    public static boolean export(ArrayList<DnevniUcinakRadnik> list, String artikal, String oper,
            Connection uConn) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(oper);
        modelirajSheet(sheet);

        CellStyle csOL = cellModel(sheet, "obicno last");
        CellStyle csO = cellModel(sheet, "obicno");
        CellStyle csZ = cellModel(sheet, "zaglavlje");

        //pisanje        
        int currRow = 0;
        int currColumn = 0;
        double defRowHeight = 15;

        currRow = insertNaslov(sheet, currRow, cellModel(sheet, "naslov"), cellModel(sheet, "zaglavlje"), artikal, oper);

        for (DnevniUcinakRadnik dur : list) {
            Row row = sheet.createRow(currRow);
            row.setHeight((short) (20 * defRowHeight));
            Cell cell;
            //pocetni row
            int tempRow = currRow;
            //operacije            
            for (OperacijaModel op : dur.getOperacije()) {
                boolean lastOp = false;
                if (op.equals(dur.getOperacije().get(dur.getOperacije().size() - 1))) {
                    lastOp = true;
                }
                currColumn = 2;
                row = sheet.createRow(currRow);
                row.setHeight((short) (20 * defRowHeight));
                //popunjava broj, nazivOp, kol_lan
                CellStyle csOX = null;
                if (lastOp) {
                    csOX = csOL;
                } else {
                    csOX = csO;
                }
                cell = row.createCell(0);
                cell.setCellStyle(csOX);
                cell = row.createCell(1);
                cell.setCellStyle(csOX);

                cell = row.createCell(currColumn++);
                cell.setCellStyle(csOX);
                cell.setCellValue(op.getNazivArtikla());
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csOX);
                cell.setCellValue(op.getKatBroj());
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csOX);
                cell.setCellValue(op.getOperacija());
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csOX);
                cell.setCellValue(op.getProizvedeno());
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csOX);
                cell.setCellValue(op.getRv());
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csOX);
                cell.setCellFormula("round(iferror(F" + (currRow + 1) + "/G" + (currRow + 1) + ",0),2)");
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csOX);
                cell.setCellValue(op.getNormaPred());

                if (lastOp) {
                    //row.createCell(currColumn).setCellStyle(csPDL);
                } else {
                    currRow++;
                }

            }
            //popunjava kat_br, naziv, brrn
            row = sheet.getRow(tempRow);
            cell = row.createCell(0);
            cell.setCellStyle(csOL);
            cell.setCellValue(dur.getDatum());
            cell = row.createCell(1);
            cell.setCellStyle(csOL);
            cell.setCellValue(dur.getRadnik());
            //merguje kat_br i naziv
            if (currRow - tempRow >= 1) {
                CellRangeAddress cra1 = new CellRangeAddress(tempRow, currRow, 0, 0);
                sheet.addMergedRegion(cra1);
                CellRangeAddress cra2 = new CellRangeAddress(tempRow, currRow, 1, 1);
                sheet.addMergedRegion(cra2);
                //borderRegion(cra1, false, workbook, sheet);
                //borderRegion(cra2, false, workbook, sheet);
            }
            currRow++;
        }
            //analiza
            int lastRow = currRow;
            currRow += 2;
            Row row = sheet.createRow(currRow);
            Cell cell = row.createCell(1);
            cell.setCellValue("Minimalna ostvarena norma:");
            cell = row.createCell(3);
            cell.setCellFormula("min($h$4:$h$" + lastRow + ")");
            currRow += 2;
            row = sheet.createRow(currRow);
            cell = row.createCell(1);
            cell.setCellValue("Maximalna ostvarena norma:");
            cell = row.createCell(3);
            cell.setCellFormula("max($h$4:$h$" + lastRow + ")");
            currRow += 2;
            row = sheet.createRow(currRow);
            cell = row.createCell(1);
            cell.setCellValue("Proseèna ostvarena norma:");
            cell = row.createCell(3);
            cell.setCellFormula("average($h$4:$h$" + lastRow + ")");
            currRow += 2;
            row = sheet.createRow(currRow);
            cell = row.createCell(1);
            cell.setCellValue("Broj uèinaka ispod proseka:");
            cell = row.createCell(3);
            cell.setCellFormula("countif($h$4:$h$" + lastRow + ",\"<\"&D" + (currRow - 1) + ")");
            currRow += 2;
            row = sheet.createRow(currRow);
            cell = row.createCell(1);
            cell.setCellValue("Broj uèinaka iznad proseka:");
            cell = row.createCell(3);
            cell.setCellFormula("countif($h$4:$h$" + lastRow + ",\">=\"&D" + (currRow - 3) + ")");
            currRow += 2;
            row = sheet.createRow(currRow);
            cell = row.createCell(1);
            cell.setCellValue("Prosek uèinaka iznad proseka:");
            cell = row.createCell(3);
            cell.setCellFormula("sumif($h$4:$h$" + lastRow + ",\">=\"&D" + (currRow - 5) + ",$h$4:$h$" + lastRow + ")/D" + (currRow - 1));
            currRow += 2;
            row = sheet.createRow(currRow);
            cell = row.createCell(1);
            cell.setCellValue("Aktuelna norma:");
            row = sheet.createRow(++currRow);
            cell = row.createCell(1);
            cell.setCellValue("Predlog norme:");
            // analiza
        // header
        //sheet.getHeader().setCenter();
        // footer - page numb
        sheet.getFooter().setCenter(HSSFFooter.font("Arial", "Bold")
                + HSSFFooter.fontSize((short) 15) + "Strana: " + HSSFFooter.page());
        //        
        conFormating(sheet);
        String folder = "norme";

        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try (FileOutputStream outputStream = new FileOutputStream(folder + "/" + artikal + ".xlsx")) {
            workbook.write(outputStream);
            return true;
        } catch (IOException ex) {

        }
        return false;
    }

    public static boolean export2(ArrayList<ArrayList<DnevniUcinakRadnik>> lista, String artikal, String folder) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        int i = 0;

        for (ArrayList<DnevniUcinakRadnik> list : lista) {
            String oper = "" + (++i);
            XSSFSheet sheet = workbook.createSheet(oper);
            modelirajSheet(sheet);

            CellStyle csOL = cellModel(sheet, "obicno last");
            CellStyle csO = cellModel(sheet, "obicno");
            CellStyle csZ = cellModel(sheet, "zaglavlje");

            //pisanje        
            int currRow = 0;
            int currColumn = 0;
            double defRowHeight = 15;

            currRow = insertNaslov(sheet, currRow, cellModel(sheet, "naslov"), cellModel(sheet, "zaglavlje"), artikal, oper);

            for (DnevniUcinakRadnik dur : list) {
                Row row = sheet.createRow(currRow);
                row.setHeight((short) (20 * defRowHeight));
                Cell cell;
                //pocetni row
                int tempRow = currRow;
                //operacije            
                for (OperacijaModel op : dur.getOperacije()) {
                    boolean lastOp = false;
                    if (op.equals(dur.getOperacije().get(dur.getOperacije().size() - 1))) {
                        lastOp = true;
                    }
                    currColumn = 2;
                    row = sheet.createRow(currRow);
                    row.setHeight((short) (20 * defRowHeight));
                    //popunjava broj, nazivOp, kol_lan
                    CellStyle csOX = null;
                    if (lastOp) {
                        csOX = csOL;
                    } else {
                        csOX = csO;
                    }
                    cell = row.createCell(0);
                    cell.setCellStyle(csOX);
                    cell = row.createCell(1);
                    cell.setCellStyle(csOX);

                    cell = row.createCell(currColumn++);
                    cell.setCellStyle(csOX);
                    cell.setCellValue(op.getNazivArtikla());
                    cell = row.createCell(currColumn++);
                    cell.setCellStyle(csOX);
                    cell.setCellValue(op.getKatBroj());
                    cell = row.createCell(currColumn++);
                    cell.setCellStyle(csOX);
                    cell.setCellValue(op.getOperacija());
                    cell = row.createCell(currColumn++);
                    cell.setCellStyle(csOX);
                    cell.setCellValue(op.getProizvedeno());
                    cell = row.createCell(currColumn++);
                    cell.setCellStyle(csOX);
                    cell.setCellValue(op.getRv());
                    cell = row.createCell(currColumn++);
                    cell.setCellStyle(csOX);
                    cell.setCellFormula("round(iferror(F" + (currRow + 1) + "/G" + (currRow + 1) + ",0),2)");
                    cell = row.createCell(currColumn++);
                    cell.setCellStyle(csOX);
                    cell.setCellValue(op.getNormaPred());

                    if (lastOp) {
                        //row.createCell(currColumn).setCellStyle(csPDL);
                    } else {
                        currRow++;
                    }

                }
                //popunjava kat_br, naziv, brrn
                row = sheet.getRow(tempRow);
                cell = row.createCell(0);
                cell.setCellStyle(csOL);
                cell.setCellValue(dur.getDatum());
                cell = row.createCell(1);
                cell.setCellStyle(csOL);
                cell.setCellValue(dur.getRadnik());
                //merguje kat_br i naziv
                if (currRow - tempRow >= 1) {
                    CellRangeAddress cra1 = new CellRangeAddress(tempRow, currRow, 0, 0);
                    sheet.addMergedRegion(cra1);
                    CellRangeAddress cra2 = new CellRangeAddress(tempRow, currRow, 1, 1);
                    sheet.addMergedRegion(cra2);
                    //borderRegion(cra1, false, workbook, sheet);
                    //borderRegion(cra2, false, workbook, sheet);
                }
                currRow++;
            }
            //analiza
            int lastRow = currRow;
            currRow += 2;
            Row row = sheet.createRow(currRow);
            Cell cell = row.createCell(1);
            cell.setCellValue("Minimalna ostvarena norma:");
            cell = row.createCell(3);
            cell.setCellFormula("min($h$4:$h$" + lastRow + ")");
            currRow += 2;
            row = sheet.createRow(currRow);
            cell = row.createCell(1);
            cell.setCellValue("Maximalna ostvarena norma:");
            cell = row.createCell(3);
            cell.setCellFormula("max($h$4:$h$" + lastRow + ")");
            currRow += 2;
            row = sheet.createRow(currRow);
            cell = row.createCell(1);
            cell.setCellValue("Proseèna ostvarena norma:");
            cell = row.createCell(3);
            cell.setCellFormula("average($h$4:$h$" + lastRow + ")");
            currRow += 2;
            row = sheet.createRow(currRow);
            cell = row.createCell(1);
            cell.setCellValue("Broj uèinaka ispod proseka:");
            cell = row.createCell(3);
            cell.setCellFormula("countif($h$4:$h$" + lastRow + ",\"<\"&D" + (currRow - 1) + ")");
            currRow += 2;
            row = sheet.createRow(currRow);
            cell = row.createCell(1);
            cell.setCellValue("Broj uèinaka iznad proseka:");
            cell = row.createCell(3);
            cell.setCellFormula("countif($h$4:$h$" + lastRow + ",\">=\"&D" + (currRow - 3) + ")");
            currRow += 2;
            row = sheet.createRow(currRow);
            cell = row.createCell(1);
            cell.setCellValue("Prosek uèinaka iznad proseka:");
            cell = row.createCell(3);
            cell.setCellFormula("sumif($h$4:$h$" + lastRow + ",\">=\"&D" + (currRow - 5) + ",$h$4:$h$" + lastRow + ")/D" + (currRow - 1));
            currRow += 2;
            row = sheet.createRow(currRow);
            cell = row.createCell(1);
            cell.setCellValue("Aktuelna norma:");
            row = sheet.createRow(++currRow);
            cell = row.createCell(1);
            cell.setCellValue("Predlog norme:");
            // analiza
            // header  
            //sheet.getHeader().setCenter();
            // footer - page numb
            sheet.getFooter().setCenter(HSSFFooter.font("Arial", "Bold")
                    + HSSFFooter.fontSize((short) 15) + "Strana: " + HSSFFooter.page());
            //        
            conFormating(sheet);
        }

        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try (FileOutputStream outputStream = new FileOutputStream(folder + "/" + artikal + ".xlsx")) {
            workbook.write(outputStream);
            return true;
        } catch (IOException ex) {

        }
        return false;
    }

}
