/**
 * Copyright: Angel-Ventura Mendo Gomez
 *	      ventura@free.fr
 *
 * $Id: PeriodicThread.java 21 2007-06-23 00:22:03Z ventura $
 */
package eu.ginere.base.util.thread;

import org.apache.log4j.Logger;

/**
 * This extens the {@link AbstractPeriodicThread} to call one listener into
 * the abstract method.
 *
 * @author Angel-Ventura Mendo
 * @version $Revision: 21 $ $Date: 2007-06-23 02:22:03 +0200 $
 *
 */
public class PeriodicThread extends AbstractPeriodicThread {
	private static final Logger log = Logger.getLogger(PeriodicThread.class);

	public interface TaskInterface{
		/**
		 * This function is called into a different threads
		 * be carefull if you use information outside this thread
		 * information must be thread safe
		 */
		public void executeTask();
	}
	
	private final TaskInterface task;


	public PeriodicThread(TaskInterface task,long timeToSleep){
		super(timeToSleep);
		this.task=task;
	}

	public PeriodicThread(TaskInterface task,long timeToSleep,String name){
		super(timeToSleep,name);
		this.task=task;
	}
	
	/**
	 * This must be implemented by the chld task
	 */
	protected void executeTask(){
		try {
			task.executeTask();
		}catch(Exception e){
			log.warn("While processing process:'"+task+"'",e);				
		}
	}
}
