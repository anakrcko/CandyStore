package dal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import beans.*;
import models.*;

public class KategorijeDAL 
{
	public EntityManagerFactory factory = Persistence.createEntityManagerFactory("Slatkisi");
	public EntityManager entityManager = factory.createEntityManager();
	
	public ArrayList<KategorijeBean> dajSveKategorije() 
	{
		List<Kategorija> results = new LinkedList<Kategorija>();
		ArrayList<KategorijeBean> listaKategorija = new ArrayList<KategorijeBean>();
		
		try {
			
			Query query = entityManager.createQuery("SELECT k FROM Kategorija k");
			
			results = query.getResultList();
			
			if (results.isEmpty()) 
				return null;
			 
			for (Kategorija k : results) 
			{
				KategorijeBean kat = new KategorijeBean();
				kat.setId(k.getId());
				kat.setNaziv(k.getNaziv());
				
				listaKategorija.add(kat);
			}
		
			return listaKategorija;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja kategorija.");
			System.err.println(e);
			return null;
		}
        
	}

	private Kategorija kategorijaPoNazivu(int i) 
	{
		try {
			
			Query query = entityManager.createQuery("SELECT k FROM Kategorija k WHERE k.id = :id");
			query.setParameter("id", i);
			
			return (Kategorija)query.getSingleResult();
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja imena kategorije.");
			System.err.println(e);
		}
		return null;
	}
	
	public void dodajKategoriju(String dodajNaziv) 
	{
		try {
			entityManager.getTransaction().begin();
			
			Kategorija k = new Kategorija();
			k.setNaziv(dodajNaziv);
		
			entityManager.persist(k);
			
			entityManager.getTransaction().commit();
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod dodavanja kategorije.");
			System.err.println(e);
		}
	}

	public void obrisiKategoriju(KategorijeBean k) 
	{
		try {
			entityManager.getTransaction().begin();
	
			Query query = entityManager.createQuery("DELETE FROM Kategorija k WHERE k.id = :kategorija ");
			query.setParameter("kategorija", k.getId());
			
			query.executeUpdate();
			
			entityManager.getTransaction().commit();
		
		}catch (Exception e) {
			System.out.println("Greska u upitu kod brisanja kategorije.");
			System.err.println(e);
		}
	}
	
}
