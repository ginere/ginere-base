package ginere.base.util.dao.cache.impl;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.dao.cache.impl.AbstractKeyCacheManager;

import org.apache.log4j.Logger;

/**
 * @author ventura
 *
 * Clase abstracta para todos los manager que gestionan objetos basados en un ID
 */
public class KeyCacheManagerTest extends AbstractKeyCacheManager<KeyCacheObjectTest>  {
	protected static final Logger log = Logger.getLogger(KeyCacheManagerTest.class);
	
	
	public static final KeyCacheManagerTest MANAGER=new KeyCacheManagerTest();
	
	private KeyCacheManagerTest(){
		super();
	}

	@Override
	protected KeyCacheObjectTest getInner(String id) throws DaoManagerException {
		return new KeyCacheObjectTest(id);
	}

	@Override
	protected void removeInner(String id) throws DaoManagerException {
		
	}

//	@Override
//	protected boolean existsInnner(String id) throws DaoManagerException {
//		return true;
//	}

	@Override
	public long getBackendElementNumber() throws DaoManagerException {
		return 0;
	}

	@Override
	protected String insertInner(String id, KeyCacheObjectTest obj)
			throws DaoManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String insertInner(KeyCacheObjectTest obj) throws DaoManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updateInner(KeyCacheObjectTest obj)
			throws DaoManagerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeAllInner() throws DaoManagerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected KeyCacheObjectTest getInner(String id,
			KeyCacheObjectTest defaultValue) throws DaoManagerException {
		// TODO Auto-generated method stub
		return null;
	}


}
