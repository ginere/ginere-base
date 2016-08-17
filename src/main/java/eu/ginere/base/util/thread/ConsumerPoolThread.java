/**
 * Copyright: Angel-Ventura Mendo Gomez
 *	      ventura@free.fr
 *
 * $Id: ConsumerPoolThread.java 21 2007-06-23 00:22:03Z ventura $
 */
package eu.ginere.base.util.thread;

import java.util.HashMap;
import java.util.Vector;

//import eu.ginere.base.util.log.Logger;

import org.apache.log4j.Logger;

/**
 * The same as ConsumerThread but using a pool of threads.
 * <p>
 * Use this pool to distribute work into a pool of thread. Add objects with the function
 * addObjectect and these objects will be consumed by the threads. The consuming
 * function must be implemented using the ConsumerThreadInterface.
 * 
 * <p>
 * This Pool has an important point if all threads are busy, stop the caller of the 
 * function addObjectect until one thread is freed, then the caller is awaked.
 * This do not  apply when the calling thread is one of the threads of the pool. 
 *
 * @author Angel-Ventura Mendo
 * @version $Revision: 21 $ $Date: 2007-06-23 02:22:03 +0200 $
 */
public class ConsumerPoolThread<E> {
	private static final Logger log = Logger.getLogger(ConsumerPoolThread.class);

	private static final int DEFAULT_THREAD_POOL_SIZE=5;


	private final ConsumerThreadInterface<E> consumer;
	private final Vector<InnerConsumerThread> threadList=new Vector<InnerConsumerThread>(); 
	private final HashMap<String,InnerConsumerThread> threadMap=new HashMap<String,InnerConsumerThread>(); 
	
	private final Vector<E> objectList=new Vector<E>(); 
	
	private String threadName;
	private int threadPoolSize=DEFAULT_THREAD_POOL_SIZE;
	private boolean stopped=false;
	private int stopedThreadNumber=0;
	

	public ConsumerPoolThread(int threadPoolSize,
							  ConsumerThreadInterface<E> consumer){
		this.threadName=getClass().getName();
		this.threadPoolSize=threadPoolSize;
		this.consumer=consumer;		

		initializePool();
	}

	public ConsumerPoolThread(String threadName,
							  int threadPoolSize,
							  ConsumerThreadInterface<E> consumer){
		this.threadName=threadName;
		this.threadPoolSize=threadPoolSize;
		this.consumer=consumer;		

		initializePool();
	}

	public ConsumerPoolThread(String threadName,
							  ConsumerThreadInterface<E> consumer){
		this.threadName=threadName;
		this.consumer=consumer;		

		initializePool();
	}

	/**
	 * add a new object into the consumer list to be consumed!
	 * Warning this wait the current thread to wait for an thread to handle this object
	 */ 
	public void addObject(E object){
		if(stopped==true){
			log.warn("Adding a new object ignored, the pool is stopped");
			return ;
		}

		// add to the end
		objectList.add(object);
//		if (log.isDebugEnabled()){
//			log.debug("ADD, size:'"+objectListSize()+"' "+object.hashCode()+" stopped:"+stopedThreadNumber+" "+object.toString());
//		}

		// Waits for an stoped worked before returning !!!!
		// except if this thread is a thread of the pool 
		if (!objectList.isEmpty() && stopedThreadNumber==0 && !threadMap.containsKey(Thread.currentThread().getName())){
			synchronized (threadList) {
				try {
					threadList.wait();
				} catch (InterruptedException e) {
					
				}
			}
		}
		
		synchronized (objectList) {
			objectList.notify();
		}
	}

	/**
	 * Return the number of thread into the pool size
	 */
	public int getThreadPoolSize(){
		return threadPoolSize;
	}

	public int getRunningThreadNumber(){
		return threadPoolSize-stopedThreadNumber;
	}
	
	/**
	 * Numer of inactive thread
	 * @return
	 */
	public int getStopedThreadNumber(){
		return stopedThreadNumber;
	}
	/**
	 * Return the number of objects waiting to be treated
	 */
	public int objectListSize(){
		return objectList.size();
	}

	
	public void stopConsumer(){
		stopped=true;
		synchronized (objectList) {
			objectList.notifyAll();		
		}
//		log.debug("Stopping consumer:"+threadName);
	}

