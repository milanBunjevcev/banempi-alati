package rs.bane.alati.dao;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.poi.ss.formula.FormulaParseException;
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
import org.apache.poi.xssf.usermodel.XSSFPatternFormatting;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheetConditionalFormatting;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import rs.bane.alati.swinggui.meni.Prozori;

public class PrisustvoXLSX {
    
    private static void conFormatingDani(XSSFSheet sheet, int lastRow, String lastColl) {
        XSSFSheetConditionalFormatting cf = sheet.getSheetConditionalFormatting();
        XSSFConditionalFormattingRule rule1
                = cf.createConditionalFormattingRule("AND(j5<8,OR(j$4=\"PET\",j$4=\"ÈET\",j$4=\"SRE\","
                        + "j$4=\"UTO\",j$4=\"PON\"))");
        
        XSSFFontFormatting fill_pattern = rule1.createFontFormatting();
        XSSFPatternFormatting x = rule1.createPatternFormatting();
        x.setFillBackgroundColor(IndexedColors.RED.getIndex());
        x.setFillPattern(CellStyle.SOLID_FOREGROUND);
        
        CellRangeAddress[] regions = {CellRangeAddress.valueOf("J" + 5 + ":" + lastColl + lastRow)};
        
        cf.addConditionalFormatting(regions, rule1);
    }
    
    private static void conFormatingUslovi(XSSFSheet sheet, int lastRow, String lastColl) {
        XSSFSheetConditionalFormatting cf = sheet.getSheetConditionalFormatting();
        XSSFConditionalFormattingRule rule1
                = cf.createConditionalFormattingRule("$C5<=0");
        
        XSSFFontFormatting fill_pattern = rule1.createFontFormatting();
        XSSFPatternFormatting x = rule1.createPatternFormatting();
        x.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        x.setFillPattern(CellStyle.SOLID_FOREGROUND);
        
        CellRangeAddress[] regions = {CellRangeAddress.valueOf("A" + 5 + ":I" + lastRow)};
        
        cf.addConditionalFormatting(regions, rule1);
    }
    
