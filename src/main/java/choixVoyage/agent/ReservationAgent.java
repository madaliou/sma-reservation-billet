package choixVoyage.agent;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import choixVoyage.model.Reservation;

public class ReservationAgent extends Agent {

    /**
     *
     */
    private static final long serialVersionUID = -6138464249953849272L;
    private static final String TYPEAGENT = "agent-reservation";

    @Override
    public void setup() {
        String str = "Hi! Agent - reservation " + getAID().getName() + " est pret.";
        // Imprimer un message de bienvenue
        System.out.println(str);
        DFAgentDescription askDFD = new DFAgentDescription();
        askDFD.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(TYPEAGENT);
        sd.setName("Agent - " + TYPEAGENT);
        askDFD.addServices(sd);

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
                myAgent.addBehaviour(new RequestPerformer());

            }
        });
    }

    private class RequestPerformer extends Behaviour {

        private MessageTemplate mt; // Le modèle pour recevoir des réponses

        @Override
        public void action() {
            mt = MessageTemplate.MatchConversationId(TYPEAGENT);
            ACLMessage repTouristAgent = myAgent.receive(mt);
            if (repTouristAgent != null) {
                // une reponse recu

                if (repTouristAgent.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                    try {
                        Reservation userConfirmReservation = (Reservation) repTouristAgent.getContentObject();
                        // recuperer le choix de la chambre du client et l'envoyer a l'agent base de
                        // donnée
                        ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                        DFAgentDescription tmp = new DFAgentDescription();
                        ServiceDescription sd = new ServiceDescription();
                        sd.setType("agent-basededonne");
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
                        } catch (IOException ex) {
                            Logger.getLogger(ReservationAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        order.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        order.setConversationId("agent-basededonne");
                        order.setReplyWith("agent-basededonne" + System.currentTimeMillis());
                        myAgent.send(order);
                        System.out.println(TYPEAGENT + " Envoi du vol choisit a l'agent "
                                + "de base de données pour vérification de la disponibilité");
                        // A la fin mettre le userConfirm hotel a null
                        // setUserConfirmHotel(null);
                    } catch (Exception e) {
                    }
                }
            }

            // mt = MessageTemplate.MatchConversationId("agent-basededonne");
            ACLMessage repAgentBaseDeDonne = repTouristAgent;// myAgent.receive(mt);
            if (repAgentBaseDeDonne != null) {
                if (repAgentBaseDeDonne.getPerformative() == ACLMessage.CONFIRM) {
                    try {
                        Reservation reservationPropose = (Reservation) repAgentBaseDeDonne.getContentObject();
                        // recuperer le choix du vol du client et l'envoyer a l'agent facilitateur
                        ACLMessage order = new ACLMessage(ACLMessage.INFORM);
                        DFAgentDescription tmp = new DFAgentDescription();
                        ServiceDescription sd = new ServiceDescription();
                        sd.setType("agent-facilitateur");
                        tmp.addServices(sd);
                        order.setConversationId("agent-facilitateur");
                        order.setReplyWith("agent-facilitateur" + System.currentTimeMillis());
                        tmp.addServices(sd);
                        if (reservationPropose == null) {// Envoi du message d'echec au client
                            sd.setType("agent-client");
                            tmp.addServices(sd);
                            order.setConversationId("agent-client");
                            order.setReplyWith("agent-client" + System.currentTimeMillis());
                            order.setPerformative(ACLMessage.CONFIRM);
                            tmp.addServices(sd);
                        }
                        try {
                            DFAgentDescription[] result = DFService.search(myAgent, tmp);
                            if (result.length > 0) {
                                order.addReceiver(result[0].getName());
                            }
                        } catch (FIPAException fe) {
                            // TODO: handle exception
                            fe.printStackTrace();
                        }
                        /*
                         * if (reservationPropose == null) { System.err.println(TYPEAGENT +
                         * " reservation null"); }
                         */
                        try {
                            order.setContentObject(reservationPropose);
                            myAgent.send(order);
                            System.out.println(
                                    TYPEAGENT + " Envoi de la confirmation de reservation a l'agent facilitateur");

                        } catch (Exception e) {
                        }

                    } catch (UnreadableException ex) {
                        Logger.getLogger(ReservationAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            ACLMessage repTouristAgencyAgent = repTouristAgent; // myAgent.receive(mt);
            if (repTouristAgencyAgent != null && repTouristAgencyAgent.getPerformative() == ACLMessage.INFORM) {
                try {
                    Reservation reservationPropose = (Reservation) repTouristAgencyAgent.getContentObject();

                    ACLMessage order = new ACLMessage(ACLMessage.CONFIRM);
                    DFAgentDescription tmp = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();

                    sd.setType("agent-client");
                    tmp.addServices(sd);
                    order.setConversationId("agent-client");
                    order.setReplyWith("agent-client" + System.currentTimeMillis());
                    tmp.addServices(sd);
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, tmp);
                        if (result.length > 0) {
                            order.addReceiver(result[0].getName());
                            System.out.println(TYPEAGENT + " Un agent-client trouvé");
                        }
                    } catch (FIPAException fe) {
                        // TODO: handle exception
                        fe.printStackTrace();
                    }
                    try {
                        order.setContentObject(reservationPropose);
                        myAgent.send(order);
                        System.out.println(TYPEAGENT + " Envoi de la confirmation de reservation a l'agent-client");

                    } catch (Exception e) {
                    }

                } catch (UnreadableException ex) {
                    Logger.getLogger(ReservationAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        @Override
        public boolean done() {
            return true;
            // throw new UnsupportedOperationException("Not supported yet."); //To change
            // body of generated methods, choose Tools | Templates.
        }

    }

    @Override
    public void beforeMove() {

    }

    @Override
    public void afterMove() {

    }

    @Override
    public void takeDown() {

        System.out.println(TYPEAGENT + getAID().getName() + " termine.");
    }

}
