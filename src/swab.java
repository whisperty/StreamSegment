import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.lang.Math;
import pku.cis.vscube.model.*;
import java.util.Map;

public class swab{
	InputBuffer d;
	public LinkedList<seg> Seg_TS, T, intersg;
	int timebase;
	double baseline=0.95, errorbound=0.022;
	int index;
	char[] diname={'P', 'Q', 'R', 'S', 'T'};
	int dimNum = 5;
	int curdimIndex = 0;
	double diffRatio = 0.99;
	double bestpoint, bestts;
	
	static double MAXCOST = 0.035;
	Queue<seg> outputPoints;
	
	LinkedList<Point>dict[] = new LinkedList[6];
	double timeSpan[] = new double[6];
	
	public swab(){
//		Cube=new vscube();
		index=0;
//		valid=false;
		timebase=0;
//		intersg=new ArrayList<seg>();
		outputPoints = new LinkedList<seg>();
		T=new LinkedList<seg>();
		intersg = new LinkedList<seg>();
		Seg_TS = new LinkedList<seg>();
		//采集点及时间跨度
		//char[] diname={'P', 'Q', 'R', 'S', 'T', 'U'};
		for(int i = 0; i < 6; i++){
			dict[i] = new LinkedList<Point>();
			timeSpan[i] = 1;
		}
		
		//P
/*		dict[0].add(new Point(39 , 0.98));
		dict[0].add(new Point(45 , 1));
		dict[0].add(new Point(49 , 1.05));
		dict[0].add(new Point(57 , 1));
		dict[0].add(new Point(58 , 0.98));*/
		dict[0].add(new Point(17, 0.95));
		dict[0].add(new Point(35, 0.95));
		dict[0].add(new Point(49, 1.05));
		dict[0].add(new Point(55, 1.02));
		dict[0].add(new Point(60, 0.96));
		
		//Q
/*		dict[1].add(new Point(58 , 0.98));
		dict[1].add(new Point(73 , 0.95));
		dict[1].add(new Point(77 , 0.91));
		dict[1].add(new Point(79 , 0.96));*/
		dict[1].add(new Point(60, 0.96));
		dict[1].add(new Point(77, 0.91));
		dict[1].add(new Point(79, 0.96));
		
		//R
/*		dict[2].add(new Point(79 , 0.96));
		dict[2].add(new Point(83 , 1.12));
		dict[2].add(new Point(85 , 1.09));
		dict[2].add(new Point(87 , 0.95));*/
		dict[2].add(new Point(79, 0.96));
		dict[2].add(new Point(83, 1.12));
		dict[2].add(new Point(85, 1.09));
		dict[2].add(new Point(87, 0.95));
		
		//S
/*		dict[3].add(new Point(87 , 0.95));
		dict[3].add(new Point(89 , 0.76));
		dict[3].add(new Point(91 , 0.7));
		dict[3].add(new Point(100 , 0.95));
		dict[3].add(new Point(113 , 0.98));*/
		dict[3].add(new Point(87, 0.95));
		dict[3].add(new Point(89, 0.76));
		dict[3].add(new Point(91, 0.7));
		dict[3].add(new Point(98, 0.93));
		dict[3].add(new Point(100, 0.95));
		
		//T
/*		dict[4].add(new Point(113 , 0.98));
		dict[4].add(new Point(134 , 1));
		dict[4].add(new Point(141 , 1.07));
		dict[4].add(new Point(148 , 1.07));
		dict[4].add(new Point(158 , 0.99));*/
//		dict[4].add(new Point(98, 0.93));
		dict[4].add(new Point(100, 0.95));
		dict[4].add(new Point(134, 0.99));
		dict[4].add(new Point(143, 1.08));
		dict[4].add(new Point(150, 1.07));
		dict[4].add(new Point(162, 0.97));
		dict[4].add(new Point(173, 0.93));
		dict[4].add(new Point(186, 0.95));
		
		//U
/*		dict[5].add(new Point(158 , 0.99));
		dict[5].add(new Point(170 , 0.93));
		dict[5].add(new Point(178 , 0.94));
		dict[5].add(new Point(183 , 0.94));
		dict[5].add(new Point(187 , 0.97));*/
		
		for(int i = 0; i < dimNum; i++){
			double sx = dict[i].getFirst().x;
			timeSpan[i] = dict[i].getLast().x - sx;
			
			double minValue = dict[i].getFirst().y;
			for(Point p: dict[i]){
				if(p.y < minValue)
					minValue = p.y;
				p.x -= sx;
			}
			for(Point p: dict[i]){
				p.y -= minValue;
			}
			//for(Point p: dict[i])
			//	System.out.println("[" + p.x + "," + p.y + "]");
			//System.out.println("");
		}
	}
	public void seg_ts(String[] dataArray){
		long startTime = System.nanoTime();
		Bottom_up(dataArray);
		
		CONCAT(0);
		
		while(T.size()>1)
			T.removeFirst();
		timebase=T.getLast().ts2+1;
		long consumingTime = System.nanoTime() - startTime;
	}
	
	public void resetOutput(){
		outputPoints.clear();
	}
	
