package rs.bane.alati.model.ucinci.dnevni;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkOrder {

    private int workOrderID;
    private int techOperationID;
    private String techOperationName;
    private double techOutturnPerHour;
    private String productName;
    private String productCatalogNumber;
    private double lansirano = 0;

    /**
     * Konstruktor
     *
     * @param workOrderID broj radnog naloga
     * @param techOperationID broj operacije
     * @param techOperationName ime operacije
     * @param thechOutturnPerHour norma - broj komada koje treba izraditi za
     * period od 1h
     * @param productName ime proizvoda
     * @param productCatalogNumber kataloski broj proizvoda
     */
    public WorkOrder(int workOrderID, int techOperationID, String techOperationName, double thechOutturnPerHour, String productName, String productCatalogNumber) {
        this.workOrderID = workOrderID;
        this.techOperationID = techOperationID;
        this.techOperationName = techOperationName;
        this.techOutturnPerHour = thechOutturnPerHour;
        this.productName = productName;
        this.productCatalogNumber = productCatalogNumber;
    }

    public WorkOrder(int workOrderID, int techOperationID, String techOperationName, double thechOutturnPerHour, String productName, String productCatalogNumber,
            double lansirano) {
        this(workOrderID, techOperationID, techOperationName, thechOutturnPerHour, productName, productCatalogNumber);
        this.lansirano = lansirano;
    }

    /**
     * Kreira instancu klase WorkOrder za zadati broj radnog naloga i broj
     * operacije
     *
     * @param workOrderID broj radnog naloga
     * @param techOperationID broj operacije
     * @param connection konekcija ka glavnoj ISUPP bazi
     * @return instanca WorkOrdera ucitana iz ISUPP baze
     */
    public static WorkOrder getWorkOrderInfo(int workOrderID, int techOperationID, Connection connection) {
        PreparedStatement stmt = null;
        WorkOrder temp = null;
        try {
            stmt = connection.prepareStatement("select orn.naziv, tp.kol_norma, a.naziv, a.kat_broj "
                    + "from pp_operacije_r_n orn "
                    + "inner join pp_radni_nalozi rn on orn.brrn = rn.brrn "
                    + "inner join np_teh_operacije tp on ((rn.brtp = tp.brtp)and(orn.broj = tp.broj)) "
                    + "inner join ma_artikli a on rn.ident = a.ident "
                    + "where orn.brrn = ? and orn.broj = ? ;");
            stmt.setInt(1, workOrderID);
            stmt.setInt(2, techOperationID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String techOperationName = rs.getString(1);
                double techOutturnPerHour = Double.parseDouble(rs.getString(2));
                String productName = rs.getString(3);
                String productCatalogNumber = rs.getString(4);
                temp = new WorkOrder(workOrderID, techOperationID, techOperationName, techOutturnPerHour,
                        productName, productCatalogNumber);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(WorkOrder.class.getName()).log(Level.SEVERE, null, ex);
            }
            return temp;
        }
    }

    public static ArrayList<WorkOrder> getWorkOrderInfo(int workOrderID, Connection connection) {
        PreparedStatement stmt = null;
        ArrayList<WorkOrder> lista = new ArrayList<>();
        try {
            stmt = connection.prepareStatement("select orn.naziv, tp.kol_norma, a.naziv, a.kat_broj, orn.broj, orn.kol_lan "
                    + "from pp_operacije_r_n orn "
                    + "inner join pp_radni_nalozi rn on orn.brrn = rn.brrn "
                    + "inner join np_teh_operacije tp on ((rn.brtp = tp.brtp)and(orn.broj = tp.broj)) "
                    + "inner join ma_artikli a on rn.ident = a.ident "
                    + "where orn.brrn = ? ;");
            stmt.setInt(1, workOrderID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String techOperationName = rs.getString(1);
                double techOutturnPerHour = Double.parseDouble(rs.getString(2));
                String productName = rs.getString(3);
                String productCatalogNumber = rs.getString(4);
                int techOperationID = rs.getInt(5);
                double lansirano = rs.getInt(6);
                WorkOrder temp = new WorkOrder(workOrderID, techOperationID, techOperationName, techOutturnPerHour,
                        productName, productCatalogNumber, lansirano);
                lista.add(temp);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(WorkOrder.class.getName()).log(Level.SEVERE, null, ex);
            }
            return lista;
        }
    }

    @Override
    public String toString() {
        return "workOrderID=" + workOrderID + ", techOperationID=" + techOperationID + ", techOperationName="
                + techOperationName + ", techOutturnPerHour=" + techOutturnPerHour + ", productName="
                + productName + ", productCatalogNumber=" + productCatalogNumber;
    }

    public int getWorkOrderID() {
        return workOrderID;
    }

    public void setWorkOrderID(int workOrderID) {
        this.workOrderID = workOrderID;
    }

    public int getTechOperationID() {
        return techOperationID;
    }

    public void setTechOperationID(int techOperationID) {
        this.techOperationID = techOperationID;
    }

    public double getLansirano() {
        return lansirano;
    }

    public String getTechOperationName() {
        return techOperationName;
    }

    public void setTechOperationName(String techOperationName) {
        this.techOperationName = techOperationName;
    }

    public double getTechOutturnPerHour() {
        return techOutturnPerHour;
    }

    public void setTechOutturnPerHour(double thechOutturnPerHour) {
        this.techOutturnPerHour = thechOutturnPerHour;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCatalogNumber() {
        return productCatalogNumber;
    }

    public void setProductCatalogNumber(String productCatalogNumber) {
        this.productCatalogNumber = productCatalogNumber;
    }

}
