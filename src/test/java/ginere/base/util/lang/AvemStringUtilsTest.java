package ginere.base.util.lang;

import eu.ginere.base.util.lang.AvemStringUtils;
import junit.framework.TestCase;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;


public class AvemStringUtilsTest extends TestCase{

	private static final String FILE_PROPERTY_PATH = "src/test/resources/test.properties";

	static final Logger log = Logger.getLogger(AvemStringUtilsTest.class);

	@Test
	static public void testConsulta() throws Exception {
		try {
			
			long timeInMillis=10;
			
			String ret=AvemStringUtils.fromLapInMillis(timeInMillis);
			
			log.error(ret+":"+timeInMillis);
			assertEquals("010ml", ret);
			
			
			timeInMillis=1234;
			ret=AvemStringUtils.fromLapInMillis(timeInMillis);
			log.error(ret+":"+timeInMillis);
			assertEquals("01sec 234ml", ret);

			timeInMillis=61001;
			ret=AvemStringUtils.fromLapInMillis(timeInMillis);
			log.error(ret+":"+timeInMillis);
			assertEquals("01min 01sec 001ml", ret);

			timeInMillis=3601001;
			ret=AvemStringUtils.fromLapInMillis(timeInMillis);
			log.error(ret+":"+timeInMillis);
			assertEquals("01h 01sec 001ml", ret);

			timeInMillis=(1000*60*60*24*2);
			ret=AvemStringUtils.fromLapInMillis(timeInMillis);
			log.error(ret+":"+timeInMillis);
			assertEquals("02d", ret);
			
			
			timeInMillis=(1000*60*60*24*1)+(1000*60*60*1)+(1000*60*1)+(1000*1)+(1);
			ret=AvemStringUtils.fromLapInMillis(timeInMillis);
			log.error(ret+":"+timeInMillis);
			assertEquals("01d 01h 01min 01sec 001ml", ret);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
	}

	@Test
	static public void testToLower() throws Exception {
		try {
			
			String id;

			long time=System.currentTimeMillis();
			long COUNT=10000;
			
			for (long i=0;i<COUNT;i++){
				String cookie=RandomStringUtils.randomAlphanumeric(64);
				String lower=cookie.toLowerCase();
				
			}
			long laps=System.currentTimeMillis()-time;

			log.error("Time:"+((double)laps/COUNT));


		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
	}
	
}
