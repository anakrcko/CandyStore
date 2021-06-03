package beans;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import dal.PorudzbinaItemDAL;

@ManagedBean
@SessionScoped
public class PorudzbinaItemBean {
	
	private int id;
	private int kolicina;
	private int porudzbinaId;
	private int proizvodId;

	private ArrayList<PorudzbinaItemBean> items;
	
	public PorudzbinaItemBean() {
		super();
		// TODO Auto-generated constructor stub
	}



	public PorudzbinaItemBean(int kolicina, int porudzbinaId, int proizvodId) {
		super();
		this.kolicina = kolicina;
		this.porudzbinaId = porudzbinaId;
		this.proizvodId = proizvodId;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public int getKolicina() {
		return kolicina;
	}



	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}



	public int getPorudzbinaId() {
		return porudzbinaId;
	}



	public void setPorudzbinaId(int porudzbinaId) {
		this.porudzbinaId = porudzbinaId;
	}



	public int getProizvodId() {
		return proizvodId;
	}



	public void setProizvodId(int proizvodId) {
		this.proizvodId = proizvodId;
	}

	
	public ArrayList<PorudzbinaItemBean> vratiItems(PorudzbinaBean porudzbina)
	{
		items = new PorudzbinaItemDAL().vratiItems(porudzbina.getId());
		return items;
	}
	
}
