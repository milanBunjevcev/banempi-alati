package rs.bane.alati.model.sastavnice;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import static rs.bane.alati.model.sastavnice.SastavnicaSqlIO.round;

public class Sastavnica {

    public static double normaSat = 450;

    public static double getNormaSat() {
        return normaSat;
    }

    public static void setNormaSat(double aNormaSat) {
        normaSat = aNormaSat;
    }

    private int nivo;
    private String ident;
    private char k; //tip sastavnice G-gotov proizv, P-polufabrikat, O-operacija
    private String katBroj;
    private String naziv;
    private String dimenzija;
    private String jm;
    private double norma;
    private double kolicina;
    private double cenaSirovine = 0;
    private double cenaMat = 0;
    private double cenaMatKulm = 0;
    private double cenaRad = 0;
    private double cenaRadKulm = 0;
    private ArrayList<Sastavnica> deca;
    private TehnoloskiPostupak tehPostupak;

    private static HashSet<Sastavnica> sirovineBezCene = new HashSet();

    public Sastavnica(int nivo,
            String ident, char k, String katBroj, String naziv, String dimenzija, String jm, double norma,
            double kolicina, double cenaSirovine) {
        this.nivo = nivo;
        this.ident = ident;
        this.k = k;
        this.katBroj = katBroj;
        this.naziv = naziv;
        this.dimenzija = dimenzija;
        this.jm = jm;
        this.norma = norma;
        this.kolicina = kolicina;
        if (k == 'S') { // AKKO je sirovina dobice cenu_mat i cenu, u suprotnom ostaju nule
            this.cenaSirovine = cenaSirovine;
            if (cenaSirovine == 0) {
                sirovineBezCene.add(this);
                System.out.println("nivo");
            }
        }
        this.cenaMat = round(kolicina * this.cenaSirovine, 3);
        this.deca = new ArrayList();
        this.tehPostupak = null;
    }

    public Sastavnica(String ident, char k, String katBroj, String naziv, String dimenzija, String jm, double norma,
            double kolicina, double cenaSirovine) {
        this.ident = ident;
        this.k = k;
        this.katBroj = katBroj;
        this.naziv = naziv;
        this.dimenzija = dimenzija;
        this.jm = jm;
        this.norma = norma;
        this.kolicina = kolicina;
        if (k == 'S') { // AKKO je sirovina dobice cenu_mat i cenu, u suprotnom ostaju nule
            this.cenaSirovine = cenaSirovine;
            if (cenaSirovine == 0) {
                sirovineBezCene.add(this);
                System.out.println("no nivo");
            }
        }
        this.cenaMat = round(kolicina * this.cenaSirovine, 3);
        this.deca = new ArrayList();
        this.tehPostupak = null;
    }

    public void addDete(Sastavnica dete) {
        dete.nivo = this.nivo + 1;
        this.deca.add(dete);
    }

    public static Sastavnica ucitajSastavnicu(String ident, double normaSat, Connection conn) {
        Sastavnica sas;
        sas = SastavnicaSqlIO.ucitajSast(ident, conn);
        sas.calcCenaMatKulm();
        sas.calcCenaRadKulm();
        return sas;
    }

    public static double getVrednostSastavnice(Sastavnica sast) {
        return round(sast.getCenaMatKulm() + sast.getCenaRadKulm(), 3);
    }

    public static double getVrednostSastavniceDoOper(Sastavnica sast, String oper) {

        return round(sast.getVrednostSastavnice(sast) - sast.getCenaRad()
                + sast.getVrednostPostupkaDo(oper), 3);
    }

    private double calcCenaMatKulm() {
        double cenM = this.cenaMat;
        if (cenM == 0) {
            //sirovineBezCene.add(this);
        }
        for (Sastavnica dete : this.deca) {
            cenM += dete.calcCenaMatKulm();
        }
        this.cenaMatKulm = cenM;
        return cenM;
    }

    private double calcCenaRadKulm() {
        double cenR = this.cenaRad;
        for (Sastavnica dete : this.deca) {
            cenR += dete.calcCenaRadKulm();
        }
        this.cenaRadKulm = cenR;
        return cenR;
    }

