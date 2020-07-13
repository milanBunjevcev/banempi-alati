package rs.bane.alati.model.ucinci.dnevni;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Worker {

    private int workerID; // primary key
    private String nameFull;

    /**
     * Upisuje novog radnika u bazu ucinaka
     *
     *
     * @param newWorker radnik kojeg treba upisati
     * @param conn konekcija ka bazi ucinaka
     * @return vraca true ako je radnik upisan, u suprotnom false
     */
    public static boolean addWorkerToDB(Worker newWorker, Connection conn) {
        boolean ok = false;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("insert into radnici values( ? );");
            stmt.setString(1, newWorker.getNameFull());
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                ok = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
            return ok;
        }
    }

    /**
     * Konstruktor
     *
     * @param nameFull ime radnika
     */
    public Worker(String nameFull) {
        this.nameFull = nameFull;
    }

    /**
     * Konstruktor - trenutno se ne koristi
     *
     * @param workerID id radnika
     * @param nameFull ime radnika
     */
    public Worker(int workerID, String nameFull) {
        this.workerID = workerID;
        this.nameFull = nameFull;
    }

    /**
     * Vraca listu radnika iz baze ucinaka
     *
     * @param conn konekcija ka bazi ucinaka
     * 
     */
    public static ArrayList<Worker> getAllWorkers(Connection conn) {
        ArrayList<Worker> workers = new ArrayList<Worker>();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from radnici"
                    + " order by ime_radnika;");
            while (rs.next()) {
                String name = rs.getString(1);
                workers.add(new Worker(name));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return workers;
    }

    /**
     * Brise radnika
     *
     * @param worker radnik koji se brise
     * @param conn konekcija
     * @return update liste radnika
     */
    public static boolean deleteWorker(String worker, Connection conn) {
        PreparedStatement stmt = null;
        boolean updatedBool = false;
        try {
            stmt = conn.prepareStatement("delete from radnici where ime_radnika = ? ;");
            stmt.setString(1, worker);
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("uspesno obrisan "); //dev debug
                updatedBool = true;
            } else {
                System.out.println("NIJE obrisan "); //dev debug
            }
        } catch (SQLException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
            return updatedBool;
        }
    }

    @Override
    public String toString() {
        return nameFull;
    }

    public int getWorkerID() {
        return workerID;
    }

    public void setWorkerID(int workerID) {
        this.workerID = workerID;
    }

    public String getNameFull() {
        return nameFull;
    }

    public void setNameFull(String nameFull) {
        this.nameFull = nameFull;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Worker other = (Worker) obj;
        if (this.workerID != other.workerID) {
            return false;
        }
        if (!this.nameFull.equalsIgnoreCase(other.nameFull)) {
            return false;
        }
        return true;
    }

}
