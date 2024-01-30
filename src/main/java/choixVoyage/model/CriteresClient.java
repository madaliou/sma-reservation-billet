package choixVoyage.model;

import java.io.Serializable;
import java.util.Date;

import jade.content.Predicate;

public class CriteresClient implements Predicate, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String AeroportDepart;
	private String AeroportArrivee;
	private String typevol;
	private String classevol;
	private Date datDepart;
	private Date datRetour;
	private int nbplace;

	public int getNbplace() {
		return nbplace;
	}

	public void setNbplace(int nbplace) {
		this.nbplace = nbplace;
	}

	public String getAeroportDepart() {
		return AeroportDepart;
	}

	public void setAeroportDepart(String aeroportDepart) {
		AeroportDepart = aeroportDepart;
	}

	public String getAeroportArrivee() {
		return AeroportArrivee;
	}

	public void setAeroportArrivee(String aeroportArrivee) {
		AeroportArrivee = aeroportArrivee;
	}

	public String getTypevol() {
		return typevol;
	}

	public void setTypevol(String typevol) {
		this.typevol = typevol;
	}

	public String getClassevol() {
		return classevol;
	}

	public void setClassevol(String classevol) {
		this.classevol = classevol;
	}

	public Date getDatRetour() {
		return datRetour;
	}

	public void setDatRetour(Date datRetour) {
		this.datRetour = datRetour;
	}

	public Date getDatDepart() {
		return datDepart;
	}

	public void setDatDepart(Date datDepart) {
		this.datDepart = datDepart;
	}

}