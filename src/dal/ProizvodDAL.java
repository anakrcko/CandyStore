package dal;

import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import beans.*;
import models.*;

public class ProizvodDAL 
{

	public EntityManagerFactory factory = Persistence.createEntityManagerFactory("Slatkisi");
	public EntityManager entityManager = factory.createEntityManager();
	
	
	public ArrayList<ProizvodBean> dajSveProizvode() 
	{
		List<Proizvod> results = new LinkedList<Proizvod>();
		ArrayList<ProizvodBean> listaProizvoda = new ArrayList<ProizvodBean>();
		
		try {
			Query query = entityManager.createQuery("SELECT p FROM Proizvod p");
			
			results = query.getResultList();
			
			if (results.isEmpty() == true) 
				return null;
			 
			for (Proizvod p : results) 
			{
				ProizvodBean proizvodBean = new ProizvodBean();
				
				proizvodBean.setId(p.getId());
				proizvodBean.setNaziv(p.getNaziv());
				proizvodBean.setOpis(p.getOpis());
				proizvodBean.setKolicina(p.getKolicina());
				proizvodBean.setCena(p.getCena());
				proizvodBean.setFotografija(Base64.getEncoder().encodeToString(p.getFotografija()));
				proizvodBean.setKategorija(p.getKategorijaId());
				
				listaProizvoda.add(proizvodBean);
			}
			
			return listaProizvoda;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja svih proizvoda.");
			System.err.println(e);
			return null;
		}    

	}
	
	public ProizvodBean vratiProizvod(int id) 
	{
		List<Proizvod> results = new LinkedList<Proizvod>();
		ProizvodBean bean  = new ProizvodBean();
		
		System.out.println("Vracam proizvod " + id); 
		
		try {
			Query query = entityManager.createQuery("SELECT p FROM Proizvod p WHERE p.id =:id ");
			query.setParameter("id", id);
			
			results = query.getResultList();
			
			if (results.isEmpty()) 
				return null;
			 
			for (Proizvod p : results) 
			{
				bean.setId(p.getId());
				bean.setNaziv(p.getNaziv());
				bean.setOpis(p.getOpis());
				bean.setKolicina(p.getKolicina());
				bean.setCena(p.getCena());
				bean.setFotografija(Base64.getEncoder().encodeToString(p.getFotografija()));
				bean.setKategorija(p.getKategorijaId());
			}
			
			return bean;
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja odredjenog proizvoda.");
			System.err.println(e);
		}
		return null;
	}


	public ArrayList<ProizvodBean> vratiProizvodePoKategoriji(Kategorija filterKategorija) 
	{
		List<Proizvod> results = new LinkedList<Proizvod>();
		ArrayList<ProizvodBean> listaProizvodaPoKategoriji = new ArrayList<ProizvodBean>();
		
		try {
			Query query = entityManager.createQuery("SELECT p FROM Proizvod p WHERE p.kategorijaId = :kat");
			query.setParameter("kat", filterKategorija.getId());
			
			results = query.getResultList();
			
			if (results.isEmpty()) 
				return null;
			 
			for (Proizvod p : results) 
			{
				ProizvodBean proizvod = new ProizvodBean();
				proizvod.setId(p.getId());
				proizvod.setNaziv(p.getNaziv());
				proizvod.setOpis(p.getOpis());
				proizvod.setKolicina(p.getKolicina());
				proizvod.setCena(p.getCena());
				proizvod.setFotografija(Base64.getEncoder().encodeToString(p.getFotografija()));
				proizvod.setKategorija(p.getKategorijaId());
	
				listaProizvodaPoKategoriji.add(proizvod);
			}
			
			return listaProizvodaPoKategoriji;
		
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja proizvoda po kategoriji.");
			System.err.println(e);
		}
		
		return null;
	}


