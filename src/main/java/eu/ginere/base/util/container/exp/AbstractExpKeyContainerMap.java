package eu.ginere.base.util.container.exp;

import org.apache.log4j.Logger;

/**
 * @author ventura
 * 
 * This abstract class shoul be implemented by all the ExpContainer.
 * This allows the ExpContainer to be subscrived this the cleaner and aloow the cleaner.
 * 
 * This alos implement the basic funcionality
 * 
 */
public abstract class AbstractExpKeyContainerMap<T extends ExpContainerKeyObject> extends AbstractExpContainerMap<T>  {
	protected static final Logger log = Logger.getLogger(AbstractExpKeyContainerMap.class);
	
	protected AbstractExpKeyContainerMap(long objectMaxUnactiveTime){
		super(objectMaxUnactiveTime);
	}

	protected AbstractExpKeyContainerMap(long objectMaxUnactiveTime,String name){
		super(objectMaxUnactiveTime,name);
	}
	
	public T put(T obj){
		return super.put(obj.getId(), obj);
	}

	public boolean containsValue(T obj){
		return super.containsKey(obj.getId());
	}
}
