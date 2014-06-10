package eu.ginere.base.util.dao.cache.impl;

import eu.ginere.base.util.dao.LastUpdateDTO;
import eu.ginere.base.util.dao.cache.KeyCacheObject;
import eu.ginere.base.util.dao.impl.AbstractKeyDTO;

import java.io.Serializable;


/**
 * @author ventura
 *
 * This is for objects that can be identified by one uniq ID.
 * Classe para los objetos que se almacenan en las caches.
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractKeyCacheObject extends AbstractKeyDTO implements KeyCacheObject,Serializable {

	protected long update;
	protected transient long access=System.currentTimeMillis(); // when one object is transmited this accesed to now
	protected transient boolean needToUpdate=false; // when one object is transmited this nas no sense to be true
	
	/**
	 * Forserializable purposes only 
	 */
	protected AbstractKeyCacheObject(String id,KeyCacheObject obj){
		super(id);
		this.update=obj.lastUpdate();
//		this.access=this.update;
//		this.needToUpdate=false;
	}
	
	protected AbstractKeyCacheObject(String id,long lastUpdated){
		super(id);
		this.update=lastUpdated;
//		this.access=this.update;
//		this.needToUpdate=false;
	}
	
	public void setNeedToUpdate(){
		update=System.currentTimeMillis();
		access=update;
		needToUpdate=true;
	}

	public void setUpdated(){
		update=System.currentTimeMillis();
		access=update;
		needToUpdate=false;
	}
	
	public void addUpdate(LastUpdateDTO obj){
		addUpdate(obj.lastUpdate());		
	}
	
	public void addUpdate(long newUpdate){
		
		if (newUpdate > update){
			update=newUpdate;
//			access=update;
//			needToUpdate=false;
		}
	}
	
	public boolean isNeedToBeUpdated(){
		return needToUpdate;
	}
	
	public long lastUpdate(){
		return update;
	}

	public void access(){
		access=System.currentTimeMillis();
	}
	
	public long lastAccess(){
		return access;
	}
	
	/**
	 * use this method to return false to avoid one object to be purged.
	 * Vor example to avoid to purge one Commet Boc when there are chils listening on it 
	 */
	@Override
	public boolean canPurge(){
		return true;
	}
}
