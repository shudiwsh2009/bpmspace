package com.chinamobile.bpmspace.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class MXMLWriterUtil {
	public void writeXMLFile(String xmlfile) { 
        File file = new File("E:/tryfile/xml"); 
        file.listFiles(); 
        if(!file.exists()) { 
             
            file.mkdirs(); 
        } 
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
        DocumentBuilder dbuilder = null; 
        try { 
            dbuilder = dbf.newDocumentBuilder(); 
        }catch(Exception ex) { 
            ex.printStackTrace(); 
        } 
         
        Document doc = dbuilder.newDocument(); 
         
        Element root = doc.createElement("menu"); 
        doc.appendChild(root); 
         
        Element stu = doc.createElement("student"); 
        stu.setAttribute("gender","male"); 
        root.appendChild(stu); 
         
        Element stu_name = doc.createElement("name"); 
        stu.appendChild(stu_name); 
        Text name_text = doc.createTextNode("AAA"); 
        stu_name.appendChild(name_text); 
         
        Element stu_age = doc.createElement("age"); 
        stu.appendChild(stu_age); 
        Text age_text = doc.createTextNode("25"); 
        stu_age.appendChild(age_text); 
        try { 
            FileOutputStream fos = new FileOutputStream(xmlfile+"/test.xml"); 
            OutputStreamWriter osw = new OutputStreamWriter(fos); 
            callDomWriter(doc, osw, "UTF-8"); 
            osw.close(); 
            fos.close(); 
        }catch(Exception ex) { 
            ex.printStackTrace(); 
        } 
         
         
    } 
     
    public void callDomWriter(Document dom , Writer writer, String encoding) { 
        try { 
             
            Source source = new DOMSource(dom); 
            Result res = new StreamResult(writer); 
            Transformer xformer = TransformerFactory.newInstance().newTransformer(); 
            xformer.setOutputProperty(OutputKeys.ENCODING, encoding);
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, res); 
        }catch (TransformerConfigurationException e) { 
               e.printStackTrace(); 
          } catch (TransformerException e) { 
           e.printStackTrace(); 
          } 
         
    }
}
