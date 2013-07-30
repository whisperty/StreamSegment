

public class Segment {

	private int id;
	private DataItem from;			//start point
	private DataItem to;			//end point
	private double k;
	private double timeSpan;		
	
	public Segment( DataItem from, DataItem to ){
		this.from = from;
		this.to = to;
		k = this.calculateK(from, to);
		timeSpan = to.getTime() - from.getTime();
		//len = this.calculateLen(from, to);
	}
	
	public DataItem getFrom(){
		return from;
	}
	
	public DataItem getTo(){
		return to;
	}
	
	public double getK(){
		return k;
	}
	
	public double getTimeSpan(){
		return timeSpan;
	}
	
	
	public int getId( )
	{
		return id;
	}

	public void setId( int id )
	{
		this.id = id;
	}
	
	public double calculateK( DataItem from, DataItem to ){
		return (to.getValue() - from.getValue()) / 
						(to.getTime() - from.getTime());
	}
	
	public double calculateLen( DataItem from, DataItem to ){
		double a = Math.pow(to.getValue()-from.getValue(), 2);
		double b = Math.pow(to.getTime()-from.getTime(), 2);
		return Math.sqrt(a+b);
	}
	
	public double calculateDistance( Segment s )
	{
		return 0;
	}
	
	public String toString(){
		return "from=" + from.toString() +";k=" + k 
				+ ";TimeSpan=" + timeSpan;
	}
	
	
}