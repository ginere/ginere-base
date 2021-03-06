package eu.ginere.base.util.dao.cache;

import java.util.Vector;

import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.cache.impl.AbstractKeyCacheManager;
import eu.ginere.base.util.properties.GlobalFileProperties;
import eu.ginere.base.util.thread.AbstractPeriodicThread;

public class KeyCacheManager extends AbstractPeriodicThread {

	protected static final Logger log = Logger.getLogger(KeyCacheManager.class);


	private final long objectMaxUnactiveTime;

	public static final KeyCacheManager MANAGER=new KeyCacheManager();
	
	public final Vector<AbstractKeyCacheManager<?>> list = new Vector<AbstractKeyCacheManager<?>>();
	

	public static final long DEFAULT_TIME_TO_SLEEP = 30*1000;
	public static final long DEFAULT_UNACTIVE_TIME = 15*60*1000;
	
	private KeyCacheManager(){
		super(DEFAULT_TIME_TO_SLEEP,KeyCacheManager.class.getName());
		setDaemon(true);
		setTimeToSleep(GlobalFileProperties.getLongValue(KeyCacheManager.class, "ThreadTimeToSleep", "KeyCacheManager ThreadTimeToSleep",DEFAULT_TIME_TO_SLEEP));
		
		objectMaxUnactiveTime=GlobalFileProperties.getLongValue(KeyCacheManager.class, "DefaultUnactiveTime","KeyCacheManager DefaultUnactiveTime", DEFAULT_UNACTIVE_TIME);
		
	}

	public void subscrive(AbstractKeyCacheManager<?> abstractKeyManager) {
		list.add(abstractKeyManager);
		
		if (!super.isProcessing()){
			super.start();
		}
	}

	@Override
	protected void executeTask() {
//		long time=System.currentTimeMillis()-objectMaxUnactiveTime;

		for (AbstractKeyCacheManager<?> manager:list){
			manager.purge(objectMaxUnactiveTime);
		}
	}

}
