package ginere.base.util.dao.cache.impl;

import ginere.base.util.dao.DaoManagerException;
import ginere.base.util.dao.cache.KeyCacheObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author ventura
 * 
 *         Esta clase sirve de madre para todas las clases que guardan una cache
 *         para solo lectura. No se efectua ninguna cambio a traves de esta
 *         cache. Esta cache es reflejo de una estructura remota por lo que el
 *         tiempo de vida de los objetos tiene que ser corto para que no se
 *         vuelvan obsoletos. Alomejor habri a que implementar un mecanismo de
 *         actualizacion automatica asinchrona por debajo.
 * 
 *         La propiedad principal es que los objetos al otro lado de la cache
 *         han podido ser actualizados por lo que hay que el tiempo de
 *         almacenamiento es pequenyo o hay que implementar un mecanismo de
 *         actualizacion asynchrona.
 * 
 */
public abstract class AbstractTemporalKeyCacheManager<T extends KeyCacheObject> extends AbstractKeyCacheManager<T>{
	
	public T get(String id) throws DaoManagerException{
		//		return super.getForAccess(id);
		// Asi se evita llamar al metodo access no se actualiza la fecha del ultimo acceso 
		// y esta deviene la fecha de creacion
		return super.getForAccess(id);
	}
	
	public long getBackendElementNumber() throws DaoManagerException{
		return 0;
	}

//	public boolean exists(String id) throws DaoManagerException{
//		throw new DaoManagerException("Why this method is called?");
//	}
//
//	protected boolean existsInnner(String id)throws DaoManagerException{
////		throw new DaoManagerException("Why this method is called?");
//		return false;
//	}

	@Override
	protected String insertInner(String id, T obj) throws DaoManagerException{
		throw new DaoManagerException("Why this method is called?");
	}

	protected  String insertInner(T obj) throws DaoManagerException{
		throw new DaoManagerException("Why this method is called?");
	}

	public void update(T object) {
		super.update(object);
	}

	protected void updateInner(T obj) throws DaoManagerException{
		// throw new DaoManagerException("Why this method is called?");
	}

	public void remove(String id) throws DaoManagerException{
		throw new DaoManagerException("Why this method is called?");
	}
	
	public void removeAllInner() throws DaoManagerException{
		throw new DaoManagerException("Why this method is called?");
	}
	
	protected void removeInner(String id) throws DaoManagerException{
		throw new DaoManagerException("Why this method is called?");
	}


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

//			Los objetos no necesitan ser actualizados
//			// Check if the object has to be saved
//			if (object.isNeedToBeUpdated()){
//				update(object);
//				objectUpdated++;
//			}
			
			// Check if the object has to be removed from the cache
			if (object.lastAccess()<time){
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
					log.info("Cache:"+getName()+" Purged:"+objectPurged+" Updated:"+objectUpdated+" Time:"+(System.currentTimeMillis()-startTime));
				}
			} else {
				log.info("Cache:"+getName()+" Purged:"+objectPurged+" Updated:"+objectUpdated+" Time:"+(System.currentTimeMillis()-startTime));
			}
		}
		
	}


	@Override
	protected void finalize() throws Throwable{
		// there is nothing to save into this cache
		
//		log.info("Saving all Objects:"+getClass().getName()+" ...");
//
//		long startTime=System.currentTimeMillis();
//		long objectPurged=0;
//		
//		Set<Map.Entry<String, T>> set=map.entrySet();
//		
//		
//		for (Iterator<Map.Entry<String, T>> i=set.iterator();i.hasNext();){
//			Map.Entry<String, T> entry=i.next();
//			T object=entry.getValue();
//
//			// Check if the object has to be saved
//			if (object.isNeedToBeUpdated()){
//				update(object);
//			}
//		}
//		
//		log.info("Saving Done:"+getClass().getName()+".");
//		super.finalize();
	}


}