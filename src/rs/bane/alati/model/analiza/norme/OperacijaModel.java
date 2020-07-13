package rs.bane.alati.model.analiza.norme;

public class OperacijaModel {

    private String nazivArtikla;
    private String katBroj;
    private String operacija;
    private double proizvedeno;
    private double rv;
    private double normaOst;
    private double normaPred;

    public OperacijaModel(String nazivArtikla, String katBroj, String operacija,
            double proizvedeno, double rv, double normaPred) {
        this.nazivArtikla = nazivArtikla;
        this.katBroj = katBroj;
        this.operacija = operacija;
        this.proizvedeno = proizvedeno;
        this.rv = rv;
        if (rv == 0) {
            this.normaOst = 0;
        } else {
            this.normaOst = proizvedeno / rv;
        }
        this.normaPred = normaPred;
    }

    public String getNazivArtikla() {
        return nazivArtikla;
    }

    public String getKatBroj() {
        return katBroj;
    }

    public String getOperacija() {
        return operacija;
    }

    public double getProizvedeno() {
        return proizvedeno;
    }

    public double getRv() {
        return rv;
    }

    public double getNormaOst() {
        return normaOst;
    }

    public double getNormaPred() {
        return normaPred;
    }

    @Override
    public String toString() {
        return nazivArtikla + "\t" + katBroj + "\t" + operacija + "\t" + proizvedeno + "\t" + rv + "\t"
                + normaOst + "\t" + normaPred;
    }

}
