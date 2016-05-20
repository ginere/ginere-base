/**
 * Copyright: Angel-Ventura Mendo Gomez
 *	      ventura@free.fr
 *
 * $Id: ConsumerThreadInterface.java 21 2007-06-23 00:22:03Z ventura $
 */
package eu.ginere.base.util.thread;

/**
 * Interface to be implemented by the class who consumes and threat objects into 
 * a thread <p>
 *
 * @author Angel-Ventura Mendo
 * @version $Revision: 21 $ $Date: 2007-06-23 02:22:03 +0200 $
 */
public interface ConsumerThreadInterface<E>{
	/**
	 * This function is called into a different threads
	 * be carefull if you use information outside this thread
	 * information must be thread safe
	 */
	public void consume(ConsumerPoolThread<E>pool,E object);


	/**
	 * For THREADLOCAL VARIABLES UPDATE.
	 * 
	 * This is call for each thread when the work is donne.
	 * use Thread.currentThread() to get the current thread.
	 * 
	 */
	public void threadWorkDone();


	/**
	 * For THREADLOCAL VARIABLES UPDATE.
	 * 
	 * This is call for each thread when the thread ends the run function.
	 * 
	 */
	public void threadStopped();


	/**
	 * For THREADLOCAL VARIABLES UPDATE.
	 * 
	 * This is call for each thread at the very begininng of the run function
	 * 
	 */
	public void threadStarted();
	
}

