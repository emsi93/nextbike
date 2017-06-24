package nextbike.model;

public class RoadStation {

	private String pointA;
	private String pointB;
	public RoadStation(String pointA, String pointB) {
		super();
		this.pointA = pointA;
		this.pointB = pointB;
	}
	
	public RoadStation()
	{
		this(null,null);
	}
	public String getPointA() {
		return pointA;
	}
	public void setPointA(String pointA) {
		this.pointA = pointA;
	}
	public String getPointB() {
		return pointB;
	}
	public void setPointB(String pointB) {
		this.pointB = pointB;
	}

	@Override
	public String toString() {
		return "RoadStation [pointA=" + pointA + ", pointB=" + pointB + "]";
	}
	
	
}
