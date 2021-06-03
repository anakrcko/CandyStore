package dal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import beans.KorpaItemBean;
import beans.ProizvodBean;
import models.Korpa;
import models.Korpaitem;

public class KorpaItemDAL {

	public EntityManagerFactory factory = Persistence.createEntityManagerFactory("Slatkisi");
	public EntityManager entityManager = factory.createEntityManager();
	
	
	
	public ArrayList<KorpaItemBean> vratiKorpaItems(Korpa korpa) 
	{
		List<Korpaitem> results = new LinkedList<Korpaitem>();
		ArrayList<KorpaItemBean> listaItema = new ArrayList<KorpaItemBean>();

		try {
			Query query = entityManager.createQuery("SELECT k FROM Korpaitem k WHERE k.korpaId=:korpaID");
			query.setParameter("korpaID", korpa.getId());
			
			results = query.getResultList();
	
			if (results.isEmpty()) return null;
			 
			for (Korpaitem item : results) 
			{
				KorpaItemBean bean = new KorpaItemBean();
				bean.setId(item.getId());
				bean.setKorpaId(item.getKorpaId());
				bean.setKolicina(item.getKolicina());
				bean.setProizvodId(item.getProizvodId());
				
				listaItema.add(bean);
			}
			
			return listaItema;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja korpa items.");
			System.err.println(e);
		}
		return null;
		
	}
	
	public KorpaItemBean proizvodUkorpi(Korpa korpa, ProizvodBean proizvod)
	{
		List<Korpaitem> results = new LinkedList<Korpaitem>();
		KorpaItemBean item = new KorpaItemBean();
		
		try {
			Query query = entityManager.createQuery("SELECT k FROM Korpaitem k WHERE k.korpaId=:korpaID AND k.proizvodId=:proizvodID");
			query.setParameter("korpaID", korpa.getId());
			query.setParameter("proizvodID", proizvod.getId());
			
			
			results = query.getResultList();
	
			if (results.isEmpty()) return null;
			
			for (Korpaitem korpaitem : results) {
				
				item.setId(korpaitem.getId());
				item.setKolicina(korpaitem.getKolicina());
				item.setKorpaId(korpaitem.getKorpaId());
				item.setProizvodId(korpaitem.getProizvodId());
			}
			return item;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja korpa itemA.");
			System.err.println(e);
			return null;
		}
		
	}
	
	public void promeniKolicinu(KorpaItemBean item, int kolicina) 
	{
		try {
			entityManager.getTransaction().begin();
			
			Query query;
			
			query = entityManager.createQuery("UPDATE Korpaitem k SET k.kolicina =:kolicina  WHERE k.id = :id");
			
			query.setParameter("kolicina", kolicina);
			query.setParameter("id", item.getId());
	
			query.executeUpdate();
			entityManager.getTransaction().commit();

		}catch (Exception e) {
			System.out.println("Greska u upitu kod menjanja kolicine proizvodima.");
			System.err.println(e);
		}

	}
}