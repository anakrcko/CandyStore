package beans;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import dal.*;
import models.*;

@ManagedBean
@SessionScoped
public class ProizvodBean implements Serializable 
{

	private static final long serialVersionUID = 3946172177700564122L;
	private int id;
	private String naziv;
	private String opis;
	private int kategorija_id;
	private int kolicina;
	private float cena;
	private String fotografija;
	
	private Part uploadedFile;
	private int kolicinaUKorpi;
	private Kategorija filterKategorija = null;
	private String pretraga = "";
	private float filterCena = -1;
	
	private ArrayList<ProizvodBean> proizvodi;
	private ProizvodBean proizvod;
	Map<String, Object> sessionMap = new KorisnikBean().getSessionMap();
	
	public ProizvodBean() {
		
	}
	
	public ProizvodBean(int id, String naziv, String opis, int kategorija_id, int kolicina,
			float cena, String fotografija, String pretraga) {
		super();
		this.id = id;
		this.naziv = naziv;
		this.opis = opis;
		this.kategorija_id = kategorija_id;
		this.kolicina = kolicina;
		this.cena = cena;
		this.fotografija = fotografija;
		this.pretraga = pretraga;
	}
	
	public ProizvodBean(String naziv, String opis, int kategorija_id, int kolicina,
			float cena, String fotografija, String pretraga) {
		super();
		this.naziv = naziv;
		this.opis = opis;
		this.kategorija_id = kategorija_id;
		this.kolicina = kolicina;
		this.cena = cena;
		this.fotografija = fotografija;
		this.pretraga = pretraga;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public int getKategorija() {
		return kategorija_id;
	}

	public void setKategorija(int kategorija_id) {
		this.kategorija_id = kategorija_id;
	}

	public int getKolicina() {
		return kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}

	public float getCena() {
		return cena;
	}

	public void setCena(float cena) {
		this.cena = cena;
	}

	public String getFotografija() {
		return fotografija;
	}

	public void setFotografija(String fotografija) {
		this.fotografija = fotografija;
	}

	public ArrayList<ProizvodBean> getProizvodi() {
		return proizvodi;
	}

	public void setProizvodi(ArrayList<ProizvodBean> proizvodi) {
		this.proizvodi = proizvodi;
	}
	
	public Kategorija getFilterKategorija() {
		return filterKategorija;
	}

	public void setFilterKategorija(Kategorija filterKategorija) {
		this.filterKategorija = filterKategorija;
	}
	
	public Part getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(Part uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	public String getPretraga() {
		return pretraga;
	}

	public void setPretraga(String pretraga) {
		this.pretraga = pretraga;
	}
	

	public float getFilterCena() {
		return filterCena;
	}

	public void setFilterCena(float filterCena) {
		this.filterCena = filterCena;
	}

	public ArrayList<ProizvodBean> vratiSveProizvode()
	{
		proizvodi = new ProizvodDAL().dajSveProizvode();
		return proizvodi;
	}
	
	public ProizvodBean vratiProizvod(int id) 
	{
		 proizvod = new ProizvodDAL().vratiProizvod(id);
		 return proizvod;
	}
	
	
	public void detaljiOProizvodu(ProizvodBean p) 
	{
		if(sessionMap.get("PROIZVOD") != null)
			sessionMap.remove("PROIZVOD");
		
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("PROIZVOD", p);
			
		try {
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("/Slatkisi/product-details.jsf");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	public int getKolicinaUKorpi() {
		return kolicinaUKorpi;
	}

	public void setKolicinaUKorpi(int kolicinaUKorpi) {
		this.kolicinaUKorpi = kolicinaUKorpi;
	}
	
	public void resetFilter() 
	{
		filtrirajPoKategoriji(null); 
		//pretraga = "";
		//filterCena = -1;
	}
	public void resetFilter2() 
	{
		
	}

	public void filtrirajPoKategoriji(KategorijeBean kat) 
	{
		Kategorija kategorija = null;
		if(kat != null)
			kategorija = new Kategorija(kat.getId(), kat.getNaziv());
		
		filterKategorija = kategorija;
		dajProizvode();
	}
	/*
	public ArrayList<ProizvodBean> vratiSveProizvodeFilter() {
		
		if(filterKategorija != null) {
			proizvodi = new ProizvodDAL().vratiProizvodePoKategoriji(filterKategorija);
			return proizvodi;
		}
		
		proizvodi = vratiSveProizvode();
		return proizvodi;
	}
	*/
	public ArrayList<ProizvodBean> dajProizvode()
	{

		//3
		if(filterKategorija != null && pretraga != "" && filterCena != -1)
		{
			proizvodi = new ProizvodDAL().vratiProizvodePoSvimFilterima(filterKategorija, pretraga, filterCena);
			return proizvodi;
		}
		
		
		//2
		if(filterKategorija != null && pretraga != "")
		{
			proizvodi = new ProizvodDAL().vratiProizvodePoKategorijiIPoPretrazi(filterKategorija, pretraga);
			return proizvodi;
		}
		
		if(filterKategorija != null && filterCena != -1)
		{
			proizvodi = new ProizvodDAL().vratiProizvodePoKategorijiIPoCeni(filterKategorija, filterCena);
			return proizvodi;
		}
		
		if(filterCena != -1 && pretraga != "" )
		{
			proizvodi = new ProizvodDAL().vratiProizvodePoCeniIPoPretrazi(filterCena, pretraga);
			return proizvodi;
		}
		
		
		//1
		if(filterKategorija != null) 
		{
			proizvodi = new ProizvodDAL().vratiProizvodePoKategoriji(filterKategorija);
			return proizvodi;
		}
		
		if(pretraga != "")
		{
			proizvodi = new ProizvodDAL().vratiProizvodePoPretrazi(pretraga);
			return proizvodi;
		}
		
		if(filterCena != -1)
		{
			proizvodi = new ProizvodDAL().vratiProizvodePoCeni(filterCena);
			return proizvodi;
		}
		
		
		
		proizvodi = vratiSveProizvode();
		return proizvodi;
	}
	
	public void obrisiProizvod(ProizvodBean p) 
	{
		Proizvod pr = new Proizvod();
		pr.setId(p.getId());
		try {
			new ProizvodDAL().obrisiProizvod(pr);
		} catch (Exception e) {
			System.out.println("Neuspesno brisanje proizvoda!");
			e.printStackTrace();
		}
	}
	
	public void izmeniProizvod(ProizvodBean p) 
	{
		try {
			if(sessionMap.get("DODAJPROIZVOD") != null)
				sessionMap.replace("DODAJPROIZVOD",null);
				
			if(sessionMap.get("IZMENIPROIZVOD") != null)	
				sessionMap.replace("IZMENIPROIZVOD",null);
				
			
			FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			sessionMap.put("IZMENIPROIZVOD", p);
				
			clear();
			this.setId(p.getId());
			this.setCena(p.getCena());
			this.setKolicina(p.getKolicina());
			this.setNaziv(p.getNaziv());
			this.setOpis(p.getOpis());
			this.setKategorija(p.getKategorija());
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("/Slatkisi/product-option.jsf");
				
		}
		catch (IOException e) {
			System.out.println("Greska pri menjanju proizvoda!");
		    e.printStackTrace();
		}	
				
	}
	
	public void dodajProizvod() 
	{
		try {
				if(sessionMap.get("IZMENIPROIZVOD") != null)
					sessionMap.replace("IZMENIPROIZVOD", null);
				
				if(sessionMap.get("DODAJPROIZVOD") != null)
					sessionMap.replace("DODAJPROIZVOD",null);
				
			
				FacesContext.getCurrentInstance().getExternalContext().getSession(true);
				sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
				sessionMap.put("DODAJPROIZVOD","1");
				
			clear();
			FacesContext.getCurrentInstance().getExternalContext().redirect("/Slatkisi/product-option.jsf");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void izmeni() {
		try 
		{
			byte[] buffer = null;
			
			if(uploadedFile!=null) {
			
				InputStream input = uploadedFile.getInputStream();
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				System.out.println(uploadedFile.getSubmittedFileName());
				buffer = new byte[(int) uploadedFile.getSize()];
				for (int length = 0; (length = input.read(buffer)) > 0;) output.write(buffer, 0, length);	
			}
			
			new ProizvodDAL().izmeniProizvod(this, buffer);
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("/Slatkisi/adminProducts.jsf");
			
		} catch (IOException e) {
			System.out.println("Greska");
			e.printStackTrace();
		}
	}
	
	public void dodaj() {
		if(uploadedFile!=null) {
			try {
				
				InputStream input = uploadedFile.getInputStream();
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				System.out.println(uploadedFile.getSubmittedFileName());
				byte[] buffer = new byte[(int) uploadedFile.getSize()];
				for (int length = 0; (length = input.read(buffer)) > 0;) output.write(buffer, 0, length);
				
				System.out.println("naziv:" + this.naziv);
				
				new ProizvodDAL().dodajProizvod(this,buffer);
				
				FacesContext.getCurrentInstance().getExternalContext().redirect("/Slatkisi/adminProducts.jsf");
				
			} catch (IOException e) {
				System.out.println("Greska");
				e.printStackTrace();
			}
		}
	}

	public void clear(){
		setId(-1);
	    setCena(0);
	    setNaziv("");
	    setOpis("");
	    setKategorija(0);
	    setKolicina(0);
	   
	}
	
}
