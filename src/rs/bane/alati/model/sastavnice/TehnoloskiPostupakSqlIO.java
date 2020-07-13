package rs.bane.alati.model.sastavnice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

class TehnoloskiPostupakSqlIO {

    static TehnoloskiPostupak ucitajTP(String ident, Connection conn) {
        PreparedStatement stmt = null;
        TehnoloskiPostupak tp = null;
        try {
            stmt = conn.prepareStatement("select top.brtp, top.broj, top.naziv, top.kol_norma, top.tk "
                    + "from np_teh_veze tv left join np_teh_operacije top on (tv.brtp=top.brtp) where tv.ident= ? ;");
            stmt.setString(1, ident);
            int brojOperacije;
            String nazivOperacije;
            double norma;
            double tk; // vreme za normativ u 
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { //ucitamo prvog posebno da bi dobili brtp
                //tp
                int brojTehPostupka = Integer.parseInt(rs.getString(1));
                tp = new TehnoloskiPostupak(brojTehPostupka);
                //top
                brojOperacije = Integer.parseInt(rs.getString(2));
                nazivOperacije = rs.getString(3);
                norma = Double.parseDouble(rs.getString(4));
                tk = Double.parseDouble(rs.getString(5));
                double normaNaSat = norma * 60 / tk;
                TehnoloskaOperacija top = new TehnoloskaOperacija(brojOperacije, nazivOperacije, normaNaSat);
                tp.dodajOperaciju(top);
            }
            while (rs.next()) { //ucitamo ostale
                brojOperacije = Integer.parseInt(rs.getString(2));
                nazivOperacije = rs.getString(3);
                norma = Double.parseDouble(rs.getString(4));
                tk = Double.parseDouble(rs.getString(5));
                double normaNaSat = norma * 60 / tk;
                TehnoloskaOperacija top = new TehnoloskaOperacija(brojOperacije, nazivOperacije, normaNaSat);
                tp.dodajOperaciju(top);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TehnoloskiPostupakSqlIO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Greska pri ucitavanju tehnologije.\n"
                    + "Postaviti teh. postupke na 'gotovo'.");
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(TehnoloskiPostupakSqlIO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return tp;
        }
    }
    
}
