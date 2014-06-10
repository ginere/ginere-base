package eu.ginere.base.util.notification;

import org.apache.log4j.Logger;



public class Notify {

	
	private static NotificationImplInterface connector=LogNotificationImpl.SINGLETON;

	static {
		if (MailNotificationImpl.useEmailNotification()){
			connector=MailNotificationImpl.SINGLETON;
		} else {
			connector=LogNotificationImpl.SINGLETON;
		}
	}
	public static boolean isDegubEnabled(){
		return connector.getDebug().isEnabled();
	}

	public static boolean isInfoEnabled(){
		return connector.getInfo().isEnabled();
	}
	public static boolean isWarnEnabled(){
		return connector.getWarn().isEnabled();
	}


	public static void debug(Logger log,String message){
		log.debug(message);

		connector.getDebug().notify(message,null);
	}

	public static void debug(Logger log,String message,Throwable e){
		log.debug(message,e);

		connector.getDebug().notify(message,e);
	}

	public static void info(Logger log,String message){
		log.info(message);

		connector.getInfo().notify(message,null);
	}

	public static void info(Logger log,String message,Throwable e){
		log.info(message,e);

		connector.getInfo().notify(message,e);
	}

	public static void warn(Logger log,String message){
		log.warn(message);

		connector.getWarn().notify(message,null);
	}

	public static void warn(Logger log,String message,Throwable e){
		log.warn(message,e);

		connector.getWarn().notify(message,e);
	}

	public static void error(Logger log,String message){
		log.error(message);

		connector.getError().notify(message,null);
	}

	public static void error(Logger log,String message,Throwable e){
		log.error(message,e);

		connector.getError().notify(message,e);
	}

	public static void fatal(Logger log,String message){
		log.fatal(message);

		connector.getFatal().notify(message,null);
	}

	public static void fatal(Logger log,String message,Throwable e){
		log.fatal(message,e);

		connector.getFatal().notify(message,e);
	}
}
