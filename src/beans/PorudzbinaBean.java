package beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import dal.PorudzbinaDAL;
import models.Korisnik;
import models.Korpa;

@ManagedBean
@SessionScoped
public class PorudzbinaBean {
	
	private int id;
	private int korisnikId;
	private Date datumKreiranja;
	private float suma;
	
	private ArrayList<PorudzbinaBean> porudzbine;
	Map<String, Object> sessionMap = new KorisnikBean().getSessionMap();
	
	public PorudzbinaBean() {
		
	}
	public PorudzbinaBean(int korisnikId, Date datumKreiranja, float suma) {
		super();
		this.korisnikId = korisnikId;
		this.datumKreiranja = datumKreiranja;
		this.suma = suma;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getKorisnikId() {
		return korisnikId;
	}
	public void setKorisnikId(int korisnikId) {
		this.korisnikId = korisnikId;
	}
	public Date getDatumKreiranja() {
		return datumKreiranja;
	}
	public void setDatumKreiranja(Date datumKreiranja) {
		this.datumKreiranja = datumKreiranja;
	}
	public float getSuma() {
		return suma;
	}
	public void setSuma(float suma) {
		this.suma = suma;
	}
	
	

	public void poruciKorpu(Korisnik korisnik, Korpa korpa)
	{
		new PorudzbinaDAL().poruciKorpu(korisnik, korpa);
	}

	public ArrayList<PorudzbinaBean> vratiSvePoruceno()
	{
		porudzbine = new PorudzbinaDAL().vratiSvePoruceno();
		return porudzbine;
	}
	
	public void dajProizvode(PorudzbinaBean p) 
	{
		if(sessionMap.get("POGLEDAJPORUDZBINU") != null)
			sessionMap.remove("POGLEDAJPORUDZBINU");
		
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("POGLEDAJPORUDZBINU", p);
			
		try {
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("/Slatkisi/adminOrders.jsf");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void obrisiPorudzbinu(PorudzbinaBean porudzbina)
	{
		new PorudzbinaDAL().obrisiPorudzbinu(porudzbina);
	}
	
	public void potvrdiPorudzbinu(PorudzbinaBean porudzbina)
	{
		new PorudzbinaDAL().potvrdiPorudzbinu(porudzbina);
	}
}
