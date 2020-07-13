package rs.bane.alati.model.sastavnice;

import static rs.bane.alati.model.sastavnice.SastavnicaSqlIO.round;

public class TehnoloskaOperacija implements Comparable<TehnoloskaOperacija> {

    private int broj;
    private String naziv;
    private double norma;

    TehnoloskaOperacija(int broj, String naziv, double norma) {
        super();
        this.broj = broj;
        if (naziv != null) {
            this.naziv = naziv;
        } else {
            this.naziv = "neimenovano";
        }
        this.norma = norma;
    }

    double getVrednostOperacije() {
        return round(Sastavnica.normaSat / norma, 3);
    }

    @Override
    public String toString() {
        return "TehnoloskaOperacija{" + "broj=" + broj + ", naziv=" + naziv + ", norma=" + norma
                + ", vrednost=" + getVrednostOperacije() + '}';
    }

    @Override
    public int compareTo(TehnoloskaOperacija other) {
        return this.broj - other.getBroj();
    }

    public int getBroj() {
        return broj;
    }

    public String getNaziv() {
        return naziv;
    }

    public double getNorma() {
        return norma;
    }

}
