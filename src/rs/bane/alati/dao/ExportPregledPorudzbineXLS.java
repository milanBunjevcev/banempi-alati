package rs.bane.alati.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFConditionalFormattingRule;
import org.apache.poi.hssf.usermodel.HSSFFontFormatting;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSheetConditionalFormatting;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFRegionUtil;
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
import rs.bane.alati.model.analiza.proizvodnja.Nalog;
import rs.bane.alati.model.analiza.proizvodnja.Operacija;
import rs.bane.alati.model.analiza.proizvodnja.Porudzbina;

public class ExportPregledPorudzbineXLS {

    private static void modelirajSheet(HSSFSheet sheet) {
        sheet.setColumnWidth(0, 17 * 261);
        sheet.setColumnWidth(1, 20 * 261);
        sheet.setColumnWidth(2, 6 * 261);
        sheet.setColumnWidth(3, 23 * 261);
        sheet.setColumnWidth(4, 9 * 261);
        for (int i = 5; i < 5 + 31; i++) {
            sheet.setColumnWidth(i, 14 * 261);
        }

        sheet.setZoom(60);
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

    private static void modelirajSheetKulm(HSSFSheet sheet) {
        modelirajSheet(sheet);
        sheet.setColumnWidth(3, 50 * 261);
        sheet.setColumnWidth(4, 10 * 261);
        sheet.setColumnWidth(5, 10 * 261);
        sheet.setColumnWidth(6, 10 * 261);
        sheet.setColumnWidth(7, 10 * 261);
        sheet.setZoom(100);
        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
        sheet.getPrintSetup().setLandscape(false);
        sheet.setAutobreaks(true);
        sheet.setFitToPage(true);
        sheet.getPrintSetup().setFitWidth((short) 1);
        sheet.getPrintSetup().setFitHeight((short) 0);
        sheet.setDefaultRowHeight((short) (15 * 15));
        sheet.setMargin(Sheet.BottomMargin, 0.5);
        sheet.setMargin(Sheet.TopMargin, 0.25);
        sheet.setMargin(Sheet.LeftMargin, 0.25);
        sheet.setMargin(Sheet.RightMargin, 0.25);
        sheet.getPrintSetup().setFooterMargin(0.25);
    }

    private static CellStyle cellModel(HSSFSheet sheet, String tip) {
        CellStyle cs = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 13);
        if (tip.equalsIgnoreCase("kb i naziv")) {
            font.setFontHeightInPoints((short) 16);
            cs.setWrapText(true);
            cs.setAlignment(HorizontalAlignment.LEFT);
            cs.setVerticalAlignment(VerticalAlignment.TOP);
        } else if (tip.equalsIgnoreCase("operacije")) {
            cs.setAlignment(HorizontalAlignment.CENTER);
            cs.setVerticalAlignment(VerticalAlignment.CENTER);
            cs.setWrapText(true);
        } else if (tip.equalsIgnoreCase("podaci gornji")) {
            cs.setBorderBottom(BorderStyle.DASHED);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("podaci donji")) {
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.DASHED);
            cs.setBorderRight(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("podaci donji last")) {
            cs.setBorderBottom(BorderStyle.MEDIUM);
            cs.setBorderTop(BorderStyle.DASHED);
            cs.setBorderRight(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("obicno")) {
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            font = sheet.getWorkbook().createFont();
            font.setFontHeightInPoints((short) 10);
        } else {
            //throw new IllegalArgumentException();
        }
        cs.setFont(font);
        return cs;
    }

    private static CellStyle cellModelKulm(HSSFSheet sheet, String tip) {
        CellStyle cs = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 10);
        if (tip.equalsIgnoreCase("kb i naziv")) {
            //font.setFontHeightInPoints((short) 16);
            cs.setWrapText(true);
            cs.setAlignment(HorizontalAlignment.LEFT);
            cs.setVerticalAlignment(VerticalAlignment.TOP);
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            font.setBold(true);
        } else if (tip.equalsIgnoreCase("operacije")) {
            cs.setAlignment(HorizontalAlignment.CENTER);
            cs.setVerticalAlignment(VerticalAlignment.CENTER);
            cs.setWrapText(true);
        } else if (tip.equalsIgnoreCase("podaci donji last")) {
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

    private static void borderRegion(CellRangeAddress cra, boolean lastOp, HSSFWorkbook workbook, HSSFSheet sheet) {
        if (!lastOp) {
            HSSFRegionUtil.setBorderBottom(1, cra, sheet, workbook);
        } else {
            HSSFRegionUtil.setBorderBottom(2, cra, sheet, workbook);
        }
        HSSFRegionUtil.setBorderLeft(1, cra, sheet, workbook);
        HSSFRegionUtil.setBorderRight(1, cra, sheet, workbook);
        HSSFRegionUtil.setBorderTop(1, cra, sheet, workbook);
    }

    private static void conFormating(HSSFSheet sheet) {
        HSSFSheetConditionalFormatting cf = sheet.getSheetConditionalFormatting();
        HSSFConditionalFormattingRule rule = cf.createConditionalFormattingRule(ComparisonOperator.EQUAL, "0");

        HSSFFontFormatting fill_pattern = rule.createFontFormatting();
        fill_pattern.setFontColorIndex(IndexedColors.WHITE.getIndex());

        CellRangeAddress[] regions = {CellRangeAddress.valueOf("E" + 1 + ":AJ" + 1500)};

        cf.addConditionalFormatting(regions, rule);
    }

    public static boolean export(Porudzbina porudzbina, int mesec, int godina, String imeFajla, boolean blanko,
            Connection uConn) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("testSheet");
        modelirajSheet(sheet);
        CellStyle csKB = cellModel(sheet, "kb i naziv");
        CellStyle csOP = cellModel(sheet, "operacije");
        CellStyle csPG = cellModel(sheet, "podaci gornji");
        CellStyle csPD = cellModel(sheet, "podaci donji");
        CellStyle csPDL = cellModel(sheet, "podaci donji last");
        //pisanje
        ArrayList<Nalog> nalozi = porudzbina.getNalozi();
        int currRow = 0;
        int currColumn = 0;
        double defRowHeight = 28.25;
        for (Nalog nlg : nalozi) {
            Row row = sheet.createRow(currRow);
            row.setHeight((short) (20 * defRowHeight));
            Cell cell;
            //pocetni row
            int tempRow = currRow;
            //operacije            
            for (Operacija op : nlg.getOperacije()) {
                currColumn = 2;
                row = sheet.createRow(++currRow);
                row.setHeight((short) (20 * defRowHeight));
                //popunjava broj, nazivOp, kol_lan
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csOP);
                cell.setCellValue(op.getBroj());
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csOP);
                cell.setCellValue(op.getNaziv());
                cell = row.createCell(currColumn);
                cell.setCellStyle(csPG);
                cell.setCellValue(op.getKol_lan());
                //drugi red za kulmulativ
                row = sheet.createRow(++currRow);
                row.setHeight((short) (20 * defRowHeight));
                //mergovanje broj i naziv oper
                CellRangeAddress cra1 = new CellRangeAddress(currRow - 1, currRow, 2, 2);
                sheet.addMergedRegion(cra1);
                CellRangeAddress cra2 = new CellRangeAddress(currRow - 1, currRow, 3, 3);
                sheet.addMergedRegion(cra2);
                boolean lastOp = false;
                if (op.equals(nlg.getOperacije().get(nlg.getOperacije().size() - 1))) {
                    lastOp = true;
                }
                borderRegion(cra1, lastOp, workbook, sheet);
                borderRegion(cra2, lastOp, workbook, sheet);
                if (lastOp) {
                    row.createCell(currColumn).setCellStyle(csPDL);
                } else {
                    row.createCell(currColumn).setCellStyle(csPD);
                }
                //pocetno stanje
                if (!blanko) {
                    String datum1 = "2017/01/01";
                    String datum2 = "" + godina + "/" + mesec + "/0";
                    double n = nlg.proizvedeno(op.getBroj(), datum1, datum2, uConn);
                    row.getCell(currColumn).setCellValue(n);
                }
                //pravi polja po danima za popunjavanje, tu je popunjavanje podataka
                //optimizacija
                String datum1 = "" + godina + "/" + mesec + "/1";
                String datum2 = "" + godina + "/" + (mesec + 1) + "/0";
                double kulm = nlg.proizvedeno(op.getBroj(), datum1, datum2, uConn);
                boolean jos = true;//kraj optimizacije
                for (int i = 1; i <= 31; i++) {
                    sheet.getRow(currRow - 1).createCell(currColumn + i).setCellStyle(csPG);
                    //poziv za bas taj dan
                    double n = 0;
                    if (!blanko && jos) {
                        String datum = "" + godina + "/" + mesec + "/" + i;
                        n = nlg.proizvedeno(op.getBroj(), datum, datum, uConn);
                        if (n > 0) {
                            kulm -= n;//opt
                            sheet.getRow(currRow - 1).getCell(currColumn + i).setCellValue(n);
                        }
                        //opt
                        if (kulm == 0) {
                            jos = false;
                        }//kraj opt
                    }
                    //poziv za kulmulativ do tog dana                    
                    if (lastOp) {
                        row.createCell(currColumn + i).setCellStyle(csPDL);
                    } else {
                        row.createCell(currColumn + i).setCellStyle(csPD);
                    }
                    if (n > -1) {
                        String ch = row.getCell(currColumn + i).getAddress().formatAsString();
                        if (Character.isDigit(ch.charAt(1))) {
                            ch = ch.substring(0, 1);
                        } else {
                            ch = ch.substring(0, 2);
                        }
                        String sum = "$E" + (currRow + 1) + "+" + "SUM($F" + currRow + ":" + ch + currRow + ")";
                        String sumIf = "IF(" + ch + currRow + "<>0," + sum + ",0)";
                        row.getCell(currColumn + i).setCellFormula(sumIf);
                    }
                }
            }
            //popunjava kat_br, naziv, brrn
            row = sheet.getRow(tempRow);
            cell = row.createCell(0);
            cell.setCellStyle(csKB);
            cell.setCellValue(nlg.getKat_broj());
            cell = row.createCell(1);
            cell.setCellStyle(csKB);
            cell.setCellValue(nlg.getNaziv());
            cell = row.createCell(2);
            cell.setCellValue(nlg.getBrrn());
            //merguje kat_br i naziv
            CellRangeAddress cra1 = new CellRangeAddress(tempRow, currRow, 0, 0);
            sheet.addMergedRegion(cra1);
            CellRangeAddress cra2 = new CellRangeAddress(tempRow, currRow, 1, 1);
            sheet.addMergedRegion(cra2);
            borderRegion(cra1, true, workbook, sheet);
            borderRegion(cra2, true, workbook, sheet);
            //zaglavlje dani
            for (int i = 1; i <= 31; i++) {
                cell = sheet.getRow(tempRow).createCell(currColumn + i);
                cell.setCellStyle(csOP);
                cell.setCellValue(i);
            }

            currRow++;
        }
        // header
        //sheet.getHeader().setCenter();
        // footer - page numb
        sheet.getFooter().setCenter(HSSFFooter.font("Arial", "Bold")
                + HSSFFooter.fontSize((short) 15) + "Strana: " + HSSFFooter.page());
        //        
        conFormating(sheet);
        String folder = "popunjeno";
        if (blanko) {
            folder = "blanko";
        }
        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try (FileOutputStream outputStream = new FileOutputStream(folder + "/" + imeFajla + ".xls")) {
            workbook.write(outputStream);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(rs.bane.alati.dao.ExportDayXLS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private static int insertNaslov(HSSFSheet sheet, int currRow, CellStyle csN, int godina, double defRowHeight) {
        // cell
        Cell cell = sheet.createRow(currRow).createCell(0);
        sheet.getRow(currRow).setHeight((short) (20 * defRowHeight));
        cell.setCellStyle(csN);
        cell.setCellValue("REALIZACIJA PLANA PROIZVODNJE ZA " + godina + ". GODINU");
        sheet.addMergedRegion(new CellRangeAddress(currRow, currRow, 0, 8));
        // podnaslov
        currRow++;
        cell = sheet.createRow(currRow).createCell(0);
        sheet.getRow(currRow).setHeight((short) (20 * defRowHeight));
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.setCellValue("-zakljuèno sa datumom " + "-");
        sheet.addMergedRegion(new CellRangeAddress(currRow, currRow, 0, 8));
        // zaglavlje
        currRow += 5;
        return currRow;
    }

    public static boolean exportKulm(Porudzbina porudzbina, int mesec, int godina, String imeFajla, boolean blanko,
            Connection uConn) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("testSheet");
        modelirajSheetKulm(sheet);
        CellStyle csKB = cellModelKulm(sheet, "kb i naziv");
        CellStyle csOP = cellModelKulm(sheet, "operacije");
        CellStyle csPDL = cellModelKulm(sheet, "podaci donji last");
        CellStyle csO = cellModelKulm(sheet, "obicno");
        //pisanje
        ArrayList<Nalog> nalozi = porudzbina.getNalozi();
        int currRow = 0;
        int currColumn = 0;
        double defRowHeight = 15;

        currRow = insertNaslov(sheet, currRow, cellModelKulm(sheet, "naslov"), godina, defRowHeight);

        for (Nalog nlg : nalozi) {
            Row row = sheet.createRow(currRow);
            row.setHeight((short) (20 * defRowHeight));
            Cell cell;
            //pocetni row
            int tempRow = currRow;
            //operacije            
            for (Operacija op : nlg.getOperacije()) {
                currColumn = 2;
                row = sheet.createRow(++currRow);
                row.setHeight((short) (20 * defRowHeight));
                //popunjava broj, nazivOp, kol_lan
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csO);
                cell.setCellValue(op.getBroj());
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csO);
                cell.setCellValue(op.getNaziv());
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csO);
                cell.setCellValue(op.getKol_lan());

                boolean lastOp = false;
                if (op.equals(nlg.getOperacije().get(nlg.getOperacije().size() - 1))) {
                    lastOp = true;
                }

                if (lastOp) {
                    //row.createCell(currColumn).setCellStyle(csPDL);
                }
                //pocetno stanje
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csO);
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csO);
                if (!blanko) {
                    String datum1 = "2017/01/01";
                    String datum2 = "" + (godina + 1) + "/" + (mesec) + "/0";
                    double n = nlg.proizvedeno(op.getBroj(), datum1, datum2, uConn);
                    cell.setCellValue(n);
                }
                cell = row.createCell(currColumn++);
                cell.setCellStyle(csO);
                if (!blanko) {
                    cell.setCellFormula("E" + (currRow + 1) + "-F" + (currRow + 1) + "-G" + (currRow + 1));
                }

            }
            //popunjava kat_br, naziv, brrn
            row = sheet.getRow(tempRow);
            cell = row.createCell(0);
            cell.setCellStyle(csKB);
            cell.setCellValue(nlg.getKat_broj());
            cell = row.createCell(1);
            cell.setCellStyle(csKB);
            cell.setCellValue(nlg.getNaziv());
            cell = row.createCell(2);
            cell.setCellValue(nlg.getBrrn());
            //merguje kat_br i naziv
            CellRangeAddress cra1 = new CellRangeAddress(tempRow, currRow, 0, 0);
            sheet.addMergedRegion(cra1);
            CellRangeAddress cra2 = new CellRangeAddress(tempRow, currRow, 1, 1);
            sheet.addMergedRegion(cra2);
            borderRegion(cra1, false, workbook, sheet);
            borderRegion(cra2, false, workbook, sheet);

            cell = row.createCell(4);
            cell.setCellStyle(csKB);
            cell.setCellValue("Lansirano");
            cell = row.createCell(5);
            cell.setCellStyle(csKB);
            cell.setCellValue("Popis");
            cell = row.createCell(6);
            cell.setCellStyle(csKB);
            cell.setCellValue("Uraðeno");
            cell = row.createCell(7);
            cell.setCellStyle(csKB);
            cell.setCellValue("Razlika");

            currRow++;
        }
        // header
        //sheet.getHeader().setCenter();
        // footer - page numb
        sheet.getFooter().setCenter(HSSFFooter.font("Arial", "Bold")
                + HSSFFooter.fontSize((short) 15) + "Strana: " + HSSFFooter.page());
        //        
        conFormating(sheet);
        String folder = "popunjeno";
        if (blanko) {
            folder = "blanko";
        }
        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try (FileOutputStream outputStream = new FileOutputStream(folder + "/" + imeFajla + ".xls")) {
            workbook.write(outputStream);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(rs.bane.alati.dao.ExportDayXLS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
