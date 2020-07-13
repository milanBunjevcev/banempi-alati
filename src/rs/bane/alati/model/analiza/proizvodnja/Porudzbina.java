/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bane.alati.model.analiza.proizvodnja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bunjac
 */
public class Porudzbina {

    private int brpor;
    private ArrayList<Nalog> nalozi;

    public Porudzbina(int brpor, ArrayList<Nalog> nalozi) {
        this.brpor = brpor;
        this.nalozi = nalozi;
    }

    public static Porudzbina ucitajNalog(int brpor, Connection conn) {
        Porudzbina porudzbina = null;
        PreparedStatement stmt = null;
        ResultSet rs;
        try {
            stmt = conn.prepareStatement(""
                    + "select a.kat_broj as kat_broj, "
                    + "a.naziv as naziv, "
                    + "orn.brrn as brrn, "
                    + "orn.broj as broj, "
                    + "orn.naziv as naziv_op, "
                    + "orn.kol_lan as kol_lan "
                    + "from ma_artikli a "
                    + "inner join pp_radni_nalozi rn on (a.ident = rn.ident) "
                    + "inner join pp_operacije_r_n orn on (rn.brrn = orn.brrn) "
                    + "where rn.brpor = ? "
                    + "order by orn.brrn, orn.broj;");
            stmt.setInt(1, brpor);
            rs = stmt.executeQuery();
            ArrayList<Nalog> nalozi = new ArrayList<>();
            while (rs.next()) {
                int brrn = rs.getInt("brrn");
                //ako je prazna lista ili je current brrn != previous brrn onda upisi kao novi
                int arrSize = nalozi.size();
                if ((arrSize == 0) || (brrn != nalozi.get(arrSize - 1).getBrrn())) {
                    String kat_broj = rs.getString("kat_broj");
                    String naziv = rs.getString("naziv");
                    Nalog nalog = new Nalog(kat_broj, naziv, brrn);
                    nalozi.add(nalog);
                }
                Nalog nalog = nalozi.get(nalozi.size() - 1);
                int broj = rs.getInt("broj");
                String naziv_op = rs.getString("naziv_op");
                int kol_lan = rs.getInt("kol_lan");
                Operacija op = new Operacija(broj, naziv_op, kol_lan);
                nalog.dodajOperaciju(op);
            }
            porudzbina = new Porudzbina(brpor, nalozi);
        } catch (SQLException ex) {
            Logger.getLogger(Porudzbina.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Porudzbina.class.getName()).log(Level.SEVERE, null, ex);
            }
            return porudzbina;
        }
    }

    @Override
    public String toString() {
        String nlz = "";
        for (Nalog n : nalozi) {
            nlz += "\n" + n;
        }
        return "brpor=" + brpor + "\n" + nlz;
    }

    public int getBrpor() {
        return brpor;
    }

    public void setBrpor(int brpor) {
        this.brpor = brpor;
    }

    public ArrayList<Nalog> getNalozi() {
        return nalozi;
    }

    public void setNalozi(ArrayList<Nalog> nalozi) {
        this.nalozi = nalozi;
    }

}
