package models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the korpaitem database table.
 * 
 */
@Entity
@NamedQuery(name="Korpaitem.findAll", query="SELECT k FROM Korpaitem k")
public class Korpaitem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private int kolicina;

	@Column(name="KORPA_ID")
	private int korpaId;

	@Column(name="PROIZVOD_ID")
	private int proizvodId;

	public Korpaitem() {
	}
	
	

	public Korpaitem(int id, int kolicina, int korpaId, int proizvodId) {
		super();
		this.id = id;
		this.kolicina = kolicina;
		this.korpaId = korpaId;
		this.proizvodId = proizvodId;
	}



	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getKolicina() {
		return this.kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}

	public int getKorpaId() {
		return this.korpaId;
	}

	public void setKorpaId(int korpaId) {
		this.korpaId = korpaId;
	}

	public int getProizvodId() {
		return this.proizvodId;
	}

	public void setProizvodId(int proizvodId) {
		this.proizvodId = proizvodId;
	}

}