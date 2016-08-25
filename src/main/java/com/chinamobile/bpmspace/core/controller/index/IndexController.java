package com.chinamobile.bpmspace.core.controller.index;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.IndexRepository;
import com.chinamobile.bpmspace.core.repository.index.ModelIndex;
import com.chinamobile.bpmspace.core.util.ClassLoadUtil;

@Controller
@RequestMapping("index")
public class IndexController {

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public void register(@RequestParam("cat") String cat,
			@RequestParam("cn") String cn, @RequestParam("desc") String desc,
			HttpSession session, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		// need to check the input id
		IndexRepository indexRepository = new IndexRepository();

		try {
			String index_id = indexRepository.register(cat, cn, desc);
			result.put("state", "SUCCESS");
			result.put("iid", index_id);
		} catch (BasicException e) {
			result.put("state", "FAIL");
			result.put("message", e.getInfo());
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

	@RequestMapping(value = "start", method = RequestMethod.POST)
	public void start(@RequestParam("iid") String index_id,
			HttpSession session, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		IndexRepository indexRepository = new IndexRepository();
		try {
			indexRepository.start(index_id);
			result.put("state", "SUCCESS");
			result.put("iid", index_id);
		} catch (BasicException e) {
			result.put("state", "FAIL");
			result.put("info", e.getInfo());
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

	@RequestMapping(value = "stop", method = RequestMethod.POST)
	public void stop(@RequestParam("iid") String index_id, HttpSession session,
			HttpServletResponse response) {
		JSONObject result = new JSONObject();
		IndexRepository indexRepository = new IndexRepository();
		try {
			indexRepository.stop(index_id);
			result.put("state", "SUCCESS");
			result.put("iid", index_id);
		} catch (BasicException e) {
			result.put("state", "FAIL");
			result.put("info", e.getInfo());
		}

		result.put("state", "SUCCESS");
		result.put("iid", index_id);
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

	@RequestMapping(value = "il", method = RequestMethod.POST)
	public void getIndexList(@RequestParam("cat") String cat,
			HttpSession session, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			IndexRepository indexRepository = new IndexRepository();
			String list = indexRepository.getIndexList(cat);
			result.put("list", list);
			result.put("state", "SUCCESS");
		} catch (BasicException ne) {
			result.put("state", "FAILED");
			result.put("message", ne.getInfo());
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

	@RequestMapping(value = "hotdeploytest", method = RequestMethod.GET)
	public void hotDeployTest(HttpSession session, HttpServletResponse response) {
		JSONObject result = new JSONObject();

		try {
			Object obj = ClassLoadUtil
					.loadIndexInstance("",
							"com.chinamobile.bpmspace.core.repository.index.hotdeploy.HotDeployTest");
			ModelIndex index = (ModelIndex) obj;
			String t = index.getType().toString();
			result.put("state", "SUCCESS");
		} catch (Exception e) {
			result.put("state", "FAIL");
			result.put("info", e.getMessage());
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

}