    @Override
    public String toString() {
        return "Sastavnica{" + "nivo=" + nivo + ", ident=" + ident + ", k=" + k + ", katBroj=" + katBroj + ", naziv=" + naziv + ", dimenzija=" + dimenzija + ", jm=" + jm + ", norma=" + norma + ", kolicina=" + kolicina + ", cenaSirovine=" + cenaSirovine + ", cenaMat=" + cenaMat + ", cenaMatKulm=" + cenaMatKulm + ", cenaRad=" + cenaRad + ", cenaRadKulm=" + cenaRadKulm + '}';
    }

    public static HashSet<Sastavnica> getSirovineBezCene() {
        return sirovineBezCene;
    }

    public static void setSirovineBezCene(HashSet<Sastavnica> sirovineBezCene) {
        Sastavnica.sirovineBezCene = sirovineBezCene;
    }

    public static void resetSirovineBezCene() {
        Sastavnica.sirovineBezCene = new HashSet();
    }

    private double getVrednostPostupkaDo(String oper) {
        return (this.getTehPostupak() != null ? this.getTehPostupak().getVrednostPostupkaDo(oper) : 0);
    }

    public int getNivo() {
        return nivo;
    }

    public void setNivo(int nivo) {
        this.nivo = nivo;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public char getK() {
        return k;
    }

    public void setK(char k) {
        this.k = k;
    }

    public String getKatBroj() {
        return katBroj;
    }

    public void setKatBroj(String katBroj) {
        this.katBroj = katBroj;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getDimenzija() {
        return dimenzija;
    }

    public void setDimenzija(String dimenzija) {
        this.dimenzija = dimenzija;
    }

    public String getJm() {
        return jm;
    }

    public void setJm(String jm) {
        this.jm = jm;
    }

    public double getNorma() {
        return norma;
    }

    public void setNorma(double norma) {
        this.norma = norma;
    }

    public double getKolicina() {
        return kolicina;
    }

    public void setKolicina(double kolicina) {
        this.kolicina = kolicina;
    }

    public double getCenaSirovine() {
        return cenaSirovine;
    }

    public void setCenaSirovine(double cenaSirovine) {
        this.cenaSirovine = cenaSirovine;
    }

    public double getCenaMatKulm() {
        return cenaMatKulm;
    }

    public void setCenaMatKulm(double cenaMatKulm) {
        this.cenaMatKulm = cenaMatKulm;
    }

    public double getCenaRad() {
        return cenaRad;
    }

    public void setCenaRad(double cenaRad) {
        this.cenaRad = cenaRad;
    }

    public double getCenaRadKulm() {
        return cenaRadKulm;
    }

    public void setCenaRadKulm(double cenaRadKulm) {
        this.cenaRadKulm = cenaRadKulm;
    }

    public ArrayList<Sastavnica> getDeca() {
        return deca;
    }

    public void setDeca(ArrayList<Sastavnica> deca) {
        this.deca = deca;
    }

    public TehnoloskiPostupak getTehPostupak() {
        return tehPostupak;
    }

    public void setTehPostupak(TehnoloskiPostupak tehPostupak) {
        this.tehPostupak = tehPostupak;
    }

    public double getCenaMat() {
        return cenaMat;
    }

    public void setCenaMat(double cenaMat) {
        this.cenaMat = cenaMat;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.nivo;
        hash = 79 * hash + Objects.hashCode(this.ident);
        hash = 79 * hash + this.k;
        hash = 79 * hash + Objects.hashCode(this.katBroj);
        hash = 79 * hash + Objects.hashCode(this.naziv);
        hash = 79 * hash + Objects.hashCode(this.dimenzija);
        hash = 79 * hash + Objects.hashCode(this.jm);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.norma) ^ (Double.doubleToLongBits(this.norma) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.kolicina) ^ (Double.doubleToLongBits(this.kolicina) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.cenaSirovine) ^ (Double.doubleToLongBits(this.cenaSirovine) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.cenaMat) ^ (Double.doubleToLongBits(this.cenaMat) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.cenaMatKulm) ^ (Double.doubleToLongBits(this.cenaMatKulm) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.cenaRad) ^ (Double.doubleToLongBits(this.cenaRad) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.cenaRadKulm) ^ (Double.doubleToLongBits(this.cenaRadKulm) >>> 32));
        hash = 79 * hash + Objects.hashCode(this.deca);
        hash = 79 * hash + Objects.hashCode(this.tehPostupak);
        return hash;
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
        final Sastavnica other = (Sastavnica) obj;
        if (!Objects.equals(this.ident, other.ident)) {
            return false;
        }
        return true;
    }

}
