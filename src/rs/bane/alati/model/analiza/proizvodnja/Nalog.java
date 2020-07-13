package rs.bane.alati.model.analiza.proizvodnja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bunjac
 */
public class Nalog {

    private String kat_broj;
    private String naziv;
    private int brrn;
    private ArrayList<Operacija> operacije;

    public Nalog(String kat_broj, String naziv, int brrn) {
        this.kat_broj = kat_broj;
        this.naziv = naziv;
        this.brrn = brrn;
        operacije = new ArrayList<>();
    }

    public double proizvedeno(int broj, String date1, String date2, Connection conn) {
        double n = 0;
        PreparedStatement stmt = null;
        ResultSet rs;
        try {
            stmt = conn.prepareStatement(""
                    + "select sum(proizvedeno) "
                    + "from dnevni_ucinak "
                    + "where brrn = ? and broj = ? and (datum between ? and ? );");
            stmt.setInt(1, brrn);
            stmt.setInt(2, broj);
            stmt.setDate(3, new java.sql.Date(new Date(date1).getTime()));
            stmt.setDate(4, new java.sql.Date(new Date(date2).getTime()));
            rs = stmt.executeQuery();
            if (rs.next()) {
                n = rs.getDouble(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Nalog.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Nalog.class.getName()).log(Level.SEVERE, null, ex);
            }
            return n;
        }
    }

    public static double proizvedenoStat(int brrn, int broj, Connection conn) {
        double n = 0;
        PreparedStatement stmt = null;
        ResultSet rs;
        try {
            stmt = conn.prepareStatement(""
                    + "select sum(proizvedeno) "
                    + "from dnevni_ucinak "
                    + "where brrn = ? and broj = ? ;");
            stmt.setInt(1, brrn);
            stmt.setInt(2, broj);
            rs = stmt.executeQuery();
            if (rs.next()) {
                n = rs.getDouble(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Nalog.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Nalog.class.getName()).log(Level.SEVERE, null, ex);
            }
            return n;
        }
    }

    public void dodajOperaciju(Operacija o) {
        operacije.add(o);
    }

    public String getKat_broj() {
        return kat_broj;
    }

    public void setKat_broj(String kat_broj) {
        this.kat_broj = kat_broj;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getBrrn() {
        return brrn;
    }

    public void setBrrn(int brrn) {
        this.brrn = brrn;
    }

    public ArrayList<Operacija> getOperacije() {
        return operacije;
    }

    public void setOperacije(ArrayList<Operacija> operacije) {
        this.operacije = operacije;
    }

    @Override
    public String toString() {
        String nlg = kat_broj + "\t" + naziv + "\t" + brrn;
        String op = "";
        for (Operacija o : operacije) {
            op += "\n" + o;
        }
        return nlg + op;
    }

}
