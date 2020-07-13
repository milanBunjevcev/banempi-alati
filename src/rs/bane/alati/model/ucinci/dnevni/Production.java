package rs.bane.alati.model.ucinci.dnevni;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Production {

    private String pogon;
    private double productWorkPrice;
    private int id;
    private WorkOrder workOrder;
    private Worker worker;
    private Date date;
    private String note;
    private double goodProductsMade;
    private double badProductsMade;
    private double workOnTime; //if techOperation does not exists in papers
    private double rezija; //smisli englsku rec XD
    private double earnedSalary;
    public static double currentWorkingPricePerHour = 180; // trebace izmestiti da bude globalnije

    /**
     * Konstruktor bez parametara
     */
    public Production() {
    }

    /**
     * Konstruktor
     *
     * @param workOrderID broj radnog naloga
     * @param techOperationID broj operacije
     * @param techOperationName ime operacije
     * @param thechOutturnPerHour norma
     * @param productName ime proizvoda
     * @param productCatalogNumber kataloski broj proizvoda
     * @param worker ime radnika
     * @param date datum izvrsavanja operacije
     * @param note napomena
     * @param goodProductsMade izradjeno proizvoda
     * @param workOnTime ako ne postoji norma pise se vreme rada, u suprotnom
     * stavlja se 0
     * @param rezija rezija
     * @param pogon we
     */
    public Production(int workOrderID, int techOperationID, String techOperationName, double thechOutturnPerHour,
            String productName, String productCatalogNumber, String worker, Date date, String note, double goodProductsMade,
            double workOnTime, double rezija, String pogon) {
        this.workOrder = new WorkOrder(workOrderID, techOperationID, techOperationName, thechOutturnPerHour, productName,
                productCatalogNumber);
        this.worker = new Worker(worker);
        this.date = date;
        this.note = note;
        this.goodProductsMade = goodProductsMade;
        this.workOnTime = workOnTime;
        this.rezija = rezija;
        if (worker.equalsIgnoreCase("nikola sekuliæ")) {
            this.earnedSalary = calculateEarnedSalary(160);
        } else {
            this.earnedSalary = calculateEarnedSalary(currentWorkingPricePerHour);
        }
        this.pogon = pogon;
    }

    /**
     * Konstruktor
     *
     * @param workOrderID broj radnog naloga
     * @param techOperationID broj operacije
     * @param techOperationName ime operacije
     * @param thechOutturnPerHour norma
     * @param productName ime proizvoda
     * @param productCatalogNumber kataloski broj proizvoda
     * @param worker ime radnika
     * @param date datum izvrsavanja operacije
     * @param note napomena
     * @param goodProductsMade izradjeno proizvoda
     * @param workOnTime ako ne postoji norma pise se vreme rada, u suprotnom
     * stavlja se 0
     * @param rezija rezija
     * @param salary zarada
     * @param id id ucinka u bazi ucinaka
     * @param pogon dw
     */
    public Production(int workOrderID, int techOperationID, String techOperationName, double thechOutturnPerHour,
            String productName, String productCatalogNumber, String worker, Date date, String note, double goodProductsMade,
            double workOnTime, double rezija, double salary, int id, double productWorkPrice, String pogon) {
        this(workOrderID, techOperationID, techOperationName, thechOutturnPerHour, productName, productCatalogNumber, worker, date, note, goodProductsMade, workOnTime, rezija, pogon);
        this.earnedSalary = salary;
        this.id = id;
        this.productWorkPrice = productWorkPrice;
    }

    /**
     * Vraca izracunatu zaradu
     *
     * @return izracunata zarada
     */
    private double calculateEarnedSalary(double currentWorkingPricePerHour) {
        double workOnTimeSalary = workOnTime * currentWorkingPricePerHour;
        //double rezijaSalary = rezija * currentWorkingPricePerHour;
        double productionSalary = 0;
        if (workOrder.getTechOutturnPerHour() != 0) {
            productionSalary = currentWorkingPricePerHour / workOrder.getTechOutturnPerHour() * goodProductsMade;
            //novina
            return productionSalary;
            //novina
        }
        return workOnTimeSalary;
        //return workOnTimeSalary + rezijaSalary + productionSalary;
    }

    /**
     * Upisuje instancu klase Production u bazu ucinaka
     *
     * @param p instanca klase Production
     * @param conn konekcija ka bazi ucinaka
     * @return true - upisa, false - nije upisan
     */
    public static boolean insertInDB(Production p, Connection conn) {
        PreparedStatement stmt = null;
        boolean updatedBool = false;
        try {
            stmt = conn.prepareStatement("insert into dnevni_ucinak (brrn, broj, operacija, norma, naziv_artikla, kat_broj, "
                    + "ime_radnika, proizvedeno, rad_na_vreme, rezija, zarada, napomena, datum, pogon) "
                    + "values( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? );");
            stmt.setInt(1, p.workOrder.getWorkOrderID());
            stmt.setInt(2, p.workOrder.getTechOperationID());
            stmt.setString(3, p.workOrder.getTechOperationName());
            stmt.setDouble(4, p.workOrder.getTechOutturnPerHour());
            stmt.setString(5, p.workOrder.getProductName());
            stmt.setString(6, p.workOrder.getProductCatalogNumber());
            stmt.setString(7, p.worker.getNameFull());
            stmt.setDouble(8, p.goodProductsMade);
            stmt.setDouble(9, p.workOnTime);
            stmt.setDouble(10, p.rezija);
            stmt.setDouble(11, p.earnedSalary);
            stmt.setString(12, p.note);
            stmt.setDate(13, new java.sql.Date(p.date.getTime()));
            stmt.setString(14, p.getPogon());
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("uspesno ubacen "); //obavestenje da je ubacen
                updatedBool = true;
            } else {
                System.out.println("NIJE ubacen ");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Production.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Production.class.getName()).log(Level.SEVERE, null, ex);
            }
            return updatedBool;
        }
    }

    /**
     * Azurira podatke ucinka
     *
     * @param p instanca klase Production koju treba upisati kao novu verziju
     * ucinka
     * @param id id ucinka iz baze ucinaka koji treba izmeniti sa podacima iz
     * 'p'
     * @param conn konekcija ka bazi ucinaka
     * @return true - izmenjen, false - nije izmenjen
     */
    public static boolean updateInDB(Production p, int id, Connection conn) {
        return false;
    }

    /**
     * Brise ucinak iz baze za zadati id ucinka
     *
     * @param id id ucinka koji treba obrisati
     * @param conn konekcija ka bazi ucinka
     * @return true - obrisan. false - nije obrisan
     */
    public static boolean deleteInDB(int id, Connection conn) {
        PreparedStatement stmt = null;
        boolean updatedBool = false;
        try {
            stmt = conn.prepareStatement("delete from dnevni_ucinak where id = ? ;");
            stmt.setInt(1, id);
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("uspesno obrisan "); //dev debug
                updatedBool = true;
            } else {
                System.out.println("NIJE obrisan "); //dev debug
            }
        } catch (SQLException ex) {
            Logger.getLogger(Production.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Production.class.getName()).log(Level.SEVERE, null, ex);
            }
            return updatedBool;
        }
    }

    /**
     * Pravi listu svih ucinaka za zadati datum
     *
     * @param date datum za ucinke
     * @param conn konekcija ka bazi ucinaka
     * @return lista instanci klase Production iz baze za zadati datum
     */
    public static ArrayList<Production> findProductionByDate(String date, Connection conn) {
        ArrayList<Production> tempList = new ArrayList<Production>();
        PreparedStatement stmt = null;
        ResultSet rs;
        try {
            stmt = conn.prepareStatement("select ime_radnika, operacija, naziv_artikla, kat_broj, napomena, proizvedeno, "
                    + "rad_na_vreme, rezija, norma, CASE WHEN norma<>0 THEN 130/norma ELSE 0 END as cena_komada, zarada, "
                    + "BRRN, BROJ, Datum, id, pogon from DNEVNI_UCINAK where datum = ? order by pogon, ime_radnika;");
            stmt.setDate(1, new java.sql.Date(new Date(date).getTime()));
            rs = stmt.executeQuery();
            while (rs.next()) {
                String ime_radnika = rs.getString("ime_radnika");
                String operacija = rs.getString("operacija");
                String naziv_artikla = rs.getString("naziv_artikla");
                String kat_broj = rs.getString("kat_broj");
                String napomena = rs.getString("napomena");
                double proizvedeno = Double.parseDouble(rs.getString("proizvedeno"));
                double rv = Double.parseDouble(rs.getString("rad_na_vreme"));
                double rezija = Double.parseDouble(rs.getString("rezija"));
                double norma = Double.parseDouble(rs.getString("norma"));
                double cena_komada = Double.parseDouble(rs.getString("cena_komada"));
                double zarada = Double.parseDouble(rs.getString("zarada"));
                int brrn = Integer.parseInt(rs.getString("brrn"));
                int broj = Integer.parseInt(rs.getString("broj"));
                String datum = rs.getString("datum").substring(0, 10).replace('-', '/');
                int id = Integer.parseInt(rs.getString("id"));
                String pogon = rs.getString("pogon");
                Production p = new Production(brrn, broj, operacija, norma, naziv_artikla, kat_broj, ime_radnika,
                        new Date(datum), napomena, proizvedeno, rv, rezija, zarada, id, cena_komada, pogon);
                tempList.add(p);
            }
        } catch (IllegalArgumentException iae) {
            Logger.getLogger(Production.class.getName()).log(Level.SEVERE, null, iae);
            throw new IllegalArgumentException();
        } catch (SQLException ex) {
            Logger.getLogger(Production.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Production.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tempList;
    }

    public static ArrayList<Production> findProductionByDatePeriod(String d1, String d2, Connection conn) {
        ArrayList<Production> tempList = new ArrayList<Production>();
        PreparedStatement stmt = null;
        ResultSet rs;
        try {
            stmt = conn.prepareStatement("select pogon, datum, ime_radnika, operacija, naziv_artikla, "
                    + "kat_broj, proizvedeno, norma, rad_na_vreme, napomena "
                    + "from dnevni_ucinak "
                    + "where datum between ? and ? "
                    + "order by ime_radnika, datum asc;");
            //+ "order by pogon, ime_radnika, datum asc;");
            stmt.setDate(1, new java.sql.Date(new Date(d1).getTime()));
            stmt.setDate(2, new java.sql.Date(new Date(d2).getTime()));
            rs = stmt.executeQuery();
            while (rs.next()) {
                String ime_radnika = rs.getString("ime_radnika");
                String operacija = rs.getString("operacija");
                String naziv_artikla = rs.getString("naziv_artikla");
                String kat_broj = rs.getString("kat_broj");
                String napomena = rs.getString("napomena");
                double proizvedeno = Double.parseDouble(rs.getString("proizvedeno"));
                double rv = Double.parseDouble(rs.getString("rad_na_vreme"));
                double rezija = 0;
                double norma = Double.parseDouble(rs.getString("norma"));
                double cena_komada = 0;
                double zarada = 0;
                int brrn = 0;
                int broj = 0;
                String datum = rs.getString("datum").substring(0, 10).replace('-', '/');
                int id = 0;
                String pogon = rs.getString("pogon");
                Production p = new Production(brrn, broj, operacija, norma, naziv_artikla, kat_broj, ime_radnika,
                        new Date(datum), napomena, proizvedeno, rv, rezija, zarada, id, cena_komada, pogon);
                tempList.add(p);
            }
        } catch (IllegalArgumentException iae) {
            Logger.getLogger(Production.class.getName()).log(Level.SEVERE, null, iae);
            throw new IllegalArgumentException();
        } catch (SQLException ex) {
            Logger.getLogger(Production.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Production.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tempList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setPogon(String pogon) {
        this.pogon = pogon;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getGoodProductsMade() {
        return goodProductsMade;
    }

    public void setGoodProductsMade(double goodProductsMade) {
        this.goodProductsMade = goodProductsMade;
    }

    public double getBadProductsMade() {
        return badProductsMade;
    }

    public void setBadProductsMade(double badProductsMade) {
        this.badProductsMade = badProductsMade;
    }

    public double getWorkOnTime() {
        return workOnTime;
    }

    public void setWorkOnTime(double workOnTime) {
        this.workOnTime = workOnTime;
    }

    public double getRezija() {
        return rezija;
    }

    public void setRezija(double rezija) {
        this.rezija = rezija;
    }

    public double getEarnedSalary() {
        return earnedSalary;
    }

    public void setEarnedSalary(double earnedSalary) {
        this.earnedSalary = earnedSalary;
    }

    public double getCurrentWorkingPricePerHour() {
        return currentWorkingPricePerHour;
    }

    public void setCurrentWorkingPricePerHour(double currentWorkingPricePerHour) {
        this.currentWorkingPricePerHour = currentWorkingPricePerHour;
    }

    public double getProductWorkPrice() {
        return productWorkPrice;
    }

    public void setProductWorkPrice(double productWorkPrice) {
        this.productWorkPrice = productWorkPrice;
    }

    public String getPogon() {
        return pogon;
    }

}
