public class seg{
	public double p1, p2;
	public int ts1, ts2;
	public double mergecost=0;
	public int mergeindex;
	public boolean valid;
	
	seg()
	{
		
	}
	seg(double p1, int ts1, double p2, int ts2){
		this.p1=p1;
		this.ts1=ts1;
		this.p2=p2;
		this.ts2=ts2;
	}
	public int getTime() {
		// TODO Auto-generated method stub
		return ts2;
	}
	
	public void print(){
		System.out.println("("+ts1+","+p1+") ("+ts2+","+p2+")");
	}
	
	public boolean isIn(double x){
		return x >= ts1 && x <= ts2;
	}
	
	public double calY(double x){
		return p1 + (p2 - p1) * (x - ts1) / (ts2 - ts1);
	}
}