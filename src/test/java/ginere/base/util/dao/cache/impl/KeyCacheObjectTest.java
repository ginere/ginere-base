package ginere.base.util.dao.cache.impl;

import eu.ginere.base.util.dao.cache.KeyCacheObject;
import eu.ginere.base.util.dao.cache.impl.AbstractKeyCacheObject;


/**
 * @author ventura
 *
 * This is for objects that can be identified by one uniq ID
 */
public class KeyCacheObjectTest extends AbstractKeyCacheObject implements KeyCacheObject {

	protected KeyCacheObjectTest(String id){
		super(id,System.currentTimeMillis());
	}
}
