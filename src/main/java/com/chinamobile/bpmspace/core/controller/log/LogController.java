package com.chinamobile.bpmspace.core.controller.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.chinamobile.bpmspace.core.domain.log.Activity;
import com.chinamobile.bpmspace.core.domain.log.Case;
import com.chinamobile.bpmspace.core.domain.log.Log;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.InstanceRepository;
import com.chinamobile.bpmspace.core.repository.LengthIndexRepository;
import com.chinamobile.bpmspace.core.repository.LogRepository;
import com.chinamobile.bpmspace.core.util.FileUtil;

@Controller
@RequestMapping("log")
public class LogController {
	@RequestMapping(value = "log2mxml", method = RequestMethod.POST)
	@ResponseBody
	public void log2mxml(@RequestParam("logId") String _logID,
			HttpSession session,
			HttpServletResponse response) {
		LogRepository lr = new LogRepository();
		InstanceRepository ir = new InstanceRepository();
		JSONObject result = new JSONObject();
		
		try {
			Log log = lr.getLogByLogId(_logID);
			List<Case> instances = ir.getInstancesOfLog(_logID);

			String realPrePath = FileUtil.WEBAPP_ROOT + "pmfiles"
					+ File.separator
					+ session.getAttribute("userId").toString();
			String inputPrePath = realPrePath + File.separator + "logs";
			String outputPrePath = realPrePath + File.separator + "models";
			
			 
	         
	        String fileNameWithoutSuffix = UUID.randomUUID().toString();
	        String outfile = inputPrePath + File.separator
					+ fileNameWithoutSuffix + ".mxml";
	        
	        this.writeMXMLFile(inputPrePath, fileNameWithoutSuffix, log, instances); 
	        System.out.println("finished");
	       
			result.put("inputPrePath", inputPrePath + File.separator
					+ fileNameWithoutSuffix + ".mxml");
			result.put("outputPrePath", outputPrePath + File.separator
					+ fileNameWithoutSuffix);

		} catch (BasicException e) {
			// TODO: handle exception
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "name_check", method = RequestMethod.GET)
	@ResponseBody
	public void checkName(@RequestParam("tmpName") String _logName,
			@RequestParam("catalogId") String _catalogId, HttpSession session,
			HttpServletResponse response) {
		LogRepository lr = new LogRepository();
		JSONObject result = new JSONObject();
		try {
			List<Log> logs = lr.findLogByCatalogId(_catalogId);
			for (Log log : logs) {
				if (log.getName().equals(_logName)) {
					result.put("state", "FAILED");
					break;
				}
			}

		} catch (BasicException e) {
			// TODO: handle exception
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "instance_load", method = RequestMethod.GET)
	@ResponseBody
	public void showAddedLog(@RequestParam("catalogId") String _catalogId,
			HttpServletResponse response) {
		LogRepository lr = new LogRepository();
		JSONArray result = new JSONArray();
		try {
			List<Log> logs = lr.findLogByCatalogId(_catalogId);
			for (Log l : logs) {
				JSONObject o = new JSONObject();
				o.put("title", l.getName());
				o.put("key", l.getId());
				o.put("isLazy", false);
				o.put("expand", false);
				o.put("isFolder", false);
				result.put(o);
			}
		} catch (BasicException e) {

		}
		// send response
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "log_delete", method = RequestMethod.POST)
	@ResponseBody
	public void deleteLog(@RequestParam("logId") String _logId,
			HttpSession session, HttpServletResponse response) {
		LogRepository lr = new LogRepository();
		LengthIndexRepository lir = new LengthIndexRepository();
		JSONObject result = new JSONObject();
		try {
			lir.removeLogInLengthIndex(_logId);
			lir.removeLogInDurationIndex(_logId);
			lr.deleteLog(_logId);
			result.put("state", "SUCCESS");
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", "FAILED");
		}
		// send response
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeMXMLFile(String filepath, String fileName, Log log, List<Case> instances) { 
        File file = new File(filepath); 
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
        
        Element root = doc.createElement("WorkflowLog"); 
        root.setAttribute("description", "Test log for decision miner");        
        doc.appendChild(root); 
        
        Element source = doc.createElement("Source"); 
        source.setAttribute("program", "LogGenerator, Tsinghua, ISE");
        root.appendChild(source);
        
        Element data = doc.createElement("Data"); 
        source.appendChild(data);
        
        Element attr = doc.createElement("Attribute"); 
        attr.setAttribute("name", "info");
        Text attr_text = doc.createTextNode("LogGenerator, Tsinghua, ISE"); 
        attr.appendChild(attr_text);
        data.appendChild(attr);
        
        Element process = doc.createElement("Process"); 
        process.setAttribute("id",log.getId()); 
        root.appendChild(process);
        String pId, nId;
        pId = instances.get(0).getId();
        
        
        
        for (Case i: instances) {
        	Element processInstance = doc.createElement("ProcessInstance"); 
	        processInstance.setAttribute("id",i.getId()); 
	        process.appendChild(processInstance);
      
        	for (Activity as: i.getActivities()) {
        		Element auditTrailEntry = doc.createElement("AuditTrailEntry");
        		processInstance.appendChild(auditTrailEntry);
        		
        		Element instance_name = doc.createElement("WorkflowModelElement");
    	        auditTrailEntry.appendChild(instance_name); 
    	        Text name_text = doc.createTextNode(as.getName()); 
    	        instance_name.appendChild(name_text); 	         
    	        
    	        Element instance_eventtype = doc.createElement("EventType");
    	        auditTrailEntry.appendChild(instance_eventtype); 
    	        Text type_text = doc.createTextNode("complete"); 
    	        instance_eventtype.appendChild(type_text);	        
    	        
    	        Element instance_starttime = doc.createElement("Timestamp"); 
    	        auditTrailEntry.appendChild(instance_starttime); 
    	        Text timestamp_text = doc.createTextNode(String.valueOf(as.getStartTime())); 
    	        instance_starttime.appendChild(timestamp_text);	        
    	        
    	        Element instance_actor = doc.createElement("Originator");
    	        auditTrailEntry.appendChild(instance_actor); 
    	        Text actor_text = doc.createTextNode(as.getActor()); 
    	        instance_actor.appendChild(actor_text);
        	}
	        
	        pId = i.getId();
        }
        try { 
            FileOutputStream fos = new FileOutputStream(filepath + File.separator + fileName + ".mxml"); 
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8"); 
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
