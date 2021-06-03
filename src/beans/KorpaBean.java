package beans;

import java.util.ArrayList;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import common.ValidatorMessage;
import dal.KorpaDAL;
import dal.KorpaItemDAL;
import dal.ProizvodDAL;
import models.Korisnik;
import models.Korpa;

@ManagedBean
@SessionScoped
public class KorpaBean 
{
	private int id;
	private int korisnikId;
	
	Map<String, Object> sessionMap = new KorisnikBean().getSessionMap();
			
	public KorpaBean() {
		super();
	}
	

	public KorpaBean(int id, int korisnikId) {
		super();
		this.id = id;
		this.korisnikId = korisnikId;
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

	
	
	//kreiranje korpe (dodavanje proizvoda)
	//isprazni korpu
	//daj korpaItems
	//daj sumu cele korpe
	
	public void kreirajKorpu(Korisnik korisnik) 
	{
		sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
		if(korisnik == null)
		{
			System.out.println("Korisnik nije ulogovan!");
		}
		else
		{
			Korpa korpa = new KorpaDAL().PronadjiKorpu(korisnik);	//daj mi njegovu korpu
			
			if(korpa == null) 
			{
				KorpaBean novaKorpa = new KorpaDAL().kreirajKorpu(korisnik);
				sessionMap.put("KORPA", novaKorpa);
			}
			else 
			{
				sessionMap.put("KORPA", korpa);
			}	
		}
	}
	
	public void dodajProizvodUKorpu(Korisnik korisnik, ProizvodBean p)
	{
		boolean succ = new KorpaDAL().dodajProizvodUKorpu(korisnik, p);
		FacesContext fc = FacesContext.getCurrentInstance();
		
		if(succ == true)
		{
			fc.addMessage("form-add:message-proizvodi-nije-dodato", new FacesMessage(ValidatorMessage.SUCCESS, "Proizvod je dodat u korpu."));
		}
		else
		{
			fc.addMessage("form-add:message-proizvodi-dodato", new FacesMessage(ValidatorMessage.UNSUCCESS, "Proizvod nije dodat u korpu."));
		}
	}
	
	public float dajSumuKorpe(Korpa korpa)
	{
		float suma = 0;
		
		ArrayList<KorpaItemBean> listaItema = new ArrayList<KorpaItemBean>();
		KorpaItemDAL items = new KorpaItemDAL();
		
		listaItema = items.vratiKorpaItems(korpa);
		if(listaItema == null)
			return suma;
		
		for (KorpaItemBean korpaItemBean : listaItema) 
		{
			int proizvodID = korpaItemBean.getProizvodId();
			int kolicina = korpaItemBean.getKolicina();
			ProizvodDAL proizvodDal = new ProizvodDAL();
			
			ProizvodBean proizvod = proizvodDal.vratiProizvod(proizvodID);
			suma += proizvod.getCena() * kolicina;
		}
		
		return suma;
		
	}
	
	public void obrisiKorpu(Korpa korpa) 
	{
		new KorpaDAL().obrisiKorpu(korpa);
	}
	
	// brisanje proizvoda iz korpe
	public void izbaciProizvodIzKorpe(Korpa k, ProizvodBean p)
	{
		new KorpaDAL().izbaciProizvodIzKorpe(k, p);
	}
}
