package models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the korpa database table.
 * 
 */
@Entity
@NamedQuery(name="Korpa.findAll", query="SELECT k FROM Korpa k")
public class Korpa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="KORISNIK_ID")
	private int korisnikId;

	public Korpa() {
	}

	
	public Korpa(int id, int korisnikId) {
		super();
		this.id = id;
		this.korisnikId = korisnikId;
	}


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getKorisnikId() {
		return this.korisnikId;
	}

	public void setKorisnikId(int korisnikId) {
		this.korisnikId = korisnikId;
	}

}