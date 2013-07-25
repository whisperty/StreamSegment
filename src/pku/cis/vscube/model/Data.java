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

	double get(String s){
		if(s == "highest")
			return highest;

		if(s == "lowest")
			return lowest;
		
		if(s == "timeSpan")
			return timeSpan;
		
		if(s == "displacement")
			return displacement;
		
		return -100000000;
	}
}
