package choixVoyage.model;

import java.io.Serializable;
import java.util.List;

import jade.content.Concept;

public class Client implements Serializable, Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5668990067486678825L;
	private int id;
	private String nom;
	private String prenom;
	private String email;
	private String telephone;

	private List<Reservation> listReservationCl; // = new LinkedList<Reservation>();

	// constructors

	// getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<Reservation> getListReservationCl() {
		return listReservationCl;
	}

	public void setListReservationCl(List<Reservation> listReservationCl) {
		this.listReservationCl = listReservationCl;
	}

	public void save() {
		// TODO Auto-generated method stub

	}

}
