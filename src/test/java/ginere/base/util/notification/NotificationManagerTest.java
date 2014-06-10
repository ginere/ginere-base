package ginere.base.util.notification;

import eu.ginere.base.util.manager.AbstractManager;
import eu.ginere.base.util.notification.Notify;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;


public class NotificationManagerTest extends TestCase{

	static final Logger log = Logger.getLogger(NotificationManagerTest.class);
	private static void setDataSource() throws Exception {
//		DataSource dataSource = JdbcManager.createMySQLDataSourceFromPropertiesFile("/etc/cgps/jdbc.properties");
//		
//		JdbcManager.setDataSource(dataSource);
		
		
//		GlobalFileProperties.setInitFilePath("src/test/resources/map-web.properties");
		AbstractManager.setPropertiespath("/etc/cgps");
	}
	

	@Test
	static public void testConsulta() throws Exception {
		try {

			setDataSource();

			Notify.debug(log,"Hello Kytty",new NullPointerException("pepe"));
			Thread.sleep(10000);
			
			Notify.debug(log,"Hello Kytty",new NullPointerException("pepe"));
			Thread.sleep(10000);
			
			Notify.debug(log,"Hello Kytty",new NullPointerException("pepe"));
			Thread.sleep(10000);
			
			
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
	}

}