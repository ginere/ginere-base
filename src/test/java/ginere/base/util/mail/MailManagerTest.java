package ginere.base.util.mail;

import eu.ginere.base.util.mail.MailManager;
import eu.ginere.base.util.manager.AbstractManager;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;


public class MailManagerTest extends TestCase{

	static final Logger log = Logger.getLogger(MailManagerTest.class);

	
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
			
			MailManager.sendEmail("pepe@spoonapps.com","ventura@spoonapps.com", "Body.", "Subject");

		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
	}

}