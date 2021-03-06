package beans;

import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import common.ValidatorMessage;
//import common.ValidatorMessage;
import dal.*;
import models.Korisnik;

@ManagedBean
public class KorisnikBean 
{
	private Integer id;
	private String email;
	private String password;
	private String ime;
	private String prezime;
	private String adresa;
	private String telefon;
	private String uloga;
	private String ponovljeniPassword;
	
	Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	
	private int idKorpe;	//u tabeli korpa imamo korisnik_id
	private float ukupna_cena_korpe;		//u tabeli korpaitem imamo proizvod_id i kolicina
	private ArrayList<ProizvodBean> proizvodiUKorpi; // proizvod_id u korpaitem
	private KorisnikBean korisnik;
	
	public KorisnikBean()
	{
	
	}
	
	public KorisnikBean(Integer id, String email, String password, String ime, String prezime, String adresa, String telefon, String uloga, String ponovljeniPassword) 
	{
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.ime = ime;
		this.prezime = prezime;
		this.adresa = adresa;
		this.telefon = telefon;
		this.uloga = uloga;
		this.ponovljeniPassword = ponovljeniPassword;
	}
	
	public KorisnikBean( String email, String password, String ime, String prezime, String adresa, String telefon, String uloga, String ponovljeniPassword) 
	{
		super();
		this.email = email;
		this.password = password;
		this.ime = ime;
		this.prezime = prezime;
		this.adresa = adresa;
		this.telefon = telefon;
		this.uloga = uloga;
		this.ponovljeniPassword = ponovljeniPassword;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	public String getUloga() {
		return uloga;
	}
	public void setUloga(String uloga) {
		this.uloga = uloga;
	}
	public String getPonovljeniPassword() {
		return ponovljeniPassword;
	}
	public void setPonovljeniPassword(String ponovljeniPassword) {
		this.ponovljeniPassword = ponovljeniPassword;
	}
	public final Map<String, Object> getSessionMap() {
		return sessionMap;
	}
	public final void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}
	
	public boolean isAdmin(Korisnik korisnik) 
	{
		if(korisnik == null) 
			return false;
		if(korisnik.getUloga().equals("admin"))
			return true;
		return false;
	}
	