	public void obrisiProizvod(Proizvod pr) 
	{
		try {	
			entityManager.getTransaction().begin();
			
	
			Query query = entityManager.createQuery("DELETE FROM Proizvod p WHERE p.id = :proizvod");
		
			query.setParameter("proizvod", pr.getId());
			
			query.executeUpdate();
			
			entityManager.getTransaction().commit();
		}catch (Exception e) {
			System.out.println("Greska u upitu kod brisanja proizvoda.");
			System.err.println(e);
		}
	}


	public void dodajProizvod(ProizvodBean p, byte[] buffer) 
	{
		try {
			entityManager.getTransaction().begin();
		
			/*
			Proizvod proizvod = new Proizvod();
			
			proizvod.setCena(p.getCena());
			proizvod.setFotografija(buffer);
			proizvod.setKolicina(p.getKolicina());
			proizvod.setNaziv(p.getNaziv());
			proizvod.setOpis(p.getOpis());
			proizvod.setKategorijaId(p.getKategorija());
			*/
			
			Proizvod proizvod = new Proizvod(p.getId(),p.getCena(), buffer, p.getKategorija(), p.getKolicina(), p.getNaziv(), p.getOpis());
			entityManager.persist(proizvod);
			
			entityManager.getTransaction().commit();
			System.out.println("Uspesno dodat proizvod.");
		}catch (Exception e) {
			System.out.println("Greska u upitu kod dodavanja proizvoda.");
			System.err.println(e);
		}	
	}
	
	public void izmeniProizvod(ProizvodBean p, byte[] buffer) 
	{
		try {
			entityManager.getTransaction().begin();
			
			int id = p.getId();
			
			Query query;
			
			if(buffer != null) 	//ako ima slika
			{
				query = entityManager.createQuery("UPDATE Proizvod p SET p.naziv = :naziv, p.opis =:opis, "
						+ "p.kategorijaId =:kategorija, p.cena = :cena, p.kolicina =:kolicina,  p.fotografija = :fotografija  WHERE p.id = :id");
			
				query.setParameter("fotografija", buffer);
			}
			else 
			{
				query = entityManager.createQuery("UPDATE Proizvod p SET p.naziv = :naziv, p.opis =:opis, "
						+ "p.kategorijaId =:kategorija, p.cena = :cena, p.kolicina =:kolicina  WHERE p.id = :id");
			}
			
			query.setParameter("id", id);
			query.setParameter("naziv", p.getNaziv());
			query.setParameter("opis", p.getOpis());
			query.setParameter("kolicina", p.getKolicina());
			query.setParameter("cena", p.getCena());
			query.setParameter("kategorija", p.getKategorija());
			
			query.executeUpdate();
			entityManager.getTransaction().commit();
		
		}catch (Exception e) {
			System.out.println("Greska u upitu kod menjanja proizvoda.");
			System.err.println(e);
		}
	}
	
	public void promeniKolicinu(int proizvodID, int kolicina) 
	{
		try {
			entityManager.getTransaction().begin();
			
			Query query;
			//UPDATE Proizvod p SET p.kolicina = p.KOLICINA-3 WHERE p.id = 1
			query = entityManager.createQuery("UPDATE Proizvod p SET p.kolicina = p.kolicina- :kolicina  WHERE p.id = :id");
			
			query.setParameter("kolicina", kolicina);
			query.setParameter("id", proizvodID);
	
			query.executeUpdate();
			entityManager.getTransaction().commit();
		}catch (Exception e) {
			System.out.println("Greska u upitu kod menjanja kolicine proizvoda.");
			System.err.println(e);
		}
	}

	public ArrayList<ProizvodBean> vratiProizvodePoPretrazi(String pretraga) {
		
		List<Proizvod> results = new LinkedList<Proizvod>();
		ArrayList<ProizvodBean> listaProizvoda = new ArrayList<ProizvodBean>();
		
		try {
			Query query = entityManager.createQuery("SELECT p FROM Proizvod p WHERE p.naziv LIKE CONCAT('%',:pretraga,'%')");
			query.setParameter("pretraga", pretraga);
			
			results = query.getResultList();
			
			if (results.isEmpty() == true) 
				return null;
			 
			for (Proizvod p : results) 
			{
				ProizvodBean proizvodBean = new ProizvodBean();
				
				proizvodBean.setId(p.getId());
				proizvodBean.setNaziv(p.getNaziv());
				proizvodBean.setOpis(p.getOpis());
				proizvodBean.setKolicina(p.getKolicina());
				proizvodBean.setCena(p.getCena());
				proizvodBean.setFotografija(Base64.getEncoder().encodeToString(p.getFotografija()));
				proizvodBean.setKategorija(p.getKategorijaId());
				
				listaProizvoda.add(proizvodBean);
			}
			
			return listaProizvoda;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja proizvoda po pretrazi.");
			System.err.println(e);
			return null;
		}    

	}

