package choixVoyage.gui;

import jade.core.AID;
import choixVoyage.agent.CompanyAgent;
import choixVoyage.agent.DatabaseAgent;
import choixVoyage.model.Aeroport;
import choixVoyage.model.Compagnie;
import choixVoyage.model.Vol;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.*;

public class CompanyGUI extends JFrame {

    private CompanyAgent myAgent;
    private JTextField codeField;
    private JTextField aeroportdepField;
    private JTextField aeroportdestField;
    private JFormattedTextField datedField;
    private JFormattedTextField dateaField;
    private JTextField descpeField;
    private JTextField mataField;
    private JSpinner nbplacebField;
    private JSpinner prixplaceeField;
    private JSpinner prixplacebField;
    private JSpinner nbplaceeField;
    private JSpinner nbescaleField;

    public CompanyGUI(CompanyAgent a) {
        super(a.getLocalName());

        myAgent = a;

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(4, 4));
        p.add(new JLabel("Code:"));
        codeField = new JTextField();
        p.add(codeField);

        p.add(new JLabel("Depature airport"));
        aeroportdepField = new JTextField();
        p.add(aeroportdepField);

        p.add(new JLabel("Arrival airport"));
        aeroportdestField = new JTextField();
        p.add(aeroportdestField);

        p.add(new JLabel("Departure date"));
        datedField = new JFormattedTextField();
        datedField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
                new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        p.add(datedField);
        p.add(new JLabel("Arrival date"));
        dateaField = new JFormattedTextField();
        dateaField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
                new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        p.add(dateaField);

        p.add(new JLabel("Number of seats (Business)"));
        nbplacebField = new JSpinner();
        p.add(nbplacebField);
        p.add(new JLabel("Number of seats(Economic)"));
        nbplaceeField = new JSpinner();
        p.add(nbplaceeField);

        p.add(new JLabel("Economic class price"));
        prixplaceeField = new JSpinner();
        p.add(prixplaceeField);

        p.add(new JLabel("Business class price"));
        prixplacebField = new JSpinner();
        p.add(prixplacebField);

        p.add(new JLabel("Number of stopovers"));
        nbescaleField = new JSpinner();
        p.add(nbescaleField);

        p.add(new JLabel("Description of stopovers"));
        descpeField = new JTextField();
        p.add(descpeField);

        p.add(new JLabel("Aircraft registration number"));
        mataField = new JTextField();
        p.add(mataField);

        getContentPane().add(p, BorderLayout.CENTER);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {

                Vol v = new Vol();

                try {
                    String code = codeField.getText().trim();
                    String mataF = mataField.getText().trim();
                    String descpe = descpeField.getText().trim();
                    String aeroportdep = aeroportdepField.getText().trim();
                    String aeroportdest = aeroportdestField.getText().trim();
                    Date dated = (Date) datedField.getValue();

                    Date datea = (Date) dateaField.getValue();
                    int nbplaceb = (int) nbplacebField.getValue();
                    int nbplacee = (int) nbplaceeField.getValue();
                    int prixplacee = (int) prixplaceeField.getValue();
                    int prixplaceb = (int) prixplacebField.getValue();
                    int escale = (int) nbescaleField.getValue();

                    Aeroport dep = new Aeroport();
                    Aeroport dest = new Aeroport();
                    if (DatabaseAgent.listeAeroport == null) {
                        dep.setId(0);
                        dest.setId(1);
                    }
                    dep.setNom(aeroportdep);
                    dest.setNom(aeroportdest);

                    v.setAeroportSrc(dep);
                    v.setAeroportDest(dest);
                    v.setCode(code);
                    v.setMatAvion(mataF);
                    v.setPrixeco(prixplacee);
                    v.setDescrEscale(descpe);
                    v.setPrixBuss(prixplaceb);
                    v.setDatArrivee(datea);
                    v.setDepart(dated);
                    v.setNbPlaceEco(nbplacee);
                    v.setNbEscale(escale);
                    v.setNbPlaceBuss(nbplaceb);

                    myAgent.updateCatalogue(v, myAgent.getName(), code, mataF, descpe, aeroportdep, aeroportdest, dated,
                            datea, nbplaceb, nbplacee, prixplacee, prixplaceb, escale);

                    codeField.setText("");
                    mataField.setText("");
                    descpeField.setText("");
                    aeroportdepField.setText("");
                    aeroportdestField.setText("");
                    datedField.setValue(new Date());
                    dateaField.setValue(new Date());
                    nbplacebField.setValue(0);
                    nbplaceeField.setValue(0);
                    prixplaceeField.setValue(0);
                    prixplacebField.setValue(0);
                    nbescaleField.setValue(0);

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(CompanyGUI.this, "Invalid values. " + e.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        p = new JPanel();
        p.add(addButton);
        getContentPane().add(p, BorderLayout.SOUTH);

        // Make the agent terminate when the user closes
        // the GUI using the button on the upper right corner
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                myAgent.doDelete();
            }
        });

        setResizable(false);
    }

    public void showGui() {
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) screenSize.getWidth() / 2;
        int centerY = (int) screenSize.getHeight() / 2;
        setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
        super.setVisible(true);
    }

}
