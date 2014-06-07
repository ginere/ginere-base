package ginere.base.util.dao.cache.impl;

import ginere.base.util.dao.KeyDTO;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;


public class AbstractKeyCacheManagerTest extends TestCase{

	static final Logger log = Logger.getLogger(AbstractKeyCacheManagerTest.class);

	@Test
	static public void testConsulta() throws Exception {
		try {

			KeyCacheManagerTest.MANAGER.setMaxAged(100);
			KeyDTO obj=KeyCacheManagerTest.MANAGER.getForAccess("pepe");
			assertNotNull(obj);
			
			for (int i=0;i<100;i++){
				KeyCacheManagerTest.MANAGER.getForAccess("pepe:"+i);
				Thread.sleep(10);
				log.error("i:"+i);
			}
			
			

		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
	}
	
}
