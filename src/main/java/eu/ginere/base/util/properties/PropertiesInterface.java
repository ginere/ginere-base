package eu.ginere.base.util.properties;

import java.util.Map;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.test.TestInterface;

public interface PropertiesInterface extends TestInterface{

//	/**
//	 * This set a value in the backend. The default vslue flag will be marked false.
//	 *
//	 * @param propertyName
//	 * @param description
//	 * @param value
//	 */
//	public void setValue(String propertyName, String description,String value);
//	
	/**
	 * @return
	 *
	 * The las time a property in the backend has been modified Used to reload all the properties.
	 */
	public long getLastModified();

//	/**
//	 * @return
//	 * Return all the properties to be cached.
//	 */
//	public Map<String, String> getAll();
	
	/**
	 * @return Get all de properties descriptors.
	 */
	public Map<String,PropertyDescriptor> getAll() throws DaoManagerException ;

}
