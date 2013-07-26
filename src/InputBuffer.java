import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

import java.util.Queue;
import java.util.LinkedList;

public class InputBuffer {
	InputStream is;
	BufferedReader reader;
	StringBuffer buffer;
	String line;
	boolean endStream;
	
	LinkedList<seg> datapoints;
	LinkedList<Float> sourceStream;
	swab divideDimen;
    /**
     * 1. ��ʾ�����е��ı�����һ�� StringBuffer ��
     * @throws IOException
     */
	public InputBuffer(){
		datapoints = new LinkedList<seg>();
		divideDimen = new swab();
	}
    public void readToBuffer()
        throws IOException {
//        String line = reader.readLine();       // ��ȡ��һ��
        while (line != null) {          // ��� line Ϊ��˵��������
            buffer.append(line);        // ��������������ӵ� buffer ��
            buffer.append(" ");        // ��ӻ��з�
//            System.out.println(line);
            if(buffer.length()/5>100) break;
            line = reader.readLine();   // ��ȡ��һ��
        }
 //       System.out.println(buffer.length());
        String temp = buffer.toString();
        String[] temp1 = temp.split(" ");
        for(int i=0; i<temp1.length; i++)
        {
        	sourceStream.offer(Float.parseFloat(temp1[i]));
        }
        buffer.setLength(0);
        divideDimen.seg_ts(temp1);
        for(int i=0; i<divideDimen.outputPoints.size(); i++){
        	datapoints.offer(divideDimen.outputPoints.poll());
        }
       
        if(line==null){
        	endStream=true;
        	is.close();
        }
    }
    /**
     * 2. ��ʾ�� StringBuffer �е����ݶ���������
     */
    public void writeFromBuffer(StringBuffer buffer, OutputStream os) {
        // �� PrintStream ���Է���İ�����������������
        // �������÷��� System.out һ��
        // ��System.out ������� PrintStream ����
        PrintStream ps = new PrintStream(os);   
        ps.print(buffer.toString());
    }
    /**
     * 3*. ���������п������ݵ���������
     * @throws IOException
     */
    public void copyStream(InputStream is, OutputStream os) throws IOException {
        // ����������̿��Բ��� readToBuffer �е�ע��
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));
        line = reader.readLine();
        while (line != null) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.flush();     // ���ȷ��Ҫ��������еĶ�����д��ȥ��
                            // ���ﲻ�ر� writer ����Ϊ os �Ǵ����洫������
                            // ��Ȼ���Ǵ�����򿪵ģ�Ҳ�Ͳ�������ر�
                            // ����رյ� writer����װ������� os Ҳ�ͱ�����
    }
    /**
     * 3. ���� copyStream(InputStream, OutputStream) ���������ı��ļ�
     */
    public void copyTextFile(String inFilename, String outFilename)
        throws IOException {
        // �ȸ�������/����ļ�������Ӧ������/�����
        InputStream is = new FileInputStream(inFilename);
        OutputStream os = new FileOutputStream(outFilename);
        copyStream(is, os);     // �� copyStream ��������
        is.close(); // is ��������򿪵ģ�������Ҫ�ر�
        os.close(); // os ��������򿪵ģ�������Ҫ�ر�
    }
    public void readData() throws IOException {
        int sw = 1;     // ���ֲ��Ե�ѡ�񿪹�
        
        switch (sw) {
        case 1: // ���Զ�
        {
            //is = new FileInputStream("F:\\javaproject\\DimenDivide\\TBI_ECG.txt");
            is = new FileInputStream("C:\\Users\\test\\Desktop\\TBI_ECG.txt");
            
            buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(is));
            endStream=false;
            line = reader.readLine();
            readToBuffer();
//            System.out.println(buffer);     // ������ buffer �е�����д����
//            System.out.println(buffer.capacity());
//            is.close();
            break;
        }
        case 2: // ����д
        {
            StringBuffer buffer = new StringBuffer("Only a test\n");
            writeFromBuffer(buffer, System.out);
            break;
        }
        case 3: // ���Կ���
        {
            copyTextFile("E:\\test.txt", "E:\\r.txt");
        }
            break;
        }
    }
}

