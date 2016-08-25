package com.chinamobile.bpmspace.core.controller.index;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinamobile.bpmspace.core.domain.index.CaseDurationIndex;
import com.chinamobile.bpmspace.core.domain.index.CaseIndexType;
import com.chinamobile.bpmspace.core.domain.index.CaseLengthIndex;
import com.chinamobile.bpmspace.core.domain.log.Log;
import com.chinamobile.bpmspace.core.domain.log.LogCatalog;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.repository.InstanceCatalogRepository;
import com.chinamobile.bpmspace.core.repository.LogRepository;
import com.chinamobile.bpmspace.core.repository.index.LogQueryResult;
import com.chinamobile.bpmspace.core.repository.index.logindex.aeventindex.AEventIndex;
import com.chinamobile.bpmspace.core.repository.index.logindex.durationindex.DurationIndex;
import com.chinamobile.bpmspace.core.repository.index.logindex.eventindex.EventIndex;
import com.chinamobile.bpmspace.core.repository.index.logindex.lengthindex.LengthIndex;

@Controller
@RequestMapping("instanceIndex")
public class InstanceIndexController {
	CaseLengthIndex ili;
	CaseDurationIndex idi;
	List<CaseLengthIndex> nilis = new ArrayList<CaseLengthIndex>();
	List<CaseDurationIndex> nidis = new ArrayList<CaseDurationIndex>();
	LengthIndex lIndex = new LengthIndex();
	DurationIndex dIndex = new DurationIndex();
	EventIndex eIndex = new EventIndex();
	AEventIndex aEIndex = new AEventIndex();

	List<LogQueryResult> results;
	String info = "";

