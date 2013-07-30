
public class ChoosePoint {

	private DataItem item;
	private String choose;		//recording it is the start or end of a base pattern
								//4 tpyes: s0(the start of a user_choosed wavelet), 
								//s(the start of a base pattern), 
								//e(the end of base pattern),
								//e0(the end of a user_choosed wavelet)
								//when divide the base pattern, it can be "repeat" or "inversion"
	
	public ChoosePoint(DataItem item, String choose){
		this.item = item;
		this.choose = choose;
	}
	
	public void setDataItem(DataItem item){
		this.item = item;
	}
	
	public void setChoose(String choose){
		this.choose = choose;
	}
	
	public String getChoose(){
		return choose;
	}
	
	public DataItem getDataItem(){
		return item;
	}
	
	public String toString(){
		return "(" + item.toString() + "->" + choose + ")";
	}
	
}