	public void Bottom_up(String[] dataArray){
		int i, k;
		int mergeSize=dataArray.length-1;
//		System.out.println(stream.length);
		if(T.size()!=0){
			T.add(new seg(T.getLast().p2, T.getLast().ts2,Double.parseDouble(dataArray[0]), timebase));
		}
		for(i=0; i<mergeSize; i++){
			seg segts=new seg();
			segts.p1=Double.parseDouble(dataArray[i]);
			segts.p2=Double.parseDouble(dataArray[i+1]);
			segts.ts1=timebase+i;
			segts.ts2=timebase+i+1;
//			segts.valid=true;
			if(T.size()!=0)
				T.getLast().mergecost=mergeCost(T.getLast(), segts);
			T.add(segts);
//			System.out.print(segts.p2+" ");
		}
		T.getLast().mergecost=MAXCOST;
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
//			T.get(i).print();
		}
		
	}
	
	public void CONCAT(int k){
		double max=baseline+errorbound;
		double min=baseline-errorbound;
		if(k==0){
			int i;
			for(i=0; i<T.size()-1; i++){
				seg cursg = T.get(i);
//			System.out.println(cursg.p1+" "+cursg.p2);
				if((cursg.p1>max&&cursg.p2<min)||(cursg.p1<min&&cursg.p2>max)){
					int baseTime = (int)((baseline-cursg.p1)*(cursg.ts2-cursg.ts1)/(cursg.p2-cursg.p1)+cursg.ts1);
					seg temp=new seg(cursg.p1, cursg.ts1, baseline, baseTime);
					Seg_TS.add(temp);
					getVariation(Seg_TS);
					seg temp1=new seg(baseline, baseTime, cursg.p2, cursg.ts2);
					Seg_TS.add(temp1);
				}else if((cursg.p1<min&&cursg.p2>=min)||(cursg.p1>max&&cursg.p2<=max)){
//					valid=true;
					bestpoint=cursg.p2;
					bestts=cursg.ts2;
					intersg.add(cursg);
				}else if((cursg.p1>=min&&cursg.p2<min)||(cursg.p1<=max&&cursg.p2>max)) {
					while(intersg.size()!=0 && intersg.get(0).ts1 != bestts){
						Seg_TS.add(intersg.get(0));
						intersg.remove(0);
					}
					getVariation(Seg_TS);
					while(intersg.size()!=0){
						Seg_TS.add(intersg.get(0));
						intersg.remove(0);
					}
					Seg_TS.add(cursg);
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
			}
			
		}else if(k==1){
			int i;
			int size=T.size();
			for(i=1; i<size; i++)
				Seg_TS.add(T.get(i));
		}else
			return;
	}
	
	public void getVariation(LinkedList<seg> Seg_TS){
//		System.out.println("getVariation");
		int id=getDimension(Seg_TS);
		System.out.println(id);
		int i;
		double maxpoint=((seg)Seg_TS.get(0)).p1;
		double minpoint=((seg)Seg_TS.get(0)).p1;
		
		int ts=((seg)Seg_TS.get(0)).ts1;
		
		//give up the first set of segments
		if(ts == 0){
			Seg_TS.clear();
			return;
		}
			
		
		int size=Seg_TS.size();
		for(i=0; i<size; i++){
			seg cur=(seg) Seg_TS.get(i);
			if(cur.p2>maxpoint)
				maxpoint=cur.p2;
			if(cur.p2<minpoint)
				minpoint=cur.p2;
		}
		int tspan=((seg)Seg_TS.getLast()).ts2-((seg)Seg_TS.getFirst()).ts1;
		
		Data newNode = new Data(ts, maxpoint, minpoint, tspan, maxpoint-minpoint);
//		System.out.println("add to cube");
		Vscube.vsModel.addNode(id, newNode);
		Seg_TS.clear();
//		System.out.println(Seg_TS.size());
	}
	
	//the segments are stored in the variable seg_TS
	//creat a dictionary which stores the info of all the dimensions
	//then compare the segments with the dimension in the dic
	public int getDimension(LinkedList<seg> Seg_TS){
//		for(seg s: Seg_TS)
//			s.print();

		//	char[] diname={'P', 'Q', 'R', 'S', 'T', 'U'};
		double [] diff = new double[dimNum];
		double sx = Seg_TS.getFirst().ts1;
		double ts = Seg_TS.getLast().ts2 - sx;
		double minValue = Seg_TS.getFirst().p1;
		for(seg s: Seg_TS){
			if(s.p2 < minValue)
				minValue = s.p2;
		}
		for(int i = 0; i < dimNum; i++){
			diff[i] = 0;
			for(Point p: dict[i]){
				double x = 0, y = 0;
				x = p.x/timeSpan[i] * ts + sx;
				
				for(seg s: Seg_TS)
					if(s.isIn(x)){
						y = s.calY(x) - minValue;
						break;
					}
				
				diff[i] += Math.abs(y - p.y);
			}
			
			//diff[i] = diffRatio * Math.pow(diff[i], 1/(double)2) + (1-diffRatio)* Math.abs(ts - timeSpan[i]);
			
			diff[i] = diffRatio * diff[i]/dict[i].size();
			if((curdimIndex+1)%5  == i)
				diff[i] *= 0.5;
		}
		
		int ans = 0;
		double minDiff = diff[0];
		for(int i = 1; i < dimNum; i++)
			if(minDiff > diff[i]){
				minDiff = diff[i];
				ans = i;
			}
		curdimIndex = ans;
/*		if(Seg_TS.getFirst().ts1 == 223){
			for(int i =0; i< dimNum; i++)
				System.out.println(diff[i]);
		}*/
		return ans;
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