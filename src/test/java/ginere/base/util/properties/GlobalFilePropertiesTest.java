package ginere.base.util.properties;

import java.util.Collection;

import eu.ginere.base.util.properties.GlobalFileProperties;
import eu.ginere.base.util.properties.PropertyDescriptor;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;


public class GlobalFilePropertiesTest extends TestCase{

	private static final String FILE_PROPERTY_PATH = "src/test/resources/test.properties";

	static final Logger log = Logger.getLogger(GlobalFilePropertiesTest.class);

	@Test
	static public void testConsulta() throws Exception {
		try {
			
			// cargando el fichero de propiedades
			log.info("Cargando el fichero de propiedades:'"+FILE_PROPERTY_PATH+"'");
			GlobalFileProperties.setInitFilePath(FILE_PROPERTY_PATH);

			Collection<PropertyDescriptor> list=GlobalFileProperties.getDescriptors();
			
			for (PropertyDescriptor desc:list){
				log.info("Dec:"+desc);
			}
			
			int value=GlobalFileProperties.getIntValue(GlobalFilePropertiesTest.class, "intTest","test property", -1);

			GlobalFileProperties.getDescriptors();
			
			TestCase.assertNotSame(value, -1);
			
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
	}
	
}
