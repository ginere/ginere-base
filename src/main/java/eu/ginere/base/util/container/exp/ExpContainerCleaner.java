package eu.ginere.base.util.container.exp;

import java.util.Vector;

import org.apache.log4j.Logger;

import eu.ginere.base.util.properties.GlobalFileProperties;
import eu.ginere.base.util.thread.AbstractPeriodicThread;

/**
 * This class is a thread that will clean the ExpContainers
 * The containers will subscribe their selfs on creation.
 * 
 * The thread is started at first container subscruved.
 * 
 * @author ventura
 *
 */
public class ExpContainerCleaner extends AbstractPeriodicThread {

	protected static final Logger log = Logger.getLogger(ExpContainerCleaner.class);


//	private final long objectMaxUnactiveTime;

	public static final ExpContainerCleaner MANAGER=new ExpContainerCleaner();
	
	public final Vector<AbstractExpContainerMap<?>> list = new Vector<AbstractExpContainerMap<?>>();
	

	public static final long DEFAULT_TIME_TO_SLEEP = 30*1000;
	public static final long DEFAULT_UNACTIVE_TIME = 15*60*1000;
	
	private ExpContainerCleaner(){
		super(DEFAULT_TIME_TO_SLEEP,ExpContainerCleaner.class.getName());
		setDaemon(true);
		setTimeToSleep(GlobalFileProperties.getLongValue(ExpContainerCleaner.class, "ThreadTimeToSleep", "Time to sleep of the container cleaner", DEFAULT_TIME_TO_SLEEP));
		
//		objectMaxUnactiveTime=GlobalFileProperties.getLongValue(ExpContainerCleaner.class, "DefaultUnactiveTime", DEFAULT_UNACTIVE_TIME);
		
	}

	/**
	 * This subscrive and start the thread if necessary
	 * @param container
	 */
	public void subscrive(AbstractExpContainerMap<?> container) {
		list.add(container);
		
		if (!super.isProcessing()){
			super.start();
		}
	}

	@Override
	protected void executeTask() {
//		long time=System.currentTimeMillis()-objectMaxUnactiveTime;

		for (AbstractExpContainerMap<?> manager:list){
			manager.purge();
		}
	}
	
	public Vector<AbstractExpContainerMap<?>> getList() {
		return list;
	}

}