	public ArrayList<ProizvodBean> vratiProizvodePoKategorijiIPoPretrazi(Kategorija filterKategorija, String pretraga) {
		List<Proizvod> results = new LinkedList<Proizvod>();
		ArrayList<ProizvodBean> listaProizvoda = new ArrayList<ProizvodBean>();
		
		try {
			Query query = entityManager.createQuery("SELECT p FROM Proizvod p WHERE p.naziv LIKE CONCAT('%',:pretraga,'%') AND p.kategorijaId = :kat");
			query.setParameter("pretraga", pretraga);
			query.setParameter("kat", filterKategorija.getId());
			
			results = query.getResultList();
			
			if (results.isEmpty() == true) 
				return null;
			 
			for (Proizvod p : results) 
			{
				ProizvodBean proizvodBean = new ProizvodBean();
				
				proizvodBean.setId(p.getId());
				proizvodBean.setNaziv(p.getNaziv());
				proizvodBean.setOpis(p.getOpis());
				proizvodBean.setKolicina(p.getKolicina());
				proizvodBean.setCena(p.getCena());
				proizvodBean.setFotografija(Base64.getEncoder().encodeToString(p.getFotografija()));
				proizvodBean.setKategorija(p.getKategorijaId());
				
				listaProizvoda.add(proizvodBean);
			}
			
			return listaProizvoda;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja proizvoda po kategoriji i po pretrazi.");
			System.err.println(e);
			return null;
		}    

	}

	public ArrayList<ProizvodBean> vratiProizvodePoCeni(float filterCena) {
		List<Proizvod> results = new LinkedList<Proizvod>();
		ArrayList<ProizvodBean> listaProizvoda = new ArrayList<ProizvodBean>();
		
		try {
			Query query = entityManager.createQuery("SELECT p FROM Proizvod p WHERE p.cena <=:filterCena ORDER BY p.cena ASC");
			query.setParameter("filterCena", filterCena);
			
			results = query.getResultList();
			
			if (results.isEmpty() == true) 
				return null;
			 
			for (Proizvod p : results) 
			{
				ProizvodBean proizvodBean = new ProizvodBean();
				
				proizvodBean.setId(p.getId());
				proizvodBean.setNaziv(p.getNaziv());
				proizvodBean.setOpis(p.getOpis());
				proizvodBean.setKolicina(p.getKolicina());
				proizvodBean.setCena(p.getCena());
				proizvodBean.setFotografija(Base64.getEncoder().encodeToString(p.getFotografija()));
				proizvodBean.setKategorija(p.getKategorijaId());
				
				listaProizvoda.add(proizvodBean);
			}
			
			return listaProizvoda;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja proizvoda manje od cene.");
			System.err.println(e);
			return null;
		}    

	}

