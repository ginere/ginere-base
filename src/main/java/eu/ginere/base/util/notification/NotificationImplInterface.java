package eu.ginere.base.util.notification;

import eu.ginere.base.util.test.TestInterface;


public interface NotificationImplInterface extends TestInterface{

	public boolean isEnabled(Level level);

	public void notify(Level fatal, String message, Throwable e);
}
