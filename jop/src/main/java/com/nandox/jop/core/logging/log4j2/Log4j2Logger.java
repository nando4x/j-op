package com.nandox.jop.core.logging.log4j2;

import com.nandox.jop.core.logging.Logger;

public class Log4j2Logger implements Logger {

	private org.apache.logging.log4j.Logger log;

	public Log4j2Logger(org.apache.logging.log4j.Logger log) {
	    this.log = log;
	}
	
	public void error(String msg, String... args) {
	    log.error(Logger.Utils.format(msg, args));
	}
	
	public void error(String msg, Object... args) {
	    log.error(Logger.Utils.format(msg, args));
	}
	
	public void error(String msg, Throwable ex, String... args) {
	    log.error(Logger.Utils.format(msg, args), ex);
	}
	
	public void info(String msg, String... args) {
	    log.info(Logger.Utils.format(msg, args));
	}
	
	public void info(String msg, Throwable ex, String... args) {
	    log.info(Logger.Utils.format(msg, args), ex);
	}
	
	public boolean isInfoEnabled() {
	    return log.isInfoEnabled();
	}
	
	public void warn(String msg, String... args) {
	    log.warn(Logger.Utils.format(msg, args));
	}
	
	public void warn(String msg, Object... args) {
	    log.warn(Logger.Utils.format(msg, args));
	}
	
	public void warn(String msg, Throwable ex, String... args) {
	    log.warn(Logger.Utils.format(msg, args), ex);
	}
	
	public boolean isDebugEnabled() {
	    return log.isDebugEnabled();
	}
	
	public void debug(String msg, String... args) {
	    log.debug(Logger.Utils.format(msg, args));
	}
	
	public void debug(String msg, Object... args) {
	    log.debug(Logger.Utils.format(msg, args));
	}
	
	public void debug(String msg, Throwable ex, String... args) {
	    log.debug(Logger.Utils.format(msg, args), ex);
	}
	
	public boolean isTraceEnabled() {
	    return log.isTraceEnabled();
	}
	
	public void trace(String msg, String... args) {
	    log.trace(Logger.Utils.format(msg, args));
	}
	
	public void trace(String msg, Object... args) {
	    log.trace(Logger.Utils.format(msg, args));
	}
	
	public void trace(String msg, Throwable ex, String... args) {
	    log.trace(Logger.Utils.format(msg, args), ex);
	}
		
	public void fatal(String msg, String... args) {
	    log.fatal(Logger.Utils.format(msg, args));
	}
	
	public void fatal(String msg, Throwable ex, String... args) {
	    log.fatal(Logger.Utils.format(msg, args), ex);
	}
	
	public boolean isErrorEnabled() {
	    return log.isErrorEnabled();
	}
	
	public boolean isFatalEnabled() {
	    return log.isFatalEnabled();
	}
	
	public boolean isWarnEnabled() {
	    return log.isWarnEnabled();
	}
}
