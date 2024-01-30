package choixVoyage.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import choixVoyage.gui.TouristGUI;
import choixVoyage.gui.TouristGUIReponse;
import choixVoyage.gui.ResultJFrame;
import choixVoyage.model.CriteresClient;
import choixVoyage.model.Reservation;

public class TouristAgent extends Agent {

    /**
     *
     */
    private static final long serialVersionUID = 4992714994039891382L;
    private TouristGUI myGui;
    //private TouristGUIReponse myGuiReponse;
    private ResultJFrame myResultJFrame;
    private static final String TYPEAGENT = "agent-client";
    private CriteresClient userRequest;
    private Reservation userConfirmReservation;

    // Mettez initialisations d'agent
    @Override
    protected void setup() {

        myGui = new TouristGUI(this);
        myGui.setVisible(true);

        String str = "Bonjour! Agent - client " + getAID().getName() + " est pret.";
        System.out.println(str);
        CriteresClient besoinClient = new CriteresClient();
        /*besoinClient.setAeroportDepart("Saigon");
	        besoinClient.setTypevol("Alle-Retour");
	        besoinClient.setDatRetour(new Date());
	        besoinClient.setDatDepart(new Date());
	        besoinClient.setAeroportArrivee("lion");
	        besoinClient.setClassevol("Business");
	        besoinClient.setNbplace(1);
	        setUserRequest(besoinClient);*/
        DFAgentDescription askDFD = new DFAgentDescription();

        ServiceDescription askSD = new ServiceDescription();
        askSD.setType(TYPEAGENT);
        askSD.setName("Agent" + TYPEAGENT);
        askDFD.addServices(askSD);
        try {
            DFService.register(this, askDFD);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        addBehaviour(new TickerBehaviour(this, 1500) {

            @Override
            protected void onTick() {
                // TODO Auto-generated method stub
                if (userRequest == null) {
                    System.out.println("Tourist Agent : no request");
                }
                myAgent.addBehaviour(new RequestPerformer());

            }
        });
    }

    public void requestCompagnie(CriteresClient u) {
        userRequest = u;
    }

    // Mettez les operations de nettoyage agents
    @Override
    protected void takeDown() {
        // Printout a dismissal message
        System.out.println(TYPEAGENT + getAID().getName() + " end.");
    }

    /**
     * Inner class RequestPerformer. Ce comportement est utilise par les agents
     * utilisateurs de demander.
     */
    private class RequestPerformer extends Behaviour {

        private MessageTemplate mt; // Le modèle pour recevoir des réponses

        @Override
        public void action() {

            if (userRequest != null) {
                //recupérer la demande et envoyer a l'agent chercheur
                //envoyer la compagnie de reservation a l'agent de reservation
                ACLMessage requestCompagnie = new ACLMessage(ACLMessage.REQUEST);
                //
                //MessageTemplate tpl = MessageTemplate.MatchReceiver(SearchAgent)              
                DFAgentDescription tmp = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("agent-chercheur");
                tmp.addServices(sd);
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, tmp);
                    if (result.length > 0) {
                        requestCompagnie.addReceiver(result[0].getName());
                        try {
                            requestCompagnie.setContentObject(userRequest);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        requestCompagnie.setConversationId("agent-chercheur");
                        requestCompagnie.setReplyWith("agent-chercheur" + System.currentTimeMillis());
                        myAgent.send(requestCompagnie);
                        System.out.println(TYPEAGENT + " Envoi de la recherche de vol a l'agent chercheur");

                        //A la fin mettre le userRequest hotel a null
                        setUserRequest(null);
                    }
                } catch (FIPAException fe) {
                    // TODO: handle exception
                    fe.printStackTrace();
                }
                //
            }
            //System.out.println(TYPEAGENT + " Fin premier if");
            if (userConfirmReservation != null) {

                ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                //
                DFAgentDescription tmp = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("agent-reservation");
                tmp.addServices(sd);
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, tmp);
                    if (result.length > 0) {
                        order.addReceiver(result[0].getName());
                    }

                } catch (FIPAException fe) {
                    // TODO: handle exception
                    fe.printStackTrace();
                }
                //
                try {
                    order.setContentObject(userConfirmReservation);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                order.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                order.setConversationId("agent-reservation");
                order.setReplyWith("agent-reservation" + System.currentTimeMillis());
                myAgent.send(order);
                System.out.println(TYPEAGENT + " Envoi de la confirmation de reservation a l'agent de reservation");
                //A la fin mettre le userConfirm vol a null
                setUserConfirmReservation(null);
            }

            //Voir s'il ya un nouveau message de l'agent chercheur
            MessageTemplate mtmt = MessageTemplate.MatchConversationId(TYPEAGENT);
            ACLMessage repAgentRecherche = myAgent.receive(mtmt);
            if (repAgentRecherche != null) {
                //une reponse recu
                System.out.println(TYPEAGENT + " Réception de la réponse de l'agent-chercheur");
                if (repAgentRecherche.getPerformative() == ACLMessage.PROPOSE) {
                    try {
                        ArrayList<Reservation> reservationPropose = new ArrayList<>();
                        reservationPropose.clear();
                        reservationPropose = (ArrayList<Reservation>) repAgentRecherche.getContentObject();
                        if (reservationPropose != null) {
                            //Envoyer la proposition au client
                            myResultJFrame = new ResultJFrame((TouristAgent) myAgent, reservationPropose);
                            myResultJFrame.setVisible(true);
                            myGui.setVisible(false);
                            System.out.println(TYPEAGENT + " Réponse non vide");
                            //renvoyer a l'utilisateur de la meilleur offre retourné
                        } else {
                            System.out.println(TYPEAGENT + " Réponse vide");
                            myGui.infoBox("Aucune offre ne correspond à votre demande!");
                            myGui.clearZoneEdit();
                            //renvoyer a l'utilsateur de l'indisponibilité de la demande
                        }
                    } catch (UnreadableException ex) {

                    }
                }

            }
            ACLMessage repReservationAgent = repAgentRecherche; //myAgent.receive(mt);
            if (repReservationAgent != null) {
                //une reponse recu
                if (repReservationAgent.getPerformative() == ACLMessage.CONFIRM) {
                    try {
                        //recuperer la confirmation pour l'envoyer au client
                        Reservation reservationPropose = (Reservation) repReservationAgent.getContentObject();
                        if (reservationPropose != null) {
                            System.out.println(reservationPropose.getClient());
                            System.out.println(reservationPropose.getCritere());
                            System.out.println(reservationPropose.getCompagnie());
                            sendEmail(reservationPropose);
                            System.out.println(TYPEAGENT + "Votre réservation a été bien pris en compte!");
                            JOptionPane.showMessageDialog(null, "Votre réservation a été bien pris en compte! ", "Succes", JOptionPane.INFORMATION_MESSAGE);
                            myResultJFrame.activeWindow(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "Désolé, une erreur s'est  produite lors de la réservation", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println(TYPEAGENT + " Désolé, une erreur s'est  produite lors de la réservation");

                        }
                        myGui.setVisible(true);
                        myGui.clearZoneEdit();
                    } catch (UnreadableException ex) {
                        Logger.getLogger(TouristAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

        }

        @Override
        public boolean done() {
            return true;
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    } // End of inner class RequestPerformer

    public CriteresClient getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(CriteresClient userRequest) {
        this.userRequest = userRequest;
    }

    public Reservation getUserConfirmReservation() {
        return userConfirmReservation;
    }

    public void setUserConfirmReservation(Reservation userConfirmReservation) {
        this.userConfirmReservation = userConfirmReservation;
    }

    public void sendEmail(Reservation reservation) {
        String emailFrom = "test.transvie@gmail.com";
        String password = "888Da6c4nBPXvE2";
        String emailTo = reservation.getClient().getEmail();
        System.out.println(emailTo);
        String sujet = "Reservation d'un vol";
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\nType de vol : " + reservation.getCritere().getTypevol());
        stringBuilder.append("\nClasse Vol : " + reservation.getCritere().getClassevol());
        if (reservation.getCritere().getClassevol() == "Business") {
            stringBuilder.append("\nPrix : " + reservation.getVol().getPrixBuss());
        } else {
            stringBuilder.append("\nPrix : " + reservation.getVol().getPrixeco());
        }
        stringBuilder.append("\nNom : " + reservation.getClient().getNom());
        stringBuilder.append("\nPrénom : " + reservation.getClient().getPrenom());
        stringBuilder.append("\nNom de la compagnie : " + reservation.getCompagnie());
        stringBuilder.append("\nAeroport de depart: " + reservation.getAeroportDepart());
        stringBuilder.append("\nAeroport d'arrivee: " + reservation.getAeroportArrivee());

        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String formatDate = simpleDateFormat.format(reservation.getDatArrivee());
        stringBuilder.append("\nDate de départ: " + formatDate);

        formatDate = simpleDateFormat.format(reservation.getDepart());
        stringBuilder.append("\nDate d'arrivée: " + formatDate);
        String message = stringBuilder.toString();

        Properties props = new Properties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.ssl.checkserveridentity", "false");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, password);
            }

        });

        try {
            Message mailMessage = new MimeMessage(session);
            mailMessage.setFrom(new InternetAddress(emailFrom));
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));

            mailMessage.setSubject(sujet);
            mailMessage.setText(message);
            Transport.send(mailMessage);
            System.out.println("Email envoye avec succes");
        } catch (MessagingException e) {
            System.out.println("Unable to send mail" + e.getMessage());
            //throw new RuntimeException(e);
        }
    }

}
