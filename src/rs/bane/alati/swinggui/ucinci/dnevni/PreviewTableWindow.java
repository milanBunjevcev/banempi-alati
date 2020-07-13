package rs.bane.alati.swinggui.ucinci.dnevni;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import rs.bane.alati.model.ucinci.dnevni.Production;
import rs.bane.alati.model.ucinci.dnevni.WorkOrder;
import rs.bane.alati.model.ucinci.dnevni.Worker;
import rs.bane.alati.dao.ExportDayXLSX;
import rs.bane.alati.dao.ExportWeekXLSX;
import rs.bane.alati.swinggui.meni.Prozori;
import static rs.bane.alati.dao.PrisustvoXLSX.export;

public class PreviewTableWindow extends javax.swing.JFrame {

    private Connection ucinakConnection;

    private PreviewTableWindow() {
    }

    private PreviewTableWindow(Connection ucinakConnection) {
        initComponents();
        this.ucinakConnection = ucinakConnection;
        podesiDatum();
        readProductionByDate(new SimpleDateFormat("yyyy/MM/dd").format(datex.getDate()));
        //
        int year = Calendar.getInstance().get(Calendar.YEAR);
        godinaS.setModel(new javax.swing.SpinnerNumberModel(year, 2018, 3000, 1));
    }

    private void podesiDatum() {
        datex.setFormats(new SimpleDateFormat("yyyy/MM/dd"));
        datex1.setFormats(new SimpleDateFormat("yyyy/MM/dd"));
        datex2.setFormats(new SimpleDateFormat("yyyy/MM/dd"));
        Date d = new Date();
        d.setTime(d.getTime() - 86400000);
        datex.setDate(d);
        datex1.setDate(d);
        datex2.setDate(d);
    }

