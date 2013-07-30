package pku.cis.vscube.model;

import java.util.HashMap;
import java.util.LinkedList;

import pku.cis.vscube.query.BasicVariation;

public class Vscube{
	public static Vscube vsModel;
	LinkedList<Dimension> mvg;
	public HashMap<String, Integer> dimDic;
	public int DimNum = 5;
	int expireTime;
	
	public Vscube(){
		initMvg();
		//vsModel = new Vscube();
		expireTime = 2000;
	}
	private void initMvg(){
		int i;
		mvg = new LinkedList<Dimension>();
		for(i=0; i<DimNum; i++){
			Dimension dim = new Dimension();
			mvg.add(dim);
		}
		dimDic = new HashMap<String, Integer>();
		dimDic.put("P", 0);
		dimDic.put("Q", 1);
		dimDic.put("R", 2);
		dimDic.put("S", 3);
		dimDic.put("T", 4);
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
	
	public LinkedList<BasicVariation> getBv(BasicVariation bv){
		return vsModel.mvg.get(bv.getDimIndex()).getBv(bv);
	}
}