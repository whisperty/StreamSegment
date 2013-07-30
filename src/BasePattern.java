import java.util.Vector;

public class BasePattern {

	private Vector<Segment> basePattern;
	private String name;
	private int type;		// 0 stands for user_not_defined, 1 stands for user defined
	
	public BasePattern(){
		this.basePattern = new Vector<Segment>();
		name = "";
		type = 0;
	}
	
	public BasePattern(Vector<Segment> bp, String name){
		this.basePattern = bp;
		this.name = name;
		this.type = 0;
	}
	
	public void setBP(Vector<Segment> bp){
		this.basePattern = bp;
	}
	
	public void setBP(Segment s){
		this.basePattern.add(s);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setType(int i){
		this.type = i;
	}
	
	public Vector<Segment> getBP(){
		return this.basePattern;
	} 
	
	public String getName(){
		return name;
	}
	
	public int getType(){
		return type;
	}
	
	public String toString(){
		return "[" + this.name + ";" 
				//+ this.basePattern.toString()
				+ ";type=" + this.type + "]"; 
	}
	
	public boolean compareSimilarity(BasePattern bp, double error){
		
		return false;
	}
}
