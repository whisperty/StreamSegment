import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.lang.Math;
import pku.cis.vscube.model.*;

public class swab{
	InputBuffer d;
	public ArrayList<seg> Seg_TS, T;
	int timebase;
	double baseline=0.94, errorbound=0.02;
	int index;
	char[] diname={'P', 'Q', 'R', 'S', 'T', 'U'};
//	boolean valid;
	ArrayList<seg> intersg;
	double bestpoint, bestts;
	
	static double MAXCOST = 0.05;
	Queue<seg> outputPoints;
	
	public swab(){
//		Cube=new vscube();
		index=0;
//		valid=false;
		timebase=0;
//		intersg=new ArrayList<seg>();
		outputPoints = new LinkedList<seg>();
		T=new ArrayList<seg>();
	}
	public void seg_ts(String[] dataArray){
		Seg_TS=new ArrayList<seg>();
		
		
		long startTime = System.nanoTime();
		Bottom_up(dataArray);
		
		CONCAT(0);
		
		//complete one phase
		while (T.size()>1){
			T.remove(0);
		}
		timebase=T.get(0).ts2+1;
		long consumingTime = System.nanoTime() - startTime;
	}
	
	public void resetOutput(){
		outputPoints.clear();
	}
	
	public void Bottom_up(String[] dataArray){
		int i, k;
		T.clear();
		int mergeSize=dataArray.length-1;
//		System.out.println(stream.length);
		for(i=0; i<mergeSize; i++){
			seg segts=new seg();
			segts.p1=Double.parseDouble(dataArray[i]);
			segts.p2=Double.parseDouble(dataArray[i+1]);
			segts.ts1=timebase+i;
			segts.ts2=timebase+i+1;
//			segts.valid=true;
			if(i!=0)
				T.get(T.size()-1).mergecost=mergeCost(T.get(T.size()-1), segts);
			T.add(segts);
//			System.out.print(segts.p2+" ");
		}
		T.get(i-1).mergecost=MAXCOST;
//		System.out.print("\n");
		while(((seg)T.get(minMerge())).mergecost<MAXCOST){
			k=minMerge();
//			System.out.println(k+"+"+T.size());
			seg segts=(seg)T.get(k);
			seg segts1=(seg)T.get(k+1);
			segts.p2=segts1.p2;
			segts.ts2=segts1.ts2;
			T.remove(k+1);
			if(T.size() == 1)
				break;
			if(k == T.size()-1)
				segts.mergecost = MAXCOST;
			else{
				segts1 = (seg)T.get(k+1);
				segts.mergecost=mergeCost(segts, segts1);
			}
			if(k!=0){
				seg segts0=(seg)T.get(k-1);
				segts0.mergecost=mergeCost(segts0, segts);
			}
		}
		AddtoOutput();
//		System.out.println("out");
	}
	private void AddtoOutput()
	{
		int i;
		for(i=0; i<T.size()-1; i++){
			outputPoints.offer((seg)T.get(i));
		}
		
	}
	
	public void CONCAT(int k){
/*		double max=baseline+errorbound;
		double min=baseline-errorbound;
		if(k==0){
			seg cursg=(seg) T.get(0);
//			System.out.println(cursg.p1+" "+cursg.p2);
			if((cursg.p1>max&&cursg.p2<min)||(cursg.p1<min&&cursg.p2>max)){
				int baseTime = (int)((baseline-cursg.p1)*(cursg.ts2-cursg.ts1)/(cursg.p2-cursg.p1)+cursg.ts1);
				seg temp=new seg(cursg.p1, cursg.ts1, baseline, baseTime);
				Seg_TS.add(temp);
				getVariation();
				seg temp1=new seg(baseline, baseTime, cursg.p2, cursg.ts2);
				Seg_TS.add(temp1);
			}else if((cursg.p1<min&&cursg.p2>=min)||(cursg.p1>max&&cursg.p2<=max)){
//				valid=true;
				bestpoint=cursg.p2;
				bestts=cursg.ts2;
				intersg.add(cursg);
			}else if((cursg.p1>=min&&cursg.p2<min)||(cursg.p1<=max&&cursg.p2>max)) {
				while(intersg.size()!=0 && intersg.get(0).ts1 != bestts){
					Seg_TS.add(intersg.get(0));
					intersg.remove(0);
				}
				getVariation();
				while(intersg.size()!=0){
					Seg_TS.add(intersg.get(0));
					intersg.remove(0);
				}
			}else{
				if(cursg.p1<=max&&cursg.p1>=min){
					intersg.add(cursg);
					if(Math.abs(cursg.p2-baseline)<Math.abs(bestpoint-baseline)){
						bestpoint=cursg.p2;
						bestts=cursg.ts2;
					}
				}else
					Seg_TS.add(cursg);
			}
		}else if(k==1){
			int i;
			int size=T.size();
			for(i=1; i<size; i++)
				Seg_TS.add(T.get(i));
		}else
			return;
	}
	public void getVariation(){
		int id=getDimension();
		int i;
		double maxpoint=((seg)Seg_TS.get(0)).p1;
		double minpoint=((seg)Seg_TS.get(0)).p1;
		
		int ts=((seg)Seg_TS.get(0)).ts1;
		int size=Seg_TS.size();
		for(i=0; i<size; i++){
			seg cur=(seg) Seg_TS.get(i);
			if(cur.p2>maxpoint)
				maxpoint=cur.p2;
			if(cur.p2<minpoint)
				minpoint=cur.p2;
		}
		int tspan=((seg)Seg_TS.get(size-1)).ts2-((seg)Seg_TS.get(0)).ts1;
		
		Data newNode = new Data(ts, maxpoint, minpoint, tspan, maxpoint-minpoint);
//		Cube.addnode(maxpoint, minpoint, tspan, maxpoint-minpoint, size, invert, timebase);
		Vscube.vsModel.addNode(id, newNode);
		Seg_TS.clear();*/
	}
	
	public int getDimension(){
		return 0;
	}
/*	public void TAKEOUT(){
//		System.out.println("before"+d.buffer.length());
		d.buffer.delete(0, (((seg)T.get(0)).ts2-timebase)*5);
//		System.out.println(d.buffer.charAt(0));
		timebase=((seg)T.get(0)).ts2;
//		System.out.println(d.buffer.length());
		T.clear();
	}*/
	public double mergeCost(seg segLeft, seg segRight){
		double gradient=(segLeft.p2-segLeft.p1)/(segLeft.ts2-segLeft.ts1);
		double cost=Math.abs(segRight.p2-(gradient*(segRight.ts2-segLeft.ts2)+segLeft.p2));
//		System.out.println(cost);
		return cost;
	}
	public int minMerge(){
		int i;
		double mincost=100;
		int minindex=0;
		int size=T.size();
		for(i=0; i<size-1; i++){
			seg segts=(seg)T.get(i);
			if(segts.mergecost<mincost){
				mincost=segts.mergecost;
				minindex=i;
			}
		}
		return minindex;
	}
}