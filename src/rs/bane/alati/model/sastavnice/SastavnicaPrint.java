package rs.bane.alati.model.sastavnice;

public class SastavnicaPrint {

    public static void printSastavnica(Sastavnica sast) {
        System.out.println(sast);
        for (Sastavnica dete : sast.getDeca()) {
            printSastavnica(dete);
        }
        System.out.println(sast.getTehPostupak());
    }

}
