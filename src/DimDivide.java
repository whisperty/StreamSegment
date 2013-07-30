import java.awt.Color;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;



public class DimDivide {
	
	private final static int DEFAULT_RADIO = 5;	//1;	//5; 
	
	public static DataItem PLR_SWAB(List<DataItem> original, double max_error , int seg_num)
	/*
	 * approximate the data£¬the array original save raw data£¬
	 * if exceed max_error, the result should be considered unacceptable£¬
	 * seg_num represents segments number
	 */
	{
		LinkedList<DataItem> linear = new LinkedList<DataItem>();
		
		if(seg_num == 0)
			return original.get(1);
		
		// copy original to linear, then subsection and merge linear
		for(int i=0; i<original.size(); i++)
			linear.add(original.get(i));
		
		if(linear.size() >= 3)
		{
			do
			{
				DataItem from = linear.getFirst();			//the start point of segment
				DataItem to;								//the end point of segment
				//current segment is a line which starts with "from" ,ends with "to" 
				int startIndex = 0, endIndex = startIndex;			
				double [] error = new double [linear.size() - 2];
				// error[i] --> error of linear[i] ~ linear[i+2]
				
				for(int i=2; i<linear.size(); i++)		
					//Starting from the finest possible approximation
				{
					to = linear.get(i);
					LinkedList< Double > distances = new LinkedList< Double >();
					
					while (endIndex < original.size() && 
							original.get(endIndex).getTime() <= to.getTime())
					{
						//System.out.println("endIndex="+endIndex);
						DataItem temp = original.get( endIndex );
						double temp_dis = calculateDistance(from, to, temp);
						distances.add(temp_dis);
						endIndex ++;
					}	
					error[i - 2] = get_s(distances);
					//System.out.println("error="+error[i - 2]);
					from = linear.get(i-1);
					startIndex = original.indexOf(from);
					endIndex = startIndex;
					
					//int exchange = startIndex;
					//startIndex = endIndex;					
					//endIndex = exchange + 1;
				}
				
				int minIndex = getMinIndex(error); 
				if (error[minIndex]> max_error)		//if exceed, unacceptable
					break;
				else
				linear.remove(minIndex + 1); // merge				
			}while(linear.size() > seg_num);			
		}	
		return linear.get(1);  //get(0) is fixed
		
	}

	private static int getMinIndex(double [] errors)			//find the index of lowest cost pairs	
	{
		int minIndex = 0;
		for (int i = 0 ; i < errors.length ; i ++)
		{
			if (errors[i] < errors[minIndex])
			{
				minIndex = i;
			}

		}
		//System.out.println("minIndex="+minIndex);
		return minIndex;
	}

	//calculate the distance between from and to for point "temp"
	private static double calculateDistance(DataItem from, DataItem to, DataItem temp) 
	{
		double k = (to.getValue() - from.getValue()) / (to.getTime() - from.getTime());
		return temp.getValue() - from.getValue() - (temp.getTime() - from.getTime())*k;
	}

	private static double get_s(LinkedList< Double > distances)
	{
		double mean = 0,count = distances.size();
		for ( int i = 0 ; i < count ; i ++ )
		{
			mean += distances.get( i );
		}
		mean = mean / count;
		double s = 0;
		for ( int i = 0 ; i < count ; i ++ )
		{
			double d = distances.get( i );
			s += ( d - mean ) * ( d - mean );
		}
		s = s / count;
		return Math.sqrt( s );
	}
	