	public void prijava()
	{
		FacesContext fc = FacesContext.getCurrentInstance();
		Korisnik k = new KorisnikDAL().pronadjiKorisnika(email, password);
		
		if(k == null) 
		{
			System.out.println("Prijava je neuspesna.");
			fc.addMessage("form-prijava:message-prijava", new FacesMessage(ValidatorMessage.UNSUCCESSFUL_LOGIN, "Nije pronadjen korisnik!"));
			
			clear();
		}
		else 
		{
			System.out.println("Uspesna prijava.");
			
			sessionMap.put("USER", k);
			new KorpaBean().kreirajKorpu(k);
			
			try {
				
				FacesContext.getCurrentInstance().getExternalContext().redirect("/Slatkisi/index.jsf");	//redirekcija
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void odjava() 
	{
		sessionMap.put("USER", null);
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		
		try {
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("/Slatkisi/index.jsf");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void registracija() 
	{
		FacesContext fc = FacesContext.getCurrentInstance();
		
		if((password.equals(ponovljeniPassword)) == false)
			fc.addMessage("form-registracija:message-registracija", new FacesMessage(ValidatorMessage.UNSUCCESSFUL_LOGIN, "Sifre moraju biti iste."));
		else 
		{
			this.uloga="korisnik";
			boolean success = new KorisnikDAL().dodajKorisnika(this);
			if(success) //da li je dodat 
			{
				new KorisnikDAL().pronadjiKorisnika(email, password);
				this.prijava();
			}
			else 
			{
				fc.addMessage("form-registracija:message-registracija", new FacesMessage(ValidatorMessage.UNSUCCESSFUL_LOGIN, "Takav korisnik vec postoji."));
			}

		}
		
	}
	
	public void redirect() 
	{
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/Slatkisi/index.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void inicijalizuj() 
	{
		
		KorisnikBean inicijalizacija = new KorisnikBean("admin@admin.com", "admin", "Admin", "Admin", "", "", "admin", "admin");
		
		if(new KorisnikDAL().proveriKorisnika(inicijalizacija.getEmail()) == false) //ako nemamo, dodajemo ga
			new KorisnikDAL().dodajKorisnika(inicijalizacija);
		
		if(new KategorijeDAL().dajSveKategorije() == null)	//ako nemamo kategorije, dodajemo
		{
			new KategorijeDAL().dodajKategoriju(new KategorijeBean(1,"Torte"));
			new KategorijeDAL().dodajKategoriju(new KategorijeBean(2,"Kola??i"));
			new KategorijeDAL().dodajKategoriju(new KategorijeBean(3,"Mafini"));
			new KategorijeDAL().dodajKategoriju(new KategorijeBean(4,"??okolade"));
			new KategorijeDAL().dodajKategoriju(new KategorijeBean(5,"Slatki hleb"));
			new KategorijeDAL().dodajKategoriju(new KategorijeBean(6,"Brownie"));
		}
		
		if(new ProizvodDAL().dajSveProizvode() == null) 	//ako nemamo proizvode, dodajemo
		{
			try {
				
				new ProizvodDAL().dodajProizvod(new ProizvodBean(1,"Cheescake", "Torta sa sirom ili torta od sira, je slatka okrugla torta koja se satoji od jednog ili vi??e slojeva. Glavni i najgu????i sloj, sastoji se od me??avine mekog, sve??eg sira, jaja i ??e??era; ako ima i donji sloj, on je obi??no tvrd ili se sastoji od samlevenih kola??i??a, slatkih krekera, peciva ili biskvit torte" , 1, 5, 550, "slika",""), extractBytes("/slike/1.jpg"));
				new ProizvodDAL().dodajProizvod(new ProizvodBean(2,"Tiramisu", "Tiramisu je italijanski desert sa kafom. Napravljen je od damskih prstiju namo??enih u kafu, slojevitih ??lagom od jaja, ??e??era i mascarpone sira, aromatiziran kakaom. Recept je prilago??en mnogim vrstama kola??a i drugih deserta. Njeno poreklo je ??esto ospor" , 1, 10, 550, "slika",""), extractBytes("/slike/2.jpg"));
				new ProizvodDAL().dodajProizvod(new ProizvodBean(3,"??okoladna torta", "??okoladna torta ili ??okoladni gato je kola?? aromatiziran rastopljenom ??okoladom, kakaom u prahu ili oboje." , 1, 10, 500, "slika",""), extractBytes("/slike/3.jpg"));
				new ProizvodDAL().dodajProizvod(new ProizvodBean(4,"Milka", "Milka ??okolada od 100g" , 4, 50, 60, "slika",""), extractBytes("/slike/4.jpg"));
				new ProizvodDAL().dodajProizvod(new ProizvodBean(5,"Najlep??e ??elje", "" , 4, 50, 120, "slika",""), extractBytes("/slike/5.jpg"));
				new ProizvodDAL().dodajProizvod(new ProizvodBean(6,"Rafaelo kola??", "Rafaelo kola?? se prodaje na gramazu. Prikazana cena je za 1 kg." , 2, 50, 400, "slika",""), extractBytes("/slike/7.jpg"));
				new ProizvodDAL().dodajProizvod(new ProizvodBean(7,"Snikers ??tanglice", "Snikers ??tanglice se prodaju na gramazu. Prikazana cena je za 1 kg." , 2, 150, 400, "slika",""), extractBytes("/slike/8 0.jpg"));
				new ProizvodDAL().dodajProizvod(new ProizvodBean(8,"Snikers kuglice", "Snikers kuglice se prodaju na gramazu. Prikazana cena je za 1 kg." , 2, 100, 450, "slika",""), extractBytes("/slike/8.jpg"));
				new ProizvodDAL().dodajProizvod(new ProizvodBean(9,"??okoladni rolat", "" , 5, 20, 300, "slika",""), extractBytes("/slike/9.jpg"));
				
				new ProizvodDAL().dodajProizvod(new ProizvodBean(10,"Mafini sa jagodama", "D??em prepun so??nih bobica, ovi muffini od jagoda sa o??trim, zlatnim vrhovima lepi su koliko i ukusni." , 3, 15, 50, "slika",""), extractBytes("/slike/10.jpg"));
				new ProizvodDAL().dodajProizvod(new ProizvodBean(11,"Mafini sa borovnicama", "Prepuni sve??om borovnicom sa ne??nom mrvicom i svetlucavom koricom ??ec??era, ovo su zaista najbolji mafini od borovnica." , 3, 15, 50, "slika",""), extractBytes("/slike/11.jpg"));
				new ProizvodDAL().dodajProizvod(new ProizvodBean(12,"Brookies (plo??ice sa kola??ic??ima od braunja i ??okolade)", "Napravljeni od mermernih slojeva testa za pecivo i testa od keksa od ??okoladnih kola??ic??a, brokiji su zabavna poslastica za sve uzraste." , 6, 25, 60, "slika",""), extractBytes("/slike/12.jpg"));
				new ProizvodDAL().dodajProizvod(new ProizvodBean(13,"Supernatural Brownies", "Prljavi u sredini i slatki na povr??ini, ovi natprirodni kola??i poslasti??ara Nick Malgieri-a zaista su van ovog sveta." , 6, 30, 60, "slika",""), extractBytes("/slike/12 0.jpg"));
				new ProizvodDAL().dodajProizvod(new ProizvodBean(14,"??okoladni hleb od banane", "??okoladni ??ips i kakao u prahu daju ovom hlebu od ??okoladne banane dubok ukus ??okolade, dok ga dodatak pavlake ??ini ne??nijim." , 5, 80, 100, "slika",""), extractBytes("/slike/13.jpg"));
				new ProizvodDAL().dodajProizvod(new ProizvodBean(15,"Medenjaci", "Voleni od dece i odraslih, ovi staromodni medenjaci savr??ena su poslastica koja vam ostaje pri ruci tokom praznika." , 5, 200, 150, "slika",""), extractBytes("/slike/13 0.jpg"));
				
			} catch (IOException e) {
				System.out.println("Nije dodat proizvod");
				e.printStackTrace();
			}
		}
		
		new KorisnikDAL().updateBaza();
		
	}
	
	public byte[] extractBytes (String ImageName) throws IOException 
	{
		 BufferedImage bImage = ImageIO.read(this.getClass().getResource(ImageName));
	      ByteArrayOutputStream bos = new ByteArrayOutputStream();
	      ImageIO.write(bImage, "jpg", bos );
	      byte [] data = bos.toByteArray();
		return data;
	}
	
	public void clear(){
		setIme("");
		setPrezime("");
		setEmail("");
		setPassword("");
		setPonovljeniPassword("");
		setAdresa("");
		setTelefon("");
	   
	}

	public int getIdKorpe() {
		return idKorpe;
	}

	public void setIdKorpe(int idKorpe) {
		this.idKorpe = idKorpe;
	}

	public float getUkupna_cena_korpe() {
		return ukupna_cena_korpe;
	}

	public void setUkupna_cena_korpe(float ukupna_cena_korpe) {
		this.ukupna_cena_korpe = ukupna_cena_korpe;
	}

	public ArrayList<ProizvodBean> getProizvodiUKorpi() {
		return proizvodiUKorpi;
	}

	public void setProizvodiUKorpi(ArrayList<ProizvodBean> proizvodiUKorpi) {
		this.proizvodiUKorpi = proizvodiUKorpi;
	}
	
	public KorisnikBean vratiKorisnika(int idKorisnika)
	{
		korisnik = new KorisnikDAL().vratiKorisnika(idKorisnika);
		return korisnik;
	}
	
}
