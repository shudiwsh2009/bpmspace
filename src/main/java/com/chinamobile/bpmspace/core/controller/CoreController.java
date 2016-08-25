package com.chinamobile.bpmspace.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class CoreController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String mainPage(HttpSession session, HttpServletRequest request) {
		Object userIdObject = session.getAttribute("userId");
		if (userIdObject == null) {
			return "login";
		}
		return "modelDB";
	}

	@RequestMapping(value = "modelDB", method = RequestMethod.GET)
	public String mainPage2(HttpSession session, HttpServletRequest request) {
		Object userIdObject = session.getAttribute("userId");
		if (userIdObject == null) {
			return "login";
		}
		return "modelDB";
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String loginPage(HttpServletRequest request) {
		return "login";
	}

	@RequestMapping(value = "fileuploaddemo", method = RequestMethod.GET)
	public String loginPae(HttpServletRequest request) {
		return "fileuploaddemo";
	}

	@RequestMapping(value = "forgot_password", method = RequestMethod.GET)
	public String forgotPassword(HttpServletRequest request) {
		return "forgot_password";
	}

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String registerPage(HttpServletRequest request) {
		return "register";
	}

	@RequestMapping(value = "modelindex", method = RequestMethod.GET)
	public String modelIndexPage(HttpServletRequest request) {
		return "modelindex";
	}

	@RequestMapping(value = "caseindex", method = RequestMethod.GET)
	public String caseIndexPage(HttpServletRequest request) {
		return "caseindex";
	}

	@RequestMapping(value = "modelquery", method = RequestMethod.GET)
	public String modelQueryPage(HttpServletRequest request) {
		return "modelquery";
	}

	@RequestMapping(value = "ProcessAnalyzeIndex", method = RequestMethod.GET)
	public String processanalyzeindexPage(HttpServletRequest request) {
		return "ProcessAnalyzeIndex";
	}

	@RequestMapping(value = "ProcessManagerIndex", method = RequestMethod.GET)
	public String processmanagerindexPage(HttpServletRequest request) {
		return "ProcessManagerIndex";
	}

	@RequestMapping(value = "executeefficiencyanalyze", method = RequestMethod.GET)
	public String ExecuteEfficiencyAnalyzePage(HttpServletRequest request) {
		return "executeefficiencyanalyze";
	}

	@RequestMapping(value = "instanceDB", method = RequestMethod.GET)
	public String instanceManagement(HttpServletRequest request) {
		return "instanceDB";
	}

	@RequestMapping(value = "instanceSearch", method = RequestMethod.GET)
	public String instanceSearch(HttpServletRequest requet) {
		return "instanceSearch";
	}

	@RequestMapping(value = "input_instance", method = RequestMethod.GET)
	public String inputinstancePage(HttpServletRequest request) {
		return "input_instance";
	}

	@RequestMapping(value = "processmining", method = RequestMethod.GET)
	public String processMiningPage(HttpServletRequest request) {
		return "processmining";
	}

	@RequestMapping(value = "miningalgorithmevaluate", method = RequestMethod.GET)
	public String algorithmEvaluatePage(HttpServletRequest request) {
		return "miningalgorithmevaluate";
	}

	@RequestMapping(value = "loggenerate", method = RequestMethod.GET)
	public String logGeneratePage(HttpServletRequest request) {
		return "loggenerate";
	}

	@RequestMapping(value = "conformancecheck", method = RequestMethod.GET)
	public String conformanceCheckPage(HttpServletRequest request) {
		return "conformancecheck";
	}

	@RequestMapping(value = "socialnetworkmining", method = RequestMethod.GET)
	public String socialNetworkMiningPage(HttpServletRequest request) {
		return "socialnetworkmining";
	}

	@RequestMapping(value = "modelindextest", method = RequestMethod.GET)
	public String modelIndexTest(HttpServletRequest request) {
		return "modelindextest";
	}

	@RequestMapping(value = "modelSimilarity", method = RequestMethod.GET)
	public String modelSimilarity(HttpServletRequest request) {
		return "modelSimilarity";
	}

	@RequestMapping(value = "modelCheck", method = RequestMethod.GET)
	public String modelCheck(HttpServletRequest request) {
		return "modelcheck";
	}

	@RequestMapping(value = "modelDifferentiation", method = RequestMethod.GET)
	public String modelDifferentiation(HttpServletRequest request) {
		return "modelDifferentiation";
	}

	@RequestMapping(value = "modelStatistics", method = RequestMethod.GET)
	public String modelStatistics(HttpServletRequest request) {
		return "modelStatistics";
	}

	@RequestMapping(value = "modelMerge", method = RequestMethod.GET)
	public String modelMerge(HttpServletRequest request) {
		return "modelmerge";
	}

	@RequestMapping(value = "modelFragmentation", method = RequestMethod.GET)
	public String modelFragmentation(HttpServletRequest request) {
		return "modelFragmentation";
	}

	@RequestMapping(value = "modelCluster", method = RequestMethod.GET)
	public String modelCluster(HttpServletRequest request) {
		return "modelCluster";
	}

	@RequestMapping(value = "navigation", method = RequestMethod.GET)
	public String navigation(HttpServletRequest request) {
		return "navigation";
	}

}