	//use user choose point "middle" to extend line to approximate points 
	public static ChoosePoint extendLine(int i, LinkedList<DataItem> link, 
			double middle, String choose, double error){
		int j;
		DataItem from, to;
		boolean flag = false;
		ChoosePoint point = null;
		
		if(link.getLast().getTime() <= middle){
			point = new ChoosePoint(link.getLast(), choose);
			return point;
		}

		for(j=i; j<link.size() && link.get(j).getTime()<middle; j++);
		if(j < link.size()){
			if(j == 0){
				to = link.get(j);
				point = new ChoosePoint(to, choose);
				return point;
			}
			else if(j == link.size()-1){
				to = link.get(link.size()-1);
				point = new ChoosePoint(to, choose);
				return point;
			}
			from = link.get(j-1);
			to = link.get(j);
				flag = exceedErrorBound(from, to, middle, error);
			if(flag == false)
				point = new ChoosePoint(to, choose);
			else 
				point = new ChoosePoint(from, choose);		
		}
		return point;
	}
	
	public static boolean exceedErrorBound(DataItem from, 
			DataItem to, double middle, double error){
		double k;
		if(from.getValue() >= to.getValue())
			k = (to.getTime() - middle) / (to.getTime() - from.getTime());
		else
			k = 1 - ((from.getTime() - middle) / (from.getTime() - to.getTime()));
		if(k <= error)		return false;
		else				return true;
	}
	
	//compare the similarity between two base patterns, if similar, record the index
	public static LinkedList<IndexInfo> compareSimilarity(BasePattern bps, BasePattern bpb, double error){
		Vector<Segment> small, big;
		int i, j;
		LinkedList<IndexInfo> index;
		small = bps.getBP();
		big = bpb.getBP();
		int l1 = small.size();
		int l2 = big.size();
		double d1, d2, tempa, tempk, temps, tempr;

		index = new LinkedList<IndexInfo>();
		tempa = 0;		tempk = 0;		temps = 0;		tempr = 0;
		for(i = 0; i < l2-l1+1;){
			tempa = 0;		tempk = 0;		temps = 0;		tempr = 0;
			for(j = 0; j < l1; j++){
				//compare value
				//d1 = big.get(j).getFrom().getValue();
				d1 = big.get(i+j).getFrom().getValue();
				d2 = small.get(j).getFrom().getValue();
				//System.out.println("d1="+d1+",d2="+d2);
				tempa += calculateDif(d1, d2);
				//d1 = big.get(j).getK();
				d1 = big.get(i+j).getK();
				d2 = small.get(j).getK();
				tempk += calculateDif(d1, d2);
				tempr += calculateDif_inversion(d1, d2);
				//d1 = big.get(j).getTimeSpan();
				d1 = big.get(i+j).getTimeSpan();
				d2 = small.get(j).getTimeSpan();
				temps += calculateDif(d1, d2);	
			}
			tempa = tempa / l1;
			tempk = tempk / l1;
			tempr = tempr / l1;
			temps = temps / l1;

			if(tempk < error && temps < error && tempa < error / DEFAULT_RADIO){
				//System.out.println("k="+tempk+",s="+temps+",a="+tempa);
				index.add(new IndexInfo(i, 0));
				i = i + l1;
				if(i < l2-l1+1)
					continue;
				else 
					break;
			}
			 //inversion
			else if(tempr < error && temps < error && tempa < error / DEFAULT_RADIO){
				index.add(new IndexInfo(i, 1));
				i = i + l1;
				if(i < l2-l1+1)
					continue;
				else 
					break;
			}
			i++;
		}
		return index;
	}
	
	private static double calculateDif(double d1, double d2){
		double temp;
		if(d1 > d2)
			temp = (d1 - d2) / d1;
		else 
			temp = (d1 - d2) / d2;
		temp = Math.abs(temp);
		return temp;
	}
	
	private static double calculateDif_inversion(double d1, double d2){
		double temp;
		if(d1 > d2)
			temp = (d1 + d2) / d1;
		else
			temp = (d1 + d2) / d2;
		temp = Math.abs(temp);
		return temp;
	}
	
	
	public static void changeDataItemToSegment(LinkedList<DataItem> link,
			LinkedList<Segment> window){
		DataItem from, to;
		to = link.get(0);
		for(int k=1; k < link.size(); k++){
			from = to;
			to = link.get(k);
			window.add(new Segment(from, to));
		}
	}

}

