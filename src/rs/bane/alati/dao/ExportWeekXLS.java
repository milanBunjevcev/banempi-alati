package rs.bane.alati.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.Collections;
import java.util.Comparator;
import org.apache.poi.ss.usermodel.PrintSetup;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.hssf.usermodel.HSSFConditionalFormattingRule;
import org.apache.poi.hssf.usermodel.HSSFFontFormatting;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFSheetConditionalFormatting;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import rs.bane.alati.model.ucinci.dnevni.Production;

public class ExportWeekXLS {

    private static void conFormating(HSSFSheet sheet) {
        HSSFSheetConditionalFormatting cf = sheet.getSheetConditionalFormatting();
        HSSFConditionalFormattingRule rule = cf.createConditionalFormattingRule(ComparisonOperator.EQUAL, "0");

        HSSFFontFormatting fill_pattern = rule.createFontFormatting();
        fill_pattern.setFontColorIndex(IndexedColors.WHITE.getIndex());

        CellRangeAddress[] regions = {CellRangeAddress.valueOf("F" + 1 + ":I" + 1000)};

        cf.addConditionalFormatting(regions, rule);
    }

    private static void modelirajSheet(HSSFSheet sheet) {
        sheet.setColumnWidth(0, 5 * 261);
        sheet.setColumnWidth(1, 8 * 261);
        sheet.setColumnWidth(2, 41 * 261);
        sheet.setColumnWidth(3, 32 * 261);
        sheet.setColumnWidth(4, 22 * 261);
        sheet.setColumnWidth(5, 10 * 261);
        sheet.setColumnWidth(6, 7 * 261);
        sheet.setColumnWidth(7, 7 * 261);
        sheet.setColumnWidth(8, 9 * 261);
        sheet.setColumnWidth(9, 10 * 261);
        sheet.setColumnWidth(10, 12 * 261);
        sheet.setColumnWidth(11, 16 * 261);
        sheet.setColumnWidth(12, 11 * 261);
        sheet.setZoom(80);
        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
        //sheet.getPrintSetup().setLandscape(true);
        sheet.setAutobreaks(true);
        sheet.setFitToPage(true);
        sheet.getPrintSetup().setFitWidth((short) 1);
        sheet.getPrintSetup().setFitHeight((short) 0);
        sheet.setDefaultRowHeight((short) (20 * 15));
        sheet.setMargin(Sheet.BottomMargin, 0.5);
        sheet.setMargin(Sheet.TopMargin, 0.25);
        sheet.setMargin(Sheet.LeftMargin, 0.75);
        sheet.setMargin(Sheet.RightMargin, 0.25);
        sheet.getPrintSetup().setFooterMargin(0.25);
    }

