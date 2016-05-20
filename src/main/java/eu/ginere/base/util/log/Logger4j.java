package eu.ginere.base.util.log;

import org.apache.log4j.Priority;

public class Logger4j implements LoggerInterface{

	private static final String CANONICAL_NAME=Logger4j.class.getCanonicalName();
	
	static public LoggerInterface getLogger(Class<?> clazz) {
		return new Logger4j(clazz);
	}
	

	final org.apache.log4j.Logger log;

	private Logger4j(Class<?> clazz){
		this.log=org.apache.log4j.Logger.getLogger(clazz);
	}


	public boolean isDebugEnabled(){
		return log.isDebugEnabled();
	}

	public void debug(Object message){
		innerLog(org.apache.log4j.Level.DEBUG,message);
	}

	public void debug(Object message, Throwable t){
		innerLog(org.apache.log4j.Level.DEBUG,message,t);
	}

	public boolean isInfoEnabled(){
		return log.isInfoEnabled();
	}

	public void info(Object message){
		innerLog(org.apache.log4j.Level.INFO,message);
	}

	public void info(Object message, Throwable t){
		innerLog(org.apache.log4j.Level.INFO,message,t);
	}


	public boolean isWarnEnabled(){
		return true;
	}

	public void warn(Object message){
		innerLog(org.apache.log4j.Level.WARN,message);
	}

	public void warn(Object message, Throwable t){
		innerLog(org.apache.log4j.Level.WARN,message,t);
	}


	public boolean isErrorEnabled(){
		return true;
	}

	public void error(Object message){
		innerLog(org.apache.log4j.Level.ERROR,message);
	}

	public void error(Object message, Throwable t){
		innerLog(org.apache.log4j.Level.ERROR,message,t);
	}


	public boolean isFatalEnabled(){
		return true;
	}

	public void fatal(Object message){
		innerLog(org.apache.log4j.Level.FATAL,message);
	}

	public void fatal(Object message, Throwable t){
		innerLog(org.apache.log4j.Level.FATAL,message,t);
	}

	public void log(String callerFQCN, Priority level, Object message, Throwable t) {
		log.log(callerFQCN,level,message,t);		
	}
	
	private void innerLog(Priority level, Object message, Throwable t) {
		log.log(CANONICAL_NAME,org.apache.log4j.Level.ERROR,message,t);
	}

	private void innerLog(Priority level, Object message) {
		log.log(CANONICAL_NAME,org.apache.log4j.Level.ERROR,message,null);
	}


}