package models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the porudzbinaitem database table.
 * 
 */
@Entity
@NamedQuery(name="Porudzbinaitem.findAll", query="SELECT p FROM Porudzbinaitem p")
public class Porudzbinaitem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private int kolicina;

	@Column(name="PORUDZBINA_ID")
	private int porudzbinaId;

	@Column(name="PROIZVOD_ID")
	private int proizvodId;

	public Porudzbinaitem() {
	}
	
	

	public Porudzbinaitem( int kolicina, int porudzbinaId, int proizvodId) {
		super();
		this.kolicina = kolicina;
		this.porudzbinaId = porudzbinaId;
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

	public int getPorudzbinaId() {
		return this.porudzbinaId;
	}

	public void setPorudzbinaId(int porudzbinaId) {
		this.porudzbinaId = porudzbinaId;
	}

	public int getProizvodId() {
		return this.proizvodId;
	}

	public void setProizvodId(int proizvodId) {
		this.proizvodId = proizvodId;
	}

}