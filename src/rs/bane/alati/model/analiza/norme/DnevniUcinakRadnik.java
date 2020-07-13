package rs.bane.alati.model.analiza.norme;

import java.util.ArrayList;

public class DnevniUcinakRadnik {

    private String datum;
    private String radnik;
    private ArrayList<OperacijaModel> operacije = new ArrayList<>();

    public DnevniUcinakRadnik(String datum, String radnik) {
        this.datum = datum;
        this.radnik = radnik;
    }

    public void addOper(OperacijaModel op) {
        operacije.add(op);
    }

    public String getDatum() {
        return datum;
    }

    public String getRadnik() {
        return radnik;
    }

    public ArrayList<OperacijaModel> getOperacije() {
        return operacije;
    }

    @Override
    public String toString() {
        String s = "";
        for (OperacijaModel op : operacije) {
            s += datum + "\t" + radnik + "\t" + op + "\n";
        }
        return s;
    }

}
