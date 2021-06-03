package dal;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import beans.KorpaBean;
import beans.KorpaItemBean;
import beans.PorudzbinaBean;
import beans.PorudzbinaItemBean;
import beans.ProizvodBean;
import models.Korisnik;
import models.Korpa;
import models.Porudzbina;
import models.Porudzbinaitem;
import models.Proizvod;

public class PorudzbinaDAL 
{

	public EntityManagerFactory factory = Persistence.createEntityManagerFactory("Slatkisi");
	public EntityManager entityManager = factory.createEntityManager();
	

	public void poruciKorpu(Korisnik korisnik, Korpa korpa)
	{
		entityManager.getTransaction().begin();
		
		ArrayList<KorpaItemBean> items = new KorpaItemDAL().vratiKorpaItems(korpa);

		Date date = new Date(System.currentTimeMillis());
		
		float suma = 0;
		KorpaBean korpaBean = new KorpaBean();
		suma = korpaBean.dajSumuKorpe(korpa);

		
		//dodavanje porudzbine
		Porudzbina porudzbina = new Porudzbina();
		porudzbina.setDatumKreiranja(date);
		porudzbina.setKorisnikId(korisnik.getId());
		porudzbina.setSuma(suma);
		
		try{	
			entityManager.persist(porudzbina);

			entityManager.getTransaction().commit();
		}
		catch (Exception e) {
			System.out.println("Greska u upitu kod dodavanja porudzbine.");
			System.err.println(e);
		}
		
		//dodavanje porudzbinaItems
		Porudzbina porudzbinaZaID = new PorudzbinaDAL().pronadjiPorudzbinu(date, korisnik.getId() );
		new PorudzbinaItemDAL().dodavanjePorudzbinaItems(items, porudzbinaZaID.getId());
			
		/*
		//smanjiti kolicinu proizvoda
		for (KorpaItemBean kp : items) 
		{
			int kolicinaProizvoda = kp.getKolicina();
				
			new ProizvodDAL().promeniKolicinu(kp.getProizvodId(), kolicinaProizvoda);
			
		}
		*/
		
		//cuvamo porudzbinu, admin potvrdjuje i smanjuje kolicinu
		
		//obrisati korpu
		korpaBean.obrisiKorpu(korpa);
		
	}


	private Porudzbina pronadjiPorudzbinu(Date date, int idKorisnika) {
		
		List<Porudzbina> results = new LinkedList<Porudzbina>();
		
		try {
			Query query = entityManager.createQuery("SELECT p FROM Porudzbina p WHERE p.datumKreiranja=:datumKreiranja AND p.korisnikId=:korisnikId");
			query.setParameter("datumKreiranja", date);
			query.setParameter("korisnikId", idKorisnika);
	
			results = query.getResultList();
		
	        if (results.isEmpty()) 
	        	return null;
	        
	        else if (results.size() >0 ) 
	        	return (Porudzbina)results.get(0);
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod pronalazenja porudzbine.");
			System.err.println(e);
		}
        return null;
	}


	public ArrayList<PorudzbinaBean> vratiSvePoruceno() {
		
		List<Porudzbina> results = new LinkedList<Porudzbina>();
		ArrayList<PorudzbinaBean> listaPorudzbina = new ArrayList<PorudzbinaBean>();
		
		try {
			Query query = entityManager.createQuery("SELECT p FROM Porudzbina p");
			
			results = query.getResultList();
			
			if (results.isEmpty() == true) 
				return null;
			 
			for (Porudzbina p : results) 
			{
				PorudzbinaBean porudzbinaBean = new PorudzbinaBean();
				
				porudzbinaBean.setId(p.getId());
				porudzbinaBean.setKorisnikId(p.getKorisnikId());
				porudzbinaBean.setDatumKreiranja(p.getDatumKreiranja());
				porudzbinaBean.setSuma(p.getSuma());
				
				listaPorudzbina.add(porudzbinaBean);
			}
			
			return listaPorudzbina;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja svih porudzbina.");
			System.err.println(e);
			return null;
		}    

	}


	public void obrisiPorudzbinu(PorudzbinaBean porudzbina) {
		
		int porudzbinaID = porudzbina.getId();
		
		try {
			entityManager.getTransaction().begin();
			
	
			Query query = entityManager.createQuery("DELETE FROM Porudzbinaitem p WHERE p.porudzbinaId = :porudzbinaId");
		
			query.setParameter("porudzbinaId", porudzbinaID);
			
			query.executeUpdate();
		
			
			Query query2 = entityManager.createQuery("DELETE FROM Porudzbina p WHERE p.id = :porudzbinaId");
		
			query2.setParameter("porudzbinaId", porudzbinaID);
			
			query2.executeUpdate();
			
			entityManager.getTransaction().commit();
		}catch (Exception e) {
			System.out.println("Greska u upitu kod brisanja porudzbine.");
			System.err.println(e);
		}
		
	}


	public void potvrdiPorudzbinu(PorudzbinaBean porudzbina) {
		
		ArrayList<PorudzbinaItemBean> items = new PorudzbinaItemDAL().vratiItems(porudzbina.getId());
		
		//smanjiti kolicinu proizvoda
		for (PorudzbinaItemBean kp : items) 
		{
			int kolicinaProizvoda = kp.getKolicina();
				
			new ProizvodDAL().promeniKolicinu(kp.getProizvodId(), kolicinaProizvoda);
			
		}
		
		obrisiPorudzbinu(porudzbina);
	}
}
