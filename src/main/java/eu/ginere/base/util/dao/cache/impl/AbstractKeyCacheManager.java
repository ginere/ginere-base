package eu.ginere.base.util.dao.cache.impl;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.dao.cache.KeyCacheManager;
import eu.ginere.base.util.dao.cache.KeyCacheObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * @author ventura
 * 
 *         Clase abstracta para todos los manager que gestionan objetos basados
 *         en un ID Manage the backend state and creation or update.
 * 
 *         Los objetos almacenados en esta cache tienen una duracion de vida
 *         determinada y un ultimo acceso. La purga se basa en este ultimo
 *         acceso, cuanto mas se utilicen, mas tiempo duran en la cache.
 * 
 *         Los objetos al otro lado de la cache no se modifican o se modifican
 *         solo a traves de esta interface
 * 
 */
public abstract class AbstractKeyCacheManager<T extends KeyCacheObject> {
	protected static final Logger log = Logger.getLogger(AbstractKeyCacheManager.class);
	
	protected final Map<String, T> map = new ConcurrentHashMap<String, T>();
	
	private final String name;
	protected long objectMaxUnactiveTime;

	private final KeyCacheManagerWatcher<T> watcher;

	protected AbstractKeyCacheManager(){
		this.objectMaxUnactiveTime=-1;
		this.name=getClass().getName();
		this.watcher=null;
		
		KeyCacheManager.MANAGER.subscrive(this);
	}

	protected AbstractKeyCacheManager(KeyCacheManagerWatcher<T> watcher){
		this.objectMaxUnactiveTime=-1;
		this.name=getClass().getName();
		this.watcher=watcher;
		
		KeyCacheManager.MANAGER.subscrive(this);
	}

	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public void setMaxAged(long objectMaxUnactiveTime) {
		this.objectMaxUnactiveTime=objectMaxUnactiveTime;
	}
	
	public long getMaxAged() {
		return this.objectMaxUnactiveTime;
	}

	/**
	 * Atecion si se utiliza este metodo directamente no se actualiza la fecha
	 * de ultimo aceso por lo que el objeto sera purgado mas facilmente.
	 * Utilizarlo para las caches que se tienen que actualizar cuando pase un
	 * tiempo desde la obtencion/creacion del objeto
	 * 
	 * @param id
	 * @return
	 * @throws DaoManagerException
	 */
	private T get(String id) throws DaoManagerException{
		T ret;

		if (map.containsKey(id)){
			ret=map.get(id);
		} else {
			ret=getInner(id);
			
			if (ret!=null){
				map.put(id,ret);
			}
		}
		
		return ret;
	}
	public T getForAccess(String id) throws DaoManagerException{
		T ret=get(id);
		
		if (ret!=null){
			ret.access();
		}
		return ret;
	}
	
	/**
	 * After work done we must call setNeedToUpdate if we want the thread to store the object
	 * 
	 * @param id
	 * @return
	 * @throws DaoManagerException
	 */
	public T getForUpdateMayBe(String id) throws DaoManagerException{
		T ret=get(id);
		
		if (ret!=null){
			ret.access();
		}
		return ret;
	}

	public void setNeedToUpdate(String id) throws DaoManagerException{
		get(id).setNeedToUpdate();
	}

	public String insert(String id,T obj) throws DaoManagerException{
		// normaly id == newId but in case
		String newId=insertInner(id,obj);
		map.put(newId,obj);
		
		return newId;
	}

	/**
	 * @param obj
	 * @throws DaoManagerException
	 */
	public String insert(T obj) throws DaoManagerException{
		String id=insertInner(obj);

		if (id!=null){
			map.put(id,obj);
		}
		
		return id;
	}
	
	/**
	 * This removes the object only from the cache.
	 * The original object stay into the parent DAO. This method do not call removeInner();
	 * @param id
	 * @throws DaoManagerException
	 */
	public void removeFromCache(String id) throws DaoManagerException{
		if (map.containsKey(id)){
			map.remove(id);
		} 
	}
	
	public void removeAll() throws DaoManagerException{
		removeAllInner();	
		
		map.clear(); 
	}
	
	public void remove(String id) throws DaoManagerException{
		removeInner(id);	
		
		if (map.containsKey(id)){
			map.remove(id);
		} 
	}

	public boolean containsKey(String id){
		return map.containsKey(id);
	}
	
