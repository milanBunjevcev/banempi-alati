package rs.bane.alati.model.sastavnice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

class SastavnicaSqlIO {

    static Sastavnica ucitajSast(String ident, Connection conn) {
        Sastavnica otac = null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("select a.sifra, a.kat_broj, a.naziv, a.dimenzija, a.jm, a.zadn_c "
                    + "from ma_artikli a where a.ident= ? ;");
            stmt.setString(1, ident);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int nivo = 0;
                char k = rs.getString("sifra").charAt(0);
                String katBroj = rs.getString("kat_broj");
                String naziv = rs.getString("naziv");
                String dimenzija = rs.getString("dimenzija");
                String jm = rs.getString("jm");
                double norma = 1;
                double kolicina = 1;
                double cenaSirovine = Double.parseDouble(rs.getString("zadn_c"));
                otac = new Sastavnica(nivo, ident, k, katBroj, naziv, dimenzija, jm, norma, kolicina, cenaSirovine);
                stmt.close();
                TehnoloskiPostupak tp = TehnoloskiPostupakSqlIO.ucitajTP(otac.getIdent(), conn);
                otac.setTehPostupak(tp);
                if (tp != null) {
                    otac.setCenaRad(tp.getVrednostPostupka());
                }
                ArrayList<Sastavnica> deca = ucitajDecu(otac, conn);
                otac.setDeca(deca);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SastavnicaSqlIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(SastavnicaSqlIO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return otac;
        }
    }

    private static ArrayList<Sastavnica> ucitajDecu(Sastavnica otac, Connection conn) {
        ArrayList<Sastavnica> deca = new ArrayList();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("select ident2, a.sifra, a.kat_broj, a.naziv, a.dimenzija, a.jm, kol_norma,"
                    + " kolicina, a.zadn_c from np_sastavnice inner join ma_artikli a on a.ident = ident2 where ident1 = ? ;");
            stmt.setString(1, otac.getIdent());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String ident = rs.getString(1);
                char k = rs.getString(2).charAt(0);
                String katBroj = rs.getString(3);
                String naziv = rs.getString(4);
                String dimenzija = rs.getString(5);
                String jm = rs.getString(6);
                double norma = Double.parseDouble(rs.getString(7));
                double kolicina = Double.parseDouble(rs.getString(8));
                norma = kolicina / norma;
                kolicina = norma * otac.getKolicina();
                double cenaSirovine = Double.parseDouble(rs.getString(9));
                Sastavnica dete = new Sastavnica(ident, k, katBroj, naziv, dimenzija, jm, norma, kolicina, cenaSirovine);
                //sas.addDete(dete);
                dete.setNivo(otac.getNivo() + 1);
                deca.add(dete);
            }
            stmt.close();
            for (Sastavnica s : deca) {
                TehnoloskiPostupak tp = TehnoloskiPostupakSqlIO.ucitajTP(s.getIdent(), conn);
                s.setTehPostupak(tp);
                if (tp != null) {
                    s.setCenaRad(tp.getVrednostPostupka() * s.getKolicina());
                }
                ArrayList<Sastavnica> detetovaDeca = ucitajDecu(s, conn);
                s.setDeca(detetovaDeca);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SastavnicaSqlIO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Greska pri ucitavanju tehnologije.\n"
                    + "Postaviti teh. postupke na 'gotovo'.");
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(SastavnicaSqlIO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return deca;
        }
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
