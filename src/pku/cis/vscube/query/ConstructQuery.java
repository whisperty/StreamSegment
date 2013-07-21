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
	        //获得所有子元素
	        
/*	        List<Element> list = rootEl.getChildren();
	        for (Element el : list) {
	            //获取name属性值
	            String name = el.getAttributeValue("variType");
	            System.out.print(name);
	            //获取子元素capacity文本值
	            String capacity = el.getChildText("capacity");
	            //获取子元素directories文本值
	            String directories = el.getChildText("directories");
	            String files = el.getChildText("files");
	            System.out.println("磁盘信息:");
	            System.out.println("分区盘符:" + name);
	            System.out.println("分区容量:" + capacity);
	            System.out.println("目录数:" + directories);
	            System.out.println("文件数:" + files);
	            System.out.println("-----------------------------------");
	        }*/
	    } catch (JDOMException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void processQuery(Element root){
		String name = root.getName();
		if(name == "bv"){
			int dimIndex = getDimIndex(root.getAttributeValue("dimIndex"));
			BasicVariation bv = new BasicVariation(dimIndex, root.getAttributeValue("variName"), Integer.parseInt(root.getAttributeValue("variType")));
			LinkedList<BasicVariation> bvColl = Vscube.vsModel.getBv(bv);
			int i;
			for(i=0; i<bvColl.size(); i++){
				(queryResult.get(dimIndex)).add(bvColl.get(i));
			}
		}else if(name == "VSSEQ"){
			
		}else if(name == "VSCON"){
			
		}else
			System.out.print("Wrong Operator");
	}
	
	private int getDimIndex(String dim){
		return Vscube.vsModel.dimDic.get(dim);
	}
}