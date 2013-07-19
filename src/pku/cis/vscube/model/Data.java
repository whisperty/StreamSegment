package pku.cis.vscube.model;

public class Data {
	int timestamp;
	double highest;
	double lowest;
	double timeSpan;
	double displacement;
	
	Data()
	{
		
	}
	public Data(int ts, double maxpoint, double minpoint, int tspan, double d) {
		this.timeSpan=ts;
		this.highest = maxpoint;
		this.lowest = minpoint;
		this.timeSpan = tspan;
		this.displacement = d;
	}

	public int getTimestamp(){
		return timestamp;
	}

}