	public ArrayList<ProizvodBean> vratiProizvodePoKategorijiIPoCeni(Kategorija filterKategorija, float filterCena) {
		List<Proizvod> results = new LinkedList<Proizvod>();
		ArrayList<ProizvodBean> listaProizvoda = new ArrayList<ProizvodBean>();
		
		try {
			//select * from proizvod where cena <=400 order by CENA asc
			Query query = entityManager.createQuery("SELECT p FROM Proizvod p WHERE p.cena <=:filterCena AND p.kategorijaId = :kat ORDER BY p.cena ASC");
			query.setParameter("filterCena", filterCena);
			query.setParameter("kat", filterKategorija.getId());
			
			results = query.getResultList();
			
			if (results.isEmpty() == true) 
				return null;
			 
			for (Proizvod p : results) 
			{
				ProizvodBean proizvodBean = new ProizvodBean();
				
				proizvodBean.setId(p.getId());
				proizvodBean.setNaziv(p.getNaziv());
				proizvodBean.setOpis(p.getOpis());
				proizvodBean.setKolicina(p.getKolicina());
				proizvodBean.setCena(p.getCena());
				proizvodBean.setFotografija(Base64.getEncoder().encodeToString(p.getFotografija()));
				proizvodBean.setKategorija(p.getKategorijaId());
				
				listaProizvoda.add(proizvodBean);
			}
			
			return listaProizvoda;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja proizvoda manje od cene i odredjene kategorije.");
			System.err.println(e);
			return null;
		}
	}

	public ArrayList<ProizvodBean> vratiProizvodePoSvimFilterima(Kategorija filterKategorija, String pretraga, float filterCena) {
		List<Proizvod> results = new LinkedList<Proizvod>();
		ArrayList<ProizvodBean> listaProizvoda = new ArrayList<ProizvodBean>();
		
		try {
			//SELECT * FROM Proizvod WHERE cena <=200 AND naziv LIKE CONCAT('%i%') AND KATEGORIJA_ID = 5 ORDER BY cena asc
			Query query = entityManager.createQuery("SELECT p FROM Proizvod p WHERE p.cena <= :filterCena AND p.naziv LIKE CONCAT('%',:pretraga,'%') AND p.kategorijaId = :kat ORDER BY p.cena ASC");
			query.setParameter("filterCena", filterCena);
			query.setParameter("pretraga", pretraga);
			query.setParameter("kat", filterKategorija.getId());
			
			results = query.getResultList();
			
			if (results.isEmpty() == true) 
				return null;
			 
			for (Proizvod p : results) 
			{
				ProizvodBean proizvodBean = new ProizvodBean();
				
				proizvodBean.setId(p.getId());
				proizvodBean.setNaziv(p.getNaziv());
				proizvodBean.setOpis(p.getOpis());
				proizvodBean.setKolicina(p.getKolicina());
				proizvodBean.setCena(p.getCena());
				proizvodBean.setFotografija(Base64.getEncoder().encodeToString(p.getFotografija()));
				proizvodBean.setKategorija(p.getKategorijaId());
				
				listaProizvoda.add(proizvodBean);
			}
			
			return listaProizvoda;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja proizvoda po svim filterima.");
			System.err.println(e);
			return null;
		}
	}

	public ArrayList<ProizvodBean> vratiProizvodePoCeniIPoPretrazi(float filterCena, String pretraga) {
		List<Proizvod> results = new LinkedList<Proizvod>();
		ArrayList<ProizvodBean> listaProizvoda = new ArrayList<ProizvodBean>();
		
		try {
			Query query = entityManager.createQuery("SELECT p FROM Proizvod p WHERE p.cena <=:filterCena AND p.naziv LIKE CONCAT('%',:pretraga,'%') ORDER BY p.cena ASC");
			query.setParameter("filterCena", filterCena);
			query.setParameter("pretraga", pretraga);
			
			results = query.getResultList();
			
			if (results.isEmpty() == true) 
				return null;
			 
			for (Proizvod p : results) 
			{
				ProizvodBean proizvodBean = new ProizvodBean();
				
				proizvodBean.setId(p.getId());
				proizvodBean.setNaziv(p.getNaziv());
				proizvodBean.setOpis(p.getOpis());
				proizvodBean.setKolicina(p.getKolicina());
				proizvodBean.setCena(p.getCena());
				proizvodBean.setFotografija(Base64.getEncoder().encodeToString(p.getFotografija()));
				proizvodBean.setKategorija(p.getKategorijaId());
				
				listaProizvoda.add(proizvodBean);
			}
			
			return listaProizvoda;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja proizvoda manje od cene i pretrage.");
			System.err.println(e);
			return null;
		}    

	}

}
