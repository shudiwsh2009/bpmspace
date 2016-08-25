package com.chinamobile.bpmspace.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.ContextLoaderListener;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.CatalogType;
import com.chinamobile.bpmspace.core.domain.log.LogCatalog;
import com.chinamobile.bpmspace.core.domain.process.ProcessCatalog;
import com.chinamobile.bpmspace.core.exception.InitialiseException;

public class ServerInit extends ContextLoaderListener implements
		ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServerInit.initDatabase();
			ServerInit.initEnv();
			ServerInit.initFolder();
		} catch (InitialiseException e) {
			e.printStackTrace();
		}
	}

	public static void initDatabase() {
		MongoAccess mongo = new MongoAccess();
		ProcessCatalog publicRootProcessCatalog = mongo
				.getPublicRootProcessCatalog();
		if (publicRootProcessCatalog == null) {
			publicRootProcessCatalog = mongo.addProcessCatalog(
					"Public Catalog", "", CatalogType.PUBLIC, "", "", "");
		}
		FileUtil.PUBLIC_ROOT_PROCESS_CATALOG = publicRootProcessCatalog.getId();
		LogCatalog publicRootInstanceCatalog = mongo.getPublicRootLogCatalog();
		if (publicRootInstanceCatalog == null) {
			publicRootInstanceCatalog = mongo.addLogCatalog("Public Catalog",
					"", CatalogType.PUBLIC, "", "", "");
		}
		FileUtil.PUBLIC_ROOT_LOG_CATALOG = publicRootInstanceCatalog.getId();
	}

	public static void initEnv() throws InitialiseException {
		String processProfile = ServerInit.getWebAppRoot();
		if (processProfile.equals("")) {
			throw new InitialiseException("未检测到系统变量。");
		}
		FileUtil.WEBAPP_ROOT = processProfile;
	}

	public static void initFolder() throws InitialiseException {
		String processProfile = ServerInit.getWebAppRoot();
		if (processProfile.equals("")) {
			throw new InitialiseException("未检测到系统变量。");
		}
		List<File> folders = new ArrayList<File>();
		folders.add(new File(processProfile + FileUtil.SVG_PREFIX));
		folders.add(new File(processProfile + FileUtil.XML_PREFIX));
		folders.add(new File(processProfile + FileUtil.JSON_PREFIX));
		folders.add(new File(processProfile + FileUtil.BPMN_PREFIX));
		folders.add(new File(processProfile + FileUtil.EPML_PREFIX));
		folders.add(new File(processProfile + FileUtil.PNML_PREFIX));
		folders.add(new File(processProfile + FileUtil.EXPORT_PREFIX));
		folders.add(new File(processProfile + FileUtil.SIM_PREFIX));
		for (File f : folders) {
			if (!f.exists()) {
				f.mkdirs();
			}
		}
	}

	public static String getWebAppRoot() {
		Map<String, String> envMap = System.getenv();
		if (envMap.containsKey("PROCESSPROFILE")) {
			String processProfile = envMap.get("PROCESSPROFILE");
			if (!processProfile.endsWith(File.separator)) {
				processProfile += File.separator;
			}
			return processProfile;
		}
		return "";
	}

	public static ProcessCatalog createPublicRootProcessCatalog() {
		MongoAccess mongo = new MongoAccess();
		ProcessCatalog publicRootProcessCatalog = mongo
				.getPublicRootProcessCatalog();
		if (publicRootProcessCatalog == null) {
			publicRootProcessCatalog = mongo.addProcessCatalog(
					"Public Catalog", "", CatalogType.PUBLIC, "", "", "");
		}
		FileUtil.PUBLIC_ROOT_PROCESS_CATALOG = publicRootProcessCatalog.getId();
		return publicRootProcessCatalog;
	}

	public static LogCatalog createPublicRootLogCatalog() {
		MongoAccess mongo = new MongoAccess();
		LogCatalog publicRootLogCatalog = mongo.getPublicRootLogCatalog();
		if (publicRootLogCatalog == null) {
			publicRootLogCatalog = mongo.addLogCatalog("Public Catalog", "",
					CatalogType.PUBLIC, "", "", "");
		}
		FileUtil.PUBLIC_ROOT_LOG_CATALOG = publicRootLogCatalog.getId();
		return publicRootLogCatalog;
	}
}
