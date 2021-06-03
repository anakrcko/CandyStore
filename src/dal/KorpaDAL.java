package dal;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import beans.*;
import models.*;


public class KorpaDAL 
{

	public EntityManagerFactory factory = Persistence.createEntityManagerFactory("Slatkisi");
	public EntityManager entityManager = factory.createEntityManager();
	
	//kreiranje korpe (dodavanje proizvoda)
		//isprazni korpu
		//daj korpaItems
		//daj sumu cele korpe
		
	
	
	public Korpa PronadjiKorpu(Korisnik k) 
	{
		List<Korpa> results = new LinkedList<Korpa>();
		
		try {
			Query query = entityManager.createQuery("SELECT k FROM Korpa k WHERE k.korisnikId=:korisnik");
			query.setParameter("korisnik", k.getId());
	
			results = query.getResultList();
		
	        if (results.isEmpty()) 
	        	return null;
	        
	        else if (results.size() >0 ) 
	        	return (Korpa)results.get(0);
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod pronalazenja korpe.");
			System.err.println(e);
		}
        return null;
	}


	public KorpaBean kreirajKorpu(Korisnik k) 
	{
		try {
			entityManager.getTransaction().begin();
			
			Korpa korpa = new Korpa();
			
			korpa.setKorisnikId(k.getId());
			
			entityManager.persist(korpa);
			entityManager.getTransaction().commit();
		}catch (Exception e) {
			System.out.println("Greska u upitu kod kreiranja korpe.");
			System.err.println(e);
		}
		return null;
	}
	
	
	public boolean dodajProizvodUKorpu(Korisnik k, ProizvodBean p)
	{
		Korpa korpa = new KorpaDAL().PronadjiKorpu(k);
		KorpaItemBean bean = new KorpaItemDAL().proizvodUkorpi(korpa, p);
		try {
			
			entityManager.getTransaction().begin();
			
			if( bean == null ) 
			{//pravimo novo
				Korpaitem korpaItem = new Korpaitem();
				
				korpaItem.setKolicina(1);
				korpaItem.setProizvodId(p.getId());
				korpaItem.setKorpaId(korpa.getId());
				entityManager.persist(korpaItem);
			}
			else
			{//vec postoji
				int kolicina = bean.getKolicina() + 1;
				new KorpaItemDAL().promeniKolicinu(bean, kolicina);
			}
			
			entityManager.getTransaction().commit();
			return true;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod dodavanja proizvoda u korpu.");
			System.err.println(e);
			return false;
		}
	}

	
/*
	public float dajSumuKorpe(Korpa korpa)
	{
		float suma = 0;
		
		ArrayList<KorpaItemBean> listaItema = new ArrayList<KorpaItemBean>();
		KorpaItemDAL items = new KorpaItemDAL();
		
		listaItema = items.vratiKorpaItems(korpa);
		
		for (KorpaItemBean korpaItemBean : listaItema) 
		{
			int proizvodID = korpaItemBean.getProizvodId();
			ProizvodDAL proizvodDal = new ProizvodDAL();
			
			ProizvodBean proizvod = proizvodDal.vratiProizvod(proizvodID);
			suma += proizvod.getCena() * proizvod.getKolicina();
		}
		
		return suma;
		
	}

*/
	public void obrisiKorpu(Korpa korpa) 
	{
		int korpaID = korpa.getId();
		
		try {
			entityManager.getTransaction().begin();
			
	
			Query query = entityManager.createQuery("DELETE FROM Korpaitem k WHERE k.korpaId = :korpaID");
		
			query.setParameter("korpaID", korpaID);
			
			query.executeUpdate();
		
			/*
			Query query2 = entityManager.createQuery("DELETE FROM Korpa k WHERE k.id = :korpaID");
		
			query2.setParameter("korpaID", korpaID);
			
			query2.executeUpdate();*/
			
			entityManager.getTransaction().commit();
		}catch (Exception e) {
			System.out.println("Greska u upitu kod brisanja korpe.");
			System.err.println(e);
		}
	}

	public void izbaciProizvodIzKorpe(Korpa k, ProizvodBean p)
	{
		System.out.println("Brisem iz korpe :" +k.getId()+"proizvod "+p.getId());
		
		try {
		entityManager.getTransaction().begin();
		
		Proizvod pr = new Proizvod();
		pr.setId(p.getId());
		Query query = entityManager.createQuery("DELETE FROM Korpaitem k WHERE k.korpaId = :korpaID AND k.proizvodId = :proizvodID ");
	
		query.setParameter("korpaID", k.getId());
		query.setParameter("proizvodID", pr.getId());
		
		query.executeUpdate();
		
		entityManager.getTransaction().commit();
		}catch (Exception e) {
			System.out.println("Greska u upitu kod brisanja proizvoda iz korpe.");
			System.err.println(e);
		}
	}
	
}
