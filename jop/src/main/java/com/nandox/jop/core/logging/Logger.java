package com.nandox.jop.core.logging;

import com.nandox.jop.core.logging.log4j2.Log4j2Logger;
/**
 * Generic logger interface.<p>
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    Logger.java
 * 
 * @date      19 set 2016 - 19 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public interface Logger {
	void trace(String msg, String... args);
	void trace(String msg, Throwable ex, String... args);
	boolean isTraceEnabled();

	void debug(String msg, String... args);
	void debug(String msg, Throwable ex, String... args);
	boolean isDebugEnabled();
    
	void info(String msg, String... args);
	void info(String msg, Throwable ex, String... args);
	boolean isInfoEnabled();
    
	void warn(String msg, String... args);
	void warn(String msg, Throwable ex, String... args);
	boolean isWarnEnabled();

	void error(String msg, String... args);
	void error(String msg, Throwable ex, String... args);
	boolean isErrorEnabled();

	void fatal(String msg, String... args);
	void fatal(String msg, Throwable ex, String... args);
	boolean isFatalEnabled();
	
	/**
	 * Utilis class to format messages and others
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 */
	public static class Utils {
		/**
		 * Format messages
		 * @param	  msg		message to format
		 * @param	  args		variables arguments
		 * @date      04 ott 2016 - 04 ott 2016
		 * @author    Fernando Costantino
		 * @revisor   Fernando Costantino
		 * @return	  formatted string 
		 */
		public static String format(String msg, String... args) {
			return "";
		}
		public static String format(String msg, Object[] args) {
			return "";
		}
	}
	/**
	 * Logger factory
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 */
	public static class Factory {
		/**
		 * Format messages
		 * @param	  msg		message to format
		 * @param	  args		variables arguments
		 * @date      04 ott 2016 - 04 ott 2016
		 * @author    Fernando Costantino
		 * @revisor   Fernando Costantino
		 * @return	  formatted string 
		 */
		public static Logger getLogger(Class<?> clazz) {
			return new Log4j2Logger(org.apache.logging.log4j.LogManager.getLogger(clazz));
		}
	}
}
