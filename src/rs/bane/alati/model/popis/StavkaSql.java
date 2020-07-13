package rs.bane.alati.model.popis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class StavkaSql {

    static boolean isSirovina(String ident, Connection conn) {
        PreparedStatement stmt = null;
        boolean sirovina = false;
        try {
            stmt = conn.prepareStatement("select a.sifra "
                    + "from ma_artikli a where a.ident= ? ;");
            stmt.setString(1, ident);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                char k = rs.getString(1).charAt(0);
                if (k == 'S') {
                    sirovina = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(StavkaSql.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(StavkaSql.class.getName()).log(Level.SEVERE, null, ex);
            }
            return sirovina;
        }
    }

    static double getCenaSirovine(String ident) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static double getCenaRada(String ident, String poslednjaOp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static ArrayList<String> getDeca(String ident) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
