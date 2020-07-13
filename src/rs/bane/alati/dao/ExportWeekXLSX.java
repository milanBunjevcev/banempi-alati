package rs.bane.alati.dao;

import rs.bane.alati.dao.ExportWeekXLS;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.poi.ss.usermodel.PrintSetup;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFConditionalFormattingRule;
import org.apache.poi.xssf.usermodel.XSSFFontFormatting;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheetConditionalFormatting;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import rs.bane.alati.model.ucinci.dnevni.Production;

public class ExportWeekXLSX {

    private static void conFormating(XSSFSheet sheet) {
        XSSFSheetConditionalFormatting cf = sheet.getSheetConditionalFormatting();
        XSSFConditionalFormattingRule rule = cf.createConditionalFormattingRule(ComparisonOperator.EQUAL, "0");

        XSSFFontFormatting fill_pattern = rule.createFontFormatting();
        fill_pattern.setFontColorIndex(IndexedColors.WHITE.getIndex());

        CellRangeAddress[] regions = {CellRangeAddress.valueOf("F" + 1 + ":I" + 1000)};

        cf.addConditionalFormatting(regions, rule);
    }

    private static void modelirajSheet(XSSFSheet sheet) {
        sheet.setColumnWidth(0, 5 * 261);
        sheet.setColumnWidth(1, 8 * 261);
        sheet.setColumnWidth(2, 35 * 261);
        sheet.setColumnWidth(3, 30 * 261);
        sheet.setColumnWidth(4, 18 * 261);
        sheet.setColumnWidth(5, 10 * 261);
        sheet.setColumnWidth(6, 7 * 261);
        sheet.setColumnWidth(7, 7 * 261);
        sheet.setColumnWidth(8, 9 * 261);
        sheet.setColumnWidth(9, 10 * 261);
        sheet.setColumnWidth(10, 12 * 261);
        sheet.setColumnWidth(11, 35 * 261);
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

    private static CellStyle modelirajCell(XSSFSheet sheet, String tip) {
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
        } else if (tip.equalsIgnoreCase("dan")) {
            cs.setShrinkToFit(true);
            cs.setBorderBottom(BorderStyle.MEDIUM);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
        } else {
            //throw new IllegalArgumentException();
        }
        cs.setFont(font);
        return cs;
    }