    private static CellStyle modelirajCell(HSSFSheet sheet, String tip) {
        CellStyle cs = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 11);
        if (tip.equalsIgnoreCase("podaci")) {
            cs.setShrinkToFit(true);
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("podaciBold")) {
            cs.setShrinkToFit(true);
            font.setBold(true);
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("maloZaglavlje")) {
            cs.setWrapText(true);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cs.setVerticalAlignment(VerticalAlignment.CENTER);
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("zaglavljeL")) {
            font.setFontHeightInPoints((short) 14);
            font.setBold(true);
        } else {
            //throw new IllegalArgumentException();
        }
        cs.setFont(font);
        return cs;
    }

    public static boolean exportVrednosno(ArrayList<Production> prodList,
            Date date1, Date date2,
            String imeFajla) {
        //        
        prodList = podeli(prodList);
        //
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(new SimpleDateFormat("dd").format(prodList.get(0).getDate()));
        modelirajSheet(sheet);
        CellStyle csPodaci = modelirajCell(sheet, "podaci");
        CellStyle csSum = modelirajCell(sheet, "podaciBold");
        CellStyle csMaloZaglavlje = modelirajCell(sheet, "maloZaglavlje");
        CellStyle csP000 = modelirajCell(sheet, "podaci");
        CellStyle csP00 = modelirajCell(sheet, "podaci");
        csP000.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
        final short format00 = workbook.createDataFormat().getFormat("0.00");
        csP00.setDataFormat(format00);
        csSum.setDataFormat(format00);
        CellStyle csVelikoZaglavlje = modelirajCell(sheet, "zaglavljeL");

        int redniBroj = 1;
        int currRow = 8;
        int pocetniRow = currRow;
        String pogon = "";
        String pretRadnik = "";
        String datumFirst = "";
        int pocetniZarada = 0;
        //naslov
        naslov:
        {
            Row row = sheet.createRow(currRow++);
            Cell cell = row.createCell(2);
            row.setHeight((short) (20 * 25));
            cell.setCellStyle(csVelikoZaglavlje);
            cell.setCellValue("NEDELJNA EVIDENCIJA UÈINKA PROIZVODNJE");
            cell = sheet.createRow(currRow++).createCell(6);
            cell.setCellStyle(csVelikoZaglavlje);
            cell.setCellValue("Za period: " + new SimpleDateFormat("dd.MM.").format(date1) + " - "
                    + new SimpleDateFormat("dd.MM.").format(date2));
            currRow++;
        }
        // kraj naslova
        for (Production p : prodList) {
            // ispisati radnika OVDE
            if (!p.getWorker().getNameFull().equalsIgnoreCase(pretRadnik)) {
                Row row = sheet.createRow(currRow++);
                Cell cell = row.createCell(0);
                Font font = sheet.getWorkbook().createFont();
                font.setFontHeightInPoints((short) 11);
                cell.getCellStyle().setFont(font);
                cell.setCellValue("Radnik: " + p.getWorker().getNameFull());
                //ime kolona
                row = sheet.createRow(currRow++);
                row.setHeight((short) (20 * 35));
                //
                cell = row.createCell(0);
                cell.setCellStyle(csMaloZaglavlje);
                cell.setCellValue("R. br");
                //
                cell = row.createCell(1);
                cell.setCellStyle(csMaloZaglavlje);
                cell.setCellValue("Datum");
                //
                cell = row.createCell(2);
                cell.setCellStyle(csMaloZaglavlje);
                cell.setCellValue("Operacija");
                //
                cell = row.createCell(3);
                cell.setCellStyle(csMaloZaglavlje);
                cell.setCellValue("Proizvod");
                //
                cell = row.createCell(4);
                cell.setCellStyle(csMaloZaglavlje);
                cell.setCellValue("Kat broj");
                //
                cell = row.createCell(5);
                cell.setCellStyle(csMaloZaglavlje);
                cell.setCellValue("Proizvedeno");
                //
                cell = row.createCell(6);
                cell.setCellStyle(csMaloZaglavlje);
                cell.setCellValue("Norma");
                //
                cell = row.createCell(7);
                cell.setCellStyle(csMaloZaglavlje);
                cell.setCellValue("RV (h)");
                //
                cell = row.createCell(8);
                cell.setCellStyle(csMaloZaglavlje);
                cell.setCellValue("Cena");
                //
                cell = row.createCell(9);
                cell.setCellStyle(csMaloZaglavlje);
                cell.setCellValue("Iznos");
                //
                cell = row.createCell(10);
                cell.setCellStyle(csMaloZaglavlje);
                cell.setCellValue("Po danu");

                pocetniRow = currRow;
                pocetniZarada = currRow;
                redniBroj = 1;
                datumFirst = "";
            }
            pretRadnik = p.getWorker().getNameFull();
            // grupisanje datuma \/
            String datumCurr = new SimpleDateFormat("dd.MM.").format(p.getDate());
            if (datumCurr.equalsIgnoreCase(datumFirst)) {
                datumCurr = "";
            } else {
                datumFirst = datumCurr;
            }
            // grupisanje datuma /\            

            Production sledeci;
            if ((prodList.indexOf(p) + 1) < prodList.size()) {
                sledeci = prodList.get(prodList.indexOf(p) + 1);
            } else {
                sledeci = null;
            }

            int columnCount = 0;
            Row row = sheet.createRow(currRow);
            row.setHeight((short) (20 * 15));
            Cell cell;
            //
            cell = row.createCell(0);
            cell.setCellStyle(csMaloZaglavlje);
            cell.setCellValue(redniBroj++);
            //            
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csPodaci);
            cell.setCellValue(datumCurr);
            //            
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csPodaci);
            cell.setCellValue(p.getWorkOrder().getTechOperationName());
            //
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csPodaci);
            cell.setCellValue(p.getWorkOrder().getProductName());
            //
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csPodaci);
            cell.setCellValue(p.getWorkOrder().getProductCatalogNumber());
            //
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csPodaci);
            cell.setCellValue(p.getGoodProductsMade());
            //
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csPodaci);
            cell.setCellValue(p.getWorkOrder().getTechOutturnPerHour());
            //
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csPodaci);
            cell.setCellValue(p.getWorkOnTime());
            //
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csP000);
            double cenaRada = 162;
            if (p.getWorker().getNameFull().equalsIgnoreCase("Nikola Sekuliæ")) {
                cenaRada = 160;
            }
            cell.setCellFormula("ROUND(IF(G" + (currRow + 1) + ">0," + cenaRada + "/G" + (currRow + 1) + ",0),3)");
            //
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csP00);
            cell.setCellFormula("ROUND(F" + (currRow + 1) + "*I" + (currRow + 1) + "+"
                    + "IF(I" + (currRow + 1) + "=0,H" + (currRow + 1) + "*" + cenaRada + ",0), 3)");

            // treba uvesti pracenje da li ima vise stavki za jedan dan
            cell = row.createCell(++columnCount);
            cell.setCellStyle(csP00);
            // grupisanje zarada \/
            boolean ok = true;
            if (sledeci != null) {
                if (sledeci.getWorker().getNameFull().equalsIgnoreCase(p.getWorker().getNameFull())) {
                    String dS = new SimpleDateFormat("dd.MM.").format(sledeci.getDate());
                    String dP = new SimpleDateFormat("dd.MM.").format(p.getDate());
                    if (dS.equalsIgnoreCase(dP)) {
                        ok = false;
                    }
                }
            }
            if (ok) {
                cell.setCellFormula("ROUND(SUM(J" + (pocetniZarada + 1) + ":J" + (currRow + 1) + "), 0)");
                pocetniZarada = currRow + 1;
            }
            // grupisanje zarada /\

            if (sledeci == null || !p.getWorker().getNameFull().equals(sledeci.getWorker().getNameFull())) {
                // ovde treba ubaciti proveru yza grupisanje datuma i zarade
                Row r = sheet.createRow(++currRow);
                Cell c = r.createCell(9);
                c.setCellStyle(csSum);
                c.setCellFormula("ROUND(sum(J" + (pocetniRow + 1) + ":J" + (currRow) + "), 2)");

                pocetniRow = currRow + 1;
                redniBroj++;
            } else {

            }
            currRow++;
        }

        // dok sastavio
