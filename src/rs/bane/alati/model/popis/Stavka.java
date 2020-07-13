package rs.bane.alati.model.popis;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import rs.bane.alati.model.sastavnice.Sastavnica;

public class Stavka {

    public int godina;
    public String lokacija;
    public String ident;
    public String katBroj;
    public String naziv;
    public String jm;
    public String poslednjaOp;
    public double kolicina;
    public double cena = 0;
    public boolean dorada;

    public Stavka(int godina, String lokacija,
            String ident, String katBroj, String naziv, String jm, String poslednjaOp, double kolicina, boolean dorada,
            Connection conn,
            boolean samoMaterijal) {
        this.godina = godina;
        this.lokacija = lokacija;
        this.ident = ident;
        this.katBroj = katBroj;
        this.naziv = naziv;
        this.jm = jm;
        this.poslednjaOp = poslednjaOp;
        this.kolicina = kolicina;
        this.cena = this.getCena(poslednjaOp, conn, samoMaterijal);
        this.dorada = dorada;
    }

    public Stavka(String ident, String katBroj, String naziv, String jm) {
        this.ident = ident;
        this.katBroj = katBroj;
        this.naziv = naziv;
        this.jm = jm;
    }

    private double getCena(String poslednjaOp, Connection conn, boolean samoMaterijal) {
        double cena = 0;
        Sastavnica sast = Sastavnica.ucitajSastavnicu(ident, Sastavnica.getNormaSat(), conn);
        if (!samoMaterijal) {
            cena = Sastavnica.getVrednostSastavniceDoOper(sast, poslednjaOp);
        } else {
            cena = sast.getCenaMatKulm();
        }
        return cena;
    }

    @Override
    public String toString() {
        return "Stavka{" + "godina=" + godina + ", lokacija=" + lokacija + ", ident=" + ident + ", katBroj=" + katBroj + ", naziv=" + naziv + ", jm=" + jm + ", poslednjaOp=" + poslednjaOp + ", kolicina=" + kolicina + '}';
    }

}
