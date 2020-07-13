package rs.bane.alati.dao.analiza.norme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.bane.alati.model.analiza.norme.DnevniUcinakRadnik;
import rs.bane.alati.model.analiza.norme.OperacijaModel;

public class NormaSQL {

    public static ArrayList<DnevniUcinakRadnik> getList(String katBrojIn, String operacijaIn,
            Connection conn) {
        ArrayList<DnevniUcinakRadnik> lista = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs;
        try {
            stmt = conn.prepareStatement("select "
                    + "m.datum as datum, "
                    + "m.ime_radnika as radnik, "
                    + "m.naziv_artikla as artikal, "
                    + "m.kat_broj as sifra, "
                    + "m.operacija as operacija, "
                    + "m.proizvedeno as kom, "
                    + "m.rad_na_vreme as rv, "
                    + "m.norma as predvidjena "
                    + "from dnevni_ucinak m "
                    + "where m.datum in "
                    + "(select n.datum from dnevni_ucinak n where n.kat_broj like ? and n.operacija like ?) "
                    + "order by m.datum, m.ime_radnika;");
            stmt.setString(1, katBrojIn);
            stmt.setString(2, operacijaIn);
            rs = stmt.executeQuery();

            DnevniUcinakRadnik dur = null;
            boolean pronadjen = false;
            String datumPom = "";
            String radnikPom = "";

            while (rs.next()) {

                String datum = rs.getString("datum").substring(0, 10).replace('-', '/');
                String radnik = rs.getString("radnik");
                String artikal = rs.getString("artikal");
                String katBroj = rs.getString("sifra");
                String operacija = rs.getString("operacija");
                double proizvedeno = Double.parseDouble(rs.getString("kom"));
                double rv = Double.parseDouble(rs.getString("rv"));
                double norma = Double.parseDouble(rs.getString("predvidjena"));
                OperacijaModel oper = new OperacijaModel(artikal, katBroj, operacija, proizvedeno, rv, norma);
                System.out.println(oper);
                if (!datumPom.equalsIgnoreCase(datum) || !radnikPom.equalsIgnoreCase(radnik)) {
                    if (pronadjen) {
                        lista.add(dur);
                        pronadjen = false;
                    }
                    dur = new DnevniUcinakRadnik(datum, radnik);
                }
                dur.addOper(oper);
                if (katBroj.equalsIgnoreCase(katBrojIn) && operacija.equalsIgnoreCase(operacijaIn)) {
                    pronadjen = true;
                }

                datumPom = datum;
                radnikPom = radnik;
            }
        } catch (SQLException ex) {
            Logger.getLogger(NormaSQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(NormaSQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }

    public static ArrayList<DnevniUcinakRadnik> getList2(String katBrojIn, String operacijaIn,
            Connection conn) {
        ArrayList<DnevniUcinakRadnik> lista = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs;
        try {
            stmt = conn.prepareStatement("select "
                    + "m.datum as datum, "
                    + "m.ime_radnika as radnik, "
                    + "m.naziv_artikla as artikal, "
                    + "m.kat_broj as sifra, "
                    + "m.operacija as operacija, "
                    + "m.proizvedeno as kom, "
                    + "m.rad_na_vreme as rv, "
                    + "m.norma as predvidjena "
                    + "from dnevni_ucinak m "
                    + "order by m.datum, m.ime_radnika;");
            rs = stmt.executeQuery();

            DnevniUcinakRadnik dur = null;
            boolean pronadjen = false;
            String datumPom = "";
            String radnikPom = "";

            while (rs.next()) {

                String datum = rs.getString("datum").substring(0, 10).replace('-', '/');
                String radnik = rs.getString("radnik");
                String artikal = rs.getString("artikal");
                String katBroj = rs.getString("sifra");
                String operacija = rs.getString("operacija");
                double proizvedeno = Double.parseDouble(rs.getString("kom"));
                double rv = Double.parseDouble(rs.getString("rv"));
                double norma = Double.parseDouble(rs.getString("predvidjena"));
                OperacijaModel oper = new OperacijaModel(artikal, katBroj, operacija, proizvedeno, rv, norma);
                //System.out.println(oper);
                if (!datumPom.equalsIgnoreCase(datum) || !radnikPom.equalsIgnoreCase(radnik)) {
                    if (pronadjen) {
                        System.out.println(dur);
                        lista.add(dur);
                        pronadjen = false;
                    }
                    dur = new DnevniUcinakRadnik(datum, radnik);
                }
                dur.addOper(oper);
//                if (katBroj.equalsIgnoreCase(katBrojIn) && operacija.equalsIgnoreCase(operacijaIn)) {
//                    pronadjen = true;
//                }
                if (liciLi(katBrojIn, katBroj) && liciLi(operacijaIn, operacija)) {
                    pronadjen = true;
                }

                datumPom = datum;
                radnikPom = radnik;
            }
        } catch (SQLException ex) {
            Logger.getLogger(NormaSQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(NormaSQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }

    private static boolean liciLi(String upit, String str) {
        boolean lici = false;
        if (upit.charAt(0) == '%' && upit.charAt(upit.length() - 1) == '%') {
            lici = str.toUpperCase().contains(upit.toUpperCase().substring(1, upit.length() - 1));
        } else if (upit.charAt(0) == '%') {
            lici = str.toUpperCase().endsWith(upit.toUpperCase().substring(1));
        } else if (upit.charAt(upit.length() - 1) == '%') {
            lici = str.toUpperCase().startsWith(upit.toUpperCase().substring(0, upit.length() - 1));
        } else {
            lici = upit.equalsIgnoreCase(str);
        }
        return lici;
    }

}
