package ginere.base.util.dao.cache.impl;

import ginere.base.util.dao.cache.KeyCacheObject;

/**
 * @author ventura
 *
 * Use this interface to make action on cache events
 */
public interface KeyCacheManagerWatcher <T extends KeyCacheObject>{

	/**
	 * This method is called when the object has been revised and it has been deficed to remain into the cache
	 * @param object
	 */
	void revised(T object);

	/**
	 * This is called when the object is purged from cache
	 * 
	 * @param object
	 */
	void purged(T object);

}
