package pku.cis.vscube.model;

public class Same extends Node{
	public Same(){
		kind = 1;
		dataList.clear();
	}
	
	public void add(Data a){
		dataList.add(a);
	}
}
