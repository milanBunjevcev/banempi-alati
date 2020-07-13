package rs.bane.alati.model.normativi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Normativ {

    private String artikal;
    private double sirovina;
    private double ugradniElem;
    private double elekEnergija;
    private double pomocniMat;
    private double ostalo;
    private double radnaSnaga;

    private Normativ(String artikal, double sirovina, double ugradniElem, double elekEnergija, double pomocniMat, double ostalo,
            double radnaSnaga) {
        this.artikal = artikal;
        this.sirovina = sirovina;
        this.ugradniElem = ugradniElem;
        this.elekEnergija = elekEnergija;
        this.pomocniMat = pomocniMat;
        this.ostalo = ostalo;
        this.radnaSnaga = radnaSnaga;
    }

    private Normativ(String artikal) {
        Random rand = new Random();
        this.artikal = artikal;
        this.sirovina = round(37 + rand.nextInt(14) + rand.nextDouble(), 2);
        this.ugradniElem = round(1 + rand.nextInt(7) + rand.nextDouble(), 2);
        this.elekEnergija = round(2 + rand.nextInt(4) + rand.nextDouble(), 2);
        this.pomocniMat = round(4 + rand.nextInt(5) + rand.nextDouble(), 2);
        this.ostalo = round(9 + rand.nextInt(10) + rand.nextDouble(), 2);
        this.radnaSnaga = round(100 - (this.sirovina + this.ugradniElem + this.elekEnergija + this.pomocniMat + this.ostalo), 2);
    }

    public static ArrayList<Normativ> izracunajNormative(ArrayList<String> listaArtikala, Connection conn) {
        ArrayList<Normativ> listaNovih = new ArrayList();
        //ArrayList<String> listaArtikala = ucitajNove("novi.txt");
        for (String s : listaArtikala) {
            Normativ norm = find(s, conn);
            listaNovih.add(norm);
        }
        return listaNovih;
    }

    private static Normativ find(String artikal, Connection conn) {
        PreparedStatement stmtQ = null;
        PreparedStatement stmtI = null;
        Normativ norm = null;
        try {
            stmtQ = conn.prepareStatement("select * from normativi where id = ? ;");
            stmtQ.setString(1, artikal.substring(0, 8));
            ResultSet rs = stmtQ.executeQuery();
            if (rs.next()) {
                Double sirovina = rs.getDouble(3);
                Double ug = rs.getDouble(4);
                Double el = rs.getDouble(5);
                Double pom = rs.getDouble(6);
                Double ostalo = rs.getDouble(7);
                Double rad = rs.getDouble(8);
                norm = new Normativ(artikal, sirovina, ug, el, pom, ostalo, rad);
            } else {
                norm = new Normativ(artikal);
                stmtI = conn.prepareStatement("insert into normativi values( ? , ? , ? , ? , ? , ? , ? , ? );");
                stmtI.setString(1, norm.artikal.substring(0, 8));
                stmtI.setString(2, norm.artikal);
                stmtI.setDouble(3, norm.sirovina);
                stmtI.setDouble(4, norm.ugradniElem);
                stmtI.setDouble(5, norm.elekEnergija);
                stmtI.setDouble(6, norm.pomocniMat);
                stmtI.setDouble(7, norm.ostalo);
                stmtI.setDouble(8, norm.radnaSnaga);
                stmtI.executeUpdate();
                try {
                    stmtI.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Normativ.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Normativ.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmtQ.close();
            } catch (SQLException ex) {
                Logger.getLogger(Normativ.class.getName()).log(Level.SEVERE, null, ex);
            }

            return norm;
        }
    }

    private static ArrayList<String> ucitajNove(String fajl) {
        Scanner novi = null;
        ArrayList<String> listaNovih = new ArrayList();
        try {
            novi = new Scanner(new FileReader(fajl));
            while (novi.hasNext()) {
                String linija = novi.nextLine();
                listaNovih.add(linija);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Normativ.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            novi.close();
            return listaNovih;
        }
    }

    private static final Locale lokacija = Locale.GERMAN;

    @Override
    public String toString() {
        String sirovina = NumberFormat.getInstance(lokacija).format(this.sirovina);
        String ugradniElem = NumberFormat.getInstance(lokacija).format(this.ugradniElem);
        String elekEnergija = NumberFormat.getInstance(lokacija).format(this.elekEnergija);
        String pomocniMat = NumberFormat.getInstance(lokacija).format(this.pomocniMat);
        String ostalo = NumberFormat.getInstance(lokacija).format(this.ostalo);
        String radnaSnaga = NumberFormat.getInstance(lokacija).format(this.radnaSnaga);
        return /*this.artikal + "\t" + */ sirovina + "\t" + ugradniElem + "\t" + elekEnergija + "\t"
                + pomocniMat + "\t" + ostalo + "\t" + radnaSnaga;

//        return /*this.artikal + "\t" + */ this.sirovina + "\t" + this.ugradniElem + "\t" + this.elekEnergija + "\t"
//                + this.pomocniMat + "\t" + this.ostalo + "\t" + this.radnaSnaga;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
