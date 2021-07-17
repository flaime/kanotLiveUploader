package kanotLiveUploader.paresePdf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Tävling {

//	private ArrayList<Lopp> loppen = new ArrayList<>();
	private ObservableList<Lopp> loppen = FXCollections.observableArrayList();
	private List<Klubb> klubbarna = new ArrayList<>();
	private String datum = "non";
	public Tävling(String datum) {
		this.datum = datum;
	}

	public String getDatum() {
		return datum;
	}
	public void addLopp(ArrayList<Lopp> lopp) {
		loppen.addAll(lopp);
		
	}
	
	
	@Override
	public String toString() {
		String ret = "";
		for(Lopp l : loppen)
			ret += l+"\n-------------------------------------------\n";

		ret += klubbarna;
		return ret;
	}

	public ObservableList<Lopp> getLopp() {
		return loppen;
	}

	public boolean remove(Lopp radera) {
		return loppen.remove(radera);
		
	}
	
	public void addLoppHoppaÖver(ObservableList<Lopp> lopp) {
		ArrayList<Lopp> temp = new ArrayList<>();
		for(Lopp bana:lopp){
			temp.add(bana);
		}
		for(Lopp loppet: temp){
			if(finnsLopp(loppet.getLoppNummerIntegerProperty().getValue()) == false){
				loppen.add(loppet);
			}
		}
		
	}
	
	public boolean finnsLopp(int i){
		boolean ret = false;
		for(Lopp x:loppen){
			if(x.getLoppNummerIntegerProperty().getValue() == i)
				ret = true;
		}
		
		return ret;
	}
	public static boolean körts = false;
	public void addLoppErsätt(ObservableList<Lopp> lopp) {
		försökTaBort(lopp);
		
		loppen.addAll(lopp);
	}
	
	public void försökTaBort(int i){
		Lopp currentTab = null;
		for(ListIterator<Lopp> iterator = loppen.listIterator(); iterator.hasNext(); currentTab = iterator.next()) {
			if(currentTab.getLoppNummerIntegerProperty().getValue()==i)
		    	iterator.remove();
		}
	}
	
	public void försökTaBort(ObservableList<Lopp> lopp){
		loppen.removeIf(p -> {
			for(Lopp bana : lopp)
				if(p.getloppnummer().equalsIgnoreCase(bana.getloppnummer()))
					return true;
			return false;
		});
	}
	
	public String getJsonString(){
		String json = "{";
		json += "\"date\":";
		json += "\"" +getDatum() + "\",";
		json += "\"name\":";
		json += "null,";
		json += "\"races\":[";
		
		for(int i=0; i < getLopp().size(); i++)
		{
//		    Console.Write(A[i]);
			json += getLopp().get(i).getJson();
		    if (i != getLopp().size()-1)
		    	json += ",";
//		        Console.Write(",");
		}
		json += "]";
//		json += ",\"Clubs\":" + getClubJson();
		json +="}";
		
		return json;
		
	}

	public String getClubJson() {
		String klubbar ="";
		try {
			klubbar = new ObjectMapper().writeValueAsString(klubbarna);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			klubbar = "[]";
		}
		return klubbar;
	}

	public void addKlubb(Klubb klubb) {
		klubbarna.add(klubb);
	}

	public void addKlubbarErsätt(List<Klubb> klubbarna) {
		this.klubbarna = klubbarna;
	}

	public List<Klubb> getCLubs() {
		return klubbarna;
	}

	public void addKlubbarHoppaÖver(List<Klubb> cLubs) { //TODO test this
		cLubs.forEach(club -> {
			if (klubbarna.stream().noneMatch(clubExisting -> clubExisting.getLicensNummer().equalsIgnoreCase(club.getLicensNummer()))) {
				klubbarna.add(club);
			}
		});
	}
}
