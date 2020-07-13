package rs.bane.alati.model.popis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.firebirdsql.gds.ISCConstants.isc_unique_key_violation;

public class StavkaSqlIO {

    public static boolean updateInDB(boolean upsert, int prevGodina, String prevLokacija, String prevIdent,
            String prevPoslednjaOp, boolean prevDorada, Stavka novaStavka, Connection ucinakConn) {
        PreparedStatement stmt = null;
        boolean updatedBool = false;
        try {
            String upsertStr = "";
            if (upsert) {
                upsertStr = "kol +";
            }
            stmt = ucinakConn.prepareStatement("update popis "
                    + "set godina = ? , lokacija = ? , "
                    + "ident = ? , kat_broj = ? , naziv = ? , jm = ? , poslednja_op = ? , kol = " + upsertStr + " ? , "
                    + "dorada = ? "
                    + "where (godina = ? ) and (lokacija = ? ) and (ident = ? ) and (poslednja_op = ? ) and (dorada = ? );");
            stmt.setInt(1, novaStavka.godina);
            stmt.setString(2, novaStavka.lokacija);
            stmt.setString(3, novaStavka.ident);
            stmt.setString(4, novaStavka.katBroj);
            stmt.setString(5, novaStavka.naziv);
            stmt.setString(6, novaStavka.jm);
            stmt.setString(7, novaStavka.poslednjaOp);
            stmt.setDouble(8, novaStavka.kolicina);
            stmt.setString(9, (novaStavka.dorada ? "y" : "n"));
            stmt.setInt(10, prevGodina);
            stmt.setString(11, prevLokacija);
            stmt.setString(12, prevIdent);
            stmt.setString(13, prevPoslednjaOp);
            stmt.setString(14, (prevDorada ? "y" : "n"));
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("uspesno azuriran "); //dev debug
                updatedBool = true;
            } else {
                System.out.println("NIJE azuriran, ubacujemo novi "); //dev debug
                // ubacivanje novog
                updatedBool = insertStavka(novaStavka, ucinakConn);
            }
        } catch (SQLException ex) {
            if (ex.getErrorCode() == isc_unique_key_violation) {
                System.out.println("UHVATIO");
                stmt.close();
                updatedBool = updateInDB(true, novaStavka.godina, novaStavka.lokacija, novaStavka.ident,
                        novaStavka.poslednjaOp, novaStavka.dorada, novaStavka, ucinakConn);
                System.out.println(novaStavka.godina + " " + novaStavka.lokacija + " " + novaStavka.ident + " "
                        + novaStavka.poslednjaOp);
                if (updatedBool) {
                    deleteInDB(prevGodina, prevLokacija, prevIdent, prevPoslednjaOp, prevDorada, ucinakConn);
                }
            } else {
                Logger.getLogger(Stavka.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Stavka.class.getName()).log(Level.SEVERE, null, ex);
            }
            return updatedBool;
        }
    }

    public static boolean insertStavka(Stavka s, Connection ucinakConn) {
        PreparedStatement stmt = null;
        boolean updatedBool = false;
        try {
            stmt = ucinakConn.prepareStatement("insert into "
                    + "popis (godina, lokacija, ident, kat_broj, naziv, jm, poslednja_op, kol, dorada)"
                    + "values( ? , ? , ? , ? , ? , ? , ? , ? , ?);");
            stmt.setInt(1, s.godina);
            stmt.setString(2, s.lokacija);
            stmt.setString(3, s.ident);
            stmt.setString(4, s.katBroj);
            stmt.setString(5, s.naziv);
            stmt.setString(6, s.jm);
            stmt.setString(7, s.poslednjaOp);
            stmt.setDouble(8, s.kolicina);
            stmt.setString(9, (s.dorada ? "y" : "n"));
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("uspesno ubacen "); //obavestenje da je ubacen
                updatedBool = true;
            } else {
                System.out.println("NIJE ubacen ");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Stavka.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Stavka.class.getName()).log(Level.SEVERE, null, ex);
            }
            return updatedBool;
        }
    }

    public static boolean deleteInDB(int godina, String lokacija, String ident, String poslednjaOp, boolean dorada,
            Connection ucinakConn) {
        PreparedStatement stmt = null;
        boolean updatedBool = false;
        try {
            stmt = ucinakConn.prepareStatement("delete from popis "
                    + "where (godina = ? ) and (lokacija = ? ) and (ident = ? ) and (poslednja_op = ? ) "
                    + "and (dorada = ? );");
            stmt.setInt(1, godina);
            stmt.setString(2, lokacija);
            stmt.setString(3, ident);
            stmt.setString(4, poslednjaOp);
            stmt.setString(5, (dorada ? "y" : "n"));
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("uspesno obrisan "); //dev debug
                updatedBool = true;
            } else {
                System.out.println("NIJE obrisan "); //dev debug
            }
        } catch (SQLException ex) {
            Logger.getLogger(Stavka.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Stavka.class.getName()).log(Level.SEVERE, null, ex);
            }
            return updatedBool;
        }
    }

    public static ArrayList<String> getOperacije(String ident, Connection conn) {
        ArrayList<String> operacije = new ArrayList<String>();
        PreparedStatement stmt = null;
        ResultSet rs;
        try {
            stmt = conn.prepareStatement("select naziv "
                    + "from np_teh_operacije op inner join np_teh_veze tv on op.brtp = tv.brtp "
                    + "where tv.ident = ? "
                    + "order by op.broj;");
            stmt.setString(1, ident);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String operacija = rs.getString("naziv");
                operacije.add(operacija);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Stavka.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Stavka.class.getName()).log(Level.SEVERE, null, ex);
            }
            return operacije;
        }
    }

    public static ArrayList<Stavka> getListGodinaLokacija(boolean samoMaterijal, int godina, String lokacija,
            Connection ucinakConn, Connection connection) {
        ArrayList<Stavka> stavke = new ArrayList();
        PreparedStatement stmt = null;
        ResultSet rs;
        try {
            stmt = ucinakConn.prepareStatement("select ident, kat_broj, naziv, jm, poslednja_op, "
                    + "sum(kol) as kolicina , dorada "
                    + "from popis "
                    + "where (godina = ? ) and (lokacija like ? ) "
                    + "group by ident, kat_broj, naziv, jm, poslednja_op, dorada " //, lokacija "
                    + "order by kat_broj;");
            stmt.setInt(1, godina);
            if (lokacija.equalsIgnoreCase("sve lokacije")) {
                stmt.setString(2, "%");
            } else {
                stmt.setString(2, lokacija);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                String ident = rs.getString("ident");
                String katBroj = rs.getString("kat_broj");
                String naziv = rs.getString("naziv");
                String jm = rs.getString("jm");
                String poslednjaOp = rs.getString("poslednja_op");
                Double kolicina = rs.getDouble("kolicina");
                boolean dorada = false;
                if (rs.getString("dorada").equalsIgnoreCase("y")) {
                    dorada = true;
                }
                Stavka st = new Stavka(godina, lokacija, ident, katBroj, naziv, jm, poslednjaOp, kolicina, dorada,
                        connection, samoMaterijal);                
                stavke.add(st);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Stavka.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Stavka.class.getName()).log(Level.SEVERE, null, ex);
            }
            return stavke;
        }
    }

}
