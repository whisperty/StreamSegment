package pku.cis.vscube.model;

import java.util.LinkedList;

public class Vscube{
	public static Vscube vsModel;
	LinkedList<Dimension> mvg;
	int DimNum = 5;
	int expireTime;
	
	Vscube(){
		initMvg();
		expireTime = 2000;
	}
	private void initMvg(){
		int i;
		for(i=0; i<DimNum; i++){
			Dimension dim = new Dimension();
			mvg.add(dim);
		}
	}
	public void addNode(int dim, Data d){
		Dimension dimension = mvg.get(dim);
		dimension.addNode(d);
		if(d.timestamp!=0 && d.timestamp / expireTime >1  ){
			updateMvg();
			expireTime += expireTime;
		}
	}
	
	public void updateMvg(){
		int i;
		for(i=0; i<DimNum; i++){
			mvg.get(i).updateDim(expireTime);//gffsdgfsd
		}
	}
}