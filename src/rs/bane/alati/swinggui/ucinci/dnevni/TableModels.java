package rs.bane.alati.swinggui.ucinci.dnevni;

import javax.swing.table.DefaultTableModel;

public class TableModels {

    /**
     * Vraca default model za tabelu sa svim podacima pojedinacno
     * @return model table
     */
    static DefaultTableModel modelAll() {
        DefaultTableModel model;
        model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Ime Radnika", "Operacija", "Naziv dela", "Kataloški broj", "Napomena", "Proizvedeno", "R.V.", "Režija", "Norma", "Cena po kom", "Zarada", "BRRN", "BROJ", "Datum", "ID", "Pogon"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        return model;
    }

    /**
     * Vraca model table u kome se prikazuje ukupna dnevna zarada po radniku za sve radnike
     * @return model table
     */
    static DefaultTableModel model2() {
        DefaultTableModel model;
        model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Ime Radnika", "Datum", "Zarada"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        return model;
    }
}
