package nextbike.model;

public class Point {

	private Double lat;
	private Double lng;
	private int idstacji;
	private String nazwaStacji;

	public Point(Double lat, Double lng, int idstacji, String nazwaStacji) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.idstacji = idstacji;
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

	public int getIdstacji() {
		return idstacji;
	}

	public void setIdstacji(int idstacji) {
		this.idstacji = idstacji;
	}

	public String getNazwaStacji() {
		return nazwaStacji;
	}

	public void setNazwaStacji(String nazwaStacji) {
		this.nazwaStacji = nazwaStacji;
	}

	@Override
	public String toString() {
		return "Point [lat=" + lat + ", lng=" + lng + ", idstacji=" + idstacji + ", nazwaStacji=" + nazwaStacji + "]";
	}

	
}