//        currRow += 2;
//        CellStyle cs = modelirajCell(sheet, "");
//        Row r = sheet.createRow(++currRow);
//        r.setHeight((short) (20 * 25));
//        Cell potpis = r.createCell(8);
//        potpis.setCellStyle(cs);
//        potpis.setCellValue("Izveštaj sastavio/la");
//        r = sheet.createRow(++currRow);
//        r.setHeight((short) (20 * 25));
//        potpis = r.createCell(9);
//        potpis.setCellStyle(cs);
//        potpis.setCellValue("Milan Bunjevèev");
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
            Logger.getLogger(ExportWeekXLS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private static ArrayList<Production> podeli(ArrayList<Production> prodList) {
        ArrayList<Production> novaList = new ArrayList();
        for (Production p : prodList) {
            String ime = p.getWorker().getNameFull();
            if (ime.contains(",")) {
                String[] imena = ime.split(",");
                for (String s : imena) {
                    Production np = new Production(p.getWorkOrder().getWorkOrderID(), p.getWorkOrder().getTechOperationID(), p.getWorkOrder().getTechOperationName(),
                            p.getWorkOrder().getTechOutturnPerHour(), p.getWorkOrder().getProductName(), p.getWorkOrder().getProductCatalogNumber(),
                            s.trim(), p.getDate(), p.getNote(), p.getGoodProductsMade() / imena.length, p.getWorkOnTime() / imena.length, p.getRezija() / imena.length,
                            p.getEarnedSalary() / imena.length, p.getId(), p.getProductWorkPrice(), p.getPogon());
                    novaList.add(np);
                }
            } else {
                novaList.add(p);
            }
        }
        novaList.sort(new ImePaDatumComparator());
        return novaList;
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

class ImePaDatumComparator implements Comparator<Production> {

    @Override
    public int compare(Production p1, Production p2) {
        int poImenu = p1.getWorker().getNameFull().compareToIgnoreCase(p2.getWorker().getNameFull());
        if (poImenu < 0) {
            return poImenu;
        } else if (poImenu == 0) {
            return p1.getDate().compareTo(p2.getDate());
        } else {
            return poImenu;
        }
    }
}
