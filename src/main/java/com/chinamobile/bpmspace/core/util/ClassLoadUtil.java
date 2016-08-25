package com.chinamobile.bpmspace.core.util;

import com.chinamobile.bpmspace.core.exception.BasicException;

public class ClassLoadUtil {
	public static Object loadIndexInstance(String jarFile, String className)
			throws BasicException {

		try {
			Class<?> cls = Class.forName(className);
			return cls.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new BasicException(
					"Registered class doesn't exists, fail to register!");
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new BasicException("Fail to initialize registered class!");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new BasicException("Fail to initialize registered class!");
		}

		/*
		 * File file = new File(jarFile);
		 * 
		 * try { URL url = file.toURL(); URL[] urls = new URL[]{url};
		 * ClassLoader cl = new
		 * URLClassLoader(urls,this.getClass().getClassLoader()); //Class cls =
		 * cl.loadClass(className); Class cls =
		 * Class.forName(className,true,cl); return cls.newInstance(); } catch
		 * (ClassNotFoundException e) { e.printStackTrace(); throw new
		 * BasicException("Registered class doesn't exists, fail to register!");
		 * } catch (MalformedURLException e) { e.printStackTrace(); throw new
		 * BasicException("Malformed jar file url!"); } catch
		 * (InstantiationException e) { e.printStackTrace(); throw new
		 * BasicException("Fail to initialize registered class!"); } catch
		 * (IllegalAccessException e) { e.printStackTrace(); throw new
		 * BasicException("Fail to initialize registered class!"); }
		 */

		/*
		 * File file = new File(jarFile); URLClassLoader loader; Object obj =
		 * null; try { URL url = file.toURI().toURL(); loader = new
		 * URLClassLoader(new URL[] { url}); Class<?> clazz =
		 * loader.loadClass(className); obj = clazz.newInstance(); } catch
		 * (MalformedURLException e) { e.printStackTrace(); throw new
		 * BasicException("Malformed jar file url!"); } catch
		 * (ClassNotFoundException e) { e.printStackTrace(); throw new
		 * BasicException("Registered class doesn't exists, fail to register!");
		 * } catch (InstantiationException e) { e.printStackTrace(); throw new
		 * BasicException("Fail to initialize registered class!"); } catch
		 * (IllegalAccessException e) { e.printStackTrace(); throw new
		 * BasicException("Fail to initialize registered class!"); } return obj;
		 */

	}
}
