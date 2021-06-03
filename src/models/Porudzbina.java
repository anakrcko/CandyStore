package models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the porudzbina database table.
 * 
 */
@Entity
@NamedQuery(name="Porudzbina.findAll", query="SELECT p FROM Porudzbina p")
public class Porudzbina implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATUM_KREIRANJA")
	private Date datumKreiranja;

	@Id
	private int id;

	@Column(name="KORISNIK_ID")
	private int korisnikId;

	private float suma;

	public Porudzbina() {
	}

	
	public Porudzbina(Date datumKreiranja, int korisnikId, float suma) {
		super();
		this.datumKreiranja = datumKreiranja;
		this.korisnikId = korisnikId;
		this.suma = suma;
	}


	public Date getDatumKreiranja() {
		return this.datumKreiranja;
	}

	public void setDatumKreiranja(Date datumKreiranja) {
		this.datumKreiranja = datumKreiranja;
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

	public float getSuma() {
		return this.suma;
	}

	public void setSuma(float suma) {
		this.suma = suma;
	}

}