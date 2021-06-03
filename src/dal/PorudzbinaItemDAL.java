package dal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import beans.KorpaItemBean;
import beans.PorudzbinaBean;
import beans.PorudzbinaItemBean;
import models.Porudzbina;
import models.Porudzbinaitem;

public class PorudzbinaItemDAL {
	
	
	public EntityManagerFactory factory = Persistence.createEntityManagerFactory("Slatkisi");
	public EntityManager entityManager = factory.createEntityManager();
	
	
	public void dodavanjePorudzbinaItems(ArrayList<KorpaItemBean> items, int porudzbinaID)
	{
		
		entityManager.getTransaction().begin();
		System.out.println("Porudzbina id je: "+porudzbinaID);
		try {
			for (KorpaItemBean kp : items) 
			{
				Porudzbinaitem porItem = new Porudzbinaitem();
				porItem.setKolicina(kp.getKolicina());
				porItem.setPorudzbinaId(porudzbinaID);
				porItem.setProizvodId(kp.getProizvodId());
				
				entityManager.persist(porItem);
			}
			entityManager.getTransaction().commit();
		}catch (Exception e) {
			System.out.println("Greska u upitu kod dodavanja porudzbina items.");
			System.err.println(e);
		}
	}


	public ArrayList<PorudzbinaItemBean> vratiItems(int id) {

		List<Porudzbinaitem> results = new LinkedList<Porudzbinaitem>();
		ArrayList<PorudzbinaItemBean> listaPorudzbinaItems = new ArrayList<PorudzbinaItemBean>();
		
		try {
			Query query = entityManager.createQuery("SELECT p FROM Porudzbinaitem p WHERE p.porudzbinaId=:id");
			query.setParameter("id", id);
			results = query.getResultList();
			
			if (results.isEmpty() == true) 
				return null;
			 
			for (Porudzbinaitem p : results) 
			{
				PorudzbinaItemBean porudzbinaItemBean = new PorudzbinaItemBean();
				
				porudzbinaItemBean.setId(p.getId());
				porudzbinaItemBean.setKolicina(p.getKolicina());
				porudzbinaItemBean.setProizvodId(p.getProizvodId());
				porudzbinaItemBean.setPorudzbinaId(p.getPorudzbinaId());
				
				listaPorudzbinaItems.add(porudzbinaItemBean);
			}
			
			return listaPorudzbinaItems;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja svih porudzbina items-a.");
			System.err.println(e);
			return null;
		}    

	}

}
