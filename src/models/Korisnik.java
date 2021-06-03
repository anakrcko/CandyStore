package models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the korisnik database table.
 * 
 */
@Entity
@NamedQuery(name="Korisnik.findAll", query="SELECT k FROM Korisnik k")
public class Korisnik implements Serializable {
	private static final long serialVersionUID = 1L;

	private String adresa;

	private String email;

	@Id
	private int id;

	private String ime;

	private String password;

	private String prezime;

	private String telefon;

	private String uloga;

	public Korisnik() {
		
	}
	
	
	public Korisnik(String adresa, String email, int id, String ime, String password, String prezime, String telefon,
			String uloga) {
		super();
		this.adresa = adresa;
		this.email = email;
		this.id = id;
		this.ime = ime;
		this.password = password;
		this.prezime = prezime;
		this.telefon = telefon;
		this.uloga = uloga;
	}



	public String getAdresa() {
		return this.adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIme() {
		return this.ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrezime() {
		return this.prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getUloga() {
		return this.uloga;
	}

	public void setUloga(String uloga) {
		this.uloga = uloga;
	}

	public String getTelefon() {
		return telefon;
	}

	
}