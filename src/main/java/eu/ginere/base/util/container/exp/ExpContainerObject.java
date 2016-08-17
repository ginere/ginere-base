package eu.ginere.base.util.container.exp;


/**
 * @author ventura
 *
 * This are the objects that can be stored in to a AbstractExpContainer
 * @see AbstractExpContainerMap
 */
public interface ExpContainerObject {
	
	/**
	 * That update the last acess to now
	 */
	public void setAccessed();
	
	/**
	 * @return Retun the last acces to the object
	 */
	public long getLastAccess();
	
	/**
	 * A free implementation to block an object to be purged
	 */
	public boolean canPurge();
}
