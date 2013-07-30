package pku.cis.vscube.query;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import pku.cis.vscube.model.Vscube;

public class ConstructQuery{
	LinkedList<LinkedList> queryResult;
	final static double VSSEQEPS = 1e-8;
	
	public ConstructQuery(){
		initResult();
	}
	
	private void initResult(){
		int i;
		for(i=0; i<Vscube.vsModel.DimNum; i++){
			LinkedList<BasicVariation> dimResult = new LinkedList<BasicVariation>();
			queryResult.add(dimResult);
		}
	}
	public void readXMLContent(String fileName) {
	    SAXBuilder builder = new SAXBuilder();
	    try {
	        Document doc = builder.build(new File(fileName));
	        Element rootEl = doc.getRootElement();
	        processQuery(rootEl);
	        //���������Ԫ��
	        
/*	        List<Element> list = rootEl.getChildren();
	        for (Element el : list) {
	            //��ȡname����ֵ
	            String name = el.getAttributeValue("variType");
	            System.out.print(name);
	            //��ȡ��Ԫ��capacity�ı�ֵ
	            String capacity = el.getChildText("capacity");
	            //��ȡ��Ԫ��directories�ı�ֵ
	            String directories = el.getChildText("directories");
	            String files = el.getChildText("files");
	            System.out.println("������Ϣ:");
	            System.out.println("�����̷�:" + name);
	            System.out.println("��������:" + capacity);
	            System.out.println("Ŀ¼��:" + directories);
	            System.out.println("�ļ���:" + files);
	            System.out.println("-----------------------------------");
	        }*/
	    } catch (JDOMException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private LinkedList<BasicVariation>VSSEQ(LinkedList<BasicVariation> a, LinkedList<BasicVariation> b){
		LinkedList<BasicVariation> ans = new LinkedList<BasicVariation>();
		for(BasicVariation i: a)
			for(BasicVariation j: b){
				BasicVariation tmp = i.VSSEQ(j, VSSEQEPS);
				if(tmp.isLegal())
					ans.add(tmp);
			}
		
		return ans;
	}
	
	private LinkedList<BasicVariation>VSCON(LinkedList<BasicVariation> a, LinkedList<BasicVariation> b){
		LinkedList<BasicVariation> ans = new LinkedList<BasicVariation>();
		for(BasicVariation i: a)
			for(BasicVariation j: b){
				BasicVariation tmp = i.VSCON(j);
				if(tmp.isLegal())
					ans.add(tmp);				
			}
		
		return ans;
	}
	
	private LinkedList<BasicVariation> processQuery(Element root){
		String name = root.getName();
		LinkedList<BasicVariation> ans = new LinkedList<BasicVariation>();
		
		if(name == "bv"){
			int dimIndex = getDimIndex(root.getAttributeValue("dimIndex"));
			BasicVariation bv = new BasicVariation(dimIndex, root.getAttributeValue("variName"), Integer.parseInt(root.getAttributeValue("variType")));
			LinkedList<BasicVariation> bvColl = Vscube.vsModel.getBv(bv);
			ans = bvColl;//*****
			int i;
			for(i=0; i<bvColl.size(); i++){
				(queryResult.get(dimIndex)).add(bvColl.get(i));
			}
		}else if(name == "VSSEQ"){
			List<Element>children = root.getChildren();
			char f = 0;
			for (Element el : children) {
				LinkedList<BasicVariation> now = processQuery(el);
				if(f == 0)	
					ans = now;
				else
					ans = VSSEQ(ans, now); 
			}
		}else if(name == "VSCON"){
			List<Element>children = root.getChildren();
			char f = 0;
			for (Element el : children) {
				LinkedList<BasicVariation> now = processQuery(el);
				if(f == 0)	
					ans = now;
				else
					ans = VSCON(ans, now); 
			}			
		}else
			System.out.print("Wrong Operator");
		
		return ans;
	}
	
	private int getDimIndex(String dim){
		return Vscube.vsModel.dimDic.get(dim);
	}
}