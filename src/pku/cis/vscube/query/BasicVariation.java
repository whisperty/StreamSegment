package pku.cis.vscube.query;

public class BasicVariation{
	int dimIndex;//ά�Ⱥ�
	int variType;//1�� 0 �� -1
	String variName;//������
	int bts;//��ʼʱ��
	int ets;//����ʱ��
	
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
}