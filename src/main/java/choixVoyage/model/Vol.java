package choixVoyage.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jade.content.Concept;
import choixVoyage.agent.CompanyAgent;

public class Vol implements Serializable, Concept {

	/**
	* 
	*/
	private static final long serialVersionUID = 2530260719412005959L;
	private int id;
	private int nbPlaceEco;
	private int nbPlaceBuss;
	private String Code;
	private int Prixeco;
	private int prixBuss;
	private Date depart;
	private Date datArrivee;
	private int nbEscale;
	private String descrEscale;
	private String matAvion; // matricule de l'avion
	private Aeroport aeroportSrc;
	private Aeroport aeroportDest;
	private String compagnie;
	private List<Reservation> listReservationC;

	public String getCompagnie() {
		return compagnie;
	}

	public void setCompagnie(String compagnie) {
		this.compagnie = compagnie;
	}

	public Aeroport getAeroportSrc() {
		return aeroportSrc;
	}

	public void setAeroportSrc(Aeroport aeroportSrc) {
		this.aeroportSrc = aeroportSrc;
	}

	public Aeroport getAeroportDest() {
		return aeroportDest;
	}

	public void setAeroportDest(Aeroport aeroportDest) {
		this.aeroportDest = aeroportDest;
	}

	public Vol(String string, String string2, int somme2, Compagnie h2) {
		// TODO Auto-generated constructor stub
	}

	public Vol() {
		// TODO Auto-generated constructor stub
	}

	public String getMatAvion() {
		return matAvion;
	}

	public void setMatAvion(String matAvion) {
		this.matAvion = matAvion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNbPlaceEco() {
		return nbPlaceEco;
	}

	public void setNbPlaceEco(int nbPlaceEco) {
		this.nbPlaceEco = nbPlaceEco;
	}

	public int getNbPlaceBuss() {
		return nbPlaceBuss;
	}

	public void setNbPlaceBuss(int nbplaceb) {
		this.nbPlaceBuss = nbplaceb;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public int getPrixeco() {
		return Prixeco;
	}

	public void setPrixeco(int prixeco) {
		Prixeco = prixeco;
	}

	public int getPrixBuss() {
		return prixBuss;
	}

	public void setPrixBuss(int prixBuss) {
		this.prixBuss = prixBuss;
	}

	public Date getDepart() {
		return depart;
	}

	public void setDepart(Date dated) {
		this.depart = dated;
	}

	public Date getDatArrivee() {
		return datArrivee;
	}

	public void setDatArrivee(Date datArrivee) {
		this.datArrivee = datArrivee;
	}

	public int getNbEscale() {
		return nbEscale;
	}

	public void setNbEscale(int nbEscale) {
		this.nbEscale = nbEscale;
	}

	public String getDescrEscale() {
		return descrEscale;
	}

	public void setDescrEscale(String descrEscale) {
		this.descrEscale = descrEscale;
	}

	public List<Reservation> getListReservationC() {
		return listReservationC;
	}

	public void setListReservationC(List<Reservation> listReservationC) {
		this.listReservationC = listReservationC;
	}

	public List<Vol> findByPredicats(String typevol2, String aeroportDepart, String aeroportArrivee, String classevol) {
		// TODO Auto-generated method stub
		return null;
	}

	public void save() {
		// TODO Auto-generated method stub

	}

	// constructors

}
