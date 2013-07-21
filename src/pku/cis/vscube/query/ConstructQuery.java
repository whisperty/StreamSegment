package pku.cis.vscube.query;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class ConstructQuery{
	public void readXMLContent() {
	    SAXBuilder builder = new SAXBuilder();
	    try {
	        Document doc = builder.build(new File("file/vsQuery.xml"));
	        Element rootEl = doc.getRootElement();
	        //���������Ԫ��
	        List<Element> list = rootEl.getChildren();
	        for (Element el : list) {
	            //��ȡname����ֵ
	            String name = el.getAttributeValue("name");
	            System.out.print(name);
/*	            //��ȡ��Ԫ��capacity�ı�ֵ
	            String capacity = el.getChildText("capacity");
	            //��ȡ��Ԫ��directories�ı�ֵ
	            String directories = el.getChildText("directories");
	            String files = el.getChildText("files");
	            System.out.println("������Ϣ:");
	            System.out.println("�����̷�:" + name);
	            System.out.println("��������:" + capacity);
	            System.out.println("Ŀ¼��:" + directories);
	            System.out.println("�ļ���:" + files);
	            System.out.println("-----------------------------------");*/
	        }
	    } catch (JDOMException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}