    /**
     * Metod koji pravi instancu klase PreviewTableWindow i otvara prozor
     *
     * @param ucinakConnection konekcija ka bazi ucinaka
     */
    public static PreviewTableWindow createAndOpen(Connection ucinakConnection) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PreviewTableWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PreviewTableWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PreviewTableWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PreviewTableWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        PreviewTableWindow previewWindow = new PreviewTableWindow(ucinakConnection);
        previewWindow.setExtendedState(MAXIMIZED_BOTH);
        previewWindow.setVisible(true);
        return previewWindow;
    }

    /**
     * Ucitava ucinke za zadati datum
     *
     * @param date datum za kriterijum pretrage
     */
    private void readProductionByDate(String date) {
        try {
            ArrayList<Production> prods = Production.findProductionByDate(date, ucinakConnection);
            clearTable(table);
            if (prods.isEmpty()) {
                //JOptionPane.showMessageDialog(this, "Nema ucinaka za zadati datum: " + date);
            } else {
                fillTable(prods);
            }
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, date + "   nije validan format datuma");
        }
    }

    /**
     * Popunjava tabelu podacima iz liste (ucitani metodom
     * readProductionByDate(date))
     *
     * @param prodList ArrayList koji sadrzi ucinke
     */
    private void fillTable(ArrayList<Production> prodList) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (Production p : prodList) {
            Worker w = p.getWorker();
            WorkOrder wo = p.getWorkOrder();
            model.addRow(new Object[]{w.getNameFull(), wo.getTechOperationName(), wo.getProductName(),
                wo.getProductCatalogNumber(), p.getNote(), p.getGoodProductsMade(), p.getWorkOnTime(), p.getRezija(),
                wo.getTechOutturnPerHour(), round(p.getProductWorkPrice(), 3), round(p.getEarnedSalary(), 0),
                wo.getWorkOrderID(), wo.getTechOperationID(), new SimpleDateFormat("yyyy/MM/dd").format(p.getDate()), p.getId(),
                p.getPogon()});
        }
    }

    /**
     * Brise podatke iz tabele
     *
     * @param table tabela iz koje se brisu podaci
     */
    private void clearTable(JTable table) {
        DefaultTableModel mod = (DefaultTableModel) table.getModel();
        if (mod.getRowCount() > 0) {
            for (int i = mod.getRowCount() - 1; i > -1; i--) {
                mod.removeRow(i);
            }
        }
    }

    /**
     * Pomera datum za n dana
     *
     * @param n +: pomera unapred, -:pomera u nazad
     */
    private void changeDate(int n) {
        Date d = new Date();
        d.setTime(d.getTime() - 86400000);
        try {
            d = new Date(new SimpleDateFormat("yyyy/MM/dd").format(datex.getDate()));
            d.setTime(d.getTime() + (n * 86400000));
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, new SimpleDateFormat("yyyy/MM/dd").format(datex.getDate())
                    + " nije validan format datuma. Datum je resetovan na jucerasnji!");
        }
        datex.setDate(d);
    }

    /**
     * Zaokruzuje broj na odredjeni broj decimala
     *
     * @param value broj koji treba zaokruziti
     * @param places broj decimala
     * @return zaokruzen broj
     */
    private static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        prikaziButton = new javax.swing.JButton();
        datumLevoButton = new javax.swing.JButton();
        datumDesnoButton = new javax.swing.JButton();
        removeLineButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        exportButton1 = new javax.swing.JButton();
        mesecCB = new javax.swing.JComboBox<>();
        godinaS = new javax.swing.JSpinner();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jButton3 = new javax.swing.JButton();
        datex = new org.jdesktop.swingx.JXDatePicker();
        datex1 = new org.jdesktop.swingx.JXDatePicker();
        datex2 = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        unosMI2 = new javax.swing.JMenuItem();
        pregledMI = new javax.swing.JMenuItem();
        pracenjeMI = new javax.swing.JMenuItem();
        normativiMI = new javax.swing.JMenuItem();
        tehnologijeMI = new javax.swing.JMenuItem();
        popisMI = new javax.swing.JMenuItem();
        sistemiMI = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pregled ucinaka");
        setLocation(new java.awt.Point(350, 180));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        table.setModel(TableModels.modelAll());
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table);

        prikaziButton.setText("OSVEŽI");
        prikaziButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prikaziButtonActionPerformed(evt);
            }
        });

        datumLevoButton.setText("<");
        datumLevoButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        datumLevoButton.setMaximumSize(new java.awt.Dimension(40, 23));
        datumLevoButton.setPreferredSize(new java.awt.Dimension(15, 23));
        datumLevoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datumLevoButtonActionPerformed(evt);
            }
        });

        datumDesnoButton.setText(">");
        datumDesnoButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        datumDesnoButton.setMaximumSize(new java.awt.Dimension(40, 23));
        datumDesnoButton.setPreferredSize(new java.awt.Dimension(15, 23));
        datumDesnoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datumDesnoButtonActionPerformed(evt);
            }
        });

        removeLineButton.setText("Obriši stavku");
        removeLineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeLineButtonActionPerformed(evt);
            }
        });

        jButton2.setText("EXCEL");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        exportButton1.setText("EXCEL");
        exportButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButton1ActionPerformed(evt);
            }
        });

        mesecCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar" }));

        godinaS.setModel(new javax.swing.SpinnerNumberModel(2017, 2008, 2050, 1));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton3.setText("EXCEL");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        datex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datexActionPerformed(evt);
            }
        });

        jLabel1.setText("Nedeljni");

        jLabel2.setText("Dnevni");

        jLabel3.setText("Meseèni");

        jMenu4.setText("File");

        unosMI2.setText("Unos ucinaka");
        unosMI2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unosMI2ActionPerformed(evt);
            }
        });
        jMenu4.add(unosMI2);

        pregledMI.setText("Pregled ucinaka");
        pregledMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pregledMIActionPerformed(evt);
            }
        });
        jMenu4.add(pregledMI);

        pracenjeMI.setText("Pracenje proizvodnje");
        pracenjeMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pracenjeMIActionPerformed(evt);
            }
        });
        jMenu4.add(pracenjeMI);

        normativiMI.setText("Normativi za izvoz");
        normativiMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normativiMIActionPerformed(evt);
            }
        });
        jMenu4.add(normativiMI);

        tehnologijeMI.setText("Tehnologije");
        tehnologijeMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tehnologijeMIActionPerformed(evt);
            }
        });
        jMenu4.add(tehnologijeMI);

        popisMI.setText("Popis");
        popisMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popisMIActionPerformed(evt);
            }
        });
        jMenu4.add(popisMI);

        sistemiMI.setText("Sistemi");
        sistemiMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sistemiMIActionPerformed(evt);
            }
        });
        jMenu4.add(sistemiMI);

        jMenuBar3.add(jMenu4);

        jMenu2.setText("Help");
        jMenuBar3.add(jMenu2);

        setJMenuBar(jMenuBar3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(removeLineButton)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(prikaziButton)
                                .addGap(45, 45, 45)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(datumLevoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(datumDesnoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(datex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(exportButton1)
                                .addGap(8, 8, 8)))
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(datex1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(datex2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(godinaS, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mesecCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jButton3))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mesecCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(godinaS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(datex, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(prikaziButton)
                                .addComponent(jLabel2)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(removeLineButton)
                                    .addComponent(datumLevoButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(datumDesnoButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(exportButton1)
                                .addGap(4, 4, 4))))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(datex1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(datex2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Ako je event source desni klick onda otvara kontext meni za kopiranje
     * sadrzaja tabele
     *
     * @param evt event source
     */
    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        // TODO add your handling code here:
        if (evt.getButton() == 3) {
            System.out.println("sad bi context menu trebao da se otvori");
        }
    }//GEN-LAST:event_tableMouseClicked

    /**
     * Ucitava sve ucinke za datum koji se nalazi u datumTextField
     *
     * @param evt event source
     */
    private void prikaziButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prikaziButtonActionPerformed
        //readProductionByDate(datumTextField.getText());
        readProductionByDate(new SimpleDateFormat("yyyy/MM/dd").format(datex.getDate()));
    }//GEN-LAST:event_prikaziButtonActionPerformed

    /**
     * Pomera datum koji se nalazi u datumTextField za jedan dan unazad
     *
     * @param evt event source
     */
    private void datumLevoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datumLevoButtonActionPerformed
        // TODO add your handling code here:
        changeDate(-1);
        //readProductionByDate(datumTextField.getText());
        readProductionByDate(new SimpleDateFormat("yyyy/MM/dd").format(datex.getDate()));
    }//GEN-LAST:event_datumLevoButtonActionPerformed

    /**
     * Pomera datum koji se nalazi u datumTextField za jedan dan unapred
     *
     * @param evt event source
     */
    private void datumDesnoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datumDesnoButtonActionPerformed
        // TODO add your handling code here:
        changeDate(1);
        //readProductionByDate(datumTextField.getText());
        readProductionByDate(new SimpleDateFormat("yyyy/MM/dd").format(datex.getDate()));
    }//GEN-LAST:event_datumDesnoButtonActionPerformed

    private void removeLineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeLineButtonActionPerformed
        if (table.getSelectedRow() >= 0) {
            int i = table.getSelectedRow();
            int j = 14;
            int id = (int) table.getModel().getValueAt(i, j);
            if (JOptionPane.showConfirmDialog(null, "Sigurno obriši?", "WARNING",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                // yes option
                Production.deleteInDB(id, ucinakConnection);
                //readProductionByDate(datumTextField.getText());
                readProductionByDate(new SimpleDateFormat("yyyy/MM/dd").format(datex.getDate()));
            } else {
                // no option
            }
        } else {
            JOptionPane.showMessageDialog(this, "Odaberite red");
        }
    }//GEN-LAST:event_removeLineButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Prozori.pregled = null;
    }//GEN-LAST:event_formWindowClosing

    private void unosMI2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unosMI2ActionPerformed
        Prozori.openUnos();
    }//GEN-LAST:event_unosMI2ActionPerformed

    private void pregledMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pregledMIActionPerformed
        Prozori.openPregled();
    }//GEN-LAST:event_pregledMIActionPerformed

    private void pracenjeMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pracenjeMIActionPerformed
        Prozori.openPracenje();
    }//GEN-LAST:event_pracenjeMIActionPerformed

    private void normativiMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_normativiMIActionPerformed
        Prozori.openNormativi();
    }//GEN-LAST:event_normativiMIActionPerformed

    private void tehnologijeMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tehnologijeMIActionPerformed
        Prozori.openTehnologije();
    }//GEN-LAST:event_tehnologijeMIActionPerformed

    private void popisMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popisMIActionPerformed
        Prozori.openPopis();
    }//GEN-LAST:event_popisMIActionPerformed

    private void sistemiMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sistemiMIActionPerformed
        Prozori.openSistemi();
    }//GEN-LAST:event_sistemiMIActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String d1s = new SimpleDateFormat("yyyy/MM/dd").format(datex1.getDate());
        String d2s = new SimpleDateFormat("yyyy/MM/dd").format(datex2.getDate());
        try {
            Date d1 = new Date(d1s);
            Date d2 = new Date(d2s);
            ArrayList<Production> prods = Production.findProductionByDatePeriod(d1s, d2s, ucinakConnection);
            if (ExportWeekXLSX.exportVrednosno2(prods, d1, d2, "week")) {
                JOptionPane.showMessageDialog(this, "Uspesno exportovan!");
            } else {
                JOptionPane.showMessageDialog(this, "NIJE exportovan!");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Pogresan format datuma");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void exportButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButton1ActionPerformed
        ArrayList<Production> prods = Production.findProductionByDate(new SimpleDateFormat("yyyy/MM/dd").format(datex.getDate()),
                ucinakConnection);
        if (ExportDayXLSX.exportVrednosno(prods, "izvestajVrednosno")) {
            JOptionPane.showMessageDialog(this, "Uspesno exportovan!");
        } else {
            JOptionPane.showMessageDialog(this, "NIJE exportovan!");
        }
    }//GEN-LAST:event_exportButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JOptionPane.showMessageDialog(this, "Odaberite odgovarajuæu tabelu sa vremenima rada, "
                + "ili pritisnite 'Cancel' za export bez vremenskog obraèuna.");
        int mesec = mesecCB.getSelectedIndex();
        int godina = (int) godinaS.getValue();
        if (export(mesec, godina, this)) {
            JOptionPane.showMessageDialog(this, "Uspesno exportovan!");
        } else {
            JOptionPane.showMessageDialog(this, "NIJE exportovan!");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void datexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datexActionPerformed
        readProductionByDate(new SimpleDateFormat("yyyy/MM/dd").format(datex.getDate()));
    }//GEN-LAST:event_datexActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXDatePicker datex;
    private org.jdesktop.swingx.JXDatePicker datex1;
    private org.jdesktop.swingx.JXDatePicker datex2;
    private javax.swing.JButton datumDesnoButton;
    private javax.swing.JButton datumLevoButton;
    private javax.swing.JButton exportButton1;
    private javax.swing.JSpinner godinaS;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JComboBox<String> mesecCB;
    private javax.swing.JMenuItem normativiMI;
    private javax.swing.JMenuItem popisMI;
    private javax.swing.JMenuItem pracenjeMI;
    private javax.swing.JMenuItem pregledMI;
    private javax.swing.JButton prikaziButton;
    private javax.swing.JButton removeLineButton;
    private javax.swing.JMenuItem sistemiMI;
    private javax.swing.JTable table;
    private javax.swing.JMenuItem tehnologijeMI;
    private javax.swing.JMenuItem unosMI2;
    // End of variables declaration//GEN-END:variables

}
