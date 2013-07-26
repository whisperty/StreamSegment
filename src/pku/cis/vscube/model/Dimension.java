package pku.cis.vscube.model;
import java.util.LinkedList;
import java.util.List;

import pku.cis.vscube.query.BasicVariation;


public class Dimension {
	static final double sameEps = 1e-8;
	static final double trendEps = 1e-8;
	List<Node> nodeList = new LinkedList<Node>();
	
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
	
	public void updateDim(int expireTime){
		while(nodeList.size() > 0){
			int num = nodeList.get(0).del(expireTime);
			if(nodeList.get(0).isEmpty())
				nodeList.remove(0);
			
			if(num <= 0)
				break;
		}		
	}
	
	public LinkedList<BasicVariation> getBv(BasicVariation bv){
		int i;
		LinkedList<BasicVariation> bvColl = new LinkedList<BasicVariation>();
		Node pre = new Node();
		for(i=0; i<nodeList.size(); i++){
			Node node = nodeList.get(i);
			int tmp = IsSameVari(node, bv);
			
			if(i > 0){
				Data a = pre.getLastData(), b = node.getFirstData();
				if(node.isSameVari(a, b, bv, trendEps)){
					BasicVariation bvResult = new BasicVariation(bv);
					bvResult.setTime(node.getMinTimestamp(), node.getMaxTimestamp());
					bvColl.add(bvResult);					
				}
			}
			
			if(tmp == 1){
				BasicVariation bvResult = new BasicVariation(bv);
				bvResult.setTime(node.getMinTimestamp(), node.getMaxTimestamp());
				bvColl.add(bvResult);
			}
			else
			if(tmp == 0){
				node.getBv(bvColl, bv, trendEps);
			}
			
			pre = node;
		}
		return bvColl;
	}
	
	public int IsSameVari(Node node, BasicVariation bv){
		//need edit
		if(node.isTrend()){
			Trend trend = (Trend)node;
			if(bv.isSameVari(trend.trend(trendEps)))
				return 1;
			return -1;
		}
		else
			if(node.isSame()){
				if(bv.isSameVari(0))
					return 1;
				return -1;
			}
		
		return 0;
	}
}
