package rs.bane.alati.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import java.text.SimpleDateFormat;
import org.apache.poi.ss.usermodel.PrintSetup;
import static java.lang.Math.abs;
import org.apache.poi.hssf.usermodel.HSSFConditionalFormattingRule;
import org.apache.poi.hssf.usermodel.HSSFFontFormatting;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFSheetConditionalFormatting;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.extensions.XSSFHeaderFooter;
import rs.bane.alati.model.ucinci.dnevni.Production;

public class ExportDayXLS {

    private static void conFormating(HSSFSheet sheet) {
        HSSFSheetConditionalFormatting cf = sheet.getSheetConditionalFormatting();
        HSSFConditionalFormattingRule rule = cf.createConditionalFormattingRule(ComparisonOperator.EQUAL, "0");

        HSSFFontFormatting fill_pattern = rule.createFontFormatting();
        fill_pattern.setFontColorIndex(IndexedColors.WHITE.getIndex());

        CellRangeAddress[] regions = {CellRangeAddress.valueOf("G" + 1 + ":K" + 1000)};

        cf.addConditionalFormatting(regions, rule);
    }

    private static void modelirajSheet(HSSFSheet sheet) {
        sheet.setColumnWidth(0, 5 * 261);
        sheet.setColumnWidth(1, 24 * 261);
        sheet.setColumnWidth(2, 41 * 261);
        sheet.setColumnWidth(3, 32 * 261);
        sheet.setColumnWidth(4, 22 * 261);
        sheet.setColumnWidth(5, 23 * 261);
        sheet.setColumnWidth(6, 12 * 261);
        sheet.setColumnWidth(7, 9 * 261);
        sheet.setColumnWidth(8, 9 * 261);
        sheet.setColumnWidth(9, 10 * 261);
        sheet.setColumnWidth(10, 12 * 261);
        sheet.setColumnWidth(11, 16 * 261);
        sheet.setColumnWidth(12, 11 * 261);
        sheet.setZoom(60);
        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
        sheet.getPrintSetup().setLandscape(true);
        sheet.setAutobreaks(true);
        sheet.setFitToPage(true);
        sheet.getPrintSetup().setFitWidth((short) 1);
        sheet.getPrintSetup().setFitHeight((short) 0);
        sheet.setDefaultRowHeight((short) (20 * 25));
        sheet.setMargin(Sheet.BottomMargin, 0.5);
        sheet.setMargin(Sheet.TopMargin, 0.25);
        sheet.setMargin(Sheet.LeftMargin, 0.25);
        sheet.setMargin(Sheet.RightMargin, 0.25);
        sheet.getPrintSetup().setFooterMargin(0.25);
    }

