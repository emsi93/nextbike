package nextbike.model;


public class Point {

	private Double lat;
	private Double lng;
	private int idstacji;
	
	public Point(Double lat, Double lng, int idstacji) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.idstacji = idstacji;
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
	
	
}
