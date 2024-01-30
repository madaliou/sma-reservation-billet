package choixVoyage.agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.util.leap.Serializable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import choixVoyage.gui.CompanyGUI;
import choixVoyage.model.Aeroport;
import choixVoyage.model.Compagnie;
import choixVoyage.model.Reservation;
import choixVoyage.model.Vol;

public class CompanyAgent extends Agent {

    /**
     *
     */
    private static final long serialVersionUID = 2696951589596250936L;

    private static final String TYPEAGENT = "agent-compagnie";

    // The GUI by means of which the user can add books in the catalogue
    private CompanyGUI myGui;

    @Override
    protected void setup() {

        // Create and show the GUI 
        myGui = new CompanyGUI(this);
        myGui.showGui();
        String str = "Hi! Agent - company " + getAID().getName() + " is ready";
        // Imprimer un message de bienvenue
        System.out.println(str);
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
                myAgent.addBehaviour(new RequestPerformer());
            }
        });

    }

    // Mettez les operations de nettoyage agents
    @Override
    protected void takeDown() {
        // Printout a dismissal message
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        // Close the GUI
        myGui.dispose();
        // Printout a dismissal message
        System.out.println(TYPEAGENT + getAID().getName() + " terminating.");
    }

    /**
     * Inner class RequestPerformer. Ce comportement est utilise par les agents
     * utilisateurs de demander.
     */
    private class RequestPerformer extends Behaviour {

        private MessageTemplate mt; // Le modèle pour recevoir des réponses

        @Override
        public void action() {
            //Voir s'il ya un nouveau message de l'agent client
            mt = MessageTemplate.MatchConversationId(TYPEAGENT);
            ACLMessage repAgentRecherche = myAgent.receive(mt);
            if (repAgentRecherche != null) {
                //une reponse recu
                if (repAgentRecherche.getPerformative() == ACLMessage.INFORM) {
                    try {
                        //recuperer la requete de recherche du client et l'envoyer a l'agent base de donnée
                        Reservation reservationDemande = (Reservation) repAgentRecherche.getContentObject();
                        if (reservationDemande != null) {

                            ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                            DFAgentDescription tmp = new DFAgentDescription();
                            ServiceDescription sd = new ServiceDescription();
                            sd.setType("agent-facilitateur");
                            tmp.addServices(sd);
                            try {
                                DFAgentDescription[] result = DFService.search(myAgent, tmp);
                                if (result.length > 0) {
                                    cfp.addReceiver(result[0].getName());
                                }

                            } catch (FIPAException fe) {
                                // TODO: handle exception
                                fe.printStackTrace();
                            }
                            try {
                                cfp.setContentObject(reservationDemande);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            cfp.setConversationId("agent-facilitateur");
                            cfp.setPerformative(ACLMessage.CONFIRM);
                            cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Valeur
                            // unique
                            myAgent.send(cfp);
                            System.out.println(TYPEAGENT + " Message de confirmation envoyé au facilitateur");
                        } else {
                            System.out.println(TYPEAGENT + " Message vide de reservation recu");
                        }
                    } catch (UnreadableException ex) {
                        Logger.getLogger(TouristAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        @Override
        public boolean done() {
            return true;
            //System.out.println("Message de confirmation envoyé au facilitateur");            
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    /**
     * This is invoked by the GUI when the user add a new compagnie with vol for
     * sale
     */
    public void updateCatalogue(Vol v, String a, String code, String mataF, String descpe, String aeroportdep, String aeroportdest, Date dated, Date datea,
            int nbplaceb, int nbplacee, int prixplacee, int prixplaceb, int escale) {
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                v.setCompagnie(a);
                DatabaseAgent.listeVol.add(v);
                System.out.println("Insertion du vol: " + code + " " + mataF + " " + descpe + " " + aeroportdep + " " + aeroportdest + " " + dated + " " + datea + " "
                        + nbplaceb + " " + nbplacee + " " + prixplacee + " " + prixplaceb + " " + escale);
            }
        });
    }

}
