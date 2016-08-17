package eu.ginere.base.util.notification;

import org.apache.log4j.Logger;

import eu.ginere.base.util.test.TestResult;



public class Notify {

	public static final Logger log = Logger.getLogger(Notify.class);

	private static NotificationImplInterface connector=LogNotificationImpl.SINGLETON;


//	static {
//		if (MailNotificationImpl.useEmailNotification()){
//			connector=MailNotificationImpl.SINGLETON;
//		} else {
//			connector=LogNotificationImpl.SINGLETON;
//		}
//	}
//	
	
	public static TestResult test() {
		TestResult ret=new TestResult(Notify.class);
		
		if (connector==null){
			ret.addError("Not implementation class defined");
		} else {
			ret.add(connector.test());
		}
		return ret;			
	}
	
	public static void init(NotificationImplInterface c) {
		if (c!=null){
			connector=c;
			log.info("Connector changed to:"+c);
		}
	}
	/**
	 * @deprecated use init
	 * @param c
	 */
	public static void setConnector(NotificationImplInterface c){
		if (c!=null){
			connector=c;
		}
	}
	
	public static boolean isDegubEnabled(){
		return connector.isEnabled(Level.DEBUG);
	}

	public static boolean isInfoEnabled(){
		return connector.isEnabled(Level.INFO);
	}

	public static boolean isWarnEnabled(){
		return connector.isEnabled(Level.WARN);
	}

	public static boolean isErrorEnabled(){
		return true;
	}

	public static boolean isFatalEnabled(){
		return true;
	}


	public static void debug(String message){
		connector.notify(Level.DEBUG,message,null);
	}

	
	public static void debug(String message,Throwable e){
		connector.notify(Level.DEBUG,message,e);
	}
	
	public static void debug(Logger log,String message){
		log.debug(message);

		connector.notify(Level.DEBUG,message,null);
	}

	public static void debug(Logger log,String message,Throwable e){
		log.debug(message,e);

		connector.notify(Level.DEBUG,message,e);
	}

	public static void info(String message){
		connector.notify(Level.INFO,message,null);
	}

	public static void info(String message,Throwable e){
		connector.notify(Level.INFO,message,e);
	}

	public static void info(Logger log,String message){
		log.info(message);

		connector.notify(Level.INFO,message,null);
	}

	public static void info(Logger log,String message,Throwable e){
		log.info(message,e);

		connector.notify(Level.INFO,message,e);
	}

	public static void warn(String message){
		connector.notify(Level.WARN,message,null);
	}

	public static void warn(String message,Throwable e){
		connector.notify(Level.WARN,message,e);
	}

	public static void warn(Logger log,String message){
		log.warn(message);

		connector.notify(Level.WARN,message,null);
	}

	public static void warn(Logger log,String message,Throwable e){
		log.warn(message,e);

		connector.notify(Level.WARN,message,e);
	}

	public static void error(String message){
		connector.notify(Level.ERROR,message,null);
	}

	public static void error(String message,Throwable e){
		connector.notify(Level.ERROR,message,e);
	}

	public static void error(Logger log,String message){
		log.error(message);

		connector.notify(Level.ERROR,message,null);
	}

	public static void error(Logger log,String message,Throwable e){
		log.error(message,e);

		connector.notify(Level.ERROR,message,e);
	}

	public static void fatal(String message){
		connector.notify(Level.FATAL,message,null);
	}

	public static void fatal(String message,Throwable e){
		connector.notify(Level.FATAL,message,e);
	}

	public static void fatal(Logger log,String message){
		log.fatal(message);

		connector.notify(Level.FATAL,message,null);
	}

	public static void fatal(Logger log,String message,Throwable e){
		log.fatal(message,e);

		connector.notify(Level.FATAL,message,e);
	}
}
