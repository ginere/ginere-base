package eu.ginere.base.util.notification;

import org.apache.log4j.Logger;


class LogNotificationImpl implements NotificationImplInterface{

	private static  Logger log = Logger.getLogger(Logger.class);
	
	static LogNotificationImpl SINGLETON= new LogNotificationImpl();

	private LogNotificationImpl(){
	}

	public static final NotificationImplInterface.Level DEBUG=new NotificationImplInterface.Level() {		
		@Override
		public void notify(String message, Throwable e) {
			log.debug(message,e);
		}
		
		@Override
		public boolean isEnabled() {
			return log.isDebugEnabled();
		}
	};

	public static final NotificationImplInterface.Level INFO=new NotificationImplInterface.Level() {		
		@Override
		public void notify(String message, Throwable e) {
			log.info(message,e);
		}
		
		@Override
		public boolean isEnabled() {
			return log.isInfoEnabled();
		}
	};

	public static final NotificationImplInterface.Level WARN=new NotificationImplInterface.Level() {		
		@Override
		public void notify(String message, Throwable e) {
			log.warn(message,e);
		}
		
		@Override
		public boolean isEnabled() {
			return true;
		}
	};


	public static final NotificationImplInterface.Level ERROR=new NotificationImplInterface.Level() {		
		@Override
		public void notify(String message, Throwable e) {
			log.error(message,e);
		}
		
		@Override
		public boolean isEnabled() {
			return true;
		}
	};


	public static final NotificationImplInterface.Level FATAL=new NotificationImplInterface.Level() {		
		@Override
		public void notify(String message, Throwable e) {
			log.fatal(message,e);
		}
		
		@Override
		public boolean isEnabled() {
			return true;
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
}
