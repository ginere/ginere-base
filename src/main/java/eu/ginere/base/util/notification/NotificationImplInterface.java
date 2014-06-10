package eu.ginere.base.util.notification;


public interface NotificationImplInterface {

	public interface Level {
		public boolean isEnabled();
		public void notify(String message,Throwable e);

	}

	public Level getDebug();
	public Level getInfo();
	public Level getWarn();
	public Level getError();
	public Level getFatal();
}
