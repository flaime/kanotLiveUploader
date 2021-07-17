package kanotLiveUploader.database;

import kanotLiveUploader.paresePdf.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class ParseDatabasToTävling {
	
//	public static void main(String[] args){
//		String databasURL = "C:\\Users\\ahlin\\OneDrive\\SPK\\testMedAcess\\Lidköping 2015.mdb";
//		String databasURL2 = "C:\\Users\\ahlin\\OneDrive\\SPK\\testMedAcess\\SUC Stockholm 20161.mdb";
//		String databasURL3 = "/Users/linus/Documents/saker/SM 2017_A_20170718_19_10.mdb";
//		System.out.println(new File(databasURL3));
//		new ParseDatabasToTävling(new File(databasURL3).getAbsolutePath());
//	}
	
	private Tävling Tävling = null;
	private DatabasConection db;
//	private LoadDatabasInformation logToOjekt = null;
//	public ParseDatabasToTävling(String databasURL, LoadDatabasInformation logToOjekt) throws IllegalArgumentException{
//	public ParseDatabasToTävling(String databasURL) throws IllegalArgumentException{
//		this(databasURL);
//		this.logToOjekt = logToOjekt;
//		log("Database connection has been esatabilitch");
//
//	}
	public ParseDatabasToTävling(String databasURL) throws IllegalArgumentException{
		
		Tävling Tävling = null;
		try {
			db = new DatabasConection(databasURL);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Databas conncetion could not set or not found or wrong type");
		}
		
		
	}
	
	private void log(String message){
//		if(logToOjekt != null)
//			logToOjekt.addDatabasLoadInfo(message);
		System.out.println("Database load: " + message);
	}
		
	public Tävling parserDatbas(){
		//create and set day string of all the days
		Tävling = createCompetitionWhitDaysSet();
		log("Loading  and parsing competition  databas");
		

		try {
			//Ladda alla klubbar
			ResultSet klubbar = db.query("SELECT * FROM Klubbar;");


			while(klubbar.next()){
				final String shortName = klubbar.getString("Klubb");
				final String fullNamn = klubbar.getString("Förklaring");
				final String displayName = klubbar.getString("Programnamn");
				final String licensnr = klubbar.getString("Licensnr");

				final Klubb klubb = new Klubb(shortName, fullNamn, displayName, licensnr);

				Tävling.addKlubb(klubb);

			}



			//load alla the lop:s and (start and finiced lopps)
			ResultSet StartOrResultat = db.query("SELECT * FROM [Start/Resultat];");
			
			Map<Integer, Lopp> lopp = new HashMap<>();
			
			while(StartOrResultat.next()){
//				Bana bb = new Bana(StartOrResultat.getString("Licens1"), "clubb"/* StartOrResultat.getString("Klubb")*/, StartOrResultat.getString("bana") , StartOrResultat.getString("tid"));
				Bana b = null;
				if(getPersons(StartOrResultat).size()>0)
					b = new Bana(getPersons(StartOrResultat), StartOrResultat.getString("bana") , StartOrResultat.getString("tid"));
				
				if(StartOrResultat.getInt("lopp") == 0){
					//emty not yet placed do noting
				}else if(lopp.containsKey(new Integer(StartOrResultat.getInt("lopp")) )&& b != null){
//					List<Person> person = getPersons(StartOrResultat);
//					Bana b = new Bana(namn, Klubb, bana, tid) //TODO titta till så att detta med personer sköts bättre
					lopp.get(StartOrResultat.getInt("lopp")).addBana(new ArrayList<Bana>( Arrays.asList(b)));// add(lop);
					System.out.println(b);
				}else if( b != null){
					Lopp lop = new Lopp(transformDate(StartOrResultat.getString("Datum")), StartOrResultat.getString("Distans"), StartOrResultat.getString("Starttid"), StartOrResultat.getString("Klass"), StartOrResultat.getString("Omgång"), StartOrResultat.getInt("Omgångnr"), StartOrResultat.getInt("Lopp"));
					lop.addBana(new ArrayList<Bana>( Arrays.asList(b)));
					lopp.put(new Integer(StartOrResultat.getInt("lopp")), lop);
				}
			}
			

			Tävling.addLopp(new ArrayList<Lopp>(lopp.values()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Wrong whit the lopp");
		}
		log("Comeptition has ben loaded.");
		System.out.println("Tävling = " + Tävling.getJsonString());
		System.out.println("Tävling = " + Tävling);
		
		return Tävling;
	}

	/**
	 * removes the "time" from the date and makes sure there always is data there.
	 * The default date is 1800-01-01
	 * @param startOrResultat
	 * @return
	 */
	public static String transformDate(String startOrResultat){
		return Optional.ofNullable(startOrResultat).orElse("1800-01-01 00:00:00.000000") .split(" ")[0];
	}

	private Tävling createCompetitionWhitDaysSet() {
		Tävling Tävling = null;
		try {
			ResultSet days = db.query("SELECT Datum FROM Dagar;");
			String daysString = "";
			while(days.next()){
				daysString += " " + days.getString("Datum");
			}
			Tävling = new Tävling(daysString);
			System.out.println("days: " + daysString);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Databas did not contain annay days set");
		}
		return Tävling;
	}
	
	private List<Person> getPersons(ResultSet startOrResultat) {
		List<Person> persons = new ArrayList<>();
		try {
			Person p = getPerson(startOrResultat.getString("Licens1"));
			if(p != null)
				persons.add(p);
		} catch (SQLException e) {
			throw new IllegalArgumentException("Wrong Licens1 for one person");
		}
		try {
			Person p = getPerson(startOrResultat.getString("Licens2"));
			if(p != null)
				persons.add(p);
		} catch (SQLException e) {
			throw new IllegalArgumentException("Wrong Licens2 for one person");
		}
		try {
			Person p = getPerson(startOrResultat.getString("Licens3"));
			if(p != null)
				persons.add(p);
		} catch (SQLException e) {
			throw new IllegalArgumentException("Wrong Licens3 for one person");
		}
		try {
			Person p = getPerson(startOrResultat.getString("Licens4"));
			if(p != null)
				persons.add(p);
		} catch (SQLException e) {
			throw new IllegalArgumentException("Wrong Licens4 for one person");
		}
		return persons;
	}

	private Person getPerson(String id){
		try {
//			PreparedStatement ps = conn.prepareStatement(
//			        "SELECT ID FROM localities WHERE locName=?");
//			ps.setString(1, "LEÓN");
			ResultSet person = db.query("SELECT * FROM [Deltagare] WHERE Licensnr = '" + id + "'");
			if(person != null && person.next()){
				System.out.println(person.getNString("Förnamn"));
				System.out.println(person.getNString("Efternamn"));
				System.out.println(person.getNString("Kön"));
				System.out.println(person.getNString("Födelseår"));
				System.out.println(person.getNString("Klubb"));
				return new Person(person.getNString("Förnamn"), person.getNString("Efternamn"), person.getNString("Kön"), person.getNString("Födelseår"), id, person.getNString("Klubb"));
			}else 
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
