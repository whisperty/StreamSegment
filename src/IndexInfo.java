
public class IndexInfo{
	private int index;
	private int type;		//0 represents repeat, 1 stands for inversion
	
	public IndexInfo(int index, int type){
		this.index = index;
		this.type = type;
	}
	
	public int getIndex(){
		return index;
	}
	
	public int getType(){
		return type;
	}
	
	public String toStirng(){
		return "[" + index + "," + type + "]";
	}
}