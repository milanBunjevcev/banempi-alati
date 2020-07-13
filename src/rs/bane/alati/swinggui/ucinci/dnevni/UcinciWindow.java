package rs.bane.alati.swinggui.ucinci.dnevni;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import rs.bane.alati.model.analiza.proizvodnja.Nalog;
import rs.bane.alati.model.ucinci.dnevni.Production;
import rs.bane.alati.model.ucinci.dnevni.WorkOrder;
import rs.bane.alati.model.ucinci.dnevni.Worker;
import rs.bane.alati.swinggui.meni.MeniWindow;
import rs.bane.alati.swinggui.meni.Prozori;

public class UcinciWindow extends javax.swing.JFrame {

    Connection connection;
    Connection ucinakConnection;
    DefaultComboBoxModel modelRadnika = new DefaultComboBoxModel();
    private ArrayList<Worker> workers;

    public UcinciWindow(Connection ucinakConnection, Connection connection) {
        initComponents();
        this.setLocationRelativeTo(null);
        setFields("0", "0", "", "0", "", "", "0", "0", "0");
        setDate();
        this.ucinakConnection = ucinakConnection;
        this.connection = connection;
        workers = ucitajRadnikeUDropIVratiArray();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rnBrrnTextField = new javax.swing.JTextField();
        saveProductionButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        datumTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        operacijaTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        normaTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        nazivDelaTextField = new javax.swing.JTextField();
        katBrojTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        proizvedenoTextField = new javax.swing.JTextField();
        radnoVremeTextField = new javax.swing.JTextField();
        rezijaTextField = new javax.swing.JTextField();
        napomenaTextField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        pretraziButton = new javax.swing.JButton();
        resetFieldsButton = new javax.swing.JButton();
        radniciCombo = new javax.swing.JComboBox<>();
        addWorkerButton = new javax.swing.JButton();
        datumLevoButton = new javax.swing.JButton();
        datumDesnoButton = new javax.swing.JButton();
        openPreviewWindowButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        pogonCombo = new javax.swing.JComboBox<>();
        cilindriComboBox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        removeWorkerButton = new javax.swing.JButton();
        brojSpin = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        operacijeCB = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        unosMI = new javax.swing.JMenuItem();
        pregledMI = new javax.swing.JMenuItem();
        pracenjeMI = new javax.swing.JMenuItem();
        normativiMI = new javax.swing.JMenuItem();
        tehnologijeMI = new javax.swing.JMenuItem();
        popisMI = new javax.swing.JMenuItem();
        sistemiMI = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ucinci");
        setLocation(new java.awt.Point(400, 220));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        rnBrrnTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        rnBrrnTextField.setToolTipText("'0' ako nije vezano za Radni nalog");
        rnBrrnTextField.setMinimumSize(new java.awt.Dimension(59, 20));
        rnBrrnTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rnBrrnTextFieldFocusGained(evt);
            }
        });
        rnBrrnTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rnBrrnTextFieldActionPerformed(evt);
            }
        });

        saveProductionButton.setText("SACUVAJ");
        saveProductionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveProductionButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("BRRN");

        jLabel2.setText("BROJ");

        datumTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        datumTextField.setToolTipText("yyyy/mm/dd");

        jLabel3.setText("Datum");

        jLabel4.setText("Pomeranje datuma");

        jLabel9.setText("Operacija");

        operacijaTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel10.setText("Norma");

        normaTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        normaTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                normaTextFieldFocusGained(evt);
            }
        });

        jLabel11.setText("Naziv dela");

        nazivDelaTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        katBrojTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel12.setText("Kat broj");

        proizvedenoTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        proizvedenoTextField.setText("0");
        proizvedenoTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                proizvedenoTextFieldFocusGained(evt);
            }
        });

        radnoVremeTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        radnoVremeTextField.setText("0");
        radnoVremeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                radnoVremeTextFieldFocusGained(evt);
            }
        });

        rezijaTextField.setEditable(false);
        rezijaTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        rezijaTextField.setText("0");
        rezijaTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rezijaTextFieldFocusGained(evt);
            }
        });

        napomenaTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel14.setText("Napomena");

        jLabel15.setText("Proizvedeno");

        jLabel16.setText("R.V.");

        jLabel17.setText("Rezija - ne radi");

        pretraziButton.setText("PRETRAZI");
        pretraziButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pretraziButtonActionPerformed(evt);
            }
        });

        resetFieldsButton.setText("RESET");
        resetFieldsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetFieldsButtonActionPerformed(evt);
            }
        });

        radniciCombo.setModel(modelRadnika);

        addWorkerButton.setText("DODAJ RADNIKA");
        addWorkerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addWorkerButtonActionPerformed(evt);
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

        openPreviewWindowButton.setText("OTVORI");
        openPreviewWindowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openPreviewWindowButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Editovanje i pregled");

        jLabel6.setText("Odabir radnika");

        jLabel7.setText("Odabir pogona");

        pogonCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mašinsko", "mašinsko (cilindri)", "montaža", "montaža (cilindri)", "livnica", "glaèara", "farbara", "galvanizacija", "krojenje", "održavanje", "alatnica", "ostalo" }));

        cilindriComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Šifriranje kljuèa", "Rezanje kombinacije kljuèa", "Vezivanje kljuèa", "Èišæenje kljuèa" }));
        cilindriComboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cilindriComboBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jButton1.setText("POPUNI");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        removeWorkerButton.setText("BRISI RADNIKA");
        removeWorkerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeWorkerButtonActionPerformed(evt);
            }
        });

        brojSpin.setModel(new javax.swing.SpinnerNumberModel(0, null, null, 10));
        brojSpin.setEnabled(false);

        jLabel8.setText("Pomoæne za cilindar");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        operacijeCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0 - Nepoznata operacija" }));
        operacijeCB.setEnabled(false);
        operacijeCB.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                operacijeCBPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jMenu1.setText("File");

        unosMI.setText("Unos ucinaka");
        unosMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unosMIActionPerformed(evt);
            }
        });
        jMenu1.add(unosMI);

        pregledMI.setText("Pregled ucinaka");
        pregledMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pregledMIActionPerformed(evt);
            }
        });
        jMenu1.add(pregledMI);

        pracenjeMI.setText("Pracenje proizvodnje");
        pracenjeMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pracenjeMIActionPerformed(evt);
            }
        });
        jMenu1.add(pracenjeMI);

        normativiMI.setText("Normativi za izvoz");
        normativiMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normativiMIActionPerformed(evt);
            }
        });
        jMenu1.add(normativiMI);

        tehnologijeMI.setText("Tehnologije");
        tehnologijeMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tehnologijeMIActionPerformed(evt);
            }
        });
        jMenu1.add(tehnologijeMI);

        popisMI.setText("Popis");
        popisMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popisMIActionPerformed(evt);
            }
        });
        jMenu1.add(popisMI);

        sistemiMI.setText("Sistemi");
        sistemiMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sistemiMIActionPerformed(evt);
            }
        });
        jMenu1.add(sistemiMI);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(operacijeCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(napomenaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(saveProductionButton))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(operacijaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel9))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel10)
                                                    .addComponent(normaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(nazivDelaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel11)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(datumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel3))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel4)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(datumLevoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(datumDesnoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(proizvedenoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel16)
                                                    .addComponent(radnoVremeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel17)
                                            .addComponent(rezijaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel12)
                                            .addComponent(katBrojTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jLabel6)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton1)
                                    .addComponent(cilindriComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(openPreviewWindowButton)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addWorkerButton, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(removeWorkerButton, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pretraziButton)
                                    .addComponent(resetFieldsButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(brojSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(rnBrrnTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(pogonCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radniciCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cilindriComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton1))
                        .addComponent(jSeparator2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(openPreviewWindowButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addWorkerButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeWorkerButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pogonCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rnBrrnTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(pretraziButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(brojSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(resetFieldsButton))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(operacijeCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radniciCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(operacijaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(normaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(nazivDelaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(katBrojTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(datumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(datumLevoButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(datumDesnoButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(proizvedenoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radnoVremeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rezijaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(napomenaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(saveProductionButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Ucitava listu radnika iz baze ucinaka i cuva ih u ArrayList, zatim listu
     * implementira u dropdown listu
     *
     * @return ArrayList radnika iz baze ucinaka
     */
    ArrayList<Worker> ucitajRadnikeUDropIVratiArray() {
        ArrayList<Worker> temp = new ArrayList<Worker>();
        temp = Worker.getAllWorkers(ucinakConnection);
        modelRadnika.removeAllElements();
        for (Worker w : temp) {
            modelRadnika.addElement(w);
        }
        return temp;
    }

    /**
     * Upisuje u bazu ucinaka novi ucinak bez 'id', koji se sam generise u bazi
     *
     * @param evt event source
     */
    private void saveProductionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveProductionButtonActionPerformed
        try {
            int brrn = Integer.parseInt(rnBrrnTextField.getText());
            //int broj = Integer.parseInt(rnBrojTextField.getText());
            int broj = (int) brojSpin.getValue();
            String operacija = operacijaTextField.getText();
            double norma = Double.parseDouble(normaTextField.getText());
            String nazivDela = nazivDelaTextField.getText();
            String katBroj = katBrojTextField.getText();
            String imeRadnika = radniciCombo.getSelectedItem().toString();
            String napomena = napomenaTextField.getText();
            double proizvedeno = Double.parseDouble(proizvedenoTextField.getText());
            double radnoVreme = Double.parseDouble(radnoVremeTextField.getText());
            double rezija = Double.parseDouble(rezijaTextField.getText());
            Date datum = new Date(datumTextField.getText());
            String pogon = pogonCombo.getSelectedItem().toString();
            Production p = new Production(brrn, broj, operacija, norma, nazivDela, katBroj, imeRadnika, datum, napomena,
                    proizvedeno, radnoVreme, rezija, pogon);
            if (Production.insertInDB(p, ucinakConnection)) {
                JOptionPane.showMessageDialog(this, "Upisana je nova stavka");
            } else {
                JOptionPane.showMessageDialog(this, "Nije upisana nova stavka");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Polja BRRN, BROJ, NORMA, PROIZVEDENO, R.V. i REZIJA moraju sadrzati"
                    + " validan format broja!!!");
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, datumTextField.getText() + "   nije validan format datuma");
        } finally {

        }
    }//GEN-LAST:event_saveProductionButtonActionPerformed

    private void resetFieldsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetFieldsButtonActionPerformed
        // TODO add your handling code here:
        setFields("0", "0", "", "0", "", "", "0", "0", "0");
        setDate();
        operacijeCB.setEnabled(false);
        DefaultComboBoxModel mod = (DefaultComboBoxModel) operacijeCB.getModel();
        mod.removeAllElements();
        mod.addElement("0 - Nepoznata operacija");
    }//GEN-LAST:event_resetFieldsButtonActionPerformed

    private void setDate() {
        Date d = new Date();
        d.setTime(d.getTime() - 86400000);
        datumTextField.setText(new SimpleDateFormat("yyyy/MM/dd").format(d));
    }

    private void setFields(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, String s9) {
        rnBrrnTextField.setText(s1);
        //rnBrojTextField.setText(s2);
        brojSpin.setValue(Integer.parseInt(s2));
        operacijaTextField.setText(s3);
        normaTextField.setText(s4);
        nazivDelaTextField.setText(s5);
        katBrojTextField.setText(s6);
        proizvedenoTextField.setText(s7);
        radnoVremeTextField.setText(s8);
        rezijaTextField.setText(s9);
        napomenaTextField.setText("");
    }

    /**
     * Pretrazuje podatke za kreiranje ucinka iz glavne ISUPP baze ako postoji
     * radni nalog
     *
     * @param evt
     */
    private void pretraziButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pretraziButtonActionPerformed
        try {
            int brrn = Integer.parseInt(rnBrrnTextField.getText());
//            int broj = (int) brojSpin.getValue();
//            WorkOrder wo = WorkOrder.getWorkOrderInfo(brrn, broj, connection);
//            if (wo == null) {
//                JOptionPane.showMessageDialog(this, "Ne postoji trazeni Radni nalog");
//                setFields("0", "0", "", "", "", "", "0", "0", "0");
//            } else {
//                int woId = wo.getWorkOrderID();
//                int techOpId = wo.getTechOperationID();
//                String techOpName = wo.getTechOperationName();
//                double techOutturn = wo.getTechOutturnPerHour();
//                String prodName = wo.getProductName();
//                String prodCatNumb = wo.getProductCatalogNumber();
//                setFields(Integer.toString(woId), Integer.toString(techOpId), techOpName, Double.toString(techOutturn), prodName, prodCatNumb, "0", "0", "0");
//            }
//        } catch (NumberFormatException nfe) {
//            JOptionPane.showMessageDialog(this, "Polja BRRN i BROJ primaju samo cele brojeve!");
//        }
            lista = WorkOrder.getWorkOrderInfo(brrn, connection);
            if (lista == null || lista.size() == 0) {
                JOptionPane.showMessageDialog(this, "Ne postoji trazeni Radni nalog");
                setFields("0", "0", "", "", "", "", "0", "0", "0");
            } else {
                WorkOrder wo = lista.get(0);
                operacijeCB.setEnabled(true);
                int woId = wo.getWorkOrderID();
                //int techOpId = wo.getTechOperationID();
                //String techOpName = wo.getTechOperationName();
                //double techOutturn = wo.getTechOutturnPerHour();
                String prodName = wo.getProductName();
                String prodCatNumb = wo.getProductCatalogNumber();
                setFields(Integer.toString(woId), "0", "", "0", prodName, prodCatNumb, "0", "0", "0");
                DefaultComboBoxModel mod = (DefaultComboBoxModel) operacijeCB.getModel();
                mod.removeAllElements();
                String op = "0 - Nepoznata operacija" + " - 0 / " + (int) wo.getLansirano();
                mod.addElement(op);
                for (WorkOrder wo1 : lista) {
                    String op1 = "" + wo1.getTechOperationID() + " - " + wo1.getTechOperationName();
                    double proizvedeno = Nalog.proizvedenoStat(woId, wo1.getTechOperationID(), ucinakConnection);
                    String op2 = " - " + (int) proizvedeno + " / " + (int) wo.getLansirano();
                    int maxLen = 70;
                    int len = op1.length() + op2.length();
                    if (len > maxLen) {
                        int diffLen = len - maxLen;
                        op1 = op1.substring(0, op1.length() - diffLen);
                    }
                    mod.addElement(op1 + op2);
                }
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Polja BRRN i BROJ primaju samo cele brojeve!");
        }
    }//GEN-LAST:event_pretraziButtonActionPerformed
    ArrayList<WorkOrder> lista;

    private void popunjavanje() {
        int i = operacijeCB.getSelectedIndex() - 1;
        if (i > -1) {
            WorkOrder wo = lista.get(i);
            int woId = wo.getWorkOrderID();
            int techOpId = wo.getTechOperationID();
            String techOpName = wo.getTechOperationName();
            double techOutturn = wo.getTechOutturnPerHour();
            String prodName = wo.getProductName();
            String prodCatNumb = wo.getProductCatalogNumber();
            setFields(Integer.toString(woId), Integer.toString(techOpId), techOpName, Double.toString(techOutturn), prodName, prodCatNumb, "0", "0", "0");
        } else if (i == -1) {
            WorkOrder wo = lista.get(0);
            int woId = wo.getWorkOrderID();
            String prodName = wo.getProductName();
            String prodCatNumb = wo.getProductCatalogNumber();
            setFields(Integer.toString(woId), "0", "", "0", prodName, prodCatNumb, "0", "0", "0");
        }
    }

    /**
     * Dodaje novog radnika u bazu ucinaka
     *
     * @param evt
     */
    private void addWorkerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addWorkerButtonActionPerformed
        String newWorkerName = JOptionPane.showInputDialog(this, "Unesite puno ime radnika");
        if ((newWorkerName != null) && (!newWorkerName.equalsIgnoreCase(""))) {
            Worker newWorker = new Worker(newWorkerName);
            if (!workers.contains(newWorker)) {
                if (Worker.addWorkerToDB(newWorker, ucinakConnection)) {
                    JOptionPane.showMessageDialog(this, "Uspesno ste kreirali radnika: " + newWorker.getNameFull());
                }
                workers = ucitajRadnikeUDropIVratiArray();
            } else {
                JOptionPane.showMessageDialog(this, "Radnik " + newWorker.getNameFull() + " vec postoji samo ga nadjite");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Niste uneli ime, ili ste uneli neispravno ime");
        }
    }//GEN-LAST:event_addWorkerButtonActionPerformed

    private void rnBrrnTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rnBrrnTextFieldFocusGained
        // TODO add your handling code here:
        rnBrrnTextField.selectAll();
    }//GEN-LAST:event_rnBrrnTextFieldFocusGained

    private void normaTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_normaTextFieldFocusGained
        // TODO add your handling code here:
        normaTextField.selectAll();
    }//GEN-LAST:event_normaTextFieldFocusGained

    private void proizvedenoTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_proizvedenoTextFieldFocusGained
        // TODO add your handling code here:
        proizvedenoTextField.selectAll();
    }//GEN-LAST:event_proizvedenoTextFieldFocusGained

    private void radnoVremeTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_radnoVremeTextFieldFocusGained
        // TODO add your handling code here:
        radnoVremeTextField.selectAll();
    }//GEN-LAST:event_radnoVremeTextFieldFocusGained

    private void rezijaTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rezijaTextFieldFocusGained
        // TODO add your handling code here:
        rezijaTextField.selectAll();
    }//GEN-LAST:event_rezijaTextFieldFocusGained

    /**
     * Pomera datum za n dana
     *
     * @param n +: pomera unapred, -:pomera u nazad
     */
    private void changeDate(int n) {
        Date d = new Date();
        d.setTime(d.getTime() - 86400000);
        try {
            d = new Date(datumTextField.getText());
            d.setTime(d.getTime() + (n * 86400000));
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, datumTextField.getText() + " nije validan format datuma. Datum je resetovan na jucerasnji!");
        }
        datumTextField.setText(new SimpleDateFormat("yyyy/MM/dd").format(d));
    }

    /**
     * Pomera datum koji se nalazi u datumTextField za jedan dan unazad
     *
     * @param evt event source
     */
    private void datumLevoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datumLevoButtonActionPerformed
        // TODO add your handling code here:
        changeDate(-1);
    }//GEN-LAST:event_datumLevoButtonActionPerformed

    /**
     * Pomera datum koji se nalazi u datumTextField za jedan dan unapred
     *
     * @param evt event source
     */
    private void datumDesnoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datumDesnoButtonActionPerformed
        // TODO add your handling code here:
        changeDate(1);
    }//GEN-LAST:event_datumDesnoButtonActionPerformed

    /**
     * Otvara prozor za pregledavanje i editovanje ucinaka
     *
     * @param evt
     */
    private void openPreviewWindowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openPreviewWindowButtonActionPerformed
        Prozori.openPregled();
    }//GEN-LAST:event_openPreviewWindowButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        cilindriPopuni();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cilindriPopuni() {
        double norma;
        switch (cilindriComboBox.getSelectedIndex()) {
            case 0:
                norma = 240;
                break;
            case 1:
                norma = 103;
                break;
            case 2:
                norma = 360;
                break;
            case 3:
                norma = 120;
                break;
            default:
                norma = 0;
                break;
        }
        operacijaTextField.setText(cilindriComboBox.getSelectedItem().toString());
        normaTextField.setText(Double.toString(norma));
        rnBrrnTextField.setText("0");
        //rnBrojTextField.setText("0");
        brojSpin.setValue(0);
        radnoVremeTextField.setText("0");
        rezijaTextField.setText("0");
        napomenaTextField.setText("");
        nazivDelaTextField.setText("Kljuè");
    }

    private void cilindriComboBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cilindriComboBoxPopupMenuWillBecomeInvisible
        cilindriPopuni();
    }//GEN-LAST:event_cilindriComboBoxPopupMenuWillBecomeInvisible

    private void removeWorkerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeWorkerButtonActionPerformed
        String imeRadnika = radniciCombo.getSelectedItem().toString();
        if (JOptionPane.showConfirmDialog(null, "Sigurno obriši?", "WARNING",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // yes option
            if (Worker.deleteWorker(imeRadnika, ucinakConnection)) {
                JOptionPane.showMessageDialog(this, "Uspešno obrisan radnik " + imeRadnika);
            } else {
                JOptionPane.showMessageDialog(this, "Neuspešno brisanje radnika");
            }
            workers = ucitajRadnikeUDropIVratiArray();
        } else {
            // no option
        }
    }//GEN-LAST:event_removeWorkerButtonActionPerformed

    private void unosMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unosMIActionPerformed
        Prozori.openUnos();
    }//GEN-LAST:event_unosMIActionPerformed

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

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Prozori.unos = null;
    }//GEN-LAST:event_formWindowClosing

    private void popisMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popisMIActionPerformed
        Prozori.openPopis();
    }//GEN-LAST:event_popisMIActionPerformed

    private void sistemiMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sistemiMIActionPerformed
        Prozori.openSistemi();
    }//GEN-LAST:event_sistemiMIActionPerformed

    private void operacijeCBPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_operacijeCBPopupMenuWillBecomeInvisible
        popunjavanje();
    }//GEN-LAST:event_operacijeCBPopupMenuWillBecomeInvisible

    private void rnBrrnTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rnBrrnTextFieldActionPerformed
        pretraziButton.doClick();
    }//GEN-LAST:event_rnBrrnTextFieldActionPerformed

    //public static void main(String args[]) {
    public static UcinciWindow createAndOpen(Connection ucinakConnection, Connection connection) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UcinciWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UcinciWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UcinciWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UcinciWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        UcinciWindow uw = new UcinciWindow(ucinakConnection, connection);
        uw.setLocationRelativeTo(null);
        uw.setVisible(true);
        return uw;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addWorkerButton;
    private javax.swing.JSpinner brojSpin;
    private javax.swing.JComboBox<String> cilindriComboBox;
    private javax.swing.JButton datumDesnoButton;
    private javax.swing.JButton datumLevoButton;
    private javax.swing.JTextField datumTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField katBrojTextField;
    private javax.swing.JTextField napomenaTextField;
    private javax.swing.JTextField nazivDelaTextField;
    private javax.swing.JTextField normaTextField;
    private javax.swing.JMenuItem normativiMI;
    private javax.swing.JButton openPreviewWindowButton;
    private javax.swing.JTextField operacijaTextField;
    private javax.swing.JComboBox<String> operacijeCB;
    private javax.swing.JComboBox<String> pogonCombo;
    private javax.swing.JMenuItem popisMI;
    private javax.swing.JMenuItem pracenjeMI;
    private javax.swing.JMenuItem pregledMI;
    private javax.swing.JButton pretraziButton;
    private javax.swing.JTextField proizvedenoTextField;
    private javax.swing.JComboBox<String> radniciCombo;
    private javax.swing.JTextField radnoVremeTextField;
    private javax.swing.JButton removeWorkerButton;
    private javax.swing.JButton resetFieldsButton;
    private javax.swing.JTextField rezijaTextField;
    private javax.swing.JTextField rnBrrnTextField;
    private javax.swing.JButton saveProductionButton;
    private javax.swing.JMenuItem sistemiMI;
    private javax.swing.JMenuItem tehnologijeMI;
    private javax.swing.JMenuItem unosMI;
    // End of variables declaration//GEN-END:variables
}
