package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import beans.KorisnikBean;
import beans.ProizvodBean;
import models.Korisnik;
import models.Proizvod;

public class KorisnikDAL 
{
	public EntityManagerFactory factory = Persistence.createEntityManagerFactory("Slatkisi");
	public EntityManager entityManager = factory.createEntityManager();
	
	public Korisnik pronadjiKorisnika(String email, String password) 
	{
		List<Korisnik> results = new LinkedList<Korisnik>();
		
		System.out.println(email + ":" + password); 
		try {
			Query query = entityManager.createQuery("SELECT k FROM Korisnik k WHERE k.email = '"+email+"' AND k.password='"+password+"'");
			
			results = query.getResultList();
	        if (results.isEmpty()) 
	        	return null;
	        else if (results.size() == 1) 
	        	return (Korisnik)results.get(0);
		
		}catch (Exception e) {
			System.out.println("Greska u upitu kod pronalazenja korisnika.");
			System.err.println(e);
		}
        return null;
	}
	
	public boolean dodajKorisnika(KorisnikBean noviKorisnik) 
	{
		try {
			entityManager.getTransaction().begin();
			
			if(proveriKorisnika(noviKorisnik.getEmail())) 
				return false;
			
			Korisnik k = new Korisnik();
			
			k.setIme(noviKorisnik.getIme());
			k.setPrezime(noviKorisnik.getPrezime());
			k.setEmail(noviKorisnik.getEmail());
			k.setPassword(noviKorisnik.getPassword());
			k.setAdresa(noviKorisnik.getAdresa());
			k.setTelefon(noviKorisnik.getTelefon());
			k.setUloga(noviKorisnik.getUloga());
			
			entityManager.persist(k);
			entityManager.getTransaction().commit();
			
			return true;
			
		}catch (Exception e) {
			System.out.println("Greska u upitu kod dodavanja korisnika.");
			System.err.println(e);
			return false;
		}
	}

	public boolean proveriKorisnika(String email) 
	{
		List<Korisnik> results = new LinkedList<Korisnik>();
		
		try {
			Query query = entityManager.createQuery("SELECT k FROM Korisnik k WHERE k.email = '"+email+"'");
			
			results = query.getResultList();
		
		
		if (results.isEmpty()) 
        	return false;
		
        return true;
        
		}catch (Exception e) {
			System.out.println("Greska u upitu kod provere korisnika.");
			System.err.println(e);

			return false;
		}
	}

	
	public void updateBaza() 
	{
		
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/slatkisi","root","");
			String sql;
			Statement stmt;
			
			sql = "ALTER TABLE proizvod DROP FOREIGN KEY FK_proizvod_KATEGORIJA_ID;";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			sql = "ALTER TABLE porudzbina DROP FOREIGN KEY FK_porudzbina_KORISNIK_ID; ";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			sql = "ALTER TABLE porudzbinaItem  DROP FOREIGN KEY FK_porudzbinaItem_PROIZVOD_ID; ";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			sql = "ALTER TABLE porudzbinaItem  DROP FOREIGN KEY FK_porudzbinaItem_PORUDZBINA_ID;";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			sql = "ALTER TABLE korpa  DROP FOREIGN KEY FK_korpa_KORISNIK_ID;";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			sql = "ALTER TABLE korpaItem  DROP FOREIGN KEY FK_korpaItem_PROIZVOD_ID;";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			sql = "ALTER TABLE korpaItem  DROP FOREIGN KEY FK_korpaItem_KORPA_ID;";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			

			sql = "ALTER TABLE proizvod ADD CONSTRAINT FK_proizvod_KATEGORIJA_ID FOREIGN KEY (KATEGORIJA_ID)  REFERENCES kategorija(ID)  ON DELETE CASCADE;";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			sql = "ALTER TABLE porudzbina ADD CONSTRAINT FK_porudzbina_KORISNIK_ID FOREIGN KEY (KORISNIK_ID) REFERENCES korisnik (ID);";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			sql = "ALTER TABLE porudzbinaItem ADD CONSTRAINT FK_porudzbinaItem_PROIZVOD_ID FOREIGN KEY (PROIZVOD_ID) REFERENCES proizvod (ID);";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			sql = "ALTER TABLE porudzbinaItem ADD CONSTRAINT FK_porudzbinaItem_PORUDZBINA_ID FOREIGN KEY (PORUDZBINA_ID) REFERENCES porudzbina (ID);";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			sql = "ALTER TABLE korpa ADD CONSTRAINT FK_korpa_KORISNIK_ID FOREIGN KEY (KORISNIK_ID) REFERENCES korisnik (ID);";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			sql = "ALTER TABLE korpaItem ADD CONSTRAINT FK_korpaItem_PROIZVOD_ID FOREIGN KEY (PROIZVOD_ID)  REFERENCES proizvod(ID);";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			sql = "ALTER TABLE korpaItem ADD CONSTRAINT FK_korpaItem_KORPA_ID FOREIGN KEY (KORPA_ID)  REFERENCES korpa(ID)  ON DELETE CASCADE;";
			stmt = conn.createStatement(); stmt.execute(sql);
			
			

		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}
	}

	public KorisnikBean vratiKorisnika(int idKorisnika) 
	{
		List<Korisnik> results = new LinkedList<Korisnik>();
		KorisnikBean bean  = new KorisnikBean();
		
		try {
			Query query = entityManager.createQuery("SELECT k FROM Korisnik k WHERE k.id =:id ");
			query.setParameter("id", idKorisnika);
			
			results = query.getResultList();
			
			if (results.isEmpty()) 
				return null;
			 
			for (Korisnik k : results) 
			{
				bean.setId(k.getId());
				bean.setIme(k.getIme());
				bean.setPrezime(k.getPrezime());
				bean.setAdresa(k.getAdresa());
				bean.setEmail(k.getEmail());
				bean.setPassword(k.getPassword());
				bean.setTelefon(k.getTelefon());
				bean.setUloga(k.getUloga());
			}
			
			return bean;
		}catch (Exception e) {
			System.out.println("Greska u upitu kod vracanja odredjenog korisnika.");
			System.err.println(e);
		}
		return null;
	}
}
