package rs.bane.alati.model.sastavnice;

import java.util.ArrayList;
import java.util.Collections;

public class TehnoloskiPostupak {

    private int brtp; // broj teh postupks
    private double vrednostPostupka = 0;
    private ArrayList<TehnoloskaOperacija> operacije = new ArrayList<>();

    public TehnoloskiPostupak(int brtp) {
        this.brtp = brtp;
        operacije = new ArrayList<>();
    }

    public void dodajOperaciju(TehnoloskaOperacija top) {
        operacije.add(top);
        vrednostPostupka += top.getVrednostOperacije();
        Collections.sort(operacije);
    }

    public int getBrtp() {
        return brtp;
    }

    public double getVrednostPostupka() {
        return vrednostPostupka;
    }

    double getVrednostPostupkaDo(String oper) {
        double n = 0;
        for (TehnoloskaOperacija top : operacije) {
            n += top.getVrednostOperacije();
            if (top.getNaziv().equalsIgnoreCase(oper)) {
                return n;
            }
        }
        return n;
    }

    public ArrayList<TehnoloskaOperacija> getOperacije() {
        return operacije;
    }

    @Override
    public String toString() {
        String tops = "";
        for (TehnoloskaOperacija top : operacije) {
            tops += top + "\n";
        }
        return "" + brtp + "\n" + tops;
    }

}
