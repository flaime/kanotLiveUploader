package kanotLiveUploader.paresePdf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Klubb {

	private String kortNamn;

	private String fullNamn;

	private String desplayNamn;

	private String licensNummer;

	public Klubb(String kortNamn, String fullNamn, String desplayNamn, String licensNummer) {
		this.kortNamn = kortNamn;
		this.fullNamn = fullNamn;
		this.desplayNamn = desplayNamn;
		this.licensNummer = licensNummer;
	}

	@JsonProperty("shortName")
	public String getKortNamn() {
		return kortNamn;
	}

	public void setKortNamn(String kortNamn) {
		this.kortNamn = kortNamn;
	}

	@JsonProperty("fullName")
	public String getFullNamn() {
		return fullNamn;
	}

	public void setFullNamn(String fullNamn) {
		this.fullNamn = fullNamn;
	}

	@JsonProperty("displayName")
	public String getDesplayNamn() {
		return desplayNamn;
	}

	public void setDesplayNamn(String desplayNamn) {
		this.desplayNamn = desplayNamn;
	}
	@JsonProperty("licensNumber")
	public String getLicensNummer() {
		return licensNummer;
	}

	public void setLicensNummer(String licensNummer) {
		this.licensNummer = licensNummer;
	}
}
