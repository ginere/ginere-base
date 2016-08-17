package eu.ginere.base.util.log;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

/**
 * @author ventura
 *
 * This allow loggers for thread ... al the logs of a thread (process) will be stored in a logger.
 * This alos have functionalities to inherit the logger from other processous,
 */
public class Logger implements LoggerInterface{

	private static final String CANONICAL_NAME=Logger.class.getCanonicalName();
	
	static public Logger getLogger(Class<?> clazz) {
		return new Logger(clazz);
	}
	

	private ThreadLocal<LoggerInterface> tl=new ThreadLocal<LoggerInterface>();
	private boolean threadLocal=false;
	final LoggerInterface log;
	LoggerInterface threadLocalLogger=null;
	

	private Logger(Class<?> clazz){
		this.log=Logger4j.getLogger(clazz);
	}

	public void setThreadLocal(LoggerInterface li){
		if (li!=null){
			threadLocal=true;
			threadLocalLogger=li;
			tl.set(li);
		}
	}

	public void propagateThreadLocal(){
		if (threadLocal){
			if (threadLocalLogger!=null){
				tl.set(threadLocalLogger);				
			} else{
				log.warn("The threadlocal logger is null while propagateThreadLocal");
			}
//			setThreadLocal(threadLocalLogger);
		}		
	}
	
	public void unPropagateThreadLocal(){
		removeThreadLocal();
	}
	
	public void removeThreadLocal(){
//		This remove the threadlocal for the thread not for the object		
//		threadLocal=false;
		tl.remove();
	}

	private LoggerInterface log(){
		if (threadLocal){
			// maybe the readloacal is active but not for this thread ...
			LoggerInterface ret=tl.get();
			if (ret!=null){
				return ret;
			}
		}
		return log;

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