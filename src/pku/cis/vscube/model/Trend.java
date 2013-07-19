package pku.cis.vscube.model;

public class Trend extends Node{
	public Trend(){
		kind = 2;
		dataList.clear();
	}
	
	public void add(Data a){
		dataList.add(a);
	}
	
	public int trend(double eps){
		int size = dataList.size();
		return new Node(dataList.get(size - 2)).trend(dataList.get(size - 1), eps);
	}
}
