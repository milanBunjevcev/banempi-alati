package rs.bane.alati.model.cilindri;

import java.util.ArrayList;
import static rs.bane.alati.model.cilindri.Jedinica.klinoviOpseg;
import static rs.bane.alati.model.cilindri.Jedinica.plociceOpseg;

public class JednaKombinacija {

    public static ArrayList<JednaKombinacija> kombinacije = gen();
    int kljuc, klin, plocica;

    private JednaKombinacija(int kljuc, int klin, int plocica) {
        this.kljuc = kljuc;
        this.klin = klin;
        this.plocica = plocica;
    }

    private boolean otkljucava() {
        if (kljuc == klin) {
            return true;
        } else if (kljuc == (klin + plocica * 2 / 10)) {
            return true;
        } else {
            return false;
        }
    }

    private static ArrayList<JednaKombinacija> gen() {
        ArrayList<JednaKombinacija> arr = new ArrayList();
        for (int i = 0; i < klinoviOpseg.length; i++) {
            for (int j = 0; j < klinoviOpseg.length; j++) {
                for (int x = 0; x < plociceOpseg.length; x++) {
                    JednaKombinacija jk = new JednaKombinacija(klinoviOpseg[i], klinoviOpseg[j], plociceOpseg[x]);
                    if (jk.otkljucava()) {
                        arr.add(jk);
                    }
                }
            }
        }
        return arr;
    }

    @Override
    public String toString() {
        return kljuc + " == " + klin + " + " + plocica;
    }

    public static void ispisi() {
        for (JednaKombinacija jk : kombinacije) {
            System.out.println(jk);
        }
    }

}
