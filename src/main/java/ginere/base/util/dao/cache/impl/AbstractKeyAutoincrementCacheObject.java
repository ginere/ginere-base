package ginere.base.util.dao.cache.impl;

import ginere.base.util.dao.KeyAutoincrementDTO;
import ginere.base.util.dao.cache.KeyCacheObject;
import ginere.base.util.descriptor.annotation.Description;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @author ventura
 *
 * Clase madre para los objetos que pueden almacenarse en una cache y que utilizan un id autoincrementeal
 * La principal caracteristica es que existe un setId que permite actuaizar el id del objeto
 * una vez que se obtiene el valor despues de almacenarlo en base de datos.
 */
public abstract class AbstractKeyAutoincrementCacheObject implements KeyAutoincrementDTO,KeyCacheObject,Serializable {


	@Description
	protected String id;
	protected long update;
	protected transient long access=System.currentTimeMillis(); // when one object is transmited this accesed to now
	protected transient boolean needToUpdate=false; // when one object is transmited this nas no sense to be true

	protected AbstractKeyAutoincrementCacheObject(String id,long lastUpdated){
		this.id=id;
		this.update=lastUpdated;
		this.access=this.update;
		this.needToUpdate=false;
	}
	
	public void setNeedToUpdate(){
//		update=System.currentTimeMillis();
//		access=update;
		needToUpdate=true;
	}

	public void setUpdated(){
		update=System.currentTimeMillis();
		access=update;
		needToUpdate=false;
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
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id=id;
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
