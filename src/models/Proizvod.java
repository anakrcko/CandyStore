package models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the proizvod database table.
 * 
 */
@Entity
@NamedQuery(name="Proizvod.findAll", query="SELECT p FROM Proizvod p")
public class Proizvod implements Serializable {
	private static final long serialVersionUID = 1L;

	private float cena;

	@Lob
	private byte[] fotografija;

	@Id
	private int id;


	@Column(name="KATEGORIJA_ID")
	private int kategorijaId;

	
	private int kolicina;

	private String naziv;

	private String opis;

	public Proizvod() {
	}
	
	public Proizvod(int id, float cena, byte[] fotografija, int kategorijaId, int kolicina, String naziv, String opis) {
		super();
		this.id = id;
		this.cena = cena;
		this.fotografija = fotografija;
		this.kategorijaId = kategorijaId;
		this.kolicina = kolicina;
		this.naziv = naziv;
		this.opis = opis;
	}

	public Proizvod(float cena, byte[] fotografija, int kategorijaId, int kolicina, String naziv, String opis) {
		super();
		this.cena = cena;
		this.fotografija = fotografija;
		this.kategorijaId = kategorijaId;
		this.kolicina = kolicina;
		this.naziv = naziv;
		this.opis = opis;
	}



	public float getCena() {
		return this.cena;
	}

	public void setCena(float cena) {
		this.cena = cena;
	}

	
	public byte[] getFotografija() {
		return this.fotografija;
	}

	public void setFotografija(byte[] fotografija) {
		this.fotografija = fotografija;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getKategorijaId() {
		return this.kategorijaId;
	}

	public void setKategorijaId(int kategorijaId) {
		this.kategorijaId = kategorijaId;
	}

	public int getKolicina() {
		return this.kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}

	public String getNaziv() {
		return this.naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return this.opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

}