    public static boolean exportVrednosno2(ArrayList<Production> prodList,
            Date date1, Date date2,
            String imeFajla) {
        //        
        prodList = podeliGrupe(prodList);
        //       
        XSSFWorkbook workbook = new XSSFWorkbook();
        //
        XSSFWorkbook rv = null;
        boolean ucitanRV = false;
        try {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose to open this file: "
                        + chooser.getSelectedFile().getAbsolutePath());
                rv = new XSSFWorkbook(new FileInputStream(chooser.getSelectedFile().getAbsolutePath()));

                workbook = rv;
                ucitanRV = true;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExportWeekXLS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExportWeekXLS.class.getName()).log(Level.SEVERE, null, ex);
        }
        //        
        ArrayList<ArrayList<Production>> poPogonu = podeliPogone(prodList);
        if (ucitanRV) {
            poPogonu.add(dodajOstale(workbook, prodList, date2));
        }
        for (ArrayList<Production> prodList2 : poPogonu) {
            prodList = prodList2;
            XSSFSheet sheet = workbook.createSheet(prodList.get(0).getPogon().toString());

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
            CellStyle csDan = modelirajCell(sheet, "dan");
            CellStyle csDanSum = modelirajCell(sheet, "dan");
            csDanSum.setDataFormat(format00);

            int redniBroj = 1;
            int currRow = -1;
            int pocetniRow = currRow;
            String pogon = "";
            String pretRadnik = "";
            String datumFirst = "";
            int pocetniZarada = 0;
            int brojacPageBreak = 0;
            //
            //insertMemo(workbook);
            int rowImena = 0;
            for (Production p : prodList) {
                // ispisati radnika OVDE
                String[] ime = p.getWorker().getNameFull().split(" ");
                String imeObrnuto = ime[1] + " " + ime[0];
                if (!p.getWorker().getNameFull().equalsIgnoreCase(pretRadnik)) {
                    //page break
                    int maxRowsPerPage = 95;
                    int n = prodList.indexOf(p) + 1;
                    int x = 1;
                    while (n < prodList.size()
                            && prodList.get(n).getWorker().getNameFull().equalsIgnoreCase(p.getWorker().getNameFull())) {
                        x++;
                        n++;
                    }
                    if (currRow - brojacPageBreak + x + 8 > maxRowsPerPage) {System.out.println(currRow+" - "+brojacPageBreak+" - "+x+" - "+(currRow - brojacPageBreak + x + 7));
                        sheet.setRowBreak(currRow);
                        brojacPageBreak = currRow;
                    }
                    //draw(sheet, currRow);
                    //currRow += 9;
                    currRow +=1;
                    //page break
                    //naslov
                    Row rowN = sheet.createRow(currRow++);
                    Cell cellN = rowN.createCell(2);
                    rowN.setHeight((short) (20 * 25));
                    cellN.setCellStyle(csVelikoZaglavlje);
                    cellN.setCellValue("NEDELJNA EVIDENCIJA UÈINKA PROIZVODNJE - " + p.getPogon());
                    cellN = sheet.createRow(currRow++).createCell(6);
                    cellN.setCellStyle(csVelikoZaglavlje);
                    cellN.setCellValue("Za period: " + new SimpleDateFormat("dd.MM.").format(date1) + " - "
                            + new SimpleDateFormat("dd.MM.YY.").format(date2));
                    //
                    Row row = sheet.createRow(currRow++);
                    Cell cell = row.createCell(2);
                    Font font = sheet.getWorkbook().createFont();
                    font.setFontHeightInPoints((short) 14);
                    font.setBold(true);
                    cell.getCellStyle().setFont(font);
                    //cell.setCellValue(p.getWorker().getNameFull());
                    cell.setCellValue(imeObrnuto);
                    rowImena = currRow - 1;
                    currRow++;
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
                    //
                    cell = row.createCell(11);
                    cell.setCellStyle(csMaloZaglavlje);
                    cell.setCellValue("Napomena");

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
                String cenaRada = "180";
                String imeKoor = "$C$" + (rowImena + 1);
                if (ucitanRV) {
                    //cenaRada = "IFERROR(VLOOKUP(\"" + imeObrnuto + "\",podaci!$A$1:$B$20,2,FALSE),crnisat)";
                    cenaRada = "IFERROR(VLOOKUP(" + imeKoor + ",podaci!$A$1:$B$20,2,FALSE),crnisat)";
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
                //
                Cell cell2 = row.createCell(11);
                cell2.setCellStyle(csPodaci);
                cell2.setCellValue(p.getNote());
                //
                if (ok) {
                    cell.setCellFormula("ROUND(SUM(J" + (pocetniZarada + 1) + ":J" + (currRow + 1) + "), 0)");
                    for (int i = 0; i < 10; i++) {
                        row.getCell(i).setCellStyle(csDan);
                    }
                    row.getCell(10).setCellStyle(csDanSum);
                    pocetniZarada = currRow + 1;
                    row.getCell(11).setCellStyle(csDan);
                }
                // grupisanje zarada /\
                
                if (sledeci == null || !p.getWorker().getNameFull().equals(sledeci.getWorker().getNameFull())) {
                    // ovde treba ubaciti proveru yza grupisanje datuma i zarade
                    Row r = sheet.createRow(++currRow);
                    Cell c = r.createCell(9);
                    c.setCellStyle(csSum);
                    c.setCellFormula("ROUND(sum(J" + (pocetniRow + 1) + ":J" + (currRow) + "), 2)");
                    c = r.createCell(8);
                    c.setCellStyle(csPodaci);
                    c.setCellValue("Po normi");

                    r = sheet.createRow(++currRow);
                    c = r.createCell(9);
                    c.setCellStyle(csSum);

                    String formula = "IFERROR(VLOOKUP(" + imeKoor + ","
                            + "ugovorci!$B$3:$K$87,9,FALSE),"
                            + "VLOOKUP(" + imeKoor + ","
                            + "crnci!$B$3:$K$87,9,FALSE))*" + cenaRada;
                    c.setCellType(CellType.FORMULA);
                    c.setCellFormula(formula);
                    //FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    //evaluator.evaluateInCell(c);
                    c = r.createCell(8);
                    c.setCellStyle(csPodaci);
                    c.setCellValue("Po vremenu");

                    r = sheet.createRow(++currRow);
                    c = r.createCell(9);
                    c.setCellStyle(csSum);
                    c.setCellFormula("J" + (currRow - 1) + "-J" + (currRow));
                    c = r.createCell(8);
                    c.setCellStyle(csPodaci);
                    c.setCellValue("Razlika");

                    currRow++;
                    //currRow += 4;

                    pocetniRow = currRow + 1;
                    redniBroj++;
                } else {

                }
                currRow++;
            }

            // page numb
            sheet.getFooter().setCenter(HSSFFooter.font("Arial", "Bold")
                    + HSSFFooter.fontSize((short) 15) + "Strana: " + HSSFFooter.page());
            //

            conFormating(sheet);
        }
        workbook.setActiveSheet(3);
        //workbook.setSheetHidden(workbook.getSheetIndex("crnci"), true);
        //workbook.setSheetHidden(workbook.getSheetIndex("ugovorci"), true);
        //workbook.setSheetHidden(workbook.getSheetIndex("podaci"), true);
        try (FileOutputStream outputStream = new FileOutputStream(imeFajla + ".xlsx")) {
            workbook.write(outputStream);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ExportWeekXLS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private static ArrayList<Production> dodajOstale(XSSFWorkbook workbook, ArrayList<Production> prodList, Date date) {
        ArrayList<Production> ostali = new ArrayList();
        for (int i = 2; i < 86; i++) {
            boolean nadjenU = false;
            boolean nadjenC = false;
            String sU = workbook.getSheet("ugovorci").getRow(i).getCell(1).getStringCellValue();
            String sC = workbook.getSheet("crnci").getRow(i).getCell(1).getStringCellValue();
            for (Production p : prodList) {
                String[] ime = p.getWorker().getNameFull().split(" ");
                String imeObrnuto = ime[1] + " " + ime[0];
                if (sU.equalsIgnoreCase(imeObrnuto)) {
                    nadjenU = true;
                }
                if (sC.equalsIgnoreCase(imeObrnuto)) {
                    nadjenC = true;
                }
            }
            if (!nadjenU && !sU.equalsIgnoreCase("")) {
                String[] ime = sU.split(" ");
                String imeRadnika = ime[1] + " " + ime[0];
                double radnoVreme = workbook.getSheet("ugovorci").getRow(i).getCell(9).getNumericCellValue();
                Production novi = new Production(0, 0, "Rad po vremenu", 0, "", "", imeRadnika, date, "", 0, radnoVreme, 0, "r.p.v");
                ostali.add(novi);
            }
            if (!nadjenC && !sC.equalsIgnoreCase("")) {
                String[] ime = sC.split(" ");
                String imeRadnika = ime[1] + " " + ime[0];
                double radnoVreme = workbook.getSheet("crnci").getRow(i).getCell(9).getNumericCellValue();
                Production novi = new Production(0, 0, "Rad po vremenu", 0, "", "", imeRadnika, date, "", 0, radnoVreme, 0, "r.p.v");
                ostali.add(novi);
            }
        }
        ostali.sort(new PrezimePaDatumComparator1());
        return ostali;
    }

    private static void draw(XSSFSheet sheet, int currRow) {
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(0); //Column B
        anchor.setRow1(currRow + 1); //Row 3
        anchor.setCol2(10); //Column C
        anchor.setRow2(currRow + 7);//Row 4
        //Creates a picture
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        Cell cell = sheet.createRow(2).createCell(1);
    }
    static int pictureIdx;
    static CreationHelper helper;

    private static void insertMemo(XSSFWorkbook workbook) {
        //header
        InputStream inputStream;
        try {
            URL res = ExportWeekXLSX.class.getResource("/images/slika.jpeg");
            inputStream = res.openStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            inputStream.close();
            helper = workbook.getCreationHelper();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExportWeekXLSX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExportWeekXLSX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static ArrayList<Production> podeliGrupe(ArrayList<Production> prodList) {
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
        novaList.sort(new PrezimePaDatumComparator1());
        return novaList;
    }

    private static ArrayList<ArrayList<Production>> podeliPogone(ArrayList<Production> prodList) {
        ArrayList<ArrayList<Production>> pogoni = new ArrayList();
        for (int i = 0; i < prodList.size(); i++) {
            int j = i + 1;
            Production curr = prodList.get(i);
            while ((j < prodList.size())
                    && curr.getWorker().equals(prodList.get(j).getWorker())) {
                prodList.get(j).setPogon(curr.getPogon());
                j++;
            }
            i = j - 1;
        }
        for (Production p : prodList) {
            String pogon = p.getPogon();
            if (pogoni.isEmpty()) {
                ArrayList<Production> l = new ArrayList();
                l.add(p);
                pogoni.add(l);
            } else {
                boolean dodat = false;
                for (ArrayList<Production> l : pogoni) {
                    if (pogon.equalsIgnoreCase(l.get(0).getPogon())) {
                        l.add(p);
                        dodat = true;
                    }
                }
                if (!dodat) {
                    ArrayList<Production> l = new ArrayList();
                    l.add(p);
                    pogoni.add(l);
                }
            }
        }

        return pogoni;
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

class ImePaDatumComparator1 implements Comparator<Production> {

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

class PrezimePaDatumComparator1 implements Comparator<Production> {

    @Override
    public int compare(Production p1, Production p2) {
        String[] ime1 = p1.getWorker().getNameFull().split(" ");
        String r1 = ime1[1] + " " + ime1[0];
        String[] ime2 = p2.getWorker().getNameFull().split(" ");
        String r2 = ime2[1] + " " + ime2[0];
        int poImenu = r1.compareToIgnoreCase(r2);
        if (poImenu < 0) {
            return poImenu;
        } else if (poImenu == 0) {
            return p1.getDate().compareTo(p2.getDate());
        } else {
            return poImenu;
        }
    }
}