	@RequestMapping(value = "index_open", method = RequestMethod.GET)
	@ResponseBody
	public void indexOpen(@RequestParam("indexName") String _indexName,
			HttpSession session, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		if (_indexName.equals(CaseIndexType.LENGTHINDEX)) {
			LengthIndex lIndex = new LengthIndex();
			if (lIndex.create()) {
				result.put("state", "SUCCESS");
			} else {
				result.put("state", "FAILED");
			}
		}
		if (_indexName.equals(CaseIndexType.DURATIONINDEX)) {
			DurationIndex dIndex = new DurationIndex();
			if (dIndex.create()) {
				result.put("state", "SUCCESS");
			} else {
				result.put("state", "FAILED");
			}
		}
		if (_indexName.equals(CaseIndexType.EVENTINDEX)) {
			EventIndex eIndex = new EventIndex();
			if (eIndex.create()) {
				result.put("state", "SUCCESS");
			} else {
				result.put("state", "FAILED");
			}
		}
		if (_indexName.equals(CaseIndexType.AEVENTINDEX)) {
			AEventIndex aEIndex = new AEventIndex();
			if (aEIndex.create()) {
				result.put("state", "SUCCESS");
			} else {
				result.put("state", "FAILED");
			}
		}

		// response body
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

	@RequestMapping(value = "index_close", method = RequestMethod.GET)
	@ResponseBody
	public void indexClose(@RequestParam("indexName") String _indexName,
			HttpSession session, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		if (_indexName.equals(CaseIndexType.LENGTHINDEX)) {
			LengthIndex lIndex = new LengthIndex();
			if (lIndex.destroy()) {
				result.put("state", "SUCCESS");
			} else {
				result.put("state", "FAILED");
			}
		}
		if (_indexName.equals(CaseIndexType.DURATIONINDEX)) {
			DurationIndex dIndex = new DurationIndex();
			if (dIndex.destroy()) {
				result.put("state", "SUCCESS");
			} else {
				result.put("state", "FAILED");
			}
		}
		if (_indexName.equals(CaseIndexType.EVENTINDEX)) {
			EventIndex eIndex = new EventIndex();
			if (eIndex.destroy()) {
				result.put("state", "SUCCESS");
			} else {
				result.put("state", "FAILED");
			}
		}
		if (_indexName.equals(CaseIndexType.AEVENTINDEX)) {
			AEventIndex aEIndex = new AEventIndex();
			if (aEIndex.destroy()) {
				result.put("state", "SUCCESS");
			} else {
				result.put("state", "FAILED");
			}
		}

		// response body
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

	@RequestMapping(value = "query_check", method = RequestMethod.GET)
	@ResponseBody
	public void checkName(@RequestParam("type") String _type,
			@RequestParam("q") String _query, HttpSession session,
			HttpServletResponse response) {
		JSONObject result = new JSONObject();
		String regExDigital = "^[0-9]+$";
		String regExBetween = "^[0-9]+\\,\\s*[0-9]+$";
		String regExGTE = "^\\>\\=[0-9]+$";
		String regExLTE = "^\\<\\=[0-9]+$";
		String regCN = "^[\\u4E00-\\u9FA5]+$";
		String regCN2 = "^[\\u4E00-\\u9FA5]+\\,[\\u4E00-\\u9FA5]+$";
		Pattern pDigital = Pattern.compile(regExDigital);
		Pattern pBetween = Pattern.compile(regExBetween);
		Pattern pGTE = Pattern.compile(regExGTE);
		Pattern pLTE = Pattern.compile(regExLTE);
		Pattern pCN = Pattern.compile(regCN);
		Pattern pCN2 = Pattern.compile(regCN2);
		Matcher mDigital = pDigital.matcher(_query);
		Matcher mBetween = pBetween.matcher(_query);
		Matcher mGTE = pGTE.matcher(_query);
		Matcher mLTE = pLTE.matcher(_query);
		if (_type.equals("caseEvent") || _type.equals("adjacentEvent")) {
			try {
				_query = new String(_query.getBytes("ISO8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Matcher mCN = pCN.matcher(_query);
		Matcher mCN2 = pCN2.matcher(_query);
		if (_query.equals("")) {
			result.put("state", "SUCCESS");
		} else if (mDigital.find()) {
			result.put("state", "SUCCESS");
		} else if (mBetween.find()) {
			if (checkBetweenQuery(_query)) {
				result.put("state", "SUCCESS");
			} else {
				result.put("state", "FAILED");
			}
		} else if (mGTE.find()) {
			result.put("state", "SUCCESS");
		} else if (mLTE.find()) {
			result.put("state", "SUCCESS");
		} else if (mCN.find()) {
			result.put("state", "SUCCESS");
		} else if (mCN2.find()) {
			result.put("state", "SUCCESS");
		} else {
			result.put("state", "FAILED");
		}
		
		// close check
		result.put("state", "SUCCESS");

		// response body
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

	public boolean checkBetweenQuery(String _query) {
		String[] query = _query.split(",");
		return (Integer.valueOf(query[0]) <= Integer.valueOf(query[1])) ? true
				: false;
	}

	@RequestMapping(value = "adjacentEvent_query", method = RequestMethod.POST)
	@ResponseBody
	public void adjacentEventQuery(@RequestParam("q") String _query,
			HttpSession session, HttpServletResponse response)
			throws NumberFormatException, EmptyFieldException {

		// DurationIndexRepository dir = new DurationIndexRepository();
		nidis = new ArrayList<CaseDurationIndex>();
		JSONArray result = new JSONArray();
		String[] query = _query.split(",");

		if (_query.equals("") || _query == null) {
			throw new EmptyFieldException("查询语句为空！");
		}
		results = aEIndex.getLogs(query[0], query[1]);

		// response
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

	@RequestMapping(value = "event_query", method = RequestMethod.POST)
	@ResponseBody
	public void eventQuery(@RequestParam("q") String _query,
			HttpSession session, HttpServletResponse response)
			throws NumberFormatException, EmptyFieldException {

		// DurationIndexRepository dir = new DurationIndexRepository();
		nidis = new ArrayList<CaseDurationIndex>();
		JSONArray result = new JSONArray();
		if (_query.equals("") || _query == null) {
			throw new EmptyFieldException("查询语句为空！");
		}
		results = eIndex.getLogs(_query);

		// response
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

	@RequestMapping(value = "duration_query", method = RequestMethod.POST)
	@ResponseBody
	public void durationQuery(@RequestParam("q") String _query,
			HttpSession session, HttpServletResponse response)
			throws NumberFormatException, EmptyFieldException {
		info = "包含实例耗时";
		// DurationIndexRepository dir = new DurationIndexRepository();
		nidis = new ArrayList<CaseDurationIndex>();
		JSONArray result = new JSONArray();
		if (_query.equals("") || _query == null) {
			throw new EmptyFieldException("查询语句为空！");
		}
		if (_query.indexOf(",") > 0) {
			String[] range = _query.split(",");
			int d = Integer.parseInt(range[0]);
			int u = Integer.parseInt(range[1]);
			// nidis = dir.getDurationBewteen(d, u);
			results = dIndex.getLogsBetween(_query, d, u);

		} else if (_query.indexOf("=") > 0) {
			String[] tp = _query.split("=");
			if (tp[0].equalsIgnoreCase(">")) {
				int down = Integer.parseInt(tp[1]);
				// nidis = dir.getDurationBewteen(down, 1000);
				results = dIndex.getLogsGTE(_query, down, 1294967296);
			} else if (tp[0].equalsIgnoreCase("<")) {
				int up = Integer.parseInt(tp[1]);
				// nidis = dir.getDurationBewteen(1, up);
				results = dIndex.getLogsLTE(_query, 1, up);
			}
		} else {
			try {
				// ili = lir.getLengthQuery(Integer.parseInt(_query));
				results = dIndex.getLogs(_query, Integer.parseInt(_query));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		// response
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

	@RequestMapping(value = "length_query", method = RequestMethod.POST)
	@ResponseBody
	public void lengthQuery(@RequestParam("q") String _query,
			HttpSession session, HttpServletResponse response)
			throws NumberFormatException, EmptyFieldException {
		info = "包含实例长度";
		// LengthIndexRepository lir = new LengthIndexRepository();
		nilis = new ArrayList<CaseLengthIndex>();
		JSONArray result = new JSONArray();
		if (_query.equals("") || _query == null) {
			throw new EmptyFieldException("查询语句为空！");
		}
		if (_query.indexOf(",") > 0) {
			String[] range = _query.split(",");
			int d = Integer.parseInt(range[0]);
			int u = Integer.parseInt(range[1]);
			// nilis = lir.getLengthBewteen(d, u);
			results = lIndex.getLogsBetween(_query, d, u);

		} else if (_query.indexOf("=") > 0) {
			String[] tp = _query.split("=");
			if (tp[0].equalsIgnoreCase(">")) {
				int down = Integer.parseInt(tp[1]);
				// nilis = lir.getLengthBewteen(down, 100000);
				results = lIndex.getLogsGTE(_query, down, 1294967296);
			} else if (tp[0].equalsIgnoreCase("<")) {
				int up = Integer.parseInt(tp[1]);
				// nilis = lir.getLengthBewteen(1, up);
				results = lIndex.getLogsLTE(_query, 1, up);
			}
		} else {
			try {
				// ili = lir.getLengthQuery(Integer.parseInt(_query));
				results = lIndex.getLogs(_query, Integer.parseInt(_query));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		// response
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

	public String getAbsolutePath(String _itemId) throws EmptyFieldException,
			NoExistException {
		InstanceCatalogRepository icr = new InstanceCatalogRepository();
		// LogRepository lr = new LogRepository();

		LogCatalog ic = null;

		ic = icr.getInstanceCatalog(_itemId);

		if (ic.getParentId().equals("")) {
			return ic.getName() + File.separator;
		} else {
			return getAbsolutePath(ic.getParentId()) + ic.getName()
					+ File.separator;
		}

	}

	@RequestMapping(value = "result_show", method = RequestMethod.GET)
	@ResponseBody
	public void getInstance(@RequestParam("logId") String _type,
			HttpServletResponse response) throws BasicException {
		LogRepository lr = new LogRepository();
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (LogQueryResult lqr : results) {

			JSONArray ja = new JSONArray();
			Log log = lr.getLogByLogId(lqr.getLog_id());
			if (log != null) {
				ja.put(log.getId());
				ja.put(log.getName());
				ja.put(getAbsolutePath(log.getCatalogId()));
				ja.put(log.getCreateTime());

				array.put(ja);
			}

		}
		/*
		 * if (_type.equals("length")) { if (ili == null && !nilis.isEmpty()) {
		 * try { for (InstanceLengthIndex iliIndex : nilis) { for (String logId
		 * : iliIndex.getLogList()) { JSONArray ja = new JSONArray(); Log log =
		 * lr.getLogByLogId(logId); ja.put(log.getId()); ja.put(log.getName());
		 * ja.put(info + iliIndex.getLength()); ja.put(log.getCatalogId());
		 * ja.put(log.getCreateTime());
		 * 
		 * array.put(ja); } } info = null; nilis.clear(); } catch
		 * (BasicException e) { // TODO: handle exception } } else if(!(ili ==
		 * null) && nilis.isEmpty()) { try { for (String logId :
		 * ili.getLogList()) { JSONArray ja = new JSONArray(); Log log =
		 * lr.getLogByLogId(logId); ja.put(log.getId()); ja.put(log.getName());
		 * ja.put(info + ili.getLength()); ja.put(log.getCatalogId());
		 * ja.put(log.getCreateTime());
		 * 
		 * array.put(ja); } info = null; ili = null; } catch (BasicException e)
		 * { // TODO: handle exception } } } else if (_type.equals("duration"))
		 * { if (idi == null && !nidis.isEmpty()) { try { for
		 * (InstanceDurationIndex idiIndex : nidis) { for (String logId :
		 * idiIndex.getLogList()) { JSONArray ja = new JSONArray(); Log log =
		 * lr.getLogByLogId(logId); ja.put(log.getId()); ja.put(log.getName());
		 * ja.put(info + idiIndex.getDuration()); ja.put(log.getCatalogId());
		 * ja.put(log.getCreateTime());
		 * 
		 * array.put(ja); } } info = null; nidis.clear(); } catch
		 * (BasicException e) { // TODO: handle exception } } else if(!(idi ==
		 * null) && nidis.isEmpty()) { try { for (String logId :
		 * idi.getLogList()) { JSONArray ja = new JSONArray(); Log log =
		 * lr.getLogByLogId(logId); ja.put(log.getId()); ja.put(log.getName());
		 * ja.put(info + idi.getDuration()); ja.put(log.getCatalogId());
		 * ja.put(log.getCreateTime());
		 * 
		 * array.put(ja); } info = null; idi = null; } catch (BasicException e)
		 * { // TODO: handle exception } } }
		 */
		result.put("aaData", array);
		results.clear();
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