    private static CellStyle modelirajCell(HSSFSheet sheet, String tip) {
        CellStyle cs = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 14);
        if (tip.equalsIgnoreCase("podaci")) {
            cs.setShrinkToFit(true);
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.DOUBLE);
            cs.setBorderRight(BorderStyle.DOUBLE);
        } else if (tip.equalsIgnoreCase("ime")) {
            cs.setWrapText(true);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cs.setVerticalAlignment(VerticalAlignment.CENTER);
            cs.setBorderBottom(BorderStyle.DOUBLE);
            cs.setBorderTop(BorderStyle.DOUBLE);
            cs.setBorderLeft(BorderStyle.DOUBLE);
            cs.setBorderRight(BorderStyle.DOUBLE);
        } else if (tip.equalsIgnoreCase("rbr")) {
            cs.setShrinkToFit(true);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cs.setVerticalAlignment(VerticalAlignment.CENTER);
            font.setFontHeightInPoints((short) 11);
            cs.setBorderBottom(BorderStyle.DOUBLE);
            cs.setBorderTop(BorderStyle.DOUBLE);
            cs.setBorderLeft(BorderStyle.DOUBLE);
            cs.setBorderRight(BorderStyle.DOUBLE);
        } else if (tip.equalsIgnoreCase("zaglavljeL")) {
            cs.setBorderBottom(BorderStyle.DOUBLE);
            cs.setBorderTop(BorderStyle.DOUBLE);
            cs.setBorderLeft(BorderStyle.DOUBLE);
            font.setBold(true);
        } else if (tip.equalsIgnoreCase("zaglavljeS")) {
            cs.setBorderBottom(BorderStyle.DOUBLE);
            cs.setBorderTop(BorderStyle.DOUBLE);
            font.setBold(true);
        } else if (tip.equalsIgnoreCase("zaglavljeD")) {
            cs.setBorderBottom(BorderStyle.DOUBLE);
            cs.setBorderTop(BorderStyle.DOUBLE);
            cs.setBorderRight(BorderStyle.DOUBLE);
            cs.setAlignment(HorizontalAlignment.RIGHT);
            font.setBold(true);
        } else {
            //throw new IllegalArgumentException();
        }
        cs.setFont(font);
        return cs;
    }

    public static boolean exportVrednosno(ArrayList<Production> prodList, String imeFajla) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(new SimpleDateFormat("dd").format(prodList.get(0).getDate()));
        modelirajSheet(sheet);
        CellStyle csP = modelirajCell(sheet, "podaci");
        CellStyle csI = modelirajCell(sheet, "ime");
        CellStyle csB = modelirajCell(sheet, "rbr");
        CellStyle csP10 = modelirajCell(sheet, "podaci");
        CellStyle csP11 = modelirajCell(sheet, "podaci");
        csP10.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
        csP11.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
        CellStyle csZL = modelirajCell(sheet, "zaglavljeL");
        CellStyle csZS = modelirajCell(sheet, "zaglavljeS");
        CellStyle csZD = modelirajCell(sheet, "zaglavljeD");

        int redniBroj = 1;
        int currRow = 7;
        // naslov
        CellStyle csNas = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        csNas.setFont(font);
        Cell nas = sheet.createRow(6).createCell(2);
        sheet.getRow(6).setHeight((short) (20 * 25));
        nas.setCellStyle(csNas);
        nas.setCellValue("DNEVNA EVIDENCIJA UÈINKA PROIZVODNJE ZA");
        nas = sheet.getRow(6).createCell(4);
        nas.setCellStyle(csNas);
        Production pNas = prodList.get(0);
        nas.setCellValue(new SimpleDateFormat("dd.MM.yyyy.").format(pNas.getDate()));
        //
        int pocetniRow = currRow;
        String pogon = "";
        for (Production p : prodList) {
            if (!p.getPogon().equalsIgnoreCase(pogon)) {
                currRow++;
                //odeljenje
                Row r = sheet.createRow(currRow++);
                Cell cc = r.createCell(0);
                r.setHeight((short) (20 * 25));
                cc.setCellStyle(csZL);
                cc.setCellValue("DNEVNA EVIDENCIJA UÈINAKA PROIZVODNJE U ODELJENJU " + p.getPogon().toUpperCase());
                for (int i = 1; i < 11; i++) {
                    cc = r.createCell(i);
                    cc.setCellStyle(csZS);
                    if (i == 10) {
                        cc.setCellValue("DATUM");
                        cc = r.createCell(++i);
                        cc.setCellStyle(csZD);
                        cc.setCellValue(new SimpleDateFormat("dd.MM.yyyy.").format(p.getDate()));
                    }
                }
                //ime kolona
                r = sheet.createRow(currRow++);
                r.setHeight((short) (20 * 36));
                cc = r.createCell(0);
                cc.setCellStyle(csI);
                cc.setCellValue("R. br");
                cc = r.createCell(1);
                cc.setCellStyle(csI);
                cc.setCellValue("Ime radnika");
                cc = r.createCell(2);
                cc.setCellStyle(csI);
                cc.setCellValue("Operacija");
                cc = r.createCell(3);
                cc.setCellStyle(csI);
                cc.setCellValue("Naziv proizvoda");
                cc = r.createCell(4);
                cc.setCellStyle(csI);
                cc.setCellValue("Šifra proizvoda");
                cc = r.createCell(5);
                cc.setCellStyle(csI);
                cc.setCellValue("Napomena");
                cc = r.createCell(6);
                cc.setCellStyle(csI);
                cc.setCellValue("Proizvedeno");
                cc = r.createCell(7);
                cc.setCellStyle(csI);
                cc.setCellValue("R.V. (h)");
                cc = r.createCell(8);
                cc.setCellStyle(csI);
                cc.setCellValue("Norma ostv.");
                cc = r.createCell(9);
                cc.setCellStyle(csI);
                cc.setCellValue("Norma pred.");
                cc = r.createCell(10);
                cc.setCellStyle(csI);
                cc.setCellValue("Cena po komadu");
                cc = r.createCell(11);
                cc.setCellStyle(csI);
                cc.setCellValue("Zarade");

                pocetniRow = currRow;
                redniBroj = 1;
            }
            pogon = p.getPogon();

            Production sledeci;
            if ((prodList.indexOf(p) + 1) < prodList.size()) {
                sledeci = prodList.get(prodList.indexOf(p) + 1);
            } else {
                sledeci = null;
            }

            int columnCount = 1;
            Row row = sheet.createRow(currRow);
            row.setHeight((short) (20 * 25));
            Cell cell;
            cell = row.createCell(0);
            cell.setCellStyle(csB);
            cell = row.createCell(1);
            cell.setCellStyle(csI);
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csP);
            cell.setCellValue(p.getWorkOrder().getTechOperationName());
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csP);
            cell.setCellValue(p.getWorkOrder().getProductName());
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csP);
            cell.setCellValue(p.getWorkOrder().getProductCatalogNumber());
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csP);
            cell.setCellValue(p.getNote());
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csP);
            cell.setCellValue(p.getGoodProductsMade());
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csP);
            cell.setCellValue(p.getWorkOnTime());
