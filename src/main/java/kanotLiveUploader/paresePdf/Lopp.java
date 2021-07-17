package kanotLiveUploader.paresePdf;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Lopp {

	private final StringProperty Datum = new SimpleStringProperty("non");
	private final StringProperty distans = new SimpleStringProperty("non");
	private final StringProperty tid = new SimpleStringProperty("non");
	private final StringProperty klass = new SimpleStringProperty("non");
	private final StringProperty typ = new SimpleStringProperty("non"); //likt final mellanhet osv
	
	
	private final IntegerProperty  typNummerInteger = new SimpleIntegerProperty(-1);
	private final StringProperty  typNummerString = new SimpleStringProperty("-1");
	private final IntegerProperty loppNummerInteger = new SimpleIntegerProperty(-1);
	private final StringProperty  loppNummerString = new SimpleStringProperty("-1");
	
	private ObservableList<Bana> banorna = FXCollections.observableArrayList();

	public Lopp(String datum, String distans, String tid, String klass, String typ, int typNummer, int loppNummer) {
		this.Datum.set(datum);
		this.distans.set(distans);
		this.tid.set(tid);
		this.klass.set(klass);
		this.typ.set(typ);
		this.typNummerInteger.set(typNummer);
		this.typNummerString.set(typNummer+"");
		this.loppNummerInteger.set(loppNummer);
		this.loppNummerString.set(loppNummer+"");
	}
	
	public ObservableList<Bana> getBanorna(){
		return banorna;
	}
	
	public void addBana(ArrayList<Bana> banor) {
		banorna.addAll(banor);
	}
	
	public void setLoppNummer(int loppNummer){
		this.loppNummerInteger.set(loppNummer);
		this.loppNummerString.set(loppNummer+"");
	}
	
	public void setTypNummer(int typNummer){
		this.typNummerInteger.set(typNummer);
		this.typNummerString.set(typNummer+"");
	}

	public IntegerProperty getLoppNummerIntegerProperty(){
		return loppNummerInteger;
	}
	

	public StringProperty getloppnummerStringProperty (){
		return loppNummerString;
	}
	public String getloppnummer (){
		return loppNummerString.getValue();
	}
	
	public IntegerProperty getTypNummerIntegerProperty(){
		return typNummerInteger;
	}
	

	public StringProperty getTypNummerStringProperty (){
		return typNummerString;
	}
	public String getTypNummer (){
		return typNummerString.getValue();
	}

	
	public StringProperty getKlassStringProperty() {
		return klass;
    }
	
	public StringProperty getDatumStringProperty() {
		return Datum;
	}
	public StringProperty getDistansStringProperty() {
		return distans;
	}
	public StringProperty getTidStringProperty() {
		return tid;
	}
	public StringProperty getTypStringProperty() {
		return typ;
	}
	
	
	public String getKlass() {
		return klass.getValue();
    }
	
	public String getDatum() {
		return Datum.getValue();
	}
	public String getDistans() {
		return distans.getValue();
	}
	public String getTid() {
		return tid.getValue();
	}
	public String getTyp() {
		return typ.getValue();
	}
	
	
	public String toString(){
		String ret = "Datum: " + Datum.get()+ "\nDistans: " + distans.get() + "\ntid: " +tid.get() + "\nklass: " + klass.get() + "\n\"typ\": " + typ.get() + "\n\"typ\" nummer: "+ typNummerInteger.get() + "\nloppnummer: " + loppNummerString.get();
		for(Bana b : banorna){
			ret+= "\n"+b +"\n";
		}
		
		return ret;
	}


	public void setdistans(String text) {
		distans.setValue(text);
	}

	public void setKlass(String text) {
		klass.setValue(text);
	}


	public void setStartTid(String text) {
		tid.setValue(text);
	}


	public void setTypAvLopp(String text) {
		typ.setValue(text);
	}


	public void setTypAvLoppNummer(int text) {
		typNummerInteger.setValue(text);
		typNummerString.setValue(text+"");
	}


	public void setDatum(String text) {
		Datum.setValue(text);
		
	}


	public String getJson() {
		String json = "{";
		json += transformTillVärde("dateTime", Datum.getValue() + "T" + tid.getValue() + ":00");
		json += ",";
		json += transformTillVärde("distance", distans.getValue());
		json += ",";
		json += transformTillVärde("time", tid.getValue());
		json += ",";
		json += transformTillVärde("raceClass", klass.getValue());
		json += ",";
		json += transformTillVärde("type", typ.getValue());
		json += ",";
		json += transformTillVärde("typeNumber", typNummerString.getValue());
		json += ",";
		json += transformTillVärde("raceNummer", loppNummerString.getValue());
		
		json += ",\"tracks\":[";
		//banor
		for(int i=0; i < banorna.size(); i++)
		{
//		    Console.Write(A[i]);
			json += banorna.get(i).getJson();
		    if (i != banorna.size()-1)
		    	json += ",";
//		        Console.Write(",");
		}
		json += "]}";
		return json;
	}
	
	private String transformTillVärde(String namn , String värde){
		return "\""+namn+"\":\""+värde+"\"";
	}

	public boolean FinnsBanaMedNummer(String nummer) {
		for(Bana x : banorna){
			if(nummer.equalsIgnoreCase(x.getBanaNummer().getValue()))
				return true;
		}
		return false;
	}

	public boolean FinnsBanaMedNamnet(String namn) {//TODO skriv den b�ttre s� att den letar efter namn i namnet och inte efter att det m�ste vara identiskt... typ K4:a eller s�
		for(Bana x : banorna){
			if(namn.trim().equalsIgnoreCase(x.getNamn().getValue().trim()))
				return true;
		}
		return false;
	}

	public boolean FinnsBanaMedKLubben(String klubb) {
		for(Bana x : banorna){
			if(klubb.trim().equalsIgnoreCase(x.getKlubb().getValue().trim()))
				return true;
		}
		return false;
	}

}
