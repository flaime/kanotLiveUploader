package kanotLiveUploader.paresePdf;

public class Placering {

	private String namn = "non";
	private String klubb = "non";
	private String placering = "non";
	private String tid = "non";
	public Placering(String namn, String klubb, String placering, String tid) {
		this.namn = namn;
		this.klubb = klubb;
		this.placering = placering;
		this.tid = tid;
	}
	@Override
	public String toString(){
		return "Namn: " + namn + "\nKlubb: " + klubb+ "\nPlacering: " + placering + "\nTid: " + tid;
	}

}
