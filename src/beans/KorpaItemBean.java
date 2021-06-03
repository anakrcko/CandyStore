package beans;

import java.util.ArrayList;

import dal.KorpaItemDAL;
import models.Korpa;

public class KorpaItemBean {

	private int id;
	private int korpaId;
	private int kolicina;
	private int proizvodId;
	
	ArrayList<KorpaItemBean> items = null;

	public KorpaItemBean() {
		
	}
	public KorpaItemBean(int id, int korpaId, int kolicina, int proizvodId) {
		super();
		this.id = id;
		this.korpaId = korpaId;
		this.kolicina = kolicina;
		this.proizvodId = proizvodId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getKorpaId() {
		return korpaId;
	}
	public void setKorpaId(int korpaId) {
		this.korpaId = korpaId;
	}
	public int getKolicina() {
		return kolicina;
	}
	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}
	public int getProizvodId() {
		return proizvodId;
	}
	public void setProizvodId(int proizvodId) {
		this.proizvodId = proizvodId;
	}
	
	public ArrayList<KorpaItemBean> vratiKorpaItems(Korpa korpa) 
	{
		items = new KorpaItemDAL().vratiKorpaItems(korpa);
		
		return items;
	}
}