    public static boolean export(int mesec, int godina, Component parent) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        boolean barJedna = ucitajRV(godina, mesec, parent, workbook);
        if (true) {
            //
            String sheetName = "crnci";
            createSheet(workbook, sheetName, godina, mesec);
            sheetName = "ugovorci";
            createSheet(workbook, sheetName, godina, mesec);
            //
            workbook.setActiveSheet(workbook.getNumberOfSheets() - 1);
            for (int i = 0; i < workbook.getNumberOfSheets() - 2; i++) {
                workbook.setSheetHidden(i, true);
            }
            return printWB(mesec, godina, workbook);
        } else {
            return false;
        }
        
    }
    
    private static void createSheet(XSSFWorkbook workbook, String sheetName, int godina, int mesec) throws FormulaParseException {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        modelirajSheet(sheet);
        int brDana = brDana(godina, mesec);
        Row row = sheet.createRow(0);
        Row rowDan = sheet.createRow(1);
        Cell cell = row.createCell(9);
        CellStyle csDatum = modelirajCell(sheet, "datum");
        CellStyle csPodaci = modelirajCell(sheet, "podaci");
        CellStyle csPodaciBold = modelirajCell(sheet, "podaciBold");
        CellStyle csPodaciBoldWrap = modelirajCell(sheet, "podaciBoldWrap");
        cell.setCellStyle(csDatum);
        cell.setCellValue(new Date(godina + "/" + (mesec + 1) + "/1"));
        for (int i = 10; i < brDana + 9; i++) {
            //datum
            cell = row.createCell(i);
            cell.setCellStyle(csDatum);
            cell.setCellFormula(row.getCell(i - 1).getAddress().formatAsString() + "+1");
            //dan            
            String dan = getDan(godina, i - 1, mesec);
            if (i == 10) {
                cell = rowDan.createCell(i - 1);
                cell.setCellStyle(csPodaciBold);
                cell.setCellValue(dan);
            }
            dan = getDan(godina, i, mesec);
            cell = rowDan.createCell(i);
            cell.setCellStyle(csPodaciBold);
            cell.setCellValue(dan);
        }
        ubaciZaglavlje(sheet, csPodaciBoldWrap);
        //
        String lastColl = sheet.getRow(0).getCell(brDana + 8).getReference().substring(0, 2);
        XSSFSheet sheetRV = workbook.getSheet(sheetName + "2");
        int pom = 0;
        for (int i = 2; i < 100; i++) {
            Row rowS = sheetRV.getRow(i);
            Cell cellS;
            try {
                cellS = rowS.getCell(1);
            } catch (NullPointerException npe) {
                cellS = null;
            }
            row = sheet.createRow(i);
            if ((cellS != null) && ((cellS.getCellType() != Cell.CELL_TYPE_BLANK) && (!cellS.toString().equalsIgnoreCase("")))) {
                //r br
                cell = row.createCell(0);
                cell.setCellStyle(csPodaci);
                cell.setCellValue(i - 1);
                //ime
                cell = row.createCell(1);
                cell.setCellStyle(csPodaci);
                cell.setCellValue(cellS.toString());
                //dani bez uslova
                cell = row.createCell(2);
                cell.setCellStyle(csPodaci);
                cell.setCellFormula("COUNTIFS($j$2:$" + lastColl + "$2,\"<>SUB\",$j$2:$" + lastColl + "$2,\"<>NED\",j" + (cell.getRowIndex() + 1) + ":"
                        + lastColl + (cell.getRowIndex() + 1) + ",\"<8\")");
                //dani veci od 8
                cell = row.createCell(3);
                cell.setCellStyle(csPodaci);
                cell.setCellFormula("COUNTIF(j" + (cell.getRowIndex() + 1) + ":" + lastColl + (cell.getRowIndex() + 1) + ",\">=8\")");
                //zarada rv
                cell = row.createCell(4);
                cell.setCellStyle(csPodaci);
                cell.setCellFormula("ax" + (cell.getRowIndex() + 1));
                //razlika c-b
                cell = row.createCell(5);
                cell.setCellStyle(csPodaci);
                cell.setCellFormula("az" + (cell.getRowIndex() + 1));
                //prebacaji
                cell = row.createCell(6);
                cell.setCellStyle(csPodaci);
                cell.setCellFormula("SUM(AR" + (cell.getRowIndex() + 1) + ":AW" + (cell.getRowIndex() + 1) + ")");
                // ukupno
                cell = row.createCell(7);
                cell.setCellStyle(csPodaci);
                cell.setCellFormula("E" + (cell.getRowIndex() + 1) + "+F" + (cell.getRowIndex() + 1) + "+G" + (cell.getRowIndex() + 1));
                // 10 posto
                cell = row.createCell(8);
                cell.setCellStyle(csPodaci);
                cell.setCellFormula("IF(C" + (cell.getRowIndex() + 1) + "<=0,MROUND(H" + (cell.getRowIndex() + 1) + "*0.1,10),0)");
                // sati
                int ned = brNedelja(godina, mesec);
                for (int j = 9; j < brDana + 9; j++) {
                    String formula = "";
                    for (Iterator<Sheet> sit = workbook.sheetIterator(); sit.hasNext();) {
                        Sheet sheetTemp = sit.next();
                        String tabela = sheetTemp.getSheetName();
                        String x = sheet.getRow(0).getCell(j).getReference();
                        if (tabela.startsWith(sheet.getSheetName()) && !tabela.equalsIgnoreCase(sheet.getSheetName())) {
                            formula += ("+IFERROR(if(HLOOKUP(TEXT(" + x + ",\"d.m.\")," + tabela + "!$C$1:$I$87,"
                                    + "VLOOKUP($B" + (cell.getRowIndex() + 1) + "," + tabela + "!$B$3:$Y$87,24,FALSE)"
                                    + "+2,FALSE)=\"\",0,HLOOKUP(TEXT(" + x + ",\"d.m.\")," + tabela + "!$C$1:$I$87,"
                                    + "VLOOKUP($B" + (cell.getRowIndex() + 1) + "," + tabela + "!$B$3:$Y$87,24,FALSE)"
                                    + "+2,FALSE)),0)");
                        }
                    }
                    cell = row.createCell(j);
                    cell.setCellStyle(csPodaci);
                    cell.setCellFormula(formula);
                }
                //suma sati
                cell = row.createCell(42);
                cell.setCellFormula("sum(J" + (cell.getRowIndex() + 1) + ":" + lastColl + (cell.getRowIndex() + 1) + ")");
                //prebacaji ned
                for (Iterator<Sheet> sit = workbook.sheetIterator(); sit.hasNext();) {
                    Sheet sheetTemp = sit.next();
                    String tabela = sheetTemp.getSheetName();
                    if (tabela.startsWith(sheet.getSheetName()) && !tabela.equalsIgnoreCase(sheet.getSheetName())) {
                        char x = tabela.charAt(tabela.length() - 1);
                        int xInt = Character.getNumericValue(x);
                        String formula;
                        if (xInt == 1) {
                            formula = ("IFERROR(IF(OR($J$2=\"PON\",$J$2=\"UTO\",$J$2=\"SRE\"),VLOOKUP(B" + (cell.getRowIndex() + 1) + "," + tabela + "!B$3:L$87,11,FALSE),0),0)");
                        } else if (xInt == ned) {
                            formula = ("IFERROR(IF(AND(" + lastColl + "2<>\"PON\"," + lastColl + "2<>\"UTO\"),"
                                    + "VLOOKUP(B" + (cell.getRowIndex() + 1) + "," + tabela + "!B$3:L$87,11,FALSE),0),0)");
                        } else {
                            formula = ("IFERROR(VLOOKUP(B" + (cell.getRowIndex() + 1) + "," + tabela + "!B$3:L$87,11,FALSE),0)");
                        }
                        cell = row.createCell(42 + xInt);
                        cell.setCellFormula(formula);
                    }
                }
                //r.d.*8*143
                cell = row.createCell(49);
                cell.setCellFormula("SUMIFS(J" + (cell.getRowIndex() + 1) + ":" + lastColl + (cell.getRowIndex() + 1) + ",$J$2:$" + lastColl + "$2,\"<>\"&\"SUB\","
                        + "$J$2:$" + lastColl + "$2,\"<>\"&\"NED\","
                        + "J" + (cell.getRowIndex() + 1) + ":" + lastColl + (cell.getRowIndex() + 1) + ",\"<=8\")*belisat+"
                        + "COUNTIFS($J$2:$" + lastColl + "$2,\"<>\"&\"SUB\",$J$2:$" + lastColl + "$2,\"<>\"&\"NED\","
                        + "J" + (cell.getRowIndex() + 1) + ":" + lastColl + (cell.getRowIndex() + 1) + ",\">8\")*8*belisat");
                //r.v.*162
                cell = row.createCell(50);
                cell.setCellFormula("SUM(J" + (cell.getRowIndex() + 1) + ":" + lastColl + (cell.getRowIndex() + 1) + ")*"
                        + "IFERROR(VLOOKUP($B" + (cell.getRowIndex() + 1) + ",podaci!$A$1:$B$20,2,FALSE),crnisat)");
                //razlika
                cell = row.createCell(51);
                cell.setCellFormula("AY" + (cell.getRowIndex() + 1) + "-AX" + (cell.getRowIndex() + 1));
                
            } else {
                pom = row.getRowNum();
                i = 1000;
            }
        }
        row = sheet.createRow(pom);
        cell = row.createCell(8);
        cell.setCellStyle(csPodaciBold);
        cell.setCellFormula("sum(i3:i" + (cell.getRowIndex()) + ")");
        //naslov
        sheet.shiftRows(0, 100, 2);
        cell = sheet.createRow(0).createCell(0);
        //cell.setCellStyle(csPodaciBoldWrap);
        String ugovor = (sheet.getSheetName().equalsIgnoreCase("crnci") ? "BEZ UGOVORA" : "SA UGOVOROM");
        cell.setCellStyle(modelirajCell(sheet, "naslovBold"));
        cell.setCellValue("STIMULACIJA NA PRISUSTVO - " + (mesec + 1) + "." + godina + ". - " + ugovor);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 8);
        sheet.addMergedRegion(cra);
        //
        conFormatingDani(sheet, pom + 2, lastColl);
        conFormatingUslovi(sheet, pom + 2, lastColl);
    }
    
    private static void ubaciZaglavlje(XSSFSheet sheet, CellStyle csPodaciBoldWrap) {
        Cell cell;
        int r1 = 0, r2 = 1;
        int c = 0;
        //zaglavlje rbr
        sheet.getRow(r2).createCell(c).setCellStyle(csPodaciBoldWrap);
        cell = sheet.getRow(r1).createCell(c);
        cell.setCellStyle(csPodaciBoldWrap);
        cell.setCellValue("R.BR.");
        CellRangeAddress cra = new CellRangeAddress(r1, r2, c, c++);
        sheet.addMergedRegion(cra);
        //zag rad
        sheet.getRow(r2).createCell(c).setCellStyle(csPodaciBoldWrap);
        cell = sheet.getRow(r1).createCell(c);
        cell.setCellStyle(csPodaciBoldWrap);
        cell.setCellValue("RADNIK");
        cra = new CellRangeAddress(r1, r2, c, c++);
        sheet.addMergedRegion(cra);
        //zag br.r.d
        sheet.getRow(r2).createCell(c).setCellStyle(csPodaciBoldWrap);
        cell = sheet.getRow(r1).createCell(c);
        cell.setCellStyle(csPodaciBoldWrap);
        cell.setCellValue("br.r.d. bez uslova");
        cra = new CellRangeAddress(r1, r2, c, c++);
        sheet.addMergedRegion(cra);
        //zag br.d.>8
        sheet.getRow(r2).createCell(c).setCellStyle(csPodaciBoldWrap);
        cell = sheet.getRow(r1).createCell(c);
        cell.setCellStyle(csPodaciBoldWrap);
        cell.setCellValue("br.d. >= 8h");
        cra = new CellRangeAddress(r1, r2, c, c++);
        sheet.addMergedRegion(cra);
        //zag rv
        sheet.getRow(r2).createCell(c).setCellStyle(csPodaciBoldWrap);
        cell = sheet.getRow(r1).createCell(c);
        cell.setCellStyle(csPodaciBoldWrap);
        cell.setCellValue("zarada r.v.");
        cra = new CellRangeAddress(r1, r2, c, c++);
        sheet.addMergedRegion(cra);
        //zag razlika rv
        sheet.getRow(r2).createCell(c).setCellStyle(csPodaciBoldWrap);
        cell = sheet.getRow(r1).createCell(c);
        cell.setCellStyle(csPodaciBoldWrap);
        cell.setCellValue("razlika c.h.-b.h.");
        cra = new CellRangeAddress(r1, r2, c, c++);
        sheet.addMergedRegion(cra);
        //zag prebacaji
        sheet.getRow(r2).createCell(c).setCellStyle(csPodaciBoldWrap);
        cell = sheet.getRow(r1).createCell(c);
        cell.setCellStyle(csPodaciBoldWrap);
        cell.setCellValue("prebaèaji");
        cra = new CellRangeAddress(r1, r2, c, c++);
        sheet.addMergedRegion(cra);
        //zag ukupno
        sheet.getRow(r2).createCell(c).setCellStyle(csPodaciBoldWrap);
        cell = sheet.getRow(r1).createCell(c);
        cell.setCellStyle(csPodaciBoldWrap);
        cell.setCellValue("ukupno");
        cra = new CellRangeAddress(r1, r2, c, c++);
        sheet.addMergedRegion(cra);
        //zag 10%
        sheet.getRow(r2).createCell(c).setCellStyle(csPodaciBoldWrap);
        cell = sheet.getRow(r1).createCell(c);
        cell.setCellStyle(csPodaciBoldWrap);
        cell.setCellValue("10%");
        cra = new CellRangeAddress(r1, r2, c, c++);
        sheet.addMergedRegion(cra);
    }
    
    private static String getDan(int godina, int i, int mesec) {
        //dan
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, godina);
        cal.set(Calendar.DAY_OF_MONTH, i - 8);
        cal.set(Calendar.MONTH, mesec);
        int d = cal.get(Calendar.DAY_OF_WEEK);
        String dan = "XXX";
        switch (d) {
            case 2:
                dan = "PON";
                break;
            case 3:
                dan = "UTO";
                break;
            case 4:
                dan = "SRE";
                break;
            case 5:
                dan = "ÈET";
                break;
            case 6:
                dan = "PET";
                break;
            case 7:
                dan = "SUB";
                break;
            case 1:
                dan = "NED";
                break;
        }
        return dan;
    }
    
    private static CellStyle modelirajCell(XSSFSheet sheet, String tip) {
        CellStyle cs = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 11);
        if (tip.equalsIgnoreCase("datum")) {
            font.setBold(true);
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);//workbook.createDataFormat().getFormat("0.000")
            cs.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("dd.MM."));
        } else if (tip.equalsIgnoreCase("podaci")) {
            cs.setShrinkToFit(true);
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("podaciBold")) {
            font.setBold(true);
            cs.setShrinkToFit(true);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cs.setVerticalAlignment(VerticalAlignment.CENTER);
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("podaciBoldWrap")) {
            font.setBold(true);
            cs.setWrapText(true);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cs.setVerticalAlignment(VerticalAlignment.CENTER);
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
        } else if (tip.equalsIgnoreCase("naslovBold")) {
            font.setBold(true);
            font.setFontHeightInPoints((short) 12);
            cs.setAlignment(HorizontalAlignment.CENTER);
            cs.setVerticalAlignment(VerticalAlignment.CENTER);
        } else {
            //throw new IllegalArgumentException();
        }
        cs.setFont(font);
        return cs;
    }
    
    private static void modelirajSheet(XSSFSheet sheet) {
        sheet.setColumnWidth(0, 5 * 261);
        sheet.setColumnWidth(1, 22 * 261);
        sheet.setColumnWidth(2, 10 * 261);
        sheet.setColumnWidth(3, 9 * 261);
        sheet.setColumnWidth(4, 8 * 261);
        sheet.setColumnWidth(5, 8 * 261);
        sheet.setColumnWidth(6, 9 * 261);
        sheet.setColumnWidth(7, 8 * 261);
        sheet.setColumnWidth(8, 8 * 261);
        for (int i = 1; i <= 31; i++) {
            sheet.setColumnWidth(8 + i, 6 * 261);
        }
        sheet.setZoom(90);
        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
        //sheet.getPrintSetup().setLandscape(true);        
        sheet.setDefaultRowHeight((short) (20 * 15));
        sheet.setMargin(Sheet.BottomMargin, 0.5);
        sheet.setMargin(Sheet.TopMargin, 0.25);
        sheet.setMargin(Sheet.LeftMargin, 0.75);
        sheet.setMargin(Sheet.RightMargin, 0.25);
        sheet.getPrintSetup().setFooterMargin(0.25);
    }
    
    private static boolean printWB(int mesec, int godina, XSSFWorkbook workbook) {
        String ime = "Prisustvo - " + (mesec + 1) + "." + godina;
        File fajl = Prozori.fileSaver(ime + ".xlsx");
        FileOutputStream outputStream;
        try {
            if (fajl != null) {
                outputStream = new FileOutputStream(fajl.getAbsoluteFile());
                workbook.write(outputStream);
                outputStream.close();
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(PrisustvoXLSX.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private static boolean ucitajRV(int godina, int mesec, Component parent, XSSFWorkbook workbook) {
        int actualMaximum = brNedelja(godina, mesec);
        JOptionPane.showMessageDialog(null, "Zadati mesec ima " + actualMaximum + " nedelja.");
        boolean barJedna = false;
        JFileChooser chooser;
        File path = null;
        for (int i = 0; i < actualMaximum; i++) {
            JOptionPane.showMessageDialog(null, "Odaberite " + (i + 1) + ". nedelju.");
            XSSFWorkbook rv;
            chooser = new JFileChooser();
            if (path != null) {
                chooser.setCurrentDirectory(path);
                chooser.setSelectedFile(new File("RV tabela " + (i + 1) + ".xlsx"));
            }
            int returnVal = chooser.showOpenDialog(parent);
            path = chooser.getCurrentDirectory();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    rv = new XSSFWorkbook(new FileInputStream(chooser.getSelectedFile().getAbsolutePath()));
                    XSSFSheet sheet = workbook.createSheet("crnci" + (i + 1));
                    ExcelUtil.copyXLSXSheet(rv, workbook, rv.getSheet("crnci"), sheet);
                    sheet = workbook.createSheet("ugovorci" + (i + 1));
                    ExcelUtil.copyXLSXSheet(rv, workbook, rv.getSheet("ugovorci"), sheet);
                    if (!barJedna) {
                        sheet = workbook.createSheet("podaci");
                        ExcelUtil.copyXLSXSheet(rv, workbook, rv.getSheet("podaci"), sheet);
                    }
                    barJedna = true;
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(PrisustvoXLSX.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                } catch (IOException ex) {
                    Logger.getLogger(PrisustvoXLSX.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
            } else {
                
            }
        }
        return barJedna;
    }
    
    private static int brNedelja(int godina, int mesec) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, godina);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, mesec);
        int actualMaximum = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        return actualMaximum;
    }
    
    private static int brDana(int godina, int mesec) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, godina);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, mesec);
        int actualMaximum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return actualMaximum;
    }
}
