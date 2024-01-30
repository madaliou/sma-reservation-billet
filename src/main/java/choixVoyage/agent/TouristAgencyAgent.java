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

public class TouristAgencyAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private static final String TYPEAGENT = "agent-facilitateur";

	@Override
	public void setup() {
		String str = "Bonjour! Agent - facilitateur " + getAID().getName() + " est pret.";
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

			private static final long serialVersionUID = 1L;

			@Override
			protected void onTick() {
				// TODO Auto-generated method stub
				myAgent.addBehaviour(new OfferRequestsServer());

			}
		});
	}

	public class OfferRequestsServer extends Behaviour {

		private static final long serialVersionUID = 1L;
		private MessageTemplate mt; // Le modèle pour recevoir des réponses

		@Override
		public void action() {
			mt = MessageTemplate.MatchConversationId(TYPEAGENT);
			ACLMessage repAgentFacile = myAgent.receive(mt);
			if (repAgentFacile != null) {
				if (repAgentFacile.getPerformative() == ACLMessage.INFORM) {
					try {
						Reservation reservationPropose = (Reservation) repAgentFacile.getContentObject();
						// recuperer le choix de la chambre du client et l'envoyer a l'agent Compagnie
						ACLMessage order = new ACLMessage(ACLMessage.INFORM);
						DFAgentDescription tmp = new DFAgentDescription();
						ServiceDescription sd = new ServiceDescription();
						sd.setType("agent-compagnie");
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
						if (reservationPropose == null) {
							System.err.println(TYPEAGENT + " reservation null");
						}
						//
						try {
							order.setContentObject(reservationPropose);
						} catch (IOException ex) {
							Logger.getLogger(ReservationAgent.class.getName()).log(Level.SEVERE, null, ex);
						}

						order.setPerformative(ACLMessage.INFORM);
						order.setConversationId("agent-compagnie");
						order.setReplyWith("agent-compagnie" + System.currentTimeMillis());
						myAgent.send(order);
						System.out.println(TYPEAGENT + " Envoi de la réservation de vol choisit a l'agent compagnie");
					} catch (UnreadableException ex) {
						Logger.getLogger(TouristAgencyAgent.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}

			ACLMessage repCompanyAgent = repAgentFacile; // myAgent.receive(mt);
			if (repCompanyAgent != null) {
				if (repCompanyAgent.getPerformative() == ACLMessage.CONFIRM) {
					try {
						Reservation reservationPropose = (Reservation) repCompanyAgent.getContentObject();

						ACLMessage order = new ACLMessage(ACLMessage.CONFIRM);
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
							order.setContentObject(reservationPropose);
						} catch (IOException ex) {
							Logger.getLogger(ReservationAgent.class.getName()).log(Level.SEVERE, null, ex);
						}

						order.setConversationId("agent-basededonne");
						order.setReplyWith("agent-basededonne" + System.currentTimeMillis());
						myAgent.send(order);
						System.out.println(TYPEAGENT + " Envoi de la confirmation a agent-basededonne");
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
							order.setContentObject(reservationPropose);
						} catch (IOException ex) {
							Logger.getLogger(ReservationAgent.class.getName()).log(Level.SEVERE, null, ex);
						}

						order.setPerformative(ACLMessage.INFORM);
						order.setConversationId("agent-reservation");
						order.setReplyWith("agent-reservation" + System.currentTimeMillis());
						myAgent.send(order);
						System.out.println(TYPEAGENT + " Envoi de la confirmation a agent-reservation");

					} catch (UnreadableException ex) {
						Logger.getLogger(TouristAgencyAgent.class.getName()).log(Level.SEVERE, null, ex);
					}
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

}
