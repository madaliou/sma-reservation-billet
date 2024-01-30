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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.persistence.sessions.serializers.Serializer;

import choixVoyage.model.CriteresClient;
import choixVoyage.model.Vol;
import choixVoyage.model.Compagnie;
import choixVoyage.gui.CompanyGUI;
import choixVoyage.model.Aeroport;
import choixVoyage.model.Reservation;

public class DatabaseAgent extends Agent {

    /**
     *
     */
    private static final long serialVersionUID = -9188607069361821897L;
    /**
     *
     */

    public static ArrayList<Reservation> reservation = new ArrayList<Reservation>();
    public static ArrayList<Vol> listeVol = new ArrayList<Vol>();
    public static ArrayList<Vol> listeAeroport = new ArrayList<Vol>();
    public static ArrayList<CriteresClient> listeCritere = new ArrayList<CriteresClient>();

    private static final String TYPEAGENT = "agent-basededonne";
    private Reservation reservationVerifier;

    @Override
    protected void setup() {

        String str = "Bonjour! Agent - basededonne " + getAID().getName() + " est pret.";

        System.out.println(str);
        DFAgentDescription askDFD = new DFAgentDescription();
        askDFD.setName(getAID());

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
                myAgent.addBehaviour(new DatabaseAgent.ReceiveMessageReqFromchercheur());
            }
        });

    }

    private class ReceiveMessageReqFromchercheur extends Behaviour {

        // Le modèle pour recevoir des réponses
        private MessageTemplate mt;

        @Override
        public void action() {

            // Message de l'agent chercheur
            mt = MessageTemplate.MatchConversationId("agent-basededonne");
            ACLMessage reqFromAgent = myAgent.receive(mt);
            ArrayList<Reservation> listMeilleurReservation = new ArrayList<Reservation>();
            listMeilleurReservation.clear();
            List<Reservation> listeReservation = new ArrayList<Reservation>();
            if (reqFromAgent != null) {
                // une requete recu
                if (reqFromAgent.getPerformative() == ACLMessage.REQUEST) {
                    try {

                        CriteresClient compagniePredicat = (CriteresClient) reqFromAgent.getContentObject();

                        if (compagniePredicat != null) {
                            listeCritere.add(compagniePredicat);
                            ArrayList<Vol> lv = new ArrayList<Vol>();
                            lv.clear();
                            Vol vl = new Vol();
                            if (listeVol.size() > 0) {
                                for (int i = 0; i < listeVol.size(); i++) {
                                    // vl=listeVol.get(i);
                                    System.out.println("vl.getAeroportSrc().getNom() = "
                                            + listeVol.get(i).getAeroportSrc().getNom());
                                    System.out.println("compagniePredicat.getAeroportDepart() = "
                                            + compagniePredicat.getAeroportDepart());

                                    if ((listeVol.get(i).getAeroportSrc().getNom().trim()
                                            .equalsIgnoreCase(compagniePredicat.getAeroportDepart().trim()))
                                            && (listeVol.get(i).getAeroportDest().getNom().trim()
                                                    .equalsIgnoreCase(compagniePredicat.getAeroportArrivee().trim()))) {
                                        // if(listeVol.get(i).getDepart()==compagniePredicat.getDatDepart() ){

                                        if ((compagniePredicat.getClassevol().trim().equalsIgnoreCase("Economic"))
                                                && (listeVol.get(i).getNbPlaceEco() >= compagniePredicat
                                                        .getNbplace())) {
                                            lv.add(listeVol.get(i));
                                        }
                                        if ((compagniePredicat.getClassevol().trim().equalsIgnoreCase("Business"))
                                                && (listeVol.get(i).getNbPlaceBuss() >= compagniePredicat
                                                        .getNbplace())) {
                                            lv.add(listeVol.get(i));
                                            System.out.println("Ok equal");

                                        }
                                        // }

                                    }
                                }
                            } else {
                                System.out.println("Aucun Vol enregistré");
                            }

                            System.out.println(lv.size());
                            if (lv.size() == 0) {
                                lv = null;
                                listMeilleurReservation = null;
                            } else {
                                for (int i = 0; i < lv.size(); i++) {
                                    Vol v = new Vol();
                                    v = lv.get(i);

                                    listMeilleurReservation.add(new Reservation());
                                    listMeilleurReservation.get(listMeilleurReservation.size() - 1).setVol(v);
                                    listMeilleurReservation.get(listMeilleurReservation.size() - 1)
                                            .setDatArrivee(v.getDatArrivee());
                                    listMeilleurReservation.get(listMeilleurReservation.size() - 1)
                                            .setDepart(compagniePredicat.getDatDepart());
                                    listMeilleurReservation.get(listMeilleurReservation.size() - 1)
                                            .setAeroportDepart(v.getAeroportSrc());
                                    listMeilleurReservation.get(listMeilleurReservation.size() - 1)
                                            .setCritere(compagniePredicat);
                                    listMeilleurReservation.get(listMeilleurReservation.size() - 1)
                                            .setNbplace(compagniePredicat.getNbplace());
                                    listMeilleurReservation.get(listMeilleurReservation.size() - 1)
                                            .setCompagnie(v.getCompagnie());
                                    System.out.println(
                                            "xxxxxxxxxxx compagnie= " + listMeilleurReservation.get(i).getCompagnie());
                                    System.out.println(
                                            "xxxxxxxxxxx vritere = " + listMeilleurReservation.get(i).getCritere());

                                }
                            }

                            ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                            DFAgentDescription tmp = new DFAgentDescription();
                            ServiceDescription sd = new ServiceDescription();
                            sd.setType("agent-chercheur");
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

                                System.out.println(listMeilleurReservation.size());
                                cfp.setContentObject(listMeilleurReservation);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            cfp.setPerformative(ACLMessage.PROPOSE);
                            cfp.setConversationId("agent-chercheur");
                            sd.setType("agent-chercheur");
                            cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Valeur
                            // unique
                            myAgent.send(cfp);
                            System.out.println(TYPEAGENT + " envoi de la réponse a l'agent chercheur");
                        }
                    } catch (UnreadableException ex) {
                        ex.printStackTrace();
                        Logger.getLogger(TouristAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            reservationVerifier = new Reservation();
            if (reqFromAgent != null) {
                // une reponse recu
                if (reqFromAgent.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                    try {
                        // recuperer l'objet de l'agent reservation
                        reservationVerifier = (Reservation) reqFromAgent.getContentObject();
                        // afficheReservation(reservationVerifier);
                        if (reservationVerifier != null) {
                            Reservation reserv = new Reservation();
                            /*
                             * listeReservation = listMeilleurReservation; if (listeReservation.isEmpty()) {
                             * reservationVerifier = null; }
                             */

                            ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                            DFAgentDescription tmp = new DFAgentDescription();
                            ServiceDescription sd = new ServiceDescription();
                            sd.setType("agent-reservation");
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
                                cfp.setContentObject(reservationVerifier);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            cfp.setPerformative(ACLMessage.CONFIRM);
                            cfp.setConversationId("agent-reservation");
                            cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Valeur
                            // unique
                            myAgent.send(cfp);
                            System.out.println(TYPEAGENT + " Envoi de la reponse de confirmation a l'agent reservation"
                                    + reservationVerifier);

                        }
                    } catch (UnreadableException ex) {
                        Logger.getLogger(TouristAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (reqFromAgent.getPerformative() == ACLMessage.CONFIRM) {
                    try {
                        // recuperer l'objet de l'agent reservation
                        reservationVerifier = (Reservation) reqFromAgent.getContentObject();
                        if (reservationVerifier != null && reservationVerifier.getClient() != null) {
                            reservationVerifier.setClient(reservationVerifier.getClient());
                            reservationVerifier.setCritere(reservationVerifier.getCritere());
                            reservationVerifier.setCompagnie(reservationVerifier.getCompagnie());
                            reservation.add(reservationVerifier);
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
        }

    }

    @Override
    protected void beforeMove() {
        try {
            System.out
                    .println("Avant de migrer  ..... du container " + this.getContainerController().getContainerName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void afterMove() {
        try {
            System.out.println(
                    "apres de migrer  ..... vers le container " + this.getContainerController().getContainerName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("avant de mourir .....");
    }

    Vol meilleurVol(ArrayList<Vol> vols) {

        if (vols.isEmpty()) {
            return null;
        }
        Vol mc = vols.get(0);
        for (Vol vol : vols) {
            if (vol.getPrixeco() < mc.getPrixeco()) {
                mc = vol;
            }
        }
        return mc;
    }

    void afficheBesoinClient(CriteresClient bs) {
        System.out.println("Tourist need display");
        // System.out.println("Nom = " + bs.getNom());
        System.out.println("Departure airport = " + bs.getAeroportDepart());
        System.out.println("Arrival airport = " + bs.getAeroportArrivee());
        System.out.println("Flight type = " + bs.getTypevol());
        System.out.println("Departure date = " + bs.getDatDepart());
        System.out.println("Arrival date = " + bs.getDatRetour());
        System.out.println("Departure aiport = " + bs.getAeroportDepart());
        System.out.println("Arrival airport = " + bs.getAeroportArrivee());
    }

    void afficheVol(Vol bs, String classe) {
        System.out.println("Flight display");
        System.out.println("Arrival airport = " + bs.getAeroportSrc().getNom());
        System.out.println("Landing airport = " + bs.getAeroportDest().getNom());
        if (classe == "Business") {
            System.out.println("Business class price = " + bs.getPrixBuss());
            System.out.println("Number of seat(Business) = " + bs.getNbPlaceBuss());
        } else {
            System.out.println("Business class price = " + bs.getPrixeco());
            System.out.println("Number of seat (Economic) = " + bs.getNbPlaceEco());
        }
        System.out.println("Number of stopovers = " + bs.getNbEscale());
        System.out.println("Description of stopvers = " + bs.getDescrEscale());

    }

    void afficheReservation(Reservation bs) {
        System.out.println("Reservation display");
        System.out.println("Company name = " + bs.getCompagnie());
        System.out.println("Flight display");
        System.out.println("Arrival airport = " + bs.getVol().getAeroportSrc().getNom());
        System.out.println("Landing airport = " + bs.getVol().getAeroportDest().getNom());
        if (bs.getCritere().getClassevol() == "Business") {
            System.out.println("Business class price = " + bs.getVol().getPrixBuss());
            System.out.println("Number of seat(Business) = " + bs.getVol().getNbPlaceBuss());
        } else {
            System.out.println("Business class price = " + bs.getVol().getPrixeco());
            System.out.println("Number of seat (Economic) = " + bs.getVol().getNbPlaceEco());
        }
        System.out.println("Number of stopover = " + bs.getVol().getNbEscale());
        System.out.println("Description of stopovers  = " + bs.getVol().getDescrEscale());
        System.out.println("Tourist = " + bs.getClient().getNom());
        System.out.println("Email Tourist = " + bs.getClient().getEmail());
        System.out.println("Arrival date = " + bs.getDatArrivee());
        System.out.println("Departure date = " + bs.getDepart());
        System.out.println("Arrival airport = " + bs.getAeroportArrivee());
        System.out.println("Arrival airport = " + bs.getAeroportDepart());
    }

}