//            cell = row.createCell(++columnCount);
//            cell.setCellStyle(csP);
//            cell.setCellValue(p.getRezija());
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csP);
            cell.setCellFormula("ROUND(IF(H" + (currRow + 1) + "=0,0,G" + (currRow + 1) + "/H" + (currRow + 1) + "),2)");
//
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csP);
            cell.setCellValue(p.getWorkOrder().getTechOutturnPerHour());
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csP10);
            double cenaRada = 162;
            if (p.getWorker().getNameFull().equalsIgnoreCase("Nikola Sekuliæ")) {
                cenaRada = 160;
            }
            cell.setCellFormula("ROUND(IF(J" + (currRow + 1) + ">0," + cenaRada + "/J" + (currRow + 1) + ",0),3)");
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csP11);
            //cell.setCellFormula("ROUND((G" + (currRow + 1) + "*K" + (currRow + 1) + ")+(H" + (currRow + 1) + "*" + cenaRada + ")+(I" + (currRow + 1) + "*" + cenaRada + "),2)");
            cell.setCellFormula("ROUND((G" + (currRow + 1) + "*K" + (currRow + 1) + ")+IF(K" + (currRow + 1) + "=0,H" + (currRow + 1) + "*" + cenaRada + ",0),2)");

            if (sledeci == null || !p.getWorker().getNameFull().equals(sledeci.getWorker().getNameFull())) {
                if (abs(pocetniRow - currRow) > 0) {
                    sheet.addMergedRegion(new CellRangeAddress(pocetniRow, currRow, 0, 0));
                    sheet.addMergedRegion(new CellRangeAddress(pocetniRow, currRow, 1, 1));
                } else if (p.getWorker().getNameFull().length() >= 15) {
                    row.setHeight((short) (20 * 36));
                }
                Cell c = sheet.getRow(currRow).createCell(12);
                CellStyle csSum = csZD;
                c.setCellStyle(csSum);
                c.setCellFormula("ROUND(sum(L" + (pocetniRow + 1) + ":L" + (currRow + 1) + "), 0)");
                //mozda jos izvuci
                CellStyle lastRowStyle = modelirajCell(sheet, "podaci");
                lastRowStyle.setBorderBottom(BorderStyle.DOUBLE);
                CellStyle lastRowStyle2x0 = modelirajCell(sheet, "podaci");
                lastRowStyle2x0.setBorderBottom(BorderStyle.DOUBLE);
                lastRowStyle2x0.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
                CellStyle lastRowStyle3x0 = modelirajCell(sheet, "podaci");
                lastRowStyle3x0.setBorderBottom(BorderStyle.DOUBLE);
                lastRowStyle3x0.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
                for (int i = 2; i < 12; i++) {
                    if (i == 10) {
                        sheet.getRow(currRow).getCell(i).setCellStyle(lastRowStyle3x0);
                    } else if (i == 11) {
                        sheet.getRow(currRow).getCell(i).setCellStyle(lastRowStyle2x0);
                    } else {
                        sheet.getRow(currRow).getCell(i).setCellStyle(lastRowStyle);
                    }
                }
                //redni broj
                c = sheet.getRow(pocetniRow).createCell(0);
                c.setCellStyle(csB);
                c.setCellValue(redniBroj);
                //ime radnika
                c = sheet.getRow(pocetniRow).createCell(1);
                c.setCellStyle(csI);
                c.setCellValue(p.getWorker().getNameFull());
                pocetniRow = currRow + 1;
                redniBroj++;
            } else {

            }
            currRow++;
        }
        // dok sastavio
//        CellStyle cs = modelirajCell(sheet, "");
//        Row r = sheet.createRow(++currRow);
//        r.setHeight((short) (20 * 25));
//        Cell potpis = r.createCell(9);
//        potpis.setCellStyle(cs);
//        potpis.setCellValue("Izveštaj sastavio/la");
//        r = sheet.createRow(++currRow);
//        r.setHeight((short) (20 * 25));
//        potpis = r.createCell(10);
//        potpis.setCellStyle(cs);
//        potpis.setCellValue("");
        //
        // page numb
        sheet.getFooter().setCenter(HSSFFooter.font("Arial", "Bold")
                + HSSFFooter.fontSize((short) 15) + "Strana: " + HSSFFooter.page());
        //

        conFormating(sheet);
        try (FileOutputStream outputStream = new FileOutputStream(imeFajla + ".xls")) {
            workbook.write(outputStream);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ExportDayXLS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
