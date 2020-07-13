package rs.bane.alati.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFConditionalFormattingRule;
import org.apache.poi.hssf.usermodel.HSSFFontFormatting;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSheetConditionalFormatting;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.apache.poi.ss.util.CellRangeAddress;
import rs.bane.alati.model.popis.Stavka;
import rs.bane.alati.model.sastavnice.Sastavnica;

public class ExportPopisXLS {

    private static short defRowH = (short) (20 * 15);

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
        sheet.setColumnWidth(1, 9 * 261);
        sheet.setColumnWidth(2, 15 * 261);
        sheet.setColumnWidth(3, 25 * 261);
        sheet.setColumnWidth(4, 32 * 261);
        sheet.setColumnWidth(5, 10 * 261);
        sheet.setColumnWidth(6, 7 * 261);
        sheet.setColumnWidth(7, 8 * 261);
        sheet.setColumnWidth(8, 10 * 261);
        sheet.setColumnWidth(9, 15 * 261);
        sheet.setZoom(100);
        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
        sheet.setAutobreaks(true);
        sheet.setFitToPage(true);
        sheet.getPrintSetup().setFitWidth((short) 1);
        sheet.getPrintSetup().setFitHeight((short) 0);
        sheet.setDefaultRowHeight(defRowH);
        sheet.setMargin(Sheet.BottomMargin, 0.5);
        sheet.setMargin(Sheet.TopMargin, 0.25);
        sheet.setMargin(Sheet.LeftMargin, 0.5);
        sheet.setMargin(Sheet.RightMargin, 0.25);
        sheet.getPrintSetup().setFooterMargin(0.25);
    }

    private static CellStyle modelirajCell(HSSFWorkbook workbook, String tip) {
        CellStyle cs = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        if (tip.equalsIgnoreCase("podaci")) {
            font.setFontHeightInPoints((short) 10);
            cs.setShrinkToFit(true);
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("zaglavlje")) {
            cs.setShrinkToFit(true);
            font.setBold(true);
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("naslov")) {
            cs.setAlignment(HorizontalAlignment.CENTER);
            font.setBold(true);
            font.setFontHeightInPoints((short) 12);
        } else if (tip.equalsIgnoreCase("brojevi")) {
            font.setFontHeightInPoints((short) 10);
            cs.setShrinkToFit(true);
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
            final short format00 = workbook.createDataFormat().getFormat("#,##0.000");
            cs.setDataFormat(format00);
        } else {
            //throw new IllegalArgumentException();
        }
        cs.setFont(font);
        return cs;
    }

    private static HSSFSheet createSheet(String sheetName, ArrayList<Stavka> lista,
            HSSFWorkbook workbook, CellStyle csP, CellStyle csN, CellStyle csZ, CellStyle csB) {
        HSSFSheet sheet = workbook.createSheet(sheetName);
        modelirajSheet(sheet);

        int godina = lista.get(0).godina;
        String lokacija = sheetName;
        int redniBroj = 0;
        int currRow = 3;
        currRow = insertNaslov(sheet, currRow, csN, csZ, godina, lokacija);
        int firstRowSum = currRow + 1;
        for (Stavka s : lista) {
            ++currRow;
            ++redniBroj;
            Row row = sheet.createRow(currRow);
            row.setHeight(defRowH);
            int currCol = -1;
            //
            Cell cell = row.createCell(++currCol);
            cell.setCellStyle(csP);
            cell.setCellValue(redniBroj);
            //
            cell = row.createCell(++currCol);
            cell.setCellStyle(csP);
            cell.setCellValue(s.ident);
            //
            cell = row.createCell(++currCol);
            cell.setCellStyle(csP);
            cell.setCellValue(s.katBroj);
            //
            cell = row.createCell(++currCol);
            cell.setCellStyle(csP);
            cell.setCellValue(s.naziv);
            //
            cell = row.createCell(++currCol);
            cell.setCellStyle(csP);
            cell.setCellValue(s.poslednjaOp);
            //
            cell = row.createCell(++currCol);
            cell.setCellStyle(csP);
            cell.setCellValue((s.dorada ? "da" : "ne"));
            //
            cell = row.createCell(++currCol);
            cell.setCellStyle(csP);
            cell.setCellValue(s.jm);
            //
            cell = row.createCell(++currCol);
            cell.setCellStyle(csB);
            cell.setCellValue(s.cena);
            //
            cell = row.createCell(++currCol);
            cell.setCellStyle(csB);
            cell.setCellValue(s.kolicina);
            //
            cell = row.createCell(++currCol);
            cell.setCellStyle(csB);
            String x = row.getCell(currCol - 2).getAddress().formatAsString();
            String y = row.getCell(currCol - 1).getAddress().formatAsString();
            cell.setCellFormula(x + "*" + y);
            //
        }
        currRow++;
        Row row = sheet.createRow(currRow);
        row.setHeight(defRowH);
        Cell cell = row.createCell(9);
        cell.setCellStyle(csB);
        String x2 = sheet.getRow(firstRowSum).getCell(9).getAddress().formatAsString();
        String y2 = sheet.getRow(currRow - 1).getCell(9).getAddress().formatAsString();
        cell.setCellFormula("SUM(" + x2 + ":" + y2 + ")");
        //

        //
        addFooter(sheet);
        //conFormating(sheet);
        return sheet;
    }

    private static HSSFSheet createSheetCene(HSSFWorkbook workbook, CellStyle csP, CellStyle csN, CellStyle csZ) {
        HSSFSheet sheet = workbook.createSheet("SIROVINE BEZ CENE");
        sheet.setColumnWidth(0, 5 * 261);
        sheet.setColumnWidth(1, 9 * 261);
        sheet.setColumnWidth(2, 15 * 261);
        sheet.setColumnWidth(3, 32 * 261);
        sheet.setZoom(100);
        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
        sheet.setAutobreaks(true);
        sheet.setFitToPage(true);
        sheet.getPrintSetup().setFitWidth((short) 1);
        sheet.getPrintSetup().setFitHeight((short) 0);
        sheet.setDefaultRowHeight(defRowH);
        sheet.setMargin(Sheet.BottomMargin, 0.5);
        sheet.setMargin(Sheet.TopMargin, 0.25);
        sheet.setMargin(Sheet.LeftMargin, 0.5);
        sheet.setMargin(Sheet.RightMargin, 0.25);
        sheet.getPrintSetup().setFooterMargin(0.25);

        int redniBroj = 0;
        int currRow = 3;
        String[] zaglavlja = {"R.BR.", "IDENT", "KAT BROJ", "NAZIV", "DIMENZIJA"};
        Row row = sheet.createRow(currRow);
        for (int currCol = 0; currCol < zaglavlja.length; currCol++) {
            Cell cell = row.createCell(currCol);
            cell.setCellStyle(csZ);
            cell.setCellValue(zaglavlja[currCol]);
        }
        for (Sastavnica s : Sastavnica.getSirovineBezCene()) {
            ++currRow;
            ++redniBroj;
            row = sheet.createRow(currRow);
            row.setHeight(defRowH);
            int currCol = -1;
            //
            Cell cell = row.createCell(++currCol);
            cell.setCellStyle(csP);
            cell.setCellValue(redniBroj);
            //
            cell = row.createCell(++currCol);
            cell.setCellStyle(csP);
            cell.setCellValue(s.getIdent());
            //
            cell = row.createCell(++currCol);
            cell.setCellStyle(csP);
            cell.setCellValue(s.getKatBroj());
            //
            cell = row.createCell(++currCol);
            cell.setCellStyle(csP);
            cell.setCellValue(s.getNaziv());
            //
            cell = row.createCell(++currCol);
            cell.setCellStyle(csP);
            cell.setCellValue(s.getDimenzija());
        }

        addFooter(sheet);
        //conFormating(sheet);
        return sheet;
    }

    private static int insertNaslov(HSSFSheet sheet, int currRow, CellStyle csN, CellStyle csZ,
            int godina, String lokacija) {
        // cell
        Cell cell = sheet.createRow(currRow).createCell(0);
        sheet.getRow(currRow).setHeight(defRowH);
        cell.setCellStyle(csN);
        cell.setCellValue("LISTA ARTIKALA SA POPISA " + godina + ". GODINE");
        sheet.addMergedRegion(new CellRangeAddress(currRow, currRow, 0, 9));
        // podnaslov
        currRow++;
        cell = sheet.createRow(currRow).createCell(0);
        sheet.getRow(currRow).setHeight(defRowH);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.setCellValue("-" + lokacija + "-");
        sheet.addMergedRegion(new CellRangeAddress(currRow, currRow, 0, 9));
        // zaglavlje
        currRow += 3;
        Row row = sheet.createRow(currRow);
        row.setHeight(defRowH);
        String[] zaglavlja = {"R.BR.", "IDENT", "KAT BROJ", "NAZIV", "OPERACIJA", "ZA DORADU", "J.M.", "CENA", "KOL.", "VREDNOST"};
        for (int currCol = 0; currCol < zaglavlja.length; currCol++) {
            cell = row.createCell(currCol);
            cell.setCellStyle(csZ);
            cell.setCellValue(zaglavlja[currCol]);
        }
        return currRow;
    }

    public static boolean exportPopis(ArrayList<ArrayList<Stavka>> liste, String imeFajla, File fajl) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        CellStyle csP = modelirajCell(workbook, "podaci");
        CellStyle csN = modelirajCell(workbook, "naslov");
        CellStyle csZ = modelirajCell(workbook, "zaglavlje");
        CellStyle csB = modelirajCell(workbook, "brojevi");
        for (ArrayList<Stavka> lista : liste) {
            if (!lista.isEmpty()) {
                String lokacija = lista.get(0).lokacija;
                HSSFSheet sheet = createSheet(lokacija, lista, workbook, csP, csN, csZ, csB);
            }
        }
        //sirovine bez cene
        HashSet<Sastavnica> sirovineBezCene = Sastavnica.getSirovineBezCene();
        if (!sirovineBezCene.isEmpty()) {
            HSSFSheet sheet = createSheetCene(workbook, csP, csN, csZ);
        }
        //
        try {
            workbook.write(fajl);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ExportPopisXLS.class.getName()).log(Level.SEVERE, null, ex);
        }
//        try (FileOutputStream outputStream = new FileOutputStream(imeFajla + ".xls")) {
//            workbook.write(outputStream);
//            return true;
//        } catch (IOException ex) {
//            Logger.getLogger(ExportPopisXLS.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return false;
    }

    private static void addFooter(HSSFSheet sheet) {
        // page numb
        sheet.getFooter().setCenter(HSSFFooter.font("Arial", "Bold")
                + HSSFFooter.fontSize((short) 11) + "Strana: " + HSSFFooter.page());
    }

}