	public boolean exists(String id) throws DaoManagerException{
		
		if (id == null){
			// en objetos auntoe increment que aun no se han insertado el valor del id puede ser nulo.
			return false;
		} else if (map.containsKey(id)){
			return true;
		} else {
			
			// Dos opciones:
			
// a) hcemos un metodos exists remoto			
//			boolean ret=existsInnner(id);
//			if (ret){
//				try {
//					// intentamos guardarlo en cache para previsibles futuros usos
//					map.put(id,getInner(id));
//				}catch (Exception e) {
//					if (log.isDebugEnabled()){
//						log.debug("While preloading obejct for key:'"+id+"'",e);
//					}
//				}
//				return true;
//			} else {
//				return false;
//			}
		
			
			// b) forzamos la descarga y si funciona ok.			
			try {
				// intentamos guardarlo en cache para previsibles futuros usos
				T obj=getInner(id,null);
				if (obj!=null){
					map.put(id,obj);
					return true;
				} else {
					return false;
				}
			}catch (Exception e) {
//				if (log.isDebugEnabled()){
//					log.debug("While preloading obejct for key:'"+id+"'",e);
//				}
				log.warn("While preloading obejct for key:'"+id+"' for cache:"+getClass().getName(),e);
				return false;
			}
			
		}
	}

	
	/**
	 * Return true if the object exists
	 * @param id
	 * @return
	 */
	// Esto no se utiliza por que se ha optado por la opcion b, se descarga y si 
	// funciona es que existe.
	//	protected abstract boolean existsInnner(String id)throws DaoManagerException;


	public long getCacheCount(String id){
		return map.size();
	}


	/**
	 * @param id
	 * 
	 * @return one object or DaoManagerExceicion if the object does not exists or some problem occurs
	 */
	protected abstract T getInner(String id) throws DaoManagerException;

	protected abstract T getInner(String id,T defaultValue) throws DaoManagerException;

	/**
	 * @param id
	 * 
	 * @return This is called to remove object from backend
	 */
	protected abstract void removeInner(String id) throws DaoManagerException;

	/**
	 * Removes all the data from backend
	 * 
	 * @throws DaoManagerException
	 */
	protected abstract void removeAllInner() throws DaoManagerException;

	public void purge(long defaultObjectMaxUnactiveTime) {
		long startTime=System.currentTimeMillis();
		long objectPurged=0;
		long objectUpdated=0;
		
		Set<Map.Entry<String, T>> set=map.entrySet();
		
		long time;
		if (objectMaxUnactiveTime<0){
			 time=System.currentTimeMillis()-defaultObjectMaxUnactiveTime;
		} else {
			 time=System.currentTimeMillis()-objectMaxUnactiveTime;
		}
		
		for (Iterator<Map.Entry<String, T>> i=set.iterator();i.hasNext();){
			Map.Entry<String, T> entry=i.next();
			T object=entry.getValue();

			// Check if the object has to be saved
			if (object.isNeedToBeUpdated()){
				update(object);
				objectUpdated++;
			}
			
			// Check if the object has to be removed from the cache
			if (object.canPurge() && object.lastAccess()<time){
				i.remove();
				purged(object);
				objectPurged++;
			} else {
				revised(object);
			}
		}
		
		if (log.isInfoEnabled()){
			if (objectPurged==0 && objectUpdated == 0){
				if (log.isDebugEnabled()){
					log.info("Cache:"+name+" Purged:"+objectPurged+" Updated:"+objectUpdated+" Time:"+(System.currentTimeMillis()-startTime));
				}
			} else {
				log.info("Cache:"+name+" Purged:"+objectPurged+" Updated:"+objectUpdated+" Time:"+(System.currentTimeMillis()-startTime));
			}
		}
		
	}

	@Override
	protected void finalize()throws Throwable{
		
		log.info("Saving all Objects:"+getClass().getName()+" ...");

//		long startTime=System.currentTimeMillis();
//		long objectPurged=0;
		
		Set<Map.Entry<String, T>> set=map.entrySet();
		
		
		for (Iterator<Map.Entry<String, T>> i=set.iterator();i.hasNext();){
			Map.Entry<String, T> entry=i.next();
			T object=entry.getValue();

			// Check if the object has to be saved
			if (object.isNeedToBeUpdated()){
				update(object);
			}
		}
		
		if (log.isInfoEnabled()){
			log.info("Saving Done:"+getClass().getName()+".");
		}
		super.finalize();
	}

	public void update(T object) {
		try {
			if (object!=null){
				updateInner(object);
				map.put(object.getId(),object);

				object.setUpdated();
			} 
		} catch (DaoManagerException e) {
			log.error("While storing object:"+object,e);
		}
		
	}

	/**
	 * This method is called when the object has been revised and it has been deficed to remain into the cache
	 * @param object
	 */
	protected void revised(T object) {
		if (watcher!=null){
			watcher.revised(object);
		}
	}


	/**
	 * This is called when the object is purged from cache
	 * 
	 * @param object
	 */
	protected void purged(T object) {
		if (watcher!=null){
			watcher.purged(object);
		}
	}	
	
	/**
	 * Returns the backend Element Number
	 * 
	 * @return
	 */
	abstract public long getBackendElementNumber() throws DaoManagerException;
	

	/**
	 * This method is called when obj.getId() is null
	 * @param id
	 * @param obj
	 */
	protected abstract String insertInner(String id, T obj) throws DaoManagerException;
	
	/**
	 * This method is called when obj.getId() is NOT null
	 * @param id
	 * @param obj
	 */
	protected abstract String insertInner(T obj) throws DaoManagerException;
	
	protected abstract void updateInner(T obj) throws DaoManagerException;
}
