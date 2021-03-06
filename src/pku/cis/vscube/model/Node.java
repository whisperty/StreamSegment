package pku.cis.vscube.model;
import java.util.LinkedList;
import java.util.List;

import pku.cis.vscube.query.BasicVariation;


public class Node {
	int kind;// 0 - basic node, 1 - Same, 2 - Trend
	List<Data>dataList = new LinkedList();
	public Node(){ 
		kind = 0;
		dataList.clear(); 
		}
	
	public Node(Data a){
		kind = 0;
		dataList.clear();
		dataList.add(a);
	}
	
	public boolean isBasic(){
		return kind == 0;
	}
	
	public boolean isSame(){
		return kind == 1;
	}
	
	public boolean isTrend(){
		return kind == 2;
	}
	
	int sgn(double a, double eps){
		if(Math.abs(a) < eps)
			return 0;
		
		if(a< -eps)	
			return -1;
		return 1;
	}
	
	public boolean equals(Data a, double eps){
		if(isBasic() == false)	
			return false;
		
		/*double highest;
		double lowest;
		double timeSpan;
		double displacement;*/
		Data b = dataList.get(dataList.size() - 1);
		if(Math.abs(a.highest - b.highest) > eps)
			return false;
		
		if(Math.abs(a.lowest - b.lowest) > eps)
			return false;
		
		if(Math.abs(a.timeSpan - b.timeSpan) > eps)
			return false;
		
		if(Math.abs(a.displacement - b.displacement) > eps)
			return false;
		
		return true;
	}
	
	public int getMaxTimestamp(){
		int size = dataList.size();
		return dataList.get(size - 1).getTimestamp();
	}
	
	public int getMinTimestamp(){
		return dataList.get(0).getTimestamp();
	}
	
	public Data getFirstData(){
		return dataList.get(0);
	}
	
	public Data getLastData(){
		return dataList.get(dataList.size() - 1);
	}
	
	public boolean isEmpty(){
		return dataList.isEmpty();
	}
	
	//[1] - all asc, [-1] - all des, [0] - other 
	public int trend(Data a, double eps){
		/*double highest;
		double lowest;
		double timeSpan;
		double displacement;*/
		Data b = dataList.get(dataList.size() - 1);
		int flag = sgn(a.highest - b.highest, eps);
		if(flag != sgn(a.lowest - b.lowest, eps))
			return 0;
		
		if(flag != sgn(a.timeSpan - b.timeSpan, eps))
			return 0;
		
		if(flag != sgn(a.displacement - b.displacement, eps))
			return 0;
		
		return flag;
	}
	
	//added by ty 20130721
	public int getKind(){
		return kind;
	}
	
	public int del(int exprieTime){
		int num = 0;
		
		while(dataList.size() > 0){
			if(dataList.get(0).timeSpan >= exprieTime){
				dataList.remove(0);
				num++;
			}
				else
				break;
		}
		
		if(dataList.size() == 1)
			kind = 0;
		
		return num;
	}

	public void getBv(LinkedList<BasicVariation> bvColl, BasicVariation bv, double eps) {
		int size = dataList.size();
		Data pre = new Data();
		for(int i = 0; i < size; i++){
			Data now = dataList.get(i);
			if(i > 0 && isSameVari(pre, now, bv, eps)){
				BasicVariation bvResult = new BasicVariation(bv);
				bvResult.setTime(pre.getTimestamp(), now.getTimestamp());
				bvColl.add(bvResult);				
			}
			
			pre = now;
		}
	}

	public boolean isSameVari(Data a, Data b, BasicVariation bv, double eps) {
		double ta = a.get(bv.getVariName()), tb = b.get(bv.getVariName());
		return bv.isSameVari(sgn(tb - ta, eps));
	}	
}
