package eu.ginere.base.util.dao.cache;

import eu.ginere.base.util.dao.KeyDTO;
import eu.ginere.base.util.dao.LastUpdateDTO;

/**
 * @author ventura
 *
 * This is for objects that can be identified by one uniq ID
 */
public interface KeyCacheObject extends KeyDTO,LastUpdateDTO {
	
	public void setNeedToUpdate();

	public void setUpdated();
	
	public boolean isNeedToBeUpdated();
	
//	public long lastUpdate();

	public void access();
	
	public long lastAccess();
	
	/**
	 * use this method to return false to avoid one object to be purged.
	 * Vor example to avoid to purge one Commet Boc when there are chils listening on it 
	 */
	public boolean canPurge();
}
