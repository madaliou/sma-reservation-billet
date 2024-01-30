package choixVoyage.model;

import java.io.Serializable;
import java.util.List;

import jade.content.Concept;

public class Compagnie implements Serializable, Concept {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private int id;

	private String nom;

	private String telephone;

	private Aeroport aeroport;

	public Compagnie() {
		// TODO Auto-generated constructor stub
	}

	public Aeroport getAeroport() {
		return aeroport;
	}

	public void setAeroport(Aeroport aeroport) {
		this.aeroport = aeroport;
	}

	public List<Vol> getVol() {
		return vol;
	}

	public void setVol(List<Vol> vol) {
		this.vol = vol;
	}

	private List<Vol> vol;

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

	public String getPhone() {
		return telephone;
	}

	public void setPhone(String telephone) {
		this.telephone = telephone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void save() {
		// TODO Auto-generated method stub

	}

}
