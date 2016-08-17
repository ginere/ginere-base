package eu.ginere.base.util.container.exp.impl;

import eu.ginere.base.util.container.exp.ExpContainerObject;


/**
 * @author ventura
 *
 * This are the objects that can be stored in to a AbstractExpContainer
 * @see AbstractExpContainer
 */
public class AbstractExpContainerObject implements ExpContainerObject{
	
	protected long accessed=System.currentTimeMillis();
	
	/**
	 * That update the last acess to now
	 */
	public void setAccessed(){
		accessed=System.currentTimeMillis();
	}
	
	/**
	 * @return Retun the last acces to the object
	 */
	public long getLastAccess(){
		return accessed;
	}
	
	/**
	 * A free implementation to block an object to be purged
	 */
	public boolean canPurge(){
		return true;
	}
}
