package choixVoyage.model;

import java.io.Serializable;

import jade.content.Concept;

public class Aeroport implements Serializable, Concept {

	/**
	* 
	*/
	private static final long serialVersionUID = -8906749090998687620L;

	private int id;

	private String nom;

	public Aeroport() {
		this.nom = nom;
	}

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

	public void save() {
		// TODO Auto-generated method stub

	}

}
