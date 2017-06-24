package nextbike.model;

public class Times {

	private int time;
	private int stationFrom;
	private int stationTo;

	public Times(int time, int stationFrom, int stationTo) {
		super();
		this.time = time;
		this.stationFrom = stationFrom;
		this.stationTo = stationTo;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getStationFrom() {
		return stationFrom;
	}

	public void setStationFrom(int stationFrom) {
		this.stationFrom = stationFrom;
	}

	public int getStationTo() {
		return stationTo;
	}

	public void setStationTo(int stationTo) {
		this.stationTo = stationTo;
	}

}
