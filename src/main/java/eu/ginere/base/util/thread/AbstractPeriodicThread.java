/**
 * Copyright: Angel-Ventura Mendo Gomez
 *	      ventura@free.fr
 *
 * $Id: AbstractPeriodicThread.java 167 2007-11-12 22:27:24Z ventura $
 */
package eu.ginere.base.util.thread;

import org.apache.log4j.Logger;

/**
 * This class implements one thread that execute one abstract function with a
 * periodicaly
 * 
 * @author Angel-Ventura Mendo
 * @version $Revision: 167 $ $Date: 2007-11-12 23:27:24 +0100 (lun, 12 nov 2007) $
 */
public abstract class AbstractPeriodicThread extends Thread {
	private static final Logger log = Logger.getLogger(AbstractPeriodicThread.class);

	private boolean processing = false;

	private long timeToSleep;

	public AbstractPeriodicThread(long timeToSleep) {
		this.timeToSleep = timeToSleep;
	}

	public AbstractPeriodicThread(long timeToSleep, String name) {
		super(name);
		this.timeToSleep = timeToSleep;
	}

	public void start() {
		if (processing) {
			return;
		} else {
			processing = true;
			super.start();
		}
	}

	public void stopProcessing() {
		processing = false;
	}

	public void run() {
		while (processing) {
			try {
				sleep(timeToSleep);
			} catch (InterruptedException e) {
			}
			try {
				executeTask();
			} catch (Exception e) {
				log.warn("While processing executting process", e);
			}
		}
	}

	/**
	 * This must be implemented by the chld task
	 */
	protected abstract void executeTask();

	/**
	 * Returns if the thread is processing ...
	 * 
	 * @return Returns the processing.
	 */
	public boolean isProcessing() {
		return processing;
	}

	/**
	 * @return the timeToSleep in millis
	 */
	public long getTimeToSleep() {
		return timeToSleep;
	}

	/**
	 * @param the time to sleep in millis
	 */
	public void setTimeToSleep(long time) {
		timeToSleep=time;
	}
}
