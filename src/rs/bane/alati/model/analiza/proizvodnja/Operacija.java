package rs.bane.alati.model.analiza.proizvodnja;

public class Operacija {

    private int broj;
    private String naziv;
    private int kol_lan;

    public Operacija(int broj, String naziv, int kol_lan) {
        this.broj = broj;
        this.naziv = naziv;
        this.kol_lan = kol_lan;
    }

    public int getKol_lan() {
        return kol_lan;
    }

    public void setKol_lan(int kol_lan) {
        this.kol_lan = kol_lan;
    }

    public int getBroj() {
        return broj;
    }

    public void setBroj(int broj) {
        this.broj = broj;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Override
    public String toString() {
        return "\t\t" + broj + "\t" + naziv + "\t" + kol_lan;
    }

}
