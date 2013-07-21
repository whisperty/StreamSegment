package pku.cis.vscube.query;

public class BasicVariation{
	int dimIndex;
	int variType;
	String variName;
	int bts;
	int ets;
	
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
}