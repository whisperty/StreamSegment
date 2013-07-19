package pku.cis.vscube.model;
import java.util.LinkedList;
import java.util.List;


public class Dimension {
	static final double sameEps = 1e-8;
	static final double trendEps = 1e-8;
	List<Node>nodeList = new LinkedList();
	
	public void addNode(Data a){
		if(nodeList.isEmpty()){
			nodeList.add(new Node(a));
			return;
		}
		
		int size = nodeList.size();
		Node last = nodeList.get(size - 1);
		
		if(last.isBasic()){
			if(last.equals(a, sameEps)){
				Same ts = new Same();
				ts.add(last.getLastData());
				ts.add(a);
				nodeList.remove(size - 1);
				nodeList.add(ts);
				return;
			}
			
			if(size <= 1){
				nodeList.add(new Node(a));
				return;
			}
			
			Node last2 = nodeList.get(size - 2);
			if(last2.isBasic() && last2.trend(last.getLastData(), trendEps) == last.trend(a, trendEps)){
				Trend tt = new Trend();
				tt.add(last.getLastData());
				tt.add(last2.getLastData());
				tt.add(a);
				nodeList.remove(size - 1);
				nodeList.remove(size - 2);
				nodeList.add(tt);
				return;
			}
		}
		
		if(last.isSame()){
			Same ts = (Same)last;
			if(last.equals(a, sameEps)){
				ts.add(a);
				return;
			}
		}
		
		if(last.isTrend()){
			Trend tt = (Trend)last;
			if(tt.trend(trendEps) == last.trend(a, trendEps)){
				tt.add(a);
				return;
			}
		}
		
		nodeList.add(new Node(a));
	}
}