	private void initializePool(){
		if (threadPoolSize<=0){
			threadPoolSize=DEFAULT_THREAD_POOL_SIZE;
		}
		for (int i=0;i<threadPoolSize;i++){
			InnerConsumerThread thread=new InnerConsumerThread(this,threadName,i);
			threadList.add(thread);
			threadMap.put(thread.getName(),thread);
		}
	}


	private class InnerConsumerThread extends Thread {
		private final ConsumerPoolThread<E>pool;
		
		private boolean workDone=false;
		
		InnerConsumerThread(ConsumerPoolThread<E>pool,String name,int number){
			super(name+"-"+number);
			this.pool=pool;

			setDaemon(true);
			
			// start the thread
			start();
		}
		
		public void threadWorkDone(){
			this.workDone=true;
		}

		public void run(){
			
			try {
				consumer.threadStarted();
			}catch(Throwable e){
				log.error("Error in threadStarted for thread:"+getName(),e);
			}
			try {
				while(!stopped){
	
					// Wait untill the list will have some elements
					synchronized (objectList) {
						if (objectList.isEmpty()){
							
	//							if (log.isDebugEnabled()){
	//								log.debug("Sleeping "+Thread.currentThread().getName()+" ... ");
	//							}
	
							synchronized (threadList) {
								threadList.notifyAll();
							}
							try {
								stopedThreadNumber++;
								objectList.wait();
							}catch (InterruptedException e){
								if (Thread.currentThread().isInterrupted()){
									log.fatal("Thread has been interupted, exit thread right now !!!",e);
									return ;
								}
							}finally{
								stopedThreadNumber--;
							}
	//						log.debug("AWAKE "+Thread.currentThread().getName()+" ... ");
						} 
					}
					
					// work done notification thread
					if (workDone){
						consumer.threadWorkDone();
						workDone=false;
					}
					
					// getting the next element to treat
					while (!objectList.isEmpty() && !stopped ){
						try {
							E object=null;
							synchronized (objectList) {
								if (!objectList.isEmpty()){
									object=objectList.remove(0);
								}
							}
							if (object!=null){
	//							if (log.isDebugEnabled()){
	//								log.debug("REMOVED, size:'"+objectListSize()+"'  "+object.hashCode()+" "+object.toString());
	//							}
								try {
									consumer.consume(pool,object);
								}catch(Exception e){
									log.error("While consumming object!",e);
								}
							}
						}catch(ArrayIndexOutOfBoundsException e){ // may bethe element has been taken by another thread
	//						log.info("While consumming object!",e);
						}
					}								
				}
	//			log.info("Consumer stopped:"+getName());
			} finally {
				try {
					consumer.threadStopped();
				}catch(Throwable e){
					log.error("Error in threadStopped for thread:"+getName(),e);
				}
			}
			
		}

	}

	/**
	 * Wait the current Thread until the work is done
	 */
	public void waitUntilWorkDone(){
		while(objectListSize()>0 || getRunningThreadNumber()>0){
			if (stopped){
				log.fatal("El pool se ha parado de forma anormal. List Size:"+objectListSize()+" running threads:"+getRunningThreadNumber()+"/"+getThreadPoolSize());
				return ;
			}
			if (log.isDebugEnabled()){
				log.debug("List Size:"+objectListSize()+" running threads:"+getRunningThreadNumber()+"/"+getThreadPoolSize());
			}
			try {
				log.info("STILL WAITING List Size:"+objectListSize()+" running threads:"+getRunningThreadNumber()+"/"+getThreadPoolSize());

				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		log.info(" Calling threadWorkDone.");

		for (InnerConsumerThread thread:threadList) {
			try {
				// update the work done flag and notify thre thread
				thread.threadWorkDone();
				synchronized (thread) {
					thread.notify();
				}
			}catch(Throwable e){
				log.error("Error in threadWorkDone for thread:"+thread.getName(),e);
			}
		}
		
		synchronized (objectList) {
			objectList.notifyAll();		
		}

		if (log.isInfoEnabled()){
			log.info("Work, done. List Size:"+objectListSize()+" running threads:"+getRunningThreadNumber()+"/"+getThreadPoolSize());
		}
		
		log.info("FINISH: Work, done. List Size:"+objectListSize()+" running threads:"+getRunningThreadNumber()+"/"+getThreadPoolSize());

	}
}
