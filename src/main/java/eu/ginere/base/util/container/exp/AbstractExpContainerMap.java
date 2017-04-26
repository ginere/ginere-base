package eu.ginere.base.util.container.exp;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;


/**
 * @author ventura
 * 
 * ATTENTION NULL KEYS ARE NOT SUPPORTED. USE OF ConcurrentHashMap.
 * 
 * This abstract class should be implemented by all the ExpContainer.
 * This allows the ExpContainer to be subscrived this the cleaner and aloow the cleaner.
 * 
 * This alos implement the basic funcionality
 * 
 */
public abstract class AbstractExpContainerMap<T extends ExpContainerObject> {
	protected static final Logger log = Logger.getLogger(AbstractExpContainerMap.class);
	
	protected final Map<String, T> map = new ConcurrentHashMap<String, T>();
	
	private final String name;
	protected long minUnactiveTime;

	protected AbstractExpContainerMap(long objectMaxUnactiveTime){
		this.minUnactiveTime=objectMaxUnactiveTime;
		this.name=getClass().getName();
		
		// This subscrive to the cleaner
		ExpContainerCleaner.MANAGER.subscrive(this);
	}

	protected AbstractExpContainerMap(long objectMaxUnactiveTime,String name){
		this.minUnactiveTime=objectMaxUnactiveTime;
		this.name=name;
		
		// This subscrive to the cleaner
		ExpContainerCleaner.MANAGER.subscrive(this);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the max unactied time before the object will be purged.
	 * @param objectMaxUnactiveTime
	 */
	public void setMinUnactiveTime(long objectMaxUnactiveTime) {
		this.minUnactiveTime=objectMaxUnactiveTime;
	}
	
	public long getMinUnactiveTime() {
		return this.minUnactiveTime;
	}

	public boolean exists(String id) {
		if (id==null){
			return false;
		} else {
			return map.containsKey(id);
		}
	}

	public boolean containsKey(String id){
		return exists(id);
	}
		
	public T get(String id,T defaultValue){
		T ret;
		
		if (exists(id)){
			ret=map.get(id);
			
			if (ret!=null){
				ret.setAccessed();
			}
			return ret;
		} else {
			return defaultValue;
		}
	}
	
	public T get(String id){
		return get(id,null);
	}
	
	public T put(String id,T obj){
		// throw exception HERE ?????
		if (id == null) {
			return null;
		} else {
			map.put(id,obj);
			
			if (obj!=null){
				obj.setAccessed();
			}
			
			return obj;
		}
	}

	/**
	 * This sets the last access time for the  object if any
	 */
	public void setAccessed(String id) {
		T obj=get(id,null);
		if (obj!=null){
			obj.setAccessed();
		}
	}

	public void clear() {
		map.clear(); 
	}

	/**
	 * @param id
	 * @return If not object in the map this retuns null.
	 */
	public T remove(String id) {
		if (exists(id)){
			return map.remove(id);
		} else {
			return null;
		}
	}

	public long size(){
		return map.size();
	}

	public static interface Interator<T> {
		public boolean iterate(T element);
	}

	public boolean iterate(Interator<T> iterator) {
		Set<Map.Entry<String, T>> set=map.entrySet();

		for (Iterator<Map.Entry<String, T>> i=set.iterator();i.hasNext();){
			Map.Entry<String, T> entry=i.next();
			
			if (!iterator.iterate(entry.getValue())){
				log.info("cache iteration stopped by client");
				return false;
			}
		}
		
		return true;
	}


	/**
	 * This purge all the object expired or objects that can be purged
	 */
	public void purge() {
		long startTime=System.currentTimeMillis();
		long objectPurged=0;
		
		Set<Map.Entry<String, T>> set=map.entrySet();
		
		long time=System.currentTimeMillis()-minUnactiveTime;
		
		for (Iterator<Map.Entry<String, T>> i=set.iterator();i.hasNext();){
			Map.Entry<String, T> entry=i.next();
			T object=entry.getValue();
			
			// Check if the object has to be removed from the cache
			if (object.getLastAccess()<time && object.canPurge() ){
				i.remove();
				objectPurged++;
			}
		}
		
		if (log.isInfoEnabled()){
			if (objectPurged==0){
				if (log.isDebugEnabled()){
					log.info("Cache:"+name+
							 " Purged:"+objectPurged+
							 " Time:"+(System.currentTimeMillis()-startTime));
				}
			} else {
				log.info("Cache:"+name+
						 " Purged:"+objectPurged+
						 " Time:"+(System.currentTimeMillis()-startTime));
			}
		}
		
	}
	
	public Collection<T> getAll(){
		return map.values();
	}

	public class ExpContainerInfo {
		public final long size=size();
		public final long minUnactiveTime=getMinUnactiveTime();
	}

	public ExpContainerInfo getInfo(){
		return new ExpContainerInfo();
	}

}
