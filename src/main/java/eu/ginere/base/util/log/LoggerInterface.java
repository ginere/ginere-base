package eu.ginere.base.util.log;

import org.apache.log4j.Priority;

public interface LoggerInterface {

	public boolean isDebugEnabled();
	public void debug(Object message);
	public void debug(Object message, Throwable t);

	public boolean isInfoEnabled();
	public void info(Object message);
	public void info(Object message, Throwable t);

	public boolean isWarnEnabled();
	public void warn(Object message);
	public void warn(Object message, Throwable t);

//	public boolean isErrorEnabled();
	public void error(Object message);
	public void error(Object message, Throwable t);

//	public boolean isFatalEnabled();
	public void fatal(Object message);
	public void fatal(Object message, Throwable t);


	// Use this function to chain loggers
	public void log(String callerFQCN, Priority level, Object message, Throwable t);

	
}
