package choixVoyage.model;

import java.io.Serializable;
import java.util.Date;
import jade.content.Concept;

public class Reservation implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7186007551401847213L;

    private int id;

    private Date datreserv;
    private Aeroport aeroportDepart;
    private Aeroport aeroportArrivee;
    private Vol Vol;
    private CriteresClient critere;
    private Client client;
    private Date depart;
    private Date datArrivee;
    private int nbplace;

    private String compagnie;

    public CriteresClient getCritere() {
        return critere;
    }

    public void setCritere(CriteresClient critere) {
        this.critere = critere;
    }

    public int getNbplace() {
        return nbplace;
    }

    public void setNbplace(int nbplace) {
        this.nbplace = nbplace;
    }

    public Date getDepart() {
        return depart;
    }

    public void setDepart(Date depart) {
        this.depart = depart;
    }

    public Date getDatArrivee() {
        return datArrivee;
    }

    public void setDatArrivee(Date datArrivee) {
        this.datArrivee = datArrivee;
    }

    public Date getDatreserv() {
        return datreserv;
    }

    public void setDatreserv(Date datreserv) {
        this.datreserv = datreserv;
    }

    // constructors
    public Aeroport getAeroportDepart() {
        return aeroportDepart;
    }

    public void setAeroportDepart(Aeroport aeroportDepart) {
        this.aeroportDepart = aeroportDepart;
    }

    public Aeroport getAeroportArrivee() {
        return aeroportArrivee;
    }

    public void setAeroportArrivee(Aeroport aeroportArrivee) {
        this.aeroportArrivee = aeroportArrivee;
    }

    public Vol getVol() {
        return Vol;
    }

    public void setVol(Vol vol) {
        Vol = vol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getCompagnie() {
        // TODO Auto-generated method stub
        return compagnie;
    }

    public void setCompagnie(String string) {
        this.compagnie = string;
    }

}
