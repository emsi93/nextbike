package nextbike.model;

public class Station {

	private int idStacji;
	private String nazwaStacji;
	private Double lat;
	private Double lng;
	private int iloscStanowisk;
	public Station(int idStacji, String nazwaStacji, Double lat, Double lng, int iloscStanowisk) {
		super();
		this.idStacji = idStacji;
		this.nazwaStacji = nazwaStacji;
		this.lat = lat;
		this.lng = lng;
		this.iloscStanowisk = iloscStanowisk;
	}
	public int getIdStacji() {
		return idStacji;
	}
	public void setIdStacji(int idStacji) {
		this.idStacji = idStacji;
	}
	public String getNazwaStacji() {
		return nazwaStacji;
	}
	public void setNazwaStacji(String nazwaStacji) {
		this.nazwaStacji = nazwaStacji;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public int getIloscStanowisk() {
		return iloscStanowisk;
	}
	public void setIloscStanowisk(int iloscStanowisk) {
		this.iloscStanowisk = iloscStanowisk;
	}
	@Override
	public String toString() {
		return "Station [idStacji=" + idStacji + ", nazwaStacji=" + nazwaStacji + ", lat=" + lat + ", lng=" + lng
				+ ", iloscStanowisk=" + iloscStanowisk + ", getIdStacji()=" + getIdStacji() + ", getNazwaStacji()="
				+ getNazwaStacji() + ", getLat()=" + getLat() + ", getLng()=" + getLng() + ", getIloscStanowisk()="
				+ getIloscStanowisk() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
	
}
