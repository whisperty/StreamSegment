//RealTimeChart .java  
import java.io.IOException;

import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.axis.ValueAxis;  
import org.jfree.chart.plot.XYPlot;  
import org.jfree.data.time.Millisecond;  
import org.jfree.data.time.TimeSeries;  
import org.jfree.data.time.TimeSeriesCollection;  
  
public class RealTimeChart extends ChartPanel implements Runnable  
{  
    private static TimeSeries timeSeries; 
    private static TimeSeries baseline;
    private static TimeSeries upBound;
    private static TimeSeries downBound;
    private static TimeSeries sourceStream;
    private long value=0;  
    
    //ty
    private InputBuffer d;
    private int sleeptime;
      
    public RealTimeChart(String chartContent,String title,String yaxisName)  
    {  
        super(createChart(chartContent,title,yaxisName)); 
        
        d = new InputBuffer();
        try {
			d.readData();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
      
    private static JFreeChart createChart(String chartContent,String title,String yaxisName){  
        //创建时序图对象  
        timeSeries = new TimeSeries(chartContent,Millisecond.class);
        baseline = new TimeSeries("basline", Millisecond.class);
        upBound = new TimeSeries("upBound", Millisecond.class);
        downBound = new TimeSeries("downBound", Millisecond.class);
        sourceStream = new TimeSeries("sourceStream", Millisecond.class);
        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
        timeseriescollection.addSeries(timeSeries);
        timeseriescollection.addSeries(baseline);
        timeseriescollection.addSeries(upBound);
        timeseriescollection.addSeries(downBound);
        timeseriescollection.addSeries(sourceStream);
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(title,"时间(秒)",yaxisName,timeseriescollection,true,true,false);  
        XYPlot xyplot = jfreechart.getXYPlot();  
        //纵坐标设定  
        ValueAxis valueaxis = xyplot.getDomainAxis();  
        //自动设置数据轴数据范围  
        valueaxis.setAutoRange(true);  
        //数据轴固定数据范围 30s  
        valueaxis.setFixedAutoRange(30000D);  
  
        valueaxis = xyplot.getRangeAxis();  
        //valueaxis.setRange(0.0D,200D);  
  
        return jfreechart;  
      }  
  
    public void run()  
    {  
        while(true)  
        {  
        try  
        {  
            timeSeries.add(new Millisecond(), randomNum());
            baseline.add(new Millisecond(), 0.94);
            upBound.add(new Millisecond(), 0.96);
            downBound.add(new Millisecond(), 0.92);
            sourceStream.add(new Millisecond(), getSource());
            Thread.sleep(200);  
            //Thread.sleep(200);
        }  
        catch (InterruptedException e)  {   }  
        }         
    }  
    
    private float getSource()
    {
    	float num;
    	if(d.endStream)
    		return 0;
    	if(d.sourceStream.isEmpty()){
    			try {
					d.readToBuffer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	}
    	num = d.sourceStream.poll();
    	//System.out.println(num);
    	return num;
    }
      
    private double randomNum()  
    {     
        //System.out.println((Math.random()*20+80));        
        //return (long)(Math.random()*20+80); 
    	double num;
    	if(d.endStream)
    		return 0;
    	if(d.datapoints.isEmpty()){
    		return 0;
    	}
    	num = d.datapoints.poll();
    	//num = d.datapoints.poll();
    	//System.out.println(num);
    	return num;
    }  
} 