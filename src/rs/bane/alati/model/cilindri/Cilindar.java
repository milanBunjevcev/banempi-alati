/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bane.alati.model.cilindri;

import java.util.ArrayList;

/**
 *
 * @author bunjac
 */
public class Cilindar extends Jedinica {

    private int[] klinovi;
    private int[] plocice;
    private ArrayList<Kljuc> otkljucavajuGa = new ArrayList();

    public Cilindar(int[] klinovi, int[] plocice) {
        this.klinovi = klinovi;
        this.plocice = plocice;
    }

    public Cilindar() {
        this(new int[brojRezova], new int[brojRezova]);
    }

    public int[] getKlinovi() {
        return klinovi;
    }

    public int[] getPlocice() {
        return plocice;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < klinovi.length; i++) {
            s += klinovi[i] + "\t";
        }
        s += "";
        for (int i = 0; i < plocice.length; i++) {
            s += plocice[i];
            if (i < plocice.length - 1) {
                s += "\t";
            }
        }
        return s;
    }

}
