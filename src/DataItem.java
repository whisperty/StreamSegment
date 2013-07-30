
public class DataItem {

	private double value;			//record the data value
	private int time;			//record the time when the data is read (time sequence)
	
	public DataItem(double value, int time)
	{
		this.value = value;
		this.time = time;
	}
	
	public DataItem(DataItem d)
	{
		value = d.value;
		time = d.time;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public int getTime()
	{
		return time;
	}
	
	public void setValue(double value)
	{
		this.value = value;
	}
	
	public void setTime(int time)
	{
		this.time = time;
	}
	
	public void setDataItem(DataItem d)
	{
		value = d.value;
		time = d.time;
	}
	
	public String toString()
	{
		return "[time=" + time+ ",value=" + value + "]"; 
	}
	
}