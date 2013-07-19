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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
      
    private static JFreeChart createChart(String chartContent,String title,String yaxisName){  
        //����ʱ��ͼ����  
        timeSeries = new TimeSeries(chartContent,Millisecond.class);
        baseline = new TimeSeries("basline", Millisecond.class);
        upBound = new TimeSeries("upBound", Millisecond.class);
        downBound = new TimeSeries("downBound", Millisecond.class);
        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
        timeseriescollection.addSeries(timeSeries);
        timeseriescollection.addSeries(baseline);
        timeseriescollection.addSeries(upBound);
        timeseriescollection.addSeries(downBound);
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(title,"ʱ��(��)",yaxisName,timeseriescollection,true,true,false);  
        XYPlot xyplot = jfreechart.getXYPlot();  
        //�������趨  
        ValueAxis valueaxis = xyplot.getDomainAxis();  
        //�Զ��������������ݷ�Χ  
        valueaxis.setAutoRange(true);  
        //������̶����ݷ�Χ 30s  
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
            Thread.sleep(200*sleeptime);  
            //Thread.sleep(200);
        }  
        catch (InterruptedException e)  {   }  
        }         
    }  
      
    private float randomNum()  
    {     
        //System.out.println((Math.random()*20+80));        
        //return (long)(Math.random()*20+80); 
    	float num;
    	if(d.endStream)
    		return 0;
    	if(d.datapoints.isEmpty()){
    		try {
				d.readToBuffer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	sleeptime=d.datapoints.getFirst().ts2-d.datapoints.getFirst().ts1;
    	num = (float) d.datapoints.poll().p1;
    	//num = d.datapoints.poll();
    	System.out.println(num);
    	return num;
    }  
} 