/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.bane.alati.model.cilindri;

/**
 *
 * @author bunjac
 */
public abstract class Jedinica {

    static int maxRez = 9;
    static int maxPrviRez = 6;
    static int maxRazlikaRezova = 6;
    static int brojRezova = 5;
    
    static int[] plociceOpseg = {0, 10, 15, 20, 25, 30, 35};
    static int[] klinoviOpseg = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    public static void podesavanja(int maxRez2, int maxPrviRez2, int maxRazlikaRezova2, int brojRezova2) {
        maxRez = maxRez2;
        maxPrviRez = maxPrviRez2;
        maxRazlikaRezova = maxRazlikaRezova2;
        brojRezova = brojRezova2;
    }

    public static boolean otkljucava(int kljuc, int klin, int plocica) {
        if (kljuc == klin) {
            return true;
        } else if (kljuc == (klin + plocica * 2 / 10)) {
            return true;
        } else {
            return false;
        }
    }

}
