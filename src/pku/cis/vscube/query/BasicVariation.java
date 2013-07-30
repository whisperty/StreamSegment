package pku.cis.vscube.query;

public class BasicVariation{
	int dimIndex;//维度号
	int variType;//1， 0 ， -1
	String variName;//变量名
	int bts;//开始时间
	int ets;//结束时间
	
	public BasicVariation(BasicVariation bv){
		this.dimIndex = bv.dimIndex;
		this.variName = bv.variName;
		this.variType = bv.variType;
	}
	
	public BasicVariation(int dimIndex, String variName, int variType){
		this.dimIndex = dimIndex;
		this.variName = variName;
		this.variType = variType;
	}
	
	public int getDimIndex(){
		return dimIndex;
	}
	
	public void setTime(int bts, int ets){
		this.bts = bts;
		this.ets = ets;
	}
	
	public boolean isSameVari(int trend) {
		// TODO Auto-generated method stub
		return trend == variType;
	}
	
	public String getVariName(){
		return variName;
	}
	
	private int min(int a, int b){
		if(a < b)	
			return a;
		return b;
	}
	
	private int max(int a, int b){
		if(a > b)	
			return a;
		return b;
	}
	
	public boolean isLegal(){
		return bts <= ets;
	}
	
	public BasicVariation VSCON(BasicVariation b){
		BasicVariation ans = new BasicVariation(b);
	
		if(ets < b.bts || b.ets < bts)
			ans.setTime(10, 0);
		else
			ans.setTime(max(bts, b.bts),  min(ets, b.ets));
		return ans;
	}
	
	public BasicVariation VSSEQ(BasicVariation b, double eps){
		BasicVariation ans = new BasicVariation(b);
	
		if(Math.abs(ets - b.bts) < eps || Math.abs(bts - b.ets) < eps)
			ans.setTime(min(bts, b.bts),  max(ets, b.ets));
		else
			ans.setTime(10, 0);
		
		return ans;
	}
	
}