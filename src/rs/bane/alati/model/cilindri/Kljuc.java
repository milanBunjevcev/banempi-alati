package rs.bane.alati.model.cilindri;

import java.util.ArrayList;
import java.util.Random;

public class Kljuc extends Jedinica {

    private int[] rezovi;

    public Kljuc(int[] rezovi) {
        this.rezovi = rezovi;
    }

    public static Kljuc generisiRndKljuc() {
        int[] arr = new int[brojRezova];
        //generacija
        Random rand = new Random();
        arr[0] = rand.nextInt(maxPrviRez + 1);
        for (int i = 1; i < arr.length; i++) {
            int prev = arr[i - 1];
            int donji = Math.max(prev - maxRazlikaRezova, 0);
            int gornji = Math.min(prev + maxRazlikaRezova, maxRez) - prev;
            int n = 0;
            boolean dobar = false;
            while (!dobar) {
                n = donji + rand.nextInt(gornji + 1);
                if (i >= 2) {
                    if (arr[i - 1] == n && arr[i - 2] == n) {

                    } else {
                        int brojac = 0;
                        for (int j = 0; j < i; j++) {
                            if (arr[j] == n) {
                                brojac++;
                            }
                        }
                        if (brojac <= 3) {
                            dobar = true;
                        }
                    }
                } else {
                    dobar = true;
                }
            }
            arr[i] = n;
        }
        //
        Kljuc k = new Kljuc(arr);
        return k;
    }
    ArrayList<Cilindar> cilindri;

    public ArrayList<Cilindar> cilindriKojeOtkljucava(Kljuc kljuc) {
        cilindri = new ArrayList();
        //for (int i = 0; i < kljuc.rezovi.length; i++) {
        cilindriKojeOtkljucavaRekur(kljuc, new int[brojRezova], new int[brojRezova], 0);
        //}
        return cilindri;
    }

    private void cilindriKojeOtkljucavaRekur(Kljuc kljuc,
            int[] klinovi, int[] plocice, int poz) {
        for (int k = 0; k < Cilindar.klinoviOpseg.length; k++) {
            for (int p = 0; p < Cilindar.plociceOpseg.length; p++) {
                if (otkljucava(kljuc.rezovi[poz], Cilindar.klinoviOpseg[k], Cilindar.plociceOpseg[p])) {
                    klinovi[poz] = Cilindar.klinoviOpseg[k];
                    plocice[poz] = Cilindar.plociceOpseg[p];
                    //System.out.println(kljuc.rezovi[poz] + "-" + klinovi[poz] + "-" + plocice[poz]);
                    if (poz < brojRezova - 1) {
                        cilindriKojeOtkljucavaRekur(kljuc, klinovi, plocice, poz + 1);
                    } else {
                        Cilindar cilind = new Cilindar(klinovi.clone(), plocice.clone());
                        cilindri.add(cilind);
                    }
                }
            }
        }
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < rezovi.length; i++) {
            s += rezovi[i];
        }
        return s;
    }

}
