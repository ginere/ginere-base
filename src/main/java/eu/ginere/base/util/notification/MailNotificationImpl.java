package eu.ginere.base.util.notification;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.mail.MailManager;
import eu.ginere.base.util.manager.AbstractManager;
import eu.ginere.base.util.notification.MailNotificationImpl.NotificationEvent;
import eu.ginere.base.util.thread.ConsumerPoolThread;
import eu.ginere.base.util.thread.ConsumerThreadInterface;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.exception.ExceptionUtils;



class MailNotificationImpl extends AbstractManager implements NotificationImplInterface,ConsumerThreadInterface<NotificationEvent>{

	public static MailNotificationImpl SINGLETON=new MailNotificationImpl();
	private static final String PROPERTIES_FILE_NAME = "MailNotification.properties";

	final static protected String FATAL_MSG = "FATAL";
	final static protected String ERROR_MSG = "ERROR";
	final static protected String WARN_MSG = "WARN";
	final static protected String INFO_MSG = "INFO";
	final static protected String DEBUG_MSG = "DEBUG";

	final static protected int FATAL_LEVEL = 0;
	final static protected int ERROR_LEVEL = 3;
	final static protected int WARN_LEVEL = 4;
	final static protected int INFO_LEVEL = 6;
	final static protected int DEBUG_LEVEL = 7;

	class NotificationEvent {
		final String level;
		final String message;
		final Throwable e;
		final Date date;
		NotificationEvent(String level,String message, Throwable e){
			this.level=level;
			this.message=message;
			this.e=e;
			this.date=new Date();
		}
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
	}

	private final ConsumerPoolThread<NotificationEvent> pool;
	
	private MailNotificationImpl(){
		pool=new ConsumerPoolThread<MailNotificationImpl.NotificationEvent>(1, this);
	}
	
	private boolean isLevelEnabled(int level) {
		int currentLevel=getFileProperties(PROPERTIES_FILE_NAME).getIntValue(MailNotificationImpl.class,"level",DEBUG_LEVEL);

		return (currentLevel>=level);
	}

	public final NotificationImplInterface.Level DEBUG=new NotificationImplInterface.Level() {		
		@Override
		public void notify(String message, Throwable e) {
			if (isEnabled()){
				NotificationEvent event=new NotificationEvent(DEBUG_MSG,message,e);
				pool.addObject(event);
			} else {
				log.debug("No DEBUG level actived message:'"+message+"'",e);
			}
		}
		
		@Override
		public boolean isEnabled() {
			return isLevelEnabled(DEBUG_LEVEL);
		}
	};

	public final NotificationImplInterface.Level INFO=new NotificationImplInterface.Level() {		
		@Override
		public void notify(String message, Throwable e) {
			if (isEnabled()){
				NotificationEvent event=new NotificationEvent(INFO_MSG,message,e);
				pool.addObject(event);
			} else {
				log.debug("No INFO level actived message:'"+message+"'",e);
			}
		}
		
		@Override
		public boolean isEnabled() {
			return isLevelEnabled(INFO_LEVEL);
		}
	};
	

	public final NotificationImplInterface.Level WARN=new NotificationImplInterface.Level() {		
		@Override
		public void notify(String message, Throwable e) {
			if (isEnabled()){
				NotificationEvent event=new NotificationEvent(WARN_MSG,message,e);
				pool.addObject(event);
			} else {
				log.debug("No WARN level actived message:'"+message+"'",e);
			}
		}
		
		@Override
		public boolean isEnabled() {
			return isLevelEnabled(WARN_LEVEL);
		}
	};

	public final NotificationImplInterface.Level ERROR=new NotificationImplInterface.Level() {		
		@Override
		public void notify(String message, Throwable e) {
			if (isEnabled()){
				NotificationEvent event=new NotificationEvent(ERROR_MSG,message,e);
				pool.addObject(event);
			} else {
				log.debug("No WARN level actived message:'"+message+"'",e);
			}

		}
		
		@Override
		public boolean isEnabled() {
			return isLevelEnabled(ERROR_LEVEL);
		}
	};

	public final NotificationImplInterface.Level FATAL=new NotificationImplInterface.Level() {		
		@Override
		public void notify(String message, Throwable e) {
			if (isEnabled()){
				NotificationEvent event=new NotificationEvent(FATAL_MSG,message,e);
				pool.addObject(event);
			} else {
				log.debug("No DEBUG level actived message:'"+message+"'",e);
			}
		}
		
		@Override
		public boolean isEnabled() {
			return isLevelEnabled(FATAL_LEVEL);
		}
	};
	
	@Override
	public Level getDebug() {
		return DEBUG;
	}

	@Override
	public Level getInfo() {
		return INFO;
	}

	@Override
	public Level getWarn() {
		return WARN;
	}

	@Override
	public Level getError() {
		return ERROR;
	}

	@Override
	public Level getFatal() {
		return FATAL;
	}
	
	
	
	
	
	
	
	

	public static String buildSubject(NotificationEvent event){
		StringBuilder buffer=new StringBuilder();
		buffer.append(event.level);
		if (event.message!=null){
			buffer.append(" - ");
			buffer.append(event.message);
		}

		return buffer.toString();
	}
	
	public static String buildBody(NotificationEvent event){
		StringBuilder buffer=new StringBuilder();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		buffer.append(sdf.format(event.date));
		buffer.append(" - ");
		buffer.append(event.level);
		buffer.append("\n");
		
		if (event.message!=null){
			buffer.append("Message:\n");
			buffer.append(event.message);
			buffer.append("\n");
			
		}
		if (event.e!=null){
			printException(buffer,event.e);
		}
		
		return buffer.toString();
	}


	private static void printException(StringBuilder buffer, Throwable e) {
		buffer.append(e.getMessage());
		buffer.append("\n");
		
		buffer.append(ExceptionUtils.getStackTrace(e));
		
		Throwable cause=e.getCause();
		
		if (cause!=null){
			buffer.append("\n");
			printException(buffer, cause);
		}
	}

	@Override
	public void consume(ConsumerPoolThread<NotificationEvent> pool,
						NotificationEvent event) {
		try {
			String to=getFileProperties(PROPERTIES_FILE_NAME).getStringValue(MailNotificationImpl.class, "to");
			String from=getFileProperties(PROPERTIES_FILE_NAME).getStringValue(MailNotificationImpl.class, "from");
			String body=buildBody(event);
			String subject=buildSubject(event);
	
			MailManager.sendEmail(from,to, body, subject);
		}catch (DaoManagerException e) {
			log.error("While sending event"+event,e);
		}
	}

	public static boolean useEmailNotification() {
		try {
			return getFileProperties(PROPERTIES_FILE_NAME).getBooleanValue(MailNotificationImpl.class, "UseMailNotification",false);
		}catch (Throwable e){
			log.warn("Use email notification",e);
			return false;
		}
		
	}
	
}
