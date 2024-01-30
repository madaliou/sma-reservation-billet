package choixVoyage.creation;

import java.util.ArrayList;
import java.util.Hashtable;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class ChoixVoyage {

    public ArrayList lc;
    public ArrayList lv;
    public ArrayList la;
    private Hashtable catalogue;

    public Hashtable getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(Hashtable catalogue) {
        this.catalogue = catalogue;
    }

    public static void main(String[] args) {

        try {

            jade.core.Runtime rt = jade.core.Runtime.instance();
            Properties p = new ExtendedProperties();
            p.setProperty(Profile.GUI, "true");
            ProfileImpl proImpl = new ProfileImpl(p);
            AgentContainer agentContainer = rt.createMainContainer(proImpl);
            AgentController agentController = agentContainer.createNewAgent("TOURIST", choixVoyage.agent.TouristAgent.class.getName(), new Object[]{});
            agentController.start();
            agentController = agentContainer.createNewAgent("SEARCHER", choixVoyage.agent.SearchAgent.class.getName(), new Object[]{});
            agentController.start();
            agentController = agentContainer.createNewAgent("DATABASE", choixVoyage.agent.DatabaseAgent.class.getName(), new Object[]{});
            agentController.start();
            agentController = agentContainer.createNewAgent("RESERVATION", choixVoyage.agent.ReservationAgent.class.getName(), new Object[]{});
            agentController.start();
            agentController = agentContainer.createNewAgent("TOURISTAGENCY", choixVoyage.agent.TouristAgencyAgent.class.getName(), new Object[]{});
            agentController.start();
            agentController = agentContainer.createNewAgent("COMPANY", choixVoyage.agent.CompanyAgent.class.getName(), new Object[]{});
            agentController.start();
        } catch (ControllerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
