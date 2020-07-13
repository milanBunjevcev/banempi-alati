package rs.bane.alati.swinggui.meni;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import rs.bane.alati.swinggui.cilindri.SistemiWindow;
import rs.bane.alati.swinggui.ucinci.dnevni.PreviewTableWindow;
import rs.bane.alati.swinggui.ucinci.dnevni.UcinciWindow;
import rs.bane.alati.swinggui.normativi.NormativiWindow;
import rs.bane.alati.swinggui.popis.PopisWindow;
import rs.bane.alati.swinggui.analiza.proizvodnja.AnalizaWindow;

public class Prozori {

    static Connection connection;
    static Connection ucinakConnection;

    public static JFrame meni = null;
    public static JFrame unos = null;
    public static JFrame pregled = null;
    public static JFrame pracenje = null;
    public static JFrame normativi = null;
    public static JFrame tehnologije = null;
    public static JFrame popis = null;
    public static JFrame sistemi = null;

    public static File fileSaver(String ime) {
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS files", "xls", "xlsx");
        fc.setSelectedFile(new File(ime));
        fc.setFileFilter(filter);
        int result = fc.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File targetFile = fc.getSelectedFile();
//            if (!targetFile.getName().endsWith(".xls")) {
//                targetFile = (new File(targetFile.getPath() + ".xls"));
//            } else if (!targetFile.getName().endsWith(".xlsx")) {
//                targetFile = (new File(targetFile.getPath() + ".xlsx"));
//            }
            if (!targetFile.exists()) {
                try {
                    targetFile.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(PopisWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return targetFile;
        }
        return null;
    }

    public static JFrame openUnos() {
        if (Prozori.unos == null) {
            Prozori.unos = UcinciWindow.createAndOpen(ucinakConnection, connection);
        } else {
            Prozori.unos.setVisible(true);
        }
        return unos;
    }

    public static JFrame openPregled() {
        if (Prozori.pregled == null) {
            Prozori.pregled = PreviewTableWindow.createAndOpen(ucinakConnection);
        } else {
            Prozori.pregled.setVisible(true);
        }
        return pregled;
    }

    public static JFrame openPracenje() {
        if (Prozori.pracenje == null) {
            Prozori.pracenje = AnalizaWindow.createAndOpen(ucinakConnection, connection);
        } else {
            Prozori.pracenje.setVisible(true);
        }
        return pracenje;
    }

    public static JFrame openNormativi() {
        if (Prozori.normativi == null) {
            Prozori.normativi = NormativiWindow.createAndOpen(ucinakConnection);
        } else {
            Prozori.normativi.setVisible(true);
        }
        return normativi;
    }

    public static JFrame openTehnologije() {
        return tehnologije;
    }

    public static JFrame openPopis() {
        if (Prozori.popis == null) {
            Prozori.popis = PopisWindow.createAndOpen(ucinakConnection, connection);
        } else {
            Prozori.popis.setVisible(true);
        }
        return popis;
    }

    public static JFrame openSistemi() {
//        if (Prozori.sistemi == null) {
//            Prozori.sistemi = SistemiWindow.createAndOpen();
//        } else {
//            Prozori.sistemi.setVisible(true);
//        }
//        return sistemi;
        return null;
    }

}
