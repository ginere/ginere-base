package eu.ginere.base.util.log;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

public class Logger implements LoggerInterface{

	private static final String CANONICAL_NAME=Logger.class.getCanonicalName();
	
	static public Logger getLogger(Class<?> clazz) {
		return new Logger(clazz);
	}
	

	private ThreadLocal<LoggerInterface> tl=new ThreadLocal<LoggerInterface>();
	private boolean threadLocal=false;
	final LoggerInterface log;
	

	private Logger(Class<?> clazz){
		this.log=Logger4j.getLogger(clazz);
	}

	public void setThreadLocal(LoggerInterface li){
		if (li!=null){
			threadLocal=true;
			tl.set(li);
		}
	}

	public void removeThreadLocal(){
		threadLocal=false;
		tl.remove();
	}

	private LoggerInterface log(){
		if (threadLocal){
			return tl.get();
		} else {
			return log;
		}
	}

	public boolean isDebugEnabled(){
		return log().isDebugEnabled();
	}

	public void debug(Object message){
		innerLog(org.apache.log4j.Level.DEBUG,message);
	}

	public void debug(Object message, Throwable t){
		innerLog(org.apache.log4j.Level.DEBUG,message,t);
	}

	public boolean isInfoEnabled(){
		return log().isInfoEnabled();
	}

	public void info(Object message){
		innerLog(org.apache.log4j.Level.INFO,message);
	}

	public void info(Object message, Throwable t){
		innerLog(org.apache.log4j.Level.INFO,message,t);
	}


	public boolean isWarnEnabled(){
		return log().isWarnEnabled();
	}

	public void warn(Object message){
		innerLog(org.apache.log4j.Level.WARN,message);
	}

	public void warn(Object message, Throwable t){
		innerLog(org.apache.log4j.Level.WARN,message,t);
	}

	public void error(Object message){
		innerLog(org.apache.log4j.Level.ERROR,message);
	}

	public void error(Object message, Throwable t){
		innerLog(org.apache.log4j.Level.ERROR,message,t);
	}

	public void fatal(Object message){
		innerLog(org.apache.log4j.Level.FATAL,message);
	}

	public void fatal(Object message, Throwable t){
		innerLog(org.apache.log4j.Level.FATAL,message,t);
	}

	@Override
	public void log(String callerFQCN, Priority level, Object message,Throwable t) {
		log().log( callerFQCN,  level,  message, t);
		
	}
	
	private void innerLog(Level level, Object message, Throwable t) {
		log().log(CANONICAL_NAME,level,message,t);				
	}

	private void innerLog(Level level, Object message) {
		log().log(CANONICAL_NAME,level,message,null);		
	}
	
}