package beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import common.ValidatorMessage;
import dal.KategorijeDAL;

@ManagedBean
public class KategorijeBean 
{
	
	private int id;
	private String naziv;
	
	private ArrayList<KategorijeBean> kategorije;
	private String novNaziv;
	private List<SelectItem> listaKategorija = new ArrayList <SelectItem> ();
	
	
	public KategorijeBean(Integer id, String naziv) 
	{
		super();
		this.id = id;
		this.naziv = naziv;
	}
	
	 @PostConstruct
	 public void init(){
		 this.getKategorije();
   
	 }
	
	public KategorijeBean() {
		super();
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	

	public String getNovNaziv() {
		return novNaziv;
	}


	public void setNovNaziv(String NovNaziv) {
		this.novNaziv = NovNaziv;
	}
	
	
    public List<SelectItem> getItems() 
    {
        return listaKategorija;
    }

    public void setItems(List<SelectItem> listaKategorija) 
    {
        this.listaKategorija = listaKategorija;
    }
	
	
	public ArrayList<KategorijeBean> getKategorije()
	{
		kategorije = null;
		kategorije = new KategorijeDAL().dajSveKategorije();
		
		listaKategorija.clear();
		
		if(kategorije != null) 
		{
			for (KategorijeBean k : kategorije) 
			{
				 SelectItem kategorija = new SelectItem();
				 kategorija.setLabel(k.getNaziv());
				 kategorija.setValue(k.getId());
			     
				 listaKategorija.add(kategorija);
			}
		}
		return kategorije;
		
	}
	
	public void dodaj() 
	{
		FacesContext fc = FacesContext.getCurrentInstance();
		
		if(this.getNovNaziv() != "") //ako ima nov naziv (nije prazno)
		{
			new KategorijeDAL().dodajKategoriju(this.getNovNaziv());	//dodajemo ga
			this.setNovNaziv("");	//praznimo
		}
		else
			fc.addMessage("form-addCategory:message-add", new FacesMessage(ValidatorMessage.UNSUCCESS, "Ime za kategoriju ne moze biti prazno!"));
	}
	
	public void obrisi(KategorijeBean kategorija) 
	{
		new KategorijeDAL().obrisiKategoriju(kategorija);
	}